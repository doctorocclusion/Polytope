package net.eekysam.jpolytope.geometric;

import java.util.HashMap;
import java.util.Set;

import net.eekysam.jpolytope.general.GradedPoset;
import net.eekysam.jpolytope.geometric.dat.IGeoDat;
import net.eekysam.jpolytope.geometric.dat.IPointDat;

public class Polytope
{
	public final int dimension;
	public final GradedPoset<IGeoDat> geo;
	
	public Polytope(int dimension)
	{
		this(new GradedPoset<>(dimension));
	}
	
	public Polytope(GradedPoset<IGeoDat> geo)
	{
		this.dimension = geo.rank;
		this.geo = geo;
	}
	
	@Override
	public Polytope clone()
	{
		Polytope clone = new Polytope(this.geo.clone());
		HashMap<Integer, Set<GradedPoset<IGeoDat>>> all = clone.geo.getLayers();
		for (Set<GradedPoset<IGeoDat>> dimset : all.values())
		{
			for (GradedPoset<IGeoDat> elem : dimset)
			{
				if (elem.data != null)
				{
					elem.data = elem.data.clone();
				}
			}
		}
		return clone;
	}
	
	public void transform(TransformMatrix matrix)
	{
		for (GradedPoset<IGeoDat> vert : this.geo.getElements(0))
		{
			if (vert.data instanceof IPointDat)
			{
				((IPointDat) vert.data).transform(matrix);
			}
		}
	}
	
	public void scale(double scale)
	{
		TransformMatrix matrix = new TransformMatrix(this.dimension);
		matrix.scale(scale);
		this.transform(matrix);
	}
}
