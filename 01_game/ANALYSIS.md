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
        * Change change from screen to screen
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
    


    * The program starts at in the GamePlayer class by initializing the StageManager for the application.  The 
    StageManager initializes all the different kinds of screen that could be displayed throughout the application.  
    The screen types are CheatKeyMode, GameLevel, MainScreen, PuaseScreen and TutorialMode.  These all extend 
    GenericScreen which 
    is a concrete class that I used for testing but in retrospect should have been an abstract class that defines the
     functionality of all the different possible kinds of screens but cannot exist concretely itself.  The animation 
     for the game is run using a Timeline and KeyFrames like in the example_bounce lab.  The animation progresses by 
     simply asking the StageManager which screen is currently being displayed, and then calling the step function for
      that screen.
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
   * All screens have different key inputs that can be used to tell the StageManager to switch to a different screen.


### Alternate Designs

Here is another way to look at my design:

![This is cool, too bad you can't see it](crc-example.png "An alternate design")

