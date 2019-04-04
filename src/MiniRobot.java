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
		super(x, y, 100, "MiniRobot");
	}

	public void update() {

		if (isDead()) {
			// System.out.println("MiniRobot is Dead");

		} else {

			if (canDie()) {
				// System.out.println("MiniRobot is dying because of water or lava");
				this.die();
			} else {

				if (this.detectPlayer()) {

					// System.out.println("MiniRobot detected a player");
					Player player = this.getDetectedPlayer();

					if (this.nextTo(player)) {
						// System.out.println("MiniRobot is next to a player");
						this.stayAtCurrentPosition();
						this.attack(player);

					} else {

						this.path = PathFinder.pathfinder.getPath(this.cur_pos, player.cur_pos);
						
						if (existPathTo(player.cur_pos)) {
							// System.out.println("MiniRobot is walking towards a player");
							this.walkTo(player);

						} else {

							if (random_pos == null || this.cur_pos.equals(this.random_pos)) {
								ArrayList<Position> legalPosition = Ecosystem.map.getAccessiblePositionMiniRobot();
								if (legalPosition.size() > 0) {
									random_pos = legalPosition.remove((int) (legalPosition.size() * Math.random()));
									if (existPathTo(random_pos)) {
										// System.out.println("MiniRobot walks toward a target");
										this.walkTo(random_pos);

									} else {
										// System.out.println("MiniRobot walks randomly");
										// this.walkRandomly();

									}
								} else {

								}

							}

						}

					}
				} else {

					if (random_pos == null || this.cur_pos.equals(this.random_pos) || this.path == null
							|| this.path.size() == 0 || timeSearch < 0) {

						ArrayList<Position> legalPosition = Ecosystem.map.getAccessiblePositionMiniRobot();
						if (legalPosition.size() > 0) {
							random_pos = legalPosition.remove((int) (legalPosition.size() * Math.random()));
							if (existPathTo(random_pos)) {
								// System.out.println("MiniRobot walks toward a target");
								this.walkTo(random_pos);

							} else {
								// System.out.println("MiniRobot walks randomly");
								// this.walkRandomly();

							}
						} else {

						}
						timeSearch = 30;
					}
					if (existPathTo(random_pos)) {
						timeSearch--;
						this.walkTo(random_pos);

					} else {
						timeSearch--;
						this.walkRandomly();

					}

				}

			}
		}
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
		this.path = PathFinder.pathfinder.getPath(this.cur_pos, item);
		
		return this.path.size() > 0;
	}

	private void stayAtCurrentPosition() {
		this.next_pos.x = this.cur_pos.x;
		this.next_pos.y = this.cur_pos.y;

	}

	

	private boolean nextTo(Player player) {

		return this.cur_pos.isAbove(player.cur_pos) || this.cur_pos.isDown(player.cur_pos)
				|| this.cur_pos.isAtTheLeftOf(player.cur_pos) || this.cur_pos.isAtTheRightOf(player.cur_pos);
	}

	private void die() {

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
		int map_x2 = robot.cur_pos.x;
		int map_y2 = robot.cur_pos.y;
		int map_x1 = player.cur_pos.x;
		int map_y1 = player.cur_pos.y;
		return (int) Math.sqrt((map_y2 - map_y1) * (map_y2 - map_y1) + (map_x2 - map_x1) * (map_x2 - map_x1));

	}

	private void attack(Player player) {
		player.life--;
		if (player.life < 0) {
			player.life = 0;
		}
	}

}
