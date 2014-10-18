package strategies;

import java.util.ArrayList;
import java.util.Map.Entry;

import processing.core.PApplet;

import main.Ant;
import main.Cell;
import main.Ant;
import main.Pheromone;

public class ReturnFoodToBase implements Strategy{

	private Ant ant;
	
	private PApplet parent;
	
	public ReturnFoodToBase( PApplet parent ){
		this.parent = parent;
	}
	
	public void setAnt( Ant ant ){
		this.ant = ant;
	}
	
	//try to get the ants to avoid their own pheromone on the way back to base
	public int ratePheromone( Pheromone pheromone ){
		int rating = 0;
		
		if( pheromone.belongsToColony( this.getAnt().getColony() ) && pheromone.getType().equals( Pheromone.NEST_SCENT ) ){			
			rating += pheromone.getStrength();
		}	
		/*		
		if( pheromone.belongsToAnt( this.getAnt() ) ){
			rating -= pheromone.getRemainingTurnsUntilDeath();
			if( pheromone.getStrength() == 0 ){
				rating += Pheromone.MAX_LIFE_SPAN_IN_TURNS - pheromone.getRemainingTurnsUntilDeath();				
			}
		}
		*/
		return rating;		
	}
	
	public Cell getBestMove(){
		int bestRating = 0;
		
		ArrayList<Cell> emptyCells = new ArrayList();
		Cell bestCell = this.getAnt().getCell().getRandomNeighbor();
		for( Entry<String, Cell> entry : this.getAnt().getNeighborCells() ){
			Cell neighbor = entry.getValue();			
			
			//get the pheremones on the neighbor
			ArrayList<Pheromone> pheremones = neighbor.getPheromones();
						
			//make sure it doesn't get stuck on enemy hives 
			
			
			//if there are pheremones, try to find the one with the strongest nest scent
			for( Pheromone pheremone : pheremones ){
				if( this.ratePheromone( pheremone ) >= bestRating ){
					bestCell = neighbor;
					bestRating = this.ratePheromone( pheremone ); 
				}
			}
		}
		
		return bestCell;
	}
	
	private Ant getAnt() {
		return this.ant;
	}

	
}
