
public class Elementaire {
	public int type;
	// 1: fire
	// 2: water
	// 3:wood
	//
	public int energy;
	public Position p;

	public Elementaire(int type, Position p) {
		super();
		this.type = type;
		this.energy = 10;
		this.p = p;
	}

	public void update() {
		int mapState = Ecosystem.map.getMapEntities()[this.p.x][this.p.y];
		int mapTexture =Ecosystem.map.getMapTexture()[this.p.x][this.p.y];
		switch (this.type) {
		case 1:
			
			if (mapState == MapEntitiesID.GREEN_TREE || mapState == MapEntitiesID.YOUNG_GREEN_TREE
					|| mapState == MapEntitiesID.BABY_GREEN_TREE) {
					Ecosystem.map.getMapEntities()[this.p.x][this.p.y] = MapEntitiesID.BURNING_TREE;
					this.energy--;
					if(this.energy< 0) {
						this.energy=0;
					}
			}
			
			switch((int) (Math.random()*4)) {
			case 0:
				this.p.x = moduloWidth(this.p.x + 1);
				this.p.y = moduloHeight(this.p.y);
				break;
			case 1:
				this.p.x = moduloWidth(this.p.x - 1);
				this.p.y = moduloHeight(this.p.y);
				break;
			case 2:
				this.p.x = moduloWidth(this.p.x);
				this.p.y = moduloHeight(this.p.y + 1);
				break;
			case 3:
				this.p.x = moduloWidth(this.p.x);
				this.p.y = moduloHeight(this.p.y - 1);
				break;
					
			}
			break;
		case 2:
			
			if (mapState == MapEntitiesID.BURNING_TREE) {
					Ecosystem.map.getMapEntities()[this.p.x][this.p.y] = MapEntitiesID.GREEN_TREE;
					this.energy--;
					if(this.energy< 0) {
						this.energy=0;
					}
			}
			if(mapTexture ==MapTextureID.LAVA) {
				Ecosystem.map.getMapTexture()[this.p.x][this.p.y] = MapTextureID.COLD_LAVA;
				this.energy--;
				if(this.energy< 0) {
					this.energy=0;
				}
			}
			
			switch((int) (Math.random()*4)) {
			case 0:
				this.p.x = moduloWidth(this.p.x + 1);
				this.p.y = moduloHeight(this.p.y);
				break;
			case 1:
				this.p.x = moduloWidth(this.p.x - 1);
				this.p.y = moduloHeight(this.p.y);
				break;
			case 2:
				this.p.x = moduloWidth(this.p.x);
				this.p.y = moduloHeight(this.p.y + 1);
				break;
			case 3:
				this.p.x = moduloWidth(this.p.x);
				this.p.y = moduloHeight(this.p.y - 1);
				break;
					
			}
			break;
		
		case 3:
			if (mapState == MapEntitiesID.NOTHING && mapTexture == MapTextureID.GROUND) {
				Ecosystem.map.getMapEntities()[this.p.x][this.p.y] = MapEntitiesID.BABY_GREEN_TREE;
				this.energy--;
				if(this.energy< 0) {
					this.energy=0;
				}
		}
		
		switch((int) (Math.random()*4)) {
		case 0:
			this.p.x = moduloWidth(this.p.x + 1);
			this.p.y = moduloHeight(this.p.y);
			break;
		case 1:
			this.p.x = moduloWidth(this.p.x - 1);
			this.p.y = moduloHeight(this.p.y);
			break;
		case 2:
			this.p.x = moduloWidth(this.p.x);
			this.p.y = moduloHeight(this.p.y + 1);
			break;
		case 3:
			this.p.x = moduloWidth(this.p.x);
			this.p.y = moduloHeight(this.p.y - 1);
			break;
				
		}
			break;
		default:
			break;
		}
	}

	private int moduloHeight(int i) {
		
		return (i + Ecosystem.mapHeight)%Ecosystem.mapHeight;
	}

	private int moduloWidth(int i) {
		// TODO Auto-generated method stub
		return (i + Ecosystem.mapWidth)%Ecosystem.mapWidth;
	}
	
	
	

}
