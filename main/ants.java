package main;

import processing.core.*;
import processing.app.*;
import antlr.*;

import java.util.HashSet;
import java.util.Stack;

import javax.swing.*;

public class ants extends PApplet{
	
	private static final long serialVersionUID = 1L;
	World world = new World( this );
	View view = new View( this );
	Controller controller = new Controller( this );
	
	private static final int WORLD_SIZE = 100;
	
	public World getWorld(){
		return this.world;	
		
	}
	
	public static void main( String[] args ){
		//new AntsFrame().setVisible(true);
		PApplet.main(new String[] { "--present", "main.ants" });
	}
	
	protected static class AntsFrame extends JFrame 
	{
		public AntsFrame()
		{
			this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			this.setSize(WORLD_SIZE * View.CELL_SIZE + View.UI_PANEL_WIDTH,WORLD_SIZE * View.CELL_SIZE);
			final JPanel antPanel = new JPanel();
			antPanel.setBounds(10,10,WORLD_SIZE * View.CELL_SIZE + View.UI_PANEL_WIDTH,WORLD_SIZE * View.CELL_SIZE);
			final PApplet antlet = new ants();
			antPanel.add(antlet);
			this.add(antPanel);
			this.setVisible(true);
			antlet.init();
		}
	}

	public void setup(){
	  size( WORLD_SIZE * View.CELL_SIZE + View.UI_PANEL_WIDTH, WORLD_SIZE * View.CELL_SIZE );  
	  noStroke();
	  smooth();
	  
	  world.setSize( ants.WORLD_SIZE, ants.WORLD_SIZE );
	  Colony redAnts = new Colony( this );
	  redAnts.setColor( this.color( 255, 0, 0 ) );
	  redAnts.setWorld(world);
	  redAnts.createNest();
	  world.getColonies().add( redAnts );
	  
	 
	  Colony blackAnts = new Colony( this );
	  blackAnts.setColor( this.color( 0, 0, 0 ) );
	  blackAnts.setWorld(world);
	  blackAnts.createNest();
	  world.getColonies().add( blackAnts );
	  
	  
	  world.initialize();
	  view.setWorld( world );
	  controller.setWorld( world );
	  controller.setView( view );
	}
	
	public void draw(){
	  background(255);
	  controller.checkInput();
	  if( !controller.isPaused() ){
		  world.runTurn();	  
	  }	  
	  view.render();
	}
	
	public void keyPressed(){
		controller.keyPressed( key );		
	}
	
}