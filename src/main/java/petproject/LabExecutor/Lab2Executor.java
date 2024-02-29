
package petproject.LabExecutor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringJoiner;

public class Lab2Executor {

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

  public static List<VersionOfLab> loadLabs() {
    var input_file = new File("input_data");
    List<VersionOfLab> labs = new ArrayList<>();

    try (Scanner s = new Scanner(input_file)) {
      int line_counter = 0;
      while (s.hasNextLine()) {
        String line = s.nextLine();
        line_counter++;

        try {
          String[] parts = line.split("&");
          int version_num = Integer.parseInt(parts[0]);
          int magic_num = Integer.parseInt(parts[4]);
          var lab = new VersionOfLab(version_num, parts[1], parts[2], parts[3], magic_num);
          labs.add(lab);
        } catch (Exception e) {
          System.out.println(String.format("Ошибка чтения строки %d", line_counter));
          e.printStackTrace();
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    return labs;
  }

  public static LabReport executeLab(VersionOfLab lab) {
    // Добавить декодер для не равномерного кода
    String fanoDecoded, unevenDecoded;
    try {
      fanoDecoded = CoderDecoder.getDefaultShannonCoderDecoder().decode(lab.line2);
    } catch (Exception e) {
      fanoDecoded = "тут произошла ошибка, декоруйте сами";
    }
    unevenDecoded = CoderDecoder.decodeUneven(lab.line1);

    Map<Character, String> unevenTable, fanoTable, huffmanTable;

    unevenTable = CodeTableBuilder.buildTableUneven(lab.line3);
    fanoTable = CodeTableBuilder.buildTableFano(lab.line3);

    var tree = CodeTableBuilder.buildHuffmanTree(lab.line3);
    String dotgraph = CodeTableBuilder.formatTree(tree);

    huffmanTable = CodeTableBuilder.buildHuffmanTable(tree);

    return new LabReport(lab, unevenDecoded, fanoDecoded, unevenTable, fanoTable, huffmanTable, dotgraph);

  }

  public static void writeReports(List<LabReport> reports, String outputDir) {

    var root_dir = new File(outputDir);
    if (!root_dir.exists())
      throw new RuntimeException("Папка для результатов не существует");

    StringJoiner sj = new StringJoiner("\n");

    reports.forEach(r -> sj.add(formatReport(r)));
    writeString(Paths.get(root_dir.getPath(), "reports.txt"), sj.toString());

    for (var rep : reports) {
      var svg_graph_file = Paths.get(root_dir.getPath(),
          "graph" + rep.version.versionNumber + ".svg");
      writeString(svg_graph_file, compileGraph(rep.dotgraph));
    }
  }

  public static void writeReportToDir(LabReport report, String outputDir) {
    var root_dir = new File(outputDir);
    if (!root_dir.exists())
      throw new RuntimeException("Папка для результатов не существует");
    var working_dir = new File(root_dir, "Version" + report.version.versionNumber);
    working_dir.mkdirs();

    var svg_graph_file = Paths.get(working_dir.getPath(), "graph.svg");
    writeString(svg_graph_file, compileGraph(report.dotgraph));

    writeString(Paths.get(working_dir.getPath(), "report.txt"), formatReport(report));
  }

  private static String formatReport(LabReport report) {

    StringJoiner mdReport = new StringJoiner("\n");
    mdReport.add("## Вариант №" + report.version.versionNumber);
    mdReport.add("### Задание 1.1 Раскодировать фразу A (неравномерным методом).");
    mdReport.add("Исходная строка: " + report.version.line1);
    mdReport.add("Расшифровка: " + CoderDecoder.decodeUneven(report.version.line1));

    mdReport.add("### Задание 1.2 Раскодировать фразу B (методом Шеннона-Фано).");
    mdReport.add("Исходная строка: " + report.version.line2);
    mdReport.add("Расшифровка: " + report.fanoDecoded);

    mdReport.add(
        "### Задание 2. Для символов фразы C построить таблицы неравномерного кода, кода Шеннона-Фано и кода Хаффмана.");
    mdReport.add("Исходная строка: " + report.version.line3 + "\n");
    var probability = CodeTableBuilder.calcProbability(report.version().line3);
    mdReport.add(MarkdownFormater.formatTables(report.unevenTable, report.fanoTable, report.huffmanTable, probability));

    mdReport.add("\n\n\n");
    mdReport.add(
        "### Задание 3. Расчет энтропии, символов на букву и среднюю информацию");
    mdReport.add(MarkdownFormater.formatEntropyFormul(probability));
    mdReport.add(MarkdownFormater.formatKFormul(report.fanoTable, probability, "фано"));
    mdReport.add(MarkdownFormater.formatKFormul(report.huffmanTable, probability, "хаффман"));
    mdReport.add(MarkdownFormater.formatKFormul(report.unevenTable, probability, "неравномерный код"));

    
    mdReport.add("### Задание 4. Построить гамма-код, дельта-код и омега-код Элиаса для чисел из таблицы с заданием.");
    mdReport.add(MarkdownFormater.formatFlatTable(UniversalCodes.getCodes(report.version.number)));

    return mdReport.toString();
  }

  private static String compileGraph(String dotgraph) {
    File temp_file;
    try {
      temp_file = File.createTempFile("svg", null);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
    writeString(temp_file, dotgraph);

    String svg_graph = "";
    try {
      var proc = Runtime.getRuntime().exec(new String[] { "dot", "-Tsvg", temp_file.getPath().toString(), ">" });
      BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
      StringBuilder sb = new StringBuilder();
      String line = null;
      while ((line = in.readLine()) != null) {
        sb.append(line);
      }
      svg_graph = sb.toString();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return svg_graph;
  }

  private static void writeString(Path p, String line) {
    writeString(p.toFile(), line);
  }

  private static void writeString(File f, String line) {
    try (PrintWriter pw = new PrintWriter(f)) {
      pw.write(line);
      pw.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

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

}
