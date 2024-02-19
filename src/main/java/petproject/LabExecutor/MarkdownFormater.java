
package petproject.LabExecutor;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class MarkdownFormater {
  private MarkdownFormater() {
  }

  public static String formatTable(Map<Character, String> table) {
    StringJoiner sj = new StringJoiner("\n");
    sj.add("| Буква | Код |");
    sj.add("|----------|:-------------|");

    Comparator<Entry<Character, String>> c = Comparator.comparing(e -> e.getValue().length());
    c = c.thenComparing(e -> e.getValue());
    table.entrySet()
        .stream()
        .sorted(c)
        .forEach(
            e -> sj.add(String.format("| %s | %s |", e.getKey(), e.getValue())));

    return sj.toString();
  }

  public static String formatTables(Map<Character, String> uneven, Map<Character, String> fano,
      Map<Character, String> huffman) {

    StringJoiner sj = new StringJoiner("\n");
    sj.add("| Буква | Неравномерный код | Код Шеннона-Фано | Код Хаффмана |");
    sj.add("|----------|:-------------|:-------------|:-------------|");

    Comparator<Entry<Character, String>> c = Comparator.comparing(e -> e.getValue().length());
    c = c.thenComparing(e -> e.getValue());
    var keys = uneven.entrySet()
        .stream()
        .sorted(c)
        .map(Entry::getKey)
        .collect(Collectors.toList());

    keys.forEach(k -> sj.add(String.format("| %s | %s | %s | %s |", k, uneven.get(k), fano.get(k), huffman.get(k))));

    return sj.toString();
  }

  public static String formatFlatTable(Map<String, String> map) {

    String l1, l2, l3;
    var keys = map.keySet().stream().collect(Collectors.toList());
    var values = keys.stream().map(map::get).collect(Collectors.toList());
    l1 = "| " + String.join(" | ", keys) + " |";

    l2 = "|" + new String(new char[keys.size()]).replace("\0", " --- |");
    l3 = "| " + String.join(" | ", values) + " |";

    return l1 + "\n" + l2 + "\n" + l3;
  }

}
