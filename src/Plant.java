import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.swing.ImageIcon;

public abstract class Plant {
    protected int x,y;
    protected int health;
    protected int maxHealth;
    protected int cost;
    public Plant(int x,int y,int cost, int health){
        this.x = x;
        this.y = y;
        this.cost = cost;
        this.health = health;
        this.maxHealth = health;
    }
    protected static BufferedImage loadResourceImage(String relativePath) {
        String[] roots = {
            "resources/graphics/",
            "../resources/graphics/",
            "src/resources/graphics/"
        };
        for (String root : roots) {
            File file = new File(root + relativePath);
            if (file.exists()) {
                ImageIcon icon = new ImageIcon(file.getPath());
                int width = icon.getIconWidth();
                int height = icon.getIconHeight();
                if (width > 0 && height > 0) {
                    return toBufferedImage(icon.getImage(), width, height);
                }
            }
        }
        return null;
    }

    private static BufferedImage toBufferedImage(Image img, int width, int height) {
        BufferedImage buffered = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = buffered.createGraphics();
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();
        
        for (int y = 0; y < buffered.getHeight(); y++) {
            for (int x = 0; x < buffered.getWidth(); x++) {
                int rgb = buffered.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                
                if (r > 240 && g > 240 && b > 240) {
                    buffered.setRGB(x, y, 0);
                }
            }
        }
        
        return buffered;
    }
    public abstract void update(List<Zombie> zombies, List<Sun> suns);
    public abstract void draw(Graphics g);
    protected void drawHealthBar(Graphics g){
        int barWidth = 40;
        int filled = (int)(barWidth * (health / (double)maxHealth));
        g.setColor(Color.RED);
        g.fillRect(x - 20, y - 55, barWidth, 5);
        g.setColor(Color.GREEN);
        g.fillRect(x - 20, y - 55, filled, 5);
    }
    public void takeDamage(int damage){
        health-=damage;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getCost() {
        return cost;
    }
    public int getHealth() {
        return health;
    }
    public boolean isDead(){
        return health<=0;
    }
}
