package learning.location;

import learning.GamePanel;

public class Location {
	
	private int x, y;
	
	public Location(int x, int y) {
		this.x = adapter(x);
		this.y = adapter(y);
	}
	
	public void set(int x, int y) {
		this.x = adapter(x);
		this.y = adapter(y);
	}
	
	public double Distance(Location loc) {
		
		int x1 = (y - loc.getY());
		int y2 = (loc.getX() - x);
		
		return Math.hypot(x1, y2);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	private int adapter(int a) {
		return (a/GamePanel.UNIT_SIZE);
	}
	
}
