
package gr.edu.ihu.spacesaving;

import java.util.Objects;

/**
 *
 * @param <T> type of the item
 */
public class Counter<T> {
    
    private T item;
    private long value;
    private long error;
    private DoublyLinkedList.Node<Bucket<T>> bucket;
        
        
    public Counter(DoublyLinkedList.Node<Bucket<T>> bucket, T item){
        this.bucket = bucket;
        this.value = 0;
        this.error = 0;
        this.item = item;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public long getError() {
        return error;
    }

    public void setError(long error) {
        this.error = error;
    }

    public DoublyLinkedList.Node<Bucket<T>> getBucket() {
        return bucket;
    }

    public void setBucket(DoublyLinkedList.Node<Bucket<T>> bucket) {
        this.bucket = bucket;
    }
    
    public int compareTo(Counter<T> o){
        long x = o.getValue();
        long y = value;
        return (x < y) ? -1 : ((x == y) ? 0:1);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.item);
        hash = 41 * hash + (int) (this.value ^ (this.value >>> 32));
        hash = 41 * hash + (int) (this.error ^ (this.error >>> 32));
        hash = 41 * hash + Objects.hashCode(this.bucket);
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
        final Counter<?> other = (Counter<?>) obj;
        if (this.value != other.value) {
            return false;
        }
        if (this.error != other.error) {
            return false;
        }
        if (!Objects.equals(this.item, other.item)) {
            return false;
        }
        if (!Objects.equals(this.bucket, other.bucket)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "item=" + item + ", value=" + value + ", error=" + error /*+ ", bucket=" + bucket */;
    }
   
     
       
}
