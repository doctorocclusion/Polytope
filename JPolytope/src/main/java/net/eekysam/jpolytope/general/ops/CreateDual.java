package net.eekysam.jpolytope.general.ops;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import net.eekysam.jpolytope.general.GradedPoset;

public class CreateDual<T> implements IOperation<GradedPoset<T>>
{
	class Dualer implements Consumer<GradedPoset<T>>
	{
		@Override
		public void accept(GradedPoset<T> t)
		{
			GradedPoset<T> dual = new GradedPoset<T>(CreateDual.this.getDualsRank(t.rank));
			dual.data = t.data;
			CreateDual.this.duals.put(t.id, dual);
			for (GradedPoset<T> parent : t.getParents())
			{
				GradedPoset<T> flipped = CreateDual.this.duals.get(parent.id);
				if (flipped != null)
				{
					dual.addChild(flipped);
				}
			}
			for (GradedPoset<T> child : t.getChildren())
			{
				GradedPoset<T> flipped = CreateDual.this.duals.get(child.id);
				if (flipped != null)
				{
					dual.addParent(flipped);
				}
			}
		}
	}
	
	private GradedPoset<T> original;
	private GradedPoset<T> dual;
	
	private int minrank;
	private int maxrank;
	
	private HashMap<UUID, GradedPoset<T>> duals;
	
	public CreateDual(GradedPoset<T> original)
	{
		this.original = original;
	}
	
	@Override
	public void apply()
	{
		this.minrank = Integer.MAX_VALUE;
		this.maxrank = Integer.MIN_VALUE;
		this.duals = new HashMap<>();
		
		Set<GradedPoset<T>> tops = this.original.getTops();
		Set<GradedPoset<T>> bots = this.original.getBots();
		for (GradedPoset<T> top : tops)
		{
			this.maxrank = Math.max(this.maxrank, top.rank);
		}
		for (GradedPoset<T> bot : bots)
		{
			this.minrank = Math.min(this.minrank, bot.rank);
		}
		
		this.original.forEach(new Dualer());
		
		this.dual = this.duals.get(this.original.id);
	}
	
	public int getMinRank()
	{
		return this.minrank;
	}
	
	public int getMaxRank()
	{
		return this.maxrank;
	}
	
	public int getDualsRank(int rank)
	{
		return this.minrank + this.maxrank - rank;
	}
	
	@Override
	public GradedPoset<T> get()
	{
		return this.dual;
	}
}
