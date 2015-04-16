package net.eekysam.jpolytope.general;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class GradedPoset<T>
{
	class PosetCloner implements Consumer<RankLevel<T>>
	{
		public HashMap<UUID, GradedPoset<T>> cloned = new HashMap<>();
		private HashSet<Integer> layers = new HashSet<>();
		
		@Override
		public void accept(RankLevel<T> l)
		{
			boolean addChildren = this.layers.contains(l.rank - 1);
			boolean addParents = this.layers.contains(l.rank + 1);
			this.layers.add(l.rank);
			for (GradedPoset<T> orig : l.elements)
			{
				GradedPoset<T> clone = new GradedPoset<T>(orig.rank);
				this.cloned.put(orig.id, clone);
				clone.data = orig.data;
				if (addChildren)
				{
					for (GradedPoset<T> child : orig.children)
					{
						GradedPoset<T> cc = this.cloned.get(child.id);
						cc.parents.add(clone);
						clone.children.add(cc);
					}
				}
				if (addParents)
				{
					for (GradedPoset<T> parent : orig.parents)
					{
						GradedPoset<T> cp = this.cloned.get(parent.id);
						cp.children.add(clone);
						clone.parents.add(cp);
					}
				}
			}
		}
	}
	
	private HashSet<GradedPoset<T>> children;
	private HashSet<GradedPoset<T>> parents;
	public final int rank;
	
	public final UUID id;
	
	public T data;
	
	public GradedPoset(int rank)
	{
		this(rank, UUID.randomUUID());
	}
	
	public GradedPoset(int rank, UUID id)
	{
		this.rank = rank;
		this.id = id;
		this.children = new HashSet<GradedPoset<T>>();
		this.parents = new HashSet<GradedPoset<T>>();
	}
	
	public Set<GradedPoset<T>> getElements(int rank)
	{
		class GetSetConsumer implements Consumer<RankLevel<T>>
		{
			public Set<GradedPoset<T>> out = Collections.unmodifiableSet(new HashSet<>());
			
			@Override
			public void accept(RankLevel<T> t)
			{
				this.out = t.elements;
			}
		}
		
		GetSetConsumer action = new GetSetConsumer();
		this.forLayers(rank, rank, action);
		return action.out;
	}
	
	private void forAboveLayers(int min, int max, Consumer<RankLevel<T>> action)
	{
		HashSet<GradedPoset<T>> cur = new HashSet<>();
		int curlayer = this.rank;
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
				action.accept(new RankLevel<T>(curlayer, cur));
			}
		}
	}
	
	private void forBelowLayers(int min, int max, Consumer<RankLevel<T>> action)
	{
		HashSet<GradedPoset<T>> cur = new HashSet<>();
		int curlayer = this.rank;
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
				action.accept(new RankLevel<T>(curlayer, cur));
			}
		}
	}
	
	public void forLayers(int min, int max, Consumer<RankLevel<T>> action)
	{
		if (this.rank <= max && this.rank >= min)
		{
			RankLevel<T> single = new RankLevel<T>(this.rank, Collections.singleton(this));
			action.accept(single);
		}
		if (this.rank > min)
		{
			this.forBelowLayers(min, max, action);
		}
		if (this.rank < max)
		{
			this.forAboveLayers(min, max, action);
		}
	}
	
	public void forLayers(Consumer<RankLevel<T>> action)
	{
		this.forLayers(Integer.MIN_VALUE, Integer.MAX_VALUE, action);
	}
	
	public void forEach(int min, int max, Consumer<GradedPoset<T>> action)
	{
		this.forLayers(min, max, new Consumer<RankLevel<T>>()
		{
			@Override
			public void accept(RankLevel<T> t)
			{
				t.elements.forEach(action);
			}
		});
	}
	
	public void forEach(Consumer<GradedPoset<T>> action)
	{
		this.forEach(Integer.MIN_VALUE, Integer.MAX_VALUE, action);
	}
	
	public HashMap<Integer, Set<GradedPoset<T>>> getLayers(int min, int max)
	{
		HashMap<Integer, Set<GradedPoset<T>>> map = new HashMap<>();
		this.forLayers(min, max, new Consumer<RankLevel<T>>()
		{
			@Override
			public void accept(RankLevel<T> t)
			{
				map.put(t.rank, t.elements);
			}
		});
		return map;
	}
	
	public HashMap<Integer, Set<GradedPoset<T>>> getLayers()
	{
		return this.getLayers(Integer.MIN_VALUE, Integer.MAX_VALUE);
	}
	
	public Set<GradedPoset<T>> getParents()
	{
		return Collections.unmodifiableSet(this.parents);
	}
	
	public Set<GradedPoset<T>> getChildren()
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
	
	public void addParent(GradedPoset<T> parent)
	{
		if (parent.rank != this.rank + 1)
		{
			throw new IllegalArgumentException(String.format("A parent element of this element should be %d (%d + 1) not %d dimensional.", this.rank + 1, this.rank, parent.rank));
		}
		this.parents.add(parent);
		parent.children.add(this);
	}
	
	public void addChild(GradedPoset<T> child)
	{
		if (child.rank != this.rank - 1)
		{
			throw new IllegalArgumentException(String.format("A child element of this element should be %d (%d - 1) not %d dimensional.", this.rank - 1, this.rank, child.rank));
		}
		this.children.add(child);
		child.parents.add(this);
	}
	
	public void removeParent(GradedPoset<T> parent)
	{
		if (parent.rank != this.rank + 1)
		{
			throw new IllegalArgumentException(String.format("A parent element of this element should be %d (%d + 1) not %d dimensional.", this.rank + 1, this.rank, parent.rank));
		}
		this.parents.remove(parent);
		parent.children.remove(this);
	}
	
	public void removeChild(GradedPoset<T> child)
	{
		if (child.rank != this.rank - 1)
		{
			throw new IllegalArgumentException(String.format("A child element of this element should be %d (%d - 1) not %d dimensional.", this.rank - 1, this.rank, child.rank));
		}
		this.children.remove(child);
		child.parents.remove(this);
	}
	
	public Set<GradedPoset<T>> splitFromParents()
	{
		Set<GradedPoset<T>> old = new HashSet<>();
		old.addAll(this.parents);
		Iterator<GradedPoset<T>> parents = this.parents.iterator();
		while (parents.hasNext())
		{
			parents.next().children.remove(this);
			parents.remove();
		}
		return old;
	}
	
	public Set<GradedPoset<T>> splitFromChildren()
	{
		Set<GradedPoset<T>> old = new HashSet<>();
		old.addAll(this.children);
		Iterator<GradedPoset<T>> children = this.children.iterator();
		while (children.hasNext())
		{
			children.next().parents.remove(this);
			children.remove();
		}
		return old;
	}
	
	public void delete()
	{
		this.splitFromParents();
		this.splitFromChildren();
	}
	
	public Set<GradedPoset<T>> getTops()
	{
		HashSet<GradedPoset<T>> out = new HashSet<>();
		this.forEach(this.rank, Integer.MAX_VALUE, new Consumer<GradedPoset<T>>()
		{
			@Override
			public void accept(GradedPoset<T> t)
			{
				if (t.parents.isEmpty())
				{
					out.add(t);
				}
			}
		});
		return out;
	}
	
	public Set<GradedPoset<T>> getBots()
	{
		HashSet<GradedPoset<T>> out = new HashSet<>();
		this.forEach(Integer.MIN_VALUE, this.rank, new Consumer<GradedPoset<T>>()
		{
			@Override
			public void accept(GradedPoset<T> t)
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
	public GradedPoset<T> clone()
	{
		PosetCloner cloner = new PosetCloner();
		this.forLayers(cloner);
		return cloner.cloned.get(this.id);
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
		if (o instanceof GradedPoset)
		{
			GradedPoset<?> elm = (GradedPoset<?>) o;
			return elm.rank == this.rank && this.id.equals(elm.id);
		}
		return false;
	}
	
	public static <T> HashSet<GradedPoset<T>> allParents(Collection<GradedPoset<T>> polys)
	{
		HashSet<GradedPoset<T>> outs = new HashSet<>();
		for (GradedPoset<T> poly : polys)
		{
			for (GradedPoset<T> par : poly.parents)
			{
				outs.add(par);
			}
		}
		return outs;
	}
	
	public static <T> HashSet<GradedPoset<T>> allChildren(Collection<GradedPoset<T>> polys)
	{
		HashSet<GradedPoset<T>> outs = new HashSet<>();
		for (GradedPoset<T> poly : polys)
		{
			for (GradedPoset<T> chi : poly.children)
			{
				outs.add(chi);
			}
		}
		return outs;
	}
}
