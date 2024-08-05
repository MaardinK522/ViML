package org.mkproductions.jnn.network.graphics.renderables;

import org.mkproductions.jnn.network.NetworkRenderingJPanel;
import org.mkproductions.jnn.network.helper.TinkeringNetHelper;
import org.mkproductions.jnn.network.graphics.NetworkRenderAble;

import java.awt.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class RenderableLayer extends NetworkRenderAble {
    private final ArrayList<RenderableNeuron> renderableNeurons = new ArrayList<>();
    private final String label;
    private ArrayList<Double> weights;

    public RenderableLayer(int numberOfNeurons) {
        // Here super does output the given values of renderable class.
        super(0, 0, TinkeringNetHelper.getInstance().renderableLayerWidth, (TinkeringNetHelper.getInstance().renderableSpacing + TinkeringNetHelper.getInstance().neuronSize) * numberOfNeurons + TinkeringNetHelper.getInstance().renderableSpacing);
        this.setSelectAble(true);
        this.weights = new ArrayList<>();
//        for (int i = 0; i < numberOfNeurons; i++) {
//            var value = random.nextDouble();
//            this.weights.add(value);
//            System.out.print("\t" + value);
//            renderableNeurons.add(new RenderableNeuron());
//        }
        System.out.println();
        this.setColor(new Color(100, 100, 100));
        this.label = TinkeringNetHelper.getInstance().generateRandomStringOfLength(5);
    }

    public String getLabel() {
        return label;
    }

    public ArrayList<RenderableNeuron> getRenderableNeurons() {
        return renderableNeurons;
    }

    @Override
    public void renderExtras(Graphics2D g) {
        for (RenderableNeuron neuron : renderableNeurons) {
            neuron.render(g);
            if (Objects.equals(NetworkRenderingJPanel.selectedLayersSelectedNeuronLabel, neuron.getLabel()))
                neuron.renderSelected(g);
        }
    }

    public void addRenderableNeuron(RenderableNeuron neuron) {
        this.renderableNeurons.add(neuron);
        this.weights.add(TinkeringNetHelper.getInstance().getRandomNumberBetween(-1.0, 1.0));
    }

    public void removeRenderableNeuron(int neuronIndex) {
        if (!this.renderableNeurons.isEmpty()) {
            this.renderableNeurons.remove(neuronIndex);
            this.weights.remove(neuronIndex);
        }
    }

    public void setWeightAtIndex(int neuronIndex, double weight) {
        this.weights.set(neuronIndex, weight);
    }

    public Double getWeightAtIndex(int index) {
        return this.weights.get(index);
    }

    public ArrayList<Double> getWeights() {
        return weights;
    }

    public void setWeights(ArrayList<Double> weights) {
        this.weights = weights;
    }

    @Override
    public void resetSize() {
        this.setSize(TinkeringNetHelper.getInstance().renderableLayerWidth, (TinkeringNetHelper.getInstance().renderableSpacing + TinkeringNetHelper.getInstance().neuronSize) * this.renderableNeurons.size() + TinkeringNetHelper.getInstance().renderableSpacing);
    }
}



