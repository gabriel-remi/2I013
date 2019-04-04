import java.awt.Rectangle;

public class Zone extends Rectangle{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static Zone healingZone = new Zone(Ecosystem.mapWidth / 2 , Ecosystem.mapWidth / 2, Ecosystem.mapWidth / 2 , Ecosystem.mapWidth / 2);
	public static Zone securityZone = new Zone(Ecosystem.mapWidth / 3 , Ecosystem.mapWidth / 3, Ecosystem.mapWidth / 2 , Ecosystem.mapWidth / 2);

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
	
	
	
	
	
}
