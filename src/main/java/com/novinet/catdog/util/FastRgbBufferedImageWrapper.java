package com.novinet.catdog.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class FastRgbBufferedImageWrapper {

	private int width;
	private int height;
	private List<Pixel> pixelList;

	public FastRgbBufferedImageWrapper(BufferedImage image) {
		pixelList = new ArrayList<Pixel>();
		width = image.getWidth();
		height = image.getHeight();
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int rgb = image.getRGB(x, y);
				Color color = new Color(rgb);
				pixelList.add(new Pixel(color));
			}
		}
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public List<Pixel> getPixels() {
		return pixelList;
	}

}