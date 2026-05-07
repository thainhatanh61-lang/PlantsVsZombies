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
    private List<Plant> plants;
    private List<Zombie> zombies;
    private String selectedPlant = null;
    private int[][] grid = new int[5][9];
    private int zombieSpawnTimer = 0;


    public GamePanel(){
        plants = new ArrayList<>();
        zombies = new ArrayList<>();
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
        for (int i=0;i<plants.size();i++){
            plants.get(i).update(zombies,suns);
            if (plants.get(i).isDead()){
                int col=(plants.get(i).getX()-80)/90;
                int row= (plants.get(i).getY()-80)/90;
                if (row>=0&& row<5&& col>=0&&col<9){
                    grid[row][col]=0;
                }
                plants.remove(i);
                i--;
            }
        }
        for (int i=0;i <zombies.size();i++) {
            zombies.get(i).update(plants);
            if (zombies.get(i).isDead()) {
                zombies.remove(i);
                i--;
            }
        }
        zombieSpawnTimer++;
        if (zombieSpawnTimer>=200){
            zombieSpawnTimer=0;
            int row=random.nextInt(5);
            zombies.add(new Zombie(850,100+row*100));
        }
        for (Zombie zombie: zombies){
            if (zombie.getX()<80){
                timer.stop();
                JOptionPane.showMessageDialog(this, "Game Over! Zombies ate your brain!");
                System.exit(0);
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
        
        // Shovel selection box
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(220, 10, 60, 40);
        g.setColor(Color.BLACK);
        g.drawString("Shovel", 222, 32);
        
        // Sun counter
        g.setColor(Color.YELLOW);
        g.fillOval(700, 10, 30, 30);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Sun: "+sunPoints, 740, 35);
        if (selectedPlant!=null){
            g.setColor(Color.RED);
            g.setFont(new Font("Arial",Font.BOLD,14));
            g.drawString("Selected: " +selectedPlant, 600,55);
        }
        for (Plant plant: plants){
            plant.draw(g);
        }
        for (Zombie zombie:zombies){
            zombie.draw(g);
        }
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
        if (my>=10&& my<=50){
            if (mx>=10&& mx<70){
                selectedPlant="Sunflower";
                return;
            }
            else if(mx>=80&&mx<=140){
                selectedPlant="Peashooter";  
                return;  
            }
            else if(mx>=150&& mx<=210){
                selectedPlant="Wallnut";
                return;
            }
            else if(mx>=220&& mx<=280){
                selectedPlant="Shovel";
                return;
            }
        }
        if (selectedPlant!=null&&mx>=80&&my>=80){
            int col=(mx-80)/90;
            int row=(my-80)/100;
            if (row >= 0 && row < 5 && col >= 0 && col < 9) {
                if ("Shovel".equals(selectedPlant)) {
                    for (int i=0; i<plants.size(); i++) {
                        Plant plant = plants.get(i);
                        if (plant.getX() == 80 + col*90 + 45 && plant.getY() == 80 + row*100 + 50) {
                            plants.remove(i);
                            grid[row][col] = 0;
                            selectedPlant = null;
                            break;
                        }
                    }
                } else if (grid[row][col] == 0) {
                    Plant plant=null;
                    int cost=0;
                    switch(selectedPlant){
                        case "Sunflower":
                            cost=50;
                            if (sunPoints>=cost){
                                plant=new Sunflower(80+col*90+45, 80+row*100+50);
                            }
                            break;
                        case "Peashooter":
                            cost=100;
                            if (sunPoints>=cost){
                                plant=new Peashooter(80+col*90+45,80+row*100+50, row);
                            }
                            break;
                        case "Wallnut":
                            cost = 50;
                            if (sunPoints >= cost) {
                                plant = new Wallnut(80 + col*90+45,80+row*100+50);
                            }
                            break;
                    }
                    if (plant!=null){
                        plants.add(plant);
                        grid[row][col]=1;
                        sunPoints-=cost;
                        selectedPlant=null;
                    }
                }
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