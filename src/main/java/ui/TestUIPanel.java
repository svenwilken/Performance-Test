package ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import network.NetworkInitializer;

public class TestUIPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private NetworkInitializer initializer;


    public TestUIPanel(NetworkInitializer initializer) {
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
      JComboBox jComboBox = getjComboBox(initializer);
      JSpinner spinner = getjSpinnerForN(initializer, gbc);
      JSpinner spinner2 = getjSpinnerForC(initializer, gbc, jComboBox);
      JLabel label = new JLabel("Number of test entities:");
      JLabel label2 = new JLabel("Network complexity:");
      JLabel label3 = new JLabel("Loops in network allowed:");

      JPanel jPanel = new JPanel();
      jPanel.setLayout(new GridLayout(0,2));
      jPanel.add(label);
      jPanel.add(spinner);
      jPanel.add(label2);
      jPanel.add(spinner2);
      jPanel.add(label3);
      jPanel.add(jComboBox);
      add(jPanel, gbc);
//      add(spinner, gbc);

      JPanel panel = panelForTestInitialisation(initializer, gbc);
      add(panel, gbc);

      JPanel panel2 = panelForCleanup(initializer, gbc);
      add(panel2, gbc);

    }

  private JComboBox getjComboBox(NetworkInitializer initializer) {
    JComboBox jComboBox = new JComboBox(new Boolean[]{false, true});
    jComboBox.setEnabled(false);
    jComboBox.addActionListener(e -> {
      initializer.setLoop((boolean)jComboBox.getSelectedItem());
      System.out.println("LOOP set to:"+(boolean)jComboBox.getSelectedItem());
    });
    return jComboBox;
  }

  private JSpinner getjSpinnerForN(NetworkInitializer initializer, GridBagConstraints gbc) {
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
  private JSpinner getjSpinnerForC(NetworkInitializer initializer, GridBagConstraints gbc,
      JComboBox jComboBox) {
    SpinnerModel model = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
    initializer.setC(0);
    JSpinner spinner = new JSpinner(model);
    spinner.addChangeListener(e -> {
      initializer.setC((int)spinner.getValue());
      jComboBox.setEnabled(initializer.getC()!=0);
      System.out.println("C set to:"+(int)spinner.getValue());
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

  private JPanel panelForTestInitialisation(NetworkInitializer initializer, GridBagConstraints gbc) {
    JPanel panel = new JPanel(new GridBagLayout());

    JButton startStopButton = new JButton("Create Test");
    startStopButton.addActionListener(e -> {
      try {
       initializer.createNetwork();
      } catch (Exception ex) {
       ex.printStackTrace();
      }
    });
    panel.add(startStopButton, gbc);
    return panel;
  }


}
