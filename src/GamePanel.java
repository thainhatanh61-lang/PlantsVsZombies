import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel {
    private List<Sun> suns;
    private Random random;
    public GamePanel(){
        suns =new ArrayList<>();
        random = new Random();
    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        // Draw green lawn background
        g.setColor(new Color(100, 180, 100));
        g.fillRect(0, 0, 900, 600);
        // Draw 5x9 grid
        g.setColor(new Color(0, 100, 0));
        for (int row = 0; row < 5; row++){
            for (int col = 0; col < 9; col++){
                int x = 80 + col * 90;
                int y = 80 + row * 100;
                g.drawRect(x, y, 90, 100);
            }
        }
        // Draw top selection bar
        g.setColor(new Color(139, 90, 43));
        g.fillRect(0, 0, 900, 60);
        // Sunflower selection box
        g.setColor(Color.YELLOW);
        g.fillRect(10, 10, 60, 40);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 10));
        g.drawString("Sunflower", 12, 32);
        g.drawString("50", 30, 48);
        
        // Peashooter selection box
        g.setColor(Color.GREEN);
        g.fillRect(80, 10, 60, 40);
        g.setColor(Color.BLACK);
        g.drawString("Peashooter", 82, 32);
        g.drawString("100", 105, 48);
        // Wall-nut selection box
        g.setColor(new Color(139, 69, 19));
        g.fillRect(150, 10, 60, 40);
        g.setColor(Color.WHITE);
        g.drawString("Wall-nut", 152, 32);
        g.drawString("50", 175, 48);
        
        // Sun counter
        g.setColor(Color.YELLOW);
        g.fillOval(700, 10, 30, 30);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Sun: 150", 740, 35);
    }
}