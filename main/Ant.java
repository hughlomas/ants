package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import processing.core.PApplet;
import strategies.*;

public class Ant implements Entity {
	
	
	private static final int MAX_HEALTH = 100;
	private static final int MAX_ENERGY = 300;
	private static final int ENERGY_LOSS_PER_TURN = 1;
	private static final int ENERGY_THRESHOLD_CHECK_HUNGRY = 40;
	private Cell cell;
	private Cell lastCell;
	private int energy = Ant.MAX_ENERGY;
	private int health = Ant.MAX_HEALTH;
	private boolean alive = true;
	private Colony colony;
	private HashMap<String, Strategy> strategies;
	private PApplet parent;
	private Strategy strategy;
	private Entity carriedEntity;

	
	Ant(PApplet parent) {
		this.parent = parent;
		this.setStrategy( new FindFood( parent ) ); 
	}

	public void setCell(Cell cell) {
		this.cell = cell;
	}
	
	public boolean isDead(){
		return !this.alive;
	}
	
	public void setLastCell(Cell lastCell) {
		this.lastCell = lastCell;
	}

	public void moveTo( Cell cell ){
		
		this.setLastCell( this.getCell() );
		this.getLastCell().removeEntity( this );
		
		this.setCell( cell );
		this.getCell().addEntity( this );
		
		if( this.isCarryingSomething() ){
			this.carriedEntity.setCell( this.getCell() );
		}
	}
	
	public Cell getCell() {
		return this.cell;
	}

	public Cell getLastCell() {
		return this.lastCell;
	}
		
	public boolean ownsPheromoneOnCell( Cell cell ){
		for( Pheromone pheromone : cell.getPheromones() ){
			if( pheromone.getAnt().equals( this ) ){
				return true;
			}
		}
		return false;			
	}

	public void takeTurn() {
		
		this.checkHealth();
		this.checkEnergy();
				
		for( Entry<String, Cell> entry : this.getNeighborCells() ){
			Cell neighbor = entry.getValue();
			for( Entity entity : neighbor.getEntities() ){
				if( entity.getClass().equals("Ant") ){
					Ant ant = (Ant) entity;
					if( ant.getColony() != this.getColony() ){
						this.attack( ant );
					}
				}
			}
		}
		
		//check for food
		ArrayList<Food> foodOnCell = this.getCell().getFood();
		boolean onNestHole = this.isOnNestHole();
		if( !foodOnCell.isEmpty() && !this.isCarryingSomething() && !onNestHole ){
			this.pickUpEntity( (Food) foodOnCell.get(0) );
			this.setStrategy( new ReturnFoodToBase( parent ) );			
		}
		
		//drop food
		if( onNestHole && this.isCarryingFood() ){
			this.dropCarriedEntity();
			this.setStrategy( new FindFood( parent ) );	
		}
		
		if( this.isCarryingFood() ){
			this.leaveFoodPheromone();
		}
		
		this.leaveNestPheromone();
		
		//move
		Cell bestMove = this.getStrategy().getBestMove();
		if( ( bestMove == this.getLastCell() ) ){
			this.moveTo( this.getCell().getRandomNeighbor() );
		}
		else{
			this.moveTo( bestMove );
		}
		
		
		this.costEnergy( Ant.ENERGY_LOSS_PER_TURN );
		
	}
	
	public Set<Entry<String, Cell>> getNeighborCells(){
		return this.getCell().getNeighbors().entrySet();
	}
		
	
	private void die(){
		this.alive = false;
	}
	
	private void checkEnergy() {
		
		if( this.energy <= 0  ){
			this.die();
			return;
		}
		
		if( this.energy <= Ant.ENERGY_THRESHOLD_CHECK_HUNGRY ){
			//eat any carried food
			if( this.isCarryingFood() ){		
				Food food = (Food) this.carriedEntity;
				this.eatFood( food );
				this.carriedEntity = null;
				((ants) parent).getWorld().getFood().remove( food );
			}
			else if( !this.getCell().getFood().isEmpty() ){
				Food food = this.getCell().getFood().get(0);
				this.eatFood( food );
				((ants) parent).getWorld().getFood().remove( food );
				this.getCell().getFood().remove(0);
			}
		}
		
		
	}

	private void eatFood(Food food) {
		this.energy = Ant.MAX_ENERGY;
	}

	private void checkHealth() {
		if( this.health <= 0  ){
			this.die();
		}		
	}

	private void costEnergy( int energy ) {
		this.energy -= energy;
		
	}

	public boolean isCarryingSomething(){
		return this.carriedEntity != null;
	}
	
	private void pickUpEntity( Entity entity ){
		this.getCell().removeEntity( entity );
		this.setCarriedEntity( entity );
	}
	
	private void setCarriedEntity( Entity entity ){
		this.carriedEntity = entity;
	}
	
	private void dropCarriedEntity(){
		this.getCell().addEntity( this.carriedEntity );
		this.carriedEntity = null;
	}
	
	private boolean isCarryingFood(){
		if( this.isCarryingSomething() ){
			return ( this.carriedEntity.getClass().toString().equalsIgnoreCase( "class main.Food" ) );
		}
		return false;
	}

	private Strategy getStrategy(){
		return this.strategy;
	}
	public void setStrategy( Strategy strategy ){
		this.strategy = strategy;
		this.getStrategy().setAnt( this );
	}
		
	private boolean isOnNestHole(){
		return this.getColony().getNest().getHoles().contains( this.getCell() );
	}
	
	private void leaveNestPheromone() {
		Pheromone pheromone = this.buildNestPheromone();		
	    ((ants) parent).getWorld().getPheromones().add( pheromone );		
	}
	
	private void leaveFoodPheromone() {
		Pheromone pheromone = this.buildFoodPheromone();		
	    ((ants) parent).getWorld().getPheromones().add( pheromone );		
	}
		
	private Pheromone buildFoodPheromone(){	
		Pheromone pheromone = ((ants) parent).getWorld().getEntityFactory().pheromoneInstanceOn(this.getCell());
		pheromone.setStrength(Pheromone.MAX_STRENGTH);
		pheromone.setType( Pheromone.FOOD_SCENT );
		pheromone.setAnt( this );
		pheromone.setDecayRate(1);	
		return pheromone;
	}
	
	private Pheromone buildNestPheromone(){	
		Pheromone pheromone = ((ants) parent).getWorld().getEntityFactory().pheromoneInstanceOn(this.getCell());
		
		if( this.isOnNestHole() ){
			pheromone.setStrength( Pheromone.MAX_STRENGTH );
		}	
		else{
			int bestNestScent = 0;
			int existingNestScent = 0;		
			for( Pheromone existingPheromone : this.getLastCell().getPheromones() ){
				if( existingPheromone.getType().equals(Pheromone.NEST_SCENT) && existingPheromone.belongsToColony( this.getColony() ) ){
				existingNestScent = existingPheromone.getStrength();
					if( existingNestScent > bestNestScent ){
						bestNestScent = existingNestScent;
					}					
				}
			}
			
			pheromone.setStrength( --bestNestScent );
			
		}		
		pheromone.setType( Pheromone.NEST_SCENT );
		pheromone.setAnt( this );
		pheromone.setDecayRate(0);
		return pheromone;
	}
	
	public void attack(Ant ant) {
		ant.receiveDamage( 1 );		
	}

	public void receiveDamage(int i) {		
		this.health -= i;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public int getEnergy() {
		return energy;
	}

	public void setStrategies(HashMap<String, Strategy> strategies) {
		this.strategies = strategies;
	}

	public HashMap<String, Strategy> getStrategies() {
		return strategies;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public boolean isAlive() {
		return alive;
	}

	public Colony getColony() {
		return this.colony;
	}
	
	public void setColony( Colony colony ){
		this.colony = colony;
	}

}
