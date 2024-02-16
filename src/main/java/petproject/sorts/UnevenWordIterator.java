
package petproject.sorts;

import java.util.Iterator;

public class UnevenWordIterator implements Iterator<String> {

  private int prevNum = 0;

  @Override
  public boolean hasNext() {
    return true;
  }

  @Override
  public String next() {
    return Integer.toString(prevNum++, 2) + "00";
  }

}
