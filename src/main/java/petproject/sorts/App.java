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
    var table = ShannonFanoCode.buildTableFano("НО ВРЕДЕН СЕВЕР ДЛЯ МЕНЯ. КОГДА ЖЕ ЮНОСТИ МЯТЕЖНОЙ");

    table.entrySet().stream()
        //.sorted(Comparator.comparing(e -> Integer.parseInt(e.getValue(), 2)))
        .sorted(Comparator.comparing(e -> e.getValue().length()))
        .map(e -> String.format("letter: %s key:%s", e.getKey(), e.getValue()))
        .forEach(System.out::println);

  }
}
