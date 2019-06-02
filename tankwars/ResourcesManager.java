package tankwars;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ResourcesManager {
    private BufferedImage tank1Sprite;
    private BufferedImage tank2Sprite;
    private BufferedImage backgroundSprite;
    private BufferedImage shellSprite;
    private BufferedImage rocketSprite;
    private BufferedImage wallBreakableSprite;
    private BufferedImage wallUnbreakableSprite;
    private BufferedImage explosionSmallSprite;
    private BufferedImage explosionBigSprite;
    private BufferedImage reloadPowerup;

    private void toTransparent(BufferedImage image) {
        // Get "transparent" color from left top pixel
        int transpColor = image.getRGB(0, 0) & 0x00FFFFFF;

        // Process each pixel
        for (int y = 0; y < image.getHeight(); ++y) {
            for (int x = 0; x < image.getWidth(); ++x) {
                int argb = image.getRGB(x, y);

                // If it's color is the same as "transparent" make it really transparent
                if ((argb & 0x00FFFFFF) == transpColor)
                {
                    image.setRGB(x, y, 0);
                }
            }
        }
    }

    private BufferedImage loadImage(String path) throws Exception {
        BufferedImage image = ImageIO.read(new File(path));
        toTransparent(image);
        return image;
    }

    public ResourcesManager(String resourcesFolder) throws Exception {
        tank1Sprite = loadImage(resourcesFolder + "Tank1.gif");
        tank2Sprite = loadImage(resourcesFolder + "Tank2.gif");
        backgroundSprite = loadImage(resourcesFolder + "Background.bmp");
        shellSprite = loadImage(resourcesFolder + "Shell.gif");
        rocketSprite = loadImage(resourcesFolder + "Rocket.gif");
        wallBreakableSprite = loadImage(resourcesFolder + "Wall2.gif");
        wallUnbreakableSprite = loadImage(resourcesFolder + "Wall1.gif");
        explosionSmallSprite = loadImage(resourcesFolder + "Explosion_small.gif");
        explosionBigSprite = loadImage(resourcesFolder + "Explosion_large.gif");
        reloadPowerup = loadImage(resourcesFolder + "reload_powerup.png");
    }

    public BufferedImage getTank1Sprite() {
        return tank1Sprite;
    }

    public BufferedImage getTank2Sprite() {
        return tank2Sprite;
    }

    public BufferedImage getBackgroundSprite() {
        return backgroundSprite;
    }

    public BufferedImage getRocketSprite() {
        return rocketSprite;
    }

    public BufferedImage getShellSprite() {
        return shellSprite;
    }

    public BufferedImage getWallBreakableSprite() {
        return wallBreakableSprite;
    }

    public BufferedImage getWallUnbreakableSprite() {
        return wallUnbreakableSprite;
    }

    public BufferedImage getExplosionBigSprite() {
        return explosionBigSprite;
    }

    public BufferedImage getExplosionSmallSprite() {
        return explosionSmallSprite;
    }

    public BufferedImage getReloadPowerup() {
        return reloadPowerup;
    }
}
