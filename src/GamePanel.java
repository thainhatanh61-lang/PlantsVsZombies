import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(100, 180, 100));
        g.fillRect(0, 0, 900, 600);
        g.setColor(new Color(0, 100, 0));
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 9; j++) {
                g.drawRect(80 + j * 90, 80 + i * 100, 90, 100);
            }
        }
    }
}