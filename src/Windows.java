import javax.swing.*;

public class Windows {

	public static void main(String[] args) {
		//Create window
		int boardWidth = 540; 
		int boardHeight = 720; 
		
		//create JFrame
		JFrame frame = new JFrame("Flappy Bird");
		//set window properties
		frame.setVisible(true);
		frame.setSize(boardWidth, boardHeight);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//create instance of Bird
		Bird bird = new Bird();
		frame.add(bird);
		frame.pack(); //made the w & h without the header
		bird.requestFocus();
		frame.setVisible(true);

	}

}
