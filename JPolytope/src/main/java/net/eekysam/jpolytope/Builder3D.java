package net.eekysam.jpolytope;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

public class Builder3D<T>
{
	static class Edge<T>
	{
		public final int v1;
		public final int v2;
		
		public T data;
		
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
				Edge<?> e = (Edge<?>) o;
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
	
	static class Face<T>
	{
		public final int[] edges;
		
		public T data;
		
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
				Face<?> f = (Face<?>) o;
				return Arrays.equals(f.edges, this.edges);
			}
			return false;
		}
	}
	
	private ArrayList<Face<T>> faces = new ArrayList<>();
	private ArrayList<Edge<T>> edges = new ArrayList<>();
	private HashMap<Integer, T> verts = new HashMap<>();
	
	public void addFace(T data, int... verts)
	{
		int[] es = new int[verts.length];
		for (int i = 0; i < verts.length; i++)
		{
			Edge<T> e = new Edge<>(verts[i], verts[(i + 1) % verts.length]);
			e.data = data;
			es[i] = this.addEdge(e);
		}
		Face<T> face = new Face<>(es);
		int ind = this.faces.indexOf(face);
		if (ind != -1)
		{
			this.faces.set(ind, face);
			return;
		}
		this.faces.add(face);
	}
	
	public void setEdge(int v1, int v2, T data)
	{
		Edge<T> e = new Edge<>(v1, v2);
		e.data = data;
		this.addEdge(e);
	}
	
	private int addEdge(Edge<T> edge)
	{
		int ind = this.edges.indexOf(edge);
		if (ind != -1)
		{
			this.edges.set(ind, edge);
			return ind;
		}
		this.edges.add(edge);
		return this.edges.size() - 1;
	}
	
	public void setVert(int index, T data)
	{
		this.verts.put(index, data);
	}
	
	public LinkedPolytope<T> generatePoly(T data)
	{
		LinkedPolytope<T> poly = new LinkedPolytope<T>(3);
		poly.data = data;
		
		HashMap<Integer, LinkedPolytope<T>> pvs = new HashMap<>();
		for (Entry<Integer, T> vert : this.verts.entrySet())
		{
			LinkedPolytope<T> pv = new LinkedPolytope<>(0);
			pv.data = vert.getValue();
			pvs.put(vert.getKey(), pv);
		}
		
		@SuppressWarnings("unchecked")
		LinkedPolytope<T>[] pes = new LinkedPolytope[this.edges.size()];
		
		for (int i = 0; i < pes.length; i++)
		{
			LinkedPolytope<T> pe = new LinkedPolytope<>(1);
			Edge<T> e = this.edges.get(i);
			pe.data = e.data;
			pe.addChild(pvs.get(e.v1));
			pe.addChild(pvs.get(e.v2));
		}
		
		@SuppressWarnings("unchecked")
		LinkedPolytope<T>[] fes = new LinkedPolytope[this.faces.size()];
		
		for (int i = 0; i < fes.length; i++)
		{
			LinkedPolytope<T> pf = new LinkedPolytope<>(2);
			Face<T> f = this.faces.get(i);
			pf.data = f.data;
			for (int e : f.edges)
			{
				pf.addChild(pes[e]);
			}
		}
		
		for (LinkedPolytope<T> f : fes)
		{
			poly.addChild(f);
		}
		
		return poly;
	}
}
