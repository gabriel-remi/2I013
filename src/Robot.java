
public class Robot extends Agent {
	int detection_area = 10;
	
	public final int NB_MAX = 5;
	public static int generated = 0;
	public Robot(int x, int y) {
		// TODO Auto-generated constructor stub
		super(x, y,10000, "Robot");
		this.level = 99;

	}

	public void update() {

		//System.out.println(this);
		if(this.detectPlayer()) {
			Player target = this.getDetectedPlayer();
			this.stayAtCurrentPosition();
			
			this.attack(target);
		}
		else {
			if(this.generated < this.NB_MAX) {
				Ecosystem.tmp.add(new MiniRobot(this.cur_pos.x, (this.cur_pos.y + 1 ) % Ecosystem.mapHeight));
				this.generated++;
			}
			else {
				doNothing();
			}
		}
		

	}

	private Player getDetectedPlayer() {
		
		for (Agent agent : Ecosystem.agents) {
			if (agent instanceof Player && agent.life > 0 && distance(this, (Player) agent) < this.detection_area) {
				return (Player)agent;
			}
		}
		return null;
	}

	private boolean detectPlayer() {
		if (Ecosystem.agents.size() == 0) {
			return false;
		}
		for (Agent agent : Ecosystem.agents) {
			if (agent instanceof Player && agent.life > 0 && distance(this, (Player) agent) < this.detection_area) {
				return true;
			}
		}
		
		return false;
	}



	private void stayAtCurrentPosition() {
		this.next_pos.x = this.cur_pos.x;
		this.next_pos.y = this.cur_pos.y;
		
	}

	private void doNothing() {
		// TODO Auto-generated method stub
		
	}


	private int distance(Robot robot, Player player) {
		// TODO Auto-generated method stub
		int map_x2 = robot.cur_pos.x;
		int map_y2 = robot.cur_pos.y;
		int map_x1 = player.cur_pos.x;
		int map_y1 = player.cur_pos.y;
		return (int) Math.sqrt((map_y2 - map_y1) * (map_y2 - map_y1) + (map_x2 - map_x1) * (map_x2 - map_x1));

	}

	private void attack(Player player) {
	
		player.life -= 999999;
		if(player.life < 0) {
			player.life = 0;
		}
	}


}
