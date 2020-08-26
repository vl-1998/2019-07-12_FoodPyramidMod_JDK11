package it.polito.tdp.food;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.CalorieCongiunte;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

//controller turno A --> switchare al branch master_turnoB per turno B

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtPorzioni;

    @FXML
    private TextField txtK;

    @FXML
    private Button btnAnalisi;

    @FXML
    private Button btnCalorie;

    @FXML
    private Button btnSimula;

    @FXML
    private ComboBox<Food> boxFood;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCalorie(ActionEvent event) {
    	txtResult.clear();
    	
    	Food f = this.boxFood.getValue();
    	
    	if(f==null) {
    		txtResult.appendText("Scegliere un cibo.");
    	}
    	List<CalorieCongiunte> res = this.model.getCalorie(f);
    	
    	int i = 0;
    	
    	for(i=0; i<=4 && i < res.size(); i++) {
    		txtResult.appendText(res.get(i).toString()+"\n");
    	}

    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	Integer NP;
    	String nPorzioni = txtPorzioni.getText();
    	
    	if(nPorzioni == null) {
    		txtResult.appendText("Inserire il numero di porzioni.");
    	}
    	
    	try {
    		NP = Integer.parseInt(nPorzioni);
    		
    	}catch (IllegalArgumentException e) {
        	txtResult.appendText("Inserire valore numerico!");
        	return;
        }
    	this.model.creaGrafo(NP);
    	this.boxFood.getItems().clear();
    	this.boxFood.getItems().addAll(this.model.getVertex());
    	txtResult.appendText("Grafo creato! \n");
    	txtResult.appendText("#Vertici = "+this.model.getVertex().size()+" #Archi = "+this.model.getEdge().size());
    	
    }

    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();

    	Food f = this.boxFood.getValue();
    	
    	if(f==null) {
    		txtResult.appendText("Scegliere un cibo.");
    	}
    	
    	Integer K;
    	String nStazioni = txtK.getText();
    	
    	if(nStazioni == null) {
    		txtResult.appendText("Inserire il numero di stazioni.");
    	}
    	
    	try {
    		K = Integer.parseInt(nStazioni);
    		
    	}catch (IllegalArgumentException e) {
        	txtResult.appendText("Inserire valore numerico!");
        	return;
        }
    	
    	txtResult.appendText(this.model.simula(f, K));

    }

    @FXML
    void initialize() {
        assert txtPorzioni != null : "fx:id=\"txtPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCalorie != null : "fx:id=\"btnCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxFood != null : "fx:id=\"boxFood\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
	}
}
