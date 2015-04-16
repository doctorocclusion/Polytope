package net.eekysam.jpolytope.general;

import java.util.Collections;
import java.util.Set;

public class RankLevel<T>
{
	public final int rank;
	public final Set<GradedPoset<T>> elements;
	
	RankLevel(int layer, Set<GradedPoset<T>> elements)
	{
		this.rank = layer;
		this.elements = Collections.unmodifiableSet(elements);
	}
}