package org.mkproductions.jnn.network.graphics;

import org.mkproductions.jnn.network.NetworkRenderingJPanel;
import org.mkproductions.jnn.network.helper.TinkeringNetHelper;

import java.awt.*;

public abstract class NetworkRenderAble {
    private double x;
    private double y;
    private double width;
    private double height;
    private Color color = new Color(0, 0, 0, 0);
    private boolean isSelectAble = false;

    public NetworkRenderAble(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isSelectAble = false;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return this.width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return this.height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isSelectAble() {
        return this.isSelectAble;
    }

    public void setSelectAble(boolean selectAble) {
        this.isSelectAble = selectAble;
    }

    public void render(Graphics2D g) {
        if (this.isSelectAble) if (this.checkIfMouseInside()) this.renderHovered(g);
        g.setColor(this.color);
        g.fillRoundRect((int) x, (int) y, (int) width, (int) height, (int) (width * 0.10), (int) (height * 0.1));
    }

    public void renderExtras(Graphics2D g) {
    }

    public void renderHovered(Graphics2D g) {
        g.setColor(TinkeringNetHelper.getInstance().hoveredElementColor);
        g.fillRect((int) (x - TinkeringNetHelper.getInstance().renderableSpacing / 2), (int) (y - TinkeringNetHelper.getInstance().renderableSpacing / 2), (int) (width + TinkeringNetHelper.getInstance().renderableSpacing), (int) (height + TinkeringNetHelper.getInstance().renderableSpacing));
    }

    public void renderSelected(Graphics2D g) {
        g.setColor(TinkeringNetHelper.getInstance().selectedElementColor);
        g.fillRect((int) (x - TinkeringNetHelper.getInstance().renderableSpacing / 2), (int) (y - TinkeringNetHelper.getInstance().renderableSpacing / 2), (int) (width + TinkeringNetHelper.getInstance().renderableSpacing), (int) (height + TinkeringNetHelper.getInstance().renderableSpacing));
    }

    public boolean checkIfMouseInside() {
        if (this.x <= NetworkRenderingJPanel.mouseX && NetworkRenderingJPanel.mouseX <= this.x + this.getWidth())
            return this.y <= NetworkRenderingJPanel.mouseY && NetworkRenderingJPanel.mouseY <= this.y + this.getHeight();
        return false;
    }


    public void update() {
    }

    public void resetSize() {
    }

    protected void setSize(double width, double height) {
        this.width = width;
        this.height = height;
    }
}
