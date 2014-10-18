package main;

import java.util.ArrayList;
import java.util.HashSet;

import processing.core.PApplet;

public class Nest{
	
	private PApplet parent;
	private HashSet<Cell> holes;
	private World world;
	private Colony colony;
	private final static int FOOD_SPAWN_NEW_ANT_COST = 1;
	private int totalFood = 0;
	
	Nest(PApplet parent) {
		this.parent = parent;
		this.holes = new HashSet();
	}
	
	public HashSet<Cell> getHoles(){
		return this.holes;
	}
	
	public void takeTurn(){
		for( Cell cell : this.getHoles() ){
			if( cell.getFood().size() >= Nest.FOOD_SPAWN_NEW_ANT_COST ){
				String antColor = ( ( this.getColony().getColor() >> 16 & 0xFF ) == 255 ) ? "Red" : "Black";
				System.out.print( antColor + " ants are spawning a new ant\n");				
				Ant ant = this.getWorld().getEntityFactory().antInstanceOn( cell );
				this.getColony().getAnts().add(ant);
				ant.setColony( this.getColony() );
				for( Food food : cell.getFood() ){
					int i = 0;
					for( i = 0; i < Nest.FOOD_SPAWN_NEW_ANT_COST; i++ ){
						cell.removeEntity( food );
						world.getFood().remove( food );
					}
				}
				this.totalFood++;
				System.out.print( antColor + " ants have collected " + totalFood + " food\n");	
			}
		}
	}
	
	public void setWorld( World world ){
		this.world = world;
	}
	
	public World getWorld(){
		return this.world;
	}

	public void setColony(Colony colony) {
		this.colony = colony;
	}

	public Colony getColony() {
		return colony;
	}
}
