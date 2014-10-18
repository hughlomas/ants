package main;

import processing.core.PVector;

public class DataVector {

	private boolean alive = true;
	private int life = View.UI_PANEL_WIDTH;
	private PVector position;
	private PVector vector;
	private int rgb;
	
	public void update(){
		if( this.life <= 0 ){
			this.alive = false;
		}		
		this.life--;
		this.position.add( this.vector );
	}

	public boolean isAlive(){
		return this.alive;
	}
	
	public void setPosition(PVector position) {
		this.position = position;
	}

	public PVector getPosition() {
		return position;
	}

	public void setVector(PVector vector) {
		this.vector = vector;
	}

	public PVector getVector() {
		return vector;
	}

	public void setRgb(int rgb) {
		this.rgb = rgb;
	}

	public int getRgb() {
		return rgb;
	}
	
	
	
	
}
