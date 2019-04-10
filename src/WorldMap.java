import java.util.ArrayList;

public class WorldMap {

	private int highest_pX;
	private int highest_pY;
	
	private int[][] mapTexture;
	private int[][] nextMapTexture;
	
	private int[][] mapEntities;
	private int[][] nextMapEntities;
	
	public double[][] mapAltitude;
	private int mapWidth, mapHeight;
	
	public int cpt_lave, duree_lave;
	public int cpt_pluie, cpt_evaporation;
	public double hauteur_pluie, hauteur_neige = 0, nb_of_snow = 0;
	public boolean is_raining, lave_coule;
	public static final int LAVE_DROITE = 0, LAVE_BAS = 1, LAVE_GAUCHE = 2, LAVE_HAUT = 3;
	

	public WorldMap() {
		this.mapWidth = Ecosystem.mapWidth;
		this.mapHeight = Ecosystem.mapHeight;
		
		mapTexture = new int[mapHeight][mapWidth];
		nextMapTexture = new int[mapHeight][mapWidth];
		mapEntities = new int[mapHeight][mapWidth];
		nextMapEntities = new int[mapHeight][mapWidth];
		mapAltitude = new double[mapHeight][mapWidth];
		
		initWorldMap();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	////				 					INITIALISATION METHODS											////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void initWorldMap() {
		is_raining = false;
		cpt_pluie = cpt_evaporation = 0;
		hauteur_pluie = 0;
		initMapAltitude();
		initMapTexture();
		initMapEntities();
	}
	
			 
	public void initMapAltitude() {
		double rand = Math.random() * 256;
		for (int x = 0; x < mapHeight; x++) {
			for (int y = 0; y < mapWidth; y++)
				mapAltitude[x][y] = PerlinNoise.noise(x + rand, y + rand, PerlinNoise.RESOLUTION)
						* PerlinNoise.INTERVALLE;
		}
	}

	public void initMapTexture() {
		for (int x = 0; x < mapHeight; x++) {
			for (int y = 0; y < mapWidth; y++) {
				mapTexture[x][y] = getTexture(x, y);
			}
		}

		
		initialisationVolcan();
	}

	@SuppressWarnings("unused")
	private boolean surroundedByGround(int x, int y) {
		return nb_of_ground_tiles_around(x,y) == 8;
	}

	public void initMapEntities() {
		for (int x = 0; x < mapEntities.length; x++) {
			for (int y = 0; y < mapEntities[0].length; y++) {
				if (Math.random() < Ecosystem.P_TREE_INIT && mapTexture[x][y] == MapTextureID.GROUND && nb_of_water_tiles_around(x,y) == 0)
						this.mapEntities[x][y] = MapEntitiesID.GREEN_TREE;
				else
					this.mapEntities[x][y] = MapEntitiesID.NOTHING;
			}
		}
	}
	

	public int getTexture(int x, int y) {
		if (mapAltitude[x][y] < -15)
			return MapTextureID.WATER;
		else if (mapAltitude[x][y] >= -15 && mapAltitude[x][y] < 5) // -15 et 15
			return MapTextureID.GROUND;
		else
			return MapTextureID.MOUNTAIN;
	}
	
	

	public void initialisationVolcan() {
		double higherPoint = 0;
		int pX = 0, pY = 0;
		lave_coule = false;
		for (int x = 0; x < mapWidth; x++) {
			for (int y = 0; y < mapHeight; y++) {
				if (mapAltitude[x][y] > higherPoint) {
					higherPoint = mapAltitude[x][y];
					highest_pX = pX = x;
					highest_pY = pY = y;
				}

			}
		}
		mapTexture[pX][pY] = MapTextureID.CRATERE;
	}
	

	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// UPDATE METHODS
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// method used to update maps
	public void update() {
		
		for(int x = 0; x < mapTexture.length; x++) {
			for(int y = 0; y < mapTexture[x].length; y++) {
				nextMapTexture[x][y] = mapTexture[x][y];
				if(nextMapTexture[x][y] == MapTextureID.WATER || nextMapTexture[x][y] == MapTextureID.ICE) {
					if(Ecosystem.season == Ecosystem.WINTER)
						frost(x,y);
					else {
						defrost(x,y);
					}
				}
				if(Ecosystem.season != Ecosystem.WINTER)
					remove_snow(x,y);
			}
			
		}
			
		if(is_raining) {
			pluie();
			snow();
		}else {
			evaporation();
			if(cpt_pluie == 0)
				cpt_pluie = 0;
			else
				cpt_pluie--;
		}
		
		if(lave_coule) 
			ecoulement_lave();

		
		for(int x = 0; x < mapTexture.length; x++) {
			for(int y= 0; y < mapTexture[x].length; y++) {
				mapTexture[x][y] = nextMapTexture[x][y];
			}
		}
		for(int x = 0; x < mapTexture.length; x++) {
			for(int y = 0; y < mapTexture[0].length; y++) {
				if(lave_coule == false) {
					if(nextMapTexture[x][y] == MapTextureID.LAVA)
						mapTexture[x][y] = cooling_lava(x,y);
					if(nextMapTexture[x][y] == MapTextureID.COLD_LAVA)
						mapTexture[x][y] = cleaning_cold_lava(x,y);
				}
			}
		}
		mapTexture[highest_pX][highest_pY] = MapTextureID.CRATERE;
		updateMapEntities();
	}


	public void pluie() {
		cpt_evaporation = 0;
		if(Ecosystem.season != Ecosystem.WINTER) {	//%100 == 30

			for(int i = 0; i < mapAltitude.length; i++) {
				for(int j = 0; j < mapAltitude[i].length; j++) {
					if(mapAltitude[i][j] < ( -15 + hauteur_pluie) && mapAltitude[i][j] > -15)
						nextMapTexture[i][j] = MapTextureID.WATER;
				}
			}
			hauteur_pluie += 0.1;
		}
		cpt_pluie++;
	}
	
	
	public void snow() {
		cpt_evaporation = 0;
		if(Ecosystem.season == Ecosystem.WINTER) {
			for(int i = 0; i < mapAltitude.length; i++) {
				for(int j = 0; j < mapAltitude[i].length; j++) {
					if(is_near_cratere(i,j) == false && mapTexture[i][j] != MapTextureID.LAVA && mapTexture[i][j] != MapTextureID.SNOW 
							&& mapTexture[i][j] != MapTextureID.CRATERE && mapAltitude[i][j] > ( mapAltitude[highest_pX][highest_pY] - hauteur_neige) && mapAltitude[i][j] > -5) {
						nextMapTexture[i][j] = MapTextureID.SNOW;
						nb_of_snow++;
					}
				}
			}
			hauteur_neige += 0.5;
		}
	}
	
	
	public void evaporation() {
		if(nb_of_snow <= 0) {
			hauteur_neige = 0;
		}
		for(int i = 0; i < mapAltitude.length; i++) {
			for(int j = 0; j < mapAltitude[i].length; j++) {
				if(mapTexture[i][j] == MapTextureID.WATER && mapAltitude[i][j] > (hauteur_pluie - 15)) 
					nextMapTexture[i][j] = getTexture(i,j);
			}
		}
		if(hauteur_pluie == 0)
			hauteur_pluie = 0;
		else
			hauteur_pluie-= 0.05;
	}
	
	
	
	public void frost(int x, int y) {
		if(Math.random()<0.6 && (nb_of_ground_tiles_around(x,y) != 0 || nb_of_ice_tiles_around(x,y) != 0))
			nextMapTexture[x][y] = MapTextureID.ICE;
	}
	
	public void defrost(int x, int y) {
		if(mapTexture[x][y] == MapTextureID.ICE && Math.random()<0.6 && (nb_of_ground_tiles_around(x,y) != 0 || nb_of_water_tiles_around(x,y) != 0))
			nextMapTexture[x][y] = MapTextureID.WATER;
	}
	
	public void remove_snow(int x, int y) {
		if(mapTexture[x][y] == MapTextureID.SNOW) {
			if( Ecosystem.season == Ecosystem.SUMMER || (Math.random() < 0.3 && Ecosystem.season != Ecosystem.WINTER && Ecosystem.season != Ecosystem.SUMMER)) {
				nextMapTexture[x][y] = getTexture(x,y);
				nb_of_snow--;
				if(Ecosystem.season == Ecosystem.SUMMER)
					hauteur_neige = 0;
			}
		}
	}
	
	

	public int cooling_lava(int x, int y) {
		if(Math.random()<0.6 && (nb_of_ground_tiles_around(x,y) != 0 || nb_of_water_tiles_around(x,y) != 0 || nb_of_mountain_tiles_around(x,y) != 0 || nb_of_cold_lava_tiles_around(x,y) != 0))
			return MapTextureID.COLD_LAVA;
		else return MapTextureID.LAVA;
	}
	
	public int cleaning_cold_lava(int x, int y) {
		if(lave_coule == false && cpt_pluie > 100 && Math.random()<0.2)
			return getTexture(x,y);
		else return MapTextureID.COLD_LAVA;
	}
	
	
		
	public void ecoulement_lave() {	
	
		if(lave_coule) {
			if((cpt_lave%4 == 0)) {
				int rand = (int)(Math.random()*4);
				for(int x = 0; x < mapTexture.length; x++) {
					for(int y = 0; y < mapTexture[x].length; y++) {
						int up = (x - 1 + mapHeight)%mapHeight;
						int down = (x + 1 + mapHeight)%mapHeight;
						int left = (y - 1 + mapWidth)%mapWidth;
						int right = (y + 1 + mapWidth)%mapWidth;
						if(mapTexture[x][y] == MapTextureID.LAVA || mapTexture[x][y] == MapTextureID.CRATERE) {
							if(rand == LAVE_DROITE) {
								if(mapAltitude[up][right] < mapAltitude[x][y] && mapTexture[up][right] != MapTextureID.WATER)
									nextMapTexture[up][right] = MapTextureID.LAVA;
								if(mapAltitude[down][right] < mapAltitude[x][y] && mapTexture[down][right] != MapTextureID.WATER)
									nextMapTexture[down][right] = MapTextureID.LAVA;
								if(mapAltitude[x][right] < mapAltitude[x][y] && mapTexture[x][right] != MapTextureID.WATER)
									nextMapTexture[x][right] = MapTextureID.LAVA;
							}else if(rand == LAVE_BAS) {
								if(mapAltitude[down][y] < mapAltitude[x][y] && mapTexture[down][y] != MapTextureID.WATER)
									nextMapTexture[down][y] = MapTextureID.LAVA;
								if(mapAltitude[down][left] < mapAltitude[x][y] && mapTexture[down][left] != MapTextureID.WATER)
									nextMapTexture[down][left] = MapTextureID.LAVA;
								if(mapAltitude[down][right] < mapAltitude[x][y] && mapTexture[down][right] != MapTextureID.WATER)
									nextMapTexture[down][right] = MapTextureID.LAVA;
							}else if(rand == LAVE_GAUCHE) {
								if(mapAltitude[x][left] < mapAltitude[x][y] && mapTexture[x][left] != MapTextureID.WATER)
									nextMapTexture[x][left] = MapTextureID.LAVA;
								if(mapAltitude[up][left] < mapAltitude[x][y] && mapTexture[up][left] != MapTextureID.WATER)
									nextMapTexture[up][left] = MapTextureID.LAVA;
								if(mapAltitude[down][left] < mapAltitude[x][y] && mapTexture[down][left] != MapTextureID.WATER)
									nextMapTexture[down][left] = MapTextureID.LAVA;
							}else if(rand == LAVE_HAUT) {
								if(mapAltitude[up][left] < mapAltitude[x][y] && mapTexture[up][left] != MapTextureID.WATER)
									nextMapTexture[up][left] = MapTextureID.LAVA;
								if(mapAltitude[up][right] < mapAltitude[x][y] && mapTexture[up][right] != MapTextureID.WATER)
									nextMapTexture[up][right] = MapTextureID.LAVA;
								nextMapTexture[down][y] = MapTextureID.LAVA;
								if(mapAltitude[up][y] < mapAltitude[x][y] && mapTexture[up][y] != MapTextureID.WATER)
									nextMapTexture[up][y] = MapTextureID.LAVA;
							}
						}
					}
				}
			}
		}else {
			for(int i = 0; i < mapTexture.length; i++) {
				for(int j = 0; j < mapTexture[i].length; j++)
					if(mapTexture[i][j] == MapTextureID.LAVA)
						nextMapTexture[i][j] = MapTextureID.COLD_LAVA;
			}
		}
		cpt_lave++;
	}

		

	private void updateMapEntities() {
		// same as updateMapTexture
		for (int x = 0; x < this.mapEntities.length; x++) {
			for (int y = 0; y < this.mapEntities[0].length; y++) {
				if (mapEntities[x][y] != 0) {
					int state = mapEntities[x][y];
					nextMapEntities[x][y] = updateStateEntities(state, x, y);
				}

			}
		}

		randomWildFire();
		for (int x = 0; x < this.mapEntities.length; x++) {
			for (int y = 0; y < this.mapEntities[0].length; y++) {
				mapEntities[x][y] = nextMapEntities[x][y];
			}
		}

	}

	private int updateStateEntities(int state, int x, int y) {
		int updatedState = state;
		switch (state) {
		case MapEntitiesID.NOTHING:
			// can become a tree if lucky and if we are on the ground and if there is no one
			// on the ground
			updatedState = MapEntitiesID.NOTHING;
			if (!agentAt(x,y) && Ecosystem.season == Ecosystem.SPRING  && is_raining == false && Math.random() < Ecosystem.P_TREE_BIRTH && mapTexture[x][y] == MapTextureID.GROUND && nb_of_water_tiles_around(x,y) == 0) {

				updatedState = MapEntitiesID.BABY_GREEN_TREE;
			}
			break;
		case MapEntitiesID.BABY_GREEN_TREE:
			updatedState = MapEntitiesID.YOUNG_GREEN_TREE;
			break;
		case MapEntitiesID.YOUNG_GREEN_TREE:
			updatedState = MapEntitiesID.GREEN_TREE;
			break;
		case MapEntitiesID.GREEN_TREE:
			updatedState = updateTree(x, y);
			break;
		case MapEntitiesID.BURNING_TREE:
			updatedState = updateBurningTree(x, y);
			break;
		case MapEntitiesID.ASHES:
			updatedState = updateAshes(x, y);
			break;
		default:
			updatedState = state;
			break;
		}
		return updatedState;
	}

	private boolean agentAt(int x, int y) {
		for(int i = 0; i < Ecosystem.agents.size(); i++) {
			Agent agent =  Ecosystem.agents.get(i);
			if(agent.cur_pos.equals(new Position(x, y))){
				return true;
			}
		}
	
		return false;
	}

	private int updateAshes(int x, int y) {
		// the ashes disappear
		return MapEntitiesID.NOTHING;
	}

	private int updateBurningTree(int x, int y) {
		// the burning tree finish burning... it becomes ashes
		return MapEntitiesID.ASHES;
	}

	
	public int updateTree(int x, int y) {
		int up = (x-1 + mapHeight)%mapHeight;
		int down = (x+1 + mapHeight)%mapHeight;
		int left = (y-1 + mapWidth)%mapWidth;
		int right = (y+1 + mapWidth)%mapWidth;
		
		boolean burn_up = (mapEntities[up][y] == MapEntitiesID.BURNING_TREE ||nextMapTexture[up][y] == MapTextureID.LAVA);
		boolean burn_down = (mapEntities[down][y] == MapEntitiesID.BURNING_TREE || nextMapTexture[down][y] == MapTextureID.LAVA);
		boolean burn_left = (mapEntities[x][left] == MapEntitiesID.BURNING_TREE || nextMapTexture[x][left] == MapTextureID.LAVA);
		boolean burn_right = (mapEntities[x][right] == MapEntitiesID.BURNING_TREE || nextMapTexture[x][right] == MapTextureID.LAVA);
		boolean burn_up_left = (mapEntities[up][left] == MapEntitiesID.BURNING_TREE || nextMapTexture[up][left] == MapTextureID.LAVA);
		boolean burn_up_right = (mapEntities[up][right] == MapEntitiesID.BURNING_TREE || nextMapTexture[up][right] == MapTextureID.LAVA);
		boolean burn_down_left = (mapEntities[down][left] == MapEntitiesID.BURNING_TREE || nextMapTexture[down][left] == MapTextureID.LAVA);
		boolean burn_down_right = (mapEntities[down][right] == MapEntitiesID.BURNING_TREE || nextMapTexture[down][right] == MapTextureID.LAVA);
		
		if(this.mapTexture[x][y] == MapTextureID.LAVA)
			return MapEntitiesID.BURNING_TREE;
		else if(is_raining == false &&(burn_up || burn_down || burn_left || burn_right || burn_down_left || burn_down_right || burn_up_left || burn_up_right || Math.random() < Ecosystem.P_SET_ON_FIRE))
			return MapEntitiesID.BURNING_TREE;
		else 
			return MapEntitiesID.GREEN_TREE;
	}

	public void randomWildFire() {
		int randX = (int) (Math.random() * mapEntities.length);
		int randY = (int) (Math.random() * mapEntities[0].length);
		if (mapEntities[randX][randY] == MapEntitiesID.GREEN_TREE && is_raining == false && Math.random() < Ecosystem.P_SET_ON_FIRE)
			nextMapEntities[randX][randY] = MapEntitiesID.BURNING_TREE;
	}


	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//// 										GETTERS AND SETTERS 										////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public double[][] getMapAltitude() {
		return mapAltitude;
	}

	public int[][] getMapTexture() {
		return mapTexture;
	}

	public void setMapTexture(int[][] mapTexture) {
		this.mapTexture = mapTexture;
	}

	public int[][] getMapEntities() {
		return mapEntities;
	}

	

	public int[][] getNextMapEntities() {
		return nextMapEntities;
	}

	

	public int getWidth() {
		return mapWidth;
	}


	public int getHeight() {
		return mapHeight;
	}


	public int getMapTextureCase(int x, int y) {
		return mapTexture[x][y];
	}

	public void setTree(int i, int j) {
		
		if (mapTexture[i][j] == MapTextureID.GROUND)
			mapEntities[i][j] = MapEntitiesID.GREEN_TREE;

	}

	public int nb_of_water_tiles_around(int x, int y) {
		int nb_of_water_tiles = 0;
		if (getTextureLeft(x, y) == MapTextureID.WATER) // left
			nb_of_water_tiles++;
		if (getTextureRight(x, y) == MapTextureID.WATER) // Right
			nb_of_water_tiles++;
		if (getTextureUpLeft(x, y) == MapTextureID.WATER) // upleft
			nb_of_water_tiles++;
		if (getTextureUpRight(x, y) == MapTextureID.WATER) // upRight
			nb_of_water_tiles++;
		if (getTextureDownLeft(x, y) == MapTextureID.WATER) // downleft
			nb_of_water_tiles++;
		if (getTextureDownRight(x, y) == MapTextureID.WATER) // downRight
			nb_of_water_tiles++;
		if (getTextureUp(x, y) == MapTextureID.WATER) // up
			nb_of_water_tiles++;
		if (getTextureDown(x, y) == MapTextureID.WATER) // down
			nb_of_water_tiles++;
		return nb_of_water_tiles;
	}

	public int nb_of_ground_tiles_around(int x, int y) {
		int nb_of_ground_tiles = 0;
		if (getTextureLeft(x, y) == MapTextureID.GROUND)
			nb_of_ground_tiles++;
		if (getTextureRight(x, y) == MapTextureID.GROUND)
			nb_of_ground_tiles++;
		if (getTextureUpLeft(x, y) == MapTextureID.GROUND)
			nb_of_ground_tiles++;
		if (getTextureUpRight(x, y) == MapTextureID.GROUND)
			nb_of_ground_tiles++;
		if (getTextureDownLeft(x, y) == MapTextureID.GROUND)
			nb_of_ground_tiles++;
		if (getTextureDownRight(x, y) == MapTextureID.GROUND)
			nb_of_ground_tiles++;
		if (getTextureUp(x, y) == MapTextureID.GROUND)
			nb_of_ground_tiles++;
		if (getTextureDown(x, y) == MapTextureID.GROUND)
			nb_of_ground_tiles++;
		return nb_of_ground_tiles;
	}
	
	public int nb_of_ice_tiles_around(int x, int y) {
		int nb_of_ice_tiles = 0;
		if (getTextureLeft(x, y) == MapTextureID.ICE)
			nb_of_ice_tiles++;
		if (getTextureRight(x, y) == MapTextureID.ICE)
			nb_of_ice_tiles++;
		if (getTextureUpLeft(x, y) == MapTextureID.ICE)
			nb_of_ice_tiles++;
		if (getTextureUpRight(x, y) == MapTextureID.ICE)
			nb_of_ice_tiles++;
		if (getTextureDownLeft(x, y) == MapTextureID.ICE)
			nb_of_ice_tiles++;
		if (getTextureDownRight(x, y) == MapTextureID.ICE)
			nb_of_ice_tiles++;
		if (getTextureUp(x, y) == MapTextureID.ICE)
			nb_of_ice_tiles++;
		if (getTextureDown(x, y) == MapTextureID.ICE)
			nb_of_ice_tiles++;
		return nb_of_ice_tiles;
	}
	
	public int nb_of_mountain_tiles_around(int x, int y) {
		int nb_of_mountain_tiles = 0;
		if (getTextureLeft(x, y) == MapTextureID.MOUNTAIN)
			nb_of_mountain_tiles++;
		if (getTextureRight(x, y) == MapTextureID.MOUNTAIN)
			nb_of_mountain_tiles++;
		if (getTextureUpLeft(x, y) == MapTextureID.MOUNTAIN)
			nb_of_mountain_tiles++;
		if (getTextureUpRight(x, y) == MapTextureID.MOUNTAIN)
			nb_of_mountain_tiles++;
		if (getTextureDownLeft(x, y) == MapTextureID.MOUNTAIN)
			nb_of_mountain_tiles++;
		if (getTextureDownRight(x, y) == MapTextureID.MOUNTAIN)
			nb_of_mountain_tiles++;
		if (getTextureUp(x, y) == MapTextureID.MOUNTAIN)
			nb_of_mountain_tiles++;
		if (getTextureDown(x, y) == MapTextureID.MOUNTAIN)
			nb_of_mountain_tiles++;
		return nb_of_mountain_tiles;
	}
	
	public int nb_of_cold_lava_tiles_around(int x, int y) {
		int nb_of_cold_lava_tiles = 0;
		if (getTextureLeft(x, y) == MapTextureID.COLD_LAVA)
			nb_of_cold_lava_tiles++;
		if (getTextureRight(x, y) == MapTextureID.COLD_LAVA)
			nb_of_cold_lava_tiles++;
		if (getTextureUpLeft(x, y) == MapTextureID.COLD_LAVA)
			nb_of_cold_lava_tiles++;
		if (getTextureUpRight(x, y) == MapTextureID.COLD_LAVA)
			nb_of_cold_lava_tiles++;
		if (getTextureDownLeft(x, y) == MapTextureID.COLD_LAVA)
			nb_of_cold_lava_tiles++;
		if (getTextureDownRight(x, y) == MapTextureID.COLD_LAVA)
			nb_of_cold_lava_tiles++;
		if (getTextureUp(x, y) == MapTextureID.COLD_LAVA)
			nb_of_cold_lava_tiles++;
		if (getTextureDown(x, y) == MapTextureID.COLD_LAVA)
			nb_of_cold_lava_tiles++;
		return nb_of_cold_lava_tiles;
	}
	

	public int nb_of_snow_tiles_around(int x, int y) {
		int nb_of_snow_tiles = 0;
		if (getTextureLeft(x, y) == MapTextureID.SNOW)
			nb_of_snow_tiles++;
		if (getTextureRight(x, y) == MapTextureID.SNOW)
			nb_of_snow_tiles++;
		if (getTextureUpLeft(x, y) == MapTextureID.SNOW)
			nb_of_snow_tiles++;
		if (getTextureUpRight(x, y) == MapTextureID.SNOW)
			nb_of_snow_tiles++;
		if (getTextureDownLeft(x, y) == MapTextureID.SNOW)
			nb_of_snow_tiles++;
		if (getTextureDownRight(x, y) == MapTextureID.SNOW)
			nb_of_snow_tiles++;
		if (getTextureUp(x, y) == MapTextureID.SNOW)
			nb_of_snow_tiles++;
		if (getTextureDown(x, y) == MapTextureID.SNOW)
			nb_of_snow_tiles++;
		return nb_of_snow_tiles;
	}
	
	
	public boolean is_near_cratere(int x, int y) {
		return (getTextureRight(x,y) == MapTextureID.CRATERE || getTextureDownRight(x,y) == MapTextureID.CRATERE || getTextureDown(x,y) == MapTextureID.CRATERE || getTextureDownLeft(x,y) == MapTextureID.CRATERE
				|| getTextureLeft(x,y) == MapTextureID.CRATERE || getTextureUpLeft(x,y) == MapTextureID.CRATERE || getTextureUp(x,y) == MapTextureID.CRATERE || getTextureUpRight(x,y) == MapTextureID.CRATERE);
	}
	
	
	// GETTERS TEXTURE rewritten

	public int getTextureRight(int x, int y) {
		return mapTexture[x][(y + 1 + mapWidth) % mapWidth];
	}

	public int getTextureLeft(int x, int y) {
		return mapTexture[x][(y - 1 + mapWidth) % mapWidth];
	}

	public int getTextureDown(int x, int y) {
		return mapTexture[(x + 1 + mapHeight) % mapHeight][y];
	}

	public int getTextureUp(int x, int y) {
		return mapTexture[(x - 1 + mapHeight) % mapHeight][y];
	}

	public int getTextureUpRight(int x, int y) {
		return mapTexture[(x - 1 + mapHeight) % mapHeight][(y + 1 + mapWidth) % mapWidth];
	}

	public int getTextureUpLeft(int x, int y) {
		return mapTexture[(x - 1 + mapHeight) % mapHeight][(y - 1 + mapWidth) % mapWidth];
	}

	public int getTextureDownRight(int x, int y) {
		return mapTexture[(x + 1 + mapHeight) % mapHeight][(y + 1 + mapWidth) % mapWidth];
	}

	public int getTextureDownLeft(int x, int y) {
		return mapTexture[(x + 1 + mapHeight) % mapHeight][(y - 1 + mapWidth) % mapWidth];
	}
	
	
	
	
	//GETTERS ALTITUDE rewritten
	
	public ArrayList<Position> getAccessiblePositionMiniRobot(){
		ArrayList<Position> array = new ArrayList<Position>();
		for (int x = 0; x < Ecosystem.mapWidth; x++) {
			for (int y = 0; y < Ecosystem.mapHeight; y++) {
				if(this.mapEntities[x][y] == MapEntitiesID.NOTHING && this.mapTexture[x][y] == MapTextureID.GROUND) {
					array.add(new Position(x, y));
				}
			}
		}
		return array;
	}
	public double getAltitude(int x, int y) {
		return mapAltitude[x][y];
	}

	public double getAltitudeRight(int x, int y) {
		return mapAltitude[x][(y + 1 + mapWidth) % mapWidth];
	}

	public double getAltitudeLeft(int x, int y) {
		return mapAltitude[x][(y - 1 + mapWidth) % mapWidth];
	}

	public double getAltitudeDown(int x, int y) {
		return mapAltitude[(x + 1 + mapHeight) % mapHeight][y];
	}

	public double getAltitudeUp(int x, int y) {
		return mapAltitude[(x - 1 + mapHeight) % mapHeight][y];
	}

	public double getAltitudeUpRight(int x, int y) {
		return mapAltitude[(x - 1 + mapHeight) % mapHeight][(y + 1 + mapWidth) % mapWidth];
	}

	public double getAltitudeDownRight(int x, int y) {
		return mapAltitude[(x + 1 + mapHeight) % mapHeight][(y + 1 + mapWidth) % mapWidth];
	}

	public double getAltitudeUpLeft(int x, int y) {
		return mapAltitude[(x - 1 + mapHeight) % mapHeight][(y - 1 + mapWidth) % mapWidth];
	}

	public double getAltitudeDownLeft(int x, int y) {
		return mapAltitude[(x + 1 + mapHeight) % mapHeight][(y - 1 + mapWidth) % mapWidth];
	}
	
	public int getHighest_pX() {
		return highest_pX;
	}
	
	public int getHighest_pY() {
		return highest_pY;
	}
	
	
	public int getEntitiesUp(int x, int y) {
		return mapEntities[x][(y - 1 + mapWidth) % mapWidth];
	}

	public int getEntitiesDown(int x, int y) {
		return mapEntities[x][(y + 1 + mapWidth) % mapWidth];
	}

	public int getEntitiesLeft(int x, int y) {
		return mapEntities[(x - 1 + mapHeight) % mapHeight][y];
	}

	public int getEntitiesRight(int x, int y) {
		return mapEntities[(x + 1 + mapHeight) % mapHeight][y];
	}

	public int getEntitiesUpRight(int x, int y) {
		return mapEntities[(x + 1 + mapHeight) % mapHeight][(y - 1 + mapWidth) % mapWidth];
	}

	public int getEntitiesDownRight(int x, int y) {
		return mapEntities[(x + 1 + mapHeight) % mapHeight][(y + 1 + mapWidth) % mapWidth];
	}

	public int getEntitiesUpLeft(int x, int y) {
		return mapEntities[(x - 1 + mapHeight) % mapHeight][(y - 1 + mapWidth) % mapWidth];
	}

	public int getEntitiesDownLeft(int x, int y) {
		return mapEntities[(x - 1 + mapHeight) % mapHeight][(y + 1 + mapWidth) % mapWidth];
	}
	
	
	
	public Position get_lowest_point(int x, int y) {
		double lowest_alt = 1000;
		int lowest_pX = x;
		int lowest_pY = y;
		if(getAltitudeLeft(x,y) < lowest_alt) {
			lowest_pX = x;
			lowest_pY = (y-1 + mapWidth) % mapWidth;
			lowest_alt = getAltitudeLeft(x,y);
		}
		if(getAltitudeDownLeft(x,y) < lowest_alt) {
			lowest_pX =  (x+1 + mapHeight) % mapHeight;
			lowest_pY =  (y-1 + mapWidth) % mapWidth;
			lowest_alt = getAltitudeDownLeft(x,y);
		}
		if(getAltitudeDown(x,y) < lowest_alt) {
			lowest_pX = (x+1 + mapHeight) % mapHeight;
			lowest_pY = y;
			lowest_alt = getAltitudeDown(x,y);
		}
		if(getAltitudeDownRight(x,y) < lowest_alt) {
			lowest_pX = (x+1 + mapHeight) % mapHeight;
			lowest_pY = (y+1 + mapWidth) % mapWidth;
			lowest_alt = getAltitudeDownRight(x,y);
		}
		if(getAltitudeRight(x,y) < lowest_alt) {
			lowest_pX = x;
			lowest_pY = (y+1 + mapWidth) % mapWidth;
			lowest_alt = getAltitudeRight(x,y);
		}
		if(getAltitudeUpRight(x,y) < lowest_alt) {
			lowest_pX = (x-1 + mapHeight) % mapHeight;
			lowest_pY =  (y+1 + mapWidth) % mapWidth;
			lowest_alt = getAltitudeUpRight(x,y);
		}
		if(getAltitudeUp(x,y) < lowest_alt) {
			lowest_pX = (x-1 + mapHeight) % mapHeight;
			lowest_pY = y;
			lowest_alt = getAltitudeUp(x,y);
		}
		else if(getAltitudeUpLeft(x,y) < lowest_alt){
			lowest_pX = (x-1 + mapHeight) % mapHeight;;
			lowest_pY =  (y-1 + mapWidth) % mapWidth;
			lowest_alt = getAltitudeUpLeft(x,y);
		}
		
		return new Position(lowest_pX, lowest_pY);
	}


	public void affichageBruitPerlin() {
		for (int x = 0; x < mapWidth; x++) { 
			for (int y = 0; y < mapHeight; y++)
			  System.out.print(String.format("%.2f ", mapAltitude[x][y]));
			
		}
	}
}
