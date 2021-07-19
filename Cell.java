package Piziali.cs146.project3;

import java.util.LinkedList;

/* THIS PROGRAM CONSTRUCTS A SIMPLE RANDOM MAZE AND SOLVES IT USING BFS AND DFS TO DETERMINE THE ORDER ROOMS WERE VISITED AND THE SHORTEST PATH */

// This class is used to populate the maze/2d array

public class Cell
{

    // Initializations
    private boolean north = false; // Used to tell if the cell has a north neighbor
    private boolean south = false; // Used to tell if the cell has a south neighbor
    private boolean east = false; // Used to tell if the cell has an east neighbor
    private boolean west = false; // Used to tell if the cell has a west neighbor
    public int x; // Stores the cell's row value in the grid
    public int y; // Stores the cells' column value in the grid
    public boolean wallsIntact = true; // Tells if the cell's walls have been altered or not
    public LinkedList<Cell> neighbors; // Used to store the initial neighbors before the maze has been created
    public LinkedList<Cell> mazeNeighbors; // This list keeps track of the neighbors after the maze has been built
    private String color; // The color of the cell is used in BFS to tell if it has been discovered or not
    private int distance; // The distance from the source cell
    private Cell parent; // The cell's parent cell is used in BFS to determine shortest path
    boolean visited = false; // Used to determine whether the cell has been visited or not
    boolean shortestPath = false; // Use to determine whether the cell is on the shortest path or not

    // Each cell is a room aka a vertex in the maze
    // The removed walls are edges between vertices
    // A grid is bunch of cells connected to one another

    // Constructor
    public Cell()
    {
        neighbors = new LinkedList<>();
        mazeNeighbors = new LinkedList<>();
    }

    // To add an edge, set the direction of the neighbor
    public void addEdge(String direction)
    {
        wallsIntact = false;
        if(direction.equalsIgnoreCase("north"))
            north = true;
        else if(direction.equalsIgnoreCase("south"))
            south = true;
        else if(direction.equalsIgnoreCase("east"))
            east = true;
        else if(direction.equalsIgnoreCase("west"))
            west = true;
        else {
            System.out.println("Invalid direction");
            wallsIntact = true;
        }
    }

    // This method is used for printing the maze by checking if there is a neighbor and printing the corresponding representation
    // Prints the top row of each cell
    public String topRow()
    {
        // We look at each cell's top row only, since the top row of the next cell will complete the bottom row of the previous cell
        String topRow;
        if(north)
            topRow = "+   ";
        else
            topRow = "+ - ";
        return topRow;
    }

    // This method is used for printing the maze by checking if there is a neighbor and printing the corresponding representation
    // Prints the bottom row of each cell
    public String bottomRow()
    {
        String bottomRow;
        if(south)
            bottomRow = "+   ";
        else
            bottomRow = "+ - ";
        return bottomRow;
    }

    // This method is used for printing the maze by checking if there is a neighbor and printing the corresponding representation
    // Prints the left side of each cell
    public String column()
    {
        // We look at each cell's left side only, since the next cell will have a left side that will complete the previous cell's right side
        String wall;
        if(west) // If there is an edge, then don't print a wall
            wall = "    ";
        else // If there is not an edge, then do print a wall
            wall = "|   ";
        return wall;
    }

    // This method is used for printing the maze by checking if there is a neighbor and printing the corresponding representation
    // Prints the distance inside the cell
    public String distanceColumn()
    {
        // We look at each cell's left side only, since the next cell will have a left side that will complete the previous cell's right side
        String wall ="";
        String distance = Integer.toString(getDistance());

        // Prints spaces in the unvisited cells only
        if(distance.equals("0") && !visited)
            distance = " ";


        if (west) // If there is an edge, then don't print a wall
            wall = "  " + distance + " ";
        else // If there is not an edge, then do print a wall
            wall = "| " + distance + " ";
        return wall;

    }

    // This method is used for printing the maze by checking if there is a neighbor and printing the corresponding representation
    // Prints the distance as 0 instead of a space
    public String zeroDistanceColumn()
    {
        String wall;

        if(west) // If there is an edge, then don't print a wall
            wall = "  0 ";
        else // If there is not an edge, then do print a wall
            wall = "| 0 ";
        return wall;
    }

    // This method is used for printing the maze by checking if there is a neighbor and printing the corresponding representation
    // Prints a pound sign instead of a number to be used in shortest path
    public String distanceColumn2()
    {
        // We look at each cell's left side only, since the next cell will have a left side that will complete the previous cell's right side
        String wall ="";
        int d = getDistance();
        String distance = "#";

        if(d == 0 && !visited)
            distance = " ";
        else if (!shortestPath)
            distance = " ";


        if (west) // If there is an edge, then don't print a wall
            wall = "  " + distance + " ";
        else // If there is not an edge, then do print a wall
            wall = "| " + distance + " ";
        return wall;
    }

    // This method is used for printing the maze by checking if there is a neighbor and printing the corresponding representation
    // Prints a pound sign
    public String zeroDistanceColumn2()
    {
        String wall;

        if(west) // If there is an edge, then don't print a wall
            wall = "  # ";
        else // If there is not an edge, then do print a wall
            wall = "| # ";
        return wall;
    }

    // Sets the color of the cell
    public void setColor(String newColor)
    {
        if(newColor.equalsIgnoreCase("white") || newColor.equalsIgnoreCase("grey") || newColor.equalsIgnoreCase("black"))
            color = newColor;
        else
            System.out.println("Invalid color");
    }

    // Returns the color of a cell
    public String getColor()
    {
        return color;
    }

    // Sets the distance of a cell
    public void setDistance(int d)
    {
        distance = d;
    }

    // Returns the distance of a cell
    public int getDistance()
    {
        return distance;
    }

    // Sets the parent of a cell
    public void setParent(Cell p)
    {
        parent = p;
    }

    // Returns the parent of a cell
    public Cell getParent()
    {
        return parent;
    }

}
