package com.novinet.catdog;

import com.novinet.catdog.neuralnet.NaiveNeuralNetwork;
import com.novinet.catdog.image.FastRgbBufferedImageWrapper;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static com.novinet.catdog.neuralnet.NaiveNeuralNetwork.buildAndTrainNetwork;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static javax.imageio.ImageIO.read;
import static org.apache.commons.csv.CSVFormat.EXCEL;

public class TrainingSetLoader {

	private static final int COLUMN_INDEX_IMAGE_NUMBER = 0;
	private static final int COLUMN_INDEX_IMAGE_LABEL = 1;
	
	private List<AnnotatedImage> annotatedImages = new ArrayList<>();
	
	private final static Logger LOGGER = Logger.getLogger(TrainingSetLoader.class);

    private static final ClassificationLabel CAT = new ClassificationLabel("cat", 0d);
    private static final ClassificationLabel DOG = new ClassificationLabel("dog", 1d);

    private static final Map<Long, ClassificationLabel> CLASSIFICATIONS = new HashMap<>();

    static {
        CLASSIFICATIONS.put(Math.round(CAT.getNetworkOutput()), CAT);
        CLASSIFICATIONS.put(Math.round(DOG.getNetworkOutput()), DOG);
    }

	private List<ClassificationLabel> labels;
	private String trainingSetPath;
	private Iterable<CSVRecord> trainingSetRecords;
	private String trainingImageFileExtension;

	public TrainingSetLoader(List<ClassificationLabel> labels, String trainingSetPath, String trainingImageFileExtension, Iterable<CSVRecord> trainingSetRecords) throws IOException {
		this.labels = labels;
		this.trainingSetPath = trainingSetPath;
		this.trainingSetRecords = trainingSetRecords;
		this.trainingImageFileExtension = trainingImageFileExtension;
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

	private static FastRgbBufferedImageWrapper buildBiw(String filename) throws IOException {
		return new FastRgbBufferedImageWrapper(read(new File(filename)));
	}

    public List<AnnotatedImage> getAnnotatedImages() {
        return annotatedImages;
    }

	public static void main(String[] args) throws IOException {
        trainNetwork();
//        classify("/Users/rcgeorge23/Downloads/.Keka-F68F43C0-B33E-4B8B-A182-9FAB65BF59E3/test/1005.png", "/Users/rcgeorge23/neuralnetworks/catdog/1521294311131.eg"); //cat
	}

    private static void classify(String imagePath, String networkPath) throws IOException {
        NaiveNeuralNetwork naiveNeuralNetwork = new NaiveNeuralNetwork(new File(networkPath));

        double classification = naiveNeuralNetwork.classify(buildBiw(imagePath));

        System.out.println(classification);

        Long result = Math.round(classification);

        System.out.println(CLASSIFICATIONS.get(result).getLabel());
    }

    private static void trainNetwork() throws IOException {
        TrainingSetLoader trainingSetLoader = new TrainingSetLoader(
                asList(CAT, DOG),
                "/Users/rcgeorge23/Downloads/train/",
                ".png",
                EXCEL.parse(new InputStreamReader(TrainingSetLoader.class.getResourceAsStream("/trainLabels.csv")))
        );

        trainingSetLoader.initialise();

        NaiveNeuralNetwork naiveNeuralNetwork = buildAndTrainNetwork(trainingSetLoader.getAnnotatedImages(), 0.2);

        String neuralNetworkFileName = format("/Users/rcgeorge23/neuralnetworks/catdog/%s.eg", new Date().getTime());
        System.out.println(format("Saving network as %s", neuralNetworkFileName));

        naiveNeuralNetwork.saveNetwork(new File(neuralNetworkFileName));

        System.out.println(naiveNeuralNetwork.classify(buildBiw("/Users/rcgeorge23/Downloads/.Keka-F68F43C0-B33E-4B8B-A182-9FAB65BF59E3/test/1005.png")));
    }
}
