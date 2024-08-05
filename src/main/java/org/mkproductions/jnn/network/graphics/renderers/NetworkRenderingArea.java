package org.mkproductions.jnn.network.graphics.renderers;

import org.mkproductions.jnn.entity.LayerConnection;
import org.mkproductions.jnn.network.NetworkRenderingJPanel;
import org.mkproductions.jnn.network.graphics.renderables.RenderableNeuron;
import org.mkproductions.jnn.network.helper.TinkeringNetHelper;
import org.mkproductions.jnn.network.graphics.NetworkRenderAble;
import org.mkproductions.jnn.network.graphics.renderables.RenderableLayer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class NetworkRenderingArea extends NetworkRenderAble {
    private ArrayList<LayerConnection> layerConnections = new ArrayList<>();

    private final ArrayList<RenderableLayer> renderableLayers = new ArrayList<>();
    private final int numberOfInputs;

    public NetworkRenderingArea(int numberOfInputs, double x, double y, double width, double height) {
        super(x, y, width, height);
        this.numberOfInputs = numberOfInputs;
        this.setColor(new Color(50, 50, 50));
    }

    public void updateNetworkConnectionRails() {

    }

    public void compileNetworkChanges() {
        // TODO : Fetch the neurons data to be converted later into neural network weights.
    }

    public void updateNetworkChanges() {
        this.layerConnections.clear();
        int spacingScale = (this.renderableLayers.size() == 1) ? 0 : 1;
        // Updating layers location as per the center of the screen without the zoom feature.
        double layerX = this.getX() + (this.getWidth() - (this.renderableLayers.size() * TinkeringNetHelper.getInstance().renderableLayerWidth) - (spacingScale * TinkeringNetHelper.getInstance().renderableSpacing)) / 2;
        for (RenderableLayer layer : this.renderableLayers) {
            layer.resetSize();
            double layerY = this.getY() + ((this.getHeight() - layer.getHeight()) / 2);
            layer.setX(layerX);
            layer.setY(layerY);
            // Updating neurons location as per the size of the layer
            double neuronX = layer.getX() + (layer.getWidth() - TinkeringNetHelper.getInstance().neuronSize) / 2;
            double neuronY = layer.getY() + TinkeringNetHelper.getInstance().renderableSpacing;
            for (int neuronIndex = 0; neuronIndex < layer.getRenderableNeurons().size(); neuronIndex++) {
                RenderableNeuron neuron = layer.getRenderableNeurons().get(neuronIndex);
                if (neuronIndex != 0) neuronY += TinkeringNetHelper.getInstance().renderableSpacing;
                neuron.setX(neuronX);
                neuron.setY(neuronY);
                neuronY += TinkeringNetHelper.getInstance().neuronSize;
            }
            layerX += layer.getWidth() + TinkeringNetHelper.getInstance().renderableLayerSpacing;
        }
        for (int currentLayerIndex = 1; currentLayerIndex < this.renderableLayers.size(); currentLayerIndex++) {
            // TODO: Get the weights from the layers as per the indexes.
            int previousLayerIndex = currentLayerIndex - 1;
            RenderableLayer previousLayer = this.renderableLayers.get(previousLayerIndex);
            while (previousLayer.getRenderableNeurons().isEmpty() && previousLayerIndex != 0) {
                previousLayerIndex--;
                previousLayer = this.renderableLayers.get(previousLayerIndex);
            }
            RenderableLayer currentLayer = this.renderableLayers.get(currentLayerIndex);
            ArrayList<RenderableNeuron> previousLayerNeurons = previousLayer.getRenderableNeurons();
            for (RenderableNeuron previousLayerNeuron : previousLayerNeurons) {
                ArrayList<RenderableNeuron> currentLayerNeurons = currentLayer.getRenderableNeurons();
                for (int currentLayerNeuronIndex = 0; currentLayerNeuronIndex < currentLayerNeurons.size(); currentLayerNeuronIndex++) {
                    RenderableNeuron currentLayerNeuron = currentLayerNeurons.get(currentLayerNeuronIndex);
                    var layerConnection = new LayerConnection();
                    if (currentLayer.getWeightAtIndex(currentLayerNeuronIndex) > 0.0) {
                        layerConnection.setColor(Color.GREEN);
                    }
                    layerConnection.setCoords(new double[]{previousLayerNeuron.getX() + previousLayerNeuron.getWidth() / 2, previousLayerNeuron.getY() + previousLayerNeuron.getHeight() / 2, currentLayerNeuron.getX() + currentLayerNeuron.getWidth() / 2, currentLayerNeuron.getY() + currentLayerNeuron.getHeight() / 2});
                    this.layerConnections.add(layerConnection);
                }
            }
        }
    }

    public void swiftSelectedLayerToLeft() {
        int selectedLayerIndex = getSelectedLayerIndex();
        if (selectedLayerIndex > 0) {
            // Swap the selected layer with the layer to its left
            RenderableLayer tempLayer = this.renderableLayers.get(selectedLayerIndex);
            this.renderableLayers.set(selectedLayerIndex, this.renderableLayers.get(selectedLayerIndex - 1));
            this.renderableLayers.set(selectedLayerIndex - 1, tempLayer);
            this.updateNetworkChanges();
        }
    }

    public void swiftSelectedLayerToRight() {
        int selectedLayerIndex = getSelectedLayerIndex();
        if (selectedLayerIndex < this.renderableLayers.size() - 1) {
            // Swap the selected layer with the layer to its right
            RenderableLayer tempLayer = this.renderableLayers.get(selectedLayerIndex);
            this.renderableLayers.set(selectedLayerIndex, this.renderableLayers.get(selectedLayerIndex + 1));
            this.renderableLayers.set(selectedLayerIndex + 1, tempLayer);
            this.updateNetworkChanges();
        }
    }

    public void addRenderableLayer(int layerIndex, RenderableLayer layer) {
//        if (this.renderableLayers.isEmpty()) NetworkRenderingJPanel.selectedLayerLabel = layer.getLabel();
        this.renderableLayers.add(layerIndex, layer);
        this.updateNetworkChanges();
    }

    public void removeRenderableLayer(int index) {
        if (!this.renderableLayers.isEmpty()) {
            this.renderableLayers.remove(index);
            this.updateNetworkChanges();
            if (this.renderableLayers.isEmpty()) NetworkRenderingJPanel.selectedLayerLabel = null;
        }
    }

    public void addRenderableNeuronToRenderAbleLayerIndex(int layerIndex, RenderableNeuron neuron) {
        this.renderableLayers.get(layerIndex).addRenderableNeuron(neuron);
        this.updateNetworkChanges();
    }

    public void removeRenderableNeuron(int layerIndex, int neuronIndex) {
        this.renderableLayers.get(layerIndex).removeRenderableNeuron(neuronIndex);
        this.updateNetworkChanges();
        var selectedLayer = this.renderableLayers.get(layerIndex);
        if (selectedLayer.getRenderableNeurons().isEmpty())
            NetworkRenderingJPanel.selectedLayersSelectedNeuronLabel = null;
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);
        for (RenderableLayer layer : this.renderableLayers) {
            if (Objects.equals(layer.getLabel(), NetworkRenderingJPanel.selectedLayerLabel)) layer.renderSelected(g);
            layer.render(g);
        }
    }

    @Override
    public void renderExtras(Graphics2D g) {
        super.renderExtras(g);
        for (RenderableLayer layer : this.renderableLayers) layer.renderExtras(g);
        for (LayerConnection layerConnection : this.layerConnections) {
            g.setColor(layerConnection.getColor());
            var coords = layerConnection.coords;
            g.drawLine((int) coords[0], (int) coords[1], (int) coords[2], (int) coords[3]);
        }
    }

    @Override
    public void update() {
        for (RenderableLayer layer : this.renderableLayers) {
            if (layer.checkIfMouseInside()) {
                for (RenderableNeuron neuron : layer.getRenderableNeurons()) {
                    if (neuron.checkIfMouseInside()) {
                        if (!Objects.equals(NetworkRenderingJPanel.selectedLayersSelectedNeuronLabel, neuron.getLabel())) {
                            NetworkRenderingJPanel.selectedLayersSelectedNeuronLabel = neuron.getLabel();
                            break;
                        }
                    }
                }
                if (!Objects.equals(NetworkRenderingJPanel.selectedLayerLabel, layer.getLabel())) {
                    NetworkRenderingJPanel.selectedLayerLabel = layer.getLabel();
                    break;
                }
            }
        }
    }

    public int getSelectedLayerIndex() {
        int result = -1;
        ArrayList<RenderableLayer> layers = this.renderableLayers;
        for (int i = 0; i < layers.size(); i++) {
            if (layers.get(i).getLabel().equals(NetworkRenderingJPanel.selectedLayerLabel)) {
                result = i;
            }
        }
        return result;
    }

    public int getSelectedLayersSelectedNeuronIndex() {
        int result = -1;
        int selectedLayerIndex = this.getSelectedLayerIndex();
        var layers = this.renderableLayers.get(selectedLayerIndex);
        ArrayList<RenderableNeuron> renderableNeurons = layers.getRenderableNeurons();
        for (int i = 0; i < renderableNeurons.size(); i++) {
            RenderableNeuron neuron = renderableNeurons.get(i);
            if (neuron.getLabel().equals(NetworkRenderingJPanel.selectedLayersSelectedNeuronLabel)) {
                result = i;
                break;
            }
        }
        return result;
    }

    public void clearAllRenderableLayers() {
        this.renderableLayers.clear();
        this.updateNetworkChanges();
    }
}
