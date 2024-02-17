
package petproject.sorts;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Lab2Executor {

  public Lab2Executor() {
    // Подгрузка данных из csv файла
    //
    // Сохранение результатов по папкам
    // output
    // var1
    // huffman_graph.svg
    // report.txt
    // var2
    // ...

  }

  public static LabReport executeLab(VersionOfLab lab) {
    // Добавить декодер для не равномерного кода
    String fanoDecoded, unevenDecoded;
    fanoDecoded = CoderDecoder.getDefaultShannonCoderDecoder().decode(lab.line2);
    unevenDecoded = "";

    Map<Character, String> unevenTable, fanoTable, huffmanTable;

    unevenTable = CodeTableBuilder.buildTableUneven(lab.line3);
    fanoTable = CodeTableBuilder.buildTableFano(lab.line3);

    var tree = CodeTableBuilder.buildHuffmanTree(lab.line3);
    String dotgraph = CodeTableBuilder.formatTree(tree);

    huffmanTable = CodeTableBuilder.buildHuffmanTable(tree);

    return new LabReport(lab, unevenDecoded, fanoDecoded, unevenTable, fanoTable, huffmanTable, dotgraph);

  }

  public static void writeReportToDir(LabReport report, String outputDir) {
    var root_dir = new File(outputDir);
    if (!root_dir.exists())
      throw new RuntimeException("Папка для результатов не существует");
    var working_dir = new File(root_dir, "Version №" + report.version.versionNumber);

    working_dir.mkdir();

  }

  /**
   * Структуры для описания варианта работы.
   * line1 - строка для декодирования неравномерным кодом. (да, название кривое)
   * line2 - строка для декодирования методом Шеннона-Фано
   * line3 - строка для построение таблиц неравномерного кода, Шеннона-Фано,
   * Хаффмана
   * 
   */
  public static record VersionOfLab(
      int versionNumber,
      String line1,
      String line2,
      String line3,
      int number) {
  }

  public static record LabReport(
      VersionOfLab version,
      String unevenDecoded,
      String fanoDecoded,
      Map<Character, String> unevenTable,
      Map<Character, String> fanoTable,
      Map<Character, String> huffmanTable,
      String dotgraph) {
  }

}
