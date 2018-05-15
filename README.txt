To assemble a jar file for your project, run the "jar" gradle task, either through IntelliJ or by doing
`gradle jar` on a terminal. Gradle will automatically download all dependencies needed to compile your jar file,
which will be stored in the build/libs folder.

Make sure to edit the main class attribute the build.gradle file, you'll need to change it in order to obtain
a working jar file.

REMEMBER TO DELETE THE DATABASE BEFORE INITIALLY ATTEMPTING TO RUN THE PROGRAM FROM WITHIN IDE. This can be done by removing the CS3733TeamQ folder continaing the log, seg(), etc files. (yes, you can delete it). The databsed will be repopulated automatically on startup. This is also the easiest method for clearing any edits made to the database through the map editor. 

Credit to all involved in this project:
The Quenched Quinotaurs:
Lead Software Engineer: Dan Duff
       Algorithms Lead: Nugzar Chkhaidze
       Database Lead: James Kenney
       Assistant Lead Software Engineers: Norman Delorey, Andrew DeRusha, Jesse d'Almeida, Aleksander Ibro
       Scrum Master: Maggie Goodwin
       Product Owner: Andrew DeRusha
       Test Engineer: Norman Delorey
       Documentation Analyst: Sarah Armstrong
       Software Engineers: Krysta Murdy
