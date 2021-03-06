package net.eekysam.jpolytope;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import net.eekysam.jpolytope.general.GradedPoset;
import net.eekysam.jpolytope.geometric.PlatonicSolid;
import net.eekysam.jpolytope.geometric.Polytope;
import net.eekysam.jpolytope.geometric.Vector;
import net.eekysam.jpolytope.geometric.dat.IGeoDat;
import net.eekysam.jpolytope.geometric.dat.Vertex;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestPlatonic
{
	@Parameters(name = "{0}")
	public static Object[] data()
	{
		return PlatonicSolid.values();
	}
	
	@Parameter
	public PlatonicSolid solid;
	
	public Polytope geometry;
	public HashMap<Integer, Set<GradedPoset<IGeoDat>>> elements;
	
	@Before
	public void setup()
	{
		this.geometry = this.solid.geometry();
		this.elements = this.geometry.geo.getLayers();
	}
	
	@Test
	public void testEulerChar()
	{
		Assert.assertEquals(2, this.solid.verts - this.solid.edges + this.solid.faces);
	}
	
	@Test
	public void testVertNum()
	{
		Assert.assertEquals(this.solid.verts, this.elements.get(0).size());
	}
	
	@Test
	public void testEdgeNum()
	{
		Assert.assertEquals(this.solid.edges, this.elements.get(1).size());
	}
	
	@Test
	public void testFaceNum()
	{
		Assert.assertEquals(this.solid.faces, this.elements.get(2).size());
	}
	
	@Test
	public void testP()
	{
		for (GradedPoset<IGeoDat> face : this.elements.get(2))
		{
			Assert.assertEquals(this.solid.p, face.childCount());
		}
	}
	
	@Test
	public void testEdgeRelations()
	{
		for (GradedPoset<IGeoDat> edge : this.elements.get(1))
		{
			Assert.assertEquals(2, edge.childCount());
			Assert.assertEquals(2, edge.parentCount());
		}
	}
	
	@Test
	public void testQ()
	{
		for (GradedPoset<IGeoDat> vert : this.elements.get(0))
		{
			Assert.assertEquals(this.solid.q, vert.parentCount());
		}
	}
	
	@Test
	public void testEdgeLength()
	{
		for (GradedPoset<IGeoDat> edge : this.elements.get(1))
		{
			Iterator<GradedPoset<IGeoDat>> verts = edge.getChildren().iterator();
			GradedPoset<IGeoDat> v1 = verts.next();
			GradedPoset<IGeoDat> v2 = verts.next();
			double length = Vector.diff((Vertex) v1.data, (Vertex) v2.data).length();
			Assert.assertEquals(1.0, length, 0.00001);
		}
	}
	
	@Test
	public void testGraphEnds()
	{
		Set<GradedPoset<IGeoDat>> tops = this.geometry.geo.getTops();
		Set<GradedPoset<IGeoDat>> bots = this.geometry.geo.getBots();
		Assert.assertEquals(1, tops.size());
		Assert.assertEquals(1, bots.size());
		GradedPoset<IGeoDat> top = tops.iterator().next();
		GradedPoset<IGeoDat> bot = bots.iterator().next();
		Assert.assertEquals(3, top.rank);
		Assert.assertEquals(-1, bot.rank);
	}
}
