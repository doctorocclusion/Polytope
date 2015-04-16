package net.eekysam.jpolytope;

import java.util.ArrayList;
import net.eekysam.jpolytope.geometric.PlatonicSolid;
import net.eekysam.jpolytope.geometric.Polytope;
import net.eekysam.jpolytope.geometric.ops.PolytopeDual;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestDuals
{
	@Parameters(name = "{0}")
	public static ArrayList<Object[]> data()
	{
		ArrayList<Object[]> data = new ArrayList<>();
		for (PlatonicSolid solid : PlatonicSolid.values())
		{
			data.add(new Object[] { solid.geometry() });
		}
		
		return data;
	}
	
	@Parameter(0)
	public Polytope poly;
	public PolytopeDual oper;
	public Polytope dual;
	
	@Before
	public void setup()
	{
		this.oper = new PolytopeDual(this.poly);
		this.oper.apply();
		this.dual = this.oper.get();
	}
}
