
public class Healer extends Agent {
	Player player;
	public Healer(int x, int y, int max_life, String name, Player player) {
		super(x, y, max_life, name);
		this.player = player;
		// TODO Auto-generated constructor stub
	}

	public void update() {
		this.path = this.getPath(this.cur_pos, this.player.cur_pos);
		if(path.size() > 0) {
			if(!this.path.peekLast().equals(this.player.cur_pos)) {
				this.next_pos.x = this.path.peekLast().x;
				this.next_pos.y = this.path.peekLast().y;
			}
			
		}
		if(this.distance(this.cur_pos.x, this.cur_pos.y, player.cur_pos.x, player.cur_pos.y) < 10) {
			this.player.life = this.player.life + 2;
			if(this.player.life > this.player.max_life) {
				this.player.life = this.player.max_life;
			}
		}
		
	}

}
