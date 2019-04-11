import java.util.ArrayList;

public class Player extends Agent {
	public static Position revivingPosition;
	int detection_area = 10;
	private Position random_pos;
	private int timeSearch;
	public Integer damageDealt = null;
	public boolean miss = false;
	public Agent target = null;
	private int earnedPoint = 0;

	public Player(int x, int y) {
		super(x, y, 100, "Gabriel");
	}

	public Player(Position playerPosition) {
		super(playerPosition.x, playerPosition.y, 100, "Gabriel");
		this.level = 1;

	}

	public void update() {
		// 
		this.damageDealt = null;
		this.target = null;
		this.miss = false;

		if (isDead()) {
			if (canRevive()) {
				this.resurection();
			} else {
				increaseIsDeadTime();
			}
		} else {

			if (canDie()) {
				this.die();
			} else {
				if(earnedPoint > 20 && numberHealer() < 1) {
					Ecosystem.tmp.add(new Healer(this.cur_pos.x, this.cur_pos.y, 999999, "Healer", this));
					this.earnedPoint -=20;
				}
				if(earnedPoint > 50 && numberMage() < 1) {
					Ecosystem.tmp.add(new Mage(this.cur_pos.x, this.cur_pos.y, 999999, "Mage", this));
					this.earnedPoint -=  50;
				}
				if (this.in(Zone.healingZone) && this.life < this.max_life) {
					regenLife();
						MiniRobot mini_robot = this.closestMiniRobot();
						if(mini_robot != null) {
							this.attack(mini_robot);
						
					
						if (this.nextTo(mini_robot)) {
							this.stayAtCurrentPosition();
							this.face(mini_robot);
							
							
							if (mini_robot.life <= 0) {
								earnPoint();
								this.levelUp();
							}
						}
						}

					
				}
				 else {
					 if (this.life < this.max_life / 3) {
							if (existPathTo(Zone.healingZone)) {
								this.walkTo(Zone.healingZone);

							} else {
								if (existPathTo(Zone.securityZone)) {
									this.walkTo(Zone.securityZone);

								} else {
									this.walkRandomly();
								}
							}
						}
				 else {

						if (this.detectMiniRobotAlive()) {
							MiniRobot mini_robot = this.closestMiniRobot();
							if (this.nextTo(mini_robot) && this.healthierThan(mini_robot)) {
								this.stayAtCurrentPosition();
								this.face(mini_robot);
								this.attack(mini_robot);

								if (mini_robot.life <= 0) {
									this.earnPoint();
									this.levelUp();;
								}
							} else {

								if (this.healthierThan(mini_robot)) {

									if (this.strongerThan(mini_robot)) {
										this.path = this.getPath(this.cur_pos, mini_robot.cur_pos);
										this.clear();
										if (existPathTo(mini_robot)) {
											this.walkTo(mini_robot);
										} else {
											if (random_pos == null || this.cur_pos.equals(this.random_pos)
													|| this.path == null || this.path.size() == 0 || timeSearch < 0) {
												ArrayList<Position> acces = Ecosystem.map
														.getAccessiblePositionMiniRobot();
												if (acces.isEmpty()) {
													this.walkRandomly();
												} else {
													this.random_pos = acces.get((int) (acces.size() * Math.random()));
												}
												timeSearch = 30;
											}
											if (existPathTo(random_pos)) {
												timeSearch--;
												this.walkTo(random_pos);

											} else {

												this.walkRandomly();

											}
										}
									} else {

										// !!!!!!
										if (existPathTo(Zone.securityZone)) {
											this.walkTo(Zone.securityZone);

										} else {
											if (existPathTo(Zone.healingZone)) {
												this.walkTo(Zone.healingZone);
											} else {
												this.walkRandomly();
											}
										}

									}
								} else {
									// !!!!!
									if (existPathTo(Zone.healingZone)) {
										this.walkTo(Zone.healingZone);

									} else {
										if (existPathTo(Zone.securityZone)) {
											this.walkTo(Zone.securityZone);

										} else {

											if (existPathTo(mini_robot)) {
												this.walkTo(mini_robot);
											} else {
												this.walkRandomly();
											}
										}
									}
								}
							}

						} else {

							if (random_pos == null || this.cur_pos.equals(this.random_pos) || this.path == null
									|| this.path.size() == 0 || timeSearch < 0) {
								ArrayList<Position> acces = Ecosystem.map.getAccessiblePositionMiniRobot();
								if (acces.isEmpty()) {
									this.walkRandomly();
								} else {
									this.random_pos = acces.get((int) (acces.size() * Math.random()));
								}
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

			}
		}
	}

	private void levelUp() {
		this.level++;
		this.attackPower+=5;
		this.defensePower+=5;
		this.max_life+=10;
		this.life=this.max_life;
		
		
	}

	private void earnPoint() {
		this.earnedPoint++;
		
	}

	private int numberMage() {
		int cpt =0;
		for(Agent agent: Ecosystem.agents) {
			if (agent instanceof Mage) {
				cpt++;
			}
		}
		return cpt;
	}

	private int numberHealer() {
		int cpt =0;
		for(Agent agent: Ecosystem.agents) {
			if (agent instanceof Healer) {
				cpt++;
			}
		}
		return cpt;
	}

	private void regenLife() {
		this.life++;
		
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

		return mini_robot.cur_pos.isAbove(this.cur_pos) || mini_robot.cur_pos.isDown(this.cur_pos)
				|| mini_robot.cur_pos.isAtTheLeftOf(this.cur_pos) || mini_robot.cur_pos.isAtTheRightOf(this.cur_pos);
	}

	private void walkTo(Position item) {
		Position next = this.path.peekLast();
		this.next_pos.x = next.x;
		this.next_pos.y = next.y;
		this.updateDirection();

	}

	private boolean existPathTo(Position item) {

		this.path = getPath(this.cur_pos, item);
		return this.path != null && this.path.size() > 0;
	}

	private void walkTo(Zone zone) {

		Position next = this.path.peekLast();
		this.next_pos.x = next.x;
		this.next_pos.y = next.y;
		this.updateDirection();

	}

	private boolean existPathTo(Zone zone) {

		ArrayList<Position> acces = zone.getLegalPosition();
	
		if(acces.isEmpty()) {
			return false;
		}
		else {
			this.path = getPath(this.cur_pos, acces.get((int) (acces.size()*Math.random())));
		}

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

	private boolean existPathTo(MiniRobot mini_robot) {

		return this.path != null && this.path.size() > 0;
	}

	private boolean strongerThan(MiniRobot mini_robot) {

		return this.level >= mini_robot.level;
	}

	private boolean healthierThan(MiniRobot mini_robot) {

		return this.life >= mini_robot.life / 2;
	}

	public MiniRobot getDetectedMiniRobot() {
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

		return Ecosystem.map.getMapTexture()[this.cur_pos.x][this.cur_pos.y] == MapTextureID.LAVA;
	}

	private void increaseIsDeadTime() {
		this.life--;

	}

	private boolean canRevive() {
		return this.life < -10;
	}

	public boolean isDead() {

		return this.life <= 0;
	}

	public void doNothing() {

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
					if (distance(this, closest) > distance(this, (MiniRobot) agent) && closest.life > agent.life) {
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



	private void attack(MiniRobot MiniRobot) {
	this.target = MiniRobot;
		if (this.precision > Math.random()) {
			if (this.criticalRate > Math.random()) {
				this.damageDealt = (int) (this.attackPower + this.attackPower * this.criticalDamage
						- MiniRobot.defensePower) + (int) (Math.random() * 20 - 10);

			} else {
				this.damageDealt = this.attackPower - MiniRobot.defensePower + (int) (Math.random() * 20 - 10);

			}
			if (this.damageDealt < 0) {
				this.damageDealt = 0;
			}
			
			MiniRobot.life = MiniRobot.life - this.damageDealt;
			if (MiniRobot.life < 0) {
				MiniRobot.life = 0;
			}
		} else {
			this.miss = true;
			
		}

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

	protected void resurection() {
		this.life = this.max_life;
		this.cur_pos.x = Player.revivingPosition.x;
		this.cur_pos.y = Player.revivingPosition.y;
		this.next_pos.x = this.cur_pos.x;
		this.next_pos.y = this.cur_pos.y;
		this.pos_offset = new Position(0, 0);

	}

}
