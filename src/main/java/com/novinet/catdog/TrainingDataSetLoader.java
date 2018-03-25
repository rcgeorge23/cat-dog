package com.novinet.catdog;

import com.novinet.catdog.image.FastRgbBufferedImageWrapper;
import com.novinet.catdog.neuralnet.Layer;
import com.novinet.catdog.neuralnet.NaiveNeuralNetwork;
import com.novinet.catdog.neuralnet.NetworkTopology;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
import org.encog.engine.network.activation.ActivationSigmoid;

import java.io.*;
import java.util.*;

import static com.novinet.catdog.neuralnet.NaiveNeuralNetwork.buildAndTrainNetwork;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static javax.imageio.ImageIO.read;
import static org.apache.commons.csv.CSVFormat.EXCEL;

public class TrainingDataSetLoader {

	private static final int COLUMN_INDEX_IMAGE_NUMBER = 0;
	private static final int COLUMN_INDEX_IMAGE_LABEL = 1;
	
	private List<AnnotatedImage> annotatedImages = new ArrayList<>();
	
	private final static Logger LOGGER = Logger.getLogger(TrainingDataSetLoader.class);

    static final ClassificationLabel CAT = new ClassificationLabel("cat", 0d);
    static final ClassificationLabel DOG = new ClassificationLabel("dog", 1d);

	private List<ClassificationLabel> labels;
	private String trainingSetPath;
	private Iterable<CSVRecord> trainingSetRecords;
	private String trainingImageFileExtension;
    private ClassificationService classificationService;

	public TrainingDataSetLoader(List<ClassificationLabel> labels, String trainingSetPath, String trainingImageFileExtension, Iterable<CSVRecord> trainingSetRecords) throws IOException {
		this.labels = labels;
		this.trainingSetPath = trainingSetPath;
		this.trainingSetRecords = trainingSetRecords;
		this.trainingImageFileExtension = trainingImageFileExtension;
        this.classificationService = new ClassificationService();
	}
	
	public void initialise() throws IOException {
		boolean currentRowIsHeader = true;

        Map<String, ClassificationLabel> classificationLabelMap = new HashMap<>();

        for (ClassificationLabel label : labels) {
            classificationLabelMap.put(label.getLabel(), label);
        }

        for (CSVRecord record : trainingSetRecords) {
			if (currentRowIsHeader) {
				currentRowIsHeader = false;	
				//skip the header row
				continue;
			}
			
			String filename = record.get(COLUMN_INDEX_IMAGE_NUMBER) + trainingImageFileExtension;
			String label = record.get(COLUMN_INDEX_IMAGE_LABEL);
			
			if (classificationLabelMap.keySet().contains(label)) {
				annotatedImages.add(buildAnnotatedImage(trainingSetPath + filename, classificationLabelMap.get(label)));
			}
		}
		
		LOGGER.info(format("Annotated image list size: %s", annotatedImages.size()));
	}

	private AnnotatedImage buildAnnotatedImage(String filename, ClassificationLabel label) throws IOException {
		AnnotatedImage annotatedImage = new AnnotatedImage();
		annotatedImage.setFilename(filename);
		annotatedImage.setLabel(label);
		annotatedImage.setBufferedImageWrapper(buildBiw(filename));
		return annotatedImage;
	}

    static FastRgbBufferedImageWrapper buildBiw(String filename) throws IOException {
		return new FastRgbBufferedImageWrapper(read(new File(filename)));
	}

    public List<AnnotatedImage> getAnnotatedImages() {
        return annotatedImages;
    }

	public static void main(String[] args) throws IOException {
//        trainNetwork();
//        classify(new FileInputStream(new File("/Users/rcgeorge23/Downloads/.Keka-F68F43C0-B33E-4B8B-A182-9FAB65BF59E3/test/1005.png")), new FileInputStream(new File("/Users/rcgeorge23/neuralnetworks/catdog/1521294311131.eg"))); //cat
        ClassificationResult classificationResult = new ClassificationService().classify(new FileInputStream(new File("/Users/rcgeorge23/Downloads/.Keka-F68F43C0-B33E-4B8B-A182-9FAB65BF59E3/test/1005.png")));//cat
        System.out.println(classificationResult);
    }

    private static void classify(InputStream imageInputStream, InputStream networkInputStream) throws IOException {
        ClassificationResult classificationResult = new ClassificationService().classify(imageInputStream, networkInputStream);
        System.out.println(classificationResult.getCertainty());
        System.out.println(classificationResult.getClassificationLabel().getLabel());
    }

    private static void trainNetwork() throws IOException {
        TrainingDataSetLoader trainingSetLoader = new TrainingDataSetLoader(
                asList(CAT, DOG),
                "/Users/rcgeorge23/Downloads/train/",
                ".png",
                EXCEL.parse(new InputStreamReader(TrainingDataSetLoader.class.getResourceAsStream("/trainLabels.csv")))
        );

        trainingSetLoader.initialise();

        NaiveNeuralNetwork naiveNeuralNetwork = buildAndTrainNetwork(
                new NetworkTopology()
                        .addLayer(new Layer(new ActivationSigmoid(), true, 1024))
                        .addLayer(new Layer(new ActivationSigmoid(), true, 128))
                        .addLayer(new Layer(new ActivationSigmoid(), true, 2)),
                trainingSetLoader.getAnnotatedImages(),
                0.2
        );

        String neuralNetworkFileName = format("/Users/rcgeorge23/neuralnetworks/catdog/%s.eg", new Date().getTime());
        System.out.println(format("Saving network as %s", neuralNetworkFileName));

        naiveNeuralNetwork.saveNetwork(new File(neuralNetworkFileName));

        System.out.println(naiveNeuralNetwork.classify(buildBiw("/Users/rcgeorge23/Downloads/.Keka-F68F43C0-B33E-4B8B-A182-9FAB65BF59E3/test/1005.png")));
    }
}
