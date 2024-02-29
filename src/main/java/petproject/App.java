
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
    var d = new Decoder();
    d.decode("SP R4UMBI ISOYDS S LMESRB, E13SRB, PM5NEB, M Q UBZIMJ SKBTDSF1TIPSU TO45BPNN EBONDSW NRBB, S ASREN3B.");
  }

  static void executeLab2() {

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

  }
}
