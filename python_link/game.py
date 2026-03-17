# CSCE 31903 Programming Paradigms
# Fall 2025
# Assignment 7 starter code

import pygame
import time
import json
import math

from pygame.locals import*
from time import sleep


class Sprite():
    def __init__(self, x, y, w, h, image):
        self.x = x
        self.y = y
        self.w = w
        self.h = h
        self.speed = 1
        self.valid = True
        self.image = pygame.image.load(image)

    def update(self):
        return self.valid

    def is_link(self):
        return False
    
    def is_tree(self):
        return False
    
    def is_butterfly(self):
        return False
    
    def is_chest(self): 
        return False

    def is_boomerang(self):
        return False

    def draw_yourself(self, screen, currentRoomX, currentRoomY):
        LOCATION = (self.x-currentRoomX, self.y-currentRoomY)
        SIZE = (self.w, self.h)
        screen.blit(pygame.transform.scale(self.image, SIZE), LOCATION)

    # for the starter code, we assume that all Sprites of a certain
    # type are the same size, and thus don't need w and h saved
    # However, it would be very easy to add more attributes to be 
    # saved here!
    def marshal(self):
        return {
            "x": self.x,
            "y": self.y,
            "w": self.w,
            "h": self.h, 
            "type": self.type
        }

class Boomerang(Sprite):
    BOOMERANG_WIDTH = 25
    BOOMERANG_HEIGHT = 25

    def __init__(self, x, y, w, h, direction):
        super().__init__(x, y, Boomerang.BOOMERANG_WIDTH, Boomerang.BOOMERANG_HEIGHT, "images/boomerang1.png")
        self.speed = 7
        self.direction = direction
        self.images = []
        self.MAX_IMAGES_PER_DIRECTION = 1
        self.TOTAL_NUM_IMAGES = 4
        self.NUM_DIRECTIONS = 4

        self.frameNum = 0
        self.rotateFrames = 0 

        index = 1

        for i in range(self.NUM_DIRECTIONS):
            self.images.append([])
            for j in range(self.MAX_IMAGES_PER_DIRECTION):
                self.images[i].append(pygame.image.load(f"images/boomerang{index}.png"))
                index += 1
    
    
    def update(self): 
        self.rotateFrames += 1
        if (self.rotateFrames >= self.TOTAL_NUM_IMAGES): 
            self.rotateFrames = 0
        
        if self.direction == 1: 
            self.x -= self.speed

        elif self.direction == 2: 
            self.x += self.speed

        if self.direction == 0: 
            self.y += self.speed
        
        if self.direction == 3: 
            self.y -= self.speed
        
        return self.valid
    
    def draw_yourself(self, screen, currentRoomX, currentRoomY):
        LOCATION = (self.x-currentRoomX, self.y-currentRoomY)
        SIZE = (self.w, self.h)
        screen.blit(pygame.transform.scale(self.images[self.rotateFrames][self.frameNum], SIZE), LOCATION)
        
            

    def is_boomerang(self):
        return True

    def collisionResolution(self, sprite): 
        if(not(sprite.is_link())):
            self.valid = False


class Chest(Sprite):
    CHEST_WIDTH = 35
    CHEST_HEIGHT = 35

    def __init__(self, x, y, w, h):
        super().__init__(x, y, Chest.CHEST_WIDTH, Chest.CHEST_HEIGHT, "images/treasurechest.png")
        self.type = 1
        self.despawnTimer = 150 
        self.activeDespawnTimer = False
        self.despawnRupee = False
        self.secondCollision = False
        self.freezeFrames = 135
    
    def update(self): 
        if(self.activeDespawnTimer):
            self.despawnTimer -= 1

        if(self.despawnTimer == 0 or self.despawnRupee):
            self.valid = False
        else:
            self.valid = True

        return self.valid
            

    def is_chest(self):
        return True

    def collisionResolution(self, sprite): 
        self.image = pygame.image.load("images/rupee.png")
        self.activeDespawnTimer = True 

        if(self.activeDespawnTimer and (self.despawnTimer < self.freezeFrames)):
            self.secondCollision = True
            self.despawnRupee = True

    def is_rupee(self):

        if(self.activeDespawnTimer and self.secondCollision): 
            return True
        return False

class Tree(Sprite):
    # variables that belong to the class, not to a specific
    # instance of the class - this is similar to Java's static variables
    TREE_WIDTH = 50
    TREE_HEIGHT = 50
    
    # constructor with default values - one of the ways you can
    # mimic Java's overloaded constructors
    # in this example, if w and h are provided, create the fish
    # as defined. If they are not provided, create a fish at 
    # half the regular size
    def __init__(self, x, y, w=None, h=None):
        super().__init__(x, y, w, h, "images/tree.png")
        self.type = 0

    def is_tree(self):
        return True
    
    def collisionResolution(self, spriteB):
        return


class Link(Sprite):
    LINK_WIDTH = 50
    LINK_HEIGHT = 50
    num_rupees = 0

    @staticmethod
    def reset_rupees():
        Link.num_rupees = 0

    def __init__(self, x, y):
        super().__init__(x, y, Link.LINK_WIDTH, Link.LINK_HEIGHT, "images/link1.png")
        self.speed = 6 
        self.images = [] 
        self.MAX_IMAGES_PER_DIRECTION = 11
        self.TOTAL_NUM_IMAGES = 44
        self.NUM_DIRECTIONS = 4
        self.frameNum = 0
        self.direction = 0
        self.type = 4
        self.px = 0
        self.py = 0

        index = 1
        for i in range(self.NUM_DIRECTIONS):
            self.images.append([])
            for j in range(self.MAX_IMAGES_PER_DIRECTION):
                self.images[i].append(pygame.image.load(f"images/link{index}.png"))
                index += 1

    def is_link(self):
        return True

    def move(self, direction):
        self.frameNum += 1
        if self.frameNum >= self.MAX_IMAGES_PER_DIRECTION: 
            self.frameNum = 0
        if direction == "up":
            self.y -= self.speed
            self.direction = 3
        if direction == "down":
            self.y += self.speed
            self.direction = 0 
        if direction == "left":
            self.x -= self.speed
            self.direction = 1
        if direction == "right":
            self.x += self.speed
            self.direction = 2

    def draw_yourself(self, screen, currentRoomX, currentRoomY):
        LOCATION = (self.x-currentRoomX, self.y-currentRoomY)
        SIZE = (self.w, self.h)
        screen.blit(pygame.transform.scale(self.images[self.direction][self.frameNum], SIZE), LOCATION)

    def collisionResolution(self, spriteB):

        if(self.px + self.w <= spriteB.x and self.x + self.w >= spriteB.x): 
            self.x = spriteB.x - self.w

        elif(self.px >= spriteB.x + spriteB.w and self.x <= spriteB.x + spriteB.w):
            self.x = spriteB.x + spriteB.w

        if(self.py + self.h <= spriteB.y and self.y + self.h >= spriteB.y):
            self.y = spriteB.y - self.h
  
        elif(self.py >= spriteB.y + spriteB.h and self.y <= spriteB.y + spriteB.h):
            self.y = spriteB.y + spriteB.h

    
    def savePreviousPosition(self): 
        self.px = self.x 
        self.py = self.y

    

class Model():
    filename = "map.json"
    
    def __init__(self):
        self.load_map()

    def load_map(self):
        # reset the fish count if we're loading (or reloading)
        # the map
        Link.reset_rupees()
        
        self.sprites = []
        
        # example of reading through the map.json file
        # and loading fishes and the turtle's location
        # open the json map and pull out the individual lists of sprite objects
        with open(Model.filename) as file:
            data = json.load(file)
            #get the lists . as "fishes" and "butterflies" from the map.json file
            sprites = data["sprites"]
        file.close()
        
        #create turtle using saved attributes
        # self.turtle = Turtle(turtlex, turtley)
        # self.sprites.append(self.turtle)
        self.link = Link(100,100)
        self.sprites.append(self.link)

        #for each entry inside the fishes list, pull the key:value pair out and create 
        #a new Fish object with (x,y,w,h)
        for entry in sprites:
            if (entry["type"] == 0): 
                self.sprites.append(Tree(entry["x"], entry["y"], Tree.TREE_WIDTH, Tree.TREE_HEIGHT))
            elif (entry["type"] == 1): 
                self.sprites.append(Chest(entry["x"], entry["y"], Chest.CHEST_WIDTH, Chest.CHEST_HEIGHT))

    def save_map(self):
        sprites = []
        # go through all of the sprites, saving them into the 
        # appropriate lists
        for s in self.sprites:
            sprites.append(s.marshal())

        # create the dictionary of sprites, split by what types
        # they are - fishes and butterflies are lists, while 
        # turtlex and turtley are singular attributes
        map_to_save = {
            "sprites": sprites
        }

        # Save to file
        with open(Model.filename, "w") as f:
            json.dump(map_to_save, f)

    def update(self):
        for spriteA in self.sprites:
            spriteA.update()

            if(not(spriteA.update())): 
                if spriteB.is_chest():
                    if spriteB.is_rupee(): 
                        Link.num_rupees += 1

                self.sprites.remove(spriteA)
            
            for spriteB in self.sprites:
                if((spriteB == spriteA) or (spriteA.is_tree() and spriteB.is_tree)):
                    continue 
            
                if(self.collisionDetection(spriteA, spriteB) and self.collisionType(spriteA, spriteB)):
                    spriteA.collisionResolution(spriteB)
                    spriteB.collisionResolution(spriteA)
                

    def collisionDetection(self, spriteA, spriteB): 
        if(spriteA.x >= spriteB.x + spriteB.w):
            return False
        if(spriteA.x + spriteA.w <= spriteB.x): 
            return False
        if(spriteA.y >= spriteB.y + spriteB.h): 
            return False
        if(spriteA.y + spriteA.h <= spriteB.y):
            return False
        return True

    def collisionType(self, a, b): 
        if(a.is_link() and b.is_tree() or a.is_tree() and b.is_link()):
            return True
        if(a.is_link() and b.is_chest() or a.is_chest() and b.is_link()):
            return True
        if(a.is_boomerang or b.is_boomerang):
            return True
        
        return False


    def saveLinkPreviousPosition(self):
        self.link.savePreviousPosition() 

    def clear_map(self):
        self.sprites.clear()
        self.sprites.append(self.link)
        # calling a static method - notice the lack of 'self'
        Link.reset_rupees()

    # pos was passed as the mouse position tuple - pos[0] is x, 
    # pos[1] is y
    def add_tree(self, pos):
        self.sprites.append(Tree(pos[0], pos[1]))

    def spawnBoomerang(self):
        boomerang = Boomerang(self.link.x+20, self.link.y+20, 20, 20, self.link.direction)
        self.sprites.append(boomerang)

class View():
    def __init__(self, model):
        self.WINDOW_WIDTH = 700
        self.WINDOW_HEIGHT = 500 
        SCREEN_SIZE = (self.WINDOW_WIDTH, self.WINDOW_HEIGHT)

        self.screen = pygame.display.set_mode(SCREEN_SIZE, 32)
        self.model = model
        self.currentRoomX = 0
        self.currentRoomY = 0
    
    def moveCameraWithLink(self):
        if(self.model.link.x > self.WINDOW_WIDTH):
            if(self.currentRoomX == 0):
                self.currentRoomX += self.WINDOW_WIDTH

        if(self.model.link.x < self.WINDOW_WIDTH):
            if(self.currentRoomX == self.WINDOW_WIDTH):
                self.currentRoomX -= self.WINDOW_WIDTH

        if(self.model.link.y > self.WINDOW_HEIGHT):
            if(self.currentRoomY == 0):
                self.currentRoomY += self.WINDOW_HEIGHT

        if(self.model.link.y < self.WINDOW_HEIGHT):
            if(self.currentRoomY == self.WINDOW_HEIGHT):
                self.currentRoomY -= self.WINDOW_HEIGHT

    def update(self):
        # change background color if the user is in edit_mode
        if Controller.edit_mode:
            self.screen.fill([150, 150, 150]) #light green
        else:
            self.screen.fill([100, 100, 100]) #dark forest green

        # draw sprites to the screen
        for sprite in self.model.sprites:
            sprite.draw_yourself(self.screen, self.currentRoomX, self.currentRoomY)

        # add text to the screen
        # Default font, size 32
        font = pygame.font.SysFont(None, 32)   
        text_string = str(Link.num_rupees) + " RUPEES COLLECTED"
        WHITE_COLOR = (255, 255, 0)
        text_surface = font.render(text_string, True, WHITE_COLOR)
        TEXT_LOCATION = (200, 10)
        self.screen.blit(text_surface, TEXT_LOCATION)
        
        # update display screen
        pygame.display.flip()

class Controller():
    edit_mode = False
    
    def __init__(self, model, view):
        self.model = model
        self.view = view
        self.keep_going = True

    def update(self):

        self.view.moveCameraWithLink()
        self.model.saveLinkPreviousPosition()

        for event in pygame.event.get():
            if event.type == QUIT:
                self.keep_going = False
            elif event.type == KEYDOWN:
                if event.key == K_ESCAPE or event.key == K_q:
                    self.keep_going = False
            elif event.type == pygame.MOUSEBUTTONUP:
                if Controller.edit_mode:
                    # add a fish at using the "overloaded" constructor
                    self.model.add_tree(pygame.mouse.get_pos())
            elif event.type == pygame.KEYUP: #this is keyReleased!
                if event.key == K_c:
                    self.model.clear_map()
                    print("Map cleared and game reset")
                if event.key == K_e:
                    Controller.edit_mode = not Controller.edit_mode
                if event.key == K_l:
                    self.model.load_map()
                    print("Map loaded")
                if event.key == K_s:
                    self.model.save_map()
                    print("Map saved")
        keys = pygame.key.get_pressed()
        # turtle's movement function changed to be closer related
        # to Link
        if keys[K_LEFT]:
            self.model.link.move("left")
        if keys[K_RIGHT]:
            self.model.link.move("right")
        if keys[K_UP]:
            self.model.link.move("up")
        if keys[K_DOWN]:
            self.model.link.move("down")
        if keys[K_SPACE]:
            self.model.spawnBoomerang()

print("Use the arrow keys to move. Press Esc to quit.")
pygame.init()
pygame.font.init()
m = Model()
v = View(m)
c = Controller(m, v)
clock = pygame.time.Clock()
while c.keep_going:
    c.update()
    m.update()
    v.update()
    #sleep(0.04)
    clock.tick(60)
print("Goodbye!")