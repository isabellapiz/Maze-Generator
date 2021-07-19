package Piziali.cs146.project3;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/* THIS PROGRAM CONSTRUCTS A SIMPLE RANDOM MAZE AND SOLVES IT USING BFS AND DFS TO DETERMINE THE ORDER ROOMS WERE VISITED AND THE SHORTEST PATH */
// THIS CLASS IS A UTILITY CLASS THAT CONTAINS A METHOD FOR COMPARING FILES

public class Util
{
    public static Boolean isEqual(File firstFile, File secondFile) throws IOException
    {
        Scanner scan1 = new Scanner(firstFile);
        Scanner scan2 = new Scanner(secondFile);

        while(scan1.hasNextLine() || scan2.hasNextLine())
        {
            Scanner linescan1 = new Scanner(scan1.nextLine());
            Scanner linescan2 = new Scanner(scan2.nextLine());

            while(linescan1.hasNext() && linescan2.hasNext())
            {
                String current1 = linescan1.next();
                String current2 = linescan2.next();

                if(!current1.equalsIgnoreCase(current2))
                    return false;
            }
        }
        scan1.close();
        scan2.close();
        return true;
    }
}
