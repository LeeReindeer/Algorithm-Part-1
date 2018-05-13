import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author leer
 * Created at 4/20/18 3:24 PM
 * use array
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

  private Item[] items;

  private int headIndex;
  private int tailIndex;

  private int size;


  private static final int DEFAULT_CAP = 2;
  private int cap = DEFAULT_CAP;//also as array max cap

  public RandomizedQueue() {
    //noinspection unchecked
    items = (Item[]) new Object[DEFAULT_CAP + 1];
  }

  /**
   * resize array and shuffle it, so the default cap is small enough.
   * @param length
   */
  private void resize(int length) {
    //noinspection unchecked
    Item[] newItems = (Item[]) new Object[cap = length];

    //rearrange the entries in random order
    //StdRandom.setSeed(System.currentTimeMillis());
    StdRandom.shuffle(items, headIndex, tailIndex);

    System.arraycopy(items, headIndex, newItems, 0, size);

    //reset
    headIndex = 0;
    tailIndex = size;

    this.items = newItems;
  }

  public void enqueue(Item item) {
    if (item == null) {
      throw new IllegalArgumentException();
    }
    if (size() == cap || tailIndex >= cap) {
      resize(2 * cap);
    }
    items[tailIndex++] = item;
    size++;
  }

  /**
   * remove a random node
   * @return node's item
   */
  public Item dequeue() {
    if (size() <= 0) {
      throw new NoSuchElementException();
    }

    if (size() <= cap / 4 ) {
      resize(cap / 2);  //shrink array only when 1/4 full
    }

    Item e = items[headIndex];
    items[headIndex] = null;
    headIndex++;
    size--;
    return e;
  }

  public boolean isEmpty() {
    return size() == 0;
  }

  public int size() {
    return size;
  }

  public Item sample() {
    if (size() <= 0) {
      throw new NoSuchElementException();
    }

    //between head and tail - 1;
    return items[StdRandom.uniform(headIndex, tailIndex)];
  }

  @Override
  public Iterator<Item> iterator() {
    return new ArrayIterator();
  }

  /**
   * //fixme
   * Test 10: create two parallel iterators over the same randomized queue
   * n = 10
   - two iterators return the same sequence of values
   - they should return the same set of values but in a
   different order
   */
  private class ArrayIterator implements Iterator<Item> {

    private int i = headIndex;

    public ArrayIterator() {
      StdRandom.shuffle(items, headIndex, tailIndex);
    }

    @Override
    public boolean hasNext() {
      return i < size;
    }

    @Override
    public Item next() {
      if (i >= size || items[i] == null) {
        throw new NoSuchElementException();
      }
      return items[i++];
    }
  }

  public static void main(String[] args) {
  }
}
