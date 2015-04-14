package net.eekysam.jpolytope;

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
		void createBuilder(Builder3D<?> builder)
		{
			builder.addFace(null, 0, 1, 2);
			builder.addFace(null, 0, 3, 1);
			builder.addFace(null, 1, 3, 2);
			builder.addFace(null, 2, 3, 0);
		}
		
		@Override
		void recreateVerts(Builder3D<?> builder)
		{
			
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
		void createBuilder(Builder3D<?> builder)
		{
			builder.addFace(null, 0, 1, 2, 3);
			builder.addFace(null, 4, 5, 6, 7);
			builder.addFace(null, 0, 1, 5, 4);
			builder.addFace(null, 1, 2, 6, 5);
			builder.addFace(null, 2, 3, 7, 6);
			builder.addFace(null, 3, 0, 4, 7);
		}
		
		@Override
		void recreateVerts(Builder3D<?> builder)
		{
			
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
		void createBuilder(Builder3D<?> builder)
		{
			
		}
		
		@Override
		void recreateVerts(Builder3D<?> builder)
		{
			
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
		void createBuilder(Builder3D<?> builder)
		{
			
		}
		
		@Override
		void recreateVerts(Builder3D<?> builder)
		{
			
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
		void createBuilder(Builder3D<?> builder)
		{
			
		}
		
		@Override
		void recreateVerts(Builder3D<?> builder)
		{
			
		}
	};
	
	public final int verts;
	public final int edges;
	public final int faces;
	
	public final int p;
	public final int q;
	
	private Builder3D<?> builder;
	
	PlatonicSolid(int p, int q)
	{
		this.p = p;
		this.q = q;
		
		int denom = (4 - (p - 2) * (1 - 2));
		this.verts = (4 * p) / denom;
		this.edges = (2 * p * q) / denom;
		this.faces = (4 * q) / denom;
		
		this.builder = new Builder3D<>();
		this.createBuilder(this.builder);
	}
	
	public abstract PlatonicSolid dual();
	
	abstract void createBuilder(Builder3D<?> builder);
	
	abstract void recreateVerts(Builder3D<?> builder);
	
	public LinkedPolytope<?> geometry()
	{
		this.recreateVerts(this.builder);
		return this.builder.generatePoly(null);
	}
}
