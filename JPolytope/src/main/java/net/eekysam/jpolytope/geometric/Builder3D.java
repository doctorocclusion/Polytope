package net.eekysam.jpolytope.geometric;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import net.eekysam.jpolytope.general.PolytopeGraph;
import net.eekysam.jpolytope.geometric.dat.IGeoDat;
import net.eekysam.jpolytope.geometric.dat.Vertex;

public class Builder3D
{
	static class Edge
	{
		public final int v1;
		public final int v2;
		
		public Edge(int v1, int v2)
		{
			this.v1 = v1;
			this.v2 = v2;
		}
		
		@Override
		public boolean equals(Object o)
		{
			if (o instanceof Edge)
			{
				Edge e = (Edge) o;
				if (e.v1 == this.v1 && e.v2 == this.v2)
				{
					return true;
				}
				if (e.v1 == this.v2 && e.v2 == this.v1)
				{
					return true;
				}
			}
			return false;
		}
	}
	
	static class Face
	{
		public final int[] edges;
		
		public Face(int... edges)
		{
			this.edges = edges;
			Arrays.sort(this.edges);
		}
		
		@Override
		public boolean equals(Object o)
		{
			if (o instanceof Face)
			{
				Face f = (Face) o;
				return Arrays.equals(f.edges, this.edges);
			}
			return false;
		}
	}
	
	private ArrayList<Face> faces = new ArrayList<>();
	private ArrayList<Edge> edges = new ArrayList<>();
	private HashMap<Integer, Vertex> verts = new HashMap<>();
	
	public void addFace(int... verts)
	{
		int[] es = new int[verts.length];
		for (int i = 0; i < verts.length; i++)
		{
			Edge e = new Edge(verts[i], verts[(i + 1) % verts.length]);
			es[i] = this.addEdge(e);
		}
		Face face = new Face(es);
		int ind = this.faces.indexOf(face);
		if (ind != -1)
		{
			this.faces.set(ind, face);
			return;
		}
		this.faces.add(face);
	}
	
	private int addEdge(Edge edge)
	{
		this.addVert(edge.v1, null, false);
		this.addVert(edge.v2, null, false);
		int ind = this.edges.indexOf(edge);
		if (ind != -1)
		{
			return ind;
		}
		ind = this.edges.size();
		this.edges.add(edge);
		return ind;
	}
	
	public void setVert(int index, double... vert)
	{
		this.setVert(index, new Vertex(vert));
	}
	
	public void setVert(int index, Vertex vert)
	{
		this.addVert(index, vert, true);
	}
	
	private void addVert(int index, Vertex vert, boolean overwrite)
	{
		if (this.verts.containsKey(index))
		{
			if (!overwrite)
			{
				return;
			}
		}
		this.verts.put(index, vert);
	}
	
	public Polytope generatePoly()
	{
		Polytope poly = new Polytope(3);
		
		PolytopeGraph<IGeoDat> nullgeo = new PolytopeGraph<IGeoDat>(-1);
		
		HashMap<Integer, PolytopeGraph<IGeoDat>> pvs = new HashMap<>();
		for (Entry<Integer, Vertex> vert : this.verts.entrySet())
		{
			PolytopeGraph<IGeoDat> pv = new PolytopeGraph<>(0);
			pv.data = vert.getValue();
			pv.addChild(nullgeo);
			pvs.put(vert.getKey(), pv);
		}
		
		@SuppressWarnings("unchecked")
		PolytopeGraph<IGeoDat>[] pes = new PolytopeGraph[this.edges.size()];
		
		for (int i = 0; i < pes.length; i++)
		{
			pes[i] = new PolytopeGraph<>(1);
			Edge e = this.edges.get(i);
			pes[i].addChild(pvs.get(e.v1));
			pes[i].addChild(pvs.get(e.v2));
		}
		
		@SuppressWarnings("unchecked")
		PolytopeGraph<IGeoDat>[] fes = new PolytopeGraph[this.faces.size()];
		
		for (int i = 0; i < fes.length; i++)
		{
			fes[i] = new PolytopeGraph<>(2);
			Face f = this.faces.get(i);
			for (int e : f.edges)
			{
				fes[i].addChild(pes[e]);
			}
		}
		
		for (PolytopeGraph<IGeoDat> f : fes)
		{
			poly.geo.addChild(f);
		}
		
		return poly;
	}
}
