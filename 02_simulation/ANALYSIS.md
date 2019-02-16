CompSci 308: Simulation Project Analysis
===================

> This is the link to the [assignment](http://www.cs.duke.edu/courses/compsci308/current/assign/02_simulation/):


Design Review
=======

### Overall Design

* High Level Design
    * Simulation is the backend of the project where the logic of what to display takes place.  The Simulation 
    abstract class and the concrete classes that extend from it are used to communicate information from the 
    configuration and visualization to the backend (simulation).  At the heart of the simulation are the cells that 
    are all subclasses of our abstract Cell class.  The cells implement the rules of the simulation to update their 
    states.  Each simulation has an Enum that enumerates the possible states for that simulation and the cell will 
    update itself to its next state using its current state and the rules of the simulation.  Another important 
    component of the simulation is the grid which is an object implementing the Grid interface.  The cells are able 
    to access the grid and are store in the grid.  The grid allows cells to find its neighbors (based on the cells 
    NeighborsType).  The grid is also able to add and remove cells to the grid and pass information about the cells 
    in aggregate to the visualization so they can be displayed.
    * Configuration communicates to the simulation by using our Simulation Factory which takes in the basic 
    parameters of a simulation (the grid size, grid type, and simulation name) to create a grid for the simulation to
     initialize a simulation with an empty grid.  The configuration then calls a method on its newly created 
     simulation to set the initial states of all the cells in the simulation.  This can be done with explicitly 
     defined states read in from the XML file or by using random states or just defining the percentages you want of 
     each kind of cell.  The XML parser class is used to extract the backend information from the XML file and to 
     create a new simulation and pass it the initial states. Configuration communicates information about the style of 
     the cells (how colors correspond to states, the shapes of the cells, etc.) to the visualization by the visualization creating a new XMLStyler and
      reading in one of our default style files.
    * Visualization creates a Simulation and can use this simulation object to get information about the cells.  Once
     the visualization has information about the cells, it is able to display the cells in a grid based on the cell 
     location, state (with a map of state to color passed from the styler), shape, etc.  The visualization has 
     several panels and buttons.  The panels are specific to each simulation and are displayed only when that 
     simulation type is running.  The panels can be used by the user to change parameters for the simulation in real 
     time.  These parameter changes are then updated in the back end using the simulation class as an intermediary 
     that passes a map of parameter changes from the GUI to the cells (that actually implement the rules based on the
      parameters).  When visualization wants to create a new simulation, it creates an XMLParser and will pass it the
       parameters set in the panel of basic controls (such as number of columns) as well as the simulation specific 
       parameters (such as % empty cells).  The XMLParser will then create a new simulation that can be used to 
       communicate back and forth between the backend and frontend.  
       
* Adding a New Simulation
    * Simulation:
        * Create an Enum enumerating all the possible states a cell in the new simulation can have.
        * Make sure the enum you create implements the CellState interace
        * Create a simulation specific cell that is extends Cell
        * Implement the abstract methods and override any methods that differ from the basic implementations given in
         the Cell abstract class
        * The rules of the simulation should be implemented in the specific cell class you create for the simulation
        * Create any agents that may be involved in the simulation (eg. SugarAgent, ForageAnt, etc)
        * Create a simulation specific simulation class that extends Simulation
        * Implement the abstract methods and override any necessary methods
        * In the simulation class, define the percentage fields corresponding to the different states in your enum in
         the form of "statenamePercentage"
        * In the simulation class, be sure to identify the data fields used in your simulation with strings that are 
        going to match the tags you want to look for in the XML file
        * Add this simulation and cell type to the possibilities in the SimulationFactory
    * Configuration:
        * Add the new simulation as a case to the getSimulationStates and getSimulationDataFields of the XMLParser
        * Create the XML files for the simulation based on the tag names you specified in the DataFields for this 
        simulation
    * Visualization:
        * Create a side panel for the simulation
        * Add the simulation as an option
    
* Dependencies:
    * The dependencies are clear and easy to find.  The configuration communicates to the simulation via a 
    SimulationFactory. Simulation and Visualization communicate to one another via methods in the Simulation's external 
    API.  Visualization gets information from configuration by creating an XMLParser object.
    
* One Component I did not Implement: Configuration (XMLParser)
    * Readability: The code in the XMLParser is very readable.  The methods are well-named, generally short, and erve
     specific, well-defined purposes.  An example of this readability is the parseRows method shown below:
    *  ````java
    private String[][] parseRows(Element root, int rows, int cols){
         NodeList cellRows = root.getElementsByTagName(CELL_ROWS_TAG_NAME);
         String[][] specifiedStates = new String[rows][cols];
         if(cellRows!=null){
             for (int i = 0; i < cellRows.getLength(); i++) {
                 parseColumns(root, specifiedStates, cellRows, i);
             }
         }
         return specifiedStates;
     }
    ````
    * Encapsulation: The XMLParser is well encapsulated.  In relation to the Visualization, the XMLParser could 
    entirely change how it is implemented and it would not affect visualizatoin at all as long as the XMLParser is 
    still able to produce Simulations from XMLFiles.  Likewise, the internal workings of the XMLParser do not 
    specifically matter as long as it can produce the initial states and basic parameters for the Simulation.  
    Neither the visualization or Simulation access any of the inner workings of the XMLParser and the Parser itself 
    is essentially a black box - very well encapsulated.
    * Design: By reading my teammate's code I have thought more about how to really focus on the endpoints of a class
     and the external APIs of a class to better think about encapsulation.  The XMLParser just needs to provide an 
     interface to the Visualization of producing a new simulation if given the necessary parameters and to simulation
      it just needs to pass the initial states and Simulation will take care of the rest.  There is no way to enter 
      the XMLParser "in the middle" of its job.

* Code Consistency:
    * The code is generally consistent in its layout, style and naming conventions.  An example of this is the shared 
    naming conventions between the front end and back end.  For example the hierarchies for the different types of 
    simulations have consistent naming conventions (SpreadingFireSimulation, GUISpreadingFirePanel, etc.).


    

    

1. You can order things with numbers.

You can put links to commits like this: [My favorite commit](https://coursework.cs.duke.edu/compsci308_2019spring/example_bins/commit/ae099c4aa864e61bccb408b285e8efb607695aa2)


### Your Design

* High Level Design:
    * Simulation is the backend of the project where the logic of what to display takes place.  The Simulation 
    abstract class and the concrete classes that extend from it are used to communicate information from the 
    configuration and visualization to the backend (simulation).  At the heart of the simulation are the cells that 
    are all subclasses of our abstract Cell class.  The cells implement the rules of the simulation to update their 
    states.  Each simulation has an Enum that enumerates the possible states for that simulation and the cell will 
    update itself to its next state using its current state and the rules of the simulation.  Another important 
    component of the simulation is the grid which is an object implementing the Grid interface.  The cells are able 
    to access the grid and are store in the grid.  The grid allows cells to find its neighbors (based on the cells 
    NeighborsType).  The grid is also able to add and remove cells to the grid and pass information back and forth 
    between visualization and the cells that are implementing the rules.  Simulation classes can communicate
* Design Checklist Issues:
    * Static Variables:
        * There is one static variable I use and the logic behind it is documented with a comment above it.  In the 
        Sugar simulation, the darkness of a cell is dependent on the maximum amount of sugar allowable on any cell.  
        Since all sugar patch cells need to know about this number and be able to access and update it, I used the 
        approach shown in the Wator source code we were given to use a static variable to access this globally shared
         state among the SugarPatches instead of passing the information to the simulation from each of the patches 
         and then passing it back to the patches because I didn't think the simulation should have to know about 
         something that only really involves the cells.  If this is bad design it could easily be fixed by a method 
         in the Simulation that would calculate the max amount allowable by any of the cells and then pass it to all 
         the cells.
    * Warnings from compiler:
        * We have deprecated methods that give compiler warnings.  The methods are no longer used and we could just 
        delete them if it is an issues but we wanted to leave them in just in case we wanted to revert back to an 
        older design easily.
    * Superclasses:
        * The Cell abstract class contains instance variables that are used by most of the simulations but not 
        necessarily all of them.  I thought it was better to have a couple go unused in a simulation here or there 
        than to have to repeatedly include all the variables in each simulation.  This could easily be fixed with 
        refactoring if we had more time.
    * Repeated Code:
        * The one area in which there is repeated code is in the Enums where there is a method to get the possible 
        states of that enum as a string.  I did not know how to resolve this instance of repeated code though because
         Enums cannot extend each other so I could not create a super enum with the method implemented and extend 
         from it.  I also can't implement a method in an interface.  One possible solution would be to implement this
          method in the Cell class itself and then use the fact that every cell has some enum that is the current 
          cell state to be able to get all the possible states as strings.
    * Down Casting:
        * The Forage Simulation has a lot of ForagePatch-specific methods that are not common to all cells so I am 
        consistently having to downcast the cells returned by the grid to a ForagePatch so I can call the appropriate
         methods.  We made this design choice because it seemed better to down cast in one simulation that for 
         visualization to have to check what kind of cell it has to display.
        * This problem could be fixed by using a CellVisualizer interface rather than an abstract class for Cell.  
        Therefore each of the cells would have to implement the specific methods called by visualization but could 
        have different functionality specific to their own simulations
        * Additionally, we possibly could have had a grid hierarchy where each simulation gets a grid that only 
        contains the cells corresponding to that simulation, all of course implementing the necessary methods that 
        get called by visualization to be able to display.
* Good Feature: Grid Implementation
    * I think one of the strength of the design that I worked on was the grid interface we made and the grids that 
    extended it.  By defining a specific interface and forcing all grids we wrote to implement the interface, it made
     the grids easily extendable and it was really easy to add the Toroidal grid during the complete implementation. 
      The grid interface makes it so that it doesn't matter to the simulation what kind of grid it is using (basic or
       toroidal), it can still call all the same methods, just the way they are implemented by the grid type is 
       different.  
    * Some design issues we wrestled with while designing the grids is the decision as to whether we should use a 2-D
     array or a map.  Ultimately, we decided that a 2-D array was better because it was much easier to calculate 
     neighbors using neighbor types and locations than to have to figure out all the neighbors for a cell and map the
      cell to its neighbors.  The map would have caused many more difficulties in adding and removing cells.
    * Another design choice we considered was whether we should make the grids general and just have the grids use 
    cell objects or whether we should make a grid hierarchy and pair each simulation with its own type of grid.  For 
    the most part, our choice of making the grid as general as possible worked well because we didn't have to creat 
    new grids for each simulation and reuse a lot of the code we already had.  On the other hand, in the case of the 
    ants simulation we had difficulties when the grid methods would return a list of cells and we would have to down 
    cast the cells to get the appropriate ForagePatches and the functionality they provide that we neeed to implement
     the logic of the simulation.
* Bad Feature: State Enums
    * The state enums such as GOLState, PercolationState, etc. started off as a good idea but when we got the new set
     of requirements they became somewhat pointless and just complicated some parts of the project.  IN the basic 
     implementation we used the state enums to tie the state to a String and to a color so that all the 
     visualitzation would have to do is ask the cell for its color and display the color.  However, when we had to be
      able to change state by clicking on the cells and when the colors for the states had to be read in from the xml
       file, we had to rework how we could get this functionality and the State enums became rather not useful.  
       Additionally, the use of enums lead to some repeated code because one thing we were consistently interested in
        was a list of all the possible states a cell could take but we couldn't figure out how to make all the enums 
        have this method without copying and pasting the code into each method.
     *  

You can put blocks of code in here like this:
```java
    /**
     * Returns sum of all values in given list.
     */
    public int getTotal (Collection<Integer> data) {
        int total = 0;
        for (int d : data) {
            total += d;
        }
        return total;
    }
```


### Flexibilty

* What makes the code flexible or not?

* Describe two features you did not implement:
    * Good Feature: XML Parsing
        * The XMLParser hierarchy allows for any type of simulation to be read in using only two subclasses of XMLParserGeneral, one that handles reading in any XML Style file and one that handles reading the XML Simulation file. Since the XMLParser depends on the names of data fields defined in the Simulation’s class, and a consistent set of tag names for fields used across simulations, adding a simulation will not require any new code or changes in any of the XML parsing classes. There are separate methods in each of the parser subclasses that parse each of the individual fields in the file. This way, if any one input type were to be changed, for example initial states is now a map instead of a list of states, then only that one method responsible for parsing initial states would need to change. The hierarchy also reduces redundant code by creating subclasses that handle the 2 categories of XML files expected: the style file and the simulation file.
    * Bad Feature: Transfer of Information from Front End to Back End
        * This code is interesting because it shows the pitfalls of the way we transfer information between frontend 
        to backend.In general, information that is necessary to be reset is organized poorly. Between the simulation panels, the default panel, and the GUIGridOptions, it is difficult to synthesize the information in a manner that can be passed back to Simulation or to Configuration. This requires chains of method calls and non-intuitive fields in particular classes. For example in GUIGridCell, the cell needs a Simulation field in order to pass back a new state for a cell if a user clicks on it. It would be quite difficult to get the grid to display more than two grids or for the two to show different simulations. These two classes combined require GUIGrid, Simulation, GUISimulationPanel, GUIDefaultPanel, SimulationFactory, GUIChart, and GUISimulationFactory. This is evidence that there is something inherently wrong with this design and probably needs to be refactored into the various jobs both accomplish. If there was another source of information (say, a window was keeping track of the order of simulations, or the duration of each simulation), it is not clear how it could be cleanly implemented to pass and store this information.

### Alternate Designs

* Changing grid data structure:
    * If we needed to change the grid from a 2-D array to a map we would have to change our XMLParser to be able to 
    identify which cells are neighbors with each other and what the starting states of the cells are.  We would also 
    have to change our simulation factory to initialize the map instead of initializing a 2-D array of cells.  We 
    would also no longer need the neighbor definitions because the logic of the neighbors would be built into the map
     when we are building it.  We would also have to change our grid methods to return the appropriate values.  The 
     grid methods may be easier to write if we used a map instead of a 2-D array but it would be much more difficult 
     to parse the xml file and set up the map so the added ease in writing the grid methods would not be worth it.
    * For the most part, the original design handled the project's extensions very well.  In fact, when adding new 
    simulations I originally tried to make the cell logic to update states a little bit different to handle the new 
    simulations but we were running into problems and once I reimagined the new simulations fit them into our 
    original design it made the simulations much easier to implement. One difficulty we had when trying to add the 
    new simulations was how to handle the simulations that can have agents that move from cell to cell instead of the
     cells themselves changing state (ant simulation and sugar simulation).  It was difficult to display the agents 
     because we didn't have any methods in place for the visualization to know about whether or not cells had agents 
     on them and how to display the agents on the cells.  When addressing this design challenge we initially thought 
     it would be best to pass the visualization more information about the state to include both the state of the 
     cell and the presence of an agent but we later decided it would be more convenient for visualization to first 
     display the cells and then display the agents later.  The changes of being able to specify how the user wants to
      map state to color forced us to redesign how the visualization knew what to display for the cells.  At first, 
      we thought it would be best if visualization didn't know anything about the states of the cells and cells would
       just tell the visualization what color to display but then we redesigned it so that cells just told the 
       visualization their state and the visualization mapped that state to a color which is more in line with how 
       the front end and back end should be separated.
    * One design decision we made as a team was to have a cell hierarchy and to implement the rules in the cells 
    rather than just having a simulation hierarchy that handled all of the simulation logic.  The pros of this 
    decision were that the simulation classes could act as more of a controller between the model (cells) and view 
    (GUI), and the simulation didn't need to know all the details about how each cell got updated.  This decision 
    prevented our simulation classes from have too many purposes by delegating functionality to the cells.  If we had
     the simulation handle all the simulation logic we would have had cells that just held their state and we would 
     then have classes based on state rather than functionality which is not effective design. 
    * A second design decision we made was to implement our grids as 2-D arrays of cell objects.  We also considered 
    a map of cells to their neighbors which would have allowed for more flexibility if the cells were not arranged in
     a grid, but it would have been much more difficult to initialize the grid.  We thought it was much easier to 
     transform the more difficult shape grids such as triangles and hexagons into a 2-D grid and calculate their 
     positions for display and their neighbors using array logic than two have rearrange an entire grid map just to 
     change shape or neighbors.  Anna and Dima probably could have figured it out but I would have really struggled 
     with the bookkeeping of keeping track of all the cells and their neighbors if we were to have used a map 
     implementation instead of 2-D grid. 

Here is a graphical look at a sample (Spreading Fire) of my design:

![This is cool, too bad you can't see it](SimulationCell.png "An alternate design")


### Conclusions
* Best feature of the project: XML Parser
    * I think the XMLParser is perhaps the best designed part of the project.  There are possibly some small fixes 
    that could be made in the methods but the design of the XMLParser is effective.  The XMLParser essentially is a 
    black box that takes in an XML file and produces a simulation with the implementation of how this conversion 
    happens hidden to the other classes.  The XML Parser also has the most thorough error checking in our project.  
    There are so many ways in which XML files could be invalid but the XML Parser, in conjunction with the 
    SimulationFactory do a great job handling invalid input trying to make a simulation as close to what the user is 
    trying to create as possible.  The XML Parser taught me a lot about how to encapsulate functionality and protect 
    against errors.
    * The worst feature that remains in our program is the communication between the front end and back end.  Every 
    time we had to be able to communicate a change entered by the user from the front end to the back end we really 
    struggled to connect the two ends.  I think this could be better designed using a more well-defined Model View 
    Controller design.  As it currently stands we have the GUI as the view, the Simulation class as a 
    pseudo-controller and the cells as the model.  If we more formally defined these roles within the MVC format I 
    think we could have more effectively sent user input from front end to back end in consistent ways.
    * How to be a better designer:
        * I need to keep thinking in terms of functionality and common functionality between classes to design 
        hierarchies that effectively use inheritance and polymorphism while presenting simple, consisten interfaces 
        to the other parts of the project.
        * I need to start using design patterns more intentionally.  Now that I am aware of different common design 
        patterns to solve the common problems I could make cleaner designs by implementing the design patterns in a 
        stricter sense rather than using pseudo design patterns that I come up with like I did in this project.
        * I need to stop looking so much at how classes (specifically subclasses) are different and look more at how 
        they can be the same.  In this project I felt the urge to write the simulations as though they were stand 
        alone simulations instead of looking at them in the context of the other simulations.  This made it much more
         difficult to communicate between config, visualization and simulation until I refactored them to be more 
         consistent with each other.
         
    
