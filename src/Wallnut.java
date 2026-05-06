import java.awt.*;
import java.util.List;

public class Wallnut extends Plant {
    public Wallnut(int x, int y) {
        super(x, y, 50, 400); // High health
    }
    @Override
    public void update(List<Zombie> zombies, List<Sun> suns) {
        // Wall-nut does nothing, just blocks zombies
    }
    @Override
    public void draw(Graphics g) {
        // Brown shell
        g.setColor(new Color(139, 90, 43));
        g.fillOval(x - 20, y - 28, 40, 45);
        // Darker outline
        g.setColor(new Color(101, 67, 33));
        g.drawOval(x - 20, y - 28, 40, 45);
        // Face
        g.setColor(Color.BLACK);
        g.fillOval(x - 8, y - 15, 5, 5);
        g.fillOval(x + 3, y - 15, 5, 5);
        g.drawArc(x - 8, y - 8, 16, 10, 0, -180);
        // Label
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 10));
        g.drawString("W", x - 5, y + 5);
    }
}