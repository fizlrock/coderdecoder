
package petproject;

import java.util.ArrayList;

import petproject.LabExecutor.ArithmeticCoding;
import petproject.LabExecutor.Lab2Executor;
import petproject.LabExecutor.Lab2Executor.LabReport;
import petproject.ReplaceDecoder.Decoder;

/**
 * Hello world!
 *
 */
public class App {

  public static void main(String[] args) {
  }

  static void executeLab2() {

    var labs = Lab2Executor.loadLabs();
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

  }
}
