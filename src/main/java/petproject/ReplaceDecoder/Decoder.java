
package petproject.ReplaceDecoder;

import static java.util.Map.entry;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.Scanner;
import java.util.StringJoiner;

public class Decoder {

  Map<String, Double> trigrams;

  public Decoder() {
    trigrams = loadTrigrams();

  }

  public String decode(String line) {
    var only_letters = line
        .replace(".", "")
        .replace(",", "")
        .replace("\"", "")
        .replace("!", "")
        .replace("?", "");
    var words = Arrays.asList(only_letters.split(" "));

    only_letters = only_letters.replace(" ", "");

    List<Character> letters = new ArrayList<>();
    for (int i = 0; i < only_letters.length(); i++)
      letters.add(only_letters.charAt(i));

    var grouped_letters = letters.stream()
        .collect(Collectors.groupingBy(Character::charValue, Collectors.counting()));

    System.out.println(grouped_letters);

    var alph1 = grouped_letters.entrySet().stream()
        .sorted(Comparator.comparing(Entry::getValue, Comparator.reverseOrder()))
        .map(Entry::getKey)
        .collect(Collectors.toList());

    var alph2 = letter_chance.entrySet().stream()
        .sorted(Comparator.comparing(Entry::getValue, Comparator.reverseOrder()))
        .map(Entry::getKey)
        .collect(Collectors.toList());

    System.out.println(alph1);
    System.out.println(alph2);

    Map<Character, Character> primary_key = genKey(alph1, alph2, 0, 0);

    double current, prev;
    current = prev = getRating(decode(line, primary_key));
    int uplimit = 20;

    while (true) {
      for (int i = 0; i < alph1.size(); i++) {
        int step = 0;
        while (step + i < alph2.size()) {
          var key = genKey(alph1, alph2, i, step);
          var decoded = decode(line, key);
          current = getRating(decoded);
          if (current > prev) {
            primary_key = key;
            prev = current;
            Collections.swap(alph2, i, i + step);
            System.out.println(decoded);
            uplimit--;
            if (uplimit == 0) {
              return null;
            }
            break;
          } else
            step++;
        }
      }
    }
  }

  private Map<Character, Character> genKey(List<Character> alph1, List<Character> alph2, int i, int step) {
    // alph2 - декодируемый alph1 - желанный
    Map<Character, Character> key = new HashMap<>();
    List<Character> copy = new ArrayList<>(alph2);

    Collections.swap(copy, i, i + step);

    var iterator = copy.iterator();
    key = new HashMap<>();
    for (var l : alph1)
      key.put(l, iterator.next());
    return key;
  }

  private String decode(String line, Map<Character, Character> key) {
    char[] result = new char[line.length()];

    for (int i = 0; i < line.length(); i++) {
      Character symbol = line.charAt(i);
      if (Character.isLetterOrDigit(symbol)) {
        result[i] = key.get(symbol);
      } else {
        result[i] = symbol;
      }
    }

    return String.valueOf(result);
  }

  private Map<String, Double> loadTrigrams() {
    System.out.println("Чтение триграмм...");

    Scanner s;
    try {
      s = new Scanner(new File("trigrams.txt"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    }
    StringJoiner sj = new StringJoiner(" ");
    while (s.hasNextLine()) {
      sj.add(s.nextLine());
    }
    s.close();
    String[] parts = sj.toString().split(" ");
    Map<String, Double> result = new HashMap<>();

    for (int i = 0; i < parts.length / 2; i += 2) {
      result.put(parts[i], Double.parseDouble(parts[i + 1]));
    }

    System.out.println("Считано: " + result.size());

    return result;
  }

  public double getRating(String line) {
    String[] sublines = new String[line.length() - 2];
    line = line.toLowerCase();
    line = line.replace(" ", "_");

    double result = 0;

    for (int i = 0; i < line.length() - 2; i++)
      sublines[i] = line.substring(i, i + 3);

    for (String subline : sublines) {
      Double r = trigrams.get(subline);
      if (r != null)
        result += r;
    }
    return result;
  }

  static final Map<Character, Double> letter_chance = Map.ofEntries(
      entry('А', 0.069),
      entry('Б', 0.013),
      entry('В', 0.038),
      entry('Г', 0.014),
      entry('Д', 0.024),
      entry('Е', 0.071),
      entry('Ё', 0.071),
      entry('Ж', 0.007),
      entry('З', 0.016),
      entry('И', 0.064),
      entry('Й', 0.010),
      entry('К', 0.029),
      entry('Л', 0.039),
      entry('М', 0.027),
      entry('Н', 0.057),
      entry('О', 0.094),
      entry('П', 0.026),
      entry('Р', 0.042),
      entry('С', 0.046),
      entry('Т', 0.054),
      entry('У', 0.023),
      entry('Ф', 0.003),
      entry('Х', 0.008),
      entry('Ц', 0.005),
      entry('Ч', 0.012),
      entry('Ш', 0.006),
      entry('Щ', 0.004),
      entry('Ъ', 0.001),
      entry('Ы', 0.015),
      entry('Ь', 0.013),
      entry('Э', 0.002),
      entry('Ю', 0.005),
      entry('Я', 0.017));
}
