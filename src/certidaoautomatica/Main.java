package certidaoautomatica;

import javax.swing.JOptionPane;
import screens.InitAssistant;

public class Main {

    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Impossível inciar o programa.");
        } catch (InstantiationException ex) {
            JOptionPane.showMessageDialog(null, "Impossível inciar o programa.");
        } catch (IllegalAccessException ex) {
            JOptionPane.showMessageDialog(null, "Impossível inciar o programa.");
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            JOptionPane.showMessageDialog(null, "Impossível inciar o programa.");
        }
        new InitAssistant();
    }

}
