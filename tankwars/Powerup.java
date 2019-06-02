package tankwars;

import java.awt.*;
import java.util.ArrayList;

public class Powerup {
    private int x, y;
    private Image image;
    private boolean shouldBeDisposed;

    public Powerup(int x, int y, Image image) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.shouldBeDisposed = false;
    }

    public void draw(Graphics2D graphics2D) {
        graphics2D.drawImage(image, x, y, null);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Rectangle getBoxRectange() {
        return new Rectangle(x, y, image.getWidth(null), image.getHeight(null));
    }

    public boolean isShouldBeDisposed() {
        return shouldBeDisposed;
    }

    public void setShouldBeDisposed(boolean shouldBeDisposed) {
        this.shouldBeDisposed = shouldBeDisposed;
    }
}
