package tankwars;

import javax.swing.*;
import java.awt.*;

public class LivesDisplay extends JComponent {

    private Image backgroundImage;
    private Image tankImage;
    private String playerName;
    private Tank tank;

    public LivesDisplay(Image backgroundImage, Image tankImage, Tank tank, String playerName) {
        this.backgroundImage = backgroundImage;
        this.tankImage = tankImage;
        this.tank = tank;
        this.playerName = playerName;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D graphics2d = (Graphics2D)g;
        graphics2d.drawImage(backgroundImage, 0, 0, this);
        graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2d.setColor(Color.BLACK);
        graphics2d.setFont(new Font("Arial", Font.BOLD, 40));
        if (tank.getLives() == 0) {
            graphics2d.drawString("Player " + playerName + ":", 0, 40);
            graphics2d.drawString("GAME OVER!", 0, 90);
        } else {
            graphics2d.drawString("Player " + playerName + " lives:", 0, 40);
            for (int i = 0; i < tank.getLives(); i++) {
                graphics2d.drawImage(tankImage, tankImage.getWidth(this) * i + 20 * (i + 1), 50,
                        tankImage.getWidth(this), tankImage.getHeight(this), this);
            }
        }
    }
}
