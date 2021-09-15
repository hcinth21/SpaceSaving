
package gr.edu.ihu.spacesaving;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

/**
 * An implementation of the Space-Saving algorithm described in <i>Efficient 
 * Computation of Frequent and Top-k Elements in Data Streams</i> by Metwally,
 * Agrawal, and Abbadi.
 * 
 * The algorithm utilizes a data structure referred to as <i>Stream Summary</i>
 * which allows O(1) insert to track frequent elements, and O(k) to retrieve 
 * the top k elements.
 * 
 * @param <T> type of element in the stream
 * 
 */
public class StreamSummary<T> {
    
     //public static String filename = "C:\\Users\\kosarak.txt"; //real data
     public static String filename = "C:\\Users\\zipf1.51.txt"; //synthetic data
     public static double a = 1.5; //skewness
     private static double phi = 0.0004;
     private static double epsilon = 0.001;
     private final int capacity; //number of counters
     private boolean guaranteed = true;
          
     private final LongAdder n = new LongAdder(); //number of elements found in the stream
     private Map <T, Counter<T>> counterMap = new HashMap<>(); //maps the element with the counter address in the linked list
     private final DoublyLinkedList<Bucket<T>> bucketList = new DoublyLinkedList<>(); //list of buckets
     
     
    /**
     *@param phi the user support (0-1)
     *@param epsilon the acceptable error rate (0-1)
     */
     public StreamSummary(double phi, double epsilon) {
         
         if(phi <= 0 || phi >= 1){
             throw new IllegalArgumentException("Support has to be >0 and <1.");
         }
         
         if(epsilon <= 0 || epsilon >= 1){
             throw new IllegalArgumentException("Error rate has to be >0 and <1.");
         }
                  
         if (a<=1){
             //number of counters for data where skewness, alpha <=1      
             this.capacity = (int) Math.ceil(1.0/epsilon);             
         } else {
             //number of counters for zipfian data where skewness, alpha >1          
             this.capacity = (int) Math.ceil((Math.pow((1.0/phi),((a+1)/(a*a)))));
         }        
         
         //insert nodes with value zero 
         Bucket<T> bucketZero = new Bucket<>(0);        
         DoublyLinkedList.Node<Bucket<T>> node = new DoublyLinkedList.Node<>(bucketZero);
         for (int i = 0; i < capacity; i++) {
            bucketZero.getCounterList().add(new Counter<>(node,null));
         }
         bucketList.insertBeginning(node);
                 
     }
    
    /**
     * The capacity is inversely proportional to the error rate (1/epsilon)
     * @return capacity the number of counters
     */     
    public int getCapacity(){
        return capacity;
    }
    
    /**
     * Insert an item from the stream and increment the count by 1
     * @param item 
     */
    public void offer(T item){
        offer(item,1);
    }    

    
    /**
     * Insert an item from the stream and increment the count as specified
     * @param item the element from the stream
     * @param increment the amount to be added to the count
     */
    public void offer(T item, int increment){        
        Counter<T> counter = counterMap.get(item);
        n.increment();         
        if (counterMap.containsKey(item)) {            
            counter = counter;            
        } else {                   
            //if (size() < capacity){
                //counter = bucketList.insertBeginning(new Bucket<T>(0)).getCounterList().add(new Counter<T>(bucketList.getTail(), item)));
                /*//Assign new item to a bucket with zero value
                DoublyLinkedList.Node<Bucket<T>> bucketZero = new DoublyLinkedList.Node<>(new Bucket<>(0));
                bucketZero.getItem().getCounterList().addLast(counter);
                bucketList.insertEnd(bucketZero);
                counter.setBucket(bucketZero);  --> !error when setting the bucket for counter
                */
            //} else {
                Counter<T> counterMin = bucketList.getTail().getItem().getCounterList().getFirst();
                counter = counterMin;
                long min = counter.getValue();
                counterMap.remove(counter.getItem());
                counter.setItem(item);
                counter.setError(min);                                
            //}
                counterMap.put(item, counter);
        }
        incrementCounter(counter, increment);        
    }
    
    /**
     * Returns the number of items observed in the stream so far
     * @return the number of items
     */
    public long getN() {
        return n.longValue();
    }
    
    public int size(){
        return counterMap.size();
    }
    
    /**
     * Method to increment the count of the item and assign it to the 
     * corresponding bucket
     * @param counter
     */
    
    private void incrementCounter(Counter<T> counter, int increment){
        DoublyLinkedList.Node<Bucket<T>> bucket = counter.getBucket(); //Bucket_i of count_i 
        DoublyLinkedList.Node<Bucket<T>> bucketNext = bucket.getPrev(); //Bucket_i's neighboring bucket with larger value = Bucket_i+
        bucket.getItem().getCounterList().remove(counter); //Detach count_i from child list
        counter.setValue(counter.getValue() + increment); //Increment count_i
        
        DoublyLinkedList.Node<Bucket<T>> bucketPrev = bucket; //Bucket_i in case increment count is more than 1
        while (bucketNext != null){
            //Finds the bucket with equivalent value
            if(counter.getValue() == bucketNext.getItem().getValue()){
                bucketNext.getItem().getCounterList().addLast(counter);
                counter.setBucket(bucketNext);
                break;
            } else if(counter.getValue() > bucketNext.getItem().getValue()){
                bucketPrev = bucketNext;
                bucketNext = bucketPrev.getNext();
            } else {
                bucketNext = null;
            }                
        }
        //if no bucket is found, create new bucket
        if (bucketNext == null) {           
            DoublyLinkedList.Node<Bucket<T>> bucketNew = new DoublyLinkedList.Node<>(new Bucket<>(counter.getValue()));
            bucketNew.getItem().getCounterList().addLast(counter);
            bucketList.insertBefore(bucketPrev, bucketNew);
            counter.setBucket(bucketNew);
        }
        //Detaches the bucket that was emptied from the bucket linked list
        if (bucket.getItem().getCounterList().isEmpty()) {
            bucketList.remove(bucket);
        }
    }  
      
        
    /**
     * Returns the frequent elements in the stream
     * Items with true count greater than phi*N are guaranteed to be in the stream summary
     * @param phi the user support
     */   
    public List<Counter<T>> queryFrequent() {
        List<Counter<T>> freq = new ArrayList<Counter<T>>();
                 
        bucketList.stream().flatMap(b -> b.getCounterList().stream())
                .forEach(
                        b -> {                             
                            if (((b.getValue()) > ((phi)*getN()) && (size() <= capacity))){
                                freq.add(b);
                            } else if ((b.getValue() - b.getError()) < (phi*getN())) {                                
                                guaranteed = false;
                            }
                        }
                );       
         
        
        return freq;
    }
    
    
    /**
     * Returns the top-k elements from the stream summary in descending order
     * @param k rank
     * @return topK elements and their respective counts
     */
    public List<Counter<T>> queryTopK(int k){
        List<Counter<T>> topK = new ArrayList<Counter<T>>(k);
                
        //Finds the top-k elements based on the order. This will not report elements at k+1.. despite having the same value as k
        topK = queryFrequent().stream()
               .limit(k).collect(Collectors.toList());
        
        //Finds the top-k elements based on the value
        //Gets the top-k bucket and returns all the elements within those buckets
        //topK = bucketList.stream()
        //       .limit(k).flatMap(b -> b.getCounterList().stream()).collect(Collectors.toList());        

        return topK;
        
    } 
    
    public void read(){
        try{
            FileReader f2 = new FileReader(filename);
            Scanner s = new Scanner(f2); 
                while (s.hasNextLine()) { 
                    String line = s.nextLine(); 
                    String[] stream = line.split("\\s"); 
                    for (String i : stream) {
                        offer((T) i);
                    }                     
                }                
                f2.close();                
        }catch(IOException ex1){                             
            System.out.println("An error occurred."); 
            ex1.printStackTrace(); 
        } 
    }

    
    public static void main(String[] args) {
        
        StreamSummary<String> ss = new StreamSummary<String>(phi, epsilon); //0.3 was chosen to check error and capacity bound
        System.out.println("STREAM ELEMENTS");                
        ss.read();
        //String[] stream = {"X", "Y", "Y", "Z", "B", "X", "A", "X", "X", "B", "Y"}; //for testing
        /*
        for (String i : stream) {
            ss.offer(i);            
            System.out.println(ss.counterMap);
        }*/
        
        //retrieve frequent elements
        List<Counter<String>> freq = ss.queryFrequent();       
        System.out.println("FREQUENT ITEMS");
        int Capacity = ss.capacity;
        System.out.println("No. of counters: " + Capacity);
        System.out.println("No. of items in the stream:" + ss.getN());
        System.out.println("No. of frequent items in SS:" + freq.size());        
        for (Counter<String> e : freq) {             
            System.out.println(e);
        }
        
      //retrieve top-k elements
        List<Counter<String>> topK = ss.queryTopK(10);
        System.out.println("TOP-K ELEMENTS");        
        for (Counter<String> c : topK) {            
            System.out.println(c.getValue());
        }
       
        Runtime runtime = Runtime.getRuntime();
        // Run the garbage collector
        runtime.gc();
        // Calculate the used memory
        long memory = runtime.totalMemory() - runtime.freeMemory();        
        System.out.println("Memory usage in bytes: " + memory);
        
        
        
        
    
    }
   
}
