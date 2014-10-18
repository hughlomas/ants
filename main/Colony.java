package main;

import java.util.HashSet;
import processing.core.PApplet;

public class Colony {

	private HashSet<Ant> ants;
	private int rgb;
	private Nest nest;
	private World world;
	
	private PApplet parent;

	Colony(PApplet parent) {
		this.parent = parent;
		this.ants = new HashSet();
	}

	public void createNest(){
		this.nest = new Nest(parent);
		this.nest.setColony(this);
		this.nest.setWorld( this.getWorld() );
	}
	public HashSet<Ant> getAnts() {
		return this.ants;
	}

	public int getColor() {
		return this.rgb;
	}

	public void setColor(int rgb) {
		this.rgb = rgb;
	}
	
	public Nest getNest(){
		return this.nest;
	}
	
	public void setWorld( World world ){
		this.world = world;
	}
	
	public World getWorld(){
		return this.world;
	}

}
