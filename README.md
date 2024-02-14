    OVERVIEW
This project is my adaptation of Pac Man game implemented in java with graphical user interface using swing. Before starting the game you need to choose how big of a map would like like to play on this time(columns/rows). The games purpose is to 
collect all of the coins displayed on your map, while avoiding the ghosts. After getting hit by a ghost 3 times or collecting all the coins the game is over 
and you are asked for your nickname that will be stored using serialization in the txt file highScores.txt implemented in HSManager class.

    ADDITIONAL INFROMATION
All of the images are provided in the src/images folder with additional folder src/images/animations with pac man's  animation images, created as all other images 
in Aseprite. All of the games logic is implemented using the JTable component, the creation of walls makes sure to not create any dead ends as well too wide paths.
User and each ghost operate on different threads, all of the GUI is created in the MenuWindow class, while the logic is mostly put in the GamePanel class.
There is also provided a highScores.txt file where your scores will be saved and can be viewed in the main panel clicking the high scores button. 
They will be displayed using JList component. The condition how to display each cell in the JTable is implemented in ColorCellRenderer that extends DefaultTableCellRenderer.


    FEATURES IN THE FUTURE
- adding new, harder levels after completing the first one
- adding better and more animations to the pac man
- adding power ups to make the game more interesting
- refactor the code to be more easily understandable
- make user not targetable for 2 seconds after losing a heart
