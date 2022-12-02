/*
 * Project 4
 * By Janista Gitbumrungsin
 * CS3010.02 Fall 2022
 * Dr. Lajpat Rai Raheja
 */
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

class Interpolate
{
    private static double[] xValues;
    private static double[] fxValues;
    private static double [] coefficients;
    private static ArrayList<Double> LFactors;

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
        coefficients = lagrange(xValues, fxValues);
        simplify(coefficients, LFactors);
        
    }

    public static double[][] dividedDifference(double[] x, double[] fx)
    {
        int i, j;
        int n = x.length;
        double[][] a = new double[n][n];

        System.out.println();
        System.out.println("Divided Difference:");
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

    public static double[] lagrange(double[] x, double[] fx)
    {
        double calc, xi;
        double mult;
        int n = x.length;
        double[] coef = new double[n];
        ArrayList<Double> factors = new ArrayList<Double>((n-1)*n);
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
                    factors.add(x[j]);
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

        LFactors = factors;
        return coef;
    }

    public static void simplify(double[] coef, ArrayList<Double> factors)
    {
        Node result = null;
        Node temp = null;
        Node temp2 = null;
        Node temp3 = null;
        int n = coef.length;

        temp = addnode(temp, 1, 1);
        temp = addnode(temp, -factors.get(0), 0);

        for(int i = 1; i < factors.size(); i++)
        {
            if(i % (n-1) != 0)
            {
                temp2 = null;
                temp2 = addnode(temp2, 1, 1);
                temp2 = addnode(temp2, -factors.get(i), 0);

                temp = multiply(temp, temp2, temp3);

                if(i == factors.size() - 1)
                {
                    temp2 = null;
                    temp2 = addnode(temp2, coef[((i+1)/(n-1)) -1], 0);

                    temp = multiply(temp, temp2, temp3);

                    Node ptr = result;
                    if(result == null)
                    {
                        result = temp;
                    }
                    else
                    {
                        while(ptr.next != null)
                        {
                        ptr = ptr.next;
                        }
                        ptr.next = temp;
                    }
                    combineTerms(result);
                }
            }
            else
            {
                temp2 = null;
                temp2 = addnode(temp2, coef[((i)/(n-1)) -1], 0);

                temp = multiply(temp, temp2, temp3);

                Node ptr = result;
                if(result == null)
                {
                    result = temp;
                }
                else
                {
                    while(ptr.next != null)
                    {
                    ptr = ptr.next;
                    }
                    ptr.next = temp;
                }
                combineTerms(result);

                temp = null;
                temp = addnode(temp, 1, 1);
                temp = addnode(temp, -factors.get(i), 0);
            }
        }

        System.out.println("\n\nSimplified form: ");
        printList(result);
    }

    static class Node
    {
        double coeff;
        int power;
        Node next;
    }

    static Node addnode(Node node, double coeff, int power) 
    { 
        Node newNode = new Node(); 
        newNode.coeff = coeff; 
        newNode.power = power; 
        newNode.next = null; 
    
        if (node == null)
        {
            return newNode;
        }

        Node ptr = node; 
        while (ptr.next != null)
        {
            ptr = ptr.next; 
        }
        ptr.next = newNode; 
        return node; 
    }
    
    static void combineTerms(Node node) 
    { 
        Node ptr1, ptr2, dupe; 
        ptr1 = node; 
    
        while (ptr1 != null && ptr1.next != null)
        { 
            ptr2 = ptr1; 
   
            while (ptr2.next != null)
            { 
                if (ptr1.power == ptr2.next.power)
                { 
                    ptr1.coeff = ptr1.coeff + ptr2.next.coeff; 
                    dupe = ptr2.next; 
                    ptr2.next = ptr2.next.next; 
                } 
                else
                {
                    ptr2 = ptr2.next; 
                }
            } 
            ptr1 = ptr1.next; 
        } 
    }
    
    static Node multiply(Node poly1, Node poly2, Node poly3) 
    { 
        Node ptr1, ptr2; 
        ptr1 = poly1; 
        ptr2 = poly2; 

        while (ptr1 != null) 
        { 
            while (ptr2 != null) 
            { 
                double coeff;
                int power; 

                coeff = ptr1.coeff * ptr2.coeff; 
                power = ptr1.power + ptr2.power; 

                poly3 = addnode(poly3, coeff, power); 

                ptr2 = ptr2.next; 
            } 
    
            ptr2 = poly2; 
            ptr1 = ptr1.next; 
        } 
    
        combineTerms(poly3); 
        return poly3; 
    }

    static void printList( Node ptr) 
    { 
        while (ptr.next != null) { 
            System.out.printf("%.2fx^%d + ", ptr.coeff, ptr.power); 
    
            ptr = ptr.next; 
        } 
        System.out.printf("%.2f\n", ptr.coeff); 
    } 
}
