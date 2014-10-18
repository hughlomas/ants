package strategies;

import java.util.ArrayList;
import java.util.Map.Entry;

import processing.core.PApplet;

import main.Ant;
import main.Cell;
import main.Colony;
import main.Entity;
import main.Pheromone;

public class FindFood implements Strategy{

	private Ant ant;
	
	private PApplet parent;
	
	public FindFood( PApplet parent ){
		this.parent = parent;
	}
	
	public void setAnt( Ant ant ){
		this.ant = ant;
	}
	
	//get them to avoid any pheromone at all that has no food associated
	public int ratePheromone( Pheromone pheromone ){		
				
		int rating = Pheromone.MAX_STRENGTH - pheromone.getStrength();
		return rating;		
	}
	
	public Cell getBestMove(){
		int bestRating = 0;
		
		ArrayList<Cell> emptyCells = new ArrayList<Cell>();
		Cell bestCell = this.getAnt().getCell().getRandomNeighbor();
		for( Entry<String, Cell> entry : this.getAnt().getNeighborCells() ){
			Cell neighbor = entry.getValue();			
			if( ( !neighbor.getFood().isEmpty() ) && ( !this.getAnt().getColony().getNest().getHoles().contains(neighbor)) ){
				return neighbor;
			}
			
			//get the pheromones on the neighbor
			ArrayList<Pheromone> pheromones = neighbor.getPheromones();
			//if there are no pheromones, it is a good place to move to
			if( pheromones.isEmpty() ){
				emptyCells.add( neighbor );
			}
						
			//if there are pheromones, try to find the oldest pheromone
			for( Pheromone pheromone : pheromones ){
				int rating = this.ratePheromone( pheromone );				
				if(  pheromone.getType().equals( Pheromone.FOOD_SCENT ) && ( rating > bestRating ) ){
					bestCell = neighbor;
					bestRating = rating; 
				}
			}
		}
		
		//TODO: try to get them to avoid their colony scent if there is no food on the same cell
		
		//if there are any empty ones, choose from them first
		if( !emptyCells.isEmpty() && (( bestRating <= 0 ) || this.getAnt().ownsPheromoneOnCell(bestCell)) ){
			//return random choice
			return emptyCells.get( PApplet.floor( parent.random( emptyCells.size() ) ) );
		}
		//otherwise we return the cell with the oldest pheromone
		return bestCell;
	}
	
	private Ant getAnt() {
		return this.ant;
	}
	
}
