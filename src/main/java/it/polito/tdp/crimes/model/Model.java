package it.polito.tdp.crimes.model;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	private Graph<Integer, DefaultWeightedEdge> graph;
	
	public Model() {
		this.dao = new EventsDao();
	}
	
	public void createGraph(Integer year) {
		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
	}
	
	public List<Integer> getYearsCrimes() {
		return this.dao.getYearsCrimes();
	}
}
