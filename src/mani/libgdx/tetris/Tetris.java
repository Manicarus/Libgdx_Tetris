package mani.libgdx.tetris;

public class Tetris {
	JFrame mainFrame;
	JPanel board;
	Block[][] fixedBlock;
	Tetrominoe curTetro;
	boolean GameOver;
	Timer timer;
	
	private static final int HEIGHT = 20;
	private static final int WIDTH = 10;
	private static final int LEFT_WALL = 0;		// x coord of Left wall 
	private static final int RIGHT_WALL = 11;		// x coord of Right wall 
	private static final int BOTTOM_WALL = 20;		// y coord of Bottom wall 
	private static final int FRAME_HEIGHT = 700;
	private static final int FRAME_WIDTH = 800;
	private static final int OFFSET = Block.edgeLen;
			
	
	Tetris() { // Constructor // Initializing Tetris
		// Set-up for visualizing GAME WINDOW
		mainFrame = new JFrame();
		board = new JPanel();
		
		mainFrame.setTitle("테트리스");
		//mainFrame.setLocation(100, 100);
		//mainFrame.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		mainFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT); // setSize(가로, 세로)
		mainFrame.setLayout(new BorderLayout());
		mainFrame.add(board);
		mainFrame.setVisible(true);
		mainFrame.addKeyListener(new TetrisKeyListener());
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Initialize();
		while(!GameOver) {
			Update();
			Render();
		}
		Release();
	}
	
	ActionListener timerMvDown = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(!checkCollision(new Tetrominoe(curTetro, 0, 1))) {
				curTetro.mvDown();
			}
		}
		
	};
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Tetris tetris = new Tetris();
	}
	
	void DropTetro() {
		while(!checkCollision(new Tetrominoe(curTetro, 0, 1))) {
			curTetro.mvDown();
		}
	}
	
	void fixTetro() {
		for(int i = 0; i < Tetrominoe.blockNum; i++) {
			fixedBlock[curTetro.blocks[i].pos[Block.Y]][curTetro.blocks[i].pos[Block.X]] = curTetro.blocks[i];
		}
		
		checkLine(); // There's some timing bug
					 // curTetro remains even after erasing lines
					 // At times, curTetro is added to fixedBlock one more time.
		
					 // I'd better do this after a new Tetrominoe is created
					 // and do line checking for entire board,
					 // not just for particular blocks indices 
		
					 // Solved!
					 // This was because I called fixTetro function 
					 // too many times at different moments
					 // I was calling that function in timer, update(), DropTetro(), ...
					 // I made it to be called only once per cycle in update()
		
					 // However, there are still some bugs
	}
	
	void checkLine() {
		int[] suspect_y = new int[Tetrominoe.blockNum];
		
		for(int i = 0; i < Tetrominoe.blockNum; i++) {
			suspect_y[i] = curTetro.blocks[i].pos[Block.Y];
		}
		
		// Sort y coord from min to max -> bubblesort
		for(int i = 0; i < Tetrominoe.blockNum - 1; i++) {
			for(int j = 0; j < Tetrominoe.blockNum - (i + 1); j++) {
				if(suspect_y[j] > suspect_y[j + 1]) {
					int tmp = suspect_y[j];
					suspect_y[j] = suspect_y[j + 1];
					suspect_y[j + 1] = tmp;
				}
			}
		}
		
		for(int i = 0; i < Tetrominoe.blockNum; i++) {
			for(int j = 0; j < WIDTH; j++) {
				if(fixedBlock[suspect_y[i]][j + 1] == null) { // Line is not full
					break;
				} else if(j + 1 == WIDTH) { // The line is full
					// pull down line
					for(int k = suspect_y[i]; k >= 0; k--) {
						for(int l = 0; l < WIDTH; l++) {
							if(k == 0) {
								fixedBlock[k][l + 1] = null;
							} else {
								fixedBlock[k][l + 1] = fixedBlock[k - 1][l + 1];
								if(fixedBlock[k][l + 1] != null) {
									fixedBlock[k][l + 1].pos[Block.Y]++;
								}
							}
						}
					}
				}
			}
			
		}
	}
	
	boolean checkCollision(Tetrominoe test) {
		for(int i = 0; i < Tetrominoe.blockNum; i++) {
			if(fixedBlock[test.blocks[i].pos[Block.Y]][test.blocks[i].pos[Block.X]] != null)
				return true;
		}
		
		return false;
	}
	
	class TetrisKeyListener implements KeyListener{

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			// Make changes according to keyboard inputs
			int key = e.getKeyCode();
			
			switch(key) { // Before moving the block, we should check collision
			case KeyEvent.VK_LEFT:
				if(!checkCollision(new Tetrominoe(curTetro, -1, 0))) {
					curTetro.mvLeft();
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(!checkCollision(new Tetrominoe(curTetro, 1, 0))) {
					curTetro.mvRight();
				}
				break;
			case KeyEvent.VK_UP:
				/*
				if(!checkCollision(new Tetrominoe(curTetro, 0, -1))) {
					curTetro.mvUp();
				}*/
				Tetrominoe tmpTetro = new Tetrominoe(curTetro);
				tmpTetro.rotate();
				if(!checkCollision(tmpTetro)) {
					curTetro.rotate();
				}
				break;
			case KeyEvent.VK_DOWN:
				if(!checkCollision(new Tetrominoe(curTetro, 0, 1))) {
					curTetro.mvDown();
				}
				break;
			case KeyEvent.VK_SPACE:
				DropTetro();
				break;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
	@Override
	void Initialize() {
		// TODO Auto-generated method stub
		GameOver = false;
		
		// Set up timer
		timer = new Timer(700, timerMvDown);
		timer.start();
		
		// Create the first Tetrominoe
		curTetro = new Tetrominoe();
				
		// Initialize fixedBlock array with Walls
		fixedBlock = new Block[HEIGHT + 1][WIDTH + 2];
		for(int i = 0; i < HEIGHT; i++) {
			fixedBlock[i][LEFT_WALL] = new Block(LEFT_WALL, i); // Left Wall
			fixedBlock[i][RIGHT_WALL] = new Block(RIGHT_WALL, i); // Right Wall
		}
		
		for(int j = 0; j < WIDTH + 2; j++) {
			fixedBlock[HEIGHT][j] = new Block(j, HEIGHT);
		}
	}

	@Override
	void Update() {
		// TODO Auto-generated method stub
		if(checkCollision(new Tetrominoe(curTetro, 0, 1))) { 
			fixTetro(); // fixTetro function should have been called from here only once
			
			if(checkCollision(new Tetrominoe())) { // Can't generate more Tetrominoes
				GameOver = false;
			} else {
				curTetro = new Tetrominoe();
			}
		}
	}

	@Override
	void Render() {
		// TODO Auto-generated method stub
		// Draw
		mainFrame.remove(board);
		board = new JPanel() {
			public void paintComponent(Graphics g) {
				Graphics2D gBlock;
				Block block;
				for(int i = 0; i < Tetris.HEIGHT + 1; i++) { // Draw wall
					for(int j = 0; j < Tetris.WIDTH + 2; j++) {
						if(fixedBlock[i][j] != null) {
							gBlock = (Graphics2D) g;
							block = fixedBlock[i][j];
							
							if(block.color != null) { // fixedBlock from Tetrominoe
								gBlock.setColor(block.color);
								gBlock.fillRect(OFFSET + block.pos[Block.X] * Block.edgeLen, OFFSET 
										+ block.pos[Block.Y] * Block.edgeLen, Block.edgeLen, Block.edgeLen);
								//gBlock.fillRoundRect(OFFSET + block.pos[Block.X] * Block.edgeLen, OFFSET 
								//		+ block.pos[Block.Y] * Block.edgeLen, Block.edgeLen, Block.edgeLen, 5, 5);
							} else { // fixedBlock from Wall
								gBlock.setColor(Color.BLACK);
								gBlock.drawRect(OFFSET + block.pos[Block.X] * Block.edgeLen, OFFSET 
										+ block.pos[Block.Y] * Block.edgeLen, Block.edgeLen, Block.edgeLen);
								//gBlock.fillRect(OFFSET + block.pos[Block.X] * Block.edgeLen, OFFSET 
								//		+ block.pos[Block.Y] * Block.edgeLen, Block.edgeLen, Block.edgeLen);
								//gBlock.drawRoundRect(OFFSET + block.pos[Block.X] * Block.edgeLen, OFFSET 
								//		+ block.pos[Block.Y] * Block.edgeLen, Block.edgeLen, Block.edgeLen, 5, 5);
								//gBlock.fillRoundRect(OFFSET + block.pos[Block.X] * Block.edgeLen, OFFSET 
								//		+ block.pos[Block.Y] * Block.edgeLen, Block.edgeLen, Block.edgeLen, 5, 5);
							}				
						}
					}
				}
				
				for(int i = 0; i < Tetrominoe.blockNum; i++) { // Draw current Tetrominoe
					gBlock = (Graphics2D) g;
					block = curTetro.blocks[i];
					gBlock.setColor(block.color);
					
					//gBlock.drawRect(OFFSET + block.pos[block.X] * block.edgeLen, OFFSET 
					//		+ block.pos[block.Y] * block.edgeLen, block.edgeLen, block.edgeLen);
					gBlock.fillRect(OFFSET + block.pos[Block.X] * Block.edgeLen, OFFSET 
							+ block.pos[Block.Y] * Block.edgeLen, Block.edgeLen, Block.edgeLen);
					//gBlock.fillRoundRect(OFFSET + block.pos[Block.X] * Block.edgeLen, OFFSET 
					//		+ block.pos[Block.Y] * Block.edgeLen, Block.edgeLen, Block.edgeLen, 5, 5);
				}
			}
		};
		board.setLocation(100, 100);
		mainFrame.add(board, BorderLayout.CENTER);
		mainFrame.setVisible(true);
		//mainFrame.repaint();
		//mainFrame.revalidate(); // These two functions are not working well
		// I choose to remove the old Panel and add a new one
	}

	@Override
	void Release() {
		// TODO Auto-generated method stub
		
	}
}
