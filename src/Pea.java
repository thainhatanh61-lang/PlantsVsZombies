import java.awt.*;

public class Pea {
    private int x,y;
    private int speed;
    public Pea(int x,int y){
        this.x=x;
        this.y=y;
        this.speed=5;
    }
    public void update(){
        x+=speed;
    }
    public void draw(Graphics g){
        g.setColor(Color.GREEN);
        g.fillOval(x-5, y-5, 10, 10);
    }
    public boolean hits(Zombie zombie){
        return Math.abs(x-zombie.getX())<30&& Math.abs(y-zombie.getY())<30;
    }
    public int getX(){
        return x;
    }
    public int getY() {
        return y;
    }
    public boolean isOffScreen(){
        return x>900;
    }
}
