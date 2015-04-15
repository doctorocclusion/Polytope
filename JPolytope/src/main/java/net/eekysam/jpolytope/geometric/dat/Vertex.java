package net.eekysam.jpolytope.geometric.dat;

import net.eekysam.jpolytope.geometric.TransformMatrix;
import net.eekysam.jpolytope.geometric.Vector;

public class Vertex extends Vector implements IGeoDat, IPointDat
{
	public Vertex(double... location)
	{
		super(location);
	}
	
	public Vertex(Vector v)
	{
		super(v.vector);
	}
	
	@Override
	public Vertex clone()
	{
		return new Vertex(this.vector.clone());
	}
	
	@Override
	public Vector point()
	{
		return this;
	}
	
	@Override
	public void transform(TransformMatrix matrix)
	{
		matrix.transform(this);
	}
}
