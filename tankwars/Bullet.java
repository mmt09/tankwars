package tankwars;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Bullet {
    private TankTeam owner;
    private float x, y;
    private float dx, dy;
    private float angle;
    private Image image;
    private boolean shouldBeDisposed;

    public Bullet(TankTeam owner, float x, float y, float dx, float dy, float angle, Image image) {
        this.owner = owner;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.angle = angle;
        this.image = image;
        this.shouldBeDisposed = false;
    }

    public void draw(Graphics2D graphics2D) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), image.getWidth(null) / 2, image.getHeight(null) / 2);
        graphics2D.drawImage(image, rotation, null);
    }

    public void update(GameField field) {
        x += dx;
        y += dy;
        shouldBeDisposed = shouldBeDisposed || x < 0 || x > field.getWidth() || y < 0 || y > field.getHeight();

        Rectangle sizeRect = getBoxRectange();
        for (Wall wall: field.getWalls()) {
            if (!shouldBeDisposed && !wall.isShouldBeDisposed() && sizeRect.intersects(wall.getBoxRectange())) {
                destroy(field);
                wall.hit(field);
            }
        }

        for (Tank tank: field.getTanks()) {
            if (!shouldBeDisposed && tank.getLives() > 0 && sizeRect.intersects(tank.getBoxRectange()) && owner != tank.getTeam()) {
                destroy(field);
                tank.hit(field);
            }
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Rectangle getBoxRectange() {
        return new Rectangle((int)x, (int)y, image.getWidth(null), image.getHeight(null));
    }

    public void destroy(GameField gameField) {
        shouldBeDisposed = true;
        gameField.spawnExplosion((int)x, (int)y, ExplosionType.SMALL);
    }

    public boolean isShouldBeDisposed() {
        return shouldBeDisposed;
    }
}
