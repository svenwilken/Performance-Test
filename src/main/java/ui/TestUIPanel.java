package ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import network.Graph;
import network.NetworkInitializer;


public class TestUIPanel extends JPanel {

  private static final long serialVersionUID = 1L;

  private NetworkInitializer initializer;

  private TextFileChooser textFileChooser;
  private JButton printMatrix = new JButton("Save Matrix");
//    private JButton saveMatrix = new JButton("Choose Matrix");


  public TestUIPanel(NetworkInitializer initializer) {
    this.initializer = initializer;
    this.textFileChooser = new TextFileChooser();
    setBorder(new EmptyBorder(10, 10, 10, 10));
    setLayout(new GridBagLayout());

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.anchor = GridBagConstraints.NORTH;

    add(new JLabel(
            "<html>"
                + "<h1><strong><i>Performance Test</i></strong></h1><hr>"
                + "</html>"),
        gbc);

    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    JComboBox jComboBox = getjComboBox(initializer);
    JSpinner spinner = getjSpinnerForN(initializer, gbc);
    JSpinner spinner2 = getjSpinnerForC(initializer, gbc, jComboBox);
    JSpinner spinner2_2 = getjSpinnerForC_lowerBound(initializer, gbc, jComboBox);
    JLabel label = new JLabel("Number of test entities:");
    JLabel label2 = new JLabel("Network complexity C:");
    JLabel label2_2 = new JLabel("Lower bound of C:");
    JLabel label3 = new JLabel("Loops in network allowed:");

    JPanel jPanelForTime = new JPanel();
    jPanelForTime.setLayout(new GridLayout(0, 2));
    JLabel labelForTime = new JLabel("Calc Time:");
    JSpinner spinnerForTime = getjSpinnerForTime(initializer, gbc, jComboBox);
    jPanelForTime.add(labelForTime);
    jPanelForTime.add(spinnerForTime);

    JPanel jPanel = new JPanel();
    jPanel.setLayout(new GridLayout(0, 2));
    jPanel.add(label);
    jPanel.add(spinner);
    jPanel.add(label2);
    jPanel.add(spinner2);
    jPanel.add(label2_2);
    jPanel.add(spinner2_2);
    jPanel.add(label3);
    jPanel.add(jComboBox);
    add(jPanel, gbc);


    JPanel panel = panelForTestInitialisation(initializer, gbc);
    add(panel, gbc);

    JPanel panel2 = panelForCleanup(initializer, gbc);
    add(panel2, gbc);

    add(jPanelForTime, gbc);

    JPanel panel3 = getPanelForMatrixCreation(gbc);
    add(panel3, gbc);
  }

  private JSpinner getjSpinnerForC_lowerBound(NetworkInitializer initializer,
      GridBagConstraints gbc, JComboBox jComboBox) {
    SpinnerModel model = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
    initializer.setC_lowerBound(0);
    JSpinner spinner = new JSpinner(model);
    spinner.addChangeListener(e -> {
      initializer.setC_lowerBound((int) spinner.getValue());
      System.out.println("C_lowerBound set to:" + (int) spinner.getValue());
    });
    gbc.weighty = 1;
    return spinner;
  }

 private JSpinner getjSpinnerForTime(NetworkInitializer initializer,
      GridBagConstraints gbc, JComboBox jComboBox) {
    SpinnerModel model = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
    JSpinner spinner = new JSpinner(model);
    spinner.addChangeListener(e -> {
      initializer.setCalcTime((int) spinner.getValue());
      System.out.println("Calc Time is set to:" + (int) spinner.getValue());
    });
    gbc.weighty = 1;
    return spinner;
  }

  private JPanel getPanelForMatrixCreation(GridBagConstraints gbc) {
    JPanel panel3 = new JPanel(new GridBagLayout());
    this.printMatrix.setEnabled(false);
    panel3.add(this.printMatrix, gbc);
    this.printMatrix.addActionListener(e -> {
//          startupFileChooser();
          Graph.setFilePath(
              System.getProperty("user.dir") + System.getProperty("file.separator") + "matrix_N"
                  + initializer.getN()
                  + "_C" + initializer.getC()
                  + "_LowerBound" + initializer.getC_lowerBound()
                  + ".txt");
          try {
            initializer.getGraph().writeMatrixToFile();
          } catch (IOException ex) {
            ex.printStackTrace();
          }
          ;
        }
    );
    return panel3;
  }

  public void startupFileChooser() {

    if (this.textFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
      Graph.setFilePath(this.textFileChooser.getSelectedFile().getAbsolutePath() + ".txt");
    } else {
      Graph.setFilePath(
          System.getProperty("user.dir") + System.getProperty("file.separator") + "matrix_N"
              + initializer.getN()
              + "_C" + initializer.getC()
              + "_LowerBound" + initializer.getC_lowerBound()
              + "_Loop" + initializer.isLoop()
              + ".txt");
    }
  }


  private JComboBox getjComboBox(NetworkInitializer initializer) {
    JComboBox jComboBox = new JComboBox(new Boolean[]{false, true});
    jComboBox.setEnabled(false);
    jComboBox.addActionListener(e -> {
      initializer.setLoop((boolean) jComboBox.getSelectedItem());
      System.out.println("LOOP set to:" + (boolean) jComboBox.getSelectedItem());
    });
    return jComboBox;
  }

  private JSpinner getjSpinnerForN(NetworkInitializer initializer, GridBagConstraints gbc) {
    SpinnerModel model = new SpinnerNumberModel(10, 2, Integer.MAX_VALUE, 1);
    initializer.setN(10);
    JSpinner spinner = new JSpinner(model);
    spinner.addChangeListener(e -> {
      initializer.setN((int) spinner.getValue());
      System.out.println("N set to:" + (int) spinner.getValue());
    });
    gbc.weighty = 1;
    return spinner;
  }

  private JSpinner getjSpinnerForC(NetworkInitializer initializer, GridBagConstraints gbc,
      JComboBox jComboBox) {
    SpinnerModel model = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
    initializer.setC(0);
    JSpinner spinner = new JSpinner(model);
    spinner.addChangeListener(e -> {
      initializer.setC((int) spinner.getValue());
      jComboBox.setEnabled(initializer.getC() != 0);
      System.out.println("C set to:" + (int) spinner.getValue());
    });
    gbc.weighty = 1;
    return spinner;
  }

  private JPanel panelForCleanup(NetworkInitializer initializer, GridBagConstraints gbc) {
    JPanel panel = new JPanel(new GridBagLayout());

    JButton startStopButton = new JButton("Delete Test Entities");
    startStopButton.addActionListener(e -> {
      try {
        initializer.deleteTestEntitys();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    });
    panel.add(startStopButton, gbc);
    return panel;
  }

  private JPanel panelForTestInitialisation(NetworkInitializer initializer,
      GridBagConstraints gbc) {
    JPanel panel = new JPanel(new GridBagLayout());

    JButton startStopButton = new JButton("Create Test");
    startStopButton.addActionListener(e -> {
      try {
        initializer.createNetwork();
        this.printMatrix.setEnabled(true);

      } catch (Exception ex) {
        ex.printStackTrace();
      }
    });
    panel.add(startStopButton, gbc);
    return panel;
  }


}
