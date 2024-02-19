
package petproject.LabExecutor;

import java.util.HashMap;
import java.util.Map;

public class UniversalCodes {
  public static String getEliasGammaCode(int value) {
    if (value < 0)
      throw new IllegalArgumentException("Гамма код Элиаса можно составить только для положительного числа");

    String result = Integer.toString(value, 2);
    var zeros = new String(new char[result.length() - 1]).replace("\0", " ");
    result = zeros + result;

    return result;
  }

  public static String getEliasDeltaCode(int value) {
    if (value < 0)
      throw new IllegalArgumentException("Дельта код Элиаса можно составить только для положительного числа");

    String bitvalue = Integer.toString(value, 2);
    return getEliasGammaCode(bitvalue.length()) + bitvalue.substring(1);
  }

  public static String getEliasOmegaCode(int value) {
    String result = "0";

    while (value != 1) {
      var bits = Integer.toString(value, 2);
      result = bits + result;
      value = bits.length() - 1;
    }
    return result;
  }

  public static Map<String, String> getCodes(int value) {
    Map<String, String> result = new HashMap<>();
    result.put("Гамма-код", getEliasGammaCode(value));
    result.put("Дельта-код", getEliasDeltaCode(value));
    result.put("Омега-код", getEliasOmegaCode(value));

    return result;
  }
}
