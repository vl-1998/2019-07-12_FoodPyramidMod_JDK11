package it.polito.tdp.food.model;

public class Event implements Comparable<Event> {
	public enum EventType{
		INIZIO,
		FINE,
	}
	private Double time;
	private Stazione stazione;
	private Food food;
	private EventType type;
	/**
	 * @param time
	 * @param stazione
	 * @param food
	 * @param type
	 */
	public Event(Double time, Stazione stazione, Food food, EventType type) {
		super();
		this.time = time;
		this.stazione = stazione;
		this.food = food;
		this.type = type;
	}
	public Double getTime() {
		return time;
	}
	public void setTime(Double time) {
		this.time = time;
	}
	public Stazione getStazione() {
		return stazione;
	}
	public void setStazione(Stazione stazione) {
		this.stazione = stazione;
	}
	public Food getFood() {
		return food;
	}
	public void setFood(Food food) {
		this.food = food;
	}
	public EventType getType() {
		return type;
	}
	public void setType(EventType type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "time=" + time + ", stazione=" + stazione + ", food=" + food + ", type=" + type;
	}
	@Override
	public int compareTo(Event o) {
		// TODO Auto-generated method stub
		return this.time.compareTo(o.getTime());
	}
	
	

}
