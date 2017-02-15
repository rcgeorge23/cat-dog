package com.novinet.catdog.neuralnet;

import java.util.List;

import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;

import com.novinet.catdog.AnnotatedImage;
import com.novinet.catdog.util.Pixel;

public class NaiveNeuralNetwork {
	private List<AnnotatedImage> trainingset;
	
	public NaiveNeuralNetwork(List<AnnotatedImage> trainingset) {
		this.trainingset = trainingset;
	}
	
	public void train() {
		NeuralDataSet trainingSet = buildTrainingSet();
	}

	private NeuralDataSet buildTrainingSet() {
		final NeuralDataSet trainingSet = new BasicNeuralDataSet();
		
		this.trainingset.forEach(annotatedImage -> {
			List<Pixel> pixels = annotatedImage.getBufferedImageWrapper().getPixels();
			String label = annotatedImage.getLabel();
			
		});
		
		
		
		return trainingSet;
	}
}
