package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import processing.core.PApplet;
import processing.core.PVector;

public class Cell {

	private PVector position;
	private HashSet<Entity> entities;

	private PApplet parent;
	
	private HashMap<String, Cell> neighbors;	

	Cell(PApplet parent) {
		this.parent = parent;
		this.entities = new HashSet<Entity>();
		this.neighbors = new HashMap<String, Cell>();
	}
	
	public HashMap<String, Cell> getNeighbors(){
		return this.neighbors;
	}
	
	public void addEntity( Entity entity ){
		this.entities.add( entity );
	}
	
	public ArrayList<Pheromone> getPheromones(){
		ArrayList pheromones = new ArrayList();
		for( Object entity : this.getEntities() ){
			if( entity.getClass().toString().equalsIgnoreCase( "class main.Pheromone" ) ){
				pheromones.add( entity );
			}
		}
		return pheromones;
	}
	
	public ArrayList<Food> getFood(){
		ArrayList food = new ArrayList();
		for( Object entity : this.getEntities() ){
			if( entity.getClass().toString().equalsIgnoreCase( "class main.Food" ) ){
				food.add( entity );
			}
		}
		return food;
	}
	
	public void removeEntity( Entity entity ){
		this.entities.remove( entity );
	}
	
	public HashSet<Entity> getEntities() {
		return this.entities;
	}

	public void setPosition(PVector position) {
		this.position = position;
	}

	public PVector getPosition() {
		return this.position;
	}
	
	public Cell getRandomNeighbor(){		
		String directions = "n,s,e,w,ne,se,nw,sw";
		String[] choices = directions.split(",");
		return this.getNeighbors().get( choices[ PApplet.floor( parent.random(8) ) ] );		
	}
	
	public void linkToNeighbors(){		
		this.getNeighbors().put( "w", ((ants) parent).getWorld().getCell( (int)( this.position.x - 1 + ((ants) parent).getWorld().getWidth() ) % ((ants) parent).getWorld().getWidth(), (int)( this.position.y % ((ants) parent).getWorld().getHeight() ) ) );  
		this.getNeighbors().put( "e", ((ants) parent).getWorld().getCell( (int)( this.position.x + 1 ) % ((ants) parent).getWorld().getWidth(), (int)( this.position.y % ((ants) parent).getWorld().getHeight() ) ) );  
		this.getNeighbors().put( "n", ((ants) parent).getWorld().getCell( (int)( this.position.x % ((ants) parent).getWorld().getWidth() ), (int)( this.position.y - 1 + ((ants) parent).getWorld().getHeight() ) % ((ants) parent).getWorld().getHeight() ) );  
		this.getNeighbors().put( "s", ((ants) parent).getWorld().getCell( (int)( this.position.x % ((ants) parent).getWorld().getWidth() ), (int)( this.position.y + 1 ) % ((ants) parent).getWorld().getHeight() ) ); 
		this.getNeighbors().put( "nw", ((ants) parent).getWorld().getCell( (int)( this.position.x - 1 + ((ants) parent).getWorld().getWidth() ) % ((ants) parent).getWorld().getWidth(), (int)( this.position.y + 1 ) % ((ants) parent).getWorld().getHeight() ) );  
		this.getNeighbors().put( "se", ((ants) parent).getWorld().getCell( (int)( this.position.x + 1 ) % ((ants) parent).getWorld().getWidth(), (int)( this.position.y - 1 + ((ants) parent).getWorld().getHeight() ) % ((ants) parent).getWorld().getHeight() ) );  
		this.getNeighbors().put( "sw", ((ants) parent).getWorld().getCell( (int)( this.position.x - 1 + ((ants) parent).getWorld().getWidth() ) % ((ants) parent).getWorld().getWidth(), (int)( this.position.y - 1 + ((ants) parent).getWorld().getHeight() ) % ((ants) parent).getWorld().getHeight() ) );  
		this.getNeighbors().put( "ne", ((ants) parent).getWorld().getCell( (int)( this.position.x + 1 ) % ((ants) parent).getWorld().getWidth(), (int)( this.position.y + 1 ) % ((ants) parent).getWorld().getHeight() ) ); 
	}
}
