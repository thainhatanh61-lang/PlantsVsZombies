import java.awt.*;
import java.util.List;

public class Sunflower extends Plant{
    private int timer=0;
    public Sunflower(int x, int y){
        super(x,y,50,100);
    }
    @Override
    public void update(List<Zombie> zombies, List<Sun> suns){
        timer++;
        if (timer>=300){
            timer=0;
            suns.add(new Sun(x,y,y-40));
        }
    }
    @Override
    public void draw(Graphics g) {
        // Yellow flower head
        g.setColor(Color.YELLOW);
        g.fillOval(x - 20, y - 35, 40, 40);
        // Brown center
        g.setColor(new Color(139, 90, 43));
        g.fillOval(x - 10, y - 25, 20, 20);
        // Green stem
        g.setColor(Color.GREEN);
        g.fillRect(x - 3, y + 5, 6, 20);
        // Label
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 8));
        g.drawString("Sun", x - 10, y + 5);
        drawHealthBar(g);
    }
}
