/*
 * Project 4
 * By Janista Gitbumrungsin
 * CS3010.02 Fall 2022
 * Dr. Lajpat Rai Raheja
 */
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

class Interpolate
{
    private static double[] xValues;
    private static double[] fxValues;

    public static void main(String[] args) throws IOException
    {
        Scanner scan = new Scanner(System.in);

        System.out.println("POLYNOMIAL INTERPOLATION");
        System.out.println("--------------------------------------------------");
        System.out.println("\nPlease enter the name of the data file: ");
        
        String filename = scan.nextLine();
        File myFile = new File(filename);
        Scanner inputFile = new Scanner(myFile);
        
        String[] xSplit = inputFile.nextLine().split(" ");
        String[] fxSplit = inputFile.nextLine().split(" ");
        xValues = new double[xSplit.length];
        fxValues = new double[fxSplit.length];
        
        for(int i = 0; i < xSplit.length; i++)
        {
            xValues[i] = Double.parseDouble(xSplit[i]);
            fxValues[i] = Double.parseDouble(fxSplit[i]);
        }
    }
}