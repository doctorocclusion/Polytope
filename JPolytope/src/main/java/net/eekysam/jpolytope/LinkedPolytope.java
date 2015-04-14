package net.eekysam.jpolytope;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class LinkedPolytope<T>
{
	private HashSet<LinkedPolytope<T>> children;
	private HashSet<LinkedPolytope<T>> parents;
	public final int dimension;
	
	public final UUID id;
	
	public T data;
	
	public LinkedPolytope(int dimension)
	{
		this(dimension, UUID.randomUUID());
	}
	
	public LinkedPolytope(int dimension, UUID id)
	{
		this.dimension = dimension;
		this.id = id;
		this.children = new HashSet<LinkedPolytope<T>>();
		this.parents = new HashSet<LinkedPolytope<T>>();
	}
	
	public Set<LinkedPolytope<T>> getElements(int dimension)
	{
		int curd = this.dimension;
		HashSet<LinkedPolytope<T>> cur = new HashSet<>();
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
	
	public Iterator<LinkedPolytope<T>> getParents()
	{
		return this.parents.iterator();
	}
	
	public Iterator<LinkedPolytope<T>> getChildren()
	{
		return this.children.iterator();
	}
	
	public void addParent(LinkedPolytope<T> parent)
	{
		if (parent.dimension != this.dimension + 1)
		{
			throw new IllegalArgumentException(String.format("A parent element of this element should be %d (%d + 1) not %d dimensional.", this.dimension + 1, this.dimension, parent.dimension));
		}
		this.parents.add(parent);
		parent.children.add(this);
	}
	
	public void addChild(LinkedPolytope<T> child)
	{
		if (child.dimension != this.dimension - 1)
		{
			throw new IllegalArgumentException(String.format("A child element of this element should be %d (%d - 1) not %d dimensional.", this.dimension - 1, this.dimension, child.dimension));
		}
		this.children.add(child);
		child.parents.add(this);
	}
	
	public void removeParent(LinkedPolytope<T> parent)
	{
		if (parent.dimension != this.dimension + 1)
		{
			throw new IllegalArgumentException(String.format("A parent element of this element should be %d (%d + 1) not %d dimensional.", this.dimension + 1, this.dimension, parent.dimension));
		}
		this.parents.remove(parent);
		parent.children.remove(this);
	}
	
	public void removeChild(LinkedPolytope<T> child)
	{
		if (child.dimension != this.dimension - 1)
		{
			throw new IllegalArgumentException(String.format("A child element of this element should be %d (%d - 1) not %d dimensional.", this.dimension - 1, this.dimension, child.dimension));
		}
		this.children.remove(child);
		child.parents.remove(this);
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
		if (o instanceof LinkedPolytope)
		{
			LinkedPolytope<?> elm = (LinkedPolytope<?>) o;
			return elm.dimension == this.dimension && this.id.equals(elm.id);
		}
		return false;
	}
	
	public static <T> HashSet<LinkedPolytope<T>> allParents(Collection<LinkedPolytope<T>> polys)
	{
		HashSet<LinkedPolytope<T>> outs = new HashSet<>();
		for (LinkedPolytope<T> poly : polys)
		{
			Iterator<LinkedPolytope<T>> iter = poly.getParents();
			while (iter.hasNext())
			{
				outs.add(iter.next());
			}
		}
		return outs;
	}
	
	public static <T> HashSet<LinkedPolytope<T>> allChildren(Collection<LinkedPolytope<T>> polys)
	{
		HashSet<LinkedPolytope<T>> outs = new HashSet<>();
		for (LinkedPolytope<T> poly : polys)
		{
			Iterator<LinkedPolytope<T>> iter = poly.getChildren();
			while (iter.hasNext())
			{
				outs.add(iter.next());
			}
		}
		return outs;
	}
}
