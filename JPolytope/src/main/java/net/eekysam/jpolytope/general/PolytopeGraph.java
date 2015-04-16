package net.eekysam.jpolytope.general;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class PolytopeGraph<T>
{
	public static class GraphLayer<T>
	{
		public final int layer;
		public final Set<PolytopeGraph<T>> members;
		
		GraphLayer(int layer, Set<PolytopeGraph<T>> members)
		{
			this.layer = layer;
			this.members = Collections.unmodifiableSet(members);
		}
	}
	
	private HashSet<PolytopeGraph<T>> children;
	private HashSet<PolytopeGraph<T>> parents;
	public final int layer;
	
	public final UUID id;
	
	public T data;
	
	public PolytopeGraph(int dimension)
	{
		this(dimension, UUID.randomUUID());
	}
	
	public PolytopeGraph(int layer, UUID id)
	{
		this.layer = layer;
		this.id = id;
		this.children = new HashSet<PolytopeGraph<T>>();
		this.parents = new HashSet<PolytopeGraph<T>>();
	}
	
	public Set<PolytopeGraph<T>> getElements(int layer)
	{
		class GetSetConsumer implements Consumer<GraphLayer<T>>
		{
			public Set<PolytopeGraph<T>> out = Collections.unmodifiableSet(new HashSet<>());
			
			@Override
			public void accept(GraphLayer<T> t)
			{
				this.out = t.members;
			}
		}
		
		GetSetConsumer action = new GetSetConsumer();
		this.forLayers(layer, layer, action);
		return action.out;
	}
	
	private void forAboveLayers(int min, int max, Consumer<GraphLayer<T>> action)
	{
		HashSet<PolytopeGraph<T>> cur = new HashSet<>();
		int curlayer = this.layer;
		cur.add(this);
		
		while (true)
		{
			curlayer++;
			if (curlayer > max)
			{
				return;
			}
			cur = allParents(cur);
			if (cur.isEmpty())
			{
				return;
			}
			if (curlayer >= min)
			{
				action.accept(new GraphLayer<T>(curlayer, cur));
			}
		}
	}
	
	private void forBelowLayers(int min, int max, Consumer<GraphLayer<T>> action)
	{
		HashSet<PolytopeGraph<T>> cur = new HashSet<>();
		int curlayer = this.layer;
		cur.add(this);
		
		while (true)
		{
			curlayer--;
			if (curlayer < min)
			{
				return;
			}
			cur = allChildren(cur);
			if (cur.isEmpty())
			{
				return;
			}
			if (curlayer <= max)
			{
				action.accept(new GraphLayer<T>(curlayer, cur));
			}
		}
	}
	
	public void forLayers(int min, int max, Consumer<GraphLayer<T>> action)
	{
		if (this.layer <= max && this.layer >= min)
		{
			GraphLayer<T> single = new GraphLayer<T>(this.layer, Collections.singleton(this));
			action.accept(single);
		}
		if (this.layer > min)
		{
			this.forBelowLayers(min, max, action);
		}
		if (this.layer < max)
		{
			this.forAboveLayers(min, max, action);
		}
	}
	
	public void forEach(int min, int max, Consumer<PolytopeGraph<T>> action)
	{
		this.forLayers(min, max, new Consumer<GraphLayer<T>>()
		{
			@Override
			public void accept(GraphLayer<T> t)
			{
				t.members.forEach(action);
			}
		});
	}
	
	public HashMap<Integer, Set<PolytopeGraph<T>>> getLayers(int min, int max)
	{
		HashMap<Integer, Set<PolytopeGraph<T>>> map = new HashMap<>();
		this.forLayers(min, max, new Consumer<GraphLayer<T>>()
		{
			@Override
			public void accept(GraphLayer<T> t)
			{
				map.put(t.layer, t.members);
			}
		});
		return map;
	}
	
	public HashMap<Integer, Set<PolytopeGraph<T>>> getLayers()
	{
		return this.getLayers(Integer.MIN_VALUE, Integer.MAX_VALUE);
	}
	
	public Set<PolytopeGraph<T>> getParents()
	{
		return Collections.unmodifiableSet(this.parents);
	}
	
	public Set<PolytopeGraph<T>> getChildren()
	{
		return Collections.unmodifiableSet(this.children);
	}
	
	public int parentCount()
	{
		return this.parents.size();
	}
	
	public int childCount()
	{
		return this.children.size();
	}
	
	public void addParent(PolytopeGraph<T> parent)
	{
		if (parent.layer != this.layer + 1)
		{
			throw new IllegalArgumentException(String.format("A parent element of this element should be %d (%d + 1) not %d dimensional.", this.layer + 1, this.layer, parent.layer));
		}
		this.parents.add(parent);
		parent.children.add(this);
	}
	
	public void addChild(PolytopeGraph<T> child)
	{
		if (child.layer != this.layer - 1)
		{
			throw new IllegalArgumentException(String.format("A child element of this element should be %d (%d - 1) not %d dimensional.", this.layer - 1, this.layer, child.layer));
		}
		this.children.add(child);
		child.parents.add(this);
	}
	
	public void removeParent(PolytopeGraph<T> parent)
	{
		if (parent.layer != this.layer + 1)
		{
			throw new IllegalArgumentException(String.format("A parent element of this element should be %d (%d + 1) not %d dimensional.", this.layer + 1, this.layer, parent.layer));
		}
		this.parents.remove(parent);
		parent.children.remove(this);
	}
	
	public void removeChild(PolytopeGraph<T> child)
	{
		if (child.layer != this.layer - 1)
		{
			throw new IllegalArgumentException(String.format("A child element of this element should be %d (%d - 1) not %d dimensional.", this.layer - 1, this.layer, child.layer));
		}
		this.children.remove(child);
		child.parents.remove(this);
	}
	
	public Set<PolytopeGraph<T>> getTops()
	{
		HashSet<PolytopeGraph<T>> out = new HashSet<>();
		this.forEach(this.layer, Integer.MAX_VALUE, new Consumer<PolytopeGraph<T>>()
		{
			@Override
			public void accept(PolytopeGraph<T> t)
			{
				if (t.parents.isEmpty())
				{
					out.add(t);
				}
			}
		});
		return out;
	}
	
	public Set<PolytopeGraph<T>> getBots()
	{
		HashSet<PolytopeGraph<T>> out = new HashSet<>();
		this.forEach(Integer.MIN_VALUE, this.layer, new Consumer<PolytopeGraph<T>>()
		{
			@Override
			public void accept(PolytopeGraph<T> t)
			{
				if (t.children.isEmpty())
				{
					out.add(t);
				}
			}
		});
		return out;
	}
	
	@Override
	public PolytopeGraph<T> clone()
	{
		return this.getClone(new HashMap<>());
	}
	
	private PolytopeGraph<T> getClone(HashMap<UUID, PolytopeGraph<T>> make)
	{
		if (make.containsKey(this.id))
		{
			return make.get(this.id);
		}
		else
		{
			PolytopeGraph<T> clone = new PolytopeGraph<T>(this.layer);
			make.put(this.id, clone);
			for (PolytopeGraph<T> elem : this.children)
			{
				clone.children.add(elem.getClone(make));
			}
			for (PolytopeGraph<T> elem : this.parents)
			{
				clone.parents.add(elem.getClone(make));
			}
			clone.data = this.data;
			return clone;
		}
	}
	
	@Override
	public int hashCode()
	{
		return this.id.hashCode();
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o instanceof PolytopeGraph)
		{
			PolytopeGraph<?> elm = (PolytopeGraph<?>) o;
			return elm.layer == this.layer && this.id.equals(elm.id);
		}
		return false;
	}
	
	public static <T> HashSet<PolytopeGraph<T>> allParents(Collection<PolytopeGraph<T>> polys)
	{
		HashSet<PolytopeGraph<T>> outs = new HashSet<>();
		for (PolytopeGraph<T> poly : polys)
		{
			for (PolytopeGraph<T> par : poly.parents)
			{
				outs.add(par);
			}
		}
		return outs;
	}
	
	public static <T> HashSet<PolytopeGraph<T>> allChildren(Collection<PolytopeGraph<T>> polys)
	{
		HashSet<PolytopeGraph<T>> outs = new HashSet<>();
		for (PolytopeGraph<T> poly : polys)
		{
			for (PolytopeGraph<T> chi : poly.children)
			{
				outs.add(chi);
			}
		}
		return outs;
	}
}
