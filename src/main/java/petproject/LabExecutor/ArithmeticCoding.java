
package petproject.LabExecutor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ArithmeticCoding {

  public static CodeReport code(String line) {
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
    String result = getSignificantFigures(selectNumber(new Range(min, max)));

    return new CodeReport(line, result, chances, steps, ranges);
  }

  /**
   * Выбор числа из заданного диапазона с наименьшим кол-вом цифр
   * 
   * @param i
   * @return
   */
  public static double selectNumber(Range i) {

    int n = 0;
    String text_start = getSignificantFigures(i.start());
    String text_stop = getSignificantFigures(i.stop());
    while (text_start.charAt(n) == text_stop.charAt(n))
      n++;

    String text_base = text_start.substring(0, n);
    double base = Double.parseDouble("0." + text_base);
    n++;

    int limit = 5;

    while (!i.contains(base)) {
      System.out.println(base);
      base += Math.pow(10, -n);
      if (base > i.stop()) {
        base -= Math.pow(1, -n);
        n++;
      }

      limit--;
      if (limit == 0)
        break;
    }
    System.out.println(base);

    return base;
  }

  /**
   * Получение значащих цифр из числа
   * 0.0131 -> 0131
   * 
   * @param num
   * @return
   */
  private static String getSignificantFigures(double num) {
    var text = Double.toString(num);
    text = text.split("\\.")[1];
    return text;
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

  public static record Range(double start, double stop) {
    public boolean contains(double n) {
      return (n >= start) && (n <= stop);
    }
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
      Map<Character, Double> probability,
      List<CodingStep> steps,
      Map<Character, Range> ranges) {
  }

}
