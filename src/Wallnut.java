import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import java.util.List;

public class Wallnut extends Plant {
    private BufferedImage[] frames;
    private int frameIndex;
    private int frameTimer;
    private BufferedImage image;

    public Wallnut(int x, int y) {
        super(x, y, 50, 400); // High health
        loadImage();
    }

    private void loadImage() {
        frames = new BufferedImage[16];
        for (int i = 0; i < frames.length; i++) {
            frames[i] = loadResourceImage("Plants/WallNut/WallNut/WallNut_" + i + ".png");
        }
        image = frames[0];
    }

    @Override
    public void update(List<Zombie> zombies, List<Sun> suns) {
        frameTimer++;
        if (frameTimer >= 2) {
            frameTimer = 0;
            frameIndex = (frameIndex + 1) % frames.length;
        }
    }
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        
        BufferedImage drawImage = frames != null && frames[frameIndex] != null ? frames[frameIndex] : image;
        if (drawImage != null) {
            g2d.drawImage(drawImage, x - 25, y - 30, 50, 50, null);
        } else {
            // Fallback draw
            g.setColor(new Color(139, 90, 43));
            g.fillOval(x - 20, y - 28, 40, 45);
            g.setColor(new Color(101, 67, 33));
            g.drawOval(x - 20, y - 28, 40, 45);
            g.setColor(Color.BLACK);
            g.fillOval(x - 8, y - 15, 5, 5);
            g.fillOval(x + 3, y - 15, 5, 5);
            g.drawArc(x - 8, y - 8, 16, 10, 0, -180);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 10));
            g.drawString("W", x - 5, y + 5);
        }
        drawHealthBar(g);
    }
}