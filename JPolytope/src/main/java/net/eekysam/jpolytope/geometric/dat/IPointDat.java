package net.eekysam.jpolytope.geometric.dat;

import net.eekysam.jpolytope.geometric.TransformMatrix;
import net.eekysam.jpolytope.geometric.Vector;

public interface IPointDat extends IGeoDat
{
	public Vector point();
	
	public void transform(TransformMatrix matrix);
}
