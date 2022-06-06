import java.io.*;
import java.util.*;
public class maze
{
    public static void main (String [] args)
    {
		int size = 15;
		
        System.out.println("The following is a maze with a grid of " + size + " boxes by " + size + " boxes. ");
		System.out.println("The start is the top-left box and the end is the bottom-right box.");
        System.out.println("All lines interior to the border can be knocked out.");
		System.out.println("\nBefore: ");
        
        Nodes maze = new Nodes (size);
        maze.print();
		
		System.out.println("\nAfter: ");
		
        while (!maze.connected(0, size*size-1)) // The start box has value 0. The end box has value size*size-1. Until they are connected
        {
            int node1 = (int)(Math.random() * size * size); // Pick a random box
            int direction;	// Join it with either the box above, below, to the right, or to the left of it.
            
            if ((node1 / size) == 0) // Top row
            {
                if ((node1 % size) == 0) // Left column
                {
                    direction = (int)(Math.random() * 2); // Remove either bottom or right wall
                    if (direction == 0)
                    {
                        direction = 2;
                    }
                    else
                    {
                        direction = 3;
                    }
                }
                else if ((node1 % size) == size-1) // Right Column
                {
                    direction = (int)(Math.random() * 2);
                    if (direction == 0) // Remove either left or bottom wall
                    {
                        direction = 1;
                    }
                    else
                    {
                        direction = 2;
                    }
                }
                else // Just the top row - Remove either left, bottom, or right wall
                {
                    direction = 1 + (int)(Math.random() * 3);
                }
            }

            else if ((node1 / size) == size - 1) // Bottom row
            {
                if ((node1 % size) == 0) // Left column
                {
                    direction = (int)(Math.random() * 2); // Remove either top or right wall
                    if (direction == 0)
                    {
                        direction = 0;
                    }
                    else
                    {
                        direction = 3;
                    }
                }
                else if ((node1 % size) == size-1) // Right column
                {
                    direction = (int)(Math.random() * 2); // Remove either top or left wall
                    if (direction == 0)
                    {
                        direction = 0;
                    }
                    else
                    {
                        direction = 1;
                    }
                }
                else
                {
                    direction = (int)(Math.random() * 3); // Just the bottom row - Remove either top, left, or right wall
                    if (direction == 2)
                    {
                        direction = 3;
                    }
                }
            }

            else if ((node1 % size) == 0) // Just the left column
            {
                direction = (int)(Math.random() * 3); // Remove either the top, bottom, or right wall
                if (direction == 1)
                {
                    direction = 3;
                }
            }

            else if ((node1 % size) == size-1) // Just the right column
            {
                direction = (int)(Math.random() * 3); // Remove either the top, left, or bottom wall
            }

            else // In the middle
            {
                direction = (int)(Math.random() * 4); // Remove any wall
            }
            
            int node2; // Find the value of the box being joined to the first one
            if (direction == 0) 
            {
                node2 = node1 - size;
            }
            else if (direction == 1)
            {
                node2 = node1 - 1;
            }
            else if (direction == 2)
            {
                node2 = node1 + size;
            }
            else
            {
                node2 = node1 + 1;
            }

            if (!maze.connected(node1, node2)) // If they aren't already connected, join them
            {
                maze.union(node1, node2, direction);
            }
        }
		
		maze.print();
        System.out.println("The maze is now solvable!");
    }
}

class Nodes
{
    private int size;
    private int [][] values;
    private Drawing visualRepresentation;

    public Nodes (int size) // Create a 2D array with dimensions size by size
    {
        this.size = size;
        values = new int [this.size][this.size];
        for (int i = 0; i < this.size; i++)
        {
            for (int j = 0; j < this.size; j++)
            {
                values[i][j] = i*this.size + j;
            }
        }
        visualRepresentation = new Drawing (this.size);
    }

    public boolean connected (int node1, int node2) // If the value of two nodes are equal, they are connected
    {
        return values[node1 / size][node1 % size] == values[node2 / size][node2 % size];
    }

    public void union (int node1, int node2, int direction) // Set the value of all nodes with one value to the value of other node
    {
        int value1, value2;
        value1 = values[node1 / size][node1 % size];
        value2 = values[node2 / size][node2 % size];
        for (int i = 0; i < this.size; i++)
        {
            for (int j = 0; j < this.size; j++)
            {
                if (values[i][j] == value2)
                {
                    values[i][j] = value1; 
                }
            }
        }
        visualRepresentation.union(node1, direction);
    }

    public void print ()
    {
        visualRepresentation.print();
    }
}

class Drawing // Drawing displayed to screen
{
    private int size;
    private String [][] maze;

    public Drawing (int size) // Draw the initial grid given the dimensions
    {
        this.size = size + 1;
        maze = new String[this.size][this.size];
        for (int i = 0; i < this.size; i++)
        {
            for (int j = 0; j < this.size; j++)
            {
                maze[i][j] = "";
            }
        }

        for (int i = 1; i < this.size; i++)
        {
            for (int j = 0; j < this.size - 1; j++)
            {
                maze[i][j] = "|_";
            }
        }

        for (int j = 1; j < this.size; j++)
        {
            maze[0][j] = " _";
        }

        for (int i = 1; i < this.size; i++)
        {
            maze[i][this.size-1] = "|";
        }
    }

    public void union (int node1, int direction) // Given a box and the wall to remove, remove the wall.
    {
        int xposition = 1 + (node1/(size - 1));
        int yposition = (node1 % (size - 1));

        if (direction == 0) // If needed to remove the wall above, change |_ from the cell above to just |
        {
            maze[xposition - 1][yposition] = maze[xposition - 1][yposition].substring(0, 1) + " ";
        }
        else if (direction == 1) // If needed to remove the wall to the right, change |_ from the current cell to just  _
        {
            maze[xposition][yposition] = " " + maze[xposition][yposition].substring(1, 2);
        }
        else if (direction == 2) // If needed to remove the wall below, change |_ from the current cell above to just |
        {
            maze[xposition][yposition] = maze[xposition][yposition].substring(0, 1) + " ";
        }
        else // If needed to remove the wall to the left, change |_ from the cell to the right to just  _
        {
            maze[xposition][yposition + 1] = " " + maze[xposition][yposition + 1].substring(1, 2);
        }
    }

    public void print () // Print
    {
        for (int i = 0; i < this.size; i++)
        {
            for (int j = 0; j < this.size; j++)
            {
                System.out.print(maze[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
}