package main;

import java.util.HashSet;
import processing.core.PApplet;

class Food implements Entity {

	private Cell cell;
	private PApplet parent;

	Food(PApplet parent) {
		this.parent = parent;
	}
	
	public void setCell(Cell cell) {
		this.cell = cell;
	}

	public Cell getCell() {
		return this.cell;
	}

}
