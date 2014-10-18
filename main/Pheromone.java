package main;

import java.util.HashMap;
import java.util.HashSet;
import processing.core.PApplet;

public class Pheromone implements Entity {
	
	public static final int MAX_LIFE_SPAN_IN_TURNS = 300;
	public static final int MAX_STRENGTH = 255;
	public static final String NEST_SCENT = "Nest";	
	public static final String FOOD_SCENT = "Food";
	
	private Cell cell;
	private boolean dead = false;

	private PApplet parent;
	private Ant ant;
	
	private int strength;
	private String type;
	private int age = 0;
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	private int decayRate;
	
	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int getDecayRate() {
		return decayRate;
	}

	public void setDecayRate(int decayRate) {
		this.decayRate = decayRate;
	}

	Pheromone(PApplet parent) {
		this.setParent(parent);
	}	
	
	public boolean isDead() {
		return this.dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public void setCell(Cell cell) {
		this.cell = cell;
	}

	public Cell getCell() {
		return this.cell;
	}
	
	public void takeTurn() {
		
		//age by one turn
		this.age += 1;	
		//reduce strength
		this.strength -= this.decayRate;
		
		if( ( this.age >= Pheromone.MAX_LIFE_SPAN_IN_TURNS ) || ( this.strength <= 0 ) ) {
			this.setDead(true);
		}

	}
		
	public int getRemainingTurnsUntilDeath() {
		return Pheromone.MAX_LIFE_SPAN_IN_TURNS - this.age;
	}

	public Ant getAnt(){
		return this.ant;
	}
	
	public void setAnt( Ant ant ){
		this.ant = ant;
	}
		
	public boolean belongsToAnt( Ant ant ){
		return this.getAnt().equals( ant );
	}
	
	public boolean belongsToColony( Colony colony ){
		return this.getAnt().getColony().equals( colony );
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setParent(PApplet parent) {
		this.parent = parent;
	}

	public PApplet getParent() {
		return parent;
	}
}
