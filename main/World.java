package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import processing.core.PApplet;
import processing.core.PVector;

class World {
	
	private static final int ANTS_AT_START_PER_COLONY = 5;
	private static final int FOOD_AT_START = 200;
	
	private Cell[][] grid;

	private int width;
	private int height;

	private HashSet<Colony> colonies;
	private HashSet<Pheromone> pheromones;
	private HashSet<Food> food;

	private EntityFactory factory;

	private PApplet parent;
	
	World(ants parent) {
		this.parent = parent;
		this.setFactory(new EntityFactory(this.parent));
		this.colonies = new HashSet<Colony>();
		this.food = new HashSet<Food>();
		this.pheromones = new HashSet<Pheromone>();
	}

	public void setSize(int height, int width) {
		this.width = width;
		this.height = height;
		this.grid = new Cell[this.width][this.height];
	}

	public Cell getCell(int x, int y) {
		return this.grid[x][y];
	}

	public HashSet<Colony> getColonies() {
		return this.colonies;
	}

	public EntityFactory getEntityFactory() {
		return this.getFactory();
	}

	public HashSet<Pheromone> getPheromones() {
		return this.pheromones;
	}

	public HashSet<Food> getFood() {
		return this.food;
	}

	public void initialize() {
		this.instantiateCells();
		this.linkCellsToNeighbors();
		this.createNestEntrances();
		this.populateWithEntities();
	}

	private void createNestEntrances(){
		for( Colony colony : this.getColonies() ){
			colony.getNest().getHoles().add( this.getRandomCell() );
		}
	}
	private void instantiateCells() {
		for (int x = 0; x < this.getWidth(); x++) {
			for (int y = 0; y < this.getHeight(); y++) {
				Cell cell = new Cell(parent);
				cell.setPosition(new PVector(x, y));
				this.grid[x][y] = cell;
			}
		}
	}

	private void linkCellsToNeighbors() {
		for (int x = 0; x < this.getWidth(); x++) {
			for (int y = 0; y < this.getHeight(); y++) {				
				this.grid[x][y].linkToNeighbors();
			}
		}
	}
	
	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	private void populateWithAnts() {
		for (Colony colony : this.colonies) {
			for( Cell hole : colony.getNest().getHoles() ){
				for( int n = 0; n < World.ANTS_AT_START_PER_COLONY; n++ ){
					Ant ant = this.getFactory().antInstanceOn( hole );
					ant.setColony(colony);
					colony.getAnts().add(ant);
				}
			}			
		}
	}

	private void antsTurn() {
		ArrayList deadAnts = new ArrayList();
		try{
			for (Colony colony : this.getColonies()) {
				for (Ant ant : colony.getAnts() ) {
					if( ant.isDead() ){
						deadAnts.add(ant);
						continue;
					}
					ant.takeTurn();
				}
				colony.getAnts().removeAll( deadAnts );
			}
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		
	}

	private void pheromonesTurn() {
		for ( Iterator<Pheromone> i = this.getPheromones().iterator(); i.hasNext(); ){
			Pheromone pheromone = i.next();
			pheromone.takeTurn();
			if (pheromone.isDead()) {
				i.remove();
				pheromone.getCell().removeEntity( pheromone );
			}
		}
	}

	public void nestsTurn(){
		for (Colony colony : this.getColonies()) {
			colony.getNest().takeTurn();
		}
	}
	public void runTurn() {
		this.pheromonesTurn();
		this.antsTurn();
		this.nestsTurn();
	}

	private void populateWithFood() {
		for (int n = 0; n < this.FOOD_AT_START; n++) {
			this.getFood().add(this.getFactory().foodInstanceOn(this.getRandomCell()));
		}
	}

	public Cell getRandomCell() {
		return this.grid[(int) (parent.random(height - 1))][(int) (parent.random(width - 1))];
	}

	public void populateWithEntities() {
		this.populateWithAnts();
		this.populateWithFood();
	}

	public void setFactory(EntityFactory factory) {
		this.factory = factory;
	}

	public EntityFactory getFactory() {
		return factory;
	}
}
