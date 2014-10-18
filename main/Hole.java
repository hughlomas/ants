package main;

import processing.core.PApplet;

public class Hole {
	
	private Cell cell;
	private PApplet parent;
	
	public Hole( PApplet parent ){
		this.parent = parent;
	}
	
	public void setCell(Cell cell) {
		this.cell = cell;
	}

	public Cell getCell() {
		return this.cell;
	}
}
