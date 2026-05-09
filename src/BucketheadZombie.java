import java.awt.*;
import java.awt.image.BufferedImage;

public class BucketheadZombie extends Zombie {
    private BufferedImage[] bucketWalkFrames;
    private BufferedImage[] bucketAttackFrames;

    public BucketheadZombie(int x, int y) {
        super(x, y, 1300, 1);
    }

    @Override
    protected void loadImages() {
        super.loadImages();
        String base = "Zombies/BucketheadZombie/";
        bucketWalkFrames = loadFrameArray(base + "BucketheadZombie/BucketheadZombie_", 15);
        bucketAttackFrames = loadFrameArray(base + "BucketheadZombieAttack/BucketheadZombieAttack_", 11);
    }

    @Override
    protected BufferedImage[] getCurrentFrames() {
        if (health > 200) {
            if (dying && dieFrames != null && dieFrames.length > 0) {
                return dieFrames;
            }
            if (eating && bucketAttackFrames != null && bucketAttackFrames.length > 0) {
                return bucketAttackFrames;
            }
            if (bucketWalkFrames != null && bucketWalkFrames.length > 0) {
                return bucketWalkFrames;
            }
        }
        return super.getCurrentFrames();
    }
}
