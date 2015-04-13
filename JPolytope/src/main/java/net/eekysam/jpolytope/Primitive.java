package net.eekysam.jpolytope;

public class Primitive implements IPolytope
{
	public final Vector[] verts;
	
	public Primitive(Vector... verts)
	{
		this.verts = verts;
	}
	
	@Override
	public Primitive[] sides()
	{
		Primitive[] sides = new Primitive[this.verts.length];
		for (int i = 0; i < sides.length; i++)
		{
			sides[i] = new Primitive(this.vertsMissing(i));
		}
		return sides;
	}
	
	private Vector[] vertsMissing(int remove)
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
}
