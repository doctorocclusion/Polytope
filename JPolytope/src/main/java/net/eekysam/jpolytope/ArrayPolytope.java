package net.eekysam.jpolytope;

public class ArrayPolytope<T>
{
	public final int dimension;
	
	public ArrayPolytope(LinkedPolytope<T> polytope)
	{
		this.dimension = polytope.dimension;
	}
}
