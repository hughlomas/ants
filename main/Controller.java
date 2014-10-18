package main;

import processing.core.PApplet;
import processing.core.PVector;

public class Controller {
	
	private PApplet parent;
	private World world;
	private View view;
	
	private boolean paused = false;
	
	Controller(PApplet parent) {
		this.parent = parent;
	}
	
	public void setWorld( World world ){
		this.world = world;
	}
	
	public World getWorld(){
		return this.world;
	}
	
	public void setView( View view ){
		this.view = view;
	}

	public boolean isPaused(){
		return this.paused;
	}
	
	public View getView(){
		return this.view;
	}
	
	public void togglePause(){
		this.paused = !this.paused;
	}
	
	public void checkInput(){
		if( parent.mousePressed ){
			PVector position = this.interpretClickCoordinatesToGrid();
			Cell cell = this.getWorld().getCell( (int)position.x, (int)position.y );
			if( cell.getFood().isEmpty() ){
				Food food = this.getWorld().getEntityFactory().foodInstanceOn( cell );
				this.getWorld().getFood().add( food );
			}
		}	
	}
	
	public void keyPressed( char key ){
		switch( key ){
			case ' ':
				this.togglePause();
				break;
			case 'p':
				this.getView().toggleDrawPheromones();
		}
	}
	
	private PVector interpretClickCoordinatesToGrid(){
		PVector mouse = new PVector( parent.mouseX, parent.mouseY);
		System.out.print( "Clicked " + mouse.x + ", " + mouse.y + "\n");
		mouse.div( View.CELL_SIZE );
		if( mouse.y > this.getWorld().getHeight() ){
			mouse.y = this.getWorld().getHeight() - 1;
		}
		if( mouse.y < 0 ){
			mouse.y = 0;
		}
		if( mouse.x > this.getWorld().getWidth() ){
			mouse.x = this.getWorld().getWidth() - 1;
		}
		if( mouse.x < 0 ){
			mouse.x = 0;
		}
		System.out.print( "Interpretted as " + (int)mouse.x + ", " + (int)mouse.y + "\n" );
		
		return mouse;
	}
}
