CompSci 308: Final Project Analysis
===================

> This is the link to the [assignment](http://www.cs.duke.edu/courses/compsci308/current/assign/04_final/):


Design Review
=======

### Overall Design
We wanted our design to be as **modular** as possible to **reduce dependencies** and make different parts of our 
project to be easily compatible if we were to replace one module with a different implementation of the same general 
ideas.  We also felt using modules would aid the development of our project by allowing us to split into sub groups 
that work on implementing the previously agreed upon **external APIs** to communicate between modules.  Another 
priority of our when designing our project was to give the user maximum **flexibility** and **customizability**. 
These ideas are reflected in our choice to use an **Entity-Component-System** design in our engine, in the design of 
our authoring environment and in the user profile features of our game center.

1) Authoring
    * Authoring has the main priority of **flexibility** for the user. This was accomplished by giving the user many 
    different panels in which they can customize their game. The authoring environment uses **property binding** and 
    a propagation mechanism to ensure any changes in one part of the authoring environment are transferred 
    appropriately to all other relevant parts of the authoring environment. The authoring environment is dependent on
     the data module and leverages the data API to save and load games and assets. The authoring environment is also 
     dependent on the engine and creates actions and events as defined in the engine subclasses. The engine exports 
     several packages of concrete classes to the authoring environment for the authoring environment to be able to 
     create the appropriate actions and events. We were initially hesitant to export concrete classes because we 
     thought it would be better to just export the abstract classes from engine and let authoring extend these 
     classes and implement the .execute() methods of the actions. However, this presented a serialization issue with 
     the xstream module so we decided it was necessary to export concrete classes from engine to authoring to make 
     our serialization process easier.
    * The design of the authoring environment is only very loosely tied to the scrolling platformer genre. The one 
    dependency I can see is the camera component that has to be tied to an entity that determines the scrolling. 
    Otherwise the authoring environment could easily be used to create games of another genre.
2) Center
    * The center is had the goals of being **interactive** and social. The center was one of our key extensions for 
    the project and we wanted it to feel like a real game center where you could browse games, get further 
    descriptions about the games, rate the games, see high scores and have well developed user profiles. The center 
    is dependent on the runner to launch the games and is powered by the data module that stores all the information 
    that is displayed in the center. The relationship between the center and data modules is the best I have ever 
    seen **model and view** work together in any of the projects I have done in this class. The center can simply 
    call variations of put and get on the data module to add to and query the model and then takes this information 
    and displays it in a very elegant way.
    * The game center can support any type of game easily. The genre of game makes no difference whatsoever to the 
    game center
    * The game center is also very easily **exstensible**. We were able to add in user ratings, profiles and 
    statistics in under a day bcause the backend and front end were so separated. The game center could also easily 
    be used in a different project completely unrelated to games in order to display information about various 
    entities and handle user profiles and ratings.
3) Data
    * The high level goal of the data module was to provide an api to the other modules for data management whether 
    it is storing images, sounds, user information, or game information. The data module also was designed to 
    **encapsulate the implementation details** of file storage, and data transformations such as serialization away 
    from 
    the other modules so they do not need to worry about the backend implementation of how data is stored and 
    accessed. Furthermore, the data module was intended to be our utility module so we had the goal of making it as 
    general as possible so other teams could easily plug the module into their project and be able to use the same data management tools. 
      
    *  The data module's api is essentially a series of puts and gets (saves and loads) that allow the other modules 
    to save information they want saved and be able to retrieve it later. The authoring module would use the data module to save and load games, sounds and images to our database.  The launcher used the data module to manage users and their passwords. The data module allowed the launcher to create new users and verify their login information. The game center loads in all game information (GameCenterData) objects from the data module, saves and loads all user profile information such as user bios and profile pics using the data modules and creates and displays comments, ratings, and high scores using the data module. The runner depends on the data module for loading in a new game or checkpoint, saving the game or checkpoint, and updating high scores. Finally, the engine uses the data module to load in the images and audio files used in the game.

4) Engine
    * The project design adopts an **Entity-Component-System architecture**: any game level played by the user is built 
    on a set of entities, and an entity can possess any combination of components. The `Engine` class accepts a `Level` object from `Runner`, and retrieves the entities and events (that were created by user in the authoring environment) from the `Level`. 
    * Internally, engine is built on multiple systems, each of which serves certain functionalities under general 
    platformer game rules, such as movement control, collision detection, ImageView display, etc. On every game loop, the `Engine` class receives input Keycodes from `Runner`, invokes the systems to update `Component` values for every `Entity`, executes each `Event` to trigger certain actions when corresponding conditions are met, and returns the updated entities back to `Runner` for front-end display.
    * The engine is actually not at all tied to the genre of scrolling platformer. The engine simply goes through the
     entities, collisions and events each step through the game loop and updates everything according to the rules 
     specified in the authoring environment. Hence, if there are rules implemented for a fighting game or tower 
     defense game the engine could easily allow for a tower defense game, speaking to the **flexibility** of our 
     design. 
    * The engine only depends on the data module which it uses to load in the assets associated with a game which are
     then **cached** for **efficiency** which help make our game engine one of the least laggy in all the groups.
5) Runner
    * At a high level the goal of the runner is very simple. The intanstiation of a new runner object should create a new window on the screen where the game is played. The idea is that the object that creates the GameRunner will not have to make any calls on it other than its creation. This way, it can be called in several places (center or authoring) and they both have the same functionality. The runner will manage the game as its being played and use the engine to update the state of the game. The runner uses systems that loop through the entities. Specific systems act on entities if those entities have the desired components and this is how the front end of the game is updated.
    * The runner is essentially the view of the engine. Much like data and center, the runner and engine of good 
    **separation of frontend and backend** and the runner could easily be detached to support the display of any 
    engine if given the correct entities to display.
6) Launcher
    * The Launcher is focused on enhancing the **user experience** and is used to tie together the authoring 
    environment and game center on the front end so a user 
    doesn't have to close the application to go back and forth between the game center and authoring environment. The
    launcher is therefore dependent on the authoring environment and game center for launching purposes and the data
    module to handle user authentication.
    

There are no new resources that need to be added to create a game. The only thing a user may want to add is a new 
asset such as an image or a sound but our authoring environment lets the user add an asset in the authoring 
environment so they can do that in real time and don't need to touch the code at all.

#### Exported Module 1: engine
* The project design adopts an **Entity-Component-System architecture**: any game level played by the user is built 
on a set of entities, and an entity can possess any combination of components. The `Engine` class accepts a `Level` object from `Runner`, and retrieves the entities and events (that were created by user in the authoring environment) from the `Level`. 
* Internally, engine is built on multiple systems, each of which serves certain functionalities under general 
platformer game rules, such as movement control, collision detection, ImageView display, etc. On every game loop, the `Engine` class receives input Keycodes from `Runner`, invokes the systems to update `Component` values for every `Entity`, executes each `Event` to trigger certain actions when corresponding conditions are met, and returns the updated entities back to `Runner` for front-end display.
* The engine is actually not at all tied to the genre of scrolling platformer. The engine simply goes through the
entities, collisions and events each step through the game loop and updates everything according to the rules 
specified in the authoring environment. Hence, if there are rules implemented for a fighting game or tower 
defense game the engine could easily allow for a tower defense game, speaking to the **flexibility** of our 
design. 
* This code is **flexible** and easily **open to extension** because of the **Entity-Component-System** design. If 
you want to include a new component in your game, from a backend perspective, you just have to create a subclass of 
the kind of component you want to create which is really easy seeing as components are essentially variable holders. 
However, on the front-end, to add the possibility of a new component in the authoring environment is very difficult 
because the different components often have different kinds of constructors. A more general **superclass** would have
 made the engine's exported API much easier to use.
* In terms of **replacing the concrete implementations with abstractions**, the engine exports several packages of 
concrete classes to the authoring environment for the authoring environment to be able to create the appropriate  
actions and events. We were initially hesitant to export concrete classes because we thought it would be better to 
just export the abstract classes from engine and let authoring extend these classes and implement the .execute() 
methods of the actions. However, this presented a serialization issue with the xstream module so we decided it was 
necessary to export concrete classes from engine to authoring to make  our serialization process easier. 
* This API does not really **encapsulate implementation decisions**, it actually forces a lot of implementation 
decisions to be made in the authoring environment by providing only the bare essentials. Furthermore, the engine has 
many **back-channel dependencies** that are included in out of date properties file describing which components must 
go together and what each entity must have in order for the engine to function properly making the API not **hard to 
misuse**. Fortunately, towards the end of the project, the engine team implemented much more **thorough error 
checking** to make the API **harder to misuse**.
* From reading the API I have learned that one part of the code being really well designed and super flexible can 
often lead to other parts of the code having to sacrifice their code design to accommodate the flexibility of another 
aspect. 

#### Exported Module 2: runner
* The runner API is incredibly **simple**, it just provides a constructor to launch the game runner that takes the name 
of a game and the name of the author that created it. The runner will then open up that game and allow the user to play.
* The runner also exports the Level and Game classes that it uses to manage the game flow in terms of level 
progressions and what constitutes a valid game. Exporting these classes and using these classes to define a game are 
somewhat of a **back-channel dependency** in the runner.  In retrospect it would be make the API **harder to misuse* 
if the runner constructor took in a Game object to make sure the error checking as to whether the game exists and is 
valid happens before it gets to the runner.
* This API only expects collections of Entities and Events as well as Levels, utilizing the **abstractions** provided
 by the engine to **encapsulate implementation details** from other parts of the project, making the runner able to 
 run almost any kind of game. However, these dependencies make our runner very tied to our **specific engine 
 implementation** because it filters for classes specific to our engine.
* In our current design, the Game and Level objects could not **be replaced by an abstraction** because they are so 
tied to our specific engine implementation. If we were to **loosen the coupling** between our runner engine 
significantly we would potentially be able to make the Game and Level objects more abstract.  Additionally, the Game 
and Level classes server primarily as containers of information bundled in convenient ways for the runner to consume 
so an abstraction may not be necessarily as useful or provide as much flexibility as it can under other circumstances.
* In reading my teammate's code I have learned how hard it can be to **decouple** a backend and a front end, in this 
case the engine in the runner when they both use and are expecting the same concrete classes.



### Your Design

#### High level design goals and API
The high level goal of the data module was to provide an api to the other modules for data management whether it is storing images, sounds, user information, or game information. The data module also was designed to encapsulate the details of file storage, and data transformations such as serialization away from the other modules so they do not need to worry about the backend implementation of how data is stored and accessed. Furthermore, the data module was intended to be our utility module so we had the goal of making it as general as possible so other teams could easily plug the module into thier project and be able to use the same data management tools. 

The data module's api is essentially a series of puts and gets (saves and loads) that allow the other modules to save information they want saved and be able to retrieve it later. The authoring module would use the data module to save and load games, sounds and images to our database.  The launcher used the data module to manage users and their passwords. The data module allowed the launcher to create new users and verify their login information. The game center loads in all game information (GameCenterData) objects from the data module, saves and loads all user profile information such as user bios and profile pics using the data modules and creates and displays comments, ratings, and high scores using the data module. The runner depends on the data module for loading in a new game or checkpoint, saving the game or checkpoint, and updating high scores. Finally, the engine uses the data module to load in the images and audio files used in the game.

#### How classes relate to each other
Before refactoring, the DataManager class is the entry point of the data module for the other modules.  The 
DataManager class implements the data external api and contains all the necessary public methods for the data module.
This decision was made to ease communication with the other modules by only having to familiarize teammates with one 
class name to use with data and it made a lot of sense when we were originally just saving and loading games. 
However, when we began to expand what we stored to include images, sounds, user information and ratings, the role of
the `DataManager` got too large and would have been better split up into separate kinds of data managers for each of
the different assets. One key advantage of this design, however, is that DataManager entirely **encapsulates away 
the implementation decisions** of how we stored our data. Between the first and second sprint we switched from using  
only the file system to using a database and it did not affect anyone else's code. This demonstrates the flexibility 
of the **delegation pattern** used in the data module because it shows how different data related decisions can be 
made in different ways and we can easily change how we manage the data without affecting other parts of the project.

The DataManager calls methods on the `DatabaseEngine`. The `DatabaseEngine` delegates responsibility to the various
Queriers, that are each associated with a corresponding table in the database for querying the database. This 
decision was made to break up the responsibility of the DatabaseEngine and to
also limit the amount of code in the class to just be database resource management (opening and closing the connection).

#### Database Design Goals and Code
A large portion of my role on the team was not specifically related to writing code but more related to setting up 
the database and using the database wisely. I realize that storing images in a database is not a good practice, but 
we decided as a team that it would be better to just use the database to store the images than to set up another 
storage system just for the images and have to worry about more dependencies in our project and getting them set up 
on everybody's computer. Furthermore, I realize that there are some other database bad practices such as relying on 
the authoring environment to uniquely concatenate the author name and game name together to use as a prefix when 
saving images instead of having an author name and game name as fields that form a primary key with the image name in
 the Images table the database, but it is what our team decided to do so that the engine would not have to know the 
 game name and the author name when loading the images back in to be used in the game. 
 
A challenge that arose in my part of the project was managing strings. Seeing as a big part of the role of the data 
module was querying the database, there were a lot of queries to manage and each of them were a string. I tried to 
balance the trade off of **readability** and not hard coding strings by using `String.format` to build my strings and
 declare them as constants at the top of each `Querier`. The `String.format` makes it easier to read the full SQL 
 statement to get an understanding of what the query is trying to accomplish. 
 
As part of the design of the data module I was also concerned with **security** when accessing the database. For user
 passwords, I made sure we did not store plain text by hashing the password and authenticating a user by checking 
 that the hash of the password they entered matched the hash that was stored in the database.  Additionally, to 
 combat the threat of **SQL Injection Attacks**, I made sure all queries were executed using the `PreparedStatement` 
 interface. The prepared statements also allowed me to reuse some popular queries, enhance the **readability** of the 
 queries, and improve the **efficiency** of the database accesses.
 
Another concern with the database was **resource management**. I know that it is important to close all resources you
 open, and as the data module there were many resources opened and closed throughout. To be methodical about the 
 opening and closing of resources while not making the code too long or complex, I used **try-with-resources** wherever 
 possible to avoid the `finally` blocks. Additionally, I implemented the `DatabaseEngine` using a **singleton design 
 pattern** after much deliberation with some TAs. I decided this would be the best way to make sure that every 
 connection to the database gets opened just once and closed at the end. To ensure this, I overrode the `stop()` 
 methods of our JavaFX Applications to close the connection to the singleton instance of the database which would go 
 through and close all the prepared statements (which closes their ResultSets), having a cascading effect of closing 
 all open resources.

#### Design Checklist
As mentioned above, I struggled to manage the strings that were to be used as SQL queries. I left them as constants 
defined in the class, rather than reading them in from a properties file, to help with the readability of the code so
the query in `String.format` form would be right there so another maintainer could easily see what query was being 
run and match up the strings (also declared as constants) with the `%s`. I also was sure to use prepared statements 
for all the SQL queries.
 
There are two main issues that show up in the Design tool. The first has to do with not logging exceptions and just 
throwing them or catching them instead and we agreed internally as a team in each case how we wanted to handle the 
exceptions, so I don't think it is too big of an issue. The second issue that comes up in the design tool is that the
`DataManager` and `DatabaseEngine` classes have too many methods and should be split up into different classes. When 
talking to my TA about this, he said that it is okay if a class is serving as a delegator for it to have a lot of one
line methods but, as mentioned earlier, I think it would be better design to split the DataManager up into three 
classes, one to manage assets, one to manage game data, and one to manage user information and to implement the data
external api across multiple classes that follow the **single responsibility principle**.
  
Another design checklist issue not mentioned on the tool is a naming convention that we agreed on internally as a 
team to refer to GameCenterData objects as game info and the Game objects that hold all the entities and events as 
game data, but to an outsider I realize these **naming conventions** are not intuitive and should be fixed to make 
the api **harder to misuse** and easier to understand.

Finally, IntelliJ gives a lot of errors about **duplicated code** between different `Querier` subclasses, but I could
 not find a way to remove that duplication no matter how hard I tried because each prepared statement had to be 
 initialized and prepared individually. I tried using maps and lists to set them all up in a for loop but it appears 
 that this amount of duplication was unavoidable


#### One Good Feature: Querier hierarchy
I chose this code because I thought it was an interesting and effective approach to breaking up how the database is 
accessed by defining Java classes that correspond to different tables in the database so the information about the 
tables can be **encapsulated** so only that querier knows about it. This class hierarchy also made for easier 
**resource management** and was inline with the **single responsibility principle**

This feature is requires the abstract superclass `Querier` and its subclasses as well as the `DatabaseEngine` and 
its connection to the database. The inheritance allows for the removal of a lot of **duplicated code** when it comes 
to closing the statements and enhances the **readability** of the code by placing all the database accesses in their 
own distinct sections of the code.

The main issue that I wrestled with when designing this aspect of my code was how to manage all the databases 
accesses in terms of where do I store the strings used in the sql queries, where do I store the information about the
 database schema and table structures, and how do I make sure I manage the resources carefully to be sure to close 
 all resources I open.

The main assumption that affects this part of the code, and the data module as a whole is that the game name + author
name will be unique. One author cannot make two games of the same name.

#### One Bad Feature: DataManager and DatabaseEngine class sizes
One poor design decision was to have the DataManager class be the main point of entry for other modules to  
access the Data module and to have the DataManager implement the entire API, making it close to a **monolithic class**. 
This decision was made to ease  
communication with the other modules by only having to familiarize teammates with one class name to use with data and
it made a lot of sense when we were originally just saving and loading games. However, when we began to expand what  
we stored to include images, sounds, user information and ratings, the role of the DataManager got too large and 
would have been better to split up into separate kinds of data managers for each of the different assets. One key 
advantage of this design, however, is that DataManager entirely encapsulates away the implementation decisions of how
we stored our data. Between the first and second sprint we switched from using only the file system to using a 
database and it did not affect anyone else's code.

Again, the main assumption that affects this part of the code, and the data module as a whole is that the game name + 
author name will be unique. One author cannot make two games of the same name.

### Flexibility


### Alternate Designs


### Conclusions

