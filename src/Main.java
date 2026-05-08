import javax.swing.*;

public class Main{
    public static void main(String[] args){
        JFrame frame= new JFrame("Plants Vs Zombies");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900,600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        MenuPanel menuPanel= new MenuPanel(frame);
        frame.add(menuPanel);
        frame.setVisible(true);
    }
}