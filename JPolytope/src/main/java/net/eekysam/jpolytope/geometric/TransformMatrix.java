package net.eekysam.jpolytope.geometric;

import net.eekysam.jpolytope.geometric.ops.IGeoOp;

public class TransformMatrix
{
	public final int dimension;
	public final double[][] matrix;
	
	public TransformMatrix(int dimension)
	{
		this.dimension = dimension;
		this.matrix = new double[this.dimension + 1][this.dimension];
		this.identity();
	}
	
	public TransformMatrix(TransformMatrix original)
	{
		this(original.dimension);
		this.copy(original.matrix);
	}
	
	public void identity()
	{
		for (int i = 0; i <= this.dimension; i++)
		{
			for (int j = 0; j < this.dimension; j++)
			{
				this.matrix[i][j] = i == j ? 1.0 : 0.0;
			}
		}
	}
	
	public void empty()
	{
		this.fill(0.0);
	}
	
	public void fill(double value)
	{
		for (int i = 0; i <= this.dimension; i++)
		{
			for (int j = 0; j < this.dimension; j++)
			{
				this.matrix[i][j] = value;
			}
		}
	}
	
	public void copy(double[][] from)
	{
		for (int i = 0; i < Math.min(this.matrix.length, from.length); i++)
		{
			for (int j = 0; j < Math.min(this.matrix[i].length, from[i].length); j++)
			{
				this.matrix[i][j] = from[i][j];
			}
		}
	}
	
	public void translate(double... x)
	{
		for (int i = 0; i < Math.min(x.length, this.dimension); i++)
		{
			this.matrix[this.dimension][i] = x[i];
		}
	}
	
	public void scale(double scale, int axis, boolean onTranslate)
	{
		for (int i = 0; i < this.dimension; i++)
		{
			this.matrix[i][axis] *= scale;
		}
		if (onTranslate)
		{
			this.matrix[this.dimension][axis] *= scale;
		}
	}
	
	public void scale(double scale, boolean onTranslate)
	{
		for (int i = 0; i < this.dimension; i++)
		{
			this.scale(scale, i, onTranslate);
		}
	}
	
	public void scale(double scale)
	{
		this.scale(scale, false);
	}
	
	public void transform(Vector vec, double translate)
	{
		int box = Math.min(vec.vector.length, this.dimension);
		for (int i = 0; i < box; i++)
		{
			double x = vec.vector[i];
			vec.vector[i] = 0;
			for (int j = 0; j < box; j++)
			{
				vec.vector[i] += x * this.matrix[j][i];
			}
			vec.vector[i] += translate * this.matrix[this.dimension][i];
		}
	}
	
	public void transform(Vector vec)
	{
		this.transform(vec, 1.0);
	}
	
	public IGeoOp operation(Polytope poly)
	{
		return new IGeoOp()
		{
			@Override
			public void apply()
			{
				poly.transform(TransformMatrix.this);
			}
			
			@Override
			public Polytope get()
			{
				return poly;
			}
		};
	}
}
