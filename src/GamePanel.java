import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;
import javax.swing.JButton;

public class GamePanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener{
    private List<Sun> suns;
    private Random random;
    private Timer timer;
    private int sunPoints=100;
    private List<Plant> plants;
    private List<Zombie> zombies;
    private String selectedPlant = null;
    private int[][] grid = new int[5][9];
    private int zombieSpawnTimer = 0;
    private int skySunTimer = 0;
    private List<LawnMower> lawnMowers;
    private String gameMode;
    private String[] plantNames;
    private int[] plantCosts;
    private ImageIcon[] plantCardIcons;
    private ImageIcon[] plantMoveIcons;
    private int[] plantRechargeTimes;
    private int[] plantCooldowns;
    private BufferedImage shovelImage;
    private BufferedImage backgroundImage;
    private BufferedImage chooserBackground;
    private JButton bMenu;
    private int mouseX;
    private int mouseY;
    private int hoverRow = -1;
    private int hoverCol = -1;

    // Background crop: source rect in the 1400x600 image
    private static final int BG_SRC_X = 230;
    private static final int BG_SRC_END_X = 1130;

    // Grid aligned to the yard cells in the cropped background
    private static final int GRID_X = 110;
    private static final int GRID_Y = 86;
    private static final int GRID_COLUMNS = 9;
    private static final int GRID_ROWS = 5;
    private static final int GRID_WIDTH = 700;
    private static final int GRID_HEIGHT = 455;
    private static final int LAWN_MOWER_X = GRID_X - 55;
    private static final double GRID_CELL_WIDTH = GRID_WIDTH / (double) GRID_COLUMNS;
    private static final double GRID_CELL_HEIGHT = GRID_HEIGHT / (double) GRID_ROWS;

    public GamePanel(){
        this("day");
    }

    public GamePanel(String gameMode){
        this.gameMode = gameMode;
        plants = new ArrayList<>();
        zombies = new ArrayList<>();
        suns =new ArrayList<>();
        lawnMowers = new ArrayList<>();
        random = new Random();
        addMouseListener(this);
        addMouseMotionListener(this);
        setFocusable(true);
        timer= new Timer(30,this);
        timer.start();
        setupPlantRoster();
        loadCardIcons();
        shovelImage = Plant.loadResourceImage("UI/shovel.jpg");
        if ("night".equals(gameMode)) {
            backgroundImage = Plant.loadResourceImage("Items/Background/Background_1.jpg");
        } else {
            backgroundImage = Plant.loadResourceImage("Items/Background/Background_0.jpg");
        }
        chooserBackground = Plant.loadResourceImage("Screen/ChooserBackground.png");
        for (int row = 0; row < GRID_ROWS; row++) {
            lawnMowers.add(new LawnMower(LAWN_MOWER_X, getCellCenterY(row)));
        }
        bMenu =new JButton("MENU");
        bMenu.setBounds(780,10,100,35);
        bMenu.setFont(new Font("Arial", Font.BOLD,14));
        bMenu.setBackground(new Color(139,69,19));
        bMenu.setForeground(Color.WHITE);
        bMenu.setFocusPainted(false);
        bMenu.setBorder(BorderFactory.createRaisedBevelBorder());
        bMenu.addActionListener(e-> returnToMenu());
        bMenu.addMouseListener(new java.awt.event.MouseAdapter() {
          public void mouseEntered(java.awt.event.MouseEvent evt){
            bMenu.setBackground(new Color(205,133,63));
          }  
          public void mouseExited(java.awt.event.MouseEvent evt){
            bMenu.setBackground(new Color(139,69,19));
          }
        });
        this.add(bMenu);
    }

    private void setupPlantRoster() {
        if ("night".equals(gameMode)) {
            plantNames = new String[]{"Sunshroom", "Puffshroom", "Scaredyshroom", "Wallnut", "CherryBomb", "Iceshroom", "PotatoMine", "Hypnoshroom"};
            plantCosts = new int[]{25, 0, 25, 50, 150, 75, 25, 75};
            plantRechargeTimes = new int[]{233, 233, 233, 1000, 1667, 1667, 1000, 1000};
        } else {
            plantNames = new String[]{"Sunflower", "Peashooter", "Wallnut", "PotatoMine", "Jalapeno", "Chomper", "CherryBomb", "Spikeweed"};
            plantCosts = new int[]{50, 100, 50, 25, 125, 150, 150, 100};
            plantRechargeTimes = new int[]{233, 233, 1000, 1000, 1667, 1000, 1667, 233};
        }
    }

    private void loadCardIcons() {
        plantCardIcons = new ImageIcon[plantNames.length];
        plantMoveIcons = new ImageIcon[plantNames.length];
        plantCooldowns = new int[plantNames.length];
        for (int i = 0; i < plantNames.length; i++) {
            BufferedImage cardImage = Plant.loadResourceImage("UI/SeedChooser/Cards/" + plantNames[i] + "_card.png");
            BufferedImage moveImage = Plant.loadResourceImage("UI/SeedChooser/Cards/" + plantNames[i] + "_card_move.png");
            plantCardIcons[i] = cardImage != null ? new ImageIcon(cardImage) : new ImageIcon();
            plantMoveIcons[i] = moveImage != null ? new ImageIcon(moveImage) : plantCardIcons[i];
        }
    }
    @Override
    public void actionPerformed(ActionEvent e){
        for (int i = 0; i < plantCooldowns.length; i++) {
            if (plantCooldowns[i] > 0) {
                plantCooldowns[i]--;
            }
        }
        skySunTimer++;
        if (skySunTimer >= 500 && countSkySuns() < 3){
            skySunTimer = 0;
            int sx= random.nextInt(800)+80;
            int sy= 0;
            int targetY= 100+random.nextInt(400);
            suns.add(new Sun(sx,sy, targetY, true));
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
        for (LawnMower lawnMower : lawnMowers) {
            lawnMower.update(zombies);
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
            boolean protectedByMower = false;
            for (LawnMower lawnMower : lawnMowers) {
                if (!lawnMower.isUsed() && Math.abs(lawnMower.getY() - zombie.getY()) <= 35) {
                    protectedByMower = true;
                    break;
                }
            }
            if (zombie.getX()<10 && !protectedByMower){
                timer.stop();
                int choice= JOptionPane.showConfirmDialog(this,"Game Over! Zombies ate your brain!\n\nDo you want to go back to the Menu?", "Game Over", JOptionPane.YES_NO_CANCEL_OPTION);
                if (choice== JOptionPane.YES_OPTION){
                    returnToMenu();
                }
                else{
                    System.exit(0);
                }
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

        drawGridEffects(g);

        // Draw chooser background bar at top
        if (chooserBackground != null) {
            g.drawImage(chooserBackground, 0, 0, 620, 87, null);
        } else {
            g.setColor(new Color(139, 90, 43));
            g.fillRect(0, 0, 620, 87);
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

        for (int i = 0; i < plantNames.length; i++) {
            if (plantCardIcons[i].getImage() != null) {
                g.drawImage(plantCardIcons[i].getImage(), cardX, cardY, cardW, cardH, null);
            }
            if (sunPoints < plantCosts[i]) {
                g.setColor(new Color(80, 80, 80, 120));
                g.fillRect(cardX, cardY, cardW, cardH);
            }
            if (plantCooldowns[i] > 0) {
                int cooldownHeight = (int)(cardH * (plantCooldowns[i] / (double)plantRechargeTimes[i]));
                g.setColor(new Color(0, 0, 0, 130));
                g.fillRect(cardX, cardY, cardW, cooldownHeight);
            }
            if (plantNames[i].equals(selectedPlant)) {
                g.setColor(new Color(255, 255, 0, 100));
                g.fillRect(cardX, cardY, cardW, cardH);
            }
            cardX += cardSpacing;
        }

        // Shovel icon area
        cardX += 10;
        g.setColor(new Color(139, 90, 43));
        g.fillRect(cardX, cardY, cardW, cardH);
        if (shovelImage != null) {
            g.drawImage(shovelImage, cardX, cardY, cardW, cardH, null);
        } else {
            g.setColor(Color.LIGHT_GRAY);
            g.setFont(new Font("Arial", Font.BOLD, 11));
            g.drawString("Shovel", cardX + 3, cardY + 40);
        }
        if ("Shovel".equals(selectedPlant)) {
            g.setColor(new Color(255, 255, 0, 100));
            g.fillRect(cardX, cardY, cardW, cardH);
        }

        drawHoverCell(g);

        // Draw plants, zombies, suns
        for (LawnMower lawnMower : lawnMowers) {
            lawnMower.draw(g);
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
        drawCursorImage(g);
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

    private void drawGridEffects(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if ("night".equals(gameMode)) {
            g2d.setColor(new Color(0, 0, 0, 80));
            for (int row = 0; row < GRID_ROWS; row++) {
                for (int col = 0; col < GRID_COLUMNS; col++) {
                    int x = GRID_X + (int)Math.round(col * GRID_CELL_WIDTH);
                    int y = GRID_Y + (int)Math.round(row * GRID_CELL_HEIGHT);
                    int w = (int)Math.round(GRID_CELL_WIDTH);
                    int h = (int)Math.round(GRID_CELL_HEIGHT);
                    g2d.fillRect(x, y, w, h);
                }
            }
        }

        g2d.setColor(new Color(255, 255, 255, 45));
        for (int col = 0; col <= GRID_COLUMNS; col++) {
            int x = GRID_X + (int)Math.round(col * GRID_CELL_WIDTH);
            g2d.drawLine(x, GRID_Y, x, GRID_Y + GRID_HEIGHT);
        }
        for (int row = 0; row <= GRID_ROWS; row++) {
            int y = GRID_Y + (int)Math.round(row * GRID_CELL_HEIGHT);
            g2d.drawLine(GRID_X, y, GRID_X + GRID_WIDTH, y);
        }
    }

    private int countSkySuns() {
        int count = 0;
        for (Sun sun : suns) {
            if (sun.isSkySun()) {
                count++;
            }
        }
        return count;
    }

    private void drawHoverCell(Graphics g) {
        if (selectedPlant == null || hoverRow < 0 || hoverCol < 0) {
            return;
        }
        if (!isInsideGrid(mouseX, mouseY)) {
            return;
        }
        int x = GRID_X + (int)Math.round(hoverCol * GRID_CELL_WIDTH);
        int y = GRID_Y + (int)Math.round(hoverRow * GRID_CELL_HEIGHT);
        int w = (int)Math.round(GRID_CELL_WIDTH);
        int h = (int)Math.round(GRID_CELL_HEIGHT);
        if ("Shovel".equals(selectedPlant) || grid[hoverRow][hoverCol] == 0) {
            g.setColor(new Color(0, 255, 0, 80));
        } else {
            g.setColor(new Color(255, 0, 0, 80));
        }
        g.fillRect(x, y, w, h);
    }

    private void drawCursorImage(Graphics g) {
        if (selectedPlant == null) {
            return;
        }
        if ("Shovel".equals(selectedPlant)) {
            if (shovelImage != null) {
                g.drawImage(shovelImage, mouseX - 20, mouseY - 20, 40, 40, null);
            }
            return;
        }
        int index = getPlantIndex(selectedPlant);
        if (index >= 0 && plantMoveIcons[index].getImage() != null) {
            g.drawImage(plantMoveIcons[index].getImage(), mouseX - 25, mouseY - 35, 50, 70, null);
        }
    }

    private int getPlantIndex(String name) {
        for (int i = 0; i < plantNames.length; i++) {
            if (plantNames[i].equals(name)) {
                return i;
            }
        }
        return -1;
    }

    private int getPlantCost(String name) {
        int index = getPlantIndex(name);
        if (index >= 0) {
            return plantCosts[index];
        }
        return 9999;
    }

    private Plant createPlant(String name, int x, int y, int row) {
        switch(name){
            case "Sunflower":
                return new Sunflower(x, y);
            case "Peashooter":
                return new Peashooter(x, y, row);
            case "Wallnut":
                return new Wallnut(x, y);
            case "PotatoMine":
                return new PotatoMine(x, y);
            case "Jalapeno":
                return new BasicPlant(x, y, 125, 300, "Plants/Jalapeno/Jalapeno/Jalapeno_", 8);
            case "Chomper":
                return new BasicPlant(x, y, 150, 300, "Plants/Chomper/Chomper/Chomper_", 13);
            case "CherryBomb":
                return new BasicPlant(x, y, 150, 300, "Plants/CherryBomb/CherryBomb_", 7);
            case "Spikeweed":
                return new BasicPlant(x, y, 100, 300, "Plants/Spikeweed/Spikeweed/Spikeweed_", 19);
            case "Sunshroom":
                return new BasicPlant(x, y, 25, 300, "Plants/SunShroom/SunShroom/SunShroom_", 10);
            case "Puffshroom":
                return new BasicPlant(x, y, 0, 200, "Plants/PuffShroom/PuffShroom/PuffShroom_", 14);
            case "Scaredyshroom":
                return new BasicPlant(x, y, 25, 300, "Plants/ScaredyShroom/ScaredyShroom/ScaredyShroom_", 17);
            case "Iceshroom":
                return new BasicPlant(x, y, 75, 300, "Plants/IceShroom/IceShroom/IceShroom_", 11);
            case "Hypnoshroom":
                return new BasicPlant(x, y, 75, 300, "Plants/HypnoShroom/HypnoShroom/HypnoShroom_", 15);
        }
        return null;
    }

    @Override
    public void mouseClicked(MouseEvent e){
        int mx= e.getX();
        int my= e.getY();
        mouseX = mx;
        mouseY = my;
        updateHoverCell(mx, my);

        if (SwingUtilities.isRightMouseButton(e)) {
            selectedPlant = null;
            repaint();
            return;
        }
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
            for (int i = 0; i < plantNames.length; i++) {
                if (mx>=cardX && mx<cardX+cardW) {
                    if (sunPoints >= plantCosts[i] && plantCooldowns[i] == 0) {
                        selectedPlant = plantNames[i];
                    }
                    return;
                }
                cardX += cardSpacing;
            }
            cardX += 10;
            if (mx>=cardX && mx<cardX+cardW) { selectedPlant="Shovel"; return; }
        }
        if (selectedPlant!=null&&isInsideGrid(mx, my)){
            int col = getColumnFromX(mx);
            int row = getRowFromY(my);
            if (row >= 0 && row < GRID_ROWS && col >= 0 && col < GRID_COLUMNS) {
                if ("Shovel".equals(selectedPlant)) {
                    boolean removedPlant = false;
                    for (int i=0; i<plants.size(); i++) {
                        Plant plant = plants.get(i);
                        int plantCol = getColumnFromX(plant.getX());
                        int plantRow = getRowFromY(plant.getY());
                        if (plantCol == col && plantRow == row) {
                            plants.remove(i);
                            grid[row][col] = 0;
                            selectedPlant = null;
                            removedPlant = true;
                            break;
                        }
                    }
                    if (!removedPlant) {
                        selectedPlant = null;
                    }
                } else if (grid[row][col] == 0) {
                    Plant plant=null;
                    int cost=getPlantCost(selectedPlant);
                    int centerX = getCellCenterX(col);
                    int centerY = getCellCenterY(row);
                    if (sunPoints>=cost){
                        plant = createPlant(selectedPlant, centerX, centerY, row);
                    }
                    if (plant!=null){
                        plants.add(plant);
                        grid[row][col]=1;
                        sunPoints-=cost;
                        int plantIndex = getPlantIndex(selectedPlant);
                        if (plantIndex >= 0) {
                            plantCooldowns[plantIndex] = plantRechargeTimes[plantIndex];
                        }
                        selectedPlant=null;
                    }
                }
            }
        } else if (selectedPlant != null) {
            selectedPlant = null;
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
    @Override public void mouseMoved(MouseEvent e){
        mouseX = e.getX();
        mouseY = e.getY();
        updateHoverCell(mouseX, mouseY);
        repaint();
    }
    @Override public void mouseDragged(MouseEvent e){
        mouseMoved(e);
    }
    private void updateHoverCell(int x, int y) {
        if (isInsideGrid(x, y)) {
            hoverCol = getColumnFromX(x);
            hoverRow = getRowFromY(y);
        } else {
            hoverCol = -1;
            hoverRow = -1;
        }
    }
    public void returnToMenu(){
        JFrame frame= (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame!=null){
            frame.getContentPane().removeAll();
            frame.add(new MenuPanel(frame));
            frame.revalidate();
            frame.repaint();
        }
    }
}
