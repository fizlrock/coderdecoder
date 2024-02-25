
package petproject.LabExecutor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ArithmeticCoding {

  public static String code(String line) {
    // Добавить к строке символ конца
    var chances = CodeTableBuilder.calcProbability(line);
    var ranges = calcRanges(chances);

    var steps = new ArrayList<CodingStep>();

    double min = 0, max = 1, delta;

    for (int i = 0; i < line.length(); i++) {
      var letter = line.charAt(i);
      delta = max - min;
      max = min + delta * ranges.get(letter).stop();
      min = min + delta * ranges.get(letter).start();
      steps.add(new CodingStep(letter, delta, min, max));
    }

    steps.forEach(System.out::println);

    return line;
  }

  /**
   * Выбор числа из заданного диапазона с наименьшим числом цифр
   * 
   * @param i
   * @return
   */
  public static double selectNumber(Range i) {
    return 0;
  }

  public static Map<Character, Range> calcRanges(Map<Character, Double> chances) {

    var letters = chances.entrySet().stream()
        .sorted(Comparator.comparing(e -> e.getValue(), Comparator.reverseOrder()))
        .collect(Collectors.toList());

    var result = new HashMap<Character, Range>();

    double last_border = 0;

    for (int i = 0; i < letters.size(); i++) {
      var f = letters.get(i);
      result.put(
          f.getKey(),
          new Range(last_border, last_border + f.getValue()));
      last_border += f.getValue();
    }

    if (last_border - 1 > 0.0001)
      throw new RuntimeException("Ошибка вычисления интервалов. Сумма вероятностей не равна одному: " + last_border);

    return result;
  }

  static record Range(double start, double stop) {
  }

  static record CodingStep(
      char letter,
      double delta,
      double min,
      double max) {
  }

  static record CodeReport(
      String input_line,
      String result,
      HashMap<Character, Double> probability,
      List<CodingStep> steps,
      List<Range> ranges) {
  }

}
