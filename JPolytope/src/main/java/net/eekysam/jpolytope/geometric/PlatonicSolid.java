package net.eekysam.jpolytope.geometric;

public enum PlatonicSolid
{
	TETRAHEDRON(3, 3, Math.sqrt(6.0) / 4.0)
	{
		@Override
		public PlatonicSolid dual()
		{
			return TETRAHEDRON;
		}
		
		@Override
		void create(Builder3D builder)
		{
			builder.addFace(0, 1, 2);
			builder.addFace(0, 3, 1);
			builder.addFace(1, 3, 2);
			builder.addFace(2, 3, 0);
			
			double h = Math.sqrt(2) / 2;
			
			builder.setVert(0, 0.5, -h / 2, 0);
			builder.setVert(1, -0.5, -h / 2, 0);
			builder.setVert(2, 0, h / 2, -0.5);
			builder.setVert(3, 0, h / 2, 0.5);
		}
	},
	CUBE(4, 3, Math.sqrt(3.0) / 2.0)
	{
		@Override
		public PlatonicSolid dual()
		{
			return OCTAHEDRON;
		}
		
		@Override
		void create(Builder3D builder)
		{
			builder.addFace(0, 1, 2, 3);
			builder.addFace(4, 5, 6, 7);
			builder.addFace(0, 1, 5, 4);
			builder.addFace(1, 2, 6, 5);
			builder.addFace(2, 3, 7, 6);
			builder.addFace(3, 0, 4, 7);
			
			builder.setVert(0, 0.5, -0.5, 0.5);
			builder.setVert(1, 0.5, -0.5, -0.5);
			builder.setVert(2, -0.5, -0.5, -0.5);
			builder.setVert(3, -0.5, -0.5, 0.5);
			
			builder.setVert(4, 0.5, 0.5, 0.5);
			builder.setVert(5, 0.5, 0.5, -0.5);
			builder.setVert(6, -0.5, 0.5, -0.5);
			builder.setVert(7, -0.5, 0.5, 0.5);
		}
	},
	OCTAHEDRON(3, 4, Math.sqrt(2.0) / 2.0)
	{
		@Override
		public PlatonicSolid dual()
		{
			return CUBE;
		}
		
		@Override
		void create(Builder3D builder)
		{
			builder.addFace(0, 4, 1);
			builder.addFace(1, 4, 2);
			builder.addFace(2, 4, 3);
			builder.addFace(3, 4, 0);
			
			builder.addFace(0, 5, 1);
			builder.addFace(1, 5, 2);
			builder.addFace(2, 5, 3);
			builder.addFace(3, 5, 0);
			
			double r = this.radius(1);
			
			builder.setVert(0, r, 0, 0);
			builder.setVert(1, 0, 0, r);
			builder.setVert(2, -r, 0, 0);
			builder.setVert(3, 0, 0, -r);
			
			builder.setVert(4, 0, r, 0);
			builder.setVert(5, 0, -r, 0);
		}
	},
	DODECAHEDRON(5, 3, (Math.sqrt(3.0) / 4.0) * (1.0 + Math.sqrt(5.0)))
	{
		@Override
		public PlatonicSolid dual()
		{
			return ICOSAHEDRON;
		}
		
		@Override
		void create(Builder3D builder)
		{
			builder.addFace(0, 12, 13, 3, 9);
			builder.addFace(1, 10, 2, 13, 12);
			builder.addFace(0, 12, 1, 17, 16);
			builder.addFace(2, 13, 3, 19, 18);
			builder.addFace(4, 8, 7, 14, 15);
			builder.addFace(14, 15, 5, 11, 6);
			builder.addFace(4, 15, 5, 17, 16);
			builder.addFace(6, 14, 7, 19, 18);
			builder.addFace(0, 16, 4, 8, 9);
			builder.addFace(8, 9, 3, 19, 7);
			builder.addFace(10, 11, 5, 17, 1);
			builder.addFace(10, 11, 6, 18, 2);
			
			double T = (1.0 + Math.sqrt(5.0)) / 2.0;
			double x = 1.0 / this.edgeLength(Math.sqrt(3.0));
			
			builder.setVert(0, -x, x, -x);
			builder.setVert(1, -x, x, x);
			builder.setVert(2, x, x, x);
			builder.setVert(3, x, x, -x);
			
			builder.setVert(4, -x, -x, -x);
			builder.setVert(5, -x, -x, x);
			builder.setVert(6, x, -x, x);
			builder.setVert(7, x, -x, -x);
			
			builder.setVert(8, 0, -x / T, -x * T);
			builder.setVert(9, 0, x / T, -x * T);
			builder.setVert(10, 0, x / T, x * T);
			builder.setVert(11, 0, -x / T, x * T);
			
			builder.setVert(12, -x / T, x * T, 0);
			builder.setVert(13, x / T, x * T, 0);
			builder.setVert(14, x / T, -x * T, 0);
			builder.setVert(15, -x / T, -x * T, 0);
			
			builder.setVert(16, -x * T, 0, -x / T);
			builder.setVert(17, -x * T, 0, x / T);
			builder.setVert(18, x * T, 0, x / T);
			builder.setVert(19, x * T, 0, -x / T);
		}
	},
	ICOSAHEDRON(3, 5, Math.sqrt(10.0 + 2.0 * Math.sqrt(5.0)) / 4.0)
	{
		@Override
		public PlatonicSolid dual()
		{
			return DODECAHEDRON;
		}
		
		@Override
		void create(Builder3D builder)
		{
			builder.addFace(0, 1, 5);
			builder.addFace(0, 6, 1);
			builder.addFace(2, 3, 4);
			builder.addFace(3, 2, 7);
			builder.addFace(5, 4, 10);
			builder.addFace(4, 5, 9);
			builder.addFace(8, 9, 1);
			builder.addFace(9, 8, 2);
			builder.addFace(6, 7, 8);
			builder.addFace(7, 6, 11);
			builder.addFace(10, 11, 0);
			builder.addFace(11, 10, 3);
			builder.addFace(5, 1, 9);
			builder.addFace(0, 5, 10);
			builder.addFace(2, 4, 9);
			builder.addFace(4, 3, 10);
			builder.addFace(1, 6, 8);
			builder.addFace(6, 0, 11);
			builder.addFace(2, 8, 7);
			builder.addFace(3, 7, 11);
			
			double T = (1.0 + Math.sqrt(5.0)) / 2.0;
			double x = 0.5;
			
			builder.setVert(0, x * T, 0, x);
			builder.setVert(1, x * T, 0, -x);
			builder.setVert(2, -x * T, 0, -x);
			builder.setVert(3, -x * T, 0, x);
			
			builder.setVert(4, -x, x * T, 0);
			builder.setVert(5, x, x * T, 0);
			builder.setVert(6, x, -x * T, 0);
			builder.setVert(7, -x, -x * T, 0);
			
			builder.setVert(8, 0, -x, -x * T);
			builder.setVert(9, 0, x, -x * T);
			builder.setVert(10, 0, x, x * T);
			builder.setVert(11, 0, -x, x * T);
		}
	};
	
	public final int verts;
	public final int edges;
	public final int faces;
	
	public final int p;
	public final int q;
	
	private final Polytope poly;
	
	private final double radius;
	
	PlatonicSolid(int p, int q, double radius)
	{
		this.p = p;
		this.q = q;
		
		this.radius = radius;
		
		int denom = (4 - (p - 2) * (q - 2));
		this.verts = (4 * p) / denom;
		this.edges = (2 * p * q) / denom;
		this.faces = (4 * q) / denom;
		
		Builder3D builder = new Builder3D();
		this.create(builder);
		this.poly = builder.generatePoly();
	}
	
	public double radius(double edgeLength)
	{
		return this.radius * edgeLength;
	}
	
	public double edgeLength(double radius)
	{
		return radius / this.radius;
	}
	
	public abstract PlatonicSolid dual();
	
	abstract void create(Builder3D builder);
	
	public Polytope geometry()
	{
		return this.poly.clone();
	}
	
	public Polytope geometry(double edge)
	{
		Polytope out = this.geometry();
		out.scale(edge);
		return out;
	}
}
