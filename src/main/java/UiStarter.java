import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import test.TestInitializer;
import ui.TestUIPanel;

public class UiStarter {
  public static void main (String args[]){
    TestInitializer initializer = new TestInitializer(10);
    startupFrame(initializer);
  }


  private static void startupFrame(TestInitializer initializer) {
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
          initializer.deleteTestEntitys();
          e.getWindow().dispose();
        }
      });
    });
  }
}
