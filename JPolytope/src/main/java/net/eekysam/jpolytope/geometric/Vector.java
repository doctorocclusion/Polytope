package net.eekysam.jpolytope.geometric;

public class Vector
{
	public final double[] vector;
	
	public Vector(double... vector)
	{
		this.vector = vector;
	}
	
	public double sqrLength()
	{
		double sum = 0.0;
		for (double x : this.vector)
		{
			sum += x * x;
		}
		return sum;
	}
	
	public double length()
	{
		return Math.sqrt(this.sqrLength());
	}
	
	public static Vector diff(Vector a, Vector b)
	{
		return mix(a, b, 1, -1);
	}
	
	public static Vector mix(Vector a, Vector b, double afac, double bfac)
	{
		return new Vector(doMix(a.vector, b.vector, afac, bfac));
	}
	
	protected static double[] doMix(double[] a, double[] b, double afac, double bfac)
	{
		if (a.length != b.length)
		{
			throw new IllegalArgumentException("The 2 vectors are not the same size");
		}
		double[] out = new double[a.length];
		for (int i = 0; i < out.length; i++)
		{
			out[i] = afac * a[i] + bfac * b[i];
		}
		return out;
	}
}
