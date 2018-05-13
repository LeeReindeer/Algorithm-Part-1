import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author leer
 * Created at 4/20/18 3:24 PM
 * use lined list
 */
public class Deque<Item> implements Iterable<Item> {

  private Node head;
  private Node tail;

  private int size;

  private class Node {
    Item item;
    Node prev;
    Node next;

    public Node(Item item) {
      this.item = item;
    }
  }

  public Deque() {
  }

  private void validItem(Item item) {
    if (item == null) {
      throw new IllegalArgumentException();
    }
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public int size() {
    return size;
  }

  /**
   * add the item to the front
   * same as push in stack
   *
   * @param item
   */
  public void addFirst(Item item) {
    validItem(item);

    Node newNode = new Node(item);
    if (head == null) {
      head = tail = newNode;
    } else {
      newNode.next = head;
      head.prev = newNode;
      head = newNode; //move head pointer
    }
    size++;
  }


  /**
   * same as enqueue in queue
   * add the item to the end
   */
  public void addLast(Item item) {
    validItem(item);

    Node newNode = new Node(item);
    if (tail == null) {
      head = tail = newNode;
    } else {
      tail.next = newNode;
      newNode.prev = tail;
      tail = newNode; //move tail pointer
    }
    size++;
  }

  public Item removeFirst() {
    if (size() == 0) {
      throw new NoSuchElementException();
    }
    Item item = head.item;
    if (head == tail) {
      head = null;
      tail = null;
      size--;
      return item;
    }
    head = head.next;
    head.prev = null;
    size--;
    return item;
  }

  public Item removeLast() {
    if (size() == 0) {
      throw new NoSuchElementException();
    }
    Item item = tail.item;
    if (tail == head) {
      head = null;
      tail = null;
      size--;
      return item;
    }
    tail = tail.prev;
    tail.next = null; //clear it!
    size--;
    return item;
  }

  /**
   * @return an iterator over items in order from front to end
   */
  public Iterator<Item> iterator() {
    return new DequeIterator();
  }

  private class DequeIterator implements Iterator<Item> {

    private Node current = head;

    @Override
    public boolean hasNext() {
      return current != null;
    }

    @Override
    public Item next() {
      if (current == null) {
        throw new NoSuchElementException();
      }
      Item item = current.item;
      current = current.next;
      return item;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException("remove");
    }
  }

  public static void main(String[] args) {
    Deque<Integer> deque = new Deque<>();
//    deque.addFirst(null);
    deque.addFirst(1);
    deque.addFirst(2);
    deque.addFirst(3);

    deque.addLast(4);
    deque.addLast(5);
    deque.addLast(6);
    // 3 -> 2 -> 1 -> 4 -> 5 -> 6
    System.out.println("size: " + deque.size());

    deque.removeFirst();
    deque.removeFirst();
    deque.removeFirst();
    deque.removeFirst();
    deque.removeFirst();
    deque.removeFirst();

//    deque.removeLast();
//    deque.removeLast();
//    deque.removeLast();
//    deque.removeLast();

    System.out.println("size: " + deque.size());

    for (int i : deque) {
      System.out.println(i);
    }
  }
}
