/*
 * Name: Michael Montana Bowman
 * Date: 10.17.25
 * Desc: Controls graphic window with keyboard and mouse input
 */

//detect moust input
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

//detect keyboard input
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener; 
import java.awt.event.KeyEvent; 

//implements keyboard and mouse input
public class Controller implements ActionListener, MouseListener, KeyListener, MouseMotionListener
{
	//used to update game if the window is still open
	private boolean keepGoing;

	//create view and model objects to manipulate within control file
	private View view; 
	private Model model; 

	//keyboard variables to determine if a key is pressed 
	private boolean keyLeft;
	private boolean keyRight;
	private boolean keyUp;
	private boolean keyDown;
	private boolean keySpace;

	public static final int BOOMERANG_COOLDOWN = 10;  
	private boolean boomerangCountdown = false; 
	private int boomerangFrameCounter = BOOMERANG_COOLDOWN; 

	public Controller(Model m)
	{
		model = m; 
		keepGoing = true;
	}

	public void actionPerformed(ActionEvent e){}

	//update method runs in while loop continously 
	public boolean update()
	{
		//get link object
		Link link = model.getLink(); 
		
		//save link position for collision adjustment
		model.saveLinkPosiiton();

		//sets the current positioning of link
		int currentX = link.getX();
		int currentY = link.getY();

		//sets links speed 
		int linkSpeed = link.getLinkSpeed();

		if(keyRight) 
		{
			//increase link position by his speed
			currentX+= linkSpeed; 
			link.setLinkDestination(currentX, currentY);

			//change links direction depending on key input and increase animation frame
			link.setLinkDirection(2);
			link.increaseFrameNum();
		}

		if(keyLeft) 
		{
			currentX-= linkSpeed; 
			link.setLinkDestination(currentX, currentY);

			link.setLinkDirection(1);
			link.increaseFrameNum();
		}

		if(keyDown)
		{
			currentY+= linkSpeed; 
			link.setLinkDestination(currentX, currentY);

			link.setLinkDirection(0);
			link.increaseFrameNum();
		}
			
		if(keyUp)
		{
			currentY-= linkSpeed; 
			link.setLinkDestination(currentX, currentY);

			link.setLinkDirection(3);
			link.increaseFrameNum();
		}

		//if spcae is pressed spawn boomerang 
		if(keySpace)
		{	
			//create new boomerang spawned in links middle area
			if(!boomerangCountdown)
			{
				Boomerang newBoomerang = new Boomerang(link.getX()+link.getWidth()/2, link.getY()+link.getHeight()/2, link.getDirection());
				model.spawnBoomerang(newBoomerang);
				boomerangCountdown = true; 
			}
		}

		//boomerang cooldown
		if(boomerangCountdown)
		{
			if(boomerangFrameCounter == 0)
			{
				boomerangFrameCounter = BOOMERANG_COOLDOWN; 
				boomerangCountdown = false;
			}
			else
				boomerangFrameCounter--; 
		}


		//if link is not moving reset animation
		if(!keyUp && !keyDown && !keyRight && !keyLeft)
			link.resetFrameNum(); 

		//moves camera between rooms determined by links posiiton 
		view.moveCameraWithLink();

		//the Controller keeps track of whether or not we have quit the program and
		//returns this value to the Game engine of whether or not to continue the game loop		
		 return keepGoing;
	}

	//mouseListener interface
	public void mousePressed(MouseEvent e)
	{
		//mouse coordinates 
		int mouesX = e.getX(); 
		int mouesY = e.getY(); 

		//check if box is clicked
		boolean boxClicked = (mouesX <= View.BOX_DIMENSION) && (mouesY <= View.BOX_DIMENSION);

		//if in editMode and box is clicked cycle items 
		if(view.getEditMode() && boxClicked)
			model.setItemNum();

		 //convert current coordinante into world coordinates for tree placement
		int worldX = e.getX() + view.getCurrentRoomX(); 
		int worldY = e.getY() + view.getCurrentRoomY(); 

		//if edit mode is enabled and add mode you can place trees
		if(view.getEditMode() && view.getAddMode())
		{	
			//if moues input is not in editMode box
			if(!boxClicked)
				model.addSprite(worldX, worldY); //add sprite
		}
		
		//if edit mode is enabled and remove mode is enabled you can remove trees
		if(view.getEditMode() && view.getRemoveMode())	
		{
			//if mouse input is not in editMode box 
			if(!boxClicked)
				model.removeSprite(worldX, worldY); //remove sprite
		}
	}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mouseClicked(MouseEvent e) {}

	public void mouseDragged(MouseEvent e) {}

	public void mouseMoved(MouseEvent e) {}

	//set boolean variable to true if key is pressed
	public void keyPressed(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_RIGHT: 
				keyRight = true; 
				break;
			case KeyEvent.VK_LEFT: 
				keyLeft = true; 
				break;
			case KeyEvent.VK_UP: 
				keyUp = true; 
				break;
			case KeyEvent.VK_DOWN: 
				keyDown = true; 
				break;
			case KeyEvent.VK_SPACE:
				keySpace = true; 
				break;
		}
	}

	//sets boolean to false once key is released 
	public void keyReleased(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_RIGHT:
				keyRight = false; 
				break;
			case KeyEvent.VK_LEFT: 
				keyLeft = false; 
				break;
			case KeyEvent.VK_UP: 
				keyUp = false; 
				break;
			case KeyEvent.VK_DOWN: 
				keyDown = false; 
				break;
			case KeyEvent.VK_SPACE:
				keySpace = false; 
				break;
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
		}

		//creates generic character to test (allows SHIFT + 'key' inputs)
		char c = Character.toLowerCase(e.getKeyChar());

		//quit game
		if(c == 'q')
			System.exit(0);

		if(c == 'e') //edit mode
		{
			view.editToggle(); 
		}

		if(c == 'a') //add mode
		{
			view.setAddMode(true);
			view.setRemoveMode(false); 
		}

		if(c == 'r') //remove mode
		{
			view.setRemoveMode(true);
			view.setAddMode(false); 
		}

		if(c == 'c') //clear 
		{
			if(view.getEditMode())
				view.clearPanel(); 
		}

		if(c == 's') //save
		{
			model.marshal().save("map.json"); 
		}

		if(c == 'l') //load
		{
			model.loadMap(Json.load("map.json"));
		}

	}

	public void keyTyped(KeyEvent e) {}

	public void setView(View v)	
	{	
		view = v; 	
	}
}
