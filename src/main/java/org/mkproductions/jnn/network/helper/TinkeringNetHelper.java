package org.mkproductions.jnn.network.helper;

import org.mkproductions.jnn.entity.NetworkLayer;
import org.mkproductions.jnn.network.JNeuralNetwork;

import java.awt.*;
import java.security.SecureRandom;

public class TinkeringNetHelper {
    private static TinkeringNetHelper INSTANCE;

    public Color selectedElementColor = new Color(255, 255, 255, 200);
    public Color hoveredElementColor = new Color(255, 255, 255, 124);
    public int renderableLayerWidth;
    public double renderableSpacing;
    public double renderableLayerSpacing;
    public double neuronSize;
    public JNeuralNetwork jNeuralNetwork;

    private TinkeringNetHelper() {
        this.jNeuralNetwork = new JNeuralNetwork();
        this.renderableLayerWidth = 70;
        this.renderableSpacing = 10;
        this.renderableLayerSpacing = 50;
        this.neuronSize = this.renderableLayerWidth - (this.renderableSpacing * 2);
    }

    public static TinkeringNetHelper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TinkeringNetHelper();
        }
        return new TinkeringNetHelper();
    }

    public String generateRandomStringOfLength(int length) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~!@#$%^&*()`-=_+[]\\{}|;':\",./<>?";
        StringBuilder stringBuilder = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(alphabet.length());
            stringBuilder.append(alphabet.charAt(index));
        }
        return stringBuilder.toString();
    }

    public double map(double value, double range1Lower, double range1Upper, double range2Upper, double range2Lower) {
        // Validate input ranges: Ensure range1Lower < range1Upper and range2Lower < range2Upper
        if (range1Lower >= range1Upper || range2Lower >= range2Upper) {
            throw new IllegalArgumentException("Invalid range boundaries: Input ranges must have a lower bound less than the upper bound.");
        }
        // Calculate the normalized value within range1 (0.0 to 1.0)
        double normalizedValue = (value - range1Lower) / (range1Upper - range1Lower);

        // Handle cases where value falls outside range1
        if (normalizedValue < 0.0) normalizedValue = 0.0;
        else if (normalizedValue > 1.0) normalizedValue = 1.0;

        return normalizedValue * (range2Upper - range2Lower) + range2Lower;
    }

    public int getRandomNumberBetween(int low, int high) {
        SecureRandom random = new SecureRandom();
        double range = high - low;
        double randomDouble = random.nextDouble();
        return (int) (randomDouble * range + low);
    }

    public double getRandomNumberBetween(double low, double high) {
        SecureRandom random = new SecureRandom();
        double range = high - low;
        double randomDouble = random.nextDouble();
        return randomDouble * range + low;
    }

    public double[][] mapValuesTo(double[][] values, double range1Lower, double range1Upper, double range2Upper, double range2Lower) {
        double[][] result = new double[values.length][values[0].length];
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[0].length; j++) {
                result[i][j] = map(values[i][j], range1Lower, range1Upper, range2Upper, range2Lower);
            }
        }
        return result;
    }

    public void compileNeuralNetwork(int numberOfInputs, NetworkLayer... networkLayers) {
        this.jNeuralNetwork = new JNeuralNetwork(numberOfInputs, networkLayers);
    }
}
