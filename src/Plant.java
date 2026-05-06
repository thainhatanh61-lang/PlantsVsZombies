import java.awt.*;
import java.util.List;

public abstract class Plant {
    protected int x,y;
    protected int health;
    protected int cost;
    public Plant(int x,int y,int cost, int health){
        this.x = x;
        this.y = y;
        this.cost = cost;
        this.health = health;
    }
    public abstract void update(List<Zombie> zombies, List<Sun> suns);
    public abstract void draw(Graphics g);
    public void takeDamege(int damge){
        health-=damege;
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
