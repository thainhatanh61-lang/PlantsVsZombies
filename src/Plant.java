import java.awt.*;
import java.util.List;

public abstract class Plant {
    protected int x,y;
    protected int health;
    protected int maxHealth;
    protected int cost;
    public Plant(int x,int y,int cost, int health){
        this.x = x;
        this.y = y;
        this.cost = cost;
        this.health = health;
        this.maxHealth = health;
    }
    public abstract void update(List<Zombie> zombies, List<Sun> suns);
    public abstract void draw(Graphics g);
    protected void drawHealthBar(Graphics g){
        int barWidth = 40;
        int filled = (int)(barWidth * (health / (double)maxHealth));
        g.setColor(Color.RED);
        g.fillRect(x - 20, y - 55, barWidth, 5);
        g.setColor(Color.GREEN);
        g.fillRect(x - 20, y - 55, filled, 5);
    }
    public void takeDamage(int damage){
        health-=damage;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getCost() {
        return cost;
    }
    public int getHealth() {
        return health;
    }
    public boolean isDead(){
        return health<=0;
    }
}
