import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MushroomShooterPlant extends Plant {
    private List<BufferedImage> frames;
    private List<BufferedImage> scaredFrames;
    private List<MushroomProjectile> shots;
    private int frameIndex;
    private int frameTimer;
    private int shootTimer;
    private int lifeTimer;
    private boolean scaredy;
    private boolean cowering;

    public MushroomShooterPlant(int x, int y, boolean scaredy) {
        super(x, y, scaredy ? 25 : 0, scaredy ? 300 : 200);
        this.scaredy = scaredy;
        frames = new ArrayList<>();
        scaredFrames = new ArrayList<>();
        shots = new ArrayList<>();

        String base = scaredy ? "Plants/ScaredyShroom/ScaredyShroom/ScaredyShroom_" : "Plants/PuffShroom/PuffShroom/PuffShroom_";
        int max = scaredy ? 17 : 14;
        for (int i = 0; i < max; i++) {
            BufferedImage img = loadResourceImage(base + i + ".png");
            if (img != null) {
                frames.add(img);
            }
        }
        if (scaredy) {
            for (int i = 0; i < 11; i++) {
                BufferedImage img = loadResourceImage("Plants/ScaredyShroom/ScaredyShroomCry/ScaredyShroomCry_" + i + ".png");
                if (img != null) {
                    scaredFrames.add(img);
                }
            }
        }
    }

    @Override
    public void update(List<Zombie> zombies, List<Sun> suns) {
        if (!scaredy) {
            lifeTimer++;
            if (lifeTimer >= 4000) {
                health = 0;
                return;
            }
        }

        cowering = false;
        boolean hasZombieInRow = false;
        for (Zombie zombie : zombies) {
            if (!zombie.isDead() && Math.abs(zombie.getY() - y) <= 35) {
                if (zombie.getX() > x) {
                    hasZombieInRow = true;
                }
                if (scaredy && Math.abs(zombie.getX() - x) <= 160) {
                    cowering = true;
                }
            }
        }

        if (hasZombieInRow && !cowering) {
            shootTimer++;
            if (shootTimer >= 50) {
                shootTimer = 0;
                shots.add(new MushroomProjectile(x + 10, y - 5));
            }
        } else {
            shootTimer = 0;
        }

        for (int i = 0; i < shots.size(); i++) {
            MushroomProjectile shot = shots.get(i);
            shot.update();
            for (Zombie zombie : zombies) {
                if (!zombie.isDead() && shot.hits(zombie)) {
                    zombie.takeDamage(20);
                    break;
                }
            }
            if (shot.isFinished()) {
                shots.remove(i);
                i--;
            }
        }

        List<BufferedImage> currentFrames = cowering && !scaredFrames.isEmpty() ? scaredFrames : frames;
        if (!currentFrames.isEmpty()) {
            frameTimer++;
            if (frameTimer >= 4) {
                frameTimer = 0;
                frameIndex = (frameIndex + 1) % currentFrames.size();
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        List<BufferedImage> currentFrames = cowering && !scaredFrames.isEmpty() ? scaredFrames : frames;
        if (!currentFrames.isEmpty()) {
            g2d.drawImage(currentFrames.get(frameIndex % currentFrames.size()), x - 25, y - 30, 50, 50, null);
        }
        drawHealthBar(g);
        for (MushroomProjectile shot : shots) {
            shot.draw(g);
        }
    }
    @Override
    public boolean canBeEaten() {
        if (scaredy && cowering) {
            return false;
        }
        return super.canBeEaten();
    }
}
