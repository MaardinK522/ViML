package org.mkproductions.jnn.network.graphics;

import org.mkproductions.jnn.network.NetworkRenderingJPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NetworkRenderingJFrame extends JFrame implements Runnable {
    private static final long FPS = 60;
    private final String title;
    private final NetworkRenderingJPanel networkRenderingJPanel;
    private boolean running = false;
    private final Thread thread;

    public NetworkRenderingJFrame() {
        this.title = "Network Rendering";
        this.thread = new Thread(this);
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> {
            this.stopRendering();
        });
        this.networkRenderingJPanel = new NetworkRenderingJPanel(graphicsDevice.getDefaultConfiguration().getBounds().width, graphicsDevice.getDefaultConfiguration().getBounds().height, closeButton);
        add(this.networkRenderingJPanel);
        setUndecorated(true);
        setTitle(this.title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                System.out.println(title + " frame is closed.");
            }

            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.out.println(title + " frame is closed.");
            }
        });
        addMouseListener(this.networkRenderingJPanel);
        addMouseMotionListener(this.networkRenderingJPanel);
        graphicsDevice.setFullScreenWindow(this);
    }


    @Override
    public void run() {
        while (this.running) {
            this.networkRenderingJPanel.repaint();
            try {
                Thread.sleep(FPS / 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void startRendering() {
        this.running = true;
        this.thread.start();
    }

    public void stopRendering() {
        this.running = false;
        this.thread.interrupt();
        this.dispose();
    }
}
