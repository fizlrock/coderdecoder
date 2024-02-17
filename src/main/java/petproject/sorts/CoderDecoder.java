
package petproject.sorts;

import static java.util.Map.entry;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class CoderDecoder {

  public CoderDecoder(Map<Character, String> codes_table) {
    character_code_table = codes_table;
    code_character_table = codes_table.entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

    sorted_codes = codes_table.entrySet().stream()
        .map(Entry::getValue)
        .sorted(Comparator.comparing(String::length))
        .collect(Collectors.toList());
  }

  private Map<Character, String> character_code_table;
  private Map<String, Character> code_character_table;
  private List<String> sorted_codes;

  public String code(String line) {

    StringBuilder sb = new StringBuilder();
    for (Character c : line.toCharArray()) {
      String code = character_code_table.get(c);
      if (code == null)
        throw new IllegalArgumentException("Таблица кодирования не содержит опеределения для символа " + c);

      sb.append(code);
    }

    return sb.toString();
  }

  public String decode(String line) {
    StringBuilder sb = new StringBuilder();

    int offset = 0;

    while (offset < line.length()) {
      boolean finded = false;
      for (String code : sorted_codes) {
        if (line.startsWith(code, offset)) {
          sb.append(code_character_table.get(code));
          offset += code.length();
          finded = true;
          break;
        }
      }
      if (!finded)
        throw new RuntimeException("Ошибка расшифровки сообщения");
    }

    return sb.toString();
  }

  public static CoderDecoder getDefaultShannonCoderDecoder() {
    return new CoderDecoder(defaultShannonCodes) {
      @Override
      public String code(String line) {
        line = line.replace('ё', 'е');
        line = line.replace('ъ', 'ь');
        line = line.toLowerCase();
        return super.code(line);
      }
    };
  }

  public static final Map<Character, String> defaultShannonCodes = Map.ofEntries(
      entry(' ', "000"),
      entry('о', "001"),
      entry('е', "0100"),
      entry('а', "0101"),
      entry('и', "0110"),
      entry('т', "0111"),
      entry('н', "1000"),
      entry('с', "1001"),
      entry('р', "10100"),
      entry('в', "10101"),
      entry('л', "10110"),
      entry('к', "10111"),
      entry('м', "11000"),
      entry('д', "110010"),
      entry('п', "110011"),
      entry('у', "110100"),
      entry('я', "110110"),
      entry('ы', "110111"),
      entry('з', "111000"),
      entry('ь', "111001"),
      entry('б', "111010"),
      entry('г', "111011"),
      entry('ч', "111100"),
      entry('й', "1111010"),
      entry('х', "1111011"),
      entry('ж', "1111100"),
      entry('ю', "1111101"),
      entry('ш', "11111100"),
      entry('ц', "11111101"),
      entry('щ', "11111110"),
      entry('э', "111111110"),
      entry('ф', "111111111"));
}
