import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import java.io.File;
import java.util.List;

public class PotatoMine extends Plant {
    private BufferedImage[] initFrames;
    private BufferedImage[] armedFrames;
    private BufferedImage[] explodeFrames;
    private int stage;
    private int stageTimer;
    private boolean exploded;
    private static final int ARM_TIME = 500;
    private static final int EXPLOSION_RADIUS = 120;

    public PotatoMine(int x, int y) {
        super(x, y, 25, 1);
        this.stage = 0;
        this.stageTimer = 0;
        this.exploded = false;
        loadImages();
    }

    private void loadImages() {
        String base = "Plants/PotatoMine";
        
        initFrames = new BufferedImage[4];
        for (int i = 0; i < 4; i++) {
            Image img = loadResourceImage(base + "/PotatoMineInit/PotatoMineInit_0.png");
            initFrames[i] = img instanceof BufferedImage ? (BufferedImage) img : (img != null ? toBufferedImage(img) : null);
        }
        
        armedFrames = new BufferedImage[4];
        for (int i = 0; i < 4; i++) {
            Image img = loadResourceImage(base + "/PotatoMine/PotatoMine_" + i + ".png");
            armedFrames[i] = img instanceof BufferedImage ? (BufferedImage) img : (img != null ? toBufferedImage(img) : null);
        }
        
        explodeFrames = new BufferedImage[1];
        Image explodeImg = loadResourceImage(base + "/PotatoMineExplode/PotatoMineExplode_0.png");
        if (explodeImg != null) {
            explodeFrames[0] = explodeImg instanceof BufferedImage
                ? (BufferedImage) explodeImg
                : toBufferedImage(explodeImg);
        } else {
            explodeFrames[0] = null;
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
    public void update(List<Zombie> zombies, List<Sun> suns) {
        if (stage == 0) {
            stageTimer++;
            if (stageTimer >= ARM_TIME) {
                stage = 1;
                stageTimer = 0;
            }
        } else if (stage == 1) {
            stageTimer++;
            for (Zombie zombie : zombies) {
                if (!zombie.isDead() && Math.abs(x - zombie.getX()) <= 60 && Math.abs(y - zombie.getY()) <= 60) {
                    exploded = true;
                    stage = 2;
                    stageTimer = 0;
                    for (Zombie z : zombies) {
                        if (!z.isDead() && Math.hypot(x - z.getX(), y - z.getY()) <= EXPLOSION_RADIUS) {
                            z.takeDamage(1800);
                        }
                    }
                    break;
                }
            }
        } else if (stage == 2) {
            stageTimer++;
            if (stageTimer >= 30) {
                health = 0;
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        
        BufferedImage img = null;
        int drawWidth = 50, drawHeight = 50;
        
        if (stage == 0) {
            int frameIndex = (stageTimer / 10) % initFrames.length;
            if (initFrames[frameIndex] != null) {
                img = initFrames[frameIndex];
            }
        } else if (stage == 1) {
            int frameIndex = (stageTimer / 10) % armedFrames.length;
            if (armedFrames[frameIndex] != null) {
                img = armedFrames[frameIndex];
            }
        } else if (stage == 2 && explodeFrames[0] != null) {
            img = explodeFrames[0];
            drawWidth = 80;
            drawHeight = 80;
        }
        
        if (img != null) {
            g2d.drawImage(img, x - drawWidth/2, y - drawHeight/2, drawWidth, drawHeight, null);
        }
        
        if (stage < 2) {
            drawHealthBar(g);
        }
    }

    @Override
    public void takeDamage(int damage) {
        // Potato Mine is not eaten by zombies in this version.
    }

    @Override
    public boolean canBeEaten() {
        return false;
    }
}
