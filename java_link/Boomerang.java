/*
 * Name: Michael Montana Bowman
 * Date: 10.17.25
 * Desc: Boomerang objects 
 */

import java.awt.image.BufferedImage;
import java.awt.Graphics;


public class Boomerang extends Sprite
{ 
    //constants for tree dimensions
    public static final int BOOMERANG_HEIGHT = 20;
    public static final int BOOMERANG_WIDTH = 20;

    //constant for boomerang speed
    public static final int BOOMERANG_SPEED = 15; 

    //direciton boomerang flys
    private int boomerangDirection = 0; 

    //boolean to despawn boomerang if collided
    private boolean despawnBoomerang = false; 

    //boomerang image
    private static BufferedImage boomerang_image = null; 

    public Boomerang(int x, int y, int direction)
    {   
        //extend sprite 
        super(x, y, BOOMERANG_HEIGHT, BOOMERANG_WIDTH);

        //find link direction and fly boomerang in directoin
        boomerangDirection = direction; 

        //load boomerang image
        if(boomerang_image == null) 
	        boomerang_image = View.loadImage("boomerang1.png"); 

    }

    //unmarshaling constructor
    public Boomerang(Json ob)
    {
        super((int)ob.getDouble("x"),
              (int)ob.getDouble("y"),
              (int)ob.getDouble("w"),
              (int)ob.getDouble("h"));

        if(boomerang_image == null) 
	        boomerang_image = View.loadImage("boomerang1.png"); 
    }

    public boolean update() 
    {
        //fly boomerang in direction with speed
        switch(boomerangDirection)
        {
            case 0: //down
                setBoomerangPosition(x, y+BOOMERANG_SPEED); 
                break; 
            case 1: //left
                setBoomerangPosition(x-BOOMERANG_SPEED, y); 
                break;
            case 2: //right
                setBoomerangPosition(x+BOOMERANG_SPEED, y); 
                break; 
            case 3: //up
                setBoomerangPosition(x, y-BOOMERANG_SPEED);
                break; 
            default: 
                System.out.println("BOOMERANG UPDATE DIRECTION ERROR");//error
        }

        //if boomerang despawn is active remove boomerang
        if(despawnBoomerang)
            return false; 

        return true;
    }

    //marshal tree object
    public Json marshal()
    {
        Json ob = Json.newObject();
        ob.add("x", x);
        ob.add("y", y);
        ob.add("w", w);
        ob.add("h", h);
        return ob;
    }

    //draw boomerang 
    public void draw(int currentRoomX, int currentRoomY, Graphics g)
    {
        g.drawImage(boomerang_image, this.x - currentRoomX, this.y - currentRoomY, BOOMERANG_WIDTH, BOOMERANG_HEIGHT, null);
    }

    @Override 
    public String toString()
    {
        return "Boomerang (x,y) = (" + x + ", " + y + "), w = " + w + ", h = " + h;
    }

    @Override
    public boolean isBoomerang()
    {
        return true; 
    }

    public void collisionResolution(Sprite b)
    {   
        //if boomerang touches like don't despawn
        if((b.isLink())) 
            despawnBoomerang = false; 
        else //despawn otherwise
           despawnBoomerang = true; 
    }

    //can not remove boomerang in editMode
    public void remove(){}

    public void setBoomerangPosition(int x, int y) 
    {
        this.x = x;
        this.y = y; 
    }

}
