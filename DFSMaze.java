package Piziali.cs146.project3;

import java.io.*;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

/* THIS PROGRAM CONSTRUCTS A SIMPLE RANDOM MAZE AND SOLVES IT USING BFS AND DFS TO DETERMINE THE ORDER ROOMS WERE VISITED AND THE SHORTEST PATH */
// THIS CLASS PERFORMS A DEPTH FIRST SEARCH ON THE MAZE TO SOLVE IT AND FIND THE SHORTEST PATH

public class DFSMaze
{
    // Initializations
    private MazeConstructor maze; // The MazeConstructor object passed to DFSMaze to perform the search upon
    private int r; // The size of the maze
    private int time = 0; // Used to track the distance from the source cell
    private int visitedCells; // Tracks how many cells were visited during the search
    private int totalCells; // Tracks the total amount of cells in the maze
    Stack<Cell> stack; // The stack used as a recursive stack to store the cells on to check if they have more neighbors to check
    LinkedList<Cell> path; // Used to store the cells on the shortest path

    // Constructor
    public DFSMaze(MazeConstructor maze)
    {
        this.maze = maze;
        this.r = maze.getR();
        path = new LinkedList<>();
    }

    // This calls the methods necessary to run the DFS search
    public void run()
    {
        DFS();
        printDFS();
        printShortestPath();
        printPath();
    }

    // This method performs a depth-first search on the maze
    public void DFS()
    {
        Random rand = new Random(); // Used to randomly choose neighbor
        rand.setSeed(100);
        stack = new Stack<>(); // Recursive stack
        totalCells = r*r; // Total cells in the maze
        visitedCells = 0; // How many cells have been visited

        Cell currentCell = maze.getGrid()[0][0]; // Set current cell to first cell to start the search

        while(visitedCells <= totalCells)
        {
            // If the current cell has at least one neighbor, explore the neighbor's neighbors
            if(currentCell.mazeNeighbors.size() > 0)
            {
                // Randomly choose one of the neighbors
                int random = rand.nextInt(currentCell.mazeNeighbors.size()); // Randomly choose a neighbor between 0 and size of neighbors list
                Cell neighbor = currentCell.mazeNeighbors.get(random);

                // Add current cell to shortest path
                currentCell.shortestPath = true;
                if(!path.contains(currentCell))
                    path.add(currentCell);

                // Remove that neighbor from currentCell's list so the size decrements
                currentCell.mazeNeighbors.remove(neighbor);

                // Resets the time to 0 so the distance stays between 0-9
                if(time == 10)
                    time = 0;

                // Set the distance from the source node
                if(!currentCell.visited) {
                    currentCell.setDistance(time);
                    time = time + 1;
                }

                // Increment visitedCells
                visitedCells++;
                currentCell.visited = true;

                // Push the current cell onto the stack
                stack.push(currentCell);

                // If reached the end of the maze, stop searching
                if(currentCell.x == r-1 && currentCell.y == r-1) {
                    break;
                }

                // Set currentCell to neighbor
                currentCell = neighbor;
            }

            // If the currentCell has no more neighbors to explore, begin recursively popping the stack and checking that each cell has no more neighbors to check
            else {
                currentCell.shortestPath = false;

                if(time == 10)
                    time = 0;

                if(!currentCell.visited) {
                    currentCell.setDistance(time);
                    time = time + 1;
                    currentCell.visited = true;
                }

                // If reached the end of the maze, stop searching
                if(currentCell.x == r-1 && currentCell.y == r-1) {
                    currentCell.shortestPath = true;
                    path.add(currentCell);
                    visitedCells++;
                    break;
                }

                if(stack.size() == 0)
                    break;

                currentCell = stack.pop();
            }
        }
    }

    // This method prints the maze's DFS search in order of rooms visited
    public void printDFS()
    {
        Cell[][] grid = maze.getGrid();

        for(int i=0; i<=r; i++)
        {
            // Rows: This loop needs to iterate r times since there are r rows
            for(int j=0; j<r; j++)
            {
                Cell currentCell;
                if(i==r) { // If it is on the last row, print the bottom row of the cells
                    currentCell = grid[i - 1][j];
                    System.out.print(currentCell.bottomRow());
                }
                else { // If it's not on the last row, print the top row of the cells
                    currentCell = grid[i][j];
                    System.out.print(currentCell.topRow());
                }
            }
            System.out.print("+");
            System.out.println();

            // This loop only needs to iterate r-1 times since there is only r-1 "|" rows
            if(i!=r) {
                for (int k = 0; k < r; k++) {
                    Cell currentCell = grid[i][k];
                    if((i==0 && k==0))
                        System.out.print(currentCell.zeroDistanceColumn());
                    else
                        System.out.print(currentCell.distanceColumn());
                }
                System.out.print("|");
                System.out.println();
            }
        }
    }

    // This method prints the shortest path of the maze
    public void printShortestPath()
    {
        Cell[][] grid = maze.getGrid();

        for(int i=0; i<=r; i++)
        {
            // Rows: This loop needs to iterate r times since there are r rows
            for(int j=0; j<r; j++)
            {
                Cell currentCell;
                if(i==r) { // If it is on the last row, print the bottom row of the cells
                    currentCell = grid[i - 1][j];
                    System.out.print(currentCell.bottomRow());
                }
                else { // If it's not on the last row, print the top row of the cells
                    currentCell = grid[i][j];
                    System.out.print(currentCell.topRow());
                }
            }
            System.out.print("+");
            System.out.println();

            // This loop only needs to iterate r-1 times since there is only r-1 "|" rows
            if(i!=r) {
                for (int k = 0; k < r; k++) {
                    Cell currentCell = grid[i][k];
                    if((i==0 && k==0))
                        System.out.print(currentCell.zeroDistanceColumn2());
                    else
                        System.out.print(currentCell.distanceColumn2());
                }
                System.out.print("|");
                System.out.println();
            }
        }
    }

    // This method prints the path taken by DFS and total number of visited cells
    public void printPath()
    {
        int count = 0;
        System.out.print("Path: ");
        for(int i=0; i<path.size(); i++)
        {
            Cell currentCell = path.get(i);
            if(currentCell.shortestPath) {
                System.out.print(" (" + currentCell.x + "," + currentCell.y + ") ");
                count++;
            }
        }
        System.out.println("\nVisited cells: "+visitedCells);
        System.out.println("Length of Path: " + count);

    }

    // This method saves the maze to a file
    public void saveMaze(File file) throws IOException
    {
        int count = 0;
        FileWriter fwriter = new FileWriter(file, true);
        PrintWriter outputFile = new PrintWriter(fwriter);
        Cell[][] grid = maze.getGrid();

        outputFile.write("DFS\n");

        // Saves the path taken (original non shortest route)
        for(int i=0; i<=r; i++)
        {
            // Rows: This loop needs to iterate r times since there are r rows
            for(int j=0; j<r; j++)
            {
                Cell currentCell;
                if(i==r) { // If it is on the last row, print the bottom row of the cells
                    currentCell = grid[i - 1][j];
                    outputFile.write(currentCell.bottomRow());
                }
                else { // If it's not on the last row, print the top row of the cells
                    currentCell = grid[i][j];
                    outputFile.write(currentCell.topRow());
                }
            }
            outputFile.write("+");
            outputFile.write("\n");

            // This loop only needs to iterate r-1 times since there is only r-1 "|" rows
            if(i!=r) {
                for (int k = 0; k < r; k++) {
                    Cell currentCell = grid[i][k];
                    if((i==0 && k==0))
                        outputFile.write(currentCell.zeroDistanceColumn());
                    else
                        outputFile.write(currentCell.distanceColumn());
                }
                outputFile.write("|");
                outputFile.write("\n");
            }
        }
        // Saves the shortest path taken.
        for(int i=0; i<=r; i++)
        {
            // Rows: This loop needs to iterate r times since there are r rows
            for(int j=0; j<r; j++)
            {
                Cell currentCell;
                if(i==r) { // If it is on the last row, print the bottom row of the cells
                    currentCell = grid[i - 1][j];
                    outputFile.write(currentCell.bottomRow());
                }
                else { // If it's not on the last row, print the top row of the cells
                    currentCell = grid[i][j];
                    outputFile.write(currentCell.topRow());
                }
            }
            outputFile.write("+");
            outputFile.write("\n");

            // This loop only needs to iterate r-1 times since there is only r-1 "|" rows
            if(i!=r) {
                for (int k = 0; k < r; k++) {
                    Cell currentCell = grid[i][k];
                    if((i==0 && k==0))
                        outputFile.write(currentCell.zeroDistanceColumn2());
                    else
                        outputFile.write(currentCell.distanceColumn2());
                }
                outputFile.write("|");
                outputFile.write("\n");
            }
        }

        // Saves the path, length, visited cells.
        outputFile.write("Path:");
        for (Cell currentCell: path)
        {
            if(currentCell.shortestPath) {
                outputFile.write("(" + currentCell.x + "," + currentCell.y + ") ");
                count ++;
            }
        }
        outputFile.write("\n");
        outputFile.write("Visited Cells: " + visitedCells + "\n");
        outputFile.write("Length of Path: " + count);
        outputFile.close();
    }
}
