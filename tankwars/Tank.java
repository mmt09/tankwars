package tankwars;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class Tank implements Observer {

    private final int MAX_HEALTH = 5;
    private final int MAX_LIVES = 3;
    private int x;
    private int y;
    private int spawnX, spawnY;
    private final int r = 6;
    private int vx;
    private int vy;
    private short angle;
    private int health;
    private int lives;
    private BufferedImage img;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean ShootPressed;
    private GameField gameField;
    private TankTeam team;
    private int shootCooldownTime = 30;
    private int shootCooldown = 0;

    public Tank(int spawnX, int spawnY, int vx, int vy, short angle, GameField gameField, TankTeam team) {
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.vx = vx;
        this.vy = vy;
        this.angle = angle;
        this.gameField = gameField;
        this.team = team;
        this.lives = MAX_LIVES;
        spawn();
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getCenterX() {
        return x + img.getWidth() / 2;
    }

    public int getCenterY() {
        return y + img.getHeight() / 2;
    }

    public float getAngle() {
        return angle;
    }

    public TankTeam getTeam() {
        return team;
    }

    public void setVx(int vx) {
        this.vx = vx;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }

    public void setAngle(short angle) {
        this.angle = angle;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    public void toggleUpPressed() {
        this.UpPressed = true;
    }

    public void toggleDownPressed() {
        this.DownPressed = true;
    }

    public void toggleRightPressed() {
        this.RightPressed = true;
    }

    public void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    public void toggleShootPressed() {
        this.ShootPressed = true;
    }

    public void unToggleUpPressed() {
        this.UpPressed = false;
    }

    public void unToggleDownPressed() {
        this.DownPressed = false;
    }

    public void unToggleRightPressed() {
        this.RightPressed = false;
    }

    public void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    public void unToggleShootPressed() {
        this.ShootPressed = false;
    }

    public void paintComponent(Graphics g) {
        if (lives == 0) {
            return;
        }
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), img.getWidth() / 2, img.getHeight() / 2);
        Graphics2D graphic2D = (Graphics2D) g;
        graphic2D.drawImage(img, rotation, null);
        graphic2D.setColor(Color.BLACK);
        graphic2D.fillRect(x, y - 10, img.getWidth(), 6);
        graphic2D.setColor(Color.GREEN);
        graphic2D.fillRect(x, y - 10, (int)(img.getWidth() * getHealth()), 6);
    }

    private void rotateLeft() {
        this.angle -= 3;
    }

    private void rotateRight() {
        this.angle += 3;
    }

    private void moveBackwards() {
        vx = (int) Math.round(r * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(r * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
    }

    private void moveForwards() {
        vx = (int) Math.round(r * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(r * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }

    public void spawn() {
        health = MAX_HEALTH;
        x = spawnX;
        y = spawnY;
        angle = 0;
    }

    public double getHealth() {
        return ((double) health) / MAX_HEALTH;
    }

    public void update(GameField gameField) {
        if (lives == 0) {
            return;
        }
        if (shootCooldown > 0) {
            shootCooldown--;
        }
        int prevX = x, prevY = y;
        if (this.UpPressed) {
            this.moveForwards();
        }
        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }
        if (this.RightPressed) {
            this.rotateRight();
        }

        if (this.ShootPressed && shootCooldown == 0) {
            shootCooldown = shootCooldownTime;
            vx = (int) Math.round(r * Math.cos(Math.toRadians(angle)));
            vy = (int) Math.round(r * Math.sin(Math.toRadians(angle)));
            gameField.spawnBullet(this);
        }

        Rectangle box = getBoxRectange();
        for (Wall wall: gameField.getWalls()) {
            Rectangle wallBox = wall.getBoxRectange();
            if (box.intersects(wallBox)) {
                if (vx > 0 && box.x + box.width > wallBox.x) {
                    x = prevX;
                }
                if (vx < 0 && wallBox.x + wallBox.width > box.x) {
                    x = prevX;
                }
                if (vy > 0 && box.y + box.height > wallBox.y) {
                    y = prevY;
                }
                if (vy < 0 && wallBox.y + wallBox.height > box.y) {
                    y = prevY;
                }
            }
        }

        for (Powerup powerup: gameField.getPowerups()) {
            Rectangle powerupBox = powerup.getBoxRectange();
            if (powerupBox.intersects(box)) {
                powerup.setShouldBeDisposed(true);
                shootCooldownTime -= 5;
            }
        }

    }

    public void hit(GameField gameField) {
        health--;
        if (health == 0) {
            spawn();
            gameField.spawnExplosion(getCenterX(), getCenterY(), ExplosionType.LARGE);
            if (lives > 0) {
                lives--;
            }
        }
    }

    public int getLives() {
        return lives;
    }

    public Rectangle getBoxRectange() {
        return new Rectangle(x, y, img.getWidth(null), img.getHeight(null));
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    public GameField getGameField() {
        return gameField;
    }
}
