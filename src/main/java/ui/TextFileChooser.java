package ui;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * Custom file chooser to choose the DB the application is using.
 *
 * @author dsiemens
 */
public class TextFileChooser extends JFileChooser {

  private static final long serialVersionUID = 1L;
  private final String fileEnding = ".txt";
  private final String fileDescription = "Text File";


  private FileFilter sqlightFilter;

  /**
   * Constructor initializing file chooser.
   */
  public TextFileChooser() {
    this.initializeFilter();
    this.setSelectedFile(new File("adjacency_matrix"));
    this.setDialogTitle("Choose your Text Directory");
    this.setFileFilter(this.sqlightFilter);
    this.setFileSelectionMode(JFileChooser.FILES_ONLY);
    this.setAcceptAllFileFilterUsed(false);
  }

  private void initializeFilter() {
    this.sqlightFilter = new FileFilter() {
      public String getDescription() {
        return String.format("%s (*%s)", fileDescription, fileEnding);
      }

      public boolean accept(File f) {
        if (f.isDirectory()) {
          return true;
        } else {
          String filename = f.getName().toLowerCase().trim();
          return filename.endsWith(fileEnding);
        }
      }
    };
  }
}
