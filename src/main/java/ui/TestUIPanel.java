package ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import test.TestInitializer;

public class TestUIPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private TestInitializer initializer;


    public TestUIPanel(TestInitializer initializer) {
      this.initializer=initializer;
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

      JSpinner spinner = getjSpinner(initializer, gbc);
      JLabel label = new JLabel("Type in the number of test entities");
      add(label, gbc);
      add(spinner, gbc);

      JPanel panel = panelForTestInitialisation(initializer, gbc);
      add(panel, gbc);

      JPanel panel2 = panelForCleanup(initializer, gbc);
      add(panel2, gbc);

    }

  private JSpinner getjSpinner(TestInitializer initializer, GridBagConstraints gbc) {
    SpinnerModel model = new SpinnerNumberModel(10, 2, Integer.MAX_VALUE, 1);
    initializer.setN(10);
    JSpinner spinner = new JSpinner(model);
    spinner.addChangeListener(e -> {
      initializer.setN((int)spinner.getValue());
      System.out.println("N set to:"+(int)spinner.getValue());
    });
    gbc.weighty = 1;
    return spinner;
  }

  private JPanel panelForCleanup(TestInitializer initializer, GridBagConstraints gbc) {
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

  private JPanel panelForTestInitialisation(TestInitializer initializer, GridBagConstraints gbc) {
    JPanel panel = new JPanel(new GridBagLayout());

    JButton startStopButton = new JButton("Create Test");
    startStopButton.addActionListener(e -> {
      try {
       initializer.createRepositorysAndMapping();
      } catch (Exception ex) {
       ex.printStackTrace();
      }
    });
    panel.add(startStopButton, gbc);
    return panel;
  }


}
