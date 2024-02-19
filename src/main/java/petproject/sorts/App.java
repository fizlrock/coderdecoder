package petproject.sorts;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static petproject.sorts.Lab2Executor.LabReport;

/**
 * Hello world!
 *
 */
public class App {
  public static void main(String[] args) {

    var labs = Lab2Executor.loadLabs();
    // labs.forEach(System.out::println);;
    var reports = new ArrayList<LabReport>();
    for (var lab : labs) {
      try {
        reports.add(Lab2Executor.executeLab(lab));
      } catch (Exception e) {
        System.out.println("Ошибка выполнение варианта: " + lab.versionNumber());
        e.printStackTrace();
      }
    }

    Lab2Executor.writeReports(reports, "output");
    reports.stream().map(LabReport::unevenDecoded).forEach(System.out::println);;


  }
}
