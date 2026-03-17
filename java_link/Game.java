/*
 * Name: Michael Montana Bowman
 * Date: 10.17.25
 * Desc: main file
 * 		 creates game space 
 *       updates to check if game is open and running
 */


//imports 
import javax.swing.JFrame;
import java.awt.Toolkit;


public class Game extends JFrame
{
	//window constants
	public final static int WINDOW_WIDTH = 700;
	public final static int WINDOW_HEIGHT = 500;
	
	//if game is running
	private boolean keepGoing; 

	//connect all MVC architecture
	private Model model = new Model(); 
	private Controller controller = new Controller(model);
	private View view = new View(controller, model); 


	public Game()
	{
		
		//create game window 
		this.setTitle("A4 - BOOMERANGE/CHEST");
		this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setFocusable(true);
		this.getContentPane().add(view);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

		//sets default value of keepGoing to true so update() can run
		keepGoing = true; 

		//listeners for keyboard and mouse input
		view.addMouseListener(controller);
		this.addKeyListener(controller);
		view.addMouseMotionListener(controller);
	}

	//runs while keepGoing is true
	public void run()
	{
		//while keepGoing is true 
		do
		{
			//checks if game is running
			keepGoing = controller.update();
			model.update();
			view.repaint(); // This will indirectly call View.paintComponent
			Toolkit.getDefaultToolkit().sync(); // Updates screen

			// Go to sleep for 50 milliseconds
			try
			{
				Thread.sleep(50);
			} 
			catch(Exception e) 
			{
				e.printStackTrace();
				System.exit(1);
			}
		}
		while(keepGoing);
	}


	//main
	public static void main(String[] args)
	{
		Game g = new Game();
		g.run();
	}
}
