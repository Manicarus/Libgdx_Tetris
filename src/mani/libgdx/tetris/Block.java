package mani.libgdx.tetris;

import com.badlogic.gdx.graphics.Color;

public class Block {
	private final int BLUE = 0, RED = 1, GREEN = 2, YELLOW = 3, CYAN = 4, BLACK = 5;
	public static final int X = 0, Y = 1;
	public static final int edgeLen = 30;
	Color color; 
	
	int[] pos = new int[2];
	
	Block() {
		color = Color.BLACK; // Default color
	}
	
	Block(int x, int y) {
		this();
		pos[X] = x;
		pos[Y] = y;
	}
	
	Block(int x, int y, Color _color) {
		this(x, y);
		this.color = _color;
	}
}
