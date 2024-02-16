
package petproject.sorts;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.stream.Collectors;

public class CodeTableBuilder {

  /**
   * @param line
   * @return
   */
  public static Map<Character, String> buildTableHuffman(String line) {

    var table = new HashMap<Character, String>();
    var letter_apriority = countLetter(line);

    class Node {

      Node(Entry<Character, Integer> e) {
        this.appriority = e.getValue();
        this.letter = e.getKey();
      }

      Node(int appriority) {
        this.appriority = appriority;
      }

      int appriority;

      public int getAppriority() {
        return appriority;
      }

      Node left, right;
      Character letter;

      void buildTable(String buffer) {
        if (letter != null) {
          table.put(letter, buffer);
        } else {
          left.buildTable(buffer + "1");
          right.buildTable(buffer + "0");
        }
      }

    }

    Comparator<Node> c = Comparator.comparing(Node::getAppriority);

    List<Node> nodes = letter_apriority.entrySet().stream()
        .map(Node::new)
        .sorted(c)
        .collect(Collectors.toList());

    while (nodes.size() > 1) {
      Node n1, n2, s;
      n1 = nodes.removeFirst();
      n2 = nodes.removeFirst();
      s = new Node(n1.appriority + n2.appriority);
      s.right = n1;
      s.left = n2;
      nodes.add(s);
      nodes.sort(c);
    }

    nodes.removeLast().buildTable("");

    return table;
  }

  /**
   * Метод строит таблицу кодирования-декодирования методом Шеннона-Фано
   * 
   * @param line
   * @return
   */
  public static Map<Character, String> buildTableFano(String line) {
    var table = new HashMap<Character, String>();

    var letter_apriority = countLetter(line);
    var sorted_letters = letter_apriority.entrySet().stream()
        .sorted(Comparator.comparing(Entry::getValue))
        .collect(Collectors.toList());

    Stack<List<Entry<Character, Integer>>> to_check = new Stack<>();

    to_check.push(sorted_letters);

    while (!to_check.isEmpty()) {
      var current = to_check.pop();
      System.out.printf("Разбивка %s \n", current);

      if (current.size() < 2)
        continue;

      int si = splitSection(current.stream()
          .map(Entry::getValue)
          .collect(Collectors.toList()));

      var l1 = current.subList(0, si);
      var l2 = current.subList(si, current.size());

      String adding_code = "0";
      for (var sublist : Set.of(l1, l2)) {
        for (var entry : sublist) {
          String code = table.get(entry.getKey());
          if (code == null)
            code = "";
          code += adding_code;
          table.put(entry.getKey(), code);
        }
        if (sublist.size() > 1)
          to_check.push(sublist);
        adding_code = "1";
      }
    }

    //
    return table;
  }

  public static String formatTable(Map<Character, String> table) {
    Comparator<Entry<Character, String>> c = Comparator
        .comparing((Entry<Character, String> e) -> e.getValue().length())
        .thenComparing(e -> Integer.parseInt(e.getValue(), 2));

    return table.entrySet().stream()
        .sorted(c)
        .map(e -> String.format("letter: %s key:%s", e.getKey(), e.getValue()))
        .collect(Collectors.joining("\n"));
  }

  /**
   * Разбивка списка вероятностей на две приблизительно равновероятные
   * группы.
   * Возращает индекс граничного элемента. Элементы с индексами от 0 до (n-1)
   * принадлежать первой группе, от n до lenght - второй.
   * Входной список должен быть отсортирован!
   * 
   * @param appriority - список вероятностей, с числом элементов больше двух
   * @return - индекс граничного элемента
   */
  public static int splitSection(List<Integer> appriority) {
    if (appriority.size() < 2)
      throw new IllegalArgumentException("Нельзя выполнить разбиение массива с длинной меньше 2");
    int i = 0;
    int limit = appriority.stream().mapToInt(Integer::intValue).sum() / 2;
    int left_sum = 0;

    while (left_sum < limit) {
      left_sum += appriority.get(i);
      i++;
    }
    if (i == appriority.size())
      i--;

    return i;
  }

  /**
   * Метод строит таблицу кодирования-декодирования для неравномерного кода
   * 
   * @return
   */
  public static Map<Character, String> buildTableUneven(String line) {

    var letter_apriority = countLetter(line);
    var gen = new UnevenWordIterator();
    var result = new HashMap<Character, String>();

    letter_apriority.entrySet().stream()
        .sorted(Comparator.comparing(Entry::getValue))
        .map(Entry::getKey)
        .forEach(k -> result.put(k, gen.next()));

    return result;
  }

  /**
   * Подсчет символов в строке.
   * 
   * @param line
   * @return
   */
  public static Map<Character, Integer> countLetter(String line) {
    Map<Character, Integer> result = new HashMap<>();

    for (int i = 0; i < line.length(); i++) {
      Character ch = line.charAt(i);
      Integer counter = result.get(ch);
      if (counter == null)
        counter = 0;

      counter++;
      result.put(ch, counter);
    }

    Map<Character, Double> app = new HashMap<>();

    result.entrySet().forEach((Entry<Character, Integer> e) -> {
      app.put(e.getKey(), (double) e.getValue() / line.length());
    });
    System.out.println(app);

    return result;
  }

}
