/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.sun.tools.javac.util.List;

import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Model;
import it.polito.tdp.imdb.model.Vicino;
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

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnAdiacenti"
    private Button btnAdiacenti; // Value injected by FXMLLoader

    @FXML // fx:id="btnCercaAffini"
    private Button btnCercaAffini; // Value injected by FXMLLoader

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxRegista"
    private ComboBox<Director> boxRegista; // Value injected by FXMLLoader

    @FXML // fx:id="txtAttoriCondivisi"
    private TextField txtAttoriCondivisi; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	int anno = boxAnno.getValue();
    	model.creaGrafo(anno);
    	txtResult.appendText(Integer.toString(model.numVertici()) + "\n");
    	txtResult.appendText(Integer.toString(model.numArchi()) + "\n");
    	
    	boxRegista.getItems().addAll(model.getDirettori());
    }

    @FXML
    void doRegistiAdiacenti(ActionEvent event) {
    	txtResult.clear();
    	Director d = boxRegista.getValue();
    	int anno = boxAnno.getValue();
    	ArrayList<Vicino> lista = model.vicini(d, anno);
    	for (Vicino v: lista) {
    	txtResult.appendText(v.toString());
    	}
    	//txtResult.appendText(model.adiacenti(d).toString());
    }

    @FXML
    void doRicorsione(ActionEvent event) {
    	String i = txtAttoriCondivisi.getText();
    	int num = Integer.parseInt(i);
    	Director d = boxRegista.getValue();
    	int anno = boxAnno.getValue();
  //  	model.trovaPercorso(d, num, anno);
    	
    	java.util.List<Director> lista = model.trovaPercorso1(d, num);
    	txtResult.clear();
    	txtResult.appendText(lista.toString());
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAdiacenti != null : "fx:id=\"btnAdiacenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCercaAffini != null : "fx:id=\"btnCercaAffini\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxRegista != null : "fx:id=\"boxRegista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtAttoriCondivisi != null : "fx:id=\"txtAttoriCondivisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
   public void setModel(Model model) {
    	
    	this.model = model;
    	
    	ArrayList<Integer> anni = new ArrayList<Integer>();
    	for (int i = 2004; i<=2008; i++) {
    		anni.add(i);
    	}
    	boxAnno.getItems().addAll(anni);
    }
    
}
