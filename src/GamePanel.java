import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class GamePanel extends JPanel implements ActionListener, MouseListener{
    private List<Sun> suns;
    private Random random;
    private Timer timer;
    private int sunPoints=150;


    public GamePanel(){
        suns =new ArrayList<>();
        random = new Random();
        addMouseListener(this);
        setFocusable(true);
        timer= new Timer(30,this);
        timer.start();
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if (random.nextInt(100)<2){
            int sx= random.nextInt(800)+80;
            int sy= 0;
            int targetY= 100+random.nextInt(400);
            suns.add(new Sun(sx,sy, targetY));
        }
        for (int i=0;i<suns.size();i++){
            suns.get(i).update();
            if (suns.get(i).isCollected()){
                suns.remove(i);
                i--;
            }
        }
        repaint();
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
        g.drawString("Sun: "+sunPoints, 740, 35);
        for (Sun sun:suns){
            sun.draw(g);
        }
    }
    @Override
    public void mouseClicked(MouseEvent e){
        int mx= e.getX();
        int my= e.getY();
        for (int i=0; i<suns.size(); i++){
            if (suns.get(i).contains(mx, my)){
                sunPoints+=25;
                suns.get(i).collect();
                break;
            }
        }
    }
    @Override public void mousePressed(MouseEvent e){
    }
    @Override public void mouseReleased(MouseEvent e){
    }
    @Override public void mouseEntered(MouseEvent e){
    }
    @Override public void mouseExited(MouseEvent e){
    }
}