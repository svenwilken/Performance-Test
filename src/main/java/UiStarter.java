import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import network.NetworkInitializer;
import ui.TestUIPanel;

public class UiStarter {
  public static void main (String args[]){
    UiStarter uiStarter = new UiStarter();
    NetworkInitializer initializer = new NetworkInitializer(10, 0, 0 ,false);
    uiStarter.startupFrame(initializer);
  }


  public void startupFrame(NetworkInitializer initializer) {
    SwingUtilities.invokeLater(() -> {
      JFrame frame = new JFrame("Performance Test");
      frame.add(new TestUIPanel(initializer));
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
      frame.setSize(400, 400);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


      frame.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
          //initializer.deleteTestEntitys();
          e.getWindow().dispose();
        }
      });
    });
  }
}
