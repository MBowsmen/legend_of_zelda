/*
 * Name: Michael Montana Bowman
 * Date: 10.17.25
 * Desc: TreasureChest objects 
 */

//creates chest image to draw
import java.awt.image.BufferedImage;

//allows chest image to be drawn to screen
import java.awt.Graphics;


public class TreasureChest extends Sprite
{ 
    //constants for tree dimensions
    public static final int TREASURECHEST_HEIGHT = 35;
    public static final int TREASURECHEST_WIDTH = 35;

    //boolean for chest despawn events
    private boolean despawnRupee = false; //if true despawns rupee
    private boolean chestDespawnTimer = false; //starts timer to despawn chest
    //private boolean removeChest = false; //if true removes chest 

    //amount of frames for chest despawn
    private int chestDespawnFrames = 80; 
    private int rupeeFreezeFrames = chestDespawnFrames - 5; 

    //chest collison box dimensions
    private int treasureChestBoxX = 0; 
    private int treasureChestBoxY = 0; 

    //chest images
    private static BufferedImage treasurechest_image = null; 
    private static BufferedImage rupee_image = null; 

    //keep track of current chest image
    private BufferedImage current_image = null; 

    /*
     * CONSTRUCTORS
     */
    public TreasureChest(int x, int y)
    {
        //extend sprite 
        super(x, y, TREASURECHEST_HEIGHT, TREASURECHEST_WIDTH);

        //load rupee and chest image 
        if(treasurechest_image == null) 
	        treasurechest_image= View.loadImage("treasurechest.png"); 
        
        if(rupee_image == null) 
            rupee_image = View.loadImage("rupee.png"); 
        
        //set current_image to chest
        current_image = treasurechest_image; 

        //set chest dimensions for collision
        treasureChestBoxX = x + TREASURECHEST_WIDTH; 
        treasureChestBoxY = y + TREASURECHEST_HEIGHT; 
    }

    //unmarshaling constructor
    public TreasureChest(Json ob)
    {
        super((int)ob.getDouble("x"),
              (int)ob.getDouble("y"),
              (int)ob.getDouble("w"),
              (int)ob.getDouble("h"));

        if(treasurechest_image == null) 
	        treasurechest_image = View.loadImage("treasurechest.png"); 
        
        if(rupee_image == null) 
            rupee_image = View.loadImage("rupee.png"); 

        current_image = treasurechest_image; 

        treasureChestBoxX = x + TREASURECHEST_WIDTH; 
        treasureChestBoxY = y + TREASURECHEST_HEIGHT; 
    }

    /*
     * METHODS
     */
    public boolean update() 
    {   
        //if chestDespawnTimer activated
        if(chestDespawnTimer)
            chestDespawnFrames--; //decrease
        
        //if timer reaches 0 or rupee is despawned or chest is removed
        if(chestDespawnFrames == 0 || despawnRupee) //|| removeChest)
            return false; //remove chest from panel 

        return true; 
    }

    //marshal chest object
    public Json marshal()
    {
        Json ob = Json.newObject();
        ob.add("x", x);
        ob.add("y", y);
        ob.add("w", w);
        ob.add("h", h);
        ob.add("type", 1); //identify what type while unmarshalling
        return ob;
    }

    //draw chest object
    public void draw(int currentRoomX, int currentRoomY, Graphics g)
    {
        g.drawImage(current_image, this.x - currentRoomX, this.y - currentRoomY, TREASURECHEST_WIDTH, TREASURECHEST_HEIGHT, null);
    }

    //draw chest object for editMode
    public static void drawStiffChest(Graphics g)
    {
        g.drawImage(treasurechest_image, 12, 12, 50, 50, null);
    }

    @Override 
    public String toString()
    {
        return "TreasureChest (x,y) = (" + x + ", " + y + "), w = " + w + ", h = " + h;
    }

    @Override
    public boolean isTreasureChest()
    {
        return true; 
    }

    public void collisionResolution(Sprite b)
    {
        //if chest
        if(current_image == treasurechest_image)
        {   
            //make rupee
            current_image = rupee_image; 
            chestDespawnTimer = true; //activate timer
        }
        
        //if image is rupee and freeze frames are over
        if(current_image == rupee_image && chestDespawnFrames < rupeeFreezeFrames)
            despawnRupee = true; //remove chest 
        
    }

    //remove chest from screen in removeMode
    public void remove()
    {
        despawnRupee = true; //or chest
    }

}
