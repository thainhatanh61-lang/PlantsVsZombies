import java.awt.*;

public class ConeheadZombie extends Zombie {
    public ConeheadZombie(int x, int y) {
        super(x, y, 200, 1);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        int cx = getX();
        int cy = getY() - 42;
        g.setColor(new Color(255, 165, 0));
        g.fillPolygon(new int[]{cx - 18, cx, cx + 18}, new int[]{cy, cy - 28, cy}, 3);
        g.setColor(new Color(205, 133, 63));
        g.fillRect(cx - 18, cy, 36, 6);
    }
}
