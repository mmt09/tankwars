/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankwars;

//import sun.audio.AudioData;
//import sun.audio.AudioPlayer;
//import sun.audio.AudioStream;
//import sun.audio.ContinuousAudioDataStream;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

public class TankWars extends JFrame {

    private final GameEventObservable geobv;

    public static void main(String[] args) {
        Thread x;
        TankWars tankWars = new TankWars(70, 52);
        tankWars.init();

        try {
            while (true) {
                tankWars.geobv.setChanged();
                tankWars.geobv.notifyObservers();
                tankWars.update();
                tankWars.redraw();
                Thread.sleep(1000/144);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(TankWars.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private int tilesWidth, tilesHeight;
    private final int TILE_WIDTH = 32, TILE_HEIGHT = 32;
    private final int WINDOW_HEIGH = 800, WINDOW_WIDTH = 1000;
    private final int MINIMAP_HEIGHT = 240, MINIMAP_WIDTH = 320;
    private GameField gameField;
    private Tank t1, t2;
    private TankViewPort tvp1, tvp2;
    private LivesDisplay ld1, ld2;
    private Minimap minimap;
    ResourcesManager resourcesManager;

    public TankWars(int tilesWidth, int tilesHeight) {
        this.geobv = new GameEventObservable();
        this.tilesWidth = tilesWidth;
        this.tilesHeight = tilesHeight;
    }

    private void init() {
        try {
            resourcesManager = new ResourcesManager("./resources/");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return;
        }

        setTitle("Tank Wars");

        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setVgap(5);
        this.setLayout(borderLayout);

        gameField = new GameField(resourcesManager, tilesWidth * TILE_WIDTH, tilesHeight * TILE_HEIGHT);

        t1 = new Tank(TILE_WIDTH * 4, TILE_HEIGHT * 4, 0, 0, (short)0, gameField, TankTeam.PLAYER);
        t2 = new Tank(gameField.getWidth() - TILE_WIDTH * 6, gameField.getHeight() - TILE_HEIGHT * 6, 0, 0, (short)0, gameField, TankTeam.ENEMY);

        gameField.addTank(t1);
        gameField.addTank(t2);

        t1.setImg(resourcesManager.getTank1Sprite());
        t2.setImg(resourcesManager.getTank2Sprite());

        TankControl tc1 = new TankControl(t1, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);

        tvp1 = new TankViewPort(t1);
        tvp2 = new TankViewPort(t2);

        ld1 = new LivesDisplay(resourcesManager.getBackgroundSprite(), resourcesManager.getTank1Sprite(), t1, "1");
        ld2 = new LivesDisplay(resourcesManager.getBackgroundSprite(), resourcesManager.getTank2Sprite(), t2, "2");
        minimap = new Minimap();


        JPanel topPanel = new JPanel(new GridLayout(1, 3));
        topPanel.add(ld1);
        topPanel.add(ld2);
        topPanel.add(minimap);
        topPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, MINIMAP_HEIGHT));

        this.add(topPanel, BorderLayout.PAGE_START);

        JPanel splitPanel = new JPanel(new GridLayout(1, 2, 5, 0));
        splitPanel.add(tvp1);
        splitPanel.add(tvp2);
        this.add(splitPanel, BorderLayout.CENTER);
        splitPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGH - MINIMAP_HEIGHT));

        this.addKeyListener(tc1);
        this.addKeyListener(tc2);

        this.geobv.addObserver(t1);
        this.geobv.addObserver(t2);

        this.setSize(1000, 800);
        this.setResizable(false);
        this.pack();
        setLocationRelativeTo(null);
        
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private void drawBackground(Graphics g) {
        Graphics2D graphics2D = (Graphics2D)g;
        for (int i = 0; i < tilesHeight; i += 7) {
            for (int j = 0; j < tilesWidth; j += 10) {
                graphics2D.drawImage(resourcesManager.getBackgroundSprite(), j * TILE_WIDTH, i * TILE_HEIGHT,
                        null);
            }
        }
    }

    private void update() {
        gameField.update();
    }

    private void redraw() {
        Image image = createImage(tilesWidth * TILE_WIDTH, tilesHeight * TILE_HEIGHT);
        drawBackground(image.getGraphics());
        t1.paintComponent(image.getGraphics());
        t2.paintComponent(image.getGraphics());
        gameField.draw((Graphics2D)image.getGraphics());

        minimap.setGameImage(image);
        tvp1.setGameImage(image);
        tvp2.setGameImage(image);

        this.repaint();
    }

//    @Override
//    public void run() {
//        while (true) {
//            try {
//                this.geobv.notifyObservers();
//                Thread.sleep(1000 / 144);
//            } catch (InterruptedException ex) {
//                System.out.println(ex.getMessage());
//            }
//        }
//    }

}
