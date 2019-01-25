CompSci 308: Game Project Analysis
===================

> This is the link to the [assignment](http://www.cs.duke.edu/courses/compsci308/current/assign/01_game/):

Design Review
=======

### Status

* Code Readability:
    * In my opinion, the code is fairly easy to read but definitely has room for improvement.  I gave methods names 
    that describe exactly what they do and gave variables descriptive names.  I also kept my methods short, for the 
    most part, only calling helper methods that handle one task at a time.  The code is well commented where comments
     are appropriate, but does not need many comments to be understandable.  Here is an example of code from my 
     MainScreen class that I find to be very readable seeing as every method explains exactly what it is doing.
     
     ```java
  protected void setUpScene(int width, int height, Paint background) {
          StackPane root = new StackPane();
          var scene = new Scene(root, width, height, background);
         
          initializeWelcomeScreenBackground();
          initializePaddle();
          initializeWelcomeText();
         
          createButtons();
         
          addSceneElementsToVBox();
         
          root.getChildren().add(welcomeScreenBackground);
          root.getChildren().add(myVBox);
         
          myScene = scene;
         
  }
     ```
     * On the other hand, there is still code with room for improvement when it comes to readability.  I think this 
     is particularly the case when I am using if statements and logic to control flow.  For example, in my GameLevel 
     class I have a step function that advances the game animation.  How the step function works depends on if the 
     game is in active mode (meaning the Bouncer should be moving) and if a power up animation is being played or not
     .  Currently, this is how my step function handles the logiic:
     
     ```java
  /**
       * Handles the animation of the level
       * Allows for pausing based on whether the game is in a moment where time needs to freeze or behave
       * differently based on what powerups are being used
       * Updates all important aspects of the level
       * @param elapsedTime
       */
      @Override
      protected void step (double elapsedTime) {
          // update attributes
          if (activeGameMode && !myPowerUpManager.isInDestroyMode() && !myPowerUpManager.isInBallDropperMode()) {
              timeRemaining -= elapsedTime;
              myPaddle.updatePaddlePosition(elapsedTime,myScene);
              List<GenericBrick> effectedBricks = myBouncer.handleBouncerCollisions(elapsedTime,myScene, myPaddle, myBricks,
                      root);
              cleanUpBricksAndCollectPowerUps(effectedBricks);
              myPowerUpManager.updatePowerUpStatus(elapsedTime);
  
              handleLifeLoss();
              handleEndOfGame();
          }
          myPowerUpManager.handleBallDropperMode(elapsedTime);
          playerScore = myBrickManager.getMyScore();
          centerHBoxText(bottomLineDisplay, myScene.getHeight()* BOTTOM_LINE_DISPLAY_LOCATION, myScene);
          updateTopLine();
          myPowerUpManager.displayStateOfPowerUps();
      }
    ```
    * Not only is this method long, but it depends on logic that may not be necessarily apparent and readable.  This 
    code should be refactored possibly into two methods.  One method would hand the general game animation and the 
    other would handle the animation if the game is in some special mode where the animation is different.  
    Alternatively, I could refactor my PowerUpManager class to be able to handle the animation whenever a power-up is
     in use which could separate the functionality so my step function within the GameLevel class would be easier to 
     follow.  An example of the basic pseudocode for this refactoring is shown below:
     
     ```java
     protected void step (double elapsedTime) {
               // update attributes
               if (myPowerUpManager.isUsingPowerUp()) {
                 myPowerUpManager.handleAnimation(elapsedTime);
               } 
               else if (inActiveMode){
                 handleAnimation(elapsedTime);
               }
  }
     ```
* Handling of Dependencies
    * In general, the dependencies in my code are rather convoluted due to poor design.  Instead of creating classes 
    that are closed to modification and can stand alone, many of the classes are interdependent.  There are two 
    really egregious examples of these interdependencies.  The first is in how I utilize my StageManager class. I 
    designed the StageManager as a way to be able to change scenes from any Screen to another Screen.  However, this 
    means that each Screen needed to be passed the StageManager and that the StageManager needed to be able to 
    initialize Screens.  This lead to the following circular dependencies that can be seen in the StageManager 
    constructor:
    
    ```java
    /**
         * StageManager Constructor creates the StageManager and initializes all screens being managed
         * Sets the currentScreen to a GenericScreen in teh beginning.
         * @param stage
         */
        public StageManager(Stage stage) {
            this.mainScreen = new MainScreen(this);
            this.gameLevel = new GameLevel(this,1,GameDifficulty.BEGINNING_MODE);
            this.pauseScreen = new PauseScreen(this);
            this.cheatKeyMode = new CheatKeyMode(this);
            this.tutorialMode = new TutorialMode(this);
            this.stage = stage;
            this.currentScreen = new GenericScreen();
            stage.setScene(currentScreen.getMyScene()); 
        }
  ```
  * I am sure that passing itself as the parameter to initialize another object in its constructor is a huge 
  malpractice but I was unsure of any other way to get the same functionality other than constantly adding and 
  removing objects from the root's children every time I wanted to make a sweeping change of what was being displayed
  .  The way the StageManager is implemented and used makes it essentially a global variable across all classes, 
  which from the readings and class I now know is not okay to have.
  * The second really problematic use of dependencies is in how I handle collisions with the Bouncer.  I decided to 
  use the Bouncer class to handle its collisions with bricks and the paddle because that is how I conceptualized 
  updating the Bouncer in our example_bounce lab.  However, to handle these collisions, the Bouncer needed to know a 
  lot about the Bricks and the Paddle as shown below:
  ```java
  /**
       * This method updates the Bouncer's position by computing new position from current position and velocity
       * This method also handles Bouncer's collisions with all other objects in the scene
       * These include walls, paddles, bricks
       * @param elapsedTime
       * @param scene
       * @param paddle
       * @param bricks
       * @param root
       * @return the bricks that the Bouncer collided with so they can be handled by the BrickManager
       */
      public List<GenericBrick> handleBouncerCollisions(double elapsedTime, Scene scene, Paddle paddle, ArrayList<GenericBrick> bricks,
                                                        Group root){
          updatePosition(elapsedTime);
          handleWallCollisions(scene);
          handlePaddleCollisions(paddle);
          List<GenericBrick> effectedBricks = findEffectedBricks(bricks, root);
          return effectedBricks;
      }
  ```
  * To fix these dependencies, I realize my code would be much better implemented if I refactored my classes. 
  Specifically, I think I should create a Sprite class that extends ImageView.  Then I should make GenericBrick, 
  Paddle, and Bouncer all extend Sprite.  Then I would create a SpriteManager class that has the functionality of 
  handling collisions, displaying sprites, and cleaning up the screen.  These changes would make my code much simpler, 
  easier to read, and more open to extension.  The SpriteManager would have all the functionality to handle the 
  collisions.  Therefore, I wouldn't have to pass information about all the other sprites to each sprite to handle 
  the collisions as I have done above, and I wouldn't need to handle collisions in the GameLevel class because the 
  SpriteManager would handle it.  
  
  
  


You can also make lists:

* Bullets are made with asterisks

1. You can order things with numbers.


Emphasis, aka italics, with *asterisks* or _underscores_.

Strong emphasis, aka bold, with **asterisks** or __underscores__.

Combined emphasis with **asterisks and _underscores_**.


You can put links to commits like this: [My favorite commit](https://coursework.cs.duke.edu/compsci308_2019spring/example_bins/commit/ae099c4aa864e61bccb408b285e8efb607695aa2)


### Design

* Current Design:
    * GamePlayer:
        * The main class that initializes the StageManager and sets up the animation
    * StageManager:
        * Initializes all the different types of possible screens
        * Change from screen to screen
        * Tells the main class (GamePlayer) what screen is currently displayed so GamePlayer can animate the scene 
        according to that screen's step function.
    * Generic Screen:
        * Currently implemented as a concrete class but after class discussion and the readings, I now realize it 
        should be an abstract class.
        * Defines the functionality for its subclasses: CheatKeyMode, GameLevel, MainScreen, PauseScreen, and 
        TutorialMode.
    * MainScreen:
        * Loads a welcome screen with buttons that can lead to loading a new level, beginning the tutorial, or 
        viewing the list of cheat keys.
    * PauseScreen:
        * The PauseScreen displays information about the state of the current game as well as the objective of the 
        game and the various cheat keys available to the player.
        * The PauseScreen also allows the player to change levels or change the difficulty.
    * TutorialMode:
        * Creates all the different scenes that are displayed during the tutorial mode
        * Allows user to advance from one tutorial to the next
        * In retrospect, after in class discussions I now know I need redesign the implementation of TutorialMode so 
        it is not dependent on the one long, messy strand of if statements either by creating separate classes for 
        the different screens or coming up with some way to better update the tutorial.
    * CheatKeyMode:
        * Displays all the different cheat keys and how to use them.
    * GameLevel:
        * Handles the game play (the simulation and the game state)
        * Updates the state of the screen and the state of the game through the step function
        * Detects when a player has won or lost and will update the game accordingly
        * Has instance variables of PowerUpManager, BrickManager, Paddle, and Bouncer that help handle the 
        functionality of playing a level.
    * Bouncer:
        * Moves around the screen
        * Updates its position based on detecting collisions with Paddle, Walls, and Bricks
        * Identifies the bricks with which it collides in a time step and can report this back to the GameLevel
            * From our in class discussions I now realize that the Bouncer perhaps has too much knowledge about the 
            state of the Bricks and Paddle and that it would be better to have a SpriteManager that knows everything 
            about the sprites on the screen but can keep the information hidden between sprites.
    * Paddle:
        * Controlled using arrow keys
        * Moves from side to side across the bottom of the screen and can wrap from one side to the other
    * BrickManager:
        * Reads in file specifying brick configuration for a given level
        * Generates and keeps track of all bricks in the current level
        * Determines how brick collisions affect the state of the game in terms of updating the score, updating the 
        number of lives remaining, and whether a 
        power-up should be given to the player
    * Bricks
        * There is a GenericBrick abstract class that outlines the functionality and characteristics of Bricks
        * There are five subclasses of GenericBrick: OneHitBrick, TwoHitBrick, ThreeHitBrick, PermanentBrick, and 
        DangerBrick.  The five subclasses all have different appearances on the screen and behave differently in 
        terms what happens when they collide with the Bouncer.
            * In retrospect, my code could have been much more readable and I could have had less duplicated code if 
            I implemented a handleCollisionWithBouncer() method for each kind of brick.  This would better leverage 
            inheritance and make my code much more open for extension to new kinds of bricks because the methods that
             handle brick collisions would be closed to modification because they would be flexible enough to adapt 
             to any subclass of GenericBrick.  Therefore, to create a new kind of brick I would just have to specify 
             its image, its score, and its handleCollisionWithBouncer() method.
    * PowerUpManager
        * Handles the number of power-ups of each kind that are available to the user
        * Updates the display based on power-up status
        * Provides the machinery to display animate and display the power-ups
            * The PowerUpManager could be better implemented if the power-ups themselves were classes.  This would 
            divide up the functionality much better into more understandable, self-contained pieces.
            
            
* How to add a new level:
    1. Create a txt file that uses '-' to denote where you want the bricks to appear.
    2. Specify the path to this file in the BrickManager class.
    3. Add a condition for the level based on the number you want assigned to the level in the BrickManagerConstructor
    4. Add a condition based on pressing the key corresponding to that level number in the PauseScreen class to 
    initialize a new level of that number.
    
    Note: I realize now that I should have created an inheritance hierarchy to handle levels in a more flexible, 
    extendable 
    way.  However, for not having done this, my implementation is not too terrible for adding new levels.
    
* Current Design Justification and Reflection:
    * The program starts at in the GamePlayer class by initializing the StageManager for the application.  The 
    StageManager initializes all the different kinds of screen that could be displayed throughout the application.  
    The screen types are CheatKeyMode, GameLevel, MainScreen, PuaseScreen and TutorialMode.  These all extend 
    GenericScreen which 
    is a concrete class that I used for testing but in retrospect should have been an abstract class that defines the
     functionality of all the different possible kinds of screens but cannot exist concretely itself.  The animation 
     for the game is run using a Timeline and KeyFrames like in the example_bounce lab.  The animation progresses by 
     simply asking the StageManager which screen is currently being displayed, and then calling the step function for
      that screen.  As mentioned above, I realize that the StageManager's construction adds a lot of complexity to 
      the program and that treating it as essentially a global variable is a bad practice, but this implementation 
      ultimately saved me a lot of headaches because it made it easy to change scenes.  I think that creating the 
      GenericScreen and its subclasses was close to being a good design choice but could have been implemented better
      .  All things considered, I think this choice was a step in the right directions 
      because it made handling animation very easy and my main GamePlayer class very simple.  I also think it is an 
      example of me almost using abstraction and inheritance in an effective way.
    * When the game starts it displays the MainScreen which is a welcome screen with buttons that can lead to either 
    the TutorialMode, a screen that displays the various cheat keys (CheatKeyMode), or to start a new GameLevel.
    * The GameLevel class handles game play.  The class initializes a BrickManager that reads in the file 
    specifying the brick configuration for the specified level and generates the bricks.  The Bricks were one of the 
    best designed aspects of my program because they were the closest I came to using abstraction and inheritance 
    effectively.  I created a GenericBrick abstract class and then five other brick classes that all extend from this
     GenericBrick class.  My implementation would have been more effective if I had included a handleCollision() 
     method for each of the bricks instead of using if statements to handle collisions with the various types of bricks.
     I feel like I gave the GameLevel class the right amount of responsibility because it is only in charge of 
     displaying the various objects (bricks, paddles, bouncers) on the screen and keeping track of and displaying the
      state of the game in terms of score, number of lives remaining, available power-ups, and important messages to 
      the user.
    * I also think I did a decent job of allocating some responsibilities away from the GameLevel class to other 
    appropriate classes.  As mentioned before, a SpriteManager class seems like it would have been the best solution, 
    but in my current implementation, I think that it was good design to have the Bouncer update its own position and
     tell GameLevel what Bricks it collided with and then the GameLevel would tell the BrickManager to handle the 
     removal of these bricks and ask how the state of the game should be updated accordingly.  I also think that 
     allocating responsibilities regarding power-ups to the PowerUpManager was a good design choice in terms of 
     keeping the GameLevel class simple.  However, the PowerUpManager could have been better implemented in a way 
     that required less sharing of information and state between the PowerUpManager and the GameLevel classes.
    * In retrospect, I should have have probably created a GenericGameLevel abstract class and extended the different
     levels from that superclass.  This was my original plan (hence the name GameLevel) but as I got started coding 
     it was hard for me to differentiate what things needed to be level specific and what needed to be general and 
     came to the conclusion that all the levels would be so similar that I would repeat a lot of code if I were to 
     implement each level separately.

* Describing Features From Assignment Specification:
    * Something Extra:
        * For my something extra I decided to create a tutorial on how to play my game that outlines the rules and 
        objectives of the game as well as allowing the user to play around with some of the functionality.  
        * The tutorial is implemented in the TutorialMode class and requires the Bouncer class, the Paddle class, all
         the Brick classes and the resource files associated with these classes (images).
        * This feature is currently designed in one big class.  At the heart of this class is an extended if 
        statement that is used to transition load the next tutorial screen based on the which tutorial screen is 
        supposed to be displaying.  Each time the mouse is clicked the current tutorial number is incremented and the
         next screen is loaded.
        * The next screen gets loaded by changing the text that is being displayed, removing all unused items from 
        the root's children list, and adding all the items that are needed for the new screen to the root.
        * To add a new screen to the tutorial one would have to add a new condition to the long series of if 
        statements and create a function that removes and adds the correct objects from the root.  To add a screen 
        between two current screens, one would have to update the whole strand of tutorial numbers.  These are 
        indicators of poor design according to the open-closed principle.  The TutorialMode class' functionality as 
        it is currently implemented is not easily extendable and would have to be heavily modified to make any real 
        changes.
        * A better design of this class would be to replace my conditionals with polymorphism and create a class 
        hierarchy of the various tutorial screens that extend a generic tutorial screen.  Then, to transfer from one 
        screen to the next I could just have a method in each subclass called goToNextScreen() that will simply 
        create a new instance of the next screen and switch to that screen.
        
    * Blocks (aka Bricks):
        * For the specification of creating at least three different kinds of bricks that behave differently when 
        colliding with the bouncer, I create five different kinds of bricks: a OneHitBrick, a TwoHitBrick, a 
        ThreeHitBrick, a PermanentBrick, and a DangerBrick.
        * The bricks require the images in the resources folder.  The bricks are contained in a BrickManager class 
        that initializes the bricks based on specified positions in the layout txt files in the resources folder.  
        The BrickManager class also handles all collisions between the bricks and the Bouncers to determine how a 
        Brick's state should be updated (should it be cleared or have fewer hits remaining, etc.) and whether a brick 
        should drop a power-up.
        * All five of the bricks I created extended the abstract class GenericBrick.  This design choice of using an 
        abstract class and extending it for different classes that are similar set me up to use inheritance and 
        polymorphism effectively, however I did not execute it very well.
        * Instead of including a handleCollision() method for each brick. I handled the collisions of each brick in a
         conditional statement using instanceof.  If I redesigned this aspect of my brick implementation to better 
         leverage polymorphism instead of conditionals as explained in the "Replace Conditional With Polymorphism" 
         reading, I could have written code that better complied with the open-closed principle.  When my 
         BrickManager is handling collisions it would be able to just call the implementation of the handleCollision
         () method as specified in that brick type's overridden method.  Therefore, when creating a new kind of brick
          I could just specify the brick's image and its handleCollision method and not have to change any of the 
         code that handles collisions in the BrickManager class.

### Alternate Designs

* Design Choice 1: StageManager Class

    * I designed the StageManager as a way to be able to change scenes from any Screen to another Screen.  However, 
    this 
    means that each Screen needed to be passed the StageManager and that the StageManager needed to be able to 
    initialize Screens.  This lead to the following circular dependencies that can be seen in the StageManager 
    constructor:
    
    ```java
    /**
         * StageManager Constructor creates the StageManager and initializes all screens being managed
         * Sets the currentScreen to a GenericScreen in teh beginning.
         * @param stage
         */
        public StageManager(Stage stage) {
            this.mainScreen = new MainScreen(this);
            this.gameLevel = new GameLevel(this,1,GameDifficulty.BEGINNING_MODE);
            this.pauseScreen = new PauseScreen(this);
            this.cheatKeyMode = new CheatKeyMode(this);
            this.tutorialMode = new TutorialMode(this);
            this.stage = stage;
            this.currentScreen = new GenericScreen();
            stage.setScene(currentScreen.getMyScene()); 
        }
  ```
     * As mentioned before, I am sure that passing itself as the parameter to initialize another object in its constructor 
  is a bad practice but I was unsure of any other way to get the same functionality other than constantly adding and 
  removing objects from the root's children every time I wanted to make a sweeping change of what was being displayed
  .  The way the StageManager is implemented and used makes it essentially a global variable across all classes, 
  which from the readings and class I now know is not okay to have.
     * The StageManager I have implemented, however, does have many advantages.  First of all, it makes the process of 
  switching screens very easy.  All that one has to do it is call stageManager.switchScene() and specify which scene 
  to switch to.  Another benefit of the StageManager is that it allows the main class to be very simple and easy to 
  understand.  All that is being done in the main class is initializing the StageManager setting up the animation.
  
  ```java
  /**
       * Initialize the stageManager and switch scenes to load the Main Screen of the application
       * Establish the animation loop
       */
      @Override
      public void start (Stage stage) {
          stageManager = new StageManager(stage);
  
          stageManager.switchScene(stageManager.getMainScreen());
  
          //attach "game loop" to timeline to play it
          var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
          var animation = new Timeline();
          animation.setCycleCount(Timeline.INDEFINITE);
          animation.getKeyFrames().add(frame);
          animation.play();
  
      }
  
      private void step(double secondDelay) {
          stageManager.getCurrentScreen().step(secondDelay);
      }
  ```
    * Furthermore, the StageManager class feels like a very readable abstraction.  The nuances of how the 
  StageManager is implemented can be kind of difficult to understand, but whenever it is used in the code, its 
  purpose is clear and readable.  An example of this readability is shown below:
  ```java
  else if (code == KeyCode.SPACE){
              myStageManager.switchScene(myStageManager.getPauseScreen());
          }
  ```
     * A final advantage of using the StageManager is that it is easy to save the current state of the game because the 
  StageManager will have access to the most recent instance of GameLevel.  This makes pause and resume functionality 
  much easier and allows me to better integrate the game state to what is being displayed in the pause screen.
    * As mentioned before, the main drawbacks of the StageManager are its confusing dependencies and its implementation
   as almost a global variable.
   
    * An alternative to the StageManager that I was considering was to simply remove everything from old contents from 
  the root and add new objects to the root every time I wanted to make a large change in what was being displayed.  
  This method for controlling what is being displayed has the advantages of not having any huge or confusing 
  dependencies like the StageManager.  However, this strategy has many disadvantages.  First, there would be a lot of 
  repetition of code when it comes to clearing out the root and re-establishing what is supposed to be displayed.  
  Second, all classes would still have to access the root somehow so I wouldn't be able to avoid having some kind of 
  implementation of a practically global variable (it would be the root instead of the StageManager).  This method 
  does not allow you to maintain state as well because each time you change from screen to screen, it would remove 
  everything from the screen and you would have to work much harder to keep track of which objects are in what state
   and how the objects that are included in the root will have to change going from each possible screen to every 
   other possible screen.  This method would lead to a lot more hard-coding of transitions.
    * Ultimately, I decided that te StageManager rote was the way to go, but I am not convinced that it would be the 
  best option in the future but I am unsure as to what would be a better way to implement and handle changing the 
  display.


* Design Choice Two: Handling of Collisions Between Sprites

    * Collisions between sprites (the bouncer, the paddle, bricks, etc.) are at the heart of the game because pretty 
    much every significant event is a collision between sprites.  Seeing as the bouncer would be in every collision, 
    I determined that the best way to handle the collisions would be through a method in the Bouncer class that 
    updates the Bouncer.  This method is shown below:
    
    ```java
    /**
         * This method updates the Bouncer's position by computing new position from current position and velocity
         * This method also handles Bouncer's collisions with all other objects in the scene
         * These include walls, paddles, bricks
         * @param elapsedTime
         * @param scene
         * @param paddle
         * @param bricks
         * @param root
         * @return the bricks that the Bouncer collided with so they can be handled by the BrickManager
         */
        public List<GenericBrick> handleBouncerCollisions(double elapsedTime, Scene scene, Paddle paddle, ArrayList<GenericBrick> bricks,
                                                          Group root){
            updatePosition(elapsedTime);
            handleWallCollisions(scene);
            handlePaddleCollisions(paddle);
            List<GenericBrick> effectedBricks = findEffectedBricks(bricks, root);
            return effectedBricks;
        }
  ```
    * Originally, the goal of this method would be to check all the objects on the screen in every time step and see 
    if the bouncer had collided with the object and if it had, make the bouncer bounce.  However, as I was coding, I 
    realized that much more can happen on a bouncer collision than just the bouncer bouncing.  For example, a brick 
    may disappear, or a brick may generate a power-up.  I had already implemented the method to make the bouncer 
    bounce and I did not want to redo that functionality so I determined that since the other key events all involve 
    a bouncer colliding with a brick, I would have this method simply return the list of bricks that the bouncer 
    collided with and that I could pass this list of effected bricks to the BrickManager to be handled and update the
     game accordingly.  At the time this felt like good design because everything that involved the bouncer was 
     happening using Bouncer class methods and everything that involved the management of Bricks was using the 
     BrickManager methods.  Now however, I see that there is a lot of sharing that has to be done across classes 
     seeing as the Bouncer has to know about all the Bricks, send the effected bricks back to the GameLevel class 
     which then has to send them to the BrickManager which then sends information back to the GameLevel class.  
     Furthermore, the Bouncer has to know everything about the paddle and the scene to be able to handle collisions. 
      Ultimately, it happens to be the case that the Bouncer and BrickManager end up knowing pretty much everything 
      about the current state of the game suggesting that functionality and information of each of these classes is 
      not encapsulated very well.
    * As mentioned earlier, creating a SpriteManager class and making Bouncer, Paddle, and GenericBrick all extend 
    Sprite would have been a solution that better isolated the functionality of handling collisions and updating the 
    state of the sprites on the screen.  With a SpriteManager class, the state of the bricks would be hidden from the
     Bouncer, and the states of the Bouncer, Bricks and Paddle would all be hidden from the GameLevel class.  The 
     SpriteManager would just handle all the collisions and report back to the game what needs to be updated in terms
      of power-ups, number of lives, score, etc.  This would allow for better encapsulation and classes would only 
      have the information they need and other information would be hidden from classes that don't really need it 
      which goes along with the principles we were practicing in the hangman lab.

* Three Most Important Bugs:
    1. Cannot use the balldropper power-up before the previous balldropper has stopped running
    2. There is no mode in which you advance levels sequentially, you are always allowed to choose which level you 
    want to play.
    3. The design as-is is not very extendable - particularly in the TutorialMode.
      
  

Here is another way to look at my design:

![This is cool, too bad you can't see it](crc-example.png "An alternate design")

