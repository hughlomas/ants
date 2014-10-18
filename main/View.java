package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map.Entry;

import processing.core.PApplet;
import processing.core.PVector;

class View {

	public static final int CELL_SIZE = 5;
	public static final int UI_PANEL_WIDTH = 300;
	
	private PApplet parent;
	private World world;
	private ArrayList<DataVector> dataVectors = new ArrayList();
	
	private boolean drawPheromones = true;
	
	public void toggleDrawPheromones(){
		this.drawPheromones = !this.drawPheromones;
	}
	
	private boolean drawPheromonesIsEnabled(){
		return this.drawPheromones;
	}
		
	View(ants parent) {
		this.parent = parent;
	}
	
	public void render() {
		if( this.drawPheromonesIsEnabled() ){
			this.drawPheromones();
		}
		this.drawNestHoles();
		this.drawAnts();		
		this.drawFood();
		this.drawUI();		
	}

	
	private void drawUI() {
		ArrayList<DataVector> deadVectors = new ArrayList();
		for( DataVector dataVector: this.dataVectors ){
			
			dataVector.update();
			
			if( !dataVector.isAlive() ){				
				deadVectors.add( dataVector );
			}
			else{
				parent.fill( dataVector.getRgb() );
				parent.rect( dataVector.getPosition().x, dataVector.getPosition().y, PApplet.abs(dataVector.getVector().x), PApplet.abs(dataVector.getVector().x) );	
			}						
		}
		
		this.dataVectors.removeAll( deadVectors );
		for( Colony colony : this.getWorld().getColonies() ){
			DataVector count = new DataVector();
			count.setPosition( new PVector(parent.width, parent.height - colony.getAnts().size()) );
			count.setVector( new PVector(-1, 0) );
			count.setRgb( colony.getColor());
			this.dataVectors.add(count);
		}	
		
		DataVector count = new DataVector();
		count.setPosition( new PVector(parent.width, parent.height - this.getWorld().getFood().size()) );
		count.setVector( new PVector(-1, 0) );
		count.setRgb( parent.color(0,255,0));
		this.dataVectors.add(count);
	}

	private void drawNestHoles(){
		for( Colony colony : ((ants) parent).getWorld().getColonies() ){
			for( Cell cell : colony.getNest().getHoles() ){
				parent.fill( parent.color( 50, 50, 50 ) );				
				parent.rect( cell.getPosition().x * View.CELL_SIZE, cell.getPosition().y * View.CELL_SIZE, View.CELL_SIZE, View.CELL_SIZE );
				int nestBorderColor = ( colony.getColor() >> 16 & 0xFF ); 
				parent.fill( parent.color( nestBorderColor, 0, 0, 100 ) );	
				for( Entry<String, Cell> entry : cell.getNeighbors().entrySet() ){
					Cell neighbor = entry.getValue();								
					parent.rect( neighbor.getPosition().x * View.CELL_SIZE, neighbor.getPosition().y * View.CELL_SIZE, View.CELL_SIZE, View.CELL_SIZE );
				}
			}
		}
	}
	
	private void drawAnts() {
		for( Colony colony : ((ants) parent).getWorld().getColonies() ){
			parent.fill(colony.getColor());
			for( Ant ant : colony.getAnts() ){
				parent.rect(ant.getCell().getPosition().x * View.CELL_SIZE, ant.getCell().getPosition().y * View.CELL_SIZE, View.CELL_SIZE, View.CELL_SIZE );
			}
		}
	}

	public World getWorld(){
		return this.world;
	}
	
	public void setWorld( World world ){
		this.world = world;
	}

	private void drawFood() {

		parent.stroke(0);
		for( Food food : world.getFood() ){
			// draw
			parent.fill( parent.color(0, 255, 0));
			//parent.rect(food.getCell().getPosition().x * View.CELL_SIZE, food.getCell().getPosition().y * View.CELL_SIZE, View.CELL_SIZE, View.CELL_SIZE );
			parent.ellipse( food.getCell().getPosition().x * View.CELL_SIZE + (View.CELL_SIZE/2), food.getCell().getPosition().y * View.CELL_SIZE + (View.CELL_SIZE/2), View.CELL_SIZE/2, View.CELL_SIZE/2 );
		}
		parent.noStroke();
	}

	private void drawPheromones() {
		for( Pheromone pheromone : world.getPheromones() ){
			// draw
			String type = pheromone.getType();

			if( type.equals( Pheromone.FOOD_SCENT ) ){
				parent.fill( parent.color( 0, pheromone.getStrength(), 0, ( pheromone.getStrength() + 1 ) / ( Pheromone.MAX_STRENGTH / 100 ) ) );
				parent.rect( pheromone.getCell().getPosition().x * View.CELL_SIZE, pheromone.getCell().getPosition().y * View.CELL_SIZE, View.CELL_SIZE, View.CELL_SIZE ); 
			}
			else if( type.equals( Pheromone.NEST_SCENT )  ){
				double rgb = pheromone.getStrength() * 0.5;
				parent.fill( parent.color( (int) rgb, ( Pheromone.MAX_LIFE_SPAN_IN_TURNS - pheromone.getAge() + 1 ) / ( Pheromone.MAX_LIFE_SPAN_IN_TURNS / 100 ) ) );
				parent.rect(pheromone.getCell().getPosition().x * View.CELL_SIZE, pheromone.getCell().getPosition().y * View.CELL_SIZE, View.CELL_SIZE, View.CELL_SIZE );
			}
					
		}
	}
}
