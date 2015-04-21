package net.eekysam.jpolytope;

import java.util.ArrayList;

import net.eekysam.jpolytope.geometric.PlatonicSolid;
import net.eekysam.jpolytope.geometric.Polytope;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestTrees
{
	@Parameters(name = "{1}")
	public static ArrayList<Object[]> data()
	{
		ArrayList<Object[]> data = new ArrayList<>();
		for (PlatonicSolid solid : PlatonicSolid.values())
		{
			data.add(new Object[] { solid.geometry(), solid.name() });
		}
		
		return data;
	}
	
	@Parameter(0)
	public Polytope polytope;
	@Parameter(1)
	public String name;
	
	@Test
	public void makeTree()
	{
		ArrayList<String> list = new ArrayList<String>();
		int[][] tree = this.polytope.geo.getTree();
		for (int i = 0; i < tree.length; i++)
		{
			for (int j = 0; j < tree[i].length; j++)
			{
				list.add(i + " -> " + tree[i][j]);
			}
		}
		String[] rules = new String[list.size()];
		list.toArray(rules);
		
		String out = "{" + String.join(", ", rules) + "}";
		
		System.out.printf("%s: %s%n", this.name, out);
	}
}