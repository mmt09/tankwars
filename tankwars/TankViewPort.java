package tankwars;

import javax.swing.*;
import java.awt.*;

public class TankViewPort extends JComponent {

    private Image gameImage;
    private Tank tank;

    public TankViewPort(Tank tank) {
        this.tank = tank;
    }

    public void setGameImage(Image gameImage) {
        this.gameImage = gameImage;
    }

    @Override
    public void paintComponent(Graphics g) {
        if (gameImage != null) {
            Graphics2D graphics2D = (Graphics2D)g;
            int x = tank.getCenterX() - getWidth() / 2;
            int y = tank.getCenterY() - getHeight() / 2;
            x = Math.max(0, x);
            y = Math.max(0, y);
            x = Math.min(x, tank.getGameField().getWidth() - getWidth());
            y = Math.min(y, tank.getGameField().getHeight() - getHeight());
            graphics2D.drawImage(gameImage, -x, -y, this);
        }
    }
}
