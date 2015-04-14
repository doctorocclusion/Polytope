package net.eekysam.jpolytope;

import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;

public class LinkedPolytope<T>
{
	private HashSet<LinkedPolytope<T>> sub;
	private HashSet<LinkedPolytope<T>> sup;
	public final int dimention;
	
	public final UUID id;
	
	public T data;
	
	public LinkedPolytope(int dimention)
	{
		this(dimention, UUID.randomUUID());
	}
	
	public LinkedPolytope(int dimention, UUID id)
	{
		this.dimention = dimention;
		this.id = id;
		this.sub = new HashSet<LinkedPolytope<T>>();
		this.sup = new HashSet<LinkedPolytope<T>>();
	}
	
	public Iterator<LinkedPolytope<T>> getParents()
	{
		return this.sup.iterator();
	}
	
	public Iterator<LinkedPolytope<T>> getChildren()
	{
		return this.sup.iterator();
	}
	
	public void addParent(LinkedPolytope<T> parent)
	{
		if (parent.dimention != this.dimention + 1)
		{
			throw new IllegalArgumentException(String.format("A parent element of this element should be %d (%d + 1) not %d dimentional.", this.dimention + 1, this.dimention, parent.dimention));
		}
		this.sup.add(parent);
		parent.sub.add(this);
	}
	
	public void addChild(LinkedPolytope<T> child)
	{
		if (child.dimention != this.dimention - 1)
		{
			throw new IllegalArgumentException(String.format("A child element of this element should be %d (%d - 1) not %d dimentional.", this.dimention - 1, this.dimention, child.dimention));
		}
		this.sub.add(child);
		child.sup.add(this);
	}
	
	public void removeParent(LinkedPolytope<T> parent)
	{
		if (parent.dimention != this.dimention + 1)
		{
			throw new IllegalArgumentException(String.format("A parent element of this element should be %d (%d + 1) not %d dimentional.", this.dimention + 1, this.dimention, parent.dimention));
		}
		this.sup.remove(parent);
		parent.sub.remove(this);
	}
	
	public void removeChild(LinkedPolytope<T> child)
	{
		if (child.dimention != this.dimention - 1)
		{
			throw new IllegalArgumentException(String.format("A child element of this element should be %d (%d - 1) not %d dimentional.", this.dimention - 1, this.dimention, child.dimention));
		}
		this.sub.remove(child);
		child.sup.remove(this);
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
			return elm.dimention == this.dimention && this.id.equals(elm.id);
		}
		return false;
	}
}
