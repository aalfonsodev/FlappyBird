import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList; //store all the pipes
import java.util.Random; //to place the pipes randomly
import javax.swing.*;

public class Bird extends JPanel implements ActionListener, KeyListener{
	private static final long serialVersionUID = 1L;
	//Create window
	int boardWidth = 720; 
	int boardHeight = 720; 
	
	//Images
	Image backgroundImg;
	Image birdImg;
	Image topPipeImg;
	Image bottomPipeImg;
	
	//Add variable for the Bird || set the bird in desire position inside the screen 
	int birdX = boardWidth/8;
	int birdY = boardHeight/2;
	int birdWidth = 40;
	int birdHeight = 40;
	
	//hold the values inside a class
	class MyBird {
		int x = birdX;
		int y = birdY;
		int width = birdWidth;
		int height = birdHeight;
		Image img;
		//MyBird Constructor
		MyBird(Image img){
			this.img = img;
		}
	}
	
	//Pipes
	int pipeX = boardWidth;
	int pipeY = 0;
	int pipeWidth = 64; //64px
	int pipeHeight = 512;
	
	class Pipe {
		int x = pipeX;
		int y = pipeY;
		int width = pipeWidth;
		int height = pipeHeight;
		Image img;
		boolean passed = false;
		//Pipe Constructor
		Pipe(Image img){
			this.img = img;
		}
	}
	
	//Game logic
	MyBird bird;
	int velocityX = -4; //move pipes to the left (simulates bird moving right)
	int velocityY = 0; //0px up initialization
	int gravity = 1; //slow down every frame by 1px
	//Storing all the pipes in a list
	ArrayList<Pipe> pipes;
	Random random = new Random();
	
	//variable for game loop
	Timer gameLoop;
	Timer placePipesTimer;
	
	//GameOver
	boolean gameOver = false;
	
	//track pipes for score
	double score = 0;
	
	//Constructor
	Bird(){
		setPreferredSize(new Dimension(boardWidth, boardHeight));
		setFocusable(true); //make sure that JPanel take the event
		addKeyListener(this); //to make sure we check the Key functions
		
		//load image
		backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
		birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
		topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
		bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
		
		//create the bird object
		bird = new MyBird(birdImg);
		pipes = new ArrayList<Pipe>();
		 
		//place pipes timer
		placePipesTimer = new Timer(1500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				placePipes();	
			}
		});
		placePipesTimer.start();
		
		//game timer
		gameLoop = new Timer(1000/60, this); //1000/60 = 16.6 frames per second
		gameLoop.start();
	}
	
	public void placePipes() {
		//Math.random = (0-1) || pipeHeight / 2 = 0-256 || pipeY = 0 || pipeHeight / 4 = 0-128
		int randomPipeY = (int) (pipeY - pipeHeight / 4 - Math.random() * (pipeHeight / 2)); //top pipes position
		int openingSpace = boardHeight / 4;
				
		Pipe topPipe = new Pipe(topPipeImg);
		topPipe.y = randomPipeY;
		pipes.add(topPipe); //add to the [] list
		
		Pipe bottomPipe =  new Pipe(bottomPipeImg);
		bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
		pipes.add(bottomPipe);
	}
	
	//draw image to background using function from JPanel
	public void paintComponent(Graphics g) {
		super.paintComponent(g); //super refers to the parent class JPanel
		draw(g);
	}
	
	public void draw(Graphics g) {
		//background || formula position (backgroundImg, x, y, width, height, null)
		g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null); //set position || 0, 0 is the top left corner
		
		//bird
		g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);
		
		//pipes
		for (int tubes = 0; tubes < pipes.size(); tubes++) {
			Pipe pipe  = pipes.get(tubes);
			g.drawImage(pipe.img,  pipe.x, pipe.y, pipe.width, pipe.height, null);
		}
		
		//score
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.PLAIN, 32)); //32px size
		if(gameOver) {
			g.drawString("Game Over: " + String.valueOf((int) score), 10, 35); //10 right 35px down
		}
		else {
			g.drawString(String.valueOf((int) score), 10, 35);
		}
	}
	
	public void move() {
		//update the bird position
		velocityY += gravity; //slow down every frame by 1px
		bird.y += velocityY;
		bird.y = Math.max(bird.y, 0); //no allow to past the header
		
		//pipes
		for (int tubes = 0; tubes < pipes.size(); tubes++) {
			Pipe pipe  = pipes.get(tubes);
			pipe.x += velocityX;
			
			//if bird passed the right side of the pipe
			if(!pipe.passed && bird.x > pipe.x + pipe.width) { 
				pipe.passed = true;
				score += 0.5; //0.5 because there are 2 pipes, so 0.5*2 = 1 for set of pipes
			}
			
			if(collision(bird, pipe)) {
				gameOver = true;
			}
		}
		//GameOver
		if (bird.y > boardHeight) {
			gameOver = true;
		}
	}
	
	public boolean collision(MyBird a, Pipe b) {
		return a.x < b.x + b.width && //a top left corner doesn't reach b top right 
				a.x + a.width > b.x && //a top right corner passes b top left corner
				a.y < b.y + b.height && //a top left corner doesn't reach b bottom left corner
				a.y + a.height > b.y; //a bottom left corner passes b top left corner
	} 

	//Needed to make work the implements ActionListener on main class
	@Override
	public void actionPerformed(ActionEvent e) {
		//update position of bird before repaint with move()
		move();
		repaint();
		if (gameOver) {
			placePipesTimer.stop(); //stop adding pipes to the [] list
			gameLoop.stop(); //stop repaint and updated frames
		}
		
	}

	//KeyListener from main class must implement the inherited abstract method KeyListener.keyTyped(KeyEvent)
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			velocityY = -9;
			if(gameOver) {
				//restart the game by resetting the conditions
				bird.y = birdY;
				velocityY = 0;
				pipes.clear();
				score= 0;
				gameOver = false;
				gameLoop.start();
				placePipesTimer.start();
			}
		}
		
	}
	@Override
	public void keyTyped(KeyEvent e) {		
	}
	@Override
	public void keyReleased(KeyEvent e) {	
	}

}
