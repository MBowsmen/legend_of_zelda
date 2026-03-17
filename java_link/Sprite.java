/*
 * Name: Michael Montana Bowman
 * Date: 10.17.25
 * Desc: extended by all sprites in game
 */

import java.awt.Graphics;

public abstract class Sprite
{
    //dimension and position variables for all sprites
    protected int x, y, w, h; 

    protected int spriteCollisionBoxX; 
    protected int spriteCollisionBoxY;

    //update method for each sprite to update on frames
    public abstract boolean update();

    //remove sprite if removeMode on and can be removed
    public abstract void remove();

    //draw sprite on to screen
    public abstract void draw(int currentRoomX, int currentRoomY, Graphics g); 

    //save sprite to JSON object
    public abstract Json marshal(); 

    //if sprite collides resolve
    public abstract void collisionResolution(Sprite b);

    //all sprites of locations and dimensions
    public Sprite(int x, int y, int h, int w)
    {
        this.x = x; 
        this.y = y; 
        this.h = h; 
        this.w = w; 

        spriteCollisionBoxX = x + w; 
        spriteCollisionBoxY = y + h; 
    }

    //if sprite is clicked on for removal 
    public boolean clickOnSprite(int x, int y) 
    {
        if(x <= spriteCollisionBoxX && x >= this.x &&
           y <= spriteCollisionBoxY && y >= this.y) 
            return true; //remove
        return false; 
    }

    /*
     * OBJET TYPE
     * overide in object.java to true to return if object 
     * is the correct type
     */
    public boolean isTree() 
    {
        return false; 
    }

    public boolean isLink()
    {
        return false; 
    }

    public boolean isTreasureChest()
    {
        return false; 
    }

    public boolean isBoomerang()
    {
        return false;
    }
    
    public int getX()
    {
        return x; 
    }

    public int getY()
    {
        return y;
    }

    public int getHeight()
    {
        return h;
    }

    public int getWidth()
    {
        return w; 
    }
}