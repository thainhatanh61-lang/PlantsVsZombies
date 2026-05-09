import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuPanel extends JPanel{
    private JFrame frame;
    private Image backgroundImage;
    private Image adventureImage;
    private Image adventureHoverImage;
    private Rectangle adventureButton;
    private Rectangle adventureNightButton;
    private boolean hoverAdventure;
    private boolean hoverNight;

    public MenuPanel(JFrame frame){
        this.frame= frame;
        setLayout(null);
        backgroundImage = new ImageIcon("resources/graphics/Screen/MainMenu.png").getImage();
        adventureImage = new ImageIcon("resources/graphics/Screen/Adventure_0.png").getImage();
        adventureHoverImage = new ImageIcon("resources/graphics/Screen/Adventure_1.png").getImage();
        adventureButton = new Rectangle(520, 100, 230, 85);
        adventureNightButton = new Rectangle(520, 190, 230, 85);

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (adventureButton.contains(e.getPoint())) {
                    startGame("day");
                } else if (adventureNightButton.contains(e.getPoint())) {
                    startGame("night");
                }
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                hoverAdventure = adventureButton.contains(e.getPoint());
                hoverNight = adventureNightButton.contains(e.getPoint());
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        Image firstButton = hoverAdventure ? adventureHoverImage : adventureImage;
        Image secondButton = hoverNight ? adventureHoverImage : adventureImage;
        if (firstButton != null) {
            g.drawImage(firstButton, adventureButton.x, adventureButton.y, adventureButton.width, adventureButton.height, null);
        }
        if (secondButton != null) {
            g.drawImage(secondButton, adventureNightButton.x, adventureNightButton.y, adventureNightButton.width, adventureNightButton.height, null);
        }
    }

    private void startGame(String mode){
        GamePanel gamePanel= new GamePanel(mode);
        frame.getContentPane().removeAll();
        frame.add(gamePanel);
        frame.revalidate();
        frame.repaint();
        gamePanel.requestFocus();
    }
}
