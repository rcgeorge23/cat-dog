package com.novinet.catdog.neuralnet;

import org.encog.engine.network.activation.ActivationFunction;

public class Layer {
    private ActivationFunction activationFunction;
    private boolean bias;
    private int numberOfNeurons;

    public Layer(ActivationFunction activationFunction, boolean bias, int numberOfNeurons) {
        this.activationFunction = activationFunction;
        this.bias = bias;
        this.numberOfNeurons = numberOfNeurons;
    }

    public int getNumberOfNeurons() {
        return numberOfNeurons;
    }

    public ActivationFunction getActivationFunction() {
        return activationFunction;
    }

    public boolean isBias() {
        return bias;
    }
}
