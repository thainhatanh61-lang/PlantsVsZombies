import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.List;

public class Peashooter extends Plant{
    private List<Pea> peas;
    private int shootTimer;
    private int row;
    private BufferedImage[] frames;
    private int frameIndex;
    private int frameTimer;

    public Peashooter(int x,int y, int row){
        super(x,y,100,300);
        this.row=row;
        peas = new ArrayList<>();
        shootTimer=0;
        frameTimer=0;
        loadImages();
    }

    private void loadImages() {
        String base = "Plants/Peashooter/";
        frames = new BufferedImage[8];
        for (int i = 0; i < 8; i++) {
            Image img = loadResourceImage(base + "Peashooter_" + i + ".png");
            if (img != null) {
                frames[i] = img instanceof BufferedImage ? (BufferedImage) img : toBufferedImage(img);
            } else {
                frames[i] = null;
            }
        }
    }

    private BufferedImage toBufferedImage(Image img) {
        BufferedImage buffered = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
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

    @Override
    public void update(List<Zombie> zombies, List<Sun> suns){
        boolean hasZombieInRow = false;
        for (Zombie zombie : zombies) {
            if (!zombie.isDead() && Math.abs(zombie.getY() - y) <= 35 && zombie.getX() > x) {
                hasZombieInRow = true;
                break;
            }
        }

        if (hasZombieInRow) {
            shootTimer++;
            if (shootTimer>=60){
                shootTimer=0;
                peas.add(new Pea(x+10,y-5));
            }
        } else {
            shootTimer = 0;
        }
        for (int i=0; i<peas.size();i++){
            Pea pea = peas.get(i);
            pea.update();
            for (Zombie zombie: zombies){
                if (!zombie.isDead() && pea.hits(zombie)){
                    zombie.takeDamage(20);
                    break;
                }
            }
            if (pea.isFinished() || pea.isOffScreen()){
                peas.remove(i);
                i--;
            }
        }
        frameTimer++;
        if (frameTimer >= 4) {
            frameTimer = 0;
            frameIndex = (frameIndex + 1) % frames.length;
        }
    }
    @Override
    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        
        if (frames != null && frameIndex < frames.length && frames[frameIndex] != null) {
            g2d.drawImage(frames[frameIndex], x - 25, y - 30, 50, 50, null);
        }
        drawHealthBar(g);
        for (Pea pea : peas) {
            pea.draw(g);
        }
    }
}
