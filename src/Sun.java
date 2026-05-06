import java.awt.*;

public class Sun {
    private int x, y;
    private int targetY;
    private boolean collected = false;
    private int timer = 0;
    
    public Sun(int x, int y, int targetY) {
        this.x = x;
        this.y = y;
        this.targetY = targetY;
    }
    public void update(){
        // Fall down slowly
        if (y < targetY) {
            y += 2;
        }
        // Disappear after some time
        timer++;
        if (timer > 300) {
            collected = true;
        }
    }
    public void draw(Graphics g){
        // Draw sun shape
        g.setColor(Color.YELLOW);
        g.fillOval(x - 12, y - 12, 24, 24);
        g.setColor(Color.ORANGE);
        g.fillOval(x - 8, y - 8, 16, 16);
    }
    public boolean contains(int mx, int my){
        return Math.abs(mx - x) < 15 && Math.abs(my - y) < 15;
    }
    public void collect(){
        collected = true;
    }
    public boolean isCollected(){
        return collected;
    }
    public int getX(){ 
        return x;
    }
    public int getY(){
        return y; 
    }
}