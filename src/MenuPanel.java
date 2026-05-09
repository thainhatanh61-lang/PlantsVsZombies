import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuPanel extends JPanel{
    private GamePanel gamePanel;
    private JFrame frame;
    public MenuPanel(JFrame frame){
        this.frame= frame;
        setLayout(null);
        setBackground(new Color(0,0,0));
        JLabel background= new JLabel();
        background.setBounds(0,0,900,600);
        add(background);
        JLabel title= new JLabel("PLANTS VS ZOMBIES", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD,48));
        title.setForeground(Color.WHITE);
        title.setBounds(0,100,900,80);
        add(title);
        JButton bDay= createButton("DAY", 350,250,200,60);
        bDay.addActionListener(e-> startGame("day"));
        add(bDay);
        JButton bNight= createButton("NIGHT", 350,330,200,60);
        bNight.addActionListener(e-> startGame("night"));
        add(bNight);
        JButton bExit= createButton("EXIT", 350,410,200,60);
        bExit.addActionListener(e-> System.exit(0));
        add(bExit);
    }
    public JButton createButton(String text, int x,int y,int w,int h){
        JButton button= new JButton(text);
        button.setBounds(x,y,w,h);
        button.setFont(new Font("Arial", Font.BOLD,24));
        button.setFocusPainted(false);
        button.setBackground(new Color(34,139,34));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e){
                button.setBackground(new Color(50,205,50));
            }
            public void mouseExited(MouseEvent e){
                button.setBackground(new Color(34,139,34));
            }            
        });
        return button;
    }
    private void startGame(String mode){
        gamePanel= new GamePanel(mode);
        frame.getContentPane().removeAll();
        frame.add(gamePanel);
        frame.revalidate();
        frame.repaint();
        gamePanel.requestFocus();
    }
}
