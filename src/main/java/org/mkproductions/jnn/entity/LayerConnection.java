package org.mkproductions.jnn.entity;

import java.awt.*;

public class LayerConnection {
    public double[] coords;
    private Color color;

    public LayerConnection() {
        this.coords = new double[0];
        this.color = Color.RED;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setCoords(double[] coords) {
        this.coords = coords;
    }
}
