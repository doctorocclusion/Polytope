package net.eekysam.jpolytope.geometric.ops;

import java.util.ArrayList;
import java.util.Set;

import net.eekysam.jpolytope.general.GradedPoset;
import net.eekysam.jpolytope.general.ops.CreateDual;
import net.eekysam.jpolytope.geometric.Polytope;
import net.eekysam.jpolytope.geometric.dat.IGeoDat;
import net.eekysam.jpolytope.geometric.dat.Vertex;

public class PolytopeDual implements IGeoOp
{
	private Polytope poly;
	private Polytope dual;
	
	public PolytopeDual(Polytope original)
	{
		this.poly = original;
	}
	
	@Override
	public void apply()
	{
		GradedPoset<IGeoDat> nul = this.poly.updateNullElement();
		if (nul == null)
		{
			this.dual = null;
			return;
		}
		CreateDual<IGeoDat> dualer = new CreateDual<>(nul);
		dualer.apply();
		this.dual = new Polytope(dualer.get());
		
		Set<GradedPoset<IGeoDat>> verts = this.dual.geo.getElements(0);
		for (GradedPoset<IGeoDat> vert : verts)
		{
			Set<GradedPoset<IGeoDat>> oldVerts = vert.getElements(dualer.getDualsRank(0));
			ArrayList<Vertex> data = new ArrayList<Vertex>();
			data.ensureCapacity(oldVerts.size());
			int dim = -1;
			for (GradedPoset<IGeoDat> old : oldVerts)
			{
				if (old.data instanceof Vertex)
				{
					Vertex v = (Vertex) old.data;
					if (dim != -1 && dim != v.vector.length)
					{
						dim = -1;
						break;
					}
					dim = v.vector.length;
					data.add(v);
				}
			}
			if (dim != -1)
			{
				double[] sum = new double[dim];
				for (Vertex old : data)
				{
					for (int i = 0; i < dim; i++)
					{
						sum[i] += old.vector[i];
					}
				}
				for (int i = 0; i < dim; i++)
				{
					sum[i] /= data.size();
				}
				vert.data = new Vertex(sum);
			}
		}
	}
	
	@Override
	public Polytope get()
	{
		return this.dual;
	}
}
