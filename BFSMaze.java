package Piziali.cs146.project3;

import java.io.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

/* THIS PROGRAM CONSTRUCTS A SIMPLE RANDOM MAZE AND SOLVES IT USING BFS AND DFS TO DETERMINE THE ORDER ROOMS WERE VISITED AND THE SHORTEST PATH */
// THIS CLASS PERFORMS A BREADTH FIRST SEARCH ON THE MAZE TO SOLVE IT AND FIND THE SHORTEST PATH

public class BFSMaze
{
    // Initializations
    private MazeConstructor maze; // The MazeConstructor object passed to DFSMaze to perform the search upon
    private int r; // The size of the maze
    private int visitedCells; // The total number of cells visited during BFS
    private LinkedList<Cell> parents; // Used to store the parent of each cell on the shortest path
    Queue<Cell> queue; // Adds neighbors to explore to this queue during the search

    // Constructor
    public BFSMaze(MazeConstructor maze) {
        this.maze = maze;
        this.r = maze.getR();
        queue = new LinkedList<>();
        parents = new LinkedList<>();
    }

    // This calls the methods necessary to run the BFS search
    public void run() {
        BFS();
        printBFS();
        shortestPathList();
        printShortestPath();
        printPath();
    }

    // This method performs a breadth-first search on the maze
    public void BFS() {
        visitedCells = 1;

        // Initialize the vertices
        for (int j = 0; j < maze.getGrid().length; j++) {
            for (int k = 0; k < maze.getGrid().length; k++) {
                Cell cell = maze.getGrid()[j][k];
                cell.setColor("WHITE");
                cell.setDistance(0);
                cell.setParent(null);
            }
        }

        Cell sourceCell = maze.getGrid()[0][0];
        Cell neighbor = null;
        Cell currentCell;
        int distance = 0;

        // Initialize the source node
        sourceCell.setColor("GREY");
        sourceCell.setDistance(0);
        sourceCell.setParent(null);

        queue.add(sourceCell);

        // Visits each cell until the queue is empty
        while (!queue.isEmpty()) {
            currentCell = queue.remove();

            for (int i = 0; i < currentCell.mazeNeighbors.size(); i++) {
                neighbor = currentCell.mazeNeighbors.get(i);

                // Stop the search if you reach the final cell
                if (neighbor.x == r - 1 && neighbor.y == r - 1) {
                    distance++;
                    if (distance == 10)
                        distance = 0;
                    neighbor.setDistance(distance);
                    neighbor.setParent(currentCell);
                    neighbor.visited = true;
                    visitedCells++;
                    break;
                }

                // Checks each neighbor and adds it to the queue if its color is white
                if (neighbor.getColor().equalsIgnoreCase("WHITE")) {
                    distance++;
                    if (distance == 10)
                        distance = 0;
                    neighbor.setColor("GREY");
                    neighbor.setDistance(distance);
                    neighbor.setParent(currentCell);
                    neighbor.visited = true;
                    queue.add(neighbor);
                    visitedCells++;
                }
            }
            // Stop the search if you reach the final cell
            if (neighbor.x == r - 1 && neighbor.y == r - 1)
                break;

            currentCell.setColor("BLACK");
        }
    }

    // Populates a list with the cells that are on the shortest path
    public void shortestPathList() {
        Cell cell = maze.getGrid()[r - 1][r - 1];

        parents.add(cell);

        while (cell.getParent() != null) {
            parents.add(cell.getParent());
            cell = cell.getParent();
        }

        Collections.reverse(parents);
    }

    // Prints the path taken by BFS
    public void printPath() {
        System.out.print("Path: ");
        for (int i = 0; i < parents.size(); i++) {
            Cell cell = parents.get(i);
            System.out.print(" (" + cell.x + "," + cell.y + ") ");
        }
        System.out.println("\nVisited cells: " + visitedCells);
        System.out.println("Length of Path: " + parents.size());
    }

    // Prints the order of the rooms visited by the BFS search
    public void printBFS() {
        Cell[][] grid = maze.getGrid();

        for (int i = 0; i <= r; i++) {
            // Rows: This loop needs to iterate r times since there are r rows
            for (int j = 0; j < r; j++) {
                Cell currentCell;
                if (i == r) { // If it is on the last row, print the bottom row of the cells
                    currentCell = grid[i - 1][j];
                    System.out.print(currentCell.bottomRow());
                } else { // If it's not on the last row, print the top row of the cells
                    currentCell = grid[i][j];
                    System.out.print(currentCell.topRow());
                }
            }
            System.out.print("+");
            System.out.println();

            // This loop only needs to iterate r-1 times since there is only r-1 "|" rows
            if (i != r) {
                for (int k = 0; k < r; k++) {
                    Cell currentCell = grid[i][k];
                    if ((i == 0 && k == 0))
                        System.out.print(currentCell.zeroDistanceColumn());
                    else
                        System.out.print(currentCell.distanceColumn());
                }
                System.out.print("|");
                System.out.println();
            }
        }
    }

    // Prints the shortest path taken by the search
    public void printShortestPath() {
        Cell[][] grid = maze.getGrid();

        for (int i = 0; i <= r; i++) {
            // Rows: This loop needs to iterate r times since there are r rows
            for (int j = 0; j < r; j++) {
                Cell currentCell;
                if (i == r) { // If it is on the last row, print the bottom row of the cells
                    currentCell = grid[i - 1][j];
                    System.out.print(currentCell.bottomRow());
                } else { // If it's not on the last row, print the top row of the cells
                    currentCell = grid[i][j];
                    System.out.print(currentCell.topRow());
                }
            }
            System.out.print("+");
            System.out.println();

            // This loop only needs to iterate r-1 times since there is only r-1 "|" rows
            if (i != r) {
                for (int k = 0; k < r; k++) {
                    Cell currentCell = grid[i][k];
                    if ((i == 0 && k == 0))
                        System.out.print(currentCell.zeroDistanceColumn2());
                    else if (parents.contains(currentCell))
                        System.out.print(currentCell.zeroDistanceColumn2());
                    else
                        System.out.print(currentCell.distanceColumn2());
                }
                System.out.print("|");
                System.out.println();
            }
        }
    }

    // Saves the maze to a file
    public void saveMaze(File file) throws IOException {
        PrintWriter outputFile = new PrintWriter(file);

        Cell[][] grid = maze.getGrid();

        outputFile.write("BFS\n");

        // Saves the original (not shortest) BFS path.
        for (int i = 0; i <= r; i++) {
            // Rows: This loop needs to iterate r times since there are r rows
            for (int j = 0; j < r; j++) {
                Cell currentCell;
                if (i == r) { // If it is on the last row, print the bottom row of the cells
                    currentCell = grid[i - 1][j];
                    outputFile.write(currentCell.bottomRow());
                } else { // If it's not on the last row, print the top row of the cells
                    currentCell = grid[i][j];
                    outputFile.write(currentCell.topRow());
                }
            }
            outputFile.write("+");
            outputFile.write("\n");

            // This loop only needs to iterate r-1 times since there is only r-1 "|" rows
            if (i != r) {
                for (int k = 0; k < r; k++) {
                    Cell currentCell = grid[i][k];
                    if ((i == 0 && k == 0))
                        outputFile.write(currentCell.zeroDistanceColumn());
                    else
                        outputFile.write(currentCell.distanceColumn());
                }
                outputFile.write("|");
                outputFile.write("\n");
            }
        }

        // Saves the shortest BFS path.
        for (int i = 0; i <= r; i++) {
            // Rows: This loop needs to iterate r times since there are r rows
            for (int j = 0; j < r; j++) {
                Cell currentCell;
                if (i == r) { // If it is on the last row, print the bottom row of the cells
                    currentCell = grid[i - 1][j];
                    outputFile.write(currentCell.bottomRow());
                } else { // If it's not on the last row, print the top row of the cells
                    currentCell = grid[i][j];
                    outputFile.write(currentCell.topRow());
                }
            }
            outputFile.write("+");
            outputFile.write("\n");

            // This loop only needs to iterate r-1 times since there is only r-1 "|" rows
            if (i != r) {
                for (int k = 0; k < r; k++) {
                    Cell currentCell = grid[i][k];
                    if ((i == 0 && k == 0))
                        outputFile.write(currentCell.zeroDistanceColumn2());
                    else if (parents.contains(currentCell))
                        outputFile.write(currentCell.zeroDistanceColumn2());
                    else
                        outputFile.write(currentCell.distanceColumn2());
                }
                outputFile.write("|");
                outputFile.write("\n");
            }
        }
        // Saves the shortest path, length, and visited cells.
        outputFile.write("Path:");
        for (Cell currentCell : parents) {
            outputFile.write("(" + currentCell.x + "," + currentCell.y + ") ");
        }
        outputFile.write("\n");
        outputFile.write("Visited Cells: " + visitedCells + "\n");
        outputFile.write("Length of Path: " + parents.size() + "\n");
        outputFile.close();

    }
}

