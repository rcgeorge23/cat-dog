package com.novinet.catdog.image;

import java.awt.Color;

public class Pixel {
	
	private Color colour;
	
	public Pixel(Color colour) {
		this.colour = colour;
	}
	
	public int getRed() {
		return this.colour.getRed();
	}
	
	public int getGreen() {
		return this.colour.getGreen();
	}
	
	public int getBlue() {
		return this.colour.getBlue();
	}

	public int getGrey() { return (getRed() + getGreen() + getBlue()) / 3; }
}
