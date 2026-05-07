import java.awt.*;
import java.awt.image.BufferedImage;

public class ConeheadZombie extends Zombie {
    private BufferedImage[] coneWalkFrames;
    private BufferedImage[] coneAttackFrames;

    public ConeheadZombie(int x, int y) {
        super(x, y, 370, 1);
    }

    @Override
    protected void loadImages() {
        super.loadImages(); // Load normal zombie frames as fallback
        String base = "Zombies/ConeheadZombie/";
        coneWalkFrames = loadFrameArray(base + "ConeheadZombie/ConeheadZombie_", 21);
        coneAttackFrames = loadFrameArray(base + "ConeheadZombieAttack/ConeheadZombieAttack_", 11);
    }

    @Override
    protected BufferedImage[] getCurrentFrames() {
        // If cone is still intact (health > normal zombie threshold), use cone frames
        if (health > 100) {
            if (dying && dieFrames != null && dieFrames.length > 0) {
                return dieFrames;
            }
            if (eating && coneAttackFrames != null && coneAttackFrames.length > 0) {
                return coneAttackFrames;
            }
            if (coneWalkFrames != null && coneWalkFrames.length > 0) {
                return coneWalkFrames;
            }
        }
        // Cone destroyed, fall back to normal zombie frames
        return super.getCurrentFrames();
    }
}
