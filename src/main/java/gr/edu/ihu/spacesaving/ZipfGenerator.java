
package gr.edu.ihu.spacesaving;

import cern.jet.random.Distributions;
import cern.jet.random.engine.RandomEngine;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Generates a random Zipfian dataset
 * @param a is the measure of skewness and must be > 1.
 * input must be below 10^8
 */
public class ZipfGenerator {
    
    public static double a = 1.5;
             
    public static void main(String[] args) {
                
        //create text file of random strings/integers
        RandomEngine re = RandomEngine.makeDefault();       
        String[] input = new String[10000000];                
        try {
        FileWriter f1 = new FileWriter("C:\\Users\\Hyae\\Documents\\Zipf1.5.txt", true);
        //Random r = new Random();
        for (int j=0; j<input.length; j++) {
            //String x = "a" + (r.nextInt(500000));
            input[j] = "a" + Distributions.nextZipfInt(a, re);
            f1.write(input[j] + "\n");            
        }        
        f1.close();
        } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
        }
    }
    
}
    
