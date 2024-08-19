#FlappyBird Java Game
##Overview
This is a simple implementation of the popular Flappy Bird game in Java using the Swing library for graphical user interface (GUI) components. The game involves controlling a bird that moves through a series of pipes without colliding with them. The goal is to achieve the highest score by passing through as many pipes as possible.

##Features
- Smooth Graphics: The game features a smooth graphical display with a background, bird, and pipes.
- Random Pipe Placement: Pipes are placed randomly with different gaps, adding variety to the gameplay.
- Score Tracking: The game tracks the player's score based on the number of pipes passed.
- Game Over Detection: The game automatically detects when the bird collides with a pipe or goes off-screen, ending the game.
- Restart Functionality: Players can restart the game by pressing the space bar after a game over.

##Installation
- To run this Flappy Bird game, you need to have Java Development Kit (JDK) installed on your machine.

- Clone or Download the Repository: Download the source code or clone the repository from the version control system.
- Compile the Code: Use a Java compiler to compile the Bird.java file. For example, using the command line:
```bash
javac Bird.java
```
**Run the Game:** After compilation, run the game using the following command:
```bash
java Bird
```

## Customization
- Pipe Speed: Adjust the speed of the pipes by changing the velocityX variable.
- Gravity: Modify the gravity variable to change how quickly the bird falls.
- Pipe Gap: Change the openingSpace variable in the placePipes method to adjust the gap between the pipes.
  
## Known Issues
- The game currently runs at a fixed resolution of 720x720 pixels.
- The game may not scale well on different screen sizes.
  
## Future Improvements
- Responsive Design: Modify the game to support different screen resolutions.
- Additional Features: Add sound effects, multiple levels, or different bird types to enhance the game.
  
## License
This project is open-source. Feel free to modify and distribute the code as you wish.
