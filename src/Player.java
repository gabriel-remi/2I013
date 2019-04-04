import java.util.ArrayList;
import java.util.Random;

public class Player extends Agent {
	int detection_area = 10;
	private Position random_pos;
	private int timeSearch;

	public Player(int x, int y) {
		
		super(x, y, 100, "Lipsum");

	}

	public void update() {
		//System.out.println(this);
		if (isDead()) {

			if (canRevive()) {
				System.out.println("Player is resurecting");
				this.resurection();
			} else {
				System.out.println("player is dead and waiting to revive");
				increaseIsDeadTime();
			}
		} else {
			if (canDie()) {
				
				this.die();
				System.out.println("player died due to water or lava");
			} else {
				if(this.in(Zone.healingZone)) {
					if(this.life < this.max_life) {
						this.life++;
						System.out.println("Player is healing himself in the healing zone");
					}
					
				}
				if (this.detectMiniRobotAlive()) {
//					MiniRobot mini_robot = this.getDetectedMiniRobot();
					MiniRobot mini_robot = this.closestMiniRobot();
					if (this.nextTo(mini_robot)) {
						this.stayAtCurrentPosition();
						this.face(mini_robot);
						this.attack(mini_robot);
						System.out.println("Player attacked a minirobot");
						if(mini_robot.life <=0) {
							Robot.generated--;
							this.level++;
							System.out.println("Player killed a minirobot and his leveled up");
						}
					} else {
						if (this.healthierThan(mini_robot)) {
							if (this.strongerThan(mini_robot)) {
								this.path = PathFinder.pathfinder.getPath(this.cur_pos, mini_robot.cur_pos);
								PathFinder.pathfinder.clear();
								if (existPathTo(mini_robot)) {
									this.walkTo(mini_robot);
									System.out.println("player is walking toward a robot " + this.path);
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
									System.out.println("player is walking randomly");
								}
							} else {
								// !!!!!!
								if (existPathTo(Zone.securityZone)) {
									this.walkTo(Zone.securityZone);
									System.out.println("Player is going to the security zone");

								} else {
									if (existPathTo(Zone.healingZone)) {
										this.walkTo(Zone.healingZone);
										System.out.println("Player is going to the healing zone");
									} else {
										this.walkRandomly();
										System.out.println(7);
									}
								}

							}
						} else {
							// !!!!!
							if (existPathTo(Zone.securityZone)) {
								this.walkTo(Zone.securityZone);
								System.out.println("Player is going to the security zone");
							} else {
								if (existPathTo(Zone.healingZone)) {
									this.walkTo(Zone.healingZone);
									System.out.println("Player is going to the healing zone");
								} else {
								
									if (existPathTo(mini_robot)) {
										this.walkTo(mini_robot);
										System.out.println("player is walking toward a robot " + this.path);
									} else {
										doNothing();
										System.out.println("player do nothing");
									}
								}
							}
						}
					}

				} else {
					if (this.detectItem()) {
						// A FAIRE
						Position item = this.getDetectedItem();
						this.path = PathFinder.pathfinder.getPath(this.cur_pos, item);
						PathFinder.pathfinder.clear();
						if (existPathTo(item)) {
							this.walkTo(item);
							System.out.println("player is walking toward an item");
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
							System.out.println("player is walking randomly");
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
						System.out.println("player is walking randomly");
					}

				}
			}
		}
		//
		// if (this.life < 10) {
		// if (this.level > 0) {
		// this.level--;
		// }
		//
		// resurection();
		// return;
		// }
		// if (this.life < 0) {
		// this.life--;
		// return;
		// }
		// Monster monsterNextTo = getNextToMonster();
		// if (monsterNextTo != null && monsterNextTo.life > 0) {
		//
		// this.pos_direction.x = monsterNextTo.cur_pos.x - this.cur_pos.x;
		// this.pos_direction.y = monsterNextTo.cur_pos.y - this.cur_pos.y;
		//
		// this.next_pos.x = this.cur_pos.x;
		// this.next_pos.y = this.cur_pos.y;
		// if (monsterNextTo.life > 0) {
		// attack(monsterNextTo);
		// if (monsterNextTo.life <= 0) {
		//
		// this.level++;
		// this.max_life++;
		// this.life = this.max_life;
		// }
		//
		// }
		//
		// } else {
		//
		// if (closestMonster() == null) {
		// target = new Position((int) (Math.random() * Ecosystem.mapWidth),
		// (int) (Math.random() * Ecosystem.mapHeight));
		// } else {
		// target = closestMonster().cur_pos;
		// }
		//
		// timeSearch--;
		// // finding the path to the object/agent
		// path = PathFinder.pathfinder.getPath(this.cur_pos, this.target);
		// // the path has to be cleared because we used it
		// PathFinder.pathfinder.clear();
		//
		// // if we found a path to the target then we set the next position we have to
		// go
		// // and if the next position we are going to go is the target then we unset
		// the
		// // target
		// if (!path.isEmpty()) {
		// Position next = path.peekLast();
		// this.next_pos.x = next.x;
		// this.next_pos.y = next.y;
		// if (this.cur_pos.x == Ecosystem.mapWidth - 1 && this.next_pos.x == 0) {
		// this.pos_direction.x = 1;
		// this.pos_direction.y = 0;
		// }
		// if (this.next_pos.x == Ecosystem.mapWidth - 1 && this.cur_pos.x == 0) {
		// this.pos_direction.x = -1;
		// this.pos_direction.y = 0;
		//
		// }
		// if (this.cur_pos.y == Ecosystem.mapHeight - 1 && this.next_pos.y == 0) {
		// this.pos_direction.x = 0;
		// this.pos_direction.y = 1;
		//
		// }
		// if (this.next_pos.y == Ecosystem.mapHeight - 1 && this.cur_pos.y == 0) {
		// this.pos_direction.x = 0;
		// this.pos_direction.y = -1;
		//
		// }
		// if (this.cur_pos.x > 0 && this.cur_pos.x < Ecosystem.mapWidth - 1 &&
		// this.cur_pos.y > 0
		// && this.cur_pos.y < Ecosystem.mapHeight - 1) {
		// this.pos_direction.x = this.next_pos.x - this.cur_pos.x;
		// this.pos_direction.y = this.next_pos.y - this.cur_pos.y;
		//
		// }
		// if (next_pos.x == target.x && next_pos.y == target.y) {
		// target = null;
		// }
		//
		// }
		//
		// }

	}

	private void face(MiniRobot mini_robot) {
		this.pos_direction.x = mini_robot.cur_pos.x - this.cur_pos.x;
		this.pos_direction.y = mini_robot.cur_pos.y - this.cur_pos.y;

	}

	private void stayAtCurrentPosition() {
		this.next_pos.x = this.cur_pos.x;
		this.next_pos.y = this.cur_pos.y;

	}

	private boolean nextTo(MiniRobot mini_robot) {

		return this.cur_pos.isAbove(mini_robot.cur_pos) || this.cur_pos.isDown(mini_robot.cur_pos)
				|| this.cur_pos.isAtTheLeftOf(mini_robot.cur_pos) || this.cur_pos.isAtTheRightOf(mini_robot.cur_pos);
	}

	private void walkTo(Position item) {
		Position next = this.path.peekLast();
		this.next_pos.x = next.x;
		this.next_pos.y = next.y;
		this.updateDirection();

	}

	private boolean existPathTo(Position item) {
		
		this.path = PathFinder.pathfinder.getPath(this.cur_pos, item);
		PathFinder.pathfinder.clear();
		return this.path != null && this.path.size() > 0;
	}

	private Position getDetectedItem() {
		
		return new Position(0, 0);
	}

	private boolean detectItem() {
		
		return false;
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
		this.updateDirection();

	}

	private boolean canMoveRight() {

		return (Ecosystem.map.getTextureRight(this.cur_pos.x, this.cur_pos.y) == MapTextureID.GROUND
				|| Ecosystem.map.getTextureRight(this.cur_pos.x, this.cur_pos.y) == MapTextureID.SPAWN) &&
				(Ecosystem.map.getEntitiesRight(this.cur_pos.x, this.cur_pos.y) == MapEntitiesID.NOTHING);
	}

	private boolean canMoveLeft() {
		
		return (Ecosystem.map.getTextureLeft(this.cur_pos.x, this.cur_pos.y) == MapTextureID.GROUND
				|| Ecosystem.map.getTextureLeft(this.cur_pos.x, this.cur_pos.y) == MapTextureID.SPAWN)&&
						(Ecosystem.map.getEntitiesLeft(this.cur_pos.x, this.cur_pos.y) == MapEntitiesID.NOTHING);
	}

	private boolean canMoveDown() {
		
		return (Ecosystem.map.getTextureDown(this.cur_pos.x, this.cur_pos.y) == MapTextureID.GROUND
				|| Ecosystem.map.getTextureDown(this.cur_pos.x, this.cur_pos.y) == MapTextureID.SPAWN)&&
						(Ecosystem.map.getEntitiesDown(this.cur_pos.x, this.cur_pos.y) == MapEntitiesID.NOTHING);
	}

	private boolean canMoveUp() {
		
		return (Ecosystem.map.getTextureDown(this.cur_pos.x, this.cur_pos.y) == MapTextureID.GROUND
				|| Ecosystem.map.getTextureDown(this.cur_pos.x, this.cur_pos.y) == MapTextureID.SPAWN)&&
						(Ecosystem.map.getEntitiesUp(this.cur_pos.x, this.cur_pos.y) == MapEntitiesID.NOTHING);
	}

	private void walkTo(Zone zone) {
		
		Position next = this.path.peekLast();
		this.next_pos.x = next.x;
		this.next_pos.y = next.y;
		this.updateDirection();

	}

	private boolean existPathTo(Zone zone) {

		Random rand = new Random();

		int randomx = rand.nextInt(zone.width) + zone.width;
		int randomy = rand.nextInt(zone.height) + zone.height;
		this.path = PathFinder.pathfinder.getPath(this.cur_pos, new Position(randomx, randomy));
		PathFinder.pathfinder.clear();
		return this.path != null && this.path.size() > 0;
	}

	private void walkTo(MiniRobot mini_robot) {

		Position next = this.path.peekLast();
		if (cur_pos.equals(mini_robot.next_pos) || next.equals(mini_robot.next_pos)) {
			return;
		}
		this.next_pos.x = next.x;
		this.next_pos.y = next.y;
		this.updateDirection();

	}

	private void updateDirection() {
		if (this.cur_pos.x > 0 && this.cur_pos.x < Ecosystem.mapWidth - 1 && this.cur_pos.y > 0
				&& this.cur_pos.y < Ecosystem.mapHeight - 1) {
			this.pos_direction.x = this.next_pos.x - this.cur_pos.x;
			this.pos_direction.y = this.next_pos.y - this.cur_pos.y;
			return;
		}
		if (this.cur_pos.x == Ecosystem.mapWidth - 1 && this.next_pos.x == 0) {
			this.pos_direction.x = 1;
			this.pos_direction.y = 0;
			return;
		}
		if (this.next_pos.x == Ecosystem.mapWidth - 1 && this.cur_pos.x == 0) {
			this.pos_direction.x = -1;
			this.pos_direction.y = 0;
			return;

		}
		if (this.cur_pos.y == Ecosystem.mapHeight - 1 && this.next_pos.y == 0) {
			this.pos_direction.x = 0;
			this.pos_direction.y = 1;
			return;

		}
		if (this.next_pos.y == Ecosystem.mapHeight - 1 && this.cur_pos.y == 0) {
			this.pos_direction.x = 0;
			this.pos_direction.y = -1;
			return;

		}
	
	}

	private boolean existPathTo(MiniRobot mini_robot) {

		return this.path != null && this.path.size() > 0;
	}

	private boolean strongerThan(MiniRobot mini_robot) {
		
		return this.level >= mini_robot.level;
	}

	private boolean healthierThan(MiniRobot mini_robot) {
		
		return this.life >= mini_robot.life;
	}

	private MiniRobot getDetectedMiniRobot() {
		for (Agent agent : Ecosystem.agents) {
			if (agent instanceof MiniRobot && agent.life > 0
					&& distance(this, (MiniRobot) agent) <= this.detection_area) {
				return (MiniRobot) agent;
			}
		}
		return null;
	}

	private boolean detectMiniRobotAlive() {
		if (Ecosystem.agents.size() == 0) {
			return false;
		}
		for (Agent agent : Ecosystem.agents) {
			if (agent instanceof MiniRobot && agent.life > 0
					&& distance(this, (MiniRobot) agent) <= this.detection_area) {
				return true;
			}
		}

		return false;

	}

	private void die() {
		this.life = 0;

	}

	private boolean canDie() {

		return Ecosystem.map.getMapTexture()[this.cur_pos.x][this.cur_pos.y] == MapTextureID.LAVA
				|| Ecosystem.map.getMapTexture()[this.cur_pos.x][this.cur_pos.y] == MapTextureID.WATER;
	}

	private void increaseIsDeadTime() {
		this.life--;

	}

	private boolean canRevive() {
		return this.life < -10;
	}

	private boolean isDead() {

		return this.life <= 0;
	}

	private void doNothing() {
		

	}

	private MiniRobot closestMiniRobot() {
		if (Ecosystem.agents.size() == 0) {
			return null;
		}
		MiniRobot closest = null;
		for (Agent agent : Ecosystem.agents) {
			if (agent instanceof MiniRobot && agent.life > 0) {

				if (closest == null) {
					closest = (MiniRobot) agent;
				} else {
					if (distance(this, closest) > distance(this, (MiniRobot) agent)) {
						closest = (MiniRobot) agent;
					}
				}

			}
		}

		return closest;

	}

	private int distance(Player player, MiniRobot MiniRobot) {
		int map_x2 = MiniRobot.cur_pos.x;
		int map_y2 = MiniRobot.cur_pos.y;
		int map_x1 = player.cur_pos.x;
		int map_y1 = player.cur_pos.y;
		return (int) Math.sqrt((map_y2 - map_y1) * (map_y2 - map_y1) + (map_x2 - map_x1) * (map_x2 - map_x1));

	}

	public int distance(int map_x1, int map_y1, int map_x2, int map_y2) {
		
		return (int) Math.sqrt((map_y2 - map_y1) * (map_y2 - map_y1) + (map_x2 - map_x1) * (map_x2 - map_x1));
	}

	private void attack(MiniRobot MiniRobot) {
		MiniRobot.life = MiniRobot.life - (int)(Math.random() * 3 + 1);

	}

	protected MiniRobot getNextToMiniRobot() {
		for (Agent agent : Ecosystem.agents) {
			boolean up = (agent.cur_pos.x == this.cur_pos.x
					&& this.moduloHeight(agent.cur_pos.y - 1) == this.cur_pos.y);
			boolean down = (agent.cur_pos.x == this.cur_pos.x
					&& this.moduloHeight(agent.cur_pos.y + 1) == this.cur_pos.y);
			boolean right = (this.moduloWidth(agent.cur_pos.x + 1) == this.cur_pos.x
					&& (agent.cur_pos.y) == this.cur_pos.y);
			boolean left = (this.moduloWidth(agent.cur_pos.x - 1) == this.cur_pos.x
					&& (agent.cur_pos.y) == this.cur_pos.y);
			if (agent instanceof MiniRobot && (up || down || right || left)) {
				return (MiniRobot) agent;
			}

		}
		return null;
	}

}
