package it.polito.tdp.crimes.model;

import java.time.LocalDate;

public class TestModel {

	public static void main(String[] args) {
		Model m = new Model();
		m.createGraph(2015);
		m.simulate(LocalDate.of(2015, 01, 01), 8);
		m.negativeCases();
	}

}
