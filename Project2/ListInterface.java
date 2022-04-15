package Project2;

public abstract interface ListInterface<T> extends Iterable<T> {
    // Used in the collection interface
    boolean add(T data);

    boolean remove(T data);

    boolean contains(T data);

    // Used in the list interface
    boolean add(T data, int index);

    T set(int index, T data);

    T get(int index);

    int indexOf(T data);

    T remove(int index);

    int size();
}
