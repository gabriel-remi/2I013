
public class Position {
	@Override
	public String toString() {
		return "Position [x=" + x + ", y=" + y + "]";
	}

	public int x, y;
	Position parent;

	public Position() {
		this.x = (int) (Math.random() * Ecosystem.mapWidth);
		this.y = (int) (Math.random() * Ecosystem.mapHeight);
		this.parent = null;
	}
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
		this.parent = null;
	}

	public Position(int x, int y, Position parent) {
		this.x = x;
		this.y = y;
		this.parent = parent;
	}

	int getX() {
		return x;
	}

	int getY() {
		return y;
	}

	Position getParent() {
		return parent;
	}
	
	public boolean isAtTheRightOf(Position p) {
		return this.x == (p.x + 1 + Ecosystem.mapWidth)%Ecosystem.mapWidth && this.y == p.y;
	}
	
	public boolean isAtTheLeftOf(Position p) {
		return this.x == (p.x - 1 + Ecosystem.mapWidth)%Ecosystem.mapWidth && this.y == p.y;
	}
	
	public boolean isAbove(Position p) {
		return this.x == p.x && this.y == (p.y - 1 + Ecosystem.mapHeight)%Ecosystem.mapHeight;
	}
	
	public boolean isDown(Position p) {
		return this.x == p.x && this.y == (p.y + 1 + Ecosystem.mapHeight)%Ecosystem.mapHeight;
	}

	public boolean equals(Position p) {
		return p.x == this.x && p.y == this.y;
	}

	public Position getAbove() {
		
		return new Position(this.x, (this.y - 1 + Ecosystem.mapHeight) %Ecosystem.mapHeight);
	}

	public Position getDown() {
		
		return new Position(this.x, (this.y + 1 + Ecosystem.mapHeight) %Ecosystem.mapHeight);
	}

	public Position getLeft() {

		return new Position((this.x  - 1 + Ecosystem.mapWidth) % Ecosystem.mapWidth, this.y);
	}

	public Position getRight() {

		return new Position((this.x  + 1 + Ecosystem.mapWidth) % Ecosystem.mapWidth, this.y);
	}
	
}
