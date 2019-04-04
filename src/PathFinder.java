import java.util.ArrayList;
import java.util.LinkedList;

public class PathFinder {
	int[][] map = new int[Ecosystem.mapWidth][Ecosystem.mapHeight];
	boolean[][] visited = new boolean[Ecosystem.mapWidth][Ecosystem.mapHeight];
	ArrayList<Position> nextToVisit = new ArrayList<>();
	private static final int ROAD = 0;
	private static final int OBSTACLE = 1;
	private static final int AGENT = 2;
	private static final int GOAL = 3;
	private final int[][] DIRECTIONS = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
	public static PathFinder pathfinder = new PathFinder();
	private PathFinder() {
		super();
		// TODO Auto-generated constructor stub
	}
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
		for(Agent agent: Ecosystem.agents) {
			map[agent.cur_pos.x][agent.cur_pos.y] = OBSTACLE;
		}
		
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

}
