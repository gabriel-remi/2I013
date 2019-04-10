
public class Mage extends Agent {
	Player player;

	public Mage(int x, int y, int max_life, String name, Player player) {
		super(x, y, max_life, name);
		this.player = player;
		
	}

	public void update() {
		this.path = this.getPath(this.cur_pos, this.player.cur_pos);
		if (path.size() > 0) {
			if (!this.path.peekLast().equals(this.player.cur_pos)) {
				this.next_pos.x = this.path.peekLast().x;
				this.next_pos.y = this.path.peekLast().y;
			}

		}
		if (this.distance(this.cur_pos.x, this.cur_pos.y, player.cur_pos.x, player.cur_pos.y) < 10) {

			for (Agent agent : Ecosystem.agents) {
				if (agent instanceof MiniRobot && this.distance(this.player.cur_pos.x, this.player.cur_pos.y,
						agent.cur_pos.x, agent.cur_pos.y) < 5) {
					agent.life--;
				}
			}

			for (int x = this.moduloWidth(this.player.cur_pos.x - 5); x < this
					.moduloWidth(this.player.cur_pos.x + 5); x++) {
				for (int y = this.moduloWidth(this.player.cur_pos.y - 5); y < this
						.moduloWidth(this.player.cur_pos.y + 5); y++) {
					if (Ecosystem.map.getMapEntities()[x][y] == MapEntitiesID.GREEN_TREE
							|| Ecosystem.map.getMapEntities()[x][y] == MapEntitiesID.BABY_GREEN_TREE
							|| Ecosystem.map.getMapEntities()[x][y] == MapEntitiesID.YOUNG_GREEN_TREE) {
						Ecosystem.map.getMapEntities()[x][y] = MapEntitiesID.BURNING_TREE;
					}
				}
			}
		}

	}

}
