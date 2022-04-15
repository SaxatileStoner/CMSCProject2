/**
 * Project 2
 * @author Christopher Stoner
 * 
 * Singly Linked List data structure
 * Each node of the list should contain one term of the polynomial
 * --- Example ---
 * File Input: 5.6x^3 + 4x + 8.3
 * Linked List: 5.6 -> 3 -> 4 -> 1 -> 8.3 -> 0
 */
package Project2;

import java.util.*;

public class SinglyLinkedList<T> implements ListInterface<T> {

    // Head, Tail, and Size of the Linked List
    private Node<T> head, tail;
    private int size = 0;

    static class Node<T> {
        private T data;
        private Node<T> next;
    }

    public class ListIterator implements Iterator<T> {

        private Node<T> current;

        @Override
        public boolean hasNext() {
            if (current == null)
                return head != null;
            else
                return current.next != null;
        }

        @Override
        public T next() {
            if (current == null)
                current = head;
            else
                current = current.next;
            return current.data;
        }

    }

    // Adds new node to the list, returns true when done
    @Override
    public boolean add(T data) {
        Node<T> newNode = new Node<>();

        newNode.data = data;
        if (tail != null) {
            tail.next = newNode;
        } else {
            head = newNode;
        }
        tail = newNode;
        size++;
        return true;
    }

    // Adds new node behind index
    @Override
    public boolean add(T data, int index) {
        if (index < 0 || index > size) {
            return false;
        }
        Node<T> newNode = new Node<>();
        newNode.data = data;
        if (index == 0) {
            newNode.next = head;
            head = newNode;
        } else {
            Node<T> previous = findData(index - 1);
            newNode.next = previous.next;
            previous.next = newNode;
        }
        size++;
        return true;
    }

    // Finds and returns the node contained at point of index in the linked list
    private Node<T> findData(int index) {
        int count = 0;
        Node<T> link = head;
        while (link.next != null) {
            if (count == index) {
                return link;
            }
            count++;
            link = link.next;
        }
        return link;
    }

    @Override
    public boolean remove(T data) {
        int location = indexOf(data);
        if (location < 0) {
            return false;
        }
        return remove(location) != null;
    }

    @Override
    public T remove(int index) {
        T data;

        if (index < 0 || index >= size) {
            return null;
        }
        if (index == 0) {
            data = head.data;
            head = head.next;
        } else {
            Node<T> previous = findData(index - 1);
            data = previous.next.data;
            previous.next = previous.next.next;
        }
        size--;
        return data;
    }

    @Override
    public boolean contains(T data) {
        return indexOf(data) != -1;
    }

    @Override
    public T set(int index, T data) {
        Node<T> node = findData(index);
        if (node == null)
            return null;
        node.data = data;
        return node.data;
    }

    @Override
    public T get(int index) {
        Node<T> node = findData(index);
        if (node == null)
            return null;
        return node.data;
    }

    @Override
    public int indexOf(T data) {
        int index = 0;
        Node<T> node = head;
        while (head != null) {
            if (data.equals(node.data)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * produces an iterator that iterates across the terms in the polynomial from
     * highest exponent to lowest.
     * Returns an object that contains only the coefficient and exponent of the next
     * term
     * 
     * This is needed in order to iterate through an object and compare values in
     * the compareTo method for Polynomial
     */
    @Override
    public Iterator<T> iterator() {
        return new ListIterator();
    }

    // for debugging purposes
    public void display() {
        Node<T> current = head;

        if (head == null) {
            System.out.println("List empty!");
            return;
        }

        System.out.println("Nodes of singly linked list: ");
        while (current != null) {
            System.out.println(current.data + " ");
            current = current.next;
        }
        System.out.println();
    }
}
