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
		void createTopo(Builder3D<?> builder)
		{
			builder.addFace(null, 0, 1, 2);
			builder.addFace(null, 0, 3, 1);
			builder.addFace(null, 1, 3, 2);
			builder.addFace(null, 2, 3, 0);
		}
		
		@Override
		void createData(Builder3D<?> builder)
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
		void createTopo(Builder3D<?> builder)
		{
			builder.addFace(null, 0, 1, 2, 3);
			builder.addFace(null, 4, 5, 6, 7);
			builder.addFace(null, 0, 1, 5, 4);
			builder.addFace(null, 1, 2, 6, 5);
			builder.addFace(null, 2, 3, 7, 6);
			builder.addFace(null, 3, 0, 4, 7);
		}
		
		@Override
		void createData(Builder3D<?> builder)
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
		void createTopo(Builder3D<?> builder)
		{
			builder.addFace(null, 0, 5, 1);
			builder.addFace(null, 1, 5, 2);
			builder.addFace(null, 2, 5, 3);
			builder.addFace(null, 3, 5, 0);
			
			builder.addFace(null, 0, 6, 1);
			builder.addFace(null, 1, 6, 2);
			builder.addFace(null, 2, 6, 3);
			builder.addFace(null, 3, 6, 0);
		}
		
		@Override
		void createData(Builder3D<?> builder)
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
		void createTopo(Builder3D<?> builder)
		{
			builder.addFace(null, 0, 1, 2, 3, 4);
			
			builder.addFace(null, 0, 5, 10, 9, 4);
			builder.addFace(null, 4, 9, 14, 8, 3);
			builder.addFace(null, 3, 8, 13, 7, 2);
			builder.addFace(null, 2, 7, 12, 6, 1);
			builder.addFace(null, 1, 6, 11, 5, 0);
			
			builder.addFace(null, 9, 10, 15, 19, 14);
			builder.addFace(null, 5, 10, 15, 16, 11);
			builder.addFace(null, 6, 12, 17, 16, 11);
			builder.addFace(null, 7, 13, 18, 17, 12);
			builder.addFace(null, 8, 14, 19, 18, 13);
			
			builder.addFace(null, 15, 16, 17, 18, 19);
		}
		
		@Override
		void createData(Builder3D<?> builder)
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
		void createTopo(Builder3D<?> builder)
		{
			builder.addFace(null, 0, 1, 2);
			
			builder.addFace(null, 0, 2, 8);
			builder.addFace(null, 0, 8, 3);
			builder.addFace(null, 0, 3, 4);
			
			builder.addFace(null, 1, 0, 4);
			builder.addFace(null, 1, 4, 5);
			builder.addFace(null, 1, 5, 6);
			
			builder.addFace(null, 2, 1, 6);
			builder.addFace(null, 2, 6, 7);
			builder.addFace(null, 2, 7, 8);
			
			builder.addFace(null, 3, 8, 9);
			builder.addFace(null, 3, 9, 10);
			builder.addFace(null, 3, 10, 4);
			
			builder.addFace(null, 5, 4, 10);
			builder.addFace(null, 5, 10, 11);
			builder.addFace(null, 5, 11, 6);
			
			builder.addFace(null, 7, 6, 11);
			builder.addFace(null, 7, 11, 9);
			builder.addFace(null, 7, 9, 8);
			
			builder.addFace(null, 9, 10, 11);
		}
		
		@Override
		void createData(Builder3D<?> builder)
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
		
		this.builder = new Builder3D<Integer>()
		{
			@Override
			public Integer createVert(int id, Integer given)
			{
				if (given == null)
				{
					return id;
				}
				return super.createVert(id, given);
			}
		};
		this.createTopo(this.builder);
	}
	
	public abstract PlatonicSolid dual();
	
	abstract void createTopo(Builder3D<?> builder);
	
	abstract void createData(Builder3D<?> builder);
	
	public LinkedPolytope<?> geometry()
	{
		this.createData(this.builder);
		return this.builder.generatePoly();
	}
}
