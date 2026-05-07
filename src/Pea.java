import java.awt.*;
import java.awt.image.BufferedImage;

public class Pea {
    private int x,y;
    private int speed;
    private BufferedImage image;
    
    public Pea(int x,int y){
        this.x=x;
        this.y=y;
        this.speed=5;
        loadImage();
    }
    
    private void loadImage() {
        image = Plant.loadResourceImage("Bullets/PeaNormal/PeaNormal_0.png");
    }
    
    public void update(){
        x+=speed;
    }
    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        
        if (image != null) {
            g2d.drawImage(image, x - 10, y - 10, 20, 20, null);
        } else {
            g.setColor(Color.GREEN);
            g.fillOval(x-5, y-5, 10, 10);
        }
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
