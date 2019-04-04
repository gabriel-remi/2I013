import java.util.ArrayList;

/**
 * @author 3679695
 *
 */
public class MiniRobot extends Agent {
	int detection_area = 4;
	Position random_pos = null;
	private int timeSearch;

	public MiniRobot(int x, int y) {
		// TODO Auto-generated constructor stub
		super(x, y,100, "MiniRobot");
	}

	public void update() {

		if (isDead()) {
			//System.out.println("MiniRobot is Dead");
			
		} else {

			if (canDie()) {
				//System.out.println("MiniRobot is dying because of water or lava");
				this.die();
			} else {

				if (this.detectPlayer()) {
					
					//System.out.println("MiniRobot detected a player");
					Player player = this.getDetectedPlayer();

					if (this.nextTo(player)) {
						//System.out.println("MiniRobot is next to a player");
						this.stayAtCurrentPosition();
						this.attack(player);

					} else {

						this.path = PathFinder.pathfinder.getPath(this.cur_pos, player.cur_pos);
						PathFinder.pathfinder.clear();
						if (existPathTo(player.cur_pos)) {
							//System.out.println("MiniRobot is walking towards a player");
							this.walkTo(player);

						} else {

							if (random_pos == null || this.cur_pos.equals(this.random_pos)) {
								random_pos = new Position(0, 0);
							}
							if (existPathTo(random_pos)) {
								//System.out.println("MiniRobot walks toward a target");
								this.walkTo(random_pos);

							} else {
								//System.out.println("MiniRobot walks randomly");
								this.walkRandomly();

							}

						}

					}
				} else {

					if (random_pos == null || this.cur_pos.equals(this.random_pos) || this.path == null
							|| this.path.size() == 0 || timeSearch < 0) {
						random_pos = new Position();
						timeSearch = 30;
					}
					if (existPathTo(random_pos)) {
						timeSearch--;
						this.walkTo(random_pos);

					} else {

						this.walkRandomly();

					}

				}
			}
		}

		// //System.out.println(this);
		// if(this.life <= 0) {
		// Robot.generated--;
		// }
		// Player playerNextTo = getNextToPlayer();
		//
		// if(playerNextTo != null) {
		// this.next_pos.x = this.cur_pos.x;
		// this.next_pos.y = this.cur_pos.y;
		// attack(playerNextTo);
		// if(playerNextTo.life <= 0) {
		// this.level++;
		// }
		//
		// }
		// else {
		// if(target == null || path.size() == 0 || timeSearch == 0) {
		//
		// target = new Position((int)(Math.random() *
		// Ecosystem.mapWidth),(int)(Math.random() * Ecosystem.mapHeight));
		// ////System.out.println(target.x +", " + target.y);
		// timeSearch = 30;
		// }
		// timeSearch--;
		// //finding the path to the object/agent
		// path = PathFinder.pathfinder.getPath(this.cur_pos, this.target);
		//
		// //the path has to be cleared because we used it
		// PathFinder.pathfinder.clear();
		// //if we found a path to the target then we set the next position we have to
		// go
		// //and if the next position we are going to go is the target then we unset the
		// target
		// if (!path.isEmpty()) {
		// Position next = path.peekLast();
		// this.next_pos.x = next.x;
		// this.next_pos.y = next.y;
		// if (next_pos.x == target.x && next_pos.y == target.y) {
		// target = null;
		// }
		//
		// }
		// }

	}

	private void walkTo(Player player) {
		Position next = this.path.peekLast();
		if (player.next_pos.equals(next)) {
			return;
		}
		this.next_pos.x = next.x;
		this.next_pos.y = next.y;

	}

	private void walkTo(Position pos) {
		Position next = this.path.peekLast();

		this.next_pos.x = next.x;
		this.next_pos.y = next.y;

	}

	private boolean existPathTo(Position item) {
		// TODO Auto-generated method stub
		this.path = PathFinder.pathfinder.getPath(this.cur_pos, item);
		PathFinder.pathfinder.clear();
		return this.path != null && this.path.size() > 0;
	}

	private void stayAtCurrentPosition() {
		this.next_pos.x = this.cur_pos.x;
		this.next_pos.y = this.cur_pos.y;

	}

	private void walkRandomly() {
		this.next_pos.x = cur_pos.x;
		this.next_pos.y = cur_pos.y;
		ArrayList<Position> possible_dir = new ArrayList<Position>();
		if (canMoveUp()) {
			possible_dir.add(this.cur_pos.getAbove());
		}
		if (canMoveDown()) {
			possible_dir.add(this.cur_pos.getDown());
		}
		if (canMoveLeft()) {
			possible_dir.add(this.cur_pos.getLeft());
		}
		if (canMoveRight()) {
			possible_dir.add(this.cur_pos.getRight());
		}

		if (possible_dir.isEmpty()) {
			return;
		}
		Position rand_next_pos = possible_dir.get((int) (Math.random() * possible_dir.size()));
		this.next_pos.x = rand_next_pos.x;
		this.next_pos.y = rand_next_pos.y;

	}

	private boolean canMoveRight() {

		return (Ecosystem.map.getTextureRight(this.cur_pos.x, this.cur_pos.y) == MapTextureID.GROUND
				|| Ecosystem.map.getTextureRight(this.cur_pos.x, this.cur_pos.y) == MapTextureID.SPAWN)
				&& (Ecosystem.map.getEntitiesRight(this.cur_pos.x, this.cur_pos.y) == MapEntitiesID.NOTHING);
	}

	private boolean canMoveLeft() {
		// TODO Auto-generated method stubfez
		return (Ecosystem.map.getTextureLeft(this.cur_pos.x, this.cur_pos.y) == MapTextureID.GROUND
				|| Ecosystem.map.getTextureLeft(this.cur_pos.x, this.cur_pos.y) == MapTextureID.SPAWN)
				&& (Ecosystem.map.getEntitiesLeft(this.cur_pos.x, this.cur_pos.y) == MapEntitiesID.NOTHING);
	}

	private boolean canMoveDown() {
		// TODO Auto-generated method stubf
		return (Ecosystem.map.getTextureDown(this.cur_pos.x, this.cur_pos.y) == MapTextureID.GROUND
				|| Ecosystem.map.getTextureDown(this.cur_pos.x, this.cur_pos.y) == MapTextureID.SPAWN)
				&& (Ecosystem.map.getEntitiesDown(this.cur_pos.x, this.cur_pos.y) == MapEntitiesID.NOTHING);
	}

	private boolean canMoveUp() {
		// TODO Auto-generated method stub
		return (Ecosystem.map.getTextureDown(this.cur_pos.x, this.cur_pos.y) == MapTextureID.GROUND
				|| Ecosystem.map.getTextureDown(this.cur_pos.x, this.cur_pos.y) == MapTextureID.SPAWN)
				&& (Ecosystem.map.getEntitiesUp(this.cur_pos.x, this.cur_pos.y) == MapEntitiesID.NOTHING);
	}

	private boolean nextTo(Player player) {

		return this.cur_pos.isAbove(player.cur_pos) || this.cur_pos.isDown(player.cur_pos)
				|| this.cur_pos.isAtTheLeftOf(player.cur_pos) || this.cur_pos.isAtTheRightOf(player.cur_pos);
	}

	private void die() {
		// TODO Auto-generated method stub
		this.life = 0;
	}

	private boolean canDie() {

		return Ecosystem.map.getMapTexture()[this.cur_pos.x][this.cur_pos.y] == MapTextureID.LAVA
				|| Ecosystem.map.getMapTexture()[this.cur_pos.x][this.cur_pos.y] == MapTextureID.WATER;
	}

	


	private boolean isDead() {

		return this.life <= 0;
	}

	private Player getDetectedPlayer() {

		for (Agent agent : Ecosystem.agents) {
			if (agent instanceof Player && agent.life > 0 && distance(this, (Player) agent) < this.detection_area) {
				return (Player) agent;
			}
		}
		return null;
	}

	private boolean detectPlayer() {
		if (Ecosystem.agents.size() == 0) {
			return false;
		}
		for (Agent agent : Ecosystem.agents) {
			if (agent instanceof Player && agent.life > 0 && !agent.in(Zone.securityZone)
					&& distance(this, (Player) agent) < this.detection_area) {
				return true;
			}
		}

		return false;
	}

	private int distance(MiniRobot robot, Player player) {
		// TODO Auto-generated method stub
		int map_x2 = robot.cur_pos.x;
		int map_y2 = robot.cur_pos.y;
		int map_x1 = player.cur_pos.x;
		int map_y1 = player.cur_pos.y;
		return (int) Math.sqrt((map_y2 - map_y1) * (map_y2 - map_y1) + (map_x2 - map_x1) * (map_x2 - map_x1));

	}

	private void attack(Player player) {
		player.life--;
		if(player.life < 0) {
			player.life = 0;
		}
	}

	// private void attack(Player player) {
	// player.life--;
	//
	// }
	//
	// protected Player getNextToPlayer() {
	// for (Agent agent : Ecosystem.agents) {
	// boolean up = (agent.cur_pos.x == this.cur_pos.x
	// && this.moduloHeight(agent.cur_pos.y - 1) == this.cur_pos.y);
	// boolean down = (agent.cur_pos.x == this.cur_pos.x
	// && this.moduloHeight(agent.cur_pos.y + 1) == this.cur_pos.y);
	// boolean right = (this.moduloWidth(agent.cur_pos.x + 1)== this.cur_pos.x
	// && (agent.cur_pos.y ) == this.cur_pos.y);
	// boolean left = (this.moduloWidth(agent.cur_pos.x - 1)== this.cur_pos.x
	// && (agent.cur_pos.y ) == this.cur_pos.y);
	// if(agent instanceof Player &&( up || down || right || left)) {
	// return (Player)agent;
	// }
	//
	// }
	// return null;
	// }

}
