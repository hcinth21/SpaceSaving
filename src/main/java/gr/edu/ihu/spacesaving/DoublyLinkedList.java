
package gr.edu.ihu.spacesaving;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 *An implementation which allows easier access to the inner structure of the
 *LinkedList compared to java.util.LinkedList
 *@param <T> type of item in the stream 
 */
public class DoublyLinkedList<T> implements Iterable<T>  {

    protected int size;
    /**
     * The node class used by this doubly linked list class
     * @param <T> type of item in the stream 
     */
    public static class Node<T> {

        private Node next;
        private Node prev;
        private T item;

        public Node(T item) {
            this.item = item;
        }

        public T getItem() {
            return item;
        }

        public Node<T> getNext() {
            return next;
        }

        public Node<T> getPrev() {
            return prev;
        }
    }

    /**
     * Pointer to the head of the linked list
     */
    private Node<T> head;

    /**
     * Pointer to the tail of the linked list
     */
    private Node<T> tail;

    /**
     * Constructor
     */
    public DoublyLinkedList() {
        this.head = null;
        this.tail = null;
    }
    
    public Node<T> getTail() {
        return tail;
    }

    public Node<T> getHead() {
        return head;
    }
    
    public boolean isEmpty(){        ;
        return size == 0;
    }
    /**
     * Insert newNode after node
     * @param node
     * @param newNode
     */
    public synchronized void insertAfter(Node node, Node newNode) {
        newNode.prev = node;
        newNode.next = node.next;
        if (node.next == null) {
            tail = newNode;
        } else {
            node.next.prev = newNode;
        }
        node.next = newNode;
    }

    /**
     * Insert newNode before node
     * @param node
     * @param newNode
     */
    public synchronized void insertBefore(Node node, Node newNode) {
        newNode.prev = node.prev;
        newNode.next = node;
        if (node.prev == null) {
            head = newNode;
        } else {
            node.prev.next = newNode;
        }
        node.prev = newNode;
    }

    /**
     * Insert newNode as the new head
     * @param newNode
     */
    public synchronized void insertBeginning(Node newNode) {
        if (head == null) {
            head = newNode;
            tail = newNode;
            newNode.prev = null;
            newNode.next = null;
        } else {
            insertBefore(head, newNode);
        }
    }

    /**
     * Insert newNode as the new tail
     * @param newNode
     */
    public synchronized void insertEnd(Node newNode) {
        if (tail == null) {
            insertBeginning(newNode);
        } else {
            insertAfter(tail, newNode);
        }
    }

    /**
     * Remove node from the linked list
     * @param node
     */
    public synchronized void remove(Node node) {
        if (node.prev == null) {
            head = node.next;
        } else {
            node.prev.next = node.next;
        }
        if (node.next == null) {
            tail = node.prev;
        } else {
            node.next.prev = node.prev;
        }
    }
    
    public Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }    
        
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private DoublyLinkedList.Node<T> curr = head;
            public boolean hasNext() {
                return curr != null;
            }
            public T next() {
                Node<T> temp = curr;
                curr = curr.next;
                return temp.getItem();
            }
            public void remove() {
            }
        };
    }    
    

}