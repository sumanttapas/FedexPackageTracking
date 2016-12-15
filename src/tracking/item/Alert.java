package tracking.item;
import java.awt.EventQueue;

import javax.swing.JOptionPane;

public class Alert
{

    public static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage,  titleBar, JOptionPane.INFORMATION_MESSAGE);
    }
}