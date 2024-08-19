import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList; // Store all the pipes
import java.util.Random; // Generate random positions for the pipes
import javax.swing.*;

public class Bird extends JPanel implements ActionListener, KeyListener {
    private static final long serialVersionUID = 1L;
    
    // Window dimensions
    int boardWidth = 720; 
    int boardHeight = 720; 
    
    // Images for game assets
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;
    
    // Bird properties and initial position
    int birdX = boardWidth / 8;
    int birdY = boardHeight / 2;
    int birdWidth = 40;
    int birdHeight = 40;
    
    // Class to hold bird properties
    class MyBird {
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;
        
        // Constructor to initialize bird image
        MyBird(Image img) {
            this.img = img;
        }
    }
    
    // Pipe properties
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64; // Pipe width in pixels
    int pipeHeight = 512; // Pipe height in pixels
    
    // Class to represent a pipe
    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false; // Track if bird has passed this pipe
        
        // Constructor to initialize pipe image
        Pipe(Image img) {
            this.img = img;
        }
    }
    
    // Game logic variables
    MyBird bird;
    int velocityX = -4; // Pipes move to the left to simulate bird moving right
    int velocityY = 0; // Initial vertical velocity
    int gravity = 1; // Gravity to pull the bird down by 1 pixel per frame
    ArrayList<Pipe> pipes; // List to store all the pipes
    Random random = new Random();
    
    // Timers for game loop and pipe placement
    Timer gameLoop;
    Timer placePipesTimer;
    
    // Game over flag
    boolean gameOver = false;
    
    // Score tracking
    double score = 0;
    
    // Constructor to set up the game
    Bird() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true); // Ensure the JPanel receives focus to handle key events
        addKeyListener(this); // Register key listener to handle user input
        
        // Load images
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
        
        // Initialize the bird object
        bird = new MyBird(birdImg);
        pipes = new ArrayList<Pipe>();
        
        // Timer to place new pipes at intervals
        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();    
            }
        });
        placePipesTimer.start();
        
        // Main game loop timer (16.6ms per frame, ~60 FPS)
        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();
    }
    
    // Method to place pipes on the screen
    public void placePipes() {
        // Randomize top pipe position and set opening space
        int randomPipeY = (int) (pipeY - pipeHeight / 4 - Math.random() * (pipeHeight / 2));
        int openingSpace = boardHeight / 4;
                
        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe); // Add top pipe to the list
        
        Pipe bottomPipe =  new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe); // Add bottom pipe to the list
    }
    
    // Override paintComponent to draw the game elements
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    
    // Method to draw game elements on the screen
    public void draw(Graphics g) {
        // Draw background image
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);
        
        // Draw bird
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);
        
        // Draw pipes
        for (int tubes = 0; tubes < pipes.size(); tubes++) {
            Pipe pipe  = pipes.get(tubes);
            g.drawImage(pipe.img,  pipe.x, pipe.y, pipe.width, pipe.height, null);
        }
        
        // Draw score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32)); // Set font size to 32px
        if(gameOver) {
            g.drawString("Game Over: " + String.valueOf((int) score), 10, 35); // Display game over text with score
        }
        else {
            g.drawString(String.valueOf((int) score), 10, 35); // Display current score
        }
    }
    
    // Method to update bird and pipe positions
    public void move() {
        // Update bird's vertical position
        velocityY += gravity; // Apply gravity
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0); // Prevent bird from moving above the screen
        
        // Update pipe positions and check for collisions
        for (int tubes = 0; tubes < pipes.size(); tubes++) {
            Pipe pipe  = pipes.get(tubes);
            pipe.x += velocityX; // Move pipes to the left
            
            // Check if bird has passed the pipe
            if(!pipe.passed && bird.x > pipe.x + pipe.width) { 
                pipe.passed = true;
                score += 0.5; // Increment score by 0.5 for each pipe passed (2 pipes per set)
            }
            
            // Check for collision between bird and pipe
            if(collision(bird, pipe)) {
                gameOver = true;
            }
        }
        
        // End game if bird falls below the screen
        if (bird.y > boardHeight) {
            gameOver = true;
        }
    }
    
    // Method to check for collision between bird and pipe
    public boolean collision(MyBird a, Pipe b) {
        return a.x < b.x + b.width && // Check if bird's left edge is left of pipe's right edge
               a.x + a.width > b.x && // Check if bird's right edge is right of pipe's left edge
               a.y < b.y + b.height && // Check if bird's top edge is above pipe's bottom edge
               a.y + a.height > b.y; // Check if bird's bottom edge is below pipe's top edge
    } 

    // Override actionPerformed to update game state
    @Override
    public void actionPerformed(ActionEvent e) {
        // Update bird and pipe positions
        move();
        repaint(); // Redraw game elements
        
        // Stop the game loop and pipe placement if game is over
        if (gameOver) {
            placePipesTimer.stop(); // Stop adding pipes to the list
            gameLoop.stop(); // Stop updating and redrawing frames
        }
    }

    // Handle key press events
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -9; // Make the bird jump
            if(gameOver) {
                // Restart the game by resetting variables
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placePipesTimer.start();
            }
        }
    }

    // Unused but required by KeyListener interface
    @Override
    public void keyTyped(KeyEvent e) {        
    }

    // Unused but required by KeyListener interface
    @Override
    public void keyReleased(KeyEvent e) {    
    }
}

