package com.novinet.catdog;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static javax.imageio.ImageIO.read;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

import com.novinet.catdog.util.FastRgbBufferedImageWrapper;

public class ImageReader {

	private static final int COLUMN_INDEX_IMAGE_NUMBER = 0;
	private static final int COLUMN_INDEX_IMAGE_LABEL = 1;
	
	private static final String LABEL_DOG = "dog";
	private static final String LABEL_CAT = "cat";
	
	private static final String TRAINING_SET_PATH = "/Users/rcgeorge23/Downloads/train/";
	private static final String TRAINING_SET_LABELS_FILENAME = "/trainLabels.csv";
	
	private List<AnnotatedImage> annotatedImages = new ArrayList<AnnotatedImage>();
	
	private final static Logger LOGGER = Logger.getLogger(ImageReader.class);

	public ImageReader(List<String> labels) throws IOException {
		Reader in = new InputStreamReader(this.getClass().getResourceAsStream(TRAINING_SET_LABELS_FILENAME));
		Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
		
		boolean currentRowIsHeader = true;
		
		for (CSVRecord record : records) {
			if (currentRowIsHeader) {
				currentRowIsHeader = false;	
				//skip the header row
				continue;
			}
			
			String filename = record.get(COLUMN_INDEX_IMAGE_NUMBER) + ".png";
			String label = record.get(COLUMN_INDEX_IMAGE_LABEL);
			
			if (labels.contains(label)) {
				annotatedImages.add(buildAnnotatedImage(TRAINING_SET_PATH + filename, label));
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
		new ImageReader(asList(LABEL_CAT, LABEL_DOG));
	}
}
