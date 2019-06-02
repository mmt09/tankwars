package tankwars;

import java.awt.*;
import java.util.ArrayList;

public class Explosion {
    private int x, y;
    private Image image;
    private boolean shouldBeDisposed;
    private int lifetime;

    public Explosion(int x, int y, Image image, int lifetime) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.shouldBeDisposed = false;
        this.lifetime = lifetime;
    }

    public void draw(Graphics2D graphics2D) {
        if (lifetime > 0) {
            graphics2D.drawImage(image, x, y, null);
        }
    }

    public void update() {
        if (lifetime > 0) {
            lifetime--;
        } else {
            shouldBeDisposed = true;
        }
    }

    public boolean isShouldBeDisposed() {
        return shouldBeDisposed;
    }
}
