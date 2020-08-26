package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.food.model.Event.EventType;
import it.polito.tdp.food.model.Food.StatoPreparazione;

public class Simulator {
	//MODELLO DEL MONDO
	private List<Stazione> stazioni;
	private List<Food> cibi;
	
	private Graph<Food, DefaultWeightedEdge> grafo;
	private Model model;
	
	//PARAMETRI DI SIMULAZIONE
	private Integer K = 5;
	
	//RISULTATI CALCOLATI
	private Integer nPreparati;
	private Double tPreparazione;
	
	//CODA DEGLI EVENTI 
	private PriorityQueue<Event> queue;
	
	public Simulator(Graph<Food, DefaultWeightedEdge> grafo, Model model) {
		this.grafo = grafo;
		this.model = model;
	}

	public Integer getnPreparati() {
		return nPreparati;
	}

	public Double gettPreparazione() {
		return tPreparazione;
	}

	public Integer getK() {
		return K;
	}

	public void setK(Integer k) {
		K = k;
	}
	
	//inizializzaione
	public void init(Food f) {
		this.nPreparati=0;
		this.tPreparazione=0.0;
		
		//Genero la lista dei cibi da preparare
		this.cibi = new ArrayList<>(this.grafo.vertexSet());
		for(Food a : this.cibi) {
			a.setStato(StatoPreparazione.DA_PREPARARE);
		}
		
		this.stazioni = new ArrayList<>();
		for(int i = 0; i<=this.K; i++) {
			Stazione s = new Stazione(null, true);
			this.stazioni.add(s); //stazioni libere che non stanno preparando alcun cibo
		}
		
		this.queue = new PriorityQueue <Event> ();
		List<CalorieCongiunte> vicini = this.model.getCalorie(f);
		
		//analizzo i vicinin fino ad un valore massimo di K vicini
		for (int i = 0; i<this.K && i<vicini.size(); i++) {
			this.stazioni.get(i).setLibera(false);
			this.stazioni.get(i).setFood(vicini.get(i).getF1());
			
			//genero l'evento di inizio preparazione dei K cibi 
			Event e = new Event (vicini.get(i).getPeso(), stazioni.get(i), vicini.get(i).getF1(), EventType.INIZIO);
			this.queue.add(e);
		}
		
	}
	
	public void run() {
		while(!this.queue.isEmpty()) {
			//estraggo l'evento
			Event e = this.queue.poll();
			System.out.println(e);
			processEvent(e);
		}
		
	}

	private void processEvent(Event e) {
		switch(e.getType()) {
		case INIZIO:
			//scelgo il nuovo cibo da iniziare a preparare
			List<CalorieCongiunte> vicini = this.model.getCalorie(e.getFood());
			CalorieCongiunte prossimo = null;
			
			for(CalorieCongiunte cc : vicini) {
				if(cc.getF1().getStato() == StatoPreparazione.DA_PREPARARE) {
					prossimo = cc;
					break;
				}
			}
			
			if(prossimo != null) {
				prossimo.getF1().setStato(StatoPreparazione.IN_PREPARAZIONE);
				e.getStazione().setLibera(false);
				e.getStazione().setFood(prossimo.getF1());
				
				Event e2 = new Event(e.getTime()+prossimo.getPeso(), e.getStazione(), prossimo.getF1(), EventType.FINE);
				queue.add(e2);
			}
			//se non ho prossimo esco dallo switch
			break;
			
		case FINE:
			//aggiorno lo stato del mondo
			this.nPreparati++;
			this.tPreparazione=e.getTime();
			
			//libero la stazione
			e.getStazione().setLibera(true);
			//aggiorno lo stato del cibo
			e.getFood().setStato(StatoPreparazione.PREPARATO);
			
			//genero un evento con il vecchio food ma di tipo inizio, nel case inizio gestirò la lista
			//di prossimi food, successivi a questo
			Event e1 = new Event(e.getTime(), e.getStazione(), e.getFood(), EventType.INIZIO);
			this.queue.add(e1);
			
			break;
		}
		
	}
	
	
	

}
