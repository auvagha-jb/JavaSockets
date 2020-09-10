# JavaSockets
A Java Application to demonstrate socket functionality  

## Dependencies
* JDK>=14
* sqlite-jdbc-3.32.3.2 -> SQLite database connection 
* jcalendar-1.4 -> A custom date picker

## Running the program
This is a NetBeans project so the latest version of **Apache NetBeans** is recommended. <br>
For any other IDE, follow the respective import instructions. For Intellij, you can find the doc <a href="https://www.jetbrains.com/help/idea/netbeans.html">here<a/> <br/> 
You can retrieve the jar file versions used from the jar package or download the latest version to your machine<br>
<br>
**For Linux and Mac based systems: change the DATABASE_PATH in utils.DatabaseUtil to a valid local path in any unrestricted folder. No further action required for windows** <br>
Run ui.ServerApp then ui.ClientApp in that order<br>


## Finding your way around: The Packages
* database -> Contains the database query logic
* jars -> Contains the version of jar files used
* models -> Contains the classes for the entities used in the project: Toy, Manufacturer e.t.c
* protocols -> Contains the logic for handling socket requests
* sockets -> Contains the socket initialization
* ui -> Contains the user interface design and logic
* utils ->  Contains helper files
