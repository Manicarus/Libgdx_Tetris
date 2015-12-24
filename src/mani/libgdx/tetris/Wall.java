package mani.libgdx.tetris;

public class Wall {
	private Block[][] wall;
	
	Wall(int height, int width) {
		int leftWall = 0;
		int rightWall = width - 1;
		
		for(int i = 0; i < height; i++) {
			wall[i][leftWall] = new Block(leftWall, i); // Left Wall
			wall[i][rightWall] = new Block(rightWall, i); // Right Wall
		}
		
		for(int j = 0; j < width; j++) {
			wall[height - 1][j] = new Block(j, height);
		}
	}
}
