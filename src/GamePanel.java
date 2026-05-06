import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(100, 180, 100));
        g.fillRect(0, 0, 900, 600);
        g.setColor(new Color(0, 100, 0));
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 9; col++) {
                int x = 80 + col * 90;
                int y = 80 + row * 100;
                g.drawRect(x, y, 90, 100);
            }
        }
    }
}