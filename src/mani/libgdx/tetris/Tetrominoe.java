package mani.libgdx.tetris;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;

public class Tetrominoe {
	public static final int BOX = 0, L = 1, MIRROR_L = 2, STICK = 3, 
			HAT = 4, L_SNAKE = 5, R_SNAKE = 6;
	
	private final int BLUE = 0, RED = 1, GREEN = 2, YELLOW = 3, 
			CYAN = 4, ORANGE = 5, PINK = 6;
	
	public static final int init_X = 5, init_Y = 3;
	public static final int blockNum = 4;
	
	Block[] blocks = new Block[blockNum]; // A Tetrominoe consists of four Blocks // This is only creating 4 variables, not objects
	int curShape;
	int curColor;
	
	int[][][] shape = {
			{{0, 0}, {1, 0}, {0, -1}, {1, -1}}, // BOX 
			{{0, 0}, {0, -1}, {0, -2}, {-1, 0}}, // MIRROR_L
			{{0, 0}, {0, -1}, {0, -2}, {1, 0}}, // L
			{{0, 0}, {0, 1}, {0, -1}, {0, -2}}, // STICK
			{{0, 0}, {-1, 0}, {1, 0}, {0, -1}}, // HAT
			{{0, 0}, {-1, 0}, {-1, -1}, {0, 1}}, // L_SNAKE
			{{0, 0}, {-1, 0}, {0, -1}, {-1, 1}} // R_SNAKE
	}; 
	
	Color[] color = {Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.CYAN, Color.ORANGE, Color.PINK};
	
	Tetrominoe() { // Contructor
		for(int i = 0; i < blockNum; i++) { // Creating objects for each variable
			blocks[i] = new Block();
		}
		Random rand = new Random();
		curShape = rand.nextInt(7);
		curColor = rand.nextInt(100) / 13 % 7; // Just like hash function
		
		for(int i = 0; i < 4; i++) {
			blocks[i].pos[Block.X] = init_X + shape[curShape][i][Block.X];
			blocks[i].pos[Block.Y] = init_Y + shape[curShape][i][Block.Y];
			blocks[i].color = color[curColor];
		}
	}
	
	Tetrominoe(Tetrominoe tetrominoe) { // Copy Constructor 1
		for(int i = 0; i < blockNum; i++) {
			blocks[i] = new Block();
		}
		
		curShape = tetrominoe.curShape;
		
		for(int i = 0; i < 4; i++) {
			blocks[i].pos[Block.X] += tetrominoe.blocks[i].pos[Block.X];
			blocks[i].pos[Block.Y] += tetrominoe.blocks[i].pos[Block.Y];
		}
	}
	
	Tetrominoe(Tetrominoe tetrominoe, int x, int y) { // Copy Constructor 2
		for(int i = 0; i < blockNum; i++) {
			blocks[i] = new Block();
		}
		
		curShape = tetrominoe.curShape;
		
		for(int i = 0; i < 4; i++) {
			blocks[i].pos[Block.X] += tetrominoe.blocks[i].pos[Block.X] + x;
			blocks[i].pos[Block.Y] += tetrominoe.blocks[i].pos[Block.Y] + y;
		}
	}
	
	void mvLeft() {
		for(int i = 0; i < 4; i++) {
			blocks[i].pos[Block.X]--;
		}
	}
	
	void mvRight() {
		for(int i = 0; i < 4; i++) {
			blocks[i].pos[Block.X]++;
		}
	}
	
	void mvUp() {
		for(int i = 0; i < 4; i++) {
			blocks[i].pos[Block.Y]--;
		}
	}
	
	void mvDown() {
		for(int i = 0; i < 4; i++) {
			blocks[i].pos[Block.Y]++;
		}
	}
	
	/*	| 0 -1 |
	 *	| 1  0 |
	 * */
	void rotate() {
		int[][] tmpPos = new int[blockNum][2];
		for(int i = 0; i < blockNum; i++) {
			tmpPos[i][Block.X] = blocks[i].pos[Block.X];
			tmpPos[i][Block.Y] = blocks[i].pos[Block.Y];
		}
		
		for(int i = 0; i < blockNum; i++) { // Move Tetrominoe to the origin and rotate
			blocks[i].pos[Block.X] = -(tmpPos[i][Block.Y] - blocks[0].pos[Block.Y]) + blocks[0].pos[Block.X];
			blocks[i].pos[Block.Y] = tmpPos[i][Block.X] - blocks[0].pos[Block.X] + blocks[0].pos[Block.Y];
		}
	}
}
