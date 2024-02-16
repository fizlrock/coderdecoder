package petproject.sorts;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class App {
  public static void main(String[] args) {
    System.out.println("hey hey");
    var table = CodeTableBuilder.buildTableHuffman("AAAAAAAAAAAAAAAAAAAAAABBBBBBBBBBBBBBBBBBBBCCCCCCCCCCCCCCCCDDDDDDDDDDDDDDDDEEEEEEEEEEFFFFFFFFFFGGGGHH");
    System.out.println(CodeTableBuilder.formatTable(table));

    

  }
}
