package it.polito.tdp.crimes.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	private Graph<Integer, DefaultWeightedEdge> graph;
	private List<DistrictLatLon> forEdges;
	private List<DistrictPair> edges;
	private Simulator simulator;
	
	public Model() {
		this.dao = new EventsDao();
		this.simulator = new Simulator();
	}
	
	public void createGraph(Integer year) {
		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.graph, this.dao.getDistrictsId());
		this.forEdges = this.dao.getDistrictsLatLon(year);
		this.edges = new ArrayList<>();
		
		for(DistrictLatLon dll1 : this.forEdges) {
			for(DistrictLatLon dll2 : this.forEdges) {
				if(! dll1.getDistrict_id().equals(dll2.getDistrict_id())
						&& this.graph.getEdge(dll1.getDistrict_id(), dll2.getDistrict_id()) == null) {
					Graphs.addEdge(this.graph, dll1.getDistrict_id(), dll2.getDistrict_id(), this.calculateDistance(dll1.getLatlon(), dll2.getLatlon()));
					this.edges.add(new DistrictPair(dll1.getDistrict_id(), dll2.getDistrict_id(), this.calculateDistance(dll1.getLatlon(), dll2.getLatlon())));
				}
			}
		}
	}
	
	public void simulate(LocalDate date, Integer N) {
		this.simulator.init(this.dao.listEventsByDay(date), this.dao.getStartDistrictId(date.getYear()), N, this.forEdges);
		this.simulator.run();
	}
	
	public Integer negativeCases() {
		return this.simulator.getNEGATIVE_CASES();
	}
	
	public Integer crimesInDay(LocalDate date) {
		return this.dao.crimesInDay(date);
	}
	
	public List<Integer> getYearsCrimes() {
		return this.dao.getYearsCrimes();
	}
	
	public List<DistrictPair> getDistrictPairs() {
		return this.edges;
	}
	
	public List<Integer> getDistricts() {
		return this.dao.getDistrictsId();
	}
	
	public List<Integer> getMonthCrimes(Integer year) {
		return this.dao.getMonthCrimes(year);
	}

	public List<Integer> getDayCrimes(Integer year, Integer month) {
		return this.dao.getDayCrimes(year, month);
	}
	
	private Double calculateDistance(LatLng latlon1, LatLng latlon2) {
		return LatLngTool.distance(latlon1, latlon2, LengthUnit.KILOMETER);
	}


}
