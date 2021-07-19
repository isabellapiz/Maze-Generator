package Piziali.cs146.project3;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Stack;

/* THIS PROGRAM CONSTRUCTS A SIMPLE RANDOM MAZE AND SOLVES IT USING BFS AND DFS TO DETERMINE THE ORDER ROOMS WERE VISITED AND THE SHORTEST PATH */
// THIS CLASS CREATES A MAZE BY POPULATING A 2D ARRAY WITH CELLS AND KNOCKING DOWN WALLS BETWEEN THE CELLS USING DFS

public class MazeConstructor
{
    private Stack<Cell> cellStack; // This is used as a recursive stack to check that each cell has no more neighbors to visit
    private int totalCells; // Tracks the total amount of cells
    private int visitedCells; // Tracks the visited cells
    private Cell currentCell; // Stores the current cell we are visiting
    private int r; // The size of the maze
    private Cell[][] grid; // The 2D array the maze is built in

    // Constructor
    public MazeConstructor(int r) {
        cellStack = new Stack();
        totalCells = r * r;
        this.r = r;
        validateInput();
        createGrid();
        buildMaze();
    }

    // Will throw an IllegalArgumentException if a value less than 1 is passed to MazeConstructor
    public void validateInput()
    {
        if(r < 2)
            throw new IllegalArgumentException("Size must be greater than 1");
    }

     // This creates a 2D array full of cells but doesn't establish edges
    public void createGrid()
    {
        grid = new Cell[r][r];
        // Populating the array with cell objects
        for(int i=0; i< r; i++)
        {
            for(int j =0; j < r; j++)
            {
                grid[i][j] = new Cell();
                grid[i][j].x = i;
                grid[i][j].y = j;

            }
        }
        // Establishes the neighbor connections between cells
        for(int i=0; i<r; i++)
        {
            for(int j=0; j<r; j++)
            {
                // Checked to see if these objects were being created and not null, and they were but the print statements were only iterating once
                try {
                    Cell north = grid[i-1][j];
                    grid[i][j].neighbors.add(north);
                }
                catch(Exception e) {}
                try {
                    Cell south = grid[i+1][j];
                    grid[i][j].neighbors.add(south);
                }
                catch(Exception e) {}
                try {
                    Cell west = grid[i][j-1];
                    grid[i][j].neighbors.add(west);
                }
                catch(Exception e) {}
                try {
                    Cell east = grid[i][j+1];
                    grid[i][j].neighbors.add(east);
                }
                catch(Exception e) {}
            }
        }
    }

    // This method removes walls between neighbors to establish a maze in the grid
    public void buildMaze() {
        currentCell = grid[0][0];
        visitedCells = 1;
        Random rand = new Random();
        rand.setSeed(100);

        while (visitedCells <= totalCells)
        {
            // Find all neighbors of CurrentCell with walls intact
            for (int i = 0; i < currentCell.neighbors.size(); i++)
            {
                // Cleans  up neighbor list by removing any neighbor that has already been visited / does not have walls intact
                Cell currentNeighbor = currentCell.neighbors.get(i);
                if (currentNeighbor.wallsIntact == false)
                {
                    i--;
                    currentCell.neighbors.remove(currentNeighbor);
                }
            }
            // Adds edges between cells while accounting for their placement
            if (currentCell.neighbors.size() > 0) // If there is one or more neighbors in the list then proceed
            {
                int random = rand.nextInt(currentCell.neighbors.size()); // Randomly choose a neighbor between 0 and size of neighbors list
                Cell neighbor = currentCell.neighbors.get(random); // Assign that randomly chosen neighbor to neighbor

                // Removes the wall between current cell and neighbor cell by determining their location relationship and adding and edge between them
                if (neighbor.x > currentCell.x)
                {
                    neighbor.addEdge("north");
                    currentCell.addEdge("south");
                    currentCell.mazeNeighbors.add(neighbor);
                }
                else if (neighbor.x < currentCell.x)
                {
                    neighbor.addEdge("south");
                    currentCell.addEdge("north");
                    currentCell.mazeNeighbors.add(neighbor);
                }
                else if (neighbor.y > currentCell.y)
                {
                    neighbor.addEdge("west");
                    currentCell.addEdge("east");
                    currentCell.mazeNeighbors.add(neighbor);
                }
                else if (neighbor.y < currentCell.y)
                {
                    neighbor.addEdge("east");
                    currentCell.addEdge("west");
                    currentCell.mazeNeighbors.add(neighbor);
                }
                else
                {
                    System.out.println("error");
                }

                cellStack.push(currentCell); // Push the current cell to the stack to be revisited later?
                currentCell = neighbor; // Set currentCell = neighbor so we then explore the neighbor's neighbors
                visitedCells++; // Increment visited cell
            }

            // If there are no more neighbors to explore then start reversing through the stack and checking the previous cell's neighbors
            else {
                if(cellStack.size() == 0)
                    break;
                currentCell = cellStack.pop();
            }
        }
        // Add entry and exit points
        grid[0][0].addEdge("north");
        grid[r-1][r-1].addEdge("south");
    }

    // Returns the 2D array (the maze)
    public Cell[][] getGrid() {
        return grid;
    }

    // Returns the number of visited cells
    public int getVisitedCells() {
        return visitedCells;
    }

    // Prints the maze in ASCII
    public void printMaze()
    {
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
                    System.out.print(currentCell.column());
                }
                System.out.print("|");
                System.out.println();
            }
        }
    }

    // This method writes the maze to a file
    public void saveMaze() throws FileNotFoundException
    {
        PrintWriter outputFile = new PrintWriter("maze.txt");
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
                    outputFile.write(currentCell.column());
                }
                outputFile.write("|");
                outputFile.write("\n");
            }
        }
        outputFile.close();
    }

    // Returns the size of the maze
    public int getR()
    {
        return r;
    }


}
