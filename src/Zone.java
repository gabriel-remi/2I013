import java.awt.Rectangle;
import java.util.ArrayList;

public class Zone extends Rectangle{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static Zone healingZone = new Zone(0 , 0, Ecosystem.mapWidth / 4 , Ecosystem.mapWidth / 4);
	public static Zone securityZone = new Zone(Player.revivingPosition.x - Ecosystem.mapWidth / 14, Player.revivingPosition.y - Ecosystem.mapWidth / 14, Ecosystem.mapWidth / 7 , Ecosystem.mapWidth / 7);

	public int animationTime = 0;
	public boolean increaseTime = true;
	public Zone(int x, int y, int width, int height) {
		super(x, y, width, height);
		// TODO Auto-generated constructor stub
	}

	
	public void updateAnimationTime() {
		// TODO Auto-generated method stub
		if(this.animationTime > 130) {
			this.increaseTime = false;
		}
		if(this.animationTime == 50) {
			this.increaseTime = true;
		}
		if(this.increaseTime){
			animationTime++;
		}
		else {
			this.animationTime--;
		}
		
		
	}


	public ArrayList<Position> getLegalPosition() {
		ArrayList<Position> acces = new ArrayList<Position>();
		for (int i = this.x; i < this.width; i++) {
			for (int j = this.y; j < this.height; j++) {
				if(Ecosystem.map.getMapEntities()[i][j] == MapEntitiesID.NOTHING && Ecosystem.map.getMapTexture()[i][j] == MapTextureID.GROUND) {
					acces.add(new Position(i, j));
				}
			}
		}
		
		return acces;
	}
	
	
	
	
	
}
