package com.novinet.catdog.neuralnet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import com.novinet.catdog.ClassificationLabel;
import com.novinet.catdog.image.FastRgbBufferedImageWrapper;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;

import com.novinet.catdog.AnnotatedImage;
import com.novinet.catdog.image.Pixel;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

import static org.encog.persist.EncogDirectoryPersistence.loadObject;
import static org.encog.persist.EncogDirectoryPersistence.saveObject;

public class NaiveNeuralNetwork {
    private BasicNetwork network;

    public NaiveNeuralNetwork() {
    }

    public NaiveNeuralNetwork(InputStream networkInputStream) {
        network = (BasicNetwork) loadObject(networkInputStream);
    }

    public static NaiveNeuralNetwork buildAndTrainNetwork(NetworkTopology networkTopology, List<AnnotatedImage> trainingSet, double targetErrorRate) {
        NaiveNeuralNetwork naiveNeuralNetwork = new NaiveNeuralNetwork();
        MLDataSet mlDataSet = naiveNeuralNetwork.buildTrainingSet(trainingSet);

        BasicNetwork network = new BasicNetwork();

        network.addLayer(new BasicLayer(null, true, totalNumberOfPixels(trainingSet)));

        networkTopology.getLayers().stream().forEach(layer -> {
            network.addLayer(new BasicLayer(layer.getActivationFunction(), layer.isBias(), layer.getNumberOfNeurons()));
        });

        network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 1));
        network.getStructure().finalizeStructure();
        network.reset();

        ResilientPropagation resilientPropagation = new ResilientPropagation(network, mlDataSet);

        int epoch = 1;
        do {
            resilientPropagation.iteration();
            System.out.println("Epoch #" + epoch + " Error: " + resilientPropagation.getError());
            epoch++;
        } while (resilientPropagation.getError() > targetErrorRate);

        resilientPropagation.finishTraining();

        naiveNeuralNetwork.setNetwork(network);
        return naiveNeuralNetwork;
    }

    private static int totalNumberOfPixels(List<AnnotatedImage> trainingSet) {
        return trainingSet.get(0).getBufferedImageWrapper().getWidth() * trainingSet.get(0).getBufferedImageWrapper().getHeight();
    }

    public void saveNetwork(File file) {
        saveObject(file, network);
    }

    public double classify(FastRgbBufferedImageWrapper fastRgbBufferedImageWrapper) {
        if (network == null) {
            throw new RuntimeException("Network has not been trained yet");
        }

        return network.compute(buildImageMlData(fastRgbBufferedImageWrapper.getPixels())).getData()[0];
    }

    private MLDataSet buildTrainingSet(List<AnnotatedImage> trainingSet) {
        final MLDataSet mlDataSet = new BasicMLDataSet();

        trainingSet.forEach(annotatedImage -> {
            List<Pixel> pixels = annotatedImage.getBufferedImageWrapper().getPixels();
            ClassificationLabel label = annotatedImage.getLabel();

            final BasicMLData imageData = buildImageMlData(pixels);
            final BasicMLData classificationData = new BasicMLData(new double[]{label.getNetworkOutput()});

            mlDataSet.add(imageData, classificationData);
        });

        return mlDataSet;
    }

    private BasicMLData buildImageMlData(List<Pixel> pixels) {
        return new BasicMLData(pixels.stream().mapToDouble(pixel -> (double) pixel.getGrey()).toArray());
    }

    void setNetwork(BasicNetwork network) {
        this.network = network;
    }
}
