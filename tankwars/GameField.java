package tankwars;

//import sun.audio.AudioPlayer;
//import sun.audio.AudioStream;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GameField {

    private ResourcesManager resourcesManager;
    private ArrayList<Tank> tanks = new ArrayList<Tank>();
    private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    private ArrayList<Wall> walls = new ArrayList<Wall>();
    private ArrayList<Explosion> explosions = new ArrayList<Explosion>();
    private ArrayList<Powerup> powerups = new ArrayList<Powerup>();
    private int width, height;
    private int powerupSpawnCooldownTime = 1000;
    private int powerupSpawnCooldown = powerupSpawnCooldownTime;

    public GameField(ResourcesManager resourcesManager, int width, int height) {
        this.resourcesManager = resourcesManager;
        this.width = width;
        this.height = height;

        for (int i = 0; i < height; i += 32) {
            for (int j = 0; j < width; j += 32) {
                if (i == 0 || j == 0 || i == height - 32 || j == width - 32) {
                    walls.add(new Wall(j, i, resourcesManager.getWallUnbreakableSprite(), WallType.UNKBREAKABLE, 32, 32));
                }
                if (i == height / 2 || j == width / 2) {
                    walls.add(new Wall(j, i, resourcesManager.getWallBreakableSprite(), WallType.BREAKABLE, 32, 32));
                }
            }
        }
    }

    public void update() {
        powerupSpawnCooldown--;
        if (powerupSpawnCooldown == 0 && powerups.size() < 10) {
            powerupSpawnCooldown = powerupSpawnCooldownTime;
            Random random = new Random();
            powerups.add(new Powerup(random.nextInt(width), random.nextInt(height), resourcesManager.getReloadPowerup()));
        }

        // Handle bullets update
        ArrayList<Bullet> bulletsLeft = new ArrayList<Bullet>();
        for (Bullet bullet: bullets) {
            bullet.update(this);
            if (!bullet.isShouldBeDisposed()) {
                bulletsLeft.add(bullet);
            }
        }
        bullets = bulletsLeft;
        // Handle walls update
        ArrayList<Wall> wallsLeft = new ArrayList<Wall>();
        for (Wall wall: walls) {
            if (!wall.isShouldBeDisposed()) {
                wallsLeft.add(wall);
            }
        }
        walls = wallsLeft;

        // Handle explosions update
        ArrayList<Explosion> explosionsLeft = new ArrayList<Explosion>();
        for (Explosion explosion: explosions) {
            explosion.update();
            if (!explosion.isShouldBeDisposed()) {
                explosionsLeft.add(explosion);
            }
        }
        explosions = explosionsLeft;


        // Handle powerups update
        ArrayList<Powerup> powerupsLeft = new ArrayList<Powerup>();
        for (Powerup powerup: powerups) {
            if (!powerup.isShouldBeDisposed()) {
                powerupsLeft.add(powerup);
            }
        }
        powerups = powerupsLeft;

        for (Tank tank: tanks) {
            tank.update(this);
        }
    }

    public void addTank(Tank tank) {
        tanks.add(tank);
    }

    public void spawnBullet(Tank tank) {
        float vx = (int) Math.round(10 * Math.cos(Math.toRadians(tank.getAngle())));
        float vy = (int) Math.round(10 * Math.sin(Math.toRadians(tank.getAngle())));
        Bullet newBullet = new Bullet(tank.getTeam(), tank.getCenterX(), tank.getCenterY(), vx,
                vy, tank.getAngle(), resourcesManager.getShellSprite());
        bullets.add(newBullet);
    }

    public void spawnExplosion(int x, int y, ExplosionType type) {
        Image img = (type == ExplosionType.SMALL) ? resourcesManager.getExplosionSmallSprite() : resourcesManager.getExplosionBigSprite();
        String explosionFilePath = (type == ExplosionType.SMALL) ? "./resources/Explosion_small.wav" : "./resources/Explosion_large.wav";
        explosions.add(new Explosion(x, y, img, 30));
        /*try {
            AudioStream audioStream = new AudioStream(new FileInputStream(new File(explosionFilePath)));
            AudioPlayer.player.start(audioStream);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public void draw(Graphics2D graphics2D) {
        for (Bullet bullet: bullets) {
            bullet.draw(graphics2D);
        }
        for (Wall wall: walls) {
            wall.draw(graphics2D);
        }
        for (Explosion explosion: explosions) {
            explosion.draw(graphics2D);
        }
        for (Powerup powerup: powerups) {
            powerup.draw(graphics2D);
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public ArrayList<Tank> getTanks() {
        return tanks;
    }

    public ArrayList<Wall> getWalls() {
        return walls;
    }

    public ArrayList<Powerup> getPowerups() {
        return powerups;
    }
}
