/*
 * Name: Michael Montana Bowman
 * Date: 10.17.25
 * Desc: removal of sprites on screen
 * 		 collision of sprites
 */

//import array list
import java.util.ArrayList;

public class Model
{
	///sprite array
	private ArrayList<Sprite> sprites = new ArrayList<Sprite>(); 

	//sprites that need to be added once updated
	private ArrayList<Sprite> spritesToAdd = new ArrayList<Sprite>(); 

	//list of sprites that can be added
	private ArrayList<Sprite> itemsICanAdd = new ArrayList<Sprite>(); 

	//index for itemsICanAdd array
	private int itemNum = 0; 

	//items that can be added instances
	Tree treeAdd = new Tree(0, 0); 
	TreasureChest chestAdd = new TreasureChest(0, 0); 

	//create link object
	private Link link = new Link(); 

	//checks if a tree is in position being clicked on
	private boolean spriteHere; 

	public Model()
	{
		//set spriteHere to false unless changed
		spriteHere = false;

		//autmoatically load map on start
		loadMap(Json.load("map.json"));
		
		//add what items that can be created in game
		itemsICanAdd.add(treeAdd); 
		itemsICanAdd.add(chestAdd); 
	}	

	//unmarshal constructor
	public Model(Json ob)
    {	
		//create JSON object temp list 
        Json tmpList = ob.get("sprites");

		//for each item in temp list create new 
		for(int i = 0; i < tmpList.size(); i++)
		{	
			//tree (tree is type 0)
			if((tmpList.get(i).getDouble("type")) == 0) //0 is tree
				sprites.add(new Tree(tmpList.get(i)));
			//chest (chest is type 1)
			if((tmpList.get(i).getDouble("type")) == 1) //1 is chest
				sprites.add(new TreasureChest(tmpList.get(i)));
		}
    }

	//called while game is running
	public void update()
	{	
		/*
		 * these steps were taken because I was getting constant index out of bound errors when removing and adding
		 * this forces the remove and add to take place after updating with little visual impact
		 * the only difference is the clear method takes a little longer 
		 * instructions say trees need to always be "valid" but I could not find a work around and it makes sense
		 * for all objects to be invalid and deleted if off screen when necessary
		 */

		//if a sprite is invalid remove it from the screen
		for(int i = 0; i < sprites.size(); i++)
			if(!(sprites.get(i).update())) //update is false causing sprite to be removed
				sprites.remove(sprites.get(i)); //remove
		
		//add sprites that recently got added to sprites list
		for(int i = 0; i < spritesToAdd.size(); i++)
			sprites.add(spritesToAdd.get(i)); //add each element in sprite to add
		
		//clear list
		spritesToAdd.clear(); 

		//check for sprite collision and adjust properly 
		spriteCollisionCheck(); 
	}

	public void addSprite(int x, int y)
	{

		//snap x and y if sprite being added is tree
		if(itemsICanAdd.get(itemNum).isTree())//tree snap
		{
			x = Math.floorDiv(x, Tree.TREE_WIDTH) * Tree.TREE_WIDTH;
			y = Math.floorDiv(y, Tree.TREE_HEIGHT) * Tree.TREE_HEIGHT; 
		}
		else //if object is a treasure chest (update for cucoos later...)
		{
			x = x - itemsICanAdd.get(itemNum).getWidth()/2; //place center
			y = y - itemsICanAdd.get(itemNum).getHeight()/2; //place center
		}

		//checks entire tree array to see if theres already a tree at desired location
		for(int i = 0; i < sprites.size(); i++)
		{	
			//if there is a sprite already where user tried to click
			if(sprites.get(i).clickOnSprite(x, y))
				return; //leave
		}

		//if item being added is a tree
		if(itemsICanAdd.get(itemNum).isTree())
			spritesToAdd.add(new Tree(x, y)); //create tree
		
		//if item being added is a chest
		if(itemsICanAdd.get(itemNum).isTreasureChest())
			spritesToAdd.add(new TreasureChest(x, y)); //create chest
		
		//loop through sprite array
		for(int i = 0; i < sprites.size(); i++)
		{
			//create last item varibale
			Sprite lastItem = null; 

			//if sprites to add has elements
			if(spritesToAdd.size() > 0)
				lastItem = spritesToAdd.get(spritesToAdd.size()-1); //set last item to most recent item in array

			//if a collision is detected with an already existing sprite and the last item is not already deleted
			if(collisionDetection(lastItem, sprites.get(i)) && lastItem != null)
			{
				spritesToAdd.remove(lastItem); //delete last item
				break; //once null no need to check for other items
			}
		}
	}


	//remove tree from array
	public void removeSprite(int x, int y)
	{
		//if tree 
		if(itemsICanAdd.get(itemNum).isTree())//tree snap
		{
			//snap x and y
			x = Math.floorDiv(x, 50) * 50; 
			y = Math.floorDiv(y, 50) * 50; 
		}

		//search sprite array for closest sprite within moues click
		for(int i = 0; i < sprites.size(); i++)
		{
			//if sprite is clicked on and its the current item
			if(sprites.get(i).clickOnSprite(x, y) && matchingItemNum(sprites.get(i)))
				sprites.get(i).remove(); //remove 
		}
	}

	//checks to see if sprite is current item being editted
	public boolean matchingItemNum(Sprite s)
	{	
		//if current item is tree and sprite is tree
		if(itemsICanAdd.get(itemNum).isTree() && s.isTree())
			return true; //return true 

		//if current item is chest and sprite is chest
		if(itemsICanAdd.get(itemNum).isTreasureChest() && s.isTreasureChest())
			return true; //return true
		
		return false; //false
	}

	//spawn boomerang 
	public void spawnBoomerang(Boomerang b)
	{
		sprites.add(b); 
	}

	//clear all trees from panel
	public void clearSpriteArray()
	{
		//for each element in sprite array 
		for(int i = 0; i < sprites.size(); i++)
			sprites.get(i).remove(); //remove
		
		//add link back into game
		sprites.add(link); 
	}

	//marshaller
	public Json marshal()
    {	
		//create JSON object
        Json ob = Json.newObject();

		//create JSON list
        Json tmpList = Json.newList();

		//for each element in sprites add to list
        ob.add("sprites", tmpList);

		//loop through sprite list and adds each sprite 
        for(int i = 0; i < sprites.size(); i++)
			//add all items that aren't link
			if(!sprites.get(i).isLink())
        		tmpList.add(sprites.get(i).marshal());

		//return JSON object
        return ob;
    }

	//unmarshal or load map
	public void loadMap(Json ob)
	{	
		//clear array so no collision with place elements
		clearSpriteArray();	

		//create JSON object temp list 
        Json tmpList = ob.get("sprites");

		//for each item in tmpList 
		for(int i = 0; i < tmpList.size(); i++)
		{	
			//if object is type tree create tree
			if((tmpList.get(i).getDouble("type")) == 0) //0 is tree
				sprites.add(new Tree(tmpList.get(i)));

			//if object is type is chest create chest
			if((tmpList.get(i).getDouble("type")) == 1) //1 is chest
				sprites.add(new TreasureChest(tmpList.get(i)));
		}
	}

	public boolean collisionDetection(Sprite a, Sprite b)	
	{
		//if sprite a current x is greater than the sprite b x plus its width there is no collision
		if (a.getX() >= b.getX() + b.getWidth()) 
			return false; 

		//if sprite a current x plus links width less than or equal to sprite b x there is no collision
		if (a.getX() + a.getWidth() <= b.getX()) 
			return false; 
		//if sprite a current y is greater than the sprite b y plus its height there is no collision
		if (a.getY() >= b.getY() + b.getHeight()) 
			return false; 

		//if sprite a current y plus links height less than or equal to sprite b y there is no collision
		if (a.getY() + a.getHeight() <= b.getY()) 
			return false; 

		return true; 
	}

	public void spriteCollisionCheck()
	{	
		//detect collision
		boolean collision = false; 

		//comparison sprites
		Sprite a = null; 
		Sprite b = null;  

		//for loops cycle and compare all sprites
		for(int i = 0; i < sprites.size(); i++)
		{
			for(int j = sprites.size()-1; j >= 1; j--)
			{
				//assign sprites 
				a = sprites.get(i); 
				b = sprites.get(j); 

				//if a and b are the same OR a OR b is null OR they are both trees 
				if((a == b) || 
				   (a == null) || 
				   (b == null) || 
				   ((a.isTree()) && (b.isTree())))
					continue; //skip
				else 
				{
					//detect collision and check what is a possible collision
					if(collisionDetection(a,b) && spriteCollider(a,b))
					{
						a.collisionResolution(b); //resolve
						b.collisionResolution(a); //resolve
					}
				}
			}
		}
	}

	//save link previous position for adjustment
	public void saveLinkPosiiton() {link.savePreviousPosition();}

	public boolean spriteCollider(Sprite a, Sprite b)
	{
		//Link vs Tree
		if(a.isLink() && b.isTree() || 
		   a.isTree() && b.isLink())
			return true; 
		
		//Link vs TreasureChest
		if(a.isLink() && b.isTreasureChest() || 
		   a.isTreasureChest() && b.isLink())
			return true; 
		
		//all boomerang interactions
		if(a.isBoomerang() || b.isBoomerang())
			return true; 

		return false;
	}

	public int numberOfSprites() {return sprites.size();}

	public Sprite getSprite(int i) {return sprites.get(i);}

	public ArrayList getSprites() {return sprites;}

	public Link getLink() {return link;}

	public int getItemNum() {return itemNum;}

	public void setItemNum() 
	{
		if(itemNum != itemsICanAdd.size() - 1)
			itemNum++;
		else
			itemNum = 0; 
	}
}