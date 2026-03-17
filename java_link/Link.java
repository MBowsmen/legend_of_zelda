/*
 * Name: Michael Montana Bowman
 * Date: 10.17.25
 * Desc: creates new link instance 
 *       animates him
 *       controls collision and movement
 */

//creates drawable image varibale
import java.awt.image.BufferedImage;

//graphics to to draw on panel 
import java.awt.Graphics;

//extends sprite class 
public class Link extends Sprite
{
    //save link previous posiitons for collision adjustment
    private int previousX, previousY; 

    //links speed
    private int speed; 

    //animation variables
    private int linkDirection; 
	private int frameNum; 

    //link image array 
	public static final int NUM_DIRECTIONS = 4; 
    public static final int MAX_IMAGES_PER_DIRECTION = 11; 
    private BufferedImage[][] link_images = new BufferedImage[NUM_DIRECTIONS][MAX_IMAGES_PER_DIRECTION]; 

    //constants for link dimensions
    public static final int LINK_HEIGHT = 50;
    public static final int LINK_WIDTH = 50;
    
    public Link()
    {
        //extend sprite class x, y, h, w
        super(150, 150, LINK_HEIGHT, LINK_WIDTH);

        //set link speed
        speed = 8; 

        //assign previous positions
        previousX = x;
        previousY = y; 

        //int for updating index of which link image (1-44)
        int index = 1; 

        //load each link frame for animation 
        for(int i = 0; i < NUM_DIRECTIONS; i++)
            for(int j = 0; j < MAX_IMAGES_PER_DIRECTION; j++)
                link_images[i][j] = View.loadImage("link" + index++ + ".png"); 

        //assign beginning frames
        linkDirection = 0; 
        frameNum = 0; 
    }

    public boolean update() 
    {
        return true; 
    }

    public void collisionResolution(Sprite b)
    {
        /* 
         * if
         * links previous x position plus links width is less than or equal to the trees x position 
         * and
         * links current x position plus links width is greater than or equal to the trees x position
         * links new current x position is the trees x position minus links width
         */
        if(previousX + w <= b.getX() && x + w >= b.getX()) 
            x = b.getX() - w; 

        /*
         * if 
         * links previous x position is greater than or equal to the trees current x position plus the trees width 
         * and
         * links current x is less than or equal to the trees current x position plus the trees width 
         * links new current x position is the trees x position plus the trees width 
         */
        else if(previousX >= b.getX() + b.getWidth() && x <= b.getX() + b.getWidth()) 
            x = b.getX() + b.getWidth(); 

        /* 
         * if
         * links previous y position plus links height is less than or equal to the trees y position 
         * and
         * links current y position plus links height is greater than or equal to the trees y position
         * links new current y position is the trees y position minus links height
         */
        if(previousY + h <= b.getY() && y + h >= b.getY())
            y = b.getY() - h; 
        
         /*
         * if 
         * links previous y position is greater than or equal to the trees current y position plus the trees height 
         * and
         * links current y is less than or equal to the trees current y position plus the trees height 
         * links new current y position is the trees y position plus the trees height 
         */
        else if(previousY >= b.getY() + b.getHeight() && y <= b.getY() + b.getHeight()) 
            y = b.getY() + b.getHeight(); 
    }

    //save previous posiiton for collision adjustment
    public void savePreviousPosition()
    {
        previousX = x;
        previousY = y; 
    }

    //draw link on panel
    public void draw(int currentRoomX, int currentRoomY, Graphics g)
    {   
        //draw link images in array depending on direction (first index) and frame number (second index)
	    g.drawImage(link_images[linkDirection][frameNum], x-currentRoomX, y-currentRoomY, w, h, null);
    }

    //cycle through frames to animate link
    public void increaseFrameNum()
	{   
        //reset frames if at max
		if(frameNum >= MAX_IMAGES_PER_DIRECTION-1) //minus one for array indexing
			frameNum = 0;
		else
			frameNum++; //cycle through else
	}

    //reset frame num back to zero when link is standing still
	public void resetFrameNum()
	{
		frameNum = 0;
	}

    //marshal link 
    public Json marshal()
    {
        Json ob = Json.newObject();
        ob.add("x", x);
        ob.add("y", y);
        ob.add("w", w);
        ob.add("h", h);
        return ob;
    }

    @Override 
    public String toString()
    {
        return "Link (x,y) = (" + x + ", " + y + "), w = " + w + ", h = " + h;
    }

    @Override
    public boolean isLink()
    {
        return true; 
    }

    public void remove(){}

    public void setLinkDestination(int x, int y)
    {
        this.x = x;
        this.y = y; 
    }

    public void setLinkDirection(int i)
	{
		/*
		 * 0 = down
		 * 1 = left 
		 * 2 = right
		 * 3 = up
		 */
		linkDirection = i; 
	}

    public int getDirection()
    {
        return linkDirection; 
    }

    public int getLinkSpeed()
    {
        return speed;
    }
}
