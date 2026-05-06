import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, MouseListener {
    private Timer timer;
    private int sunPoints = 150;
    private String selectedPlant = null;
    private List<Plant> plants;
    private List<Zombie> zombies;
    private List<Sun> suns;
    private Random random;
    private int[][] grid = new int[5][9];
    private int zombieSpawnTimer = 0;
    
    public GamePanel() {
        setFocusable(true);
        addMouseListener(this);
        plants = new ArrayList<>();
        zombies =new ArrayList<>();
        suns = new ArrayList<>();
        random = new Random();
        timer =new Timer(30, this);
        timer.start();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }
    private void updateGame() {
        for (int i = 0; i <suns.size(); i++) {
            suns.get(i).update();
            if (suns.get(i).isCollected()) {
                sunPoints += 25;
                suns.remove(i);
                i--;
            }
        }
        if (random.nextInt(100) < 2) {
            suns.add(new Sun(random.nextInt(800) + 80, 0, 100 + random.nextInt(400)));
        }
        for (Plant plant : plants) {
            plant.update(zombies, suns);
        }
        for (Zombie zombie : zombies) {
            zombie.update(plants);
        }
        for (int i = 0; i < zombies.size(); i++) {
            if (zombies.get(i).getHealth() <= 0) {
                zombies.remove(i);
                i--;
            }
        }
        zombieSpawnTimer++;
        if (zombieSpawnTimer > 200) {
            zombieSpawnTimer = 0;
            int row = random.nextInt(5);
            zombies.add(new Zombie(850, 100 + row * 100));
        }
        for (Zombie zombie : zombies) {
            if (zombie.getX() < 80) {
                timer.stop();
                JOptionPane.showMessageDialog(this, "Game Over!");
                System.exit(0);
            }
        }
    }
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
        g.setColor(new Color(139, 90, 43));
        g.fillRect(0, 0, 900, 60);
        g.setColor(Color.YELLOW);
        g.fillRect(10, 10, 60, 40);
        g.setColor(Color.BLACK);
        g.drawString("Sunflower", 12, 30);
        g.drawString("Cost: 50", 12, 48);
        g.setColor(Color.GREEN);
        g.fillRect(80, 10, 60, 40);
        g.setColor(Color.BLACK);
        g.drawString("Peashooter", 82, 30);
        g.drawString("Cost: 100", 82, 48);
        g.setColor(new Color(139, 69, 19));
        g.fillRect(150, 10, 60, 40);
        g.setColor(Color.WHITE);
        g.drawString("Wall-nut", 152, 30);
        g.drawString("Cost: 50", 152, 48);
        g.setColor(Color.YELLOW);
        g.fillOval(700, 10, 30, 30);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Sun: " + sunPoints, 740, 35);
        for (Sun sun : suns) {
            sun.draw(g);
        }
        for (Plant plant : plants) {
            plant.draw(g);
        }
        for (Zombie zombie : zombies) {
            zombie.draw(g);
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        for (int i = 0; i < suns.size(); i++) {
            if (suns.get(i).contains(x, y)) {
                suns.get(i).collect();
                return;
            }
        }
        if (y >= 10 && y <= 50) {
            if (x >= 10 && x <= 70) {
                selectedPlant = "Sunflower";
            } else if (x >= 80 && x <= 140) {
                selectedPlant = "Peashooter";
            } else if (x >= 150 && x <= 210) {
                selectedPlant = "Wallnut";
            }
            return;
        }
        if (selectedPlant != null && x >= 80 && y >= 80) {
            int col = (x - 80) / 90;
            int row = (y - 80) / 100;
            if (row < 5 && col < 9 && grid[row][col] == 0) {
                int cost = 0;
                Plant plant = null;
                switch (selectedPlant) {
                    case "Sunflower":
                        cost = 50;
                        if (sunPoints >= cost) {
                            plant = new Sunflower(80 + col * 90 + 45, 80 + row * 100 + 50);
                        }
                        break;
                    case "Peashooter":
                        cost = 100;
                        if (sunPoints >= cost) {
                            plant = new Peashooter(80 + col * 90 + 45, 80 + row * 100 + 50, row);
                        }
                        break;
                    case "Wallnut":
                        cost = 50;
                        if (sunPoints >= cost) {
                            plant = new Wallnut(80 + col * 90 + 45, 80 + row * 100 + 50);
                        }
                        break;
                }
                if (plant != null) {
                    plants.add(plant);
                    grid[row][col] = 1;
                    sunPoints -= cost;
                    selectedPlant = null;
                }
            }
        }
    }
    
    @Override 
    public void mousePressed(MouseEvent e){

    }
    @Override
    public void mouseReleased(MouseEvent e){

    }
    @Override 
    public void mouseEntered(MouseEvent e){

    }
    @Override 
    public void mouseExited(MouseEvent e){

    }
}