import java.awt.*;
import java.awt.image.BufferedImage;

public class FlagZombie extends Zombie {
    private BufferedImage[] flagWalkFrames;
    private BufferedImage[] flagAttackFrames;
    private BufferedImage[] flagLostHeadFrames;
    private BufferedImage[] flagLostHeadAttackFrames;

    public FlagZombie(int x, int y) {
        super(x, y, 200, 2); // Same health as normal but faster
    }

    @Override
    protected void loadImages() {
        super.loadImages();
        String base = "Zombies/FlagZombie/";
        flagWalkFrames = loadFrameArray(base + "FlagZombie/FlagZombie_", 12);
        flagAttackFrames = loadFrameArray(base + "FlagZombieAttack/FlagZombieAttack_", 11);
        flagLostHeadFrames = loadFrameArray(base + "FlagZombieLostHead/FlagZombieLostHead_", 18);
        flagLostHeadAttackFrames = loadFrameArray(base + "FlagZombieLostHeadAttack/FlagZombieLostHeadAttack_", 11);
    }

    @Override
    protected BufferedImage[] getCurrentFrames() {
        if (dying && dieFrames != null && dieFrames.length > 0) {
            return dieFrames;
        }
        if (lostHead) {
            if (eating && flagLostHeadAttackFrames != null && flagLostHeadAttackFrames.length > 0) {
                return flagLostHeadAttackFrames;
            }
            if (flagLostHeadFrames != null && flagLostHeadFrames.length > 0) {
                return flagLostHeadFrames;
            }
        }
        if (eating && flagAttackFrames != null && flagAttackFrames.length > 0) {
            return flagAttackFrames;
        }
        if (flagWalkFrames != null && flagWalkFrames.length > 0) {
            return flagWalkFrames;
        }
        return super.getCurrentFrames();
    }
}
