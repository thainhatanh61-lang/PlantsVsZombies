import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BasicPlant extends Plant {
    private List<BufferedImage> frames;
    private int frameIndex;
    private int frameTimer;

    public BasicPlant(int x, int y, int cost, int health, String framePath, int maxFrames) {
        super(x, y, cost, health);
        frames = new ArrayList<>();
        for (int i = 0; i < maxFrames; i++) {
            BufferedImage img = loadResourceImage(framePath + i + ".png");
            if (img != null) {
                frames.add(img);
            }
        }
    }

    @Override
    public void update(List<Zombie> zombies, List<Sun> suns) {
        if (!frames.isEmpty()) {
            frameTimer++;
            if (frameTimer >= 4) {
                frameTimer = 0;
                frameIndex = (frameIndex + 1) % frames.size();
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        if (!frames.isEmpty()) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.drawImage(frames.get(frameIndex), x - 25, y - 30, 50, 50, null);
        } else {
            g.setColor(Color.GREEN);
            g.fillOval(x - 20, y - 25, 40, 45);
        }
        drawHealthBar(g);
    }
}
