package org.mkproductions.jnn.network;

import org.mkproductions.jnn.entity.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class JNeuralNetwork implements Serializable {
    private final int numberOfInputNode;
    private final ArrayList<NetworkLayer> netWorkNetworkLayers;
    private final ArrayList<Matrix> weightsMatrices;
    private final ArrayList<Matrix> biasesMatrices;
    private double learningRate;
    private int networkAccuracy = 0;

    public JNeuralNetwork() {
        // Storing the design of the Neural Network
        this.learningRate = 0.01;
        this.numberOfInputNode = 0;
        this.netWorkNetworkLayers = null;
        // Initializing the arrays
        this.weightsMatrices = new ArrayList<>();
        this.biasesMatrices = new ArrayList<>();
        initNetwork();
    }

    public JNeuralNetwork(int numberOfInputNode, NetworkLayer... networkLayers) {
        // Storing the design of the Neural Network
        this.learningRate = 0.01;
        this.numberOfInputNode = numberOfInputNode;
        this.netWorkNetworkLayers = new ArrayList<>();
        this.netWorkNetworkLayers.addAll(Arrays.stream(networkLayers).toList());
        // Initializing the arrays
        this.weightsMatrices = new ArrayList<>();
        this.biasesMatrices = new ArrayList<>();
        initNetwork();
    }

    public JNeuralNetwork(JNeuralNetwork jNeuralNetwork) {
        this.numberOfInputNode = jNeuralNetwork.numberOfInputNode;
        this.netWorkNetworkLayers = jNeuralNetwork.netWorkNetworkLayers;
        this.learningRate = 0.01;
        // Initializing the arrays
        this.weightsMatrices = new ArrayList<>();
        this.biasesMatrices = new ArrayList<>();
        initNetwork();
    }

    public void initNetwork() {
        // Assign weights and biases to matrix arrays
        for (int a = 0; a < this.weightsMatrices.size(); a++) {
            if (a == 0) {
                this.weightsMatrices.add(new Matrix(this.netWorkNetworkLayers.get(a).numberOfNodes(), this.numberOfInputNode));
            } else {
                this.weightsMatrices.add(new Matrix(this.netWorkNetworkLayers.get(a).numberOfNodes(), this.netWorkNetworkLayers.get(a - 1).numberOfNodes()));
            }
            this.biasesMatrices.add(new Matrix(this.netWorkNetworkLayers.get(a).numberOfNodes(), 1));
            // Randomizing the weights and bias
            this.weightsMatrices.get(a).randomize();
            this.biasesMatrices.get(a).randomize();
        }
    }

    /**
     * Process inputs and produces outputs as per the network schema.
     *
     * @param inputs A double array to predict the output.
     * @return double array of output predicted by the network.
     */
    public ArrayList<Double> processInputs(ArrayList<Double> inputs) {
        if (inputs.size() != this.numberOfInputNode)
            throw new RuntimeException("Mismatch length of inputs to the network.");
        Matrix inputMatrix = Matrix.fromArray(inputs).transpose();
        Matrix[] outputMatrices = new Matrix[this.netWorkNetworkLayers.size()];
        for (int a = 0; a < this.weightsMatrices.size(); a++) {
            if (a == 0) {
                outputMatrices[a] = Matrix.matrixMapping(Matrix.add(Matrix.matrixMultiplication(this.weightsMatrices.get(a), inputMatrix), this.biasesMatrices.get(a)), this.netWorkNetworkLayers.get(a).activationFunction().equation);
                continue;
            }
            outputMatrices[a] = Matrix.matrixMapping(Matrix.add(Matrix.matrixMultiplication(this.weightsMatrices.get(a), outputMatrices[a - 1]), this.biasesMatrices.get(a)), this.netWorkNetworkLayers.get(a).activationFunction().equation);
        }
        return outputMatrices[outputMatrices.length - 1].getColumn(0);
    }

    /**
     * Function to perform back-propagate and adjusts weights and biases as per the given inputs with targets.
     *
     * @param inputs       2D array of inputs to be learned by network.
     * @param targetOutput 2D array to train the network as per inputs index.
     */
    private void backPropagate(ArrayList<Double> inputs, ArrayList<Double> targetOutput) {
        if (inputs.size() != this.numberOfInputNode)
            throw new RuntimeException("Mismatch length of inputs to the network.");

        Matrix inputMatrix = Matrix.fromArray(inputs).transpose();
        Matrix[] outputMatrices = new Matrix[this.netWorkNetworkLayers.size()];

        for (int a = 0; a < this.weightsMatrices.size(); a++) {
            if (a == 0) {
                outputMatrices[a] = Matrix.matrixMultiplication(this.weightsMatrices.get(a), inputMatrix);
                outputMatrices[a] = Matrix.add(outputMatrices[a], this.biasesMatrices.get(a));
                outputMatrices[a] = Matrix.matrixMapping(outputMatrices[a], this.netWorkNetworkLayers.get(a).activationFunction().equation);
                continue;
            }
            outputMatrices[a] = Matrix.matrixMultiplication(this.weightsMatrices.get(a), outputMatrices[a - 1]);
            outputMatrices[a] = Matrix.add(outputMatrices[a], this.biasesMatrices.get(a));
            outputMatrices[a] = Matrix.matrixMapping(outputMatrices[a], this.netWorkNetworkLayers.get(a).activationFunction().equation);
        }

        Matrix outputMatrix = outputMatrices[outputMatrices.length - 1];
        var targetArray = new ArrayList<ArrayList<Double>>();
        targetArray.add(targetOutput);
        Matrix targetMatrix = new Matrix(targetArray).transpose();

        // Output error matrix.
        Matrix errorMatrix = Matrix.scalarMultiply(Matrix.subtract(outputMatrix, targetMatrix), -1);

        for (int layerIndex = this.netWorkNetworkLayers.size() - 1; layerIndex >= 0; layerIndex--) {
            // Calculating gradients
            Matrix gradientMatrix = Matrix.matrixMapping(outputMatrices[layerIndex], this.netWorkNetworkLayers.get(layerIndex).activationFunction().derivative);
            gradientMatrix = Matrix.elementWiseMultiply(gradientMatrix, errorMatrix);
            gradientMatrix = Matrix.scalarMultiply(gradientMatrix, -this.learningRate);

            // Calculating error
            errorMatrix = Matrix.matrixMultiplication(this.weightsMatrices.get(layerIndex).transpose(), errorMatrix);

            // Getting the inputs of the current layer.
            Matrix previousOutputMatrixTranspose = (layerIndex == 0) ? inputMatrix.transpose() : outputMatrices[layerIndex - 1].transpose();

            // Calculating the change in weights matrix for each layer of the network.
            Matrix deltaWeightsMatrix = Matrix.matrixMultiplication(gradientMatrix, previousOutputMatrixTranspose);

            // Applying the change of weights in the current weights of the network.
            this.weightsMatrices.get(layerIndex).subtract(deltaWeightsMatrix);
            this.biasesMatrices.get(layerIndex).subtract(gradientMatrix);
        }
    }

    /**
     * Function to train model for mass amount of training inputs and outputs with random samples.
     *
     * @param epochCount      number of back-propagation iterations performed by the model.
     * @param trainingInputs  2D array of inputs for training the model.
     * @param trainingOutputs 2D array of outputs for training the model.
     */
    public void train(ArrayList<ArrayList<Double>> trainingInputs, ArrayList<ArrayList<Double>> trainingOutputs, int epochCount) {
        if (trainingInputs.getFirst().size() != this.numberOfInputNode)
            throw new RuntimeException("Mismatch inputs size.");
        if (trainingOutputs.getFirst().size() != this.netWorkNetworkLayers.getLast().numberOfNodes())
            throw new RuntimeException("Mismatch outputs size.");
        int progress;
        int lastProgress = 0;
        for (int epoch = 0; epoch < epochCount; epoch++) {
            // Random index for training random data from the training data set.
            progress = (epoch * 100) / epochCount;
            int randomIndex = new Random().nextInt(0, trainingInputs.size());
            ArrayList<Double> trainingInput = trainingInputs.get(randomIndex);
            ArrayList<Double> trainingOutput = trainingOutputs.get(randomIndex);
            backPropagate(trainingInput, trainingOutput);
            if (progress != lastProgress) {
                lastProgress = progress;
                int a;
                System.out.print("Training progress: " + progress + " [");
                for (a = 0; a < progress + 1; a++) {
                    System.out.print("#");
                }
                for (int b = 0; b < 100 - a; b++) {
                    System.out.print(" ");
                }
                System.out.print("]");
                System.out.println();
            }
        }
    }

    public double calculateAccuracy(ArrayList<ArrayList<Double>> testingInputs, ArrayList<ArrayList<Double>> testingOutputs) {
        System.out.println("Calculating network Accuracy");
        double accuracy;
        Random random = new Random();
        double correctCount = 0;
        int progress;
        int lastProgress = 0;
        for (int i = 0; i < testingInputs.size(); i++) {
            progress = (int) (((double) i / testingInputs.size()) * 100);
            int randomIndex = random.nextInt(testingInputs.size());
            ArrayList<Double> prediction = this.processInputs(testingInputs.get(randomIndex));
            ArrayList<Double> testingOutput = testingOutputs.get(randomIndex);
            int correctPredictionCount = 0;
            for (int a = 0; a < prediction.size(); a++) {
                if (Math.abs(prediction.get(a)) == Math.abs(testingOutput.get(a))) correctPredictionCount++;
            }
            if (correctPredictionCount < 10) correctCount++;
            if (progress != lastProgress) {
                lastProgress = progress;
                int a;
                System.out.print("Testing progress: " + progress + " [");
                for (a = 0; a < progress + 1; a++) {
                    System.out.print("#");
                }
                for (int b = 0; b < 100 - a; b++) {
                    System.out.print(" ");
                }
                System.out.print("]");
                System.out.println();
            }
        }
        accuracy = correctCount / testingInputs.size();
        System.out.println("Thanks for waiting.");
        return accuracy;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

}