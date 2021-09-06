
package gr.edu.ihu.spacesaving;

import java.util.LinkedList;
import java.util.Objects;

/**
 *
 * @param <T> type of the item
 */
public class Bucket<T> {
    
    //Child list of counters attached to a parent bucket
    private LinkedList<Counter<T>> counterList = new LinkedList<Counter<T>>();
    
    private long value;
    
    public Bucket(long value){
        this.value = value;
    }
        
    public LinkedList<Counter<T>> getCounterList() {
        return counterList;
    }

    public void setCounterList(LinkedList<Counter<T>> counterList) {
        this.counterList = counterList;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }   
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.counterList);
        hash = 83 * hash + (int) (this.value ^ (this.value >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Bucket<?> other = (Bucket<?>) obj;
        if (this.value != other.value) {
            return false;
        }
        if (!Objects.equals(this.counterList, other.counterList)) {
            return false;
        }
        return true;
    }
     
    @Override
    public String toString() {
        return "Bucket{" + "counterList=" + counterList + ", value=" + value + '}';
    }
       
        
}
