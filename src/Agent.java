import java.util.LinkedList;

public class Agent {
	@Override
	public String toString() {
		return "Agent [name=" + name + ", cur_pos=" + cur_pos + ", next_pos=" + next_pos + ", path=" + path
				 + "]";
	}

	public int max_life;
	public String name;
	public Position cur_pos, next_pos;
	public Position pos_offset;
	public int life;
	public int level = 1;
	LinkedList<Position> path = new LinkedList<Position>();
	public Position pos_direction;


	public Agent(int x, int y,int max_life, String name) {
		super();
		this.max_life = max_life;
		this.life = max_life;
		this.name = name;
		this.cur_pos = new Position(x, y);
		this.next_pos = new Position(x, y);
		this.pos_offset = new Position(0, 0);
		this.pos_direction = new Position(0,0);
	}

	
	/*protected Agent getNextToAgent() {
		for (Agent agent : Ecosystem.agents) {
			boolean up = (agent.cur_pos.x == this.cur_pos.x
					&& this.moduloHeight(agent.cur_pos.y - 1) == this.cur_pos.y);
			boolean down = (agent.cur_pos.x == this.cur_pos.x
					&& this.moduloHeight(agent.cur_pos.y + 1) == this.cur_pos.y);
			boolean right = (this.moduloWidth(agent.cur_pos.x + 1)== this.cur_pos.x
					&& (agent.cur_pos.y ) == this.cur_pos.y);
			boolean left = (this.moduloWidth(agent.cur_pos.x - 1)== this.cur_pos.x
					&& (agent.cur_pos.y ) == this.cur_pos.y);
			if(up || down || right || left) {
				return agent;
			}
			
		}
		return null;
	}
	private boolean agentAt(Position next_pos2) {
		for (Agent agent : Ecosystem.agents) {
			if (agent != this && agent.cur_pos.x == next_pos2.x && agent.cur_pos.y == next_pos2.y) {
				return true;
			}
		}
		return false;
	}*/
	public void updateOffset() {
	
	
		
		//torique 
		if ((this.next_pos.x - this.cur_pos.x == 1 || this.next_pos.y - this.cur_pos.y == 1
				|| this.next_pos.x - this.cur_pos.x == -1 || this.next_pos.y - this.cur_pos.y == -1)) {

			this.pos_offset.x += this.next_pos.x - this.cur_pos.x;
			this.pos_offset.y += this.next_pos.y - this.cur_pos.y;
		}

	}

	protected int moduloHeight(int i) {
		// TODO Auto-generated method stub
		return (i + Ecosystem.mapHeight) % Ecosystem.mapHeight;
	}

	protected int moduloWidth(int i) {

		return (i + Ecosystem.mapWidth) % Ecosystem.mapWidth;
	}
	
	protected void resurection() {
		this.life = this.max_life;
		this.cur_pos.x = Ecosystem.spawn_pos.x;
		this.cur_pos.y = Ecosystem.spawn_pos.y;
		this.next_pos.x = this.cur_pos.x;
		this.next_pos.y =this.cur_pos.y;
		
		this.pos_offset = new Position(0, 0);
		
		
	}


	public boolean in(Zone zone) {
		return this.cur_pos.x > zone.x && this.cur_pos.x < zone.x + zone.width && this.cur_pos.y > zone.y && this.cur_pos.y < zone.y + zone.height;
		
		
	}
}
