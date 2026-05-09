import javax.swing.*;

public class Main{
    public static void main(String[] args){
        JFrame frame= new JFrame("Plants Vs Zombies");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900,600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        MainMenuPanel menuPanel = new MainMenuPanel(new Runnable() {
            @Override
            public void run() {
                GamePanel gamePanel = new GamePanel();
                frame.setContentPane(gamePanel);
                frame.revalidate();
                gamePanel.requestFocusInWindow();
            }
        });
        frame.add(menuPanel);
        frame.setVisible(true);
    }
}
