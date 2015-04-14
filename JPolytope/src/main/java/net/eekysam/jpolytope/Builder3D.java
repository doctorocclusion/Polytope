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
	public T cell;
	
	public void addFace(T data, int... verts)
	{
		int[] es = new int[verts.length];
		for (int i = 0; i < verts.length; i++)
		{
			Edge<T> e = new Edge<>(verts[i], verts[(i + 1) % verts.length]);
			es[i] = this.addEdge(e, false);
		}
		Face<T> face = new Face<>(es);
		face.data = data;
		int ind = this.faces.indexOf(face);
		if (ind != -1)
		{
			this.faces.set(ind, face);
			return;
		}
		ind = this.faces.size();
		face.data = this.createFace(ind, face.data);
		this.faces.add(face);
	}
	
	public void setEdge(int v1, int v2, T data)
	{
		Edge<T> e = new Edge<>(v1, v2);
		e.data = data;
		this.addEdge(e, true);
	}
	
	private int addEdge(Edge<T> edge, boolean overwrite)
	{
		this.addVert(edge.v1, null, false);
		this.addVert(edge.v2, null, false);
		int ind = this.edges.indexOf(edge);
		if (ind != -1)
		{
			if (overwrite)
			{
				this.edges.set(ind, edge);
			}
			return ind;
		}
		ind = this.edges.size();
		edge.data = this.createEdge(ind, edge.data);
		this.edges.add(edge);
		return ind;
	}
	
	public void setVert(int index, T data)
	{
		this.addVert(index, data, true);
	}
	
	public void addVert(int index, T data, boolean overwrite)
	{
		if (this.verts.containsKey(index))
		{
			if (overwrite)
			{
				this.verts.put(index, data);
			}
			return;
		}
		this.verts.put(index, this.createVert(index, data));
	}
	
	public LinkedPolytope<T> generatePoly()
	{
		LinkedPolytope<T> poly = new LinkedPolytope<T>(3);
		poly.data = this.cell;
		
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
			pes[i] = new LinkedPolytope<>(1);
			Edge<T> e = this.edges.get(i);
			pes[i].data = e.data;
			pes[i].addChild(pvs.get(e.v1));
			pes[i].addChild(pvs.get(e.v2));
		}
		
		@SuppressWarnings("unchecked")
		LinkedPolytope<T>[] fes = new LinkedPolytope[this.faces.size()];
		
		for (int i = 0; i < fes.length; i++)
		{
			fes[i] = new LinkedPolytope<>(2);
			Face<T> f = this.faces.get(i);
			fes[i].data = f.data;
			for (int e : f.edges)
			{
				fes[i].addChild(pes[e]);
			}
		}
		
		for (LinkedPolytope<T> f : fes)
		{
			poly.addChild(f);
		}
		
		return poly;
	}
	
	public T createVert(int id, T given)
	{
		return given;
	}
	
	public T createEdge(int id, T given)
	{
		return given;
	}
	
	public T createFace(int id, T given)
	{
		return given;
	}
}
