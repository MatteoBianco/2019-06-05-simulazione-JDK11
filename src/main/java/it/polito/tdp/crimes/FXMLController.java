/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.DistrictPair;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="boxGiorno"
    private ComboBox<Integer> boxGiorno; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaReteCittadina"
    private Button btnCreaReteCittadina; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaReteCittadina(ActionEvent event) {
    	txtResult.clear();
    	
    	Integer year = boxAnno.getValue();
    	if(year == null) {
    		txtResult.setText("Selezionare un anno dall'apposita tendina.\n");
    		return;
    	}
    	this.model.createGraph(year);
    	this.btnSimula.setDisable(false);
    	txtResult.appendText("Grafo creato!\n\n");
    	List<Integer> districts = this.model.getDistricts();
    	List<DistrictPair> pairs = this.model.getDistrictPairs();
    	Collections.sort(pairs);
    	for(Integer i : districts) {
    		for(DistrictPair dp : pairs) {
    			if(dp.getDistrictId1().equals(i))
    				txtResult.appendText(String.format("Distretto %d - Distretto %d --> Distanza %.3f\n", i, dp.getDistrictId2(), dp.getDistance()));
    			if(dp.getDistrictId2().equals(i)) 
    				txtResult.appendText(String.format("Distretto %d - Distretto %d --> Distanza %.3f\n", i, dp.getDistrictId1(), dp.getDistance()));
    		}
    		txtResult.appendText("\n");
    	}
    	boxMese.setDisable(false);
    	boxMese.getItems().addAll(this.model.getMonthCrimes(year));
    }
    
    @FXML
    void doAbilitaGiorno(ActionEvent event) {
    	Integer year = boxAnno.getValue();
    	if(year == null) {
    		txtResult.setText("Seleziona un anno\n");
    		return;
    	}
    	Integer month = boxMese.getValue();
    	if(month == null) {
    		txtResult.setText("Seleziona un mese\n");
    		return;
    	}
    	boxGiorno.getItems().clear();
    	boxGiorno.setDisable(false);
    	boxGiorno.getItems().addAll(this.model.getDayCrimes(year, month));
    }
    
    @FXML
    void doBloccoTotale(ActionEvent event) {
    	btnSimula.setDisable(true);
    	boxMese.setDisable(true);
    	boxGiorno.setDisable(true);
    }

    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	Integer day = boxGiorno.getValue();
    	Integer month = boxMese.getValue();
    	Integer year = boxAnno.getValue();
    	if(day == null || month == null || year == null) {
    		txtResult.appendText("Errore, selezionare una data per eseguire la simulazione!");
    		return;
    	}
    	
    	Integer N;
    	try {
    		N = Integer.parseInt(txtN.getText());
    	} catch(NumberFormatException e) {
    		txtResult.appendText("Inserire un numero intero che rappresenta il numero di poliziotti a disposizione.");
    		return;
    	}
    	
    	if(N > 10 || N < 1) {
    		txtResult.appendText("Il numero di poliziotti deve essere un numero compreso tra 1 e 10.");
    		return;
    	}
    	LocalDate date;
    	try {
    		 date = LocalDate.of(year, month, day);
    	} catch(RuntimeException e) {
    		txtResult.appendText("Errore nella creazione della data.");
    		return;
    	}
    	this.model.simulate(date, N);
    	txtResult.appendText(String.format("Simulazione completata.\n"
    			+ "Per problemi di mancanza di personale o ritardo di intervento, sui %d crimini "
    			+ "totali avvenuti nel giorno in analisi, %d non sono stati gestiti o sono stati gestiti "
    			+ "con un ritardo superiore ai 15 minuti.\n", this.model.crimesInDay(date), this.model.negativeCases()));
    	

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaReteCittadina != null : "fx:id=\"btnCreaReteCittadina\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxAnno.getItems().addAll(this.model.getYearsCrimes());
    	btnSimula.setDisable(true);
    	boxMese.setDisable(true);
    	boxGiorno.setDisable(true);
    }
}
