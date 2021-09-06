
package gr.edu.ihu.spacesaving;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Does not produce the correct skew
 * Generates uniform data distribution instead
 */
public class Zipf {
    
    private Random rnd = new Random(System.currentTimeMillis());
    private static int size = (10000000);
    private static double skew = 0.5;
    private double bottom = 0;
    public static String outputname = ("C:\\Users\\Hyae\\Documents\\Zipftest.txt");
    
    public void zipfGen(int size, double skew) {
         this.size = size;
         this.skew = skew;
 
         for(int i=1;i <= size; i++) {
             this.bottom += (1/Math.pow(i, this.skew));
         }
    }    
    
    
    public int next() {
        int rank;
        double frequency = 0;
        double dice;
         
        rank = rnd.nextInt(size)+1;
        frequency = (1.0d / Math.pow(rank, this.skew)) / this.bottom;
        dice = rnd.nextDouble();

        while(!(dice < frequency)) {
            rank = rnd.nextInt(size)+1;
            frequency = (1.0d / Math.pow(rank, this.skew)) / this.bottom;
            dice = rnd.nextDouble();
        }
        return rank;
    } 
    
    public double getProbability(int rank) {
        return (1.0d / Math.pow(rank, this.skew)) / this.bottom;
    }
    
    public static void main(String[] args) {
                
        //create text file of random strings/integers
        Zipf z = new Zipf();
        //z.zipfGen(size,skew);
        String[] input = new String[10000000];                
        try {
        FileWriter f1 = new FileWriter(outputname, true);
        Random r = new Random();
        for (int j=0; j<input.length; j++) {
            //input[j] = "a" + (r.nextInt(500000));            
            input[j] = "a" + z.next();
            f1.write(input[j] + "\n");            
        }        
        f1.close();
        } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
        }
    }

    
}
