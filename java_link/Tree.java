/*
 * Name: Michael Montana Bowman
 * Date: 10.17.25
 * Desc: tree objects (walls)
 */

//allow image on panel to be drawn
import java.awt.image.BufferedImage;

//draws image 
import java.awt.Graphics;


public class Tree extends Sprite
{ 
    //constants for tree dimensions
    public static final int TREE_HEIGHT = 50;
    public static final int TREE_WIDTH = 50;

    //tree image
    private static BufferedImage tree_image = null; 

    //if removeTree is true tree is removed from screen
    private boolean removeTree = false; 

    public Tree(int x, int y)
    {
        //extend sprite
        super(x, y, TREE_HEIGHT, TREE_WIDTH);

        //load tree_image 
        if(tree_image == null) 
	        tree_image = View.loadImage("tree.png"); 
    }

    //unmarshaling constructor
    public Tree(Json ob)
    {
        super((int)ob.getDouble("x"),
              (int)ob.getDouble("y"),
              (int)ob.getDouble("w"),
              (int)ob.getDouble("h"));

        //load tree_image
        if(tree_image == null) 
	        tree_image = View.loadImage("tree.png"); 
    }

    //checks if tree is still valid
    public boolean update() 
    {
        if(removeTree)
            return false; 

        return true; 
    }

    //remove tree 
    public void remove() 
    {
        removeTree = true;
    }

    //marshal tree object
    public Json marshal()
    {
        Json ob = Json.newObject();
        ob.add("x", x);
        ob.add("y", y);
        ob.add("w", w);
        ob.add("h", h);
        ob.add("type", 0);
        return ob;
    }

    //draw tree to screen
    public void draw(int currentRoomX, int currentRoomY, Graphics g)
    {
        g.drawImage(tree_image, this.x - currentRoomX, this.y - currentRoomY, TREE_WIDTH, TREE_HEIGHT, null);
    }

    //draw tree for edit mode
    public static void drawStiffTree(Graphics g)
    {
        g.drawImage(tree_image, 12, 12, TREE_WIDTH, TREE_HEIGHT, null);
    }

    @Override 
    public String toString()
    {
        return "Tree (x,y) = (" + x + ", " + y + "), w = " + w + ", h = " + h;
    }

    @Override
    public boolean isTree()
    {
        return true; 
    }

    @Override
    public boolean clickOnSprite(int x, int y) 
    {
        return (x == this.x && y == this.y);
    }

    //no collisionResolution for tree
    public void collisionResolution(Sprite b) {}

}
