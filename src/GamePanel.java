import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
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
    private ImageIcon potatoCardIcon;
    private ImageIcon sunflowerCardIcon;
    private ImageIcon peashooterCardIcon;
    private ImageIcon wallnutCardIcon;
    private BufferedImage backgroundImage;
    private BufferedImage chooserBackground;

    // Background crop: source rect in the 1400x600 image
    private static final int BG_SRC_X = 230;
    private static final int BG_SRC_END_X = 1130;

    // Grid aligned to the yard cells in the cropped background
    private static final int GRID_X = 23;
    private static final int GRID_Y = 86;
    private static final int GRID_COLUMNS = 9;
    private static final int GRID_ROWS = 5;
    private static final int GRID_WIDTH = 843;
    private static final int GRID_HEIGHT = 489;
    private static final double GRID_CELL_WIDTH = GRID_WIDTH / (double) GRID_COLUMNS;
    private static final double GRID_CELL_HEIGHT = GRID_HEIGHT / (double) GRID_ROWS;

    public GamePanel(){
        plants = new ArrayList<>();
        zombies = new ArrayList<>();
        suns =new ArrayList<>();
        random = new Random();
        addMouseListener(this);
        setFocusable(true);
        timer= new Timer(30,this);
        timer.start();
        BufferedImage potatoCardImage = Plant.loadResourceImage("Cards/card_potatomine.png");
        BufferedImage sunflowerCardImage = Plant.loadResourceImage("Cards/card_sunflower.png");
        BufferedImage peashooterCardImage = Plant.loadResourceImage("Cards/card_peashooter.png");
        BufferedImage wallnutCardImage = Plant.loadResourceImage("Cards/card_wallnut.png");
        backgroundImage = Plant.loadResourceImage("Items/Background/Background_0.jpg");
        chooserBackground = Plant.loadResourceImage("Screen/ChooserBackground.png");
        potatoCardIcon = potatoCardImage != null ? new ImageIcon(potatoCardImage) : new ImageIcon();
        sunflowerCardIcon = sunflowerCardImage != null ? new ImageIcon(sunflowerCardImage) : new ImageIcon();
        peashooterCardIcon = peashooterCardImage != null ? new ImageIcon(peashooterCardImage) : new ImageIcon();
        wallnutCardIcon = wallnutCardImage != null ? new ImageIcon(wallnutCardImage) : new ImageIcon();
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
                int col = getColumnFromX(plants.get(i).getX());
                int row = getRowFromY(plants.get(i).getY());
                if (row>=0&& row<GRID_ROWS && col>=0&&col<GRID_COLUMNS){
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
            int row=random.nextInt(GRID_ROWS);
            int zombieY = getCellCenterY(row);
            int roll = random.nextInt(10);
            if (roll == 0) {
                zombies.add(new BucketheadZombie(920, zombieY));
            } else if (roll <= 2) {
                zombies.add(new ConeheadZombie(920, zombieY));
            } else if (roll == 3) {
                zombies.add(new FlagZombie(920, zombieY));
            } else {
                zombies.add(new Zombie(920, zombieY));
            }
        }
        for (Zombie zombie: zombies){
            if (zombie.getX()<10){
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
        // Draw background zoomed in to show only the yard (hide road and house)
        if (backgroundImage != null) {
            g.drawImage(backgroundImage,
                0, 0, getWidth(), getHeight(),
                BG_SRC_X, 0, BG_SRC_END_X, backgroundImage.getHeight(),
                null);
        } else {
            g.setColor(new Color(100, 180, 100));
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // Draw chooser background bar at top
        if (chooserBackground != null) {
            g.drawImage(chooserBackground, 0, 0, 470, 87, null);
        } else {
            g.setColor(new Color(139, 90, 43));
            g.fillRect(0, 0, 470, 87);
        }

        // Sun counter (on the chooser bar, left side)
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString(String.valueOf(sunPoints), 23, 78);

        // Plant cards on the chooser bar
        int cardX = 77;
        int cardY = 6;
        int cardW = 50;
        int cardH = 70;
        int cardSpacing = 51;

        // Sunflower card
        if (sunflowerCardIcon.getImage() != null) {
            g.drawImage(sunflowerCardIcon.getImage(), cardX, cardY, cardW, cardH, null);
        }
        if ("Sunflower".equals(selectedPlant)) {
            g.setColor(new Color(255, 255, 0, 100));
            g.fillRect(cardX, cardY, cardW, cardH);
        }

        // Peashooter card
        cardX += cardSpacing;
        if (peashooterCardIcon.getImage() != null) {
            g.drawImage(peashooterCardIcon.getImage(), cardX, cardY, cardW, cardH, null);
        }
        if ("Peashooter".equals(selectedPlant)) {
            g.setColor(new Color(255, 255, 0, 100));
            g.fillRect(cardX, cardY, cardW, cardH);
        }

        // Wall-nut card
        cardX += cardSpacing;
        if (wallnutCardIcon.getImage() != null) {
            g.drawImage(wallnutCardIcon.getImage(), cardX, cardY, cardW, cardH, null);
        }
        if ("Wallnut".equals(selectedPlant)) {
            g.setColor(new Color(255, 255, 0, 100));
            g.fillRect(cardX, cardY, cardW, cardH);
        }

        // PotatoMine card
        cardX += cardSpacing;
        if (potatoCardIcon.getImage() != null) {
            g.drawImage(potatoCardIcon.getImage(), cardX, cardY, cardW, cardH, null);
        }
        if ("PotatoMine".equals(selectedPlant)) {
            g.setColor(new Color(255, 255, 0, 100));
            g.fillRect(cardX, cardY, cardW, cardH);
        }

        // Shovel icon area
        cardX += cardSpacing;
        g.setColor(new Color(139, 90, 43));
        g.fillRect(cardX, cardY, cardW, cardH);
        g.setColor(Color.LIGHT_GRAY);
        g.setFont(new Font("Arial", Font.BOLD, 11));
        g.drawString("Shovel", cardX + 3, cardY + 40);
        if ("Shovel".equals(selectedPlant)) {
            g.setColor(new Color(255, 255, 0, 100));
            g.fillRect(cardX, cardY, cardW, cardH);
        }

        // Draw plants, zombies, suns
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

    private boolean isInsideGrid(int x, int y) {
        return x >= GRID_X && x < GRID_X + GRID_WIDTH && y >= GRID_Y && y < GRID_Y + GRID_HEIGHT;
    }

    private int getColumnFromX(int x) {
        return (int) ((x - GRID_X) / GRID_CELL_WIDTH);
    }

    private int getRowFromY(int y) {
        return (int) ((y - GRID_Y) / GRID_CELL_HEIGHT);
    }

    private int getCellCenterX(int col) {
        return GRID_X + (int) Math.round(col * GRID_CELL_WIDTH + GRID_CELL_WIDTH / 2.0);
    }

    private int getCellCenterY(int row) {
        return GRID_Y + (int) Math.round(row * GRID_CELL_HEIGHT + GRID_CELL_HEIGHT / 2.0);
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
        // Card selection (aligned to the chooser bar cards)
        if (my>=6&& my<=76){
            int cardX = 77;
            int cardW = 50;
            int cardSpacing = 51;
            if (mx>=cardX && mx<cardX+cardW) { selectedPlant="Sunflower"; return; }
            cardX += cardSpacing;
            if (mx>=cardX && mx<cardX+cardW) { selectedPlant="Peashooter"; return; }
            cardX += cardSpacing;
            if (mx>=cardX && mx<cardX+cardW) { selectedPlant="Wallnut"; return; }
            cardX += cardSpacing;
            if (mx>=cardX && mx<cardX+cardW) { selectedPlant="PotatoMine"; return; }
            cardX += cardSpacing;
            if (mx>=cardX && mx<cardX+cardW) { selectedPlant="Shovel"; return; }
        }
        if (selectedPlant!=null&&isInsideGrid(mx, my)){
            int col = getColumnFromX(mx);
            int row = getRowFromY(my);
            if (row >= 0 && row < GRID_ROWS && col >= 0 && col < GRID_COLUMNS) {
                if ("Shovel".equals(selectedPlant)) {
                    for (int i=0; i<plants.size(); i++) {
                        Plant plant = plants.get(i);
                        int plantCol = getColumnFromX(plant.getX());
                        int plantRow = getRowFromY(plant.getY());
                        if (plantCol == col && plantRow == row) {
                            plants.remove(i);
                            grid[row][col] = 0;
                            selectedPlant = null;
                            break;
                        }
                    }
                } else if (grid[row][col] == 0) {
                    Plant plant=null;
                    int cost=0;
                    int centerX = getCellCenterX(col);
                    int centerY = getCellCenterY(row);
                    switch(selectedPlant){
                        case "Sunflower":
                            cost=50;
                            if (sunPoints>=cost){
                                plant=new Sunflower(centerX, centerY);
                            }
                            break;
                        case "Peashooter":
                            cost=100;
                            if (sunPoints>=cost){
                                plant=new Peashooter(centerX, centerY, row);
                            }
                            break;
                        case "Wallnut":
                            cost = 50;
                            if (sunPoints >= cost) {
                                plant = new Wallnut(centerX, centerY);
                            }
                            break;
                        case "PotatoMine":
                            cost = 25;
                            if (sunPoints >= cost) {
                                plant = new PotatoMine(centerX, centerY);
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