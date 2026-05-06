import javax.swing.*;

public class Main{
    public static void main(String[] args){
        JFrame frame= new JFrame("Plants Vs Zombies");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900,600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        GamePanel gamePanel= new GamePanel();
        frame.add(gamePanel);
        frame.setVisible(true);
    }
}