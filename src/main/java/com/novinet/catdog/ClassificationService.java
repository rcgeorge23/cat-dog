package com.novinet.catdog;

import com.novinet.catdog.image.FastRgbBufferedImageWrapper;
import com.novinet.catdog.neuralnet.NaiveNeuralNetwork;
import net.coobird.thumbnailator.Thumbnails;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.novinet.catdog.TrainingSetLoader.CAT;
import static com.novinet.catdog.TrainingSetLoader.DOG;
import static java.lang.Math.abs;
import static java.lang.Math.round;

public class ClassificationService {

    static final String DEFAULT_NETWORK_NAME = "/naivenetwork.eg";

    static final Map<Long, ClassificationLabel> CLASSIFICATIONS = new HashMap<>();

    static {
        CLASSIFICATIONS.put(round(CAT.getNetworkOutput()), CAT);
        CLASSIFICATIONS.put(round(DOG.getNetworkOutput()), DOG);
    }

    public ClassificationResult classify(InputStream imageInputStream) throws IOException {
        return classify(imageInputStream, this.getClass().getResourceAsStream(DEFAULT_NETWORK_NAME));
    }

    public ClassificationResult classify(InputStream imageInputStream, InputStream networkStream) throws IOException {
        double classification = new NaiveNeuralNetwork(networkStream).classify(new FastRgbBufferedImageWrapper(resize(imageInputStream, 32, 32)));
        ClassificationLabel classificationLabel = CLASSIFICATIONS.get(round(classification));
        return new ClassificationResult(abs(1 - classification), classificationLabel);
    }

    private BufferedImage resize(InputStream imageInputStream, int width, int height) {
        try {
            return Thumbnails.of(imageInputStream).forceSize(width, height).asBufferedImage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
