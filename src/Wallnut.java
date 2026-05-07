import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.List;

public class Wallnut extends Plant {
    private List<BufferedImage> frames;
    private List<BufferedImage> cracked1Frames;
    private List<BufferedImage> cracked2Frames;
    private int frameIndex;
    private int frameTimer;
    private int currentState; // 0 = normal, 1 = cracked1, 2 = cracked2

    public Wallnut(int x, int y) {
        super(x, y, 50, 400); // High health
        loadImage();
        currentState = 0;
    }

    private void loadImage() {
        frames = loadFrames("Plants/WallNut/WallNut/WallNut_");
        cracked1Frames = loadFrames("Plants/WallNut/WallNut_cracked1/WallNut_cracked1_");
        cracked2Frames = loadFrames("Plants/WallNut/WallNut_cracked2/WallNut_cracked2_");
    }

    private List<BufferedImage> loadFrames(String basePath) {
        List<BufferedImage> result = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            BufferedImage img = loadResourceImage(basePath + i + ".png");
            if (img == null) {
                break;
            }
            result.add(img);
        }
        return result;
    }

    @Override
    public void update(List<Zombie> zombies, List<Sun> suns) {
        int newState = health <= maxHealth / 3 ? 2 : (health <= (maxHealth * 2) / 3 ? 1 : 0);
        if (newState != currentState) {
            currentState = newState;
            frameIndex = 0;
            frameTimer = 0;
        }

        List<BufferedImage> currentFrames = getCurrentFrames();
        if (!currentFrames.isEmpty()) {
            frameTimer++;
            if (frameTimer >= 2) {
                frameTimer = 0;
                frameIndex = (frameIndex + 1) % currentFrames.size();
            }
        }
    }

    private List<BufferedImage> getCurrentFrames() {
        if (currentState == 2) {
            return cracked2Frames;
        } else if (currentState == 1) {
            return cracked1Frames;
        } else {
            return frames;
        }
    }
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        
        List<BufferedImage> currentFrames = getCurrentFrames();
        if (!currentFrames.isEmpty()) {
            frameIndex %= currentFrames.size();
        } else {
            frameIndex = 0;
        }
        BufferedImage drawImage = !currentFrames.isEmpty() ? currentFrames.get(frameIndex) : null;
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