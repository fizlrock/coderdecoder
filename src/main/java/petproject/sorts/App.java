package petproject.sorts;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class App {
  public static void main(String[] args) {
    String line1, line2, line3;
    line1 = "1010010010100000101100111001010000101110010101000110001010100110000000100101000001100010001110100100111100100101100110011000100001011000";
    line2 = "01101010101011000000101000101100110010100101001100100110101101001110110";
    line3 = "НО ВРЕДЕН СЕВЕР ДЛЯ МЕНЯ. КОГДА ЖЕ ЮНОСТИ МЯТЕЖНОЙ";
    var myv = new Lab2Executor.VersionOfLab(1, line1, line2, line3, 5);
    var report = Lab2Executor.executeLab(myv);
    Lab2Executor.writeReportToDir(report, "output");

    
  }
}
