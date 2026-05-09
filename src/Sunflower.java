import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import java.util.List;

public class Sunflower extends Plant{
    private int timer=0;
    private BufferedImage[] frames;
    private int frameIndex;
    private int frameTimer;

    public Sunflower(int x, int y){
        super(x,y,50,300);
        loadImage();
    }

    private void loadImage() {
        String base = "Plants/SunFlower/";
        frames = new BufferedImage[18];
        for (int i = 0; i < 18; i++) {
            Image img = loadResourceImage(base + "SunFlower_" + i + ".png");
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
        timer++;
        if (timer>=500){
            timer=0;
            suns.add(new Sun(x,y,y-40));
        }
        frameTimer++;
        if (frameTimer >= 3) {
            frameTimer = 0;
            frameIndex = (frameIndex + 1) % frames.length;
        }
    }
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        
        if (frames != null && frameIndex < frames.length && frames[frameIndex] != null) {
            g2d.drawImage(frames[frameIndex], x - 25, y - 30, 50, 50, null);
        }
        drawHealthBar(g);
    }
}
