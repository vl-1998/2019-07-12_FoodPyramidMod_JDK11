package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDAO;

public class Model {
	private Graph <Food, DefaultWeightedEdge> grafo;
	private Map <Integer, Food> idMap;
	private FoodDAO dao;
	
	public Model() {
		this.dao = new FoodDAO();
	}
	
	public void creaGrafo(Integer nPorzioni) {
		this.grafo = new SimpleWeightedGraph <>(DefaultWeightedEdge.class);
		this.idMap = new HashMap<>();
		
		this.dao.getVertex(nPorzioni, idMap);
		
		for(Food f: this.idMap.values()) {
			this.grafo.addVertex(f);
		}
		
		for(Adiacenza a : this.dao.getArchi(idMap)) {
			Graphs.addEdgeWithVertices(this.grafo, a.getF1(), a.getF2(), a.getPeso());
		}
	}
	
	public List<Food> getVertex(){
		List<Food> vertici = new ArrayList<>(this.grafo.vertexSet());
		Collections.sort(vertici);
		return vertici;
	}
		
	public List<DefaultWeightedEdge> getEdge(){
		List<DefaultWeightedEdge> archi = new ArrayList<>(this.grafo.edgeSet());
		return archi;
	}
	
	public List<CalorieCongiunte> getCalorie(Food f){
		List<CalorieCongiunte> result = new ArrayList<>();
		
		List<Food> adiacenti = Graphs.neighborListOf(this.grafo, f);
		
		for(Food a: adiacenti) {
			CalorieCongiunte cc = new CalorieCongiunte (a, this.grafo.getEdgeWeight(this.grafo.getEdge(f, a)));
			result.add(cc);
		}
		Collections.sort(result);
		return result;
	}
	
	public String simula(Food f, Integer K) {
		Simulator sim = new Simulator(this.grafo, this);
		sim.setK(K);
		sim.init(f);
		sim.run();
		
		String result = String.format("Preparati %d cibi in %f minuti\n", sim.getnPreparati(), sim.gettPreparazione());
		return result;
		
	}
	
	


}
