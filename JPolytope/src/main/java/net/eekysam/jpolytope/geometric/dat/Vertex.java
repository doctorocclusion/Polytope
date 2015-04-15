package net.eekysam.jpolytope.geometric.dat;

import net.eekysam.jpolytope.geometric.Vector;

public class Vertex extends Vector implements IGeoDat
{
	public Vertex(double... location)
	{
		super(location);
	}
	
	@Override
	public Vertex clone()
	{
		return new Vertex(this.vector.clone());
	}
}
