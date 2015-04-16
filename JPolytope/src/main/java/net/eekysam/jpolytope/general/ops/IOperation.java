package net.eekysam.jpolytope.general.ops;

public interface IOperation<T>
{
	public void apply();
	
	public T get();
}
