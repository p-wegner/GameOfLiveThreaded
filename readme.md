# GameOfLife but each cell is a thread
It started as an attempt to make game of life constrained by the 'object calisthenics' rulset but went slightly further into a threading mess that has little to do with object calisthenics and acts more like a distributed sensor network or something.

## Why, thou?
During a coding dojo for [object calisthenics](https://williamdurand.fr/2013/06/03/object-calisthenics/) someone asked: What about game of life without setters? How would you implement the field datastructure?
So here we go:

### Field Factory
First, we need to create a row of cells that knows its left an top neighbor. Then it notifies these neighbors to register itself so that they are double linked. This is important, because we want to avoid knowing like an array or even matrix of cells. Because that would defy the whole idea of not-setting-stuff into other stuff.
Now that they are double linked, we can create another row, and we can find the corresponding top neighbor, by using an iterator to navigate through the top row. Yeah, we expose an iterator to the outside world. Is this cell internals? ... kinda....
Anyway, then we can ask all the nodes to double link them by the corners and we have a double linked matrix. And we return the nodes as randomized list (or even one single node would be enough)

### But who knows if the cell is alive?
The cell knows. And we are not allowed to use a getter.
So i guess, the cell needs to be a thread, managing itself. But wait, how do we know if an adjacent cell is alive? Well, we ask it with a AliveRequest. Which it may respond with an AliveResponse.
Does all that make any sense, or is it better than just using a getter? No. No, not really.

### Game Logic
As GameOfLife Enterprises, we specialise in making awesome enterprisey implementations of the very algorithm. So we split the field generation and communication between cells and the gameOfLife logic. So each cell contains a GameOfLifeCell. Which knows its container, and again, object calisthenics go out the window, because creating 2 things knowing each other without some kind of setter.... 

# So....?
So, is it a problem throwing the object calisthenics out of the window?
No. As you wouldn't use reflection in business logic classes, but in framework/library-like classes, we try and keep the business logic clean but need to do certain other things in other places. Also these rules are so restrictive to be a challenge and see what is possible using objects and not exposing everything inside them.

# Todos
- A lot of beautification-todos still present
- It should be possible to use less threads than cells. Let's see who starves now >:D
- Only active cells should be active and owning a thread. This means, that to revive a cell, the cells around need to vote and find out if the other cells around are alive. That sounds like fun to implement!.
- rxjava instead of threads?

# How to run
Maybe import the maven project into eclipse and run the GameOfLifeTest. Your machine then tries to handle ~250 threads, which on my machine actually works surprisingly well. Also because they are sleeping like 95% of the time, but still...
