import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class Agent {
	@Override
	public String toString() {
		return "Agent [name=" + name + ", cur_pos=" + cur_pos + ", next_pos=" + next_pos + ", path=" + path + "]";
	}

	public int max_life;
	public String name;
	public Position cur_pos, next_pos;
	public Position pos_offset;
	public int life;
	public int level;
	LinkedList<Position> path = new LinkedList<Position>();
	public Position pos_direction;
	public int attackPower;
	public int defensePower;
	public double criticalRate;
	public double criticalDamage;
	public double precision;
	int[][] map = new int[Ecosystem.mapWidth][Ecosystem.mapHeight];
	boolean[][] visited = new boolean[Ecosystem.mapWidth][Ecosystem.mapHeight];
	ArrayList<Position> nextToVisit = new ArrayList<>();
	private static final int ROAD = 0;
	private static final int OBSTACLE = 1;
	private static final int AGENT = 2;
	private static final int GOAL = 3;
	private final int[][] DIRECTIONS = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

	public LinkedList<Position> getPath(Position cur_pos, Position target) {
		
		for (int y = 0; y < map[0].length; y++) {
			for (int x = 0; x < map.length; x++) {
				if (Ecosystem.map.getMapEntities()[x][y] != MapEntitiesID.NOTHING || (Ecosystem.map.getMapTexture()[x][y] != MapTextureID.GROUND )) {
					map[x][y] = OBSTACLE;
				} else {
					map[x][y] = ROAD;
				}
			}
		}
		/*
		for(Agent agent: Ecosystem.agents) {
			map[agent.cur_pos.x][agent.cur_pos.y] = OBSTACLE;
		}
		*/
		map[cur_pos.x][cur_pos.y] = AGENT;
		
		map[target.x][target.y] = GOAL;
		
		
		
		
		
		nextToVisit.add(cur_pos);
		
		while (!nextToVisit.isEmpty()) {
			
			Position current = nextToVisit.remove(0);

			if  (visited[current.x][current.y]) {
				continue;
			}

			if (map[current.x][current.y] == OBSTACLE) {
				visited[current.x][current.y] = true;
				continue;
			}

			if (map[current.x][current.y] == GOAL) {
				this.clear();
				return backtrackPath(current);
			}

			
			for (int[] direction : DIRECTIONS) {
				Position Position = new Position((current.x + direction[0] + Ecosystem.mapWidth)%Ecosystem.mapWidth, (current.y + direction[1] + Ecosystem.mapHeight)%Ecosystem.mapHeight, current);
				nextToVisit.add(Position);
				visited[current.x][current.y] = true;
			}
		}
	this.clear();
		return new LinkedList<Position>();
	}

	
	private  LinkedList<Position> backtrackPath(Position cur) {
		LinkedList<Position> path = new LinkedList<>();
		Position iter = cur;
		while (iter.parent != null) {
			path.add(iter);
			iter = iter.parent;
		}
		
		//path.removeLast();
		
		
		return path;
	}
	
	
	public void clear() {
		// TODO Auto-generated method stub
		for (int y = 0; y < map[0].length; y++) {
			for (int x = 0; x < map.length; x++) {
				visited[x][y] = false;
			}
		}
		nextToVisit.clear();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Agent(int x, int y, int max_life, String name) {
		super();
		this.max_life = max_life;
		this.life = max_life;
		this.level = 1;
		this.attackPower = this.max_life / 10;
		this.defensePower = this.max_life / 20;
		this.criticalRate = 0.1;
		this.criticalDamage = 0.5;
		this.precision = 0.8;
		this.name = name;
		this.cur_pos = new Position(x, y);
		this.next_pos = new Position(x, y);
		this.pos_offset = new Position(0, 0);
		this.pos_direction = new Position(0, 1);
	}

	public void updateOffset() {

		// torique
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

	public boolean in(Zone zone) {
		return this.cur_pos.x > zone.x && this.cur_pos.x < zone.x + zone.width && this.cur_pos.y > zone.y
				&& this.cur_pos.y < zone.y + zone.height;

	}

	protected void walkRandomly() {
		this.next_pos.x = cur_pos.x;
		this.next_pos.y = cur_pos.y;
		ArrayList<Position> possible_dir = new ArrayList<Position>();

		if (canMoveLeft())
			possible_dir.add(new Position(this.moduloWidth(this.cur_pos.x - 1), this.cur_pos.y));
		if (canMoveRight())
			possible_dir.add(new Position(this.moduloWidth(this.cur_pos.x + 1), this.cur_pos.y));
		if (canMoveDown())
			possible_dir.add(new Position(this.moduloWidth(this.cur_pos.x), this.moduloHeight(this.cur_pos.y + 1)));
		if (canMoveUp())
			possible_dir.add(new Position(this.moduloWidth(this.cur_pos.x), this.moduloHeight(this.cur_pos.y - 1)));
		if (possible_dir.isEmpty()) {
			return;
		}
		Position rand_next_pos = possible_dir.get((int) (Math.random() * possible_dir.size()));
		this.next_pos.x = rand_next_pos.x;
		this.next_pos.y = rand_next_pos.y;
		this.updateDirection();

	}

	private boolean canMoveUp() {
		int x = this.moduloWidth(this.cur_pos.x);
		int y = this.moduloHeight((this.cur_pos.y - 1));
		int[][] mapE = Ecosystem.map.getMapEntities();
		int[][] mapT = Ecosystem.map.getMapTexture();
		return mapE[x][y] == MapEntitiesID.NOTHING && mapT[x][y] == MapTextureID.GROUND && !agentAt(x, y);
	}

	private boolean agentAt(int x, int y) {
		
		for(Agent agent : Ecosystem.agents) {
			if(agent.cur_pos.equals(new Position(x,y))) {
				return true;
			}
		}
	
		return false;
	}

	private boolean canMoveDown() {
		int x = this.moduloWidth(this.cur_pos.x);
		int y = this.moduloHeight((this.cur_pos.y + 1));
		int[][] mapE = Ecosystem.map.getMapEntities();
		int[][] mapT = Ecosystem.map.getMapTexture();
		return mapE[x][y] == MapEntitiesID.NOTHING && mapT[x][y] == MapTextureID.GROUND &&!agentAt(x, y);
	}

	private boolean canMoveRight() {
		int x = this.moduloWidth(this.cur_pos.x + 1);
		int y = (this.cur_pos.y);
		int[][] mapE = Ecosystem.map.getMapEntities();
		int[][] mapT = Ecosystem.map.getMapTexture();
		return mapE[x][y] == MapEntitiesID.NOTHING && mapT[x][y] == MapTextureID.GROUND &&!agentAt(x, y);
	}

	private boolean canMoveLeft() {
		int x = this.moduloWidth(this.cur_pos.x - 1);
		int y = (this.cur_pos.y);
		int[][] mapE = Ecosystem.map.getMapEntities();
		int[][] mapT = Ecosystem.map.getMapTexture();
		return mapE[x][y] == MapEntitiesID.NOTHING && mapT[x][y] == MapTextureID.GROUND &&!agentAt(x, y);
	}

	protected void updateDirection() {
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

	public int distance(int map_x1, int map_y1, int map_x2, int map_y2) {

		return (int) Math.sqrt((map_y2 - map_y1) * (map_y2 - map_y1) + (map_x2 - map_x1) * (map_x2 - map_x1));
	}
}
