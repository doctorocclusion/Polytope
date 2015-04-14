package net.eekysam.jpolytope;

public class LinkedGeoPolytope
{
	public final int dimension;
	public final LinkedPolytope<Integer> poly;
	
	public LinkedGeoPolytope(int dimension)
	{
		this(new LinkedPolytope<>(dimension));
	}
	
	public LinkedGeoPolytope(LinkedPolytope<Integer> poly)
	{
		this.poly = poly;
		this.dimension = poly.dimension;
	}
}
