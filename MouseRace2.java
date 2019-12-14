import java.util.Scanner;
import java.util.Random;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;

import java.util.Stack;


/*
* CAMERON KEEPERMAN
* ETHAN FOX
* FIDEL HERRERA
* 12/4/2019
* PROGRAMMING 21000
* FALL 2019
* PROGRAMMING ASSIGNMENT 6
*/


class MouseRace2 {

   /*
   * In this class are declared mouse location through row and column variables, cheese location
   * through row and column variables, the player score and the "move" variable which just captures
   * the players input for which direction they want to move.
   
   * The maze itself is declared in a nested array (2-dimensional), where the outer edge is all 0s
   * for the wall, and then 0s on the inside where the wall continues. The 1s represent open areas
   * where the mouse can move through in order to find it's way to the cheese. The goal of the player is
   * to maneuver through the maze in the fewest amount of moves and get to the cheese.
   * 
   * The game is won by the mouse and cheese occupying the same space(same row and same column).
   * The mouse can move but the cheese does not.
   */

      static int[][] maze =
            {   {0,0,0,0,0,0,0,0,0,0,0,0},
                {0,1,1,0,0,0,0,0,0,0,1,0},
                {0,0,1,0,0,0,0,0,0,1,1,0},
                {0,0,1,1,1,1,1,0,0,1,0,0},
                {0,1,1,0,0,0,1,0,1,1,0,0},
                {0,1,0,0,0,0,1,0,1,1,0,0},
                {0,1,0,1,1,0,1,0,0,1,0,0},
                {0,1,0,0,1,0,1,0,0,1,0,0},
                {0,1,1,0,1,0,1,0,0,1,0,0},
                {0,0,1,0,1,0,1,1,1,1,0,0},
                {0,1,1,1,1,0,0,0,0,1,1,0},
                {0,0,0,0,0,0,0,0,0,0,0,0}  };
      
      public int mouseLocRow = 1;
      public int mouseLocCol = 1;
      
      Random rand = new Random();

      public int catLocRow = 0;
      public int catLocCol = 0;         
                  
      public int cheeseLocRow = 10;
      public int cheeseLocCol = 10;
      
      public int playerScore = 0;
      
      public String move;
      
      Stack<String> mouseSt = new Stack<String>();
      Stack<String> catSt = new Stack<String>();
      
  
    public MouseRace2() throws IOException {

        System.out.println("**************************************");
        System.out.println("*** WELCOME TO THE MOUSE RACE GAME ***");
        System.out.println("**************************************");

        // Creating new topscore object and printing topscores
        TopScore scores = new TopScore();
        System.out.println("TOP TEN SCORES");
        scores.printScores();
        
        // Variables for location of the mouse
        this.mouseLocRow = mouseLocRow;
        this.mouseLocCol = mouseLocCol;
        
        // This loop will assign a cat a position until it is not inside of a wall
        while (maze[catLocRow][catLocCol] == 0) {
            this.catLocRow = rand.nextInt(12);
            this.catLocCol = rand.nextInt(12);
        }

        this.playerScore = playerScore;

        Scanner scan = new Scanner(System.in);
                
        
        // gameWon() is initially set to false, so while !false will keep the loop running until changed.
        while (!gameWon()) {
                        
            // This loop will print the maze, print the players score and then prompt user to input a direction for the mouse to move.

            printMaze();
            
            System.out.println("Your current score: " + playerScore);
            System.out.print("Select a move direction (n/s/w/e): ");
            
            move = scan.next().toLowerCase(); // This scans in user input to a lowercase variable
            
            parseCmd(move); // Inputting user command to the parseCmd method
            
            } // Game loop closer

        if (catWon()) {
            loserMessage(); // If catWon() returns true, returns loserMessage()
        } else {
            winnerMessage(); // Otherwise, returns winnerMessage()
        }
        

        // Writing player score to topscores.txt
        scores.writeNewScoreToFile(this.playerScore);
        
        System.out.println("\nTOP TEN SCORES");
        scores.printScores();
                

    } // MouseRace() closer


    public void cheeseMover(int cheeseLocRow, int cheeseLocCol) {

        // This method will move the cheese for specific objects

        this.cheeseLocRow = cheeseLocRow;
        this.cheeseLocCol = cheeseLocCol;
    }
    
    
    public void printMaze() {
        
        /*
        * This method prints the maze with the mouse and cheese using nested for loops.
        * When i = 0, j will increment by 1 each iteration and decide whether to print a wall, an open space,
        * mouse or cheese. Since the maze is a square, both i and j run until length of the maze.          
        */

        System.out.println(""); // Spacing
        
        for (int i = 0; i < maze.length; i++) {
            for (int j=0; j < maze.length; j++) {        		          	  
                if (i == mouseLocRow && j == mouseLocCol) { // Prints mouse when i and j are equal to the location of the mouse
                    System.out.print("%");
                    }
                else if (i == cheeseLocRow && j == cheeseLocCol) { // Prints cheese when i and j are equal to location of cheese
                    System.out.print("$");
                    }
                else if (i == catLocRow && j == catLocCol) {
                    System.out.print("C");
                }
                else if (maze[i][j] == 0) {
                    System.out.print("#");
                    }
                else if (maze[i][j] == 1) {
                    System.out.print(" ");
                    }
                } System.out.println(""); // Prints new line for new row of maze
            
            } System.out.println(""); // Spacing
    }

    public void parseCmd(String move) {

        /* This method will check which direction the user wants to move and then check to see
        * if that space is open or a wall. If it is open, it will then adjust the location of
        * the mouse using the makeMove function. Otherwise, if the space is blocked or the 
        * input was not n/s/e/w, it will display the error message.
        */

        if (move.equals("n") && maze[mouseLocRow - 1][mouseLocCol] != 0) {
            makeMove(mouseLocRow - 1, mouseLocCol);
            mouseSt.push(move);
        }
        else if (move.equals("s") && maze[mouseLocRow + 1][mouseLocCol] != 0) {
            makeMove(mouseLocRow + 1, mouseLocCol);
            mouseSt.push(move);
        }
        else if (move.equals("e") && maze[mouseLocRow][mouseLocCol + 1] != 0) {
            makeMove(mouseLocRow, mouseLocCol + 1);
            mouseSt.push(move);
        }
        else if (move.equals("w") && maze[mouseLocRow][mouseLocCol - 1] != 0) {
            makeMove(mouseLocRow, mouseLocCol - 1);
            mouseSt.push(move);
        }
        else if (move.equals("u") && !mouseSt.isEmpty()) {
            undoMove();
        }
        else if (move.equals("u") && mouseSt.isEmpty()) {
        	System.out.println("");
        	System.out.println("No more undos left!");
        }
        else {
            System.out.println("");
            System.out.println("You cannot move there!\n");
        }


    }
    
    public void makeMove(int rowMove, int colMove) {
        
    	// This function will change the variables of the mouse's location and player score
        
    	mouseLocRow = rowMove;
        mouseLocCol = colMove;
        catMove();
        playerScore -= 1;
        
    }

    public void undoMove() {
    	
    	/*
         * This method will alter the location of both mouse and cat. It will take the mousePop or catPop variable
         * and interpret it as a direction then perform the move of that cat or mouse by doing the OPPOSITE.
         * So, if the mouse had moved north then that would be a mouseLocRow - 1. It undoes this by
         * incrementing mouseLocrow by one. It will do this for all directions.
         * In the catSt stack, there are also x's. This is added to catSt when the cat attempts to move but it blocked by the
         * wall and thus stays in place. The undoMove() method will do nothing in this case, except remove that x. 
         */
    	
    	playerScore += 1;

    	
        String mousePop = mouseSt.pop();
        
    	if (mousePop.equals("n")) {
        	mouseLocRow += 1;
        }
        else if (mousePop.equals("w")) {
        	mouseLocCol += 1;
        }
        else if (mousePop.equals("s")) {
        	mouseLocRow -= 1;
        }
        else if (mousePop.equals("e")) {
        	mouseLocCol -= 1;
        }
    	
    	String catPop = catSt.pop();
    	
    	if (catPop.equals("n")) {
        	catLocRow += 1;
        }
        else if (catPop.equals("w")) {
        	catLocCol += 1;
        }
        else if (catPop.equals("s")) {
        	catLocRow -= 1;
        }
        else if (catPop.equals("e")) {
        	catLocCol -= 1;
        }
    	
    }

    public void catMove() {
        // This method moves the cat through random attempts and pushes that attempt to the stack.

       int catChoice = rand.nextInt(4); // Generating a random number to use for decision making

       if (catChoice == 0 && maze[catLocRow + 1][catLocCol] != 0) {
            catLocRow++;
            catSt.push("s");
        }
        else if (catChoice == 1 && maze[catLocRow - 1][catLocCol] != 0) {
            catLocRow--;
            catSt.push("n");
        }
        else if (catChoice == 2 && maze[catLocRow][catLocCol + 1] != 0) {
            catLocCol++;
            catSt.push("e");
        }
        else if (catChoice == 3 && maze[catLocRow][catLocCol - 1] != 0) {
            catLocCol--;
            catSt.push("w");
        } else {
        	catSt.push("x");
        }
    }

    
    public boolean catWon() {
        // This method checks to see if the cat and mouse are in the same spot.

        if (mouseLocRow == catLocRow && mouseLocCol == catLocCol) {
            return true;
        }
        return false;
    }

    
    public boolean gameWon() {
        /*
        * Checks if the mouse location and cheese location are the same and return TRUE if they
        * are. If not, passes through and returns FALSE. When this returns "false," the cheese
        * and mouse are in separate locations and the loop in the MouseRace2() constructor
        * continues
        */ 

        if (mouseLocRow == cheeseLocRow && mouseLocCol == cheeseLocRow) {
            playerScore += 100;
            return true;
        } else if (catWon()) {
            playerScore -= 100;
            return true;
        }
        return false;

    }

    public void winnerMessage() {
        
        // Prints the winning map, winning message, and score.

        System.out.println(""); // Spacing

        for (int i = 0; i < maze.length; i++) {
                for (int j=0; j < maze.length; j++) {        		          	  
                    if (i == mouseLocRow && j == mouseLocCol) {
                        System.out.print("%");
                    }
                    else if (i == catLocRow && j == catLocCol) {
                        System.out.print("C");
                    }
                    else if (maze[i][j] == 0) {
                        System.out.print("#");
                        }
                    else if (maze[i][j] == 1) {
                        System.out.print(" ");
                        }
                    } System.out.println("");
            }

            System.out.println("GAME OVER! MOUSE GOT THE CHEESE!");
            System.out.println("Your score was " + playerScore);

    }

    public void loserMessage() {

        // Prints the losing map, losing message, and score.
     
        System.out.println(""); // Spacing

        for (int i = 0; i < maze.length; i++) {
                for (int j=0; j < maze.length; j++) {        		          	  
                    if (i == mouseLocRow && j == mouseLocCol) {
                        System.out.print("C");
                    }
                    else if (maze[i][j] == 0) {
                        System.out.print("#");
                        }
                    else if (maze[i][j] == 1) {
                        System.out.print(" ");
                        }
                    } System.out.println("");
            }

            System.out.println("GAME OVER! CAT GOT THE MOUSE!");
            System.out.println("Your score was " + playerScore);

    }

    
    public static void main(String[] args) throws IOException {

        // Displays header information and creates a new instance of MouseRace2()
                
        System.out.println("CPSC 21000");
        System.out.println("NAME: CAMERON KEEPERMAN");
        System.out.println("NAME: ETHAN FOX");
        System.out.println("NAME: FIDEL HERRERA");
        System.out.println("PROGRAMMING ASSIGNMENT 6\n");

        MouseRace2 mouse = new MouseRace2();

      
  
  } // main close

} // MouseRace2 class close




class TopScore {

    int[] list;

    TopScore() throws FileNotFoundException, IOException {
        
        int[] list = readFromTopScoresFile(); // Creating an array variable from the topscores.txt file

        this.list = list;
    
    } // TopScore() closer
    

    public void printScores() throws FileNotFoundException, IOException {
        // This prints out scores to console

        list = readFromTopScoresFile();

    	for (int i=0; i < list.length-1; i++) {
    		System.out.println(i + ". " + list[i]);
    	}
    } // printScores() closer

    
    public static int[] readFromTopScoresFile() throws FileNotFoundException, IOException {
        
        // Parses through topscores.txt and creates an array of scores
        
        // Checks to see if file exists; if it doesn't, it creates it.
        createTopScoresFile();

        Scanner fileScan = new Scanner(new File("topscores.txt"));
        
        String line = "";

        // while topscores.txt has another token, adds that token to "line" variable
        while (fileScan.hasNext()) {
            line += fileScan.nextLine();
            }
        
        // This will split the line variable into strList
        String[] strList = line.split(",");
        
        // A new int array to store integer values of strList
        int[] list = new int[strList.length];

        // This populates a new array, "list," with integers from the string array "strList"
        for (int i = 0; i<list.length; i++) {
            list[i] = Integer.parseInt(strList[i]);
        }
    
       fileScan.close();

       return list;
        
    } // readFromTopScoresFile() closer

    
    public static void createTopScoresFile() throws IOException {
        
        // Creating file and writing initial blank score data to it. Should only run once upon initial run of MouseRace.java, then file.createNewFile() will return FALSE and not execute the following code.

        try {
            
            File file = new File("topscores.txt");
            
            if (file.createNewFile()) {
                FileWriter writer = new FileWriter(file);
                
                writer.write("-10000,\n-10000,\n-10000,\n-10000,\n-10000,\n-10000,\n-10000,\n-10000,\n-10000,\n-10000,\n-10000,\n");
                
                writer.close();
            }

        } catch (IOException e) {
            
            System.out.println("IOException");
        
        }

    } // createTopScoresFile() closer

    
    public void writeNewScoreToFile(int playerScore) throws IOException {

        /*
        * This function takes "playerScore" and then adds and sorts that value into the
        * top 10 list and writes a new sorted top 11 list to the topscores.txt file.
        * This way, we have 11 top scores in order to allow for sorting and makes it easier
        * to place the playerScore variable inside of a top 10 list.
        *
        * This is done through the followings steps:

        *   1) Creates sortingList which holds 11 integers.
        *   2) Populates sortingList with all of the values of list (a variable which holds all the top scores)
        *   3) Makes the 10th index (or 11th element) the playerScore variable, which is the most recent addition
        *   4) Calls inserstion sort to make it in REVERSE order, so the biggest is at the beginning and the smallest is at the bottom
        *   5) Makes a file object for writing
        *   6) Writes those 11 scores to topscores.txt and closes the file.
        */

        // 1. Size 11 array
        int[] sortingList = new int[11];
        
        // 2. Populates the new array with the top 10 scores and with the players score.
        for (int i=0; i<this.list.length; i++) {
            sortingList[i] = this.list[i];
            }
            
        // 3. Adding playerScore
        sortingList[10] = playerScore;
        
        // 4. Calls insertion sort on sortingList and returns it
        sortingList = insertionSort(sortingList);
        
        // 5. Making object to use to write
        File file = new File("topscores.txt");
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        
        // 6. Writing new sorted scores to file
        for (int i = 0; i < sortingList.length; i++) {
        	writer.write(sortingList[i] + ",\n");
        }

        writer.close();
        
        
    } // writeNewScoreToFile() closer
    

    public int[] insertionSort(int[] sortingList) {    	
        
        // This function performs an insertion sort on the list and returns it out sorted, but does it in REVERSE ORDER so to make a "top 10" where the 0th index is the biggest and the last index is the smallest.
        
        int n = sortingList.length; 
        for (int i = 1; i < n; ++i) { 
            int key = sortingList[i]; 
            int j = i - 1; 
            
            while (j >= 0 && sortingList[j] < key) { 
                sortingList[j + 1] = sortingList[j]; 
                j = j - 1; 
            
            } 
            sortingList[j + 1] = key; 
        }


    	return sortingList;

    } // inserstionSort() closer


} // TopScore closer
