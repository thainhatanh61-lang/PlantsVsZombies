import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
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
        adventureImage = Plant.loadResourceImage("Screen/Adventure_0.png");
        adventureHoverImage = Plant.loadResourceImage("Screen/Adventure_1.png");
        // Brighten adventure_1 to match adventure_0 if it's too gray
        if (adventureHoverImage instanceof BufferedImage) {
            BufferedImage bi = (BufferedImage) adventureHoverImage;
            for (int y = 0; y < bi.getHeight(); y++) {
                for (int x = 0; x < bi.getWidth(); x++) {
                    int argb = bi.getRGB(x, y);
                    int a = (argb >> 24) & 0xFF;
                    int r = (argb >> 16) & 0xFF;
                    int g = (argb >> 8) & 0xFF;
                    int b = argb & 0xFF;
                    r = Math.min(255, (int)(r * 1.2));
                    g = Math.min(255, (int)(g * 1.2));
                    b = Math.min(255, (int)(b * 1.2));
                    bi.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
                }
            }
        }
        adventureButton = new Rectangle(450, 80, 360, 100);
        adventureNightButton = new Rectangle(440, 200, 360, 100);

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
        Graphics2D g2d = (Graphics2D) g;
        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        } else {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }

        drawMenuButton(g2d, adventureImage, adventureButton, hoverAdventure);
        drawMenuButton(g2d, adventureHoverImage, adventureNightButton, hoverNight);
    }

    private void drawMenuButton(Graphics2D g2d, Image img, Rectangle rect, boolean isHovered) {
        if (img != null) {
            int x = rect.x;
            int y = rect.y;
            int w = rect.width;
            int h = rect.height;
            
            if (isHovered) {
                // Scale up slightly on hover
                int offset = 10;
                x -= offset;
                y -= offset;
                w += offset * 2;
                h += offset * 2;
            }
            
            g2d.drawImage(img, x, y, w, h, null);
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
