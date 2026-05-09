import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SunshroomPlant extends Plant {
    private List<BufferedImage> frames;
    private int frameIndex;
    private int frameTimer;
    private int sunTimer;
    private int ageTimer;

    public SunshroomPlant(int x, int y) {
        super(x, y, 25, 300);
        frames = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            BufferedImage img = loadResourceImage("Plants/SunShroom/SunShroom/SunShroom_" + i + ".png");
            if (img != null) {
                frames.add(img);
            }
        }
    }

    @Override
    public void update(List<Zombie> zombies, List<Sun> suns) {
        ageTimer++;
        sunTimer++;
        if (sunTimer >= 800) {
            sunTimer = 0;
            int value = 15;
            if (ageTimer >= 2400) {
                value = 50;
            } else if (ageTimer >= 1200) {
                value = 25;
            }
            suns.add(new Sun(x, y, y - 40, value));
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
