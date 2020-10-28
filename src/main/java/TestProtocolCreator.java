import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import org.apache.commons.io.filefilter.WildcardFileFilter;

public class TestProtocolCreator {

  public static void main(String args[]) throws IOException {
    File file = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "TestProtocol.txt");
    FileWriter fileWriter = new FileWriter(file);
    PrintWriter printWriter = new PrintWriter(fileWriter);
    printWriter.println("Test Protocol");
    File dir = new File(System.getProperty("user.dir"));
    FileFilter fileFilter = new WildcardFileFilter("matrix_*.txt");
    File[] files = dir.listFiles(fileFilter);
    Scanner myReader;
    for (int i = 0; i < files.length; i++) {
      File matrix = new File(String.valueOf(files[i]));
      myReader = new Scanner(matrix);
      printWriter.println("N:"+myReader.nextLine()+", C:"+myReader.nextLine()+", Lower-Bound:"+myReader.nextLine()+", Loop:"+myReader.nextLine()+", Calc-Time:" +myReader.nextLine());
      myReader.close();
    }
    printWriter.close();
  }
}
