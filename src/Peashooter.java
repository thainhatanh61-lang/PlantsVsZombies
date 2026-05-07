import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Peashooter extends Plant{
    private List<Pea> peas;
    private int shootTimer;
    private int row;
    public Peashooter(int x,int y, int row){
        super(x,y,100,100);
        this.row=row;
        peas = new ArrayList<>();
        shootTimer=0;
    }
    @Override
    public void update(List<Zombie> zombies, List<Sun> suns){
        shootTimer++;
        if (shootTimer>=60){
            shootTimer=0;
            peas.add(new Pea(x+10,y-5));
        }
        for (int i=0; i<peas.size();i++){
            peas.get(i).update();
            boolean hit=false;
            for (Zombie zombie: zombies){
                if (!zombie.isDead()&& peas.get(i).hits(zombie)){
                    zombie.takeDamage(20);
                    hit =true;
                    break;
                }
            }
            if (hit||peas.get(i).isOffScreen()){
                peas.remove(i);
                i--;
            }
        }
    }
    @Override
    public void draw(Graphics g){
        g.setColor(new Color(34,180,34));
        g.fillOval(x-18, y-30, 36, 36);
        g.setColor(new Color(0, 130, 0));
        g.fillRect(x + 10, y - 12, 20, 10);
        g.setColor(Color.GREEN);
        g.fillRect(x - 3, y + 6, 6, 18);
        drawHealthBar(g);
        for (Pea pea : peas) {
            pea.draw(g);
        }
    }
}
