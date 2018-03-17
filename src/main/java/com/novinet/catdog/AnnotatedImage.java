package com.novinet.catdog;

import static org.apache.commons.lang3.builder.ReflectionToStringBuilder.toStringExclude;

import com.novinet.catdog.util.FastRgbBufferedImageWrapper;

public class AnnotatedImage {
	private ClassificationLabel label;
	private String filename;
	private FastRgbBufferedImageWrapper bufferedImageWrapper;

	public ClassificationLabel getLabel() {
		return label;
	}

	public void setLabel(ClassificationLabel label) {
		this.label = label;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Override
	public String toString() {
		return toStringExclude(this, "bufferedImage");
	}

	public FastRgbBufferedImageWrapper getBufferedImageWrapper() {
		return bufferedImageWrapper;
	}

	public void setBufferedImageWrapper(FastRgbBufferedImageWrapper bufferedImageWrapper) {
		this.bufferedImageWrapper = bufferedImageWrapper;
	}
}
