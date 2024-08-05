package org.mkproductions.jnn.network.graphics.renderables;

import org.mkproductions.jnn.entity.ActivationFunction;
import org.mkproductions.jnn.network.helper.TinkeringNetHelper;
import org.mkproductions.jnn.network.graphics.NetworkRenderAble;

import java.awt.*;
import java.util.Random;

public class RenderableNeuron extends NetworkRenderAble {
    private double bias;
    private final String label;

    private ActivationFunction activationFunction;

    public RenderableNeuron() {
        super(0, 0, TinkeringNetHelper.getInstance().neuronSize, TinkeringNetHelper.getInstance().neuronSize);
        this.activationFunction = ActivationFunction.SIGMOID;
        this.setSelectAble(true);
        var random = new Random();
        this.bias = random.nextDouble() * 2 - 1;
        this.setColor(new Color(255, 255, 255));
        this.label = TinkeringNetHelper.getInstance().generateRandomStringOfLength(5);
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }

    public String getLabel() {
        return label;
    }

    public ActivationFunction getActivationFunction() {
        return activationFunction;
    }

    public void setActivationFunction(ActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(this.getColor());
        g.fillOval((int) this.getX(), (int) this.getY(), (int) this.getWidth(), (int) this.getHeight());
        g.setFont(new Font("TimesRoman", Font.PLAIN, 10));
        g.setColor(Color.BLACK);
        g.drawString(this.activationFunction.name(), (int) this.getX(), (int) (this.getY() + this.getHeight() / 2));
    }
}
