
package petproject.LabExecutor;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;
import java.util.stream.Collector;
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
      Map<Character, String> huffman, Map<Character, Double> probability) {

    StringJoiner sj = new StringJoiner("\n");
    sj.add("| Буква | $p_i$ | Неравномерный код | $k_i$ | Код Шеннона-Фано | $k_i$ | Код Хаффмана | $k_i$ |");
    sj.add("|---|:---|:---|:---|:---|:---|:---|:---|");

    Comparator<Entry<Character, String>> c = Comparator.comparing(e -> e.getValue().length());
    c = c.thenComparing(e -> e.getValue());
    var keys = uneven.entrySet()
        .stream()
        .sorted(c)
        .map(Entry::getKey)
        .collect(Collectors.toList());

    keys.forEach(k -> sj.add(String.format("| %s | %.2f | %s | %d | %s | %d | %s | %d |",
        k, probability.get(k),
        uneven.get(k), uneven.get(k).length(),
        fano.get(k), fano.get(k).length(),
        huffman.get(k), huffman.get(k).length())));

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

  public static String formatEntropyFormul(Map<Character, Double> chances) {
    List<Double> probabilites = chances.entrySet().stream()
        .map(Entry::getValue)
        .collect(Collectors.toList());

    Double result = Double.valueOf(0);
    for (Double p : probabilites)
      result += p * (Math.log(p) / Math.log(2));
    result = -result;

    var grouped_chances = probabilites.stream().collect(Collectors.groupingBy(e -> e));

    StringJoiner elements = new StringJoiner("+ \\\\");

    grouped_chances.forEach((chance, chance_list) -> {
      if (chance_list.size() == 1)
        elements.add(String.format("%.2f log_2 %.2f", chance, chance));
      else
        elements.add(String.format("%d * %.2f log_2 %.2f", chance_list.size(), chance, chance));
    });

    String formated_element = "\\begin{gather}" +
        elements.toString() +
        "\\end{gather}";

    StringBuffer sb = new StringBuffer();
    sb.append("$$");
    sb.append("E = -\\sum_i{p_ilog_2{p_i}} = -(");
    sb.append(formated_element);
    sb.append(") \\approx");
    sb.append(String.format("%.2f", result));
    sb.append("$$");
    return sb.toString();
  }

  public static String formatKFormul(Map<Character, String> codes, Map<Character, Double> chances, String name) {

    var fs = chances.entrySet().stream().limit(2).collect(Collectors.toList());
    var first = fs.get(0);
    var second = fs.get(1);

    String f1 = String.format("%.2f * %d", first.getValue(), codes.get(first.getKey()).length());
    String f2 = String.format("%.2f * %d", second.getValue(), codes.get(second.getKey()).length());

    double result = 0;
    for (var entry : chances.entrySet()) {
      var letter = entry.getKey();
      var prob = entry.getValue();
      result += prob * codes.get(letter).length();
    }

    StringBuffer sb = new StringBuffer();
    sb.append("$$");
    sb.append(String.format("k_{%s} = \\sum_{i=1}{k_ip_i} =",name));
    sb.append(f1);
    sb.append(" + ... + ");
    sb.append(f2);
    sb.append(" \\approx");
    sb.append(String.format("%.2f", result));
    sb.append("$$");
    return sb.toString();

  }

}
