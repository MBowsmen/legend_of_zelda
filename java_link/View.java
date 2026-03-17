/*
 * Name: Michael Montana Bowman
 * Date: 10.17.25
 * Desc: creaets jpanel for game interface 
 * 		 helps control inputs from controller relating to model
 */

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.awt.Color;
import java.util.Iterator;

public class View extends JPanel
{	
	//dimension for edit box
	public static final int BOX_DIMENSION = 75;
	
	//buffered image object (turtle imaage) 
	private static BufferedImage missingTexture; 	

	//editMode variables
	private boolean editMode;
	private boolean addMode;
	private boolean removeMode;

	//room position variables
	private int currentRoomX;
	private int currentRoomY;  

	//camera buffer to allow link to pass through rooms quicker (higher = faster)
	private static final int CAMERA_BUFFER = 25;

	//make model visible to View.java
	private Model model;

	public View(Controller c, Model m)
	{	
		//refernce to controller
		c.setView(this); 

		//load missing texture default
		try
		{	
			missingTexture = ImageIO.read(new File("images/missingTexture.png"));
		}
		//system error if failure (exit)
		catch(Exception e) 
		{
			e.printStackTrace(System.err);
			System.exit(1);
		}

		//load model object
		model = m;

		//mode sets
		editMode = false;
		addMode = false;
		removeMode = false;

		//room position default 
		currentRoomX = 0;
		currentRoomY = 0;
	}

	public boolean getEditMode()
	{
		return editMode;  
	}

	public boolean getAddMode()
	{
		return addMode; 
	}

	public boolean getRemoveMode()
	{
		return removeMode; 
	}

	public int getCurrentRoomX()
	{
		return currentRoomX; 
	}

	public int getCurrentRoomY()
	{
		return currentRoomY; 
	}

	public void setEditMode(boolean b)
	{
		editMode = b; 
	}

	public void setAddMode(boolean b)
	{
		addMode = b; 
	}

	public void setRemoveMode(boolean b)
	{
		removeMode = b; 
	}

	//looad image static function
	public static BufferedImage loadImage(String s)
	{
		try
		{	
			return ImageIO.read(new File("images/" + s));
		}
		//system error if failure (exit)
		catch(Exception e) 
		{
			e.printStackTrace(System.err);
			System.exit(1);
		}
		
		//return missing texture image by default
		return missingTexture; 
	}

	//paint JPanel
	public void paintComponent(Graphics g)
	{
		//background
		g.setColor(new Color(150, 150, 150));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		

		//drawing sprites
		for(int i = 0; i < model.numberOfSprites(); i++) 
			model.getSprite(i).draw(currentRoomX, currentRoomY, g);

		//if editMode and addMode toggled 
		if(editMode && addMode)
		{	
			//create green square
			g.setColor(new Color(0, 255, 0));
			g.fillRect(0, 0, 75, 75);

			//draw still images
			if(model.getItemNum() == 0) // tree
				Tree.drawStiffTree(g); 
			if(model.getItemNum() == 1) // chest
				TreasureChest.drawStiffChest(g);
		}

		//if editMode and removeMode togglesd
		if(editMode && removeMode)
		{	
			//create red square
			g.setColor(new Color(255, 0, 0));
			g.fillRect(0, 0, 75, 75);

			//draw still image
			if(model.getItemNum() == 0) // tree
				Tree.drawStiffTree(g); 
			if(model.getItemNum() == 1) // chest
				TreasureChest.drawStiffChest(g);
		}
	}

	//toggle edit mode
	public void editToggle()
	{
		//toggle editmode
		editMode = !editMode;

		//turn modes off if editmode not enabled
		if(!editMode)
		{
			addMode = false;
			removeMode = false;
		}
		
		//default add mode enabled
		else 
		{
			addMode = true; 
			removeMode = false; 
		}
		
	}

	//clear panel
	public void clearPanel()
	{
		model.clearSpriteArray(); 
	}


	//camera movement functions
	public void moveCameraLeft()
	{
		if(currentRoomX == Game.WINDOW_WIDTH)
			currentRoomX -= Game.WINDOW_WIDTH;
	}

	public void moveCameraRight()
	{
		if(currentRoomX == 0)
			currentRoomX += Game.WINDOW_WIDTH;
	}

	public void moveCameraUp()
	{
		if(currentRoomY == Game.WINDOW_HEIGHT)
			currentRoomY -= Game.WINDOW_HEIGHT; 
	}

	public void moveCameraDown()
	{
		if(currentRoomY == 0)
			currentRoomY += Game.WINDOW_HEIGHT;
	}

	//allows camera to move with link controlling it
	public void moveCameraWithLink() 
	{
		//get link object 
		Link link = model.getLink(); 

		//if links current x is greater than the game windows width move right
		if(link.getX() > Game.WINDOW_WIDTH - CAMERA_BUFFER)
			moveCameraRight();
		
		//if links current x is less than the game windows width move left
		if(link.getX() < Game.WINDOW_WIDTH - CAMERA_BUFFER)
			moveCameraLeft();

		//if links current y is greater than the game windows width move down
		if(link.getY() > Game.WINDOW_HEIGHT - CAMERA_BUFFER)
			moveCameraDown();
		
		//if links current y is less than the game windows width move up
		if(link.getY() < Game.WINDOW_HEIGHT - CAMERA_BUFFER)
			moveCameraUp();
	}

}


