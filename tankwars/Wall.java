package tankwars;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class Wall {
    private int x, y;
    private Image image;
    private WallType wallType;
    private int width, height;
    private boolean shouldBeDisposed;

    public Wall(int x, int y, Image image, WallType wallType, int width, int height) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.wallType = wallType;
        this.width = width;
        this.height = height;
        this.shouldBeDisposed = false;
    }

    public void draw(Graphics2D graphics2D) {
        graphics2D.drawImage(image, x, y, null);
    }

    public void update(GameField field, ArrayList<Bullet> bullets) {

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Rectangle getBoxRectange() {
        return new Rectangle(x, y, image.getWidth(null), image.getHeight(null));
    }

    public void hit(GameField gameField) {
        if (wallType == WallType.BREAKABLE) {
            shouldBeDisposed = true;
        }
    }

    public boolean isShouldBeDisposed() {
        return shouldBeDisposed;
    }
}
