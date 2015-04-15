package net.eekysam.jpolytope.geometric;

public enum PlatonicSolid
{
	TETRAHEDRON(3, 3)
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
		}
	},
	CUBE(4, 3)
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
		}
	},
	OCTAHEDRON(3, 4)
	{
		@Override
		public PlatonicSolid dual()
		{
			return CUBE;
		}
		
		@Override
		void create(Builder3D builder)
		{
			builder.addFace(0, 5, 1);
			builder.addFace(1, 5, 2);
			builder.addFace(2, 5, 3);
			builder.addFace(3, 5, 0);
			
			builder.addFace(0, 6, 1);
			builder.addFace(1, 6, 2);
			builder.addFace(2, 6, 3);
			builder.addFace(3, 6, 0);
		}
	},
	DODECAHEDRON(5, 3)
	{
		@Override
		public PlatonicSolid dual()
		{
			return ICOSAHEDRON;
		}
		
		@Override
		void create(Builder3D builder)
		{
			builder.addFace(0, 1, 2, 3, 4);
			
			builder.addFace(0, 5, 10, 9, 4);
			builder.addFace(4, 9, 14, 8, 3);
			builder.addFace(3, 8, 13, 7, 2);
			builder.addFace(2, 7, 12, 6, 1);
			builder.addFace(1, 6, 11, 5, 0);
			
			builder.addFace(9, 10, 15, 19, 14);
			builder.addFace(5, 10, 15, 16, 11);
			builder.addFace(6, 12, 17, 16, 11);
			builder.addFace(7, 13, 18, 17, 12);
			builder.addFace(8, 14, 19, 18, 13);
			
			builder.addFace(15, 16, 17, 18, 19);
		}
	},
	ICOSAHEDRON(3, 5)
	{
		@Override
		public PlatonicSolid dual()
		{
			return DODECAHEDRON;
		}
		
		@Override
		void create(Builder3D builder)
		{
			builder.addFace(0, 1, 2);
			
			builder.addFace(0, 2, 8);
			builder.addFace(0, 8, 3);
			builder.addFace(0, 3, 4);
			
			builder.addFace(1, 0, 4);
			builder.addFace(1, 4, 5);
			builder.addFace(1, 5, 6);
			
			builder.addFace(2, 1, 6);
			builder.addFace(2, 6, 7);
			builder.addFace(2, 7, 8);
			
			builder.addFace(3, 8, 9);
			builder.addFace(3, 9, 10);
			builder.addFace(3, 10, 4);
			
			builder.addFace(5, 4, 10);
			builder.addFace(5, 10, 11);
			builder.addFace(5, 11, 6);
			
			builder.addFace(7, 6, 11);
			builder.addFace(7, 11, 9);
			builder.addFace(7, 9, 8);
			
			builder.addFace(9, 10, 11);
		}
	};
	
	public final int verts;
	public final int edges;
	public final int faces;
	
	public final int p;
	public final int q;
	
	private final Polytope poly;
	
	PlatonicSolid(int p, int q)
	{
		this.p = p;
		this.q = q;
		
		int denom = (4 - (p - 2) * (1 - 2));
		this.verts = (4 * p) / denom;
		this.edges = (2 * p * q) / denom;
		this.faces = (4 * q) / denom;
		
		Builder3D builder = new Builder3D();
		this.create(builder);
		this.poly = builder.generatePoly();
	}
	
	public abstract PlatonicSolid dual();
	
	abstract void create(Builder3D builder);
	
	public Polytope geometry()
	{
		return this.poly.clone();
	}
}
