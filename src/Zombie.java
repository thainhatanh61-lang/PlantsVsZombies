import java.awt.*;
import java.util.List;

public class Zombie {
    private int x, y;
    private int health;
    private int speed;
    private int attackTimer;
    private boolean dead;
    private boolean eating;
    public Zombie(int x, int y){
        this(x, y, 100, 1);
    }
    protected Zombie(int x, int y, int health, int speed){
        this.x = x;
        this.y = y;
        this.health = health;
        this.speed = speed;
        this.attackTimer = 0;
        this.dead = false;
    }
    public void update(List<Plant> plants){
        eating = false;
        for (Plant plant : plants){
            if (!plant.isDead() && 
                Math.abs(y-plant.getY()) <= 35 && 
                Math.abs(x- plant.getX()) <= 35) {
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
            x -= speed;
        }
        if (health <= 0) {
            dead = true;
        }
    }
    public void draw(Graphics g) {
        // Body
        g.setColor(new Color(128, 128, 128));
        g.fillRect(x - 12, y - 25, 24, 45);
        // Head
        g.setColor(new Color(100, 100, 100));
        g.fillOval(x - 10, y - 42, 20, 22);
        // Eyes
        g.setColor(Color.RED);
        g.fillOval(x - 7, y - 38, 5, 5);
        g.fillOval(x + 2, y - 38, 5, 5);
        // Arms
        g.setColor(new Color(110, 110, 110));
        if (eating) {
            g.fillRect(x - 20, y - 22, 10, 5);
            g.fillRect(x + 10, y - 22, 10, 5);
        } else {
            g.fillRect(x - 20, y - 15, 10, 5);
            g.fillRect(x + 10, y - 15, 10, 5);
        }
        // Health bar
        g.setColor(Color.RED);
        g.fillRect(x - 15, y - 48, 30, 4);
        g.setColor(Color.GREEN);
        int healthWidth = (int)(30 * (health / 100.0));
        g.fillRect(x - 15, y - 48, healthWidth, 4);
    }
    public int getX() { 
        return x; 
    }
    public int getY() { 
        return y; 
    }
    public int getHealth() { 
        return health; 
    }
    public boolean isDead() { 
        return dead; 
    }
    public void takeDamage(int damage){
        health -= damage;
    }
}