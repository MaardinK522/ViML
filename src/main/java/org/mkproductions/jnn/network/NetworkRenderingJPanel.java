package org.mkproductions.jnn.network;

import org.mkproductions.jnn.network.graphics.renderables.RenderableLayer;
import org.mkproductions.jnn.network.graphics.renderables.RenderableNeuron;
import org.mkproductions.jnn.network.graphics.renderers.NetworkRenderingArea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NetworkRenderingJPanel extends JPanel implements MouseMotionListener, MouseListener {
    public static int mouseX = 0;
    public static int mouseY = 0;
    public static String selectedLayerLabel;
    public static String selectedLayersSelectedNeuronLabel;
    private static boolean mousePressed = false;
    private final NetworkRenderingArea networkRenderingArea;
    private final int numberOfInputs = 10;
    private static final int buttonFontSize = 15;

    public NetworkRenderingJPanel(int width, int height, JButton closeButton) {
        super(true);

        Panel windowActionPanel = new Panel();
        Panel buttonPanel = new Panel();

        JButton swiftLayerToLeftJButton = new JButton("<= Swift layer");
        JButton swiftLayerToRightJButton = new JButton("Swift layer =>");
        JButton addLayerButton = new JButton("+ Add layer");
        JButton removeLayerButton = new JButton("- Remove layer");
        JButton addNeuronToLayerButton = new JButton("+O Add neuron to layer");
        JButton removeNeuronToLayer = new JButton("-O Remove neuron to layer");
        JButton clearNetworkJButton = new JButton("X Clear network");
        JButton compileNetworkButton = new JButton("Compile network");
        JButton refreshNetworkButton = new JButton("Refresh network");

        swiftLayerToLeftJButton.setFont(new Font("Arial", Font.BOLD, buttonFontSize));
        swiftLayerToRightJButton.setFont(new Font("Arial", Font.BOLD, buttonFontSize));
        addLayerButton.setFont(new Font("Arial", Font.BOLD, buttonFontSize));
        removeLayerButton.setFont(new Font("Arial", Font.BOLD, buttonFontSize));
        addNeuronToLayerButton.setFont(new Font("Arial", Font.BOLD, buttonFontSize));
        removeNeuronToLayer.setFont(new Font("Arial", Font.BOLD, buttonFontSize));
        compileNetworkButton.setFont(new Font("Arial", Font.BOLD, buttonFontSize));
        clearNetworkJButton.setFont(new Font("Arial", Font.BOLD, buttonFontSize));
        closeButton.setFont(new Font("Arial", Font.BOLD, buttonFontSize));
        refreshNetworkButton.setFont(new Font("Arial", Font.BOLD, buttonFontSize));

        windowActionPanel.setBounds(getWidth() - 1000, 0, 1000, 50);
        windowActionPanel.setLayout(new FlowLayout());
        buttonPanel.setBounds(0, 0, 50, this.getHeight());
        buttonPanel.setLayout(new GridLayout(8, 1));

        buttonPanel.add(addLayerButton);
        buttonPanel.add(removeLayerButton);
        buttonPanel.add(swiftLayerToLeftJButton);
        buttonPanel.add(swiftLayerToRightJButton);
        buttonPanel.add(addNeuronToLayerButton);
        buttonPanel.add(removeNeuronToLayer);
        buttonPanel.add(compileNetworkButton);
        buttonPanel.add(clearNetworkJButton);

        windowActionPanel.add(refreshNetworkButton, FlowLayout.LEFT);
        windowActionPanel.add(closeButton, FlowLayout.LEFT);
        setLayout(new BorderLayout());

        add(windowActionPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.LINE_START);

        setVisible(true);
        setBackground(Color.BLACK);
        setSize(width, height);

        System.out.println("JPanel dimension");
        System.out.println("X: " + this.getX() + ", Y: " + this.getY() + ", Width: " + this.getWidth() + ", Height: " + this.getHeight());

        this.networkRenderingArea = new NetworkRenderingArea(this.numberOfInputs, getX() + 200, getY() + 50, getWidth() - 200, getHeight() - 50);

        swiftLayerToLeftJButton.addActionListener(e -> swiftSelectedLayerToLeft());
        swiftLayerToRightJButton.addActionListener(e -> swiftSelectedLayerToRight());
        addLayerButton.addActionListener(e -> addRenderableLayer());
        removeLayerButton.addActionListener(e -> removeRenderableLayer());
        addNeuronToLayerButton.addActionListener(e -> addNeuronToSelectedLayer());
        removeNeuronToLayer.addActionListener(e -> removeSelectedNeuronFromLayer());
        clearNetworkJButton.addActionListener(e -> clearAllRenderableLayers());
        refreshNetworkButton.addActionListener(e -> this.networkRenderingArea.updateNetworkChanges());
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, getWidth(), getHeight());
        this.networkRenderingArea.render(g);
        this.networkRenderingArea.renderExtras(g);
        g.setFont(new Font("Serif", Font.BOLD, 20));
        g.setColor(Color.WHITE);
        g.drawString("Selected layer: " + NetworkRenderingJPanel.selectedLayerLabel, (int) (this.networkRenderingArea.getX() + 10), getHeight() - 100);
        g.drawString("Selected layer's selected neuron: " + NetworkRenderingJPanel.selectedLayersSelectedNeuronLabel, (int) (this.networkRenderingArea.getX() + 10), getHeight() - 50);
    }

    public void swiftSelectedLayerToLeft() {
        this.networkRenderingArea.swiftSelectedLayerToLeft();
    }

    public void swiftSelectedLayerToRight() {
        this.networkRenderingArea.swiftSelectedLayerToRight();
    }

    public void addRenderableLayer() {
        int selectedLayerIndex = this.networkRenderingArea.getSelectedLayerIndex();
        if (selectedLayerIndex < 0) this.networkRenderingArea.addRenderableLayer(0, new RenderableLayer(0));
        else this.networkRenderingArea.addRenderableLayer(selectedLayerIndex + 1, new RenderableLayer(0));
    }

    public void removeRenderableLayer() {
        if (NetworkRenderingJPanel.selectedLayerLabel != null) {
            int selectedLayerIndex = this.networkRenderingArea.getSelectedLayerIndex();
            this.networkRenderingArea.removeRenderableLayer(selectedLayerIndex);
        }
    }

    public void addNeuronToSelectedLayer() {
        this.networkRenderingArea.addRenderableNeuronToRenderAbleLayerIndex(this.networkRenderingArea.getSelectedLayerIndex(), new RenderableNeuron());
    }

    public void removeSelectedNeuronFromLayer() {
        if (NetworkRenderingJPanel.selectedLayersSelectedNeuronLabel != null) {
            this.networkRenderingArea.removeRenderableNeuron(this.networkRenderingArea.getSelectedLayerIndex(), this.networkRenderingArea.getSelectedLayersSelectedNeuronIndex());
        }
    }

    public void clearAllRenderableLayers() {
        this.networkRenderingArea.clearAllRenderableLayers();
    }


    @Override
    public void mouseMoved(MouseEvent e) {
        NetworkRenderingJPanel.mouseX = e.getX();
        NetworkRenderingJPanel.mouseY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        NetworkRenderingJPanel.mouseX = e.getX();
        NetworkRenderingJPanel.mouseY = e.getY();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.networkRenderingArea.update();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        NetworkRenderingJPanel.mousePressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        NetworkRenderingJPanel.mousePressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

//    @Override
//    public void keyPressed(KeyEvent e) {
//        System.out.println("Key pressed: " + e.getKeyCode());
//        if (e.getKeyCode() == KeyEvent.VK_C) {
//            System.out.println("Control is down.");
//        }
//        if (e.getKeyCode() == KeyEvent.VK_PLUS) {
//            addRenderableLayer();
//        } else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
//            removeRenderableLayer();
//        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
//            swiftSelectedLayerToLeft();
//        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
//            swiftSelectedLayerToRight();
//        }
////        else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
////            running = false;
////        }
//    }
}
