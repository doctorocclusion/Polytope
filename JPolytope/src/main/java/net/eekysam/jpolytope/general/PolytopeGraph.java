package net.eekysam.jpolytope.general;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class PolytopeGraph<T>
{
	private HashSet<PolytopeGraph<T>> children;
	private HashSet<PolytopeGraph<T>> parents;
	public final int dimension;
	
	public final UUID id;
	
	public T data;
	
	public PolytopeGraph(int dimension)
	{
		this(dimension, UUID.randomUUID());
	}
	
	public PolytopeGraph(int dimension, UUID id)
	{
		this.dimension = dimension;
		this.id = id;
		this.children = new HashSet<PolytopeGraph<T>>();
		this.parents = new HashSet<PolytopeGraph<T>>();
	}
	
	public Set<PolytopeGraph<T>> getElements(int dimension)
	{
		int curd = this.dimension;
		HashSet<PolytopeGraph<T>> cur = new HashSet<>();
		cur.add(this);
		while (curd != dimension)
		{
			if (curd < dimension)
			{
				cur = allParents(cur);
				curd++;
			}
			else
			{
				cur = allChildren(cur);
				curd--;
			}
		}
		return cur;
	}
	
	public HashMap<Integer, Set<PolytopeGraph<T>>> getElements()
	{
		HashMap<Integer, Set<PolytopeGraph<T>>> all = new HashMap<>();
		
		int curd = this.dimension;
		HashSet<PolytopeGraph<T>> cur = new HashSet<>();
		cur.add(this);
		
		all.put(curd, cur);
		
		while (true)
		{
			cur = allParents(cur);
			curd++;
			if (cur.isEmpty())
			{
				break;
			}
			else
			{
				all.put(curd, cur);
			}
		}
		
		curd = this.dimension;
		cur = new HashSet<>();
		cur.add(this);
		
		while (true)
		{
			cur = allChildren(cur);
			curd--;
			if (cur.isEmpty())
			{
				break;
			}
			else
			{
				all.put(curd, cur);
			}
		}
		
		return all;
	}
	
	public Iterator<PolytopeGraph<T>> getParents()
	{
		return this.parents.iterator();
	}
	
	public Iterator<PolytopeGraph<T>> getChildren()
	{
		return this.children.iterator();
	}
	
	public void addParent(PolytopeGraph<T> parent)
	{
		if (parent.dimension != this.dimension + 1)
		{
			throw new IllegalArgumentException(String.format("A parent element of this element should be %d (%d + 1) not %d dimensional.", this.dimension + 1, this.dimension, parent.dimension));
		}
		this.parents.add(parent);
		parent.children.add(this);
	}
	
	public void addChild(PolytopeGraph<T> child)
	{
		if (child.dimension != this.dimension - 1)
		{
			throw new IllegalArgumentException(String.format("A child element of this element should be %d (%d - 1) not %d dimensional.", this.dimension - 1, this.dimension, child.dimension));
		}
		this.children.add(child);
		child.parents.add(this);
	}
	
	public void removeParent(PolytopeGraph<T> parent)
	{
		if (parent.dimension != this.dimension + 1)
		{
			throw new IllegalArgumentException(String.format("A parent element of this element should be %d (%d + 1) not %d dimensional.", this.dimension + 1, this.dimension, parent.dimension));
		}
		this.parents.remove(parent);
		parent.children.remove(this);
	}
	
	public void removeChild(PolytopeGraph<T> child)
	{
		if (child.dimension != this.dimension - 1)
		{
			throw new IllegalArgumentException(String.format("A child element of this element should be %d (%d - 1) not %d dimensional.", this.dimension - 1, this.dimension, child.dimension));
		}
		this.children.remove(child);
		child.parents.remove(this);
	}
	
	@Override
	public PolytopeGraph<T> clone()
	{
		PolytopeGraph<T> clone = new PolytopeGraph<>(this.dimension);
		clone.data = this.data;
		for (PolytopeGraph<T> elem : this.children)
		{
			clone.children.add(elem.clone());
		}
		for (PolytopeGraph<T> elem : this.parents)
		{
			clone.parents.add(elem.clone());
		}
		return clone;
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
			return elm.dimension == this.dimension && this.id.equals(elm.id);
		}
		return false;
	}
	
	public boolean fullEquals(Object o)
	{
		throw new UnsupportedOperationException();
	}
	
	public static <T> HashSet<PolytopeGraph<T>> allParents(Collection<PolytopeGraph<T>> polys)
	{
		HashSet<PolytopeGraph<T>> outs = new HashSet<>();
		for (PolytopeGraph<T> poly : polys)
		{
			Iterator<PolytopeGraph<T>> iter = poly.getParents();
			while (iter.hasNext())
			{
				outs.add(iter.next());
			}
		}
		return outs;
	}
	
	public static <T> HashSet<PolytopeGraph<T>> allChildren(Collection<PolytopeGraph<T>> polys)
	{
		HashSet<PolytopeGraph<T>> outs = new HashSet<>();
		for (PolytopeGraph<T> poly : polys)
		{
			Iterator<PolytopeGraph<T>> iter = poly.getChildren();
			while (iter.hasNext())
			{
				outs.add(iter.next());
			}
		}
		return outs;
	}
}
