
package petproject.LabExecutor;

import static java.util.Map.entry;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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

  public static String decodeUneven(String line) {
    line = line.replace("000001", "00&fuck&1");
    line = line.replace("001", "00&1");
    var codes = Arrays.asList(line.split("&"));
    var result = codes.stream().map(unevenDecodeTable::get).collect(Collectors.joining());
    return result;
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

  public static final Map<String, String> unevenDecodeTable = Map.ofEntries(
      entry("fuck", " "),
      entry("100", "о"),
      entry("1000", "е"),
      entry("1100", "а"),
      entry("10000", "и"),
      entry("10100", "т"),
      entry("11000", "н"),
      entry("11100", "с"),
      entry("101000", "р"),
      entry("101100", "в"),
      entry("110000", "л"),
      entry("110100", "к"),
      entry("111000", "м"),
      entry("111100", "д"),
      entry("1010000", "п"),
      entry("1010100", "у"),
      entry("1011000", "я"),
      entry("1011100", "ы"),
      entry("1101000", "з"),
      entry("1101100", "ь"),
      entry("1110000", "б"),
      entry("1110100", "г"),
      entry("1111000", "ч"),
      entry("1111100", "й"),
      entry("10101000", "х"),
      entry("10101100", "ж"),
      entry("10110000", "ю"),
      entry("10110100", "ш"),
      entry("10111000", "ц"),
      entry("10111100", "щ"),
      entry("11010000", "э"),
      entry("11011000", "ф"));
  public static final Map<Character, String> unevenCodeTable = Map.ofEntries(
      entry(' ', "000"),
      entry('о', "100"),
      entry('е', "1000"),
      entry('а', "1100"),
      entry('и', "10000"),
      entry('т', "10100"),
      entry('н', "11000"),
      entry('с', "11100"),
      entry('р', "101000"),
      entry('в', "101100"),
      entry('л', "110000"),
      entry('к', "110100"),
      entry('м', "111000"),
      entry('д', "111100"),
      entry('п', "1010000"),
      entry('у', "1010100"),
      entry('я', "1011000"),
      entry('ы', "1011100"),
      entry('з', "1101000"),
      entry('ь', "1101100"),
      entry('б', "1110000"),
      entry('г', "1110100"),
      entry('ч', "1111000"),
      entry('й', "1111100"),
      entry('х', "10101000"),
      entry('ж', "10101100"),
      entry('ю', "10110000"),
      entry('ш', "10110100"),
      entry('ц', "10111000"),
      entry('щ', "10111100"),
      entry('э', "11010000"),
      entry('ф', "11011000"));
}
