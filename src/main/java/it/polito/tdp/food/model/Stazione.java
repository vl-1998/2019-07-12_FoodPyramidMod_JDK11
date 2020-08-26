package it.polito.tdp.food.model;

public class Stazione {
	private Food food;
	private Boolean libera;
	/**
	 * @param food
	 * @param libera
	 */
	public Stazione(Food food, Boolean libera) {
		super();
		this.food = food;
		this.libera = libera;
	}
	public Food getFood() {
		return food;
	}
	public void setFood(Food food) {
		this.food = food;
	}
	public Boolean getLibera() {
		return libera;
	}
	public void setLibera(Boolean libera) {
		this.libera = libera;
	}
	
	

}
