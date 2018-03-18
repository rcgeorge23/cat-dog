package com.novinet.catdog;

import com.novinet.catdog.image.FastRgbBufferedImageWrapper;
import com.novinet.catdog.neuralnet.NaiveNeuralNetwork;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.novinet.catdog.TrainingSetLoader.CAT;
import static com.novinet.catdog.TrainingSetLoader.DOG;
import static javax.imageio.ImageIO.read;

public class ClassificationService {

    static final String DEFAULT_NETWORK_NAME = "/naivenetwork.eg";

    static final Map<Long, ClassificationLabel> CLASSIFICATIONS = new HashMap<>();

    static {
        CLASSIFICATIONS.put(Math.round(CAT.getNetworkOutput()), CAT);
        CLASSIFICATIONS.put(Math.round(DOG.getNetworkOutput()), DOG);
    }

    public ClassificationResult classify(InputStream imageInputStream) throws IOException {
        return classify(imageInputStream, this.getClass().getResourceAsStream(DEFAULT_NETWORK_NAME));
    }

    public ClassificationResult classify(InputStream imageInputStream, InputStream networkStream) throws IOException {
        NaiveNeuralNetwork naiveNeuralNetwork = new NaiveNeuralNetwork(networkStream);
        double classification = naiveNeuralNetwork.classify(buildBiw(imageInputStream));
        Long result = Math.round(classification);
        ClassificationLabel classificationLabel = CLASSIFICATIONS.get(result);
        double certainty = Math.abs(1 - classification);
        return new ClassificationResult(certainty, classificationLabel);
    }

    private FastRgbBufferedImageWrapper buildBiw(InputStream imageInputStream) throws IOException {
        return new FastRgbBufferedImageWrapper(read(imageInputStream));
    }
}
