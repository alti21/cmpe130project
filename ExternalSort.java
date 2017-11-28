/**
 Project Overview:
 
  JAVA program to implement external sorting using MERGE-SORT an example of an External-memory sorting algorithm. In MERGE-SORT, the file to be sorted is broke up into smaller blocks and then these blocks are seperately sorted and then the sorted blocks are merged together.
 
 External memory sorting algorithm is called "external" as in the sorting is done exterenal to the processing unit. External memory sorting algorithms has high cost of accessing an item. There are several restrictions to access the data depending on the external storage medium used. For example, magnetic tape can access data only in a sequential manner
 
 Alternatively, internal memory sorting algorithm where locality of reference is present works well in virtual memory system, for example, Quick Sort. Internal memory sorting algorithms can cause severe problems in sorting disk files in a virtual-memory system.
 */

//ExternalSort.java
//Package name.
package externalsort;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

//Class.
public class ExternalSort
{
    
    //Size of file in disk.
    static int SIZE = 2000000;
    
    //Maximum data the memory buffer can hold.
    static int MAXDATA = 100000;
    
    //Function externalSort().
    public static void ExternalSort(String fName)
    {
        String tempFile = "temp-file-";
        int[] myBuffer = new int[MAXDATA < SIZE ? MAXDATA : SIZE];
        try
        {
            FileReader myFR = new FileReader(fName);
            BufferedReader myBR = new BufferedReader(myFR);
            int mySlices = (int) Math.ceil((double) SIZE/MAXDATA);
            int idx1, idx2;
            idx1 = idx2 = 0;
            
            //For loop to iterate through data in the file.
            for (idx1 = 0; idx1 < mySlices; idx1++)
            {
                
                //Read MAXDATA at a time from the file.
                for (idx2 = 0; idx2 < (MAXDATA < SIZE ? MAXDATA : SIZE); idx2++)
                {
                    String myT = myBR.readLine();
                    if (myT != null)
                        myBuffer[idx2] = Integer.parseInt(myT);
                    else
                        break;
                }
                
                //Sort MAXDATA elements
                Arrays.sort(myBuffer);
                
                //Write sorted data to the temp file
                FileWriter myFW = new FileWriter(tempFile + Integer.toString(idx1) + ".txt");
                PrintWriter myPW = new PrintWriter(myFW);
                for (int idx3 = 0; idx3 < idx2; idx3++)
                    myPW.println(myBuffer[idx3]);
                myPW.close();
                myFW.close();
            }
            myBR.close();
            myFR.close();
            
            //Now open each temp file.
            //Merge them and write back to the disk.
            int[] topNumbers = new int[mySlices];
            BufferedReader[] brs = new BufferedReader[mySlices];
            for (idx1 = 0; idx1 < mySlices; idx1++)
            {
                brs[idx1] = new BufferedReader(new FileReader(tempFile + Integer.toString(idx1) + ".txt"));
                String myT = brs[idx1].readLine();
                if (myT != null)
                    topNumbers[idx1] = Integer.parseInt(myT);
                else
                    topNumbers[idx1] = Integer.MAX_VALUE;
            }
            FileWriter myFW = new FileWriter("E:\\sorted.txt");
            PrintWriter myPW = new PrintWriter(myFW);
            for (idx1 = 0; idx1 < SIZE; idx1++)
            {
                int min = topNumbers[0];
                int minFile = 0;
                for (idx2 = 0; idx2 < mySlices; idx2++)
                {
                    if (min > topNumbers[idx2])
                    {
                        min = topNumbers[idx2];
                        minFile = idx2;
                    }
                }
                myPW.println(min);
                String myT = brs[minFile].readLine();
                if (myT != null)
                    topNumbers[minFile] = Integer.parseInt(myT);
                else
                    topNumbers[minFile] = Integer.MAX_VALUE;
            }
            for (idx1 = 0; idx1 < mySlices; idx1++)
                brs[idx1].close();
            myPW.close();
            myFW.close();
        }
        catch (FileNotFoundException fnfe)
        {
            fnfe.printStackTrace();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
    
    //Function inputGeneration().
    static String inputGeneration(int dataSize)
    {
        String fName = "input.txt";
        Random rand = new Random();
        try
        {
            FileWriter myFW = new FileWriter(fName);
            PrintWriter myPW = new PrintWriter(myFW);
            for (int idx1 = 0; idx1 < dataSize; idx1++)
                myPW.println(rand.nextInt(101));
            myPW.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        return fName;
    }
    
    //Function main().
    public static void main(String[] args)
    {
        String fName = inputGeneration(SIZE);
        ExternalSort(fName);
    }
}
