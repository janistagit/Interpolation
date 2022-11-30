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

    public void dividedDifference(double[] x, double[] fx)
    {
        int i, j;
        int n = x.length;
        double[][] a = new double[n][n];

        for(i = 0; i < n; i++)
        {
            a[i][0] = fx[i];
        }
        for(j = 1; j < n; j++)
        {
            for(i = 0; i < n-j; i++)
            {
                a[i][j] = (a[i+1][j-1] - a[i][j-1]) / (x[i+j] - x[i]);
            }
        }
    }
}