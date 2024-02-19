
package petproject.LabExecutor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.StringJoiner;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CodeTableBuilder {

  static class Node {

    public Node(double probability, char letter) {
      this.probability = probability;
      this.letter = letter;
    }

    public Node(double appriority) {
      this.probability = appriority;
    }

    public final double probability;

    public double getProbability() {
      return probability;
    }

    Node left, right;
    Character letter;

  }

  /**
   * Представляет бинарное дерево на языке dot
   * 
   * @param root
   * @return
   */
  public static String formatTree(Node root) {
    StringJoiner sj = new StringJoiner("\n");

    sj.add("digraph BST {");

    Consumer<Node> formater = new Consumer<CodeTableBuilder.Node>() {
      @Override
      public void accept(Node n) {

        if (n.letter == null) {
          sj.add(String.format("%d [label=\"%.2f\"]", n.hashCode(), n.probability));
          sj.add(String.format("%d -> %d", n.hashCode(), n.left.hashCode()));
          sj.add(String.format("%d -> %d", n.hashCode(), n.right.hashCode()));
          this.accept(n.left);
          this.accept(n.right);
        } else
          sj.add(String.format("%d [label=\"%s %.2f\", style=filled, fillcolor=\"#78f7c2\"]", n.hashCode(), n.letter,
              n.probability));
      }
    };
    formater.accept(root);

    sj.add("}");
    return sj.toString();
  }

  public static Map<Character, String> buildHuffmanTable(Node root) {
    var table = new HashMap<Character, String>();

    BiConsumer<Node, String> table_builder = new BiConsumer<CodeTableBuilder.Node, String>() {
      @Override
      public void accept(Node n, String buffer) {
        if (n.letter != null)
          table.put(n.letter, buffer);
        else {
          this.accept(n.left, buffer + "1");
          this.accept(n.right, buffer + "0");
        }
      }
    };
    table_builder.accept(root, "");

    return table;
  }

  /**
   * Метод строит бинарное дерево для кодирования Хаффмана
   * 
   * @param line
   * @return
   */
  public static Node buildHuffmanTree(String line) {
    var letter_counters = countLetter(line);
    Comparator<Node> c = Comparator.comparing(Node::getProbability);
    List<Node> nodes = letter_counters.entrySet().stream()
        .map(e -> new Node((double) e.getValue() / line.length(), e.getKey()))
        .sorted(c)
        .collect(Collectors.toList());

    while (nodes.size() > 1) {
      Node n1, n2, s;
      n1 = nodes.removeFirst();
      n2 = nodes.removeFirst();
      s = new Node(n1.probability + n2.probability);
      s.right = n1;
      s.left = n2;
      nodes.add(s);
      nodes.sort(c);
    }
    var root = nodes.removeFirst();
    return root;
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
    var codes = new String[] { "100", "1000", "1100", "10000", "10100", "11000", "11100", "101000", "101100",
        "110000", "110100", "111000", "111100", "1010000", "1010100", "1011000", "1011100", "1101000", "1101100",
        "1110000", "1110100", "1111000", "1111100", "10101000", "10101100", "10110000", "10110100", "10111000",
        "10111100", "11010000", "11011000" };

    var iterator = Arrays.stream(codes).iterator();

    var result = new HashMap<Character, String>();
    result.put(' ', "000");
    letter_apriority.remove(' ');

    letter_apriority.entrySet().stream()
        .sorted(Comparator.comparing(Entry::getValue, Comparator.reverseOrder()))
        .forEach(e -> result.put(e.getKey(), iterator.next()));

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

    return result;
  }

}
