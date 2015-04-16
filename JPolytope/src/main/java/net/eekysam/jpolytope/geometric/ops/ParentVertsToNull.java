package net.eekysam.jpolytope.geometric.ops;

import java.util.HashSet;
import java.util.Set;

import net.eekysam.jpolytope.general.GradedPoset;
import net.eekysam.jpolytope.geometric.Polytope;
import net.eekysam.jpolytope.geometric.dat.IGeoDat;

public class ParentVertsToNull implements IGeoOp
{
	private Polytope poly;
	private GradedPoset<IGeoDat> theNull = null;
	
	public ParentVertsToNull(Polytope poly)
	{
		this.poly = poly;
	}
	
	public GradedPoset<IGeoDat> getNull()
	{
		return this.theNull;
	}
	
	@Override
	public void apply()
	{
		this.theNull = null;
		Set<GradedPoset<IGeoDat>> bots = this.poly.geo.getBots();
		if (bots.size() == 1)
		{
			GradedPoset<IGeoDat> bot = bots.iterator().next();
			if (bot.rank < -1)
			{
				throw new IllegalStateException();
			}
			else if (bot.rank == -1)
			{
				return;
			}
		}
		Set<GradedPoset<IGeoDat>> togive = new HashSet<>();
		for (GradedPoset<IGeoDat> bot : bots)
		{
			if (bot.rank < -1)
			{
				throw new IllegalStateException();
			}
			else if (bot.rank == -1)
			{
				if (this.theNull == null)
				{
					this.theNull = bot;
				}
				else
				{
					togive.addAll(bot.splitFromParents());
				}
			}
			else if (bot.rank == 0)
			{
				togive.add(bot);
			}
		}
		if (togive.isEmpty())
		{
			return;
		}
		if (this.theNull == null)
		{
			this.theNull = new GradedPoset<IGeoDat>(-1);
		}
		for (GradedPoset<IGeoDat> to : togive)
		{
			to.addChild(this.theNull);
		}
	}
	
	@Override
	public Polytope get()
	{
		return this.poly;
	}
}
