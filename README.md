# SchedulingManagementSystem
A scheduling management app built for a Java course required for my degree.

This makes use of the JavaFX library and a MySQL database.
Use of the MySQL JDBC Connector library is needed to link the application to the database.

The app allows a user to login and create and delete customers with personal information such as names, addresses, and phone numbers.
From there users can create and delete appointments with said customers and information such as appointment description, date and time, appointment type, and location.
There is a separate view provided for users that shows a calender layout with appointments. This view can be adjusted to either a monthly or weekly view.
Theres is also a few different reports that can be created with the press of a button should the user need them.

A SQL script is provided in the repo to build a database should you wish to try the app yourself.
The code points out where you may need to change out some parameters to the connector object and the repo has images that consist of the same info.

The code to setup the DB connection is in the schedulingmanagementsystem.java file under src/schedulingmanagementsystem/
