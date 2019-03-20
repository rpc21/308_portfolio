CompSci 308: Parser Project Analysis
===================

> This is the link to the [assignment](http://www.cs.duke.edu/courses/compsci308/current/assign/03_parser/):


Design Review
=======

### Overall Design
* High Level Design
    * Our project is split into two modules: front_end and back_end.  The front_end module contains all the JavaFX 
    components and is in charge of the user interface, updating what the user sees and responding to user interactions.  
    The back_end module can be thought of in two main components: the parser and the commands.  The parser handles the 
    parsing of the commands entered by the user and builds a tree of commands that are then passed to the commands for 
    execution.  The parser also is where most of the error checking happens and will throw exceptions if the user enters 
    invalid commands that are caught and displayed in a user-friendly way by the front end.  
    * Since modules can only export in one direction (so there aren't circular dependencies), we chose to have the 
    back-end export to the front-end.  One of the most important things the back-end exports is the VisualUpdateAPI 
    which is the interface a front-end must implement in order or the effects of visual commands to be shown on the 
    display.  The back-end relies on the front-end implementing this interface because all visual commands call 
    methods on a parameter that implements the interface.
    * Perhaps the biggest dependency in our program is that the front-end has to know how to create commands and send
     them back through the parser for updates to appropriately take place in the back-end.  Because the front-end and
      back-end both need similar data and we can't have shared global state there has to be a way for the front-end 
      to communicate changes in the shared data to the back-end and the best way we could come up with is by having 
      the front end run a command that makes the appropriate changes.  This decision, however, makes the front-end 
      and back-end less separable because many of the updates in the front-end happen by passing a command through 
      the back-end so our front-end could not easily be plugged into a different project.

* Design Patterns
    * The back-end's two most powerfully used design patterns were the command design pattern and an iterator design 
    pattern.  The command nodes we created use a command design pattern that allow the execution of commands to be 
    carried out by simply calling the execute method on the nodes.  The iterator design pattern is used in Bale to be
     able to adapt to handling multiple turtles at a time.  The iterator design pattern choice was based of the 
     composite design pattern and having both Bale (the collection of turtles) and individual turtles implement the 
     ITurtle interface so methods that take in an ITurtle parameter have the specific implementations of whether the 
     command is being run on an individual turtle or a collection of turtles hidden from the method call itself.
    * The main design pattern used in the front-end is the delegator design pattern.  The VisualUpdateAPI is 
    implemented in a tree-like fashion where one class (Delegator) serves as the root, implementing the entire API 
    and delegates away each method to a class where the changes can be more appropriately handled.  Additionally, the
     front-end uses a variation of the the observer/observable design pattern when it comes to language of the 
     program and changing the language.   
     
* Making Additions
    * Non-Turtle Command
        * Add a subclass of CommandNode and implement its evaluate method to return the appropriate value
        * Add the command key that corresponds to the name of the subclass into each command properties file for each language. For the value of this key, add in the possible ways to call this command. 
        * Add the command key to the PackageLocation properties file with the appropriate package location as the value. 
        * Add the command key to the Parameters properties file with the number of expected parameters the command has 
        * Add information to “help” portion of properties’ files
    * Turtle Command
        * Add a subclass of CommandNode and implement its evaluate method to return the appropriate value
        * Add the command key that corresponds to the name of the subclass into each command properties file for each language. For the value of this key, add in the possible ways to call this command. 
        * Add the command key to the Parameters properties file with the number of expected parameters the command has 
        * Add the command key to the PackageLocation properties file with the appropriate package location as the value. 
        * Add a variable (potentially) and a method to Turtle to keep track of and update the state of this particular command’s action
        * Add a subclass of ImmutableVisualCommand that invokes the front-end’s implementation of the  VisualUpdateAPI’s to respond appropriately
        * Implement the method(s) needed to display the changes in the canvas
        * Add information to “help” portion of properties’ files
    * New Language
        * Add language as an option in drop-down menu of GUI
        * Add properties file to languages folder in resources 
        * Add the new language to the front-end Languages properties file
    * New Argument Syntax
        * In order to add in a new type of syntax, add the regular expression with a defining key into the Synax properties file. 
        * In order to add an additional way to call a method, add “|” and the new way to call each method. 
    * New Exception
        * Add in the exception and have it extend InvalidInputException
        * Add the desired error message to the Exceptions properties file
        * Set the reason according to the exceptions file
    * New Type of Palette
        * Simply decide what kind of node you want to display in the palette and you can just use the Palette and Palette element classes already created in the project because they use generics
    * New Type of Turtle
        * Find the image you want to use and create a subclass of DisplayView that points to this image
        * Add this new turtle’s class name to the Shapes properties file
        * Because of our use of properties files and reflection, no hard coding or factories have to be modified to add a new type of turtle with a new image
    * Feature that was not implemented: Animation
        * To add animation we would need to change how we handle turtle moves and rotations.  We currently tell the 
        turtle to move to a specific location specified by x and y coordinates.  The turtle then begins drawing a new
         path using the graphics context of the canvas, moves to the new path and draws the line.  If we were to add 
         animation we would have to be able to create PathTransitions and RotateTransitions and separate the drawing 
         on the canvas from the moving of the turtle.
        * This feature honestly would probably not be too difficult to add but we decided as a team that we were not 
        going to pursue it.
* Dependencies
    * For the most part, the dependencies between parts are clear and easy to find.  We put a lot of thought and 
    effort into using interfaces to define functionality and dependencies because it not only made it easier for us 
    to communicate about what we need from the other part of the projects, but it made the dependencies much easier 
    to find. 
    * Additionally, we made concerted efforts to include the parameters that were to be modified in the method 
    headings so it would be clear what is going to be affected by a method call.
    * One way in which our dependencies are less apparent is in our use of two-step constructors in the front-end.  
    We decided to establish all the GUI components first and make them visually appear on the screen and then go back
     through the relevant components to give them access to the parser and to set their languages.  This choice made 
     the constructors for GUI components simpler (require fewer parameters), but creates a sort of back channel in 
     terms of the implementation of the GUI components and how they are able to access the parser.   
* Reflection on APIs: VisualUpdateAPI
    * The VisualUpdateAPI is easy to use because it as all the possible methods that the back-ed can call on the 
    front-end in one centralized location

### Your Design


### Flexibilty


### Alternate Designs
* Immutable States and Moves
* GUIDisplay implementing VisualUpdateAPI

### Conclusions


