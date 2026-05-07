import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Sun {
    private int x, y;
    private int targetY;
    private boolean collected = false;
    private int timer = 0;
    private List<BufferedImage> frames;
    private int frameIndex;
    private int frameTimer;
    
    public Sun(int x, int y, int targetY) {
        this.x = x;
        this.y = y;
        this.targetY = targetY;
        loadImages();
    }
    
    private void loadImages() {
        frames = new java.util.ArrayList<>();
        for (int i = 0; i < 22; i++) {
            BufferedImage img = Plant.loadResourceImage("Plants/Sun/Sun_" + i + ".png");
            if (img != null) {
                frames.add(img);
            }
        }
    }
    
    public void update(){
        // Move slowly until reaching the target position
        if (y < targetY) {
            y += 2;
        } else if (y > targetY) {
            y -= 2;
        }
        // Disappear after some time
        timer++;
        if (timer > 300) {
            collected = true;
        }
        // Animate
        if (!frames.isEmpty()) {
            frameTimer++;
            if (frameTimer >= 4) { // Slower animation
                frameTimer = 0;
                frameIndex = (frameIndex + 1) % frames.size();
            }
        }
    }
    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        
        if (!frames.isEmpty()) {
            BufferedImage img = frames.get(frameIndex);
            g2d.drawImage(img, x - 15, y - 15, 30, 30, null);
        } else {
            // Fallback
            g.setColor(Color.YELLOW);
            g.fillOval(x - 12, y - 12, 24, 24);
            g.setColor(Color.ORANGE);
            g.fillOval(x - 8, y - 8, 16, 16);
        }
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
