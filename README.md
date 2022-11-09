# JavaSockets
A Java Application to demonstrate socket functionality in distributed systems using a chat application

## Dependencies
* JDK>=14
* sqlite-jdbc-3.32.3.2 -> SQLite database connection 
* jcalendar-1.4 -> A custom date picker


## Finding your way around
### The Packages
* database -> Contains the database query logic
* jars -> Contains the version of jar files used
* models -> Contains the classes for the entities used in the project: Toy, Manufacturer e.t.c
* protocols -> Contains the logic for handling socket requests
* sockets -> Contains the socket initialization
* ui -> Contains the user interface design and logic
* utils ->  Contains helper files

## Running the program

### Prerequisites
* Import source code correctly. Note that this is a NetBeans app so if you are on a different IDE see the correct way import to import a NetBeans project.
* Make sure you have resolved all jar file dependencies before you attempt to run the program
* Note it is more optimal to run the ui.ServerApp before ui.ClientApp 

### Server Start-up: ui.ServerApp
On server start-up, the server socket is bound to a random port which is displayed on the top right corner. Take note of this as it will be needed for connection on the client side.<br><br>
![image](https://user-images.githubusercontent.com/38747358/93003878-0ac11600-f54b-11ea-8ec4-9f55345c8131.png)


### Client Start-up: ui.ClientApp
On client start-up, the first thing you need to do is connect the client socket. You can do this by clicking the "connect" button on the bottom left corner.<br><br>
![image](https://user-images.githubusercontent.com/38747358/93003074-486e7080-f544-11ea-839a-62612aef25fd.png)


### Establishing connection
Once you click connect, a popup dialog will appear prompting you to enter the port number. Make sure you enter the port number you saw earlier on the top right corner of the server.<br><br>
![image](https://user-images.githubusercontent.com/38747358/93002308-c62f7d80-f53e-11ea-936d-399b4aef7693.png)
<br><br>
If done correctly, you will see the dialog box below.<br><br>
![image](https://user-images.githubusercontent.com/38747358/93003913-39d78780-f54b-11ea-8c33-1356ed8296e6.png)


### Adding a new toy
To add a new toy fill the form below, ensuring price and batch number are the numbers. Manufacturers are populated from the database so if the manufacturers for a toy is missing from the dropdown list. Add them from the "New Manufacturer" form.<br><br>
On successful submission you will receive a message dialog and the server will be notified with the details of the new toy.<br><br>
![image](https://user-images.githubusercontent.com/38747358/93003801-6dfe7880-f54a-11ea-9892-25846a8cf853.png)

### Adding a new manufacturer
To add a new manufacturer fill in the form below, making sure the zip code is a numeric input.
On successful submission you will receive a message dialog and the server will be notified with the details of the new manufacturer.<br><br>
![image](https://user-images.githubusercontent.com/38747358/93003827-a56d2500-f54a-11ea-9e02-5640056ef970.png)


### Client-Server communication
#### Server to client
Select an action from the dropdown , then click the send button. The message should appear on the client side.<br><br>
![image](https://user-images.githubusercontent.com/38747358/93003764-12cc8600-f54a-11ea-8475-68f3e48b40ce.png)

#### Client to server
To send a message from client choose one of the actions from the dropdown and click send. The message should appear on the server side.<br><br>
![image](https://user-images.githubusercontent.com/38747358/93003746-e9abf580-f549-11ea-865d-65f5ec03853a.png)


