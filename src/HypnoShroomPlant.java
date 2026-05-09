import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class HypnoShroomPlant extends Plant {
    private List<BufferedImage> frames;
    private int frameIndex;
    private int frameTimer;

    public HypnoShroomPlant(int x, int y) {
        super(x, y, 75, 300);
        frames = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            BufferedImage img = loadResourceImage("Plants/HypnoShroom/HypnoShroom/HypnoShroom_" + i + ".png");
            if (img != null) {
                frames.add(img);
            }
        }
    }

    @Override
    public void update(List<Zombie> zombies, List<Sun> suns) {
        for (Zombie zombie : zombies) {
            if (!zombie.isDead() && !zombie.isFriendly() && Math.abs(zombie.getY() - y) <= 35 && Math.abs(zombie.getX() - x) <= 35) {
                zombie.hypnotize();
                health = 0;
                break;
            }
        }

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
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        if (!frames.isEmpty()) {
            g2d.drawImage(frames.get(frameIndex), x - 25, y - 30, 50, 50, null);
        }
        drawHealthBar(g);
    }
}
