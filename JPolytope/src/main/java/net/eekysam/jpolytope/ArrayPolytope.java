package net.eekysam.jpolytope;

public class ArrayPolytope<T>
{
	public final int dimention;
	
	public ArrayPolytope(LinkedPolytope<T> polytope)
	{
		this.dimention = polytope.dimention;
	}
}
