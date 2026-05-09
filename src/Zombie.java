import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Zombie {
    protected int x, y;
    protected int health;
    protected int maxHealth;
    protected int speed;
    protected int attackTimer;
    protected boolean dead;
    protected boolean eating;

    // Animation frames
    protected BufferedImage[] walkFrames;
    protected BufferedImage[] attackFrames;
    protected BufferedImage[] dieFrames;
    protected BufferedImage[] lostHeadFrames;
    protected BufferedImage[] lostHeadAttackFrames;

    protected int frameIndex;
    protected int frameTimer;
    protected int drawWidth = 75;
    protected int drawHeight = 110;

    // States
    protected boolean lostHead;
    protected boolean dying;
    protected int dyingTimer;
    protected boolean burning;
    protected int burnTimer;
    protected int frozenTimer;
    protected int chilledTimer;
    protected int chillMoveTimer;
    protected BufferedImage burnImage;
    protected BufferedImage iceTrapImage;

    public Zombie(int x, int y) {
        this(x, y, ZombieStats.NORMAL, 1);
    }

    protected Zombie(int x, int y, int health, int speed) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.maxHealth = health;
        this.speed = speed;
        this.attackTimer = 0;
        this.dead = false;
        this.lostHead = false;
        this.dying = false;
        loadImages();
        burnImage = Plant.loadResourceImage("Effects/burntZombie.gif");
        iceTrapImage = Plant.loadResourceImage("Plants/IceShroom/IceShroomTrap/IceShroomTrap_0.png");
    }

    protected void loadImages() {
        String base = "Zombies/NormalZombie/";
        walkFrames = loadFrameArray(base + "Zombie/Zombie_", 22);
        attackFrames = loadFrameArray(base + "ZombieAttack/ZombieAttack_", 21);
        dieFrames = loadFrameArray(base + "ZombieDie/ZombieDie_", 10);
        lostHeadFrames = loadFrameArray(base + "ZombieLostHead/ZombieLostHead_", 18);
        lostHeadAttackFrames = loadFrameArray(base + "ZombieLostHeadAttack/ZombieLostHeadAttack_", 11);
    }

    protected BufferedImage[] loadFrameArray(String basePath, int maxCount) {
        List<BufferedImage> frameList = new ArrayList<>();
        for (int i = 0; i < maxCount; i++) {
            BufferedImage img = Plant.loadResourceImage(basePath + i + ".png");
            if (img != null) {
                frameList.add(img);
            }
        }
        return frameList.toArray(new BufferedImage[0]);
    }

    public void update(List<Plant> plants) {
        if (burning) {
            burnTimer++;
            if (burnTimer >= 35) {
                dead = true;
            }
            return;
        }

        if (dying) {
            dyingTimer++;
            frameTimer++;
            if (frameTimer >= 5) {
                frameTimer = 0;
                frameIndex++;
                if (dieFrames == null || frameIndex >= dieFrames.length) {
                    dead = true;
                }
            }
            return;
        }

        if (frozenTimer > 0) {
            frozenTimer--;
            if (frozenTimer == 0) {
                chilledTimer = 167;
            }
            return;
        }

        eating = false;
        for (Plant plant : plants) {
            if (!plant.isDead() &&
                plant.canBeEaten() &&
                Math.abs(y - plant.getY()) <= 35 &&
                Math.abs(x - plant.getX()) <= 35) {
                attackTimer++;
                if (attackTimer >= 60) {
                    plant.takeDamage(10);
                    attackTimer = 0;
                }
                eating = true;
                break;
            }
        }
        if (!eating) {
            if (chilledTimer > 0) {
                chilledTimer--;
                chillMoveTimer++;
                if (chillMoveTimer >= 2) {
                    chillMoveTimer = 0;
                    x -= speed;
                }
            } else {
                x -= speed;
            }
        }

        // Lost head starts when zombie is low on health.
        if (!lostHead && health <= maxHealth * 0.3 && health > 0) {
            lostHead = true;
            frameIndex = 0;
            frameTimer = 0;
        }

        if (health <= 0 && !dying) {
            dying = true;
            frameIndex = 0;
            frameTimer = 0;
            dyingTimer = 0;
        }

        // Animate
        frameTimer++;
        if (frameTimer >= 4) {
            frameTimer = 0;
            BufferedImage[] currentFrames = getCurrentFrames();
            if (currentFrames != null && currentFrames.length > 0) {
                frameIndex = (frameIndex + 1) % currentFrames.length;
            }
        }
    }

    protected BufferedImage[] getCurrentFrames() {
        if (dying && dieFrames != null && dieFrames.length > 0) {
            return dieFrames;
        }
        if (lostHead) {
            if (eating && lostHeadAttackFrames != null && lostHeadAttackFrames.length > 0) {
                return lostHeadAttackFrames;
            }
            if (lostHeadFrames != null && lostHeadFrames.length > 0) {
                return lostHeadFrames;
            }
        }
        if (eating && attackFrames != null && attackFrames.length > 0) {
            return attackFrames;
        }
        return walkFrames;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        if (burning) {
            if (burnImage != null) {
                g2d.drawImage(burnImage, x - 35, y - 80, 70, 100, null);
            } else {
                g.setColor(Color.DARK_GRAY);
                g.fillRect(x - 20, y - 70, 40, 70);
            }
            return;
        }

        if (frozenTimer > 0 && iceTrapImage != null) {
            g2d.drawImage(iceTrapImage, x - 35, y - 15, 70, 30, null);
        }

        BufferedImage[] currentFrames = getCurrentFrames();
        if (currentFrames != null && currentFrames.length > 0) {
            int idx = frameIndex % currentFrames.length;
            if (currentFrames[idx] != null) {
                g2d.drawImage(currentFrames[idx],
                    x - drawWidth / 2, y - drawHeight + 30,
                    drawWidth, drawHeight, null);
            }
        }

        if (frozenTimer > 0) {
            g.setColor(new Color(0, 100, 255, 100));
            g.fillRect(x - drawWidth / 2, y - drawHeight + 30, drawWidth, drawHeight);
        }

        // Health bar
        if (!dying) {
            int barWidth = 40;
            g.setColor(Color.RED);
            g.fillRect(x - barWidth / 2, y - drawHeight + 20, barWidth, 4);
            g.setColor(Color.GREEN);
            int filled = (int) (barWidth * (health / (double) maxHealth));
            g.fillRect(x - barWidth / 2, y - drawHeight + 20, Math.max(filled, 0), 4);
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getHealth() { return health; }
    public boolean isDead() { return dead; }
    public void takeDamage(int damage) { health -= damage; }
    public void freeze(int freezeTime, int chillTime) {
        frozenTimer = freezeTime;
        chilledTimer = chillTime;
    }
    public void burnKill() {
        health = 0;
        burning = true;
        burnTimer = 0;
        dying = false;
    }
}
