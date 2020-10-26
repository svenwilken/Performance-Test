package network;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Graph representing Network Used to create adjacency matrix
 */
public class Graph {

  private static String filePath;
  private int numOfNodes;
  private boolean[][] isSetMatrix;

  public Graph(int numOfNodes) {

    this.numOfNodes = numOfNodes;

    isSetMatrix = new boolean[numOfNodes][numOfNodes];
  }

  public static void setFilePath(String absolutePath) {
    filePath = absolutePath;
  }

  public void addEdge(int source, int destination) {

    isSetMatrix[source - 1][destination - 1] = true;
  }

  public void printMatrix() {
    for (int i = 0; i < numOfNodes; i++) {
      for (int j = 0; j < numOfNodes; j++) {
        // We only want to print the values of those positions that have been marked as set
        if (isSetMatrix[i][j]) {
          System.out.format("%8s", "1,");
        } else {
          System.out.format("%8s", "0,");
        }
      }
      System.out.println();
    }
  }

  public void writeMatrixToFile() throws IOException {
    File file = new File(filePath);
    FileWriter fileWriter = new FileWriter(file);
    PrintWriter printWriter = new PrintWriter(fileWriter);
    for (int i = 0; i < numOfNodes; i++) {
      for (int j = 0; j < numOfNodes; j++) {
        // We only want to print the values of those positions that have been marked as set
        if (isSetMatrix[i][j]) {
          printWriter.print("1,");
        } else {
          printWriter.print("0,");
        }
      }
      printWriter.println();
    }
    printWriter.close();
    java.awt.Desktop.getDesktop().edit(file);
  }

}
