package strategies;

import main.Cell;
import main.Ant;

public interface Strategy {
	public int ratePheromone( main.Pheromone pheremone );
	public Cell getBestMove();
	public void setAnt( Ant ant );	
}

