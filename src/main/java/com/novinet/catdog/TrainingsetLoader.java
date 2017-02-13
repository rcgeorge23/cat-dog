package com.novinet.catdog;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static javax.imageio.ImageIO.read;
import static org.apache.commons.csv.CSVFormat.EXCEL;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

import com.novinet.catdog.util.FastRgbBufferedImageWrapper;

public class TrainingsetLoader {

	private static final int COLUMN_INDEX_IMAGE_NUMBER = 0;
	private static final int COLUMN_INDEX_IMAGE_LABEL = 1;
	
	private List<AnnotatedImage> annotatedImages = new ArrayList<AnnotatedImage>();
	
	private final static Logger LOGGER = Logger.getLogger(TrainingsetLoader.class);
	
	private List<String> labels;
	private String trainingsetPath;
	private Iterable<CSVRecord> trainingsetRecords;
	private String trainingImageFileExtension;

	public TrainingsetLoader(List<String> labels, String trainingsetPath, String trainingImageFileExtension, Iterable<CSVRecord> trainingsetRecords) throws IOException {
		this.labels = labels;
		this.trainingsetPath = trainingsetPath;
		this.trainingsetRecords = trainingsetRecords;
		this.trainingImageFileExtension = trainingImageFileExtension;
	}
	
	public void initialise() throws IOException {
		boolean currentRowIsHeader = true;
		
		for (CSVRecord record : trainingsetRecords) {
			if (currentRowIsHeader) {
				currentRowIsHeader = false;	
				//skip the header row
				continue;
			}
			
			String filename = record.get(COLUMN_INDEX_IMAGE_NUMBER) + trainingImageFileExtension;
			String label = record.get(COLUMN_INDEX_IMAGE_LABEL);
			
			if (labels.contains(label)) {
				annotatedImages.add(buildAnnotatedImage(trainingsetPath + filename, label));
			}
		}
		
		LOGGER.info(format("Annotated image list size: %s", annotatedImages.size()));
	}

	private AnnotatedImage buildAnnotatedImage(String filename, String label) throws IOException {
		AnnotatedImage annotatedImage = new AnnotatedImage();
		annotatedImage.setFilename(filename);
		annotatedImage.setLabel(label);
		annotatedImage.setBufferedImageWrapper(buildBiw(filename));
		return annotatedImage;
	}

	private FastRgbBufferedImageWrapper buildBiw(String filename) throws IOException {
		return new FastRgbBufferedImageWrapper(read(new File(filename)));
	}

	public static void main(String[] args) throws IOException {
		TrainingsetLoader trainingsetLoader = new TrainingsetLoader(asList("cat", "dog"), "/Users/rcgeorge23/Downloads/train/", ".png", EXCEL.parse(new InputStreamReader(TrainingsetLoader.class.getResourceAsStream("/trainLabels.csv"))));
		trainingsetLoader.initialise();
	}

	public List<AnnotatedImage> getAnnotatedImages() {
		return annotatedImages;
	}
}
