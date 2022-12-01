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

        double[][] a = dividedDifference(xValues, fxValues);
        printNewton(a, xValues);
        lagrange(xValues, fxValues);
        
    }

    public static double[][] dividedDifference(double[] x, double[] fx)
    {
        int i, j;
        int n = x.length;
        double[][] a = new double[n][n];

        System.out.println();
        System.out.print("x:\t");
        for(i = 0; i < n; i++)
        {
            System.out.printf("%.2f\t", x[i]);
        }
        System.out.println();

        System.out.print("f[]:\t");
        for(i = 0; i < n; i++)
        {
            a[i][0] = fx[i];
            System.out.printf("%.2f\t", a[i][0]);
        }
        for(j = 1; j < n; j++)
        {
            System.out.println();
            System.out.print("f[");
            for(i = 0; i < j; i++)
            {
                System.out.print(",");
            }
            System.out.print("]:\t");

            for(i = 0; i < n-j; i++)
            {
                a[i][j] = (a[i+1][j-1] - a[i][j-1]) / (x[i+j] - x[i]);
                System.out.printf("%.2f\t", a[i][j]);
            }
        }

        return a;
    }

    public static void printNewton(double[][] a, double[] x)
    {
        int n = x.length;
        int counter = 1;
        System.out.println("\n\nNewton's form:");

        System.out.printf("%.2f ", a[0][0]);

        for(int i = 1; i < n; i++)
        {
            if(a[0][i] < 0)
            {
                System.out.printf("- %.2f", Math.abs(a[0][i]));   
            }
            else
            {
                System.out.printf("+ %.2f", a[0][i]);
            }

            for(int j = 0; j < counter; j++)
            {
                System.out.print("(x-");
                System.out.print(x[j]);
                System.out.print(")");
            }
            System.out.print(" ");
            counter++;
        }
    }

    public static void lagrange(double[] x, double[] fx)
    {
        double calc, xi;
        double mult;
        double[] coef = new double[x.length];
        int n = x.length;
        System.out.println("\n\nLagrange's form:");

        for(int i = 0; i < n; i++)
        {
            mult = 1;
            xi = x[i];
            calc = fx[i];

            for(int j = 0; j < n; j++)
            {
                if(j != i)
                {
                    mult *= (xi - x[j]);
                }
            }

            coef[i] = calc / mult;
        }

        for(int i = 0; i < n; i++)
        {
            System.out.printf("%.2f", Math.abs(coef[i]));

            for(int j = 0; j < n; j++)
            {
                if(j != i)
                {
                    System.out.print("(x-");
                    System.out.print(x[j]);
                    System.out.print(")");
                }
            }

            if(i < n-1)
            {
                if(coef[i+1] > 0)
                {
                    System.out.print(" + ");
                }
                else
                {
                    System.out.print(" - ");
                }
            }
        }
    }
}