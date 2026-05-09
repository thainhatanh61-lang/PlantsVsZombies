import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class MainMenuPanel extends JPanel implements MouseListener {
    private BufferedImage backgroundImage;
    private BufferedImage adventure0Image;
    private BufferedImage adventure1Image;
    private Rectangle adventure0Button;
    private Rectangle adventure1Button;
    private Runnable startGame;

    public MainMenuPanel(Runnable startGame) {
        this.startGame = startGame;
        backgroundImage = Plant.loadResourceImage("Screen/MainMenu.png");
        adventure0Image = Plant.loadResourceImage("Screen/Adventure_0.png");
        adventure1Image = Plant.loadResourceImage("Screen/Adventure_1.png");

        int buttonWidth = 166;
        int buttonHeight = 70;
        int graveCenterX = 625;
        int topY = 95;
        int buttonX = graveCenterX - buttonWidth / 2;

        adventure0Button = new Rectangle(buttonX, topY, buttonWidth, buttonHeight);
        adventure1Button = new Rectangle(buttonX, topY + buttonHeight, buttonWidth, buttonHeight);

        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        } else {
            g.setColor(new Color(40, 120, 80));
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        if (adventure0Image != null) {
            g.drawImage(adventure0Image, adventure0Button.x, adventure0Button.y,
                    adventure0Button.width, adventure0Button.height, null);
        }

        if (adventure1Image != null) {
            g.drawImage(adventure1Image, adventure1Button.x, adventure1Button.y,
                    adventure1Button.width, adventure1Button.height, null);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point p = e.getPoint();
        if (adventure0Button.contains(p) || adventure1Button.contains(p)) {
            startGame.run();
        }
    }

    @Override public void mousePressed(MouseEvent e) {
    }
    @Override public void mouseReleased(MouseEvent e) {
    }
    @Override public void mouseEntered(MouseEvent e) {
    }
    @Override public void mouseExited(MouseEvent e) {
    }
}
