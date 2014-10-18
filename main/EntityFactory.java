package main;

import java.util.HashSet;

import strategies.FindFood;
import processing.core.PApplet;

class EntityFactory {
	private PApplet parent;

	EntityFactory(PApplet parent) {
		this.parent = parent;
	}
	
	public Ant antInstanceOn(Cell cell) {
		Ant ant = new Ant( this.parent );
		ant.setCell(cell);
		ant.setLastCell(cell);
		cell.addEntity( ant );
		return ant;
	}

	public Food foodInstanceOn(Cell cell) {
		Food food = new Food(parent);
		food.setCell(cell);
		cell.addEntity( food );
		return food;
	}

	public Pheromone pheromoneInstanceOn(Cell cell) {
		Pheromone pheromone = new Pheromone(parent);
		pheromone.setCell(cell);
		cell.addEntity( pheromone );
		return pheromone;
	}

}
