package net.eekysam.jpolytope;

import java.util.HashSet;

import net.eekysam.jpolytope.general.GradedPoset;
import net.eekysam.jpolytope.general.ops.CreateDual;
import net.eekysam.jpolytope.geometric.PlatonicSolid;

import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestDuals
{
	@Parameters(name = "{1}")
	public static HashSet<Object[]> data()
	{
		HashSet<Object[]> data = new HashSet<>();
		for (PlatonicSolid solid : PlatonicSolid.values())
		{
			data.add(new Object[] { solid.geometry().geo, String.format("%s (3D", solid.name()) });
		}
		
		RandomGenerator rng = new JDKRandomGenerator();
		RandomPoset<Object> gen = new RandomPoset<>();
		
		gen.addLayer(10, 1, 0);
		gen.addLayer(16, 2, 0);
		gen.addLayer(18, 6, 3);
		gen.addLayer(16, 4, 2);
		gen.addLayer(2, 6, 2);
		
		data.add(new Object[] { gen.create(rng), "RAND1 (5D)" });
		
		return data;
	}
	
	@Parameter(0)
	public GradedPoset<?> poset;
	public CreateDual<?> cdual;
	public GradedPoset<?> dual;
	@Parameter(1)
	public String name;
	
	@Before
	public void setup()
	{
		this.cdual = new CreateDual<>(this.poset);
		this.cdual.run();
		this.dual = this.cdual.get();
	}
	
	@Test
	public void testCounts()
	{
		for (int i = this.cdual.getMinRank(); i <= this.cdual.getMaxRank(); i++)
		{
			Assert.assertEquals("rank " + i + " elements: ", this.poset.getElements(i).size(), this.dual.getElements(this.cdual.getDualsRank(i)).size());
		}
	}
}
