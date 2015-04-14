package net.eekysam.jpolytope;

public class Primitive
{
	public final Vector[] verts;
	
	public Primitive(Vector... verts)
	{
		this.verts = verts;
	}
	
	public Primitive[] facets()
	{
		Primitive[] facets = new Primitive[this.verts.length];
		for (int i = 0; i < facets.length; i++)
		{
			facets[i] = new Primitive(this.vertsWithout(i));
		}
		return facets;
	}
	
	private Vector[] vertsWithout(int remove)
	{
		Vector[] newverts = new Vector[this.verts.length - 1];
		int j = 0;
		for (int i = 0; i < this.verts.length; i++)
		{
			if (i != remove)
			{
				newverts[j++] = this.verts[i];
			}
		}
		return newverts;
	}
	
	public int getDimension()
	{
		return this.verts.length - 1;
	}
}
