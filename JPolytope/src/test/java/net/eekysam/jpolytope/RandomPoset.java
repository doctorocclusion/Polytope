package net.eekysam.jpolytope;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.random.RandomGenerator;

import net.eekysam.jpolytope.general.GradedPoset;

public class RandomPoset<T>
{
	class LayerInfo
	{
		int elements;
		int children;
		int childrenMargin;
		
		public LayerInfo(int elements, int children, int childrenMargin)
		{
			this.elements = elements;
			this.children = children;
			this.childrenMargin = childrenMargin;
		}
	}
	
	private ArrayList<LayerInfo> spec = new ArrayList<LayerInfo>();
	private RandomGenerator gen;
	
	private GradedPoset<T> top;
	private GradedPoset<?>[][] poset;
	private GradedPoset<T> bot;
	
	public void addLayer(int elements, int childrenPer, int randomness)
	{
		this.spec.add(new LayerInfo(elements, childrenPer, randomness));
	}
	
	@SuppressWarnings("unchecked")
	public GradedPoset<T> create(RandomGenerator gen)
	{
		this.gen = gen;
		
		this.poset = new GradedPoset<?>[this.spec.size()][];
		
		for (int i = 0; i < this.poset.length; i++)
		{
			this.poset[i] = new GradedPoset<?>[this.spec.get(i).elements];
			for (int j = 0; j < this.poset[i].length; j++)
			{
				this.poset[i][j] = new GradedPoset<T>(i);
			}
		}
		
		this.top = new GradedPoset<T>(this.poset.length);
		this.bot = new GradedPoset<T>(-1);
		
		for (int i = 0; i < this.poset.length; i++)
		{
			LayerInfo info = this.spec.get(i);
			for (int j = 0; j < this.poset[i].length; j++)
			{
				int off = 0;
				if (info.childrenMargin > 0)
				{
					off = this.gen.nextInt(info.childrenMargin * 2) - info.childrenMargin;
				}
				this.pick((GradedPoset<T>) this.poset[i][j], off + info.children);
			}
		}
		
		return this.top;
	}
	
	@SuppressWarnings("unchecked")
	private void pick(GradedPoset<T> customer, int num)
	{
		if (customer.rank < 0 || customer.rank >= this.poset.length)
		{
			return;
		}
		if (customer.rank == this.poset.length - 1)
		{
			customer.addParent(this.top);
		}
		if (customer.rank == 0)
		{
			customer.addChild(this.bot);
			return;
		}
		
		List<GradedPoset<?>> belows = new ArrayList<>();
		belows.addAll(Arrays.asList(this.poset[customer.rank - 1]));
		num = Math.min(num, belows.size());
		
		while (num-- > 0)
		{
			int i = this.gen.nextInt(belows.size());
			customer.addChild((GradedPoset<T>) belows.get(i));
			belows.remove(i);
		}
	}
}
