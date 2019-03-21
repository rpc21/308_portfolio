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
        * In order to add in a new type of syntax, add the regular expression with a defining key into the Syntax 
        properties file. 
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
    
    
* Code Consistency:
    * The code is mostly consistent between members.  The naming conventions can be slightly inconsistent in 
    parameter names.  A couple places in Parser such as splitCommand(String s) and addNameChild(CommandNode 
    currentNode, String s) don't use full names for the parameter names which is what we had done throughout the rest
    of the program generally uses full words in camel case for parameter names.
    * Another inconsistency throughout the code is that some instance variables begin with "my" and others do not.  
    Everyone was somewhat inconsistent with this convention so in some ways we were consistently inconsistent. 

### Your Design
* High Level Design:
    * Class Functionality:
        * GUIDisplay:
            * The GUIDisplay class is in charge of setting up all the GUI components.  We decided to create classes 
            for each of our GUI components so the GUIDisplay is mostly just responsible for calling the constructors 
            of the GUIComponents we had already created and set the preference of.  The GUIDisplay class is also 
            responsible for organizing the components for batch updates such as language changes.
        * StackedCanvasPane:
            * The StackedCanvasPane is responsible for updates to the canvases and turtles.  
        * DisplayView:
            * DisplayView is the abstract super class for all the different turtles.  The DisplayView handles updates
             to the turtles and moving the turtles around the canvas
        * Palette:
            * Palettes display an index corresponding to a node and can be used change a turtle icon or color by 
            using the index of the corresponding palette element
        * TabExplorer:
            * Creates tabs that display information about the current state of turtles, pens, variables, methods, etc.
    * Design Patterns:
        * Delegation:
            * In the front-end, there were often at least two ways any element of the display could change. This made 
            delegation important as having shorter methods reduced code duplication and allowed different parts of the 
            project to accomplish the same job under different circumstances.  The front-end also used a tree-like 
            delegation structure where the Delegator class can be seen as the root of the tree and delegates our visual 
            commands down to classes with more specific functionality to execute the commands.
        * Observer/Observable:
            * We used a variation of the observer/observable design pattern by using a LanguageChangeable interface 
            and grouping the language changeable components together in a list in GUIDisplay so when the language 
            changed we could notify all the language changeable components (observers)
    * Advanced Java:
        * Consumers/Lambdas:
            * We used consumers and lambdas to aid our encapsulation.  Instead of passing references to entire 
            objects to another class when classes needed to be able update another class, we would just pass lambdas 
            to methods that could make the specific update required.
            * Consumers and lambdas made the communication between gui components much easier and made it so gui 
            components only had the minimum amount of information necessary about other gui components
        * Reflection:
            * We used reflection and properties files to generate our choosers.  The reflection is used instead of a 
            factory design pattern because it eliminate the need of if-trees.  Also, the use of properties files 
            makes the design more extensible because if you want to add a new turtle icon you can just add the name 
            of it to the properties file and it will update all places in the code that use the class name since it 
            is retrieved from the properties file rather than having to update several hard coded places of the code.
* Design Checklist Issues:
    * Magic Numbers:
        * One of the biggest design checklist issues left in my part of the project is the use of magic numbers in 
        GUIDisplay to organize the GUI components in the grid pane.  In the future, this should be handled using a 
        properties file to assign the gui component to its position in the grid pane.  Therefore all positions would 
        be specified in the same location and it would be very easy to change the position of elements just by 
        changing the properties file.
    * DisplayView too many methods:
        * The DisplayView class has too many methods.  This is likely a result of the number of overloaded 
        constructors that the DisplayView has as well as its role in the chain of delegation.  DisplayView could be 
        refactored to have a DisplayViewDelegator object that will handle the delegation functionality of the 
        DisplayViewClass.  This would also help with our design issue that DisplayView is used to display non-moving 
        images in the palettes as well as the moving images in the canvas.  If we split up the class into two 
        different kinds of display views (static and moving) they would each have more specific purposes and the 
        class would be smaller.
* Good Feature: Palette/PaletteElement
    * The Palette and PaletteElement classes are probably the best designed part of the project that I worked on.  
    The classes use Java generics to be able to handle many different kinds of palettes.  I initially wrote a Palette
    abstract super class and then created separate subclasses for the TurtlePalette and ColorPalette.  I then 
    realized that the two palettes had very similar code and purposes but different types.  When we learned about 
    generics in class I saw the Palettes as a great place to be able to use the generics.  After implementing the 
    generics version of the palette I was able to get rid of two classes and my code became much more easily 
    extensible.  Now to add a new kind of palette, instead of extending from an abstract class and changing the code
    to be able to handle the type of palette, I can just create a new typed palette in the GUIDisplay of whatever 
    node type I want and all the functionality will already be implemented.
    * The dependencies for this class are that when it is instantiated you have to keep track of the type of Palette 
    you created so the methods you use use the correct types.
    
* Bad Feature: DisplayView
    * As mentioned above, the DisplayView has too many methods and too many constructors indicating that it has too 
    much responsibility and is being used too generally.
    * The number of methods and responsibilities the DisplayView has could be decreased by creating a 
    DisplayViewDelegator that handles the delegation responsibilities of the DisplayView.
    * The number of constructors could be decreased if we separated DisplayView into two different kinds of 
    DisplayViews, a static (non-moving) one for display in the palettes, and a moving one that runs on the canvas and
    can draw on the canvas, etc. 
        * A potential design for this would be to create the non-moving DisplayView as the top level super class and 
        then have the moving DisplayView extend from it seeing as it would have extended functionality (being able to
        move around and draw on the canvas)
    * Another issue with the DisplayView class is that the subclasses are essentially containers that just specify 
    the image for that icon but keep all the other functionality the same. It may be better to change from icon to 
    icon using a setIcon method or changeIcon method rather than creating a new display view and copying the state.  
    This would save us from a lot of bookkeeping and would prevent us from using so many container subclasses.

   
### Flexibility
* Design Flexibility:
    * There are several design patterns and design choices we made to enhance the flexibility of the design. One of 
    these design choices was using the command design pattern for our command nodes.  This design choice makes it 
    easy to add new nodes because they just need to extend the CommandNode abstract superclass, implement the 
    evaluate method and be added to the appropriate properties files.  
    * Similarly, adding visual commands is one of the most extensible parts of the project.  To add a visual command 
    one only has to create a subclass of VisualCommand and either conform the call on the VisualCanvasAPI object to 
    be one of the calls already in the API or add an appropriate method to the API and have the Delegator class 
    implement it by delegating to the appropriate GUI component.
    * Adding various GUI elements such as a new kind of palette or a new chooser is also very extensible.  Our use of
    generics to create the palettes makes it so all that someone would have to do to add a new kind of palette is 
    just instantiate it with the type of node they want and start adding elements.  Likewise for the chooser, they 
    would have extend the abstract PaletteChooser class, specify a properties file to use for the chooser, and 
    provide a consumer for what to do with the selection.
    * The parser is also very extensible to adding new commands of the same types that we already have (just need to 
    specify the number of arguments for that command and make sure it is in the correct properties files).  The 
    parser can also easily add commands of a new syntax by specifying the regular expression and defining key in the 
    Syntax properties file.

* Feature I did not Implement 1: Command Nodes
    * This code is interesting because it is so simple (such few methods and few lines of code) yet it carries so 
    much of the functionality of our program because it implements all the possible commands that can be run by the 
    user.
    * The main class used to implement this feature is the CommandNode class which is an abstract superclass that 
    defines the default properties of a CommandNode as well as the default functionality. CommandNode has many 
    subclasses that extend from it to implement the concrete commands.  The commands/Arguments property file is 
    required for the class to be able to get the maxChildren.
    * The ways command nodes are created and evaluated are closed to modification.  Command nodes are created using 
    reflection on a string received from the parser meaning nothing needs to be changed in a factory for the addition
    of a new kind of command node.  Also, all command nodes are evaluated by calling their evaluate method meaning 
    when new subclasses of command nodes are added, no new methods or logic need to be able to added to evaluate the
    commands, the evaluate command just needs to be implemented.
    * One assumption I can see is 
    that there is the assumption that the children of the node will come after the command node in the tree rather 
    than before it.  This assumption assumes that the parser is creating the tree so that the children are in the 
    correct position. A second assumption in the CommandNode implementation is that there will be a known number of 
    children before it is evaluated.  This assumption caused us problems when we were trying to implement the 
    extension that allows for any number of arguments such as fd 10 20 30 40.
    * Changes to the number of children of a command node could be hard for this design to handle when it comes to 
    flexibility. For example, if a command's number of arguments changes based on when or where it is called relative
    to other commands, our code would not be able to handle this because it reads in the number of children from a 
    properties file so it is set and cannot be changed dynamically.  Other than in this case, as mentioned before, 
    the CommandNode implementation makes it very easy to add new commands.


* Feature I did not Implement 2: Parser
    * This code is interesting to me because it does so much of the logic of the SLogo program and is so important to
    its overall functionality yet it is very few lines of code.  I am also interested in this code because the 
    parsing always seemed like an interesting challenge to me and I am interested in how the regular expressions are
    used to parse in an intelligent way instead of using a mess of logic.
    * This feature is implemented using the Parser class, CommandFactory, UserCommand, UserCreated and Validator.  
    The important resources for the parser are Exceptions, PackageLocation and Parameters properties files as will as
    the PropertiesKeys txt file.
    * The parser code is closed to adding new commands of similar types to the commands that are already accepted.  
    The commands simply have to be added to the properties files and the parser will be able to handle them.  The 
    implementation details of what kind of command is being created is somewhat encapsulated.  There is some logic to
    detect user-created commands, method declarations, and when nodes need the name which would ideally be handled 
    with some kind of abstraction so we could avoid detecting each of these different situations separately, but the 
    differences in these types of commands warrant the need for this logic, in my opinion.  Along the same lines, the
    parser code is not very flexible when it comes to allowing new possibilities such as users defining their own 
    methods, etc.  Another area in which the lack of flexibility in the parser hurt us was in trying to implement the
    extension about allowing commands with an unknown number of inputs such as fd 20 30 40 60 because it was not in 
    the format we were expecting.
    * As mentioned above, the code is easily extensible when adding commands of similar types to the ones already 
    created but not as extensible when adding completely new ways for the user to interact with the program.
    

### Alternate Designs
* Success of Original Design in Handling Extensions
    * Handling the new commands that had to be created
        * We were able to handle the addition of new commands easily because the command design pattern we used and 
        the abstractions and hierarchies we created in terms of CommandNodes, TurtleCommands, and VisualCommands were
        able to really easily accommodate the addition of new commands.  To add new command nodes we simply had to 
        extend the appropriate abstract class, add the command properties to the correct properties files and 
        implement any additions to the VisualCanvasAPI if applicable.  The command design pattern and the command 
        node hierarchies were definitely one of the best designed parts of the project.
    * Handling Graphical Update to Turtle on Front-End:
        * We were able to handle this addition rather easily by creating commands in the front-end using the 
        appropriate string concatenations and passing these commands to the parser to run through the background. 
        Although this was rather easy to implement, it is likely not the best design.  In some sense we are using the
        parser as a controller for the front end to be able to update the state of the back-end but it requires the 
        front-end to know a lot of information about commands and hinges on what feels like rather risky string 
        concatenations and hard-coding of commands.  It would perhaps be better to have a more traditional controller
        (rather than the parser) that is able to communicate the front-end changes to the back-end so the program is 
        not as reliant on the parser and the front-end does not require as much knowledge or logic involving the 
        commands.
        * The dependency of having to update certain state properties on the back-end to allow for undoing commands 
        also made it harder for the front-end to simply communicate to itself seeing as most updates had to go 
        through the back-end.
    * Challenging Extensions:
        * We decided to not implement recursion, grouping, unknown number of commands, and animation.  We decided to 
        prioritize functionality and cleaner code over going for the maximum amount of functionality and thought that
        doing a good job implementing most of the extensions would be better than doing an okay job implementing all
        of the extensions.

* Design Decision 1: Immutable States and Moves vs. VisualUpdateAPI
    * We spent a very long time discussing whether we should pass information from the back-end to the front-end 
    through immutable turtle state and immutable pen-states or through the VisualUpdateAPI.  
    * At first we thought it 
    would be easier to just pass the front-end the entire state of each turtle from the back end and the front-end 
    could query that turtle state to update the display of the front-end turtles.  This solution has the benefit of 
    making communication easy between front-end and back-end and it ensures data consistency because front-end and 
    back-end are consistently passing the entire state of program to each other.  Drawbacks of this approach include 
    the fact that we would basically be passing a container class back and forth that contains way more information 
    than is necessary to make the updates.  For example, if only the x-position of a turtle is changing, it seems 
    like overkill to pass an entire turtle state to the front-end.  Additionally, the process of iterating through 
    the turtle state and looking for what has changed appeared that it would lead to repetitive, redundant code that 
    we were trying to avoid. 
    * The VisualUpdateAPI, on the other hand allowed us to have the back-end directly call methods on the front-end, 
    only passing the information necessary to make the update, leaving the specific implementation of how the update 
    is made to the front-end.  This approach gave us what felt like a better division of responsibility between 
    front-end and back-end.  Using the VisualUpdateAPI allowed the back-end to do the logic of determining how things
    needed to be updated, then passed only the relevant information to the front-end to implement the update.  The 
    VisualUpdateAPI also allowed us to use interfaces effectively to establish a contract between the front-end and 
    back-end as to what methods would be available for the back-end to call on the front-end.  A disadvantage of the 
    VisualUpdateAPI is that it is rather long and requires that one class implement a lot of methods that can have a 
    wide arrange of effects.  The counteract this downside of the VisualUpdateAPI, we created a Delegator class and 
    used a delegator design pattern to break up implementation of the API into more appropriate, specific classes.
* Design Decision 2: Role of GUIDisplay
    * Throughout the project we knew that GUIDisplay was an important class that had a lot of responsibility. 
    GUIDisplay is the class that is in charge of setting up the entire GUI, including all the GUI components.  At 
    times it was also the class that implemented the VisualUpdateAPI.  In the beginning it made sense for the GUI to 
    implement the VisualUpdateAPI because it had access to all the GUI components and could delegate appropriately.  
    However, it quickly became clear that between initializing all the GUI components and implementing the 
    VisualUpdateAPI, the GUIDisplay class had too much responsibility and was turning into a God class.  To try to 
    eliminate some of the responsibilities of GUIDisplay there were several steps we took.  First, we decided to make
    classes for each of the GUI components.  This allowed us to set the specific preferences for each component in 
    their constructor so GUIDisplay would just have to call the constructors and add the components to the grid pane 
    adn would not have worry about setting preferred height and text, etc.  A second change we made was to create 
    interfaces for LanguageChangeable components and CommandExecutable components.  This allowed GUIDisplay to just 
    maintain collections of these components and iterate through them calling the appropriate methods on each object,
    rather than having to implement all the logic and all the updates for a language change.  Finally, to reduce the 
    number of methods and responsibilities in GUIDisplay, we created a Delegator class than handled the 
    implementation of the VisualUpdateAPI.
    * Throughout the project the roles of GUIDisplay changed drastically from originally being a sort of God class, 
    handling many responsibilities because it could access all the GUI components easily, to being more of an 
    initializer and an entry point by the end of the project.  I feel that the final role of GUIDisplay is much 
    better design than its initial role because it follows the principles of Object Oriented Design of letting 
    objects do things such as change language or run commands for themselves instead of one class doing those things 
    on the objects.  Additionally, our final design follows the single responsibility principle where GUIDisplay is 
    simply in charge of initializing all the GUI components, rather than the multiple responsibilities of setting up 
    GUI components, implementing the VisualUpdateAPI, etc. 
    

### Conclusions


