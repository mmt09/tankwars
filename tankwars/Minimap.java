package tankwars;

import javax.swing.*;
import java.awt.*;

public class Minimap extends JComponent {

    private Image gameImage;

    public void setGameImage(Image gameImage) {
        this.gameImage = gameImage;
    }

    @Override
    public void paintComponent(Graphics g) {
        if (gameImage != null) {
            Graphics2D graphics2D = (Graphics2D)g;
            graphics2D.drawImage(gameImage, 0, 0, getWidth(), getHeight(),this);
        }
    }
}
