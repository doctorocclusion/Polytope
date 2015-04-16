package net.eekysam.jpolytope.general.ops;

public interface IOperation<T>
{
	public IOperation<T> run();
	
	public T get();
}
