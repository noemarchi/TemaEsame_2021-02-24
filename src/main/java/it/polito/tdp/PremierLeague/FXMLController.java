
/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.GiocatoreMigliore;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Model;
import it.polito.tdp.PremierLeague.model.OutputSim;
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
    @FXML // fx:id="btnGiocatoreMigliore"
    private Button btnGiocatoreMigliore; // Value injected by FXMLLoader
    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader
    @FXML // fx:id="cmbMatch"
    private ComboBox<Match> cmbMatch; // Value injected by FXMLLoader
    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader
    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) 
    {
    	Match m = this.cmbMatch.getValue();
    	
    	if(m == null)
    	{
    		this.txtResult.setText("Errore!! Seleziona una partita!");
    		return;
    	}
    	
    	this.model.creaGrafo(m);
    	this.txtResult.setText("Grafo creato!\n");
    	this.txtResult.appendText("Numero vertici: " + model.nVertici() +"\n");
    	this.txtResult.appendText("Numero archi: " + model.nArchi() +"\n");
    }

    @FXML
    void doGiocatoreMigliore(ActionEvent event) 
    {    	
    	GiocatoreMigliore best = this.model.getMigliore();
    	
    	if(best == null)
    	{
    		this.txtResult.setText("Errore!! Seleziona prima una partita e crea il grafo!");
    		return;
    	}
    	
    	this.txtResult.appendText("\n" + best.toString());
    }
    
    @FXML
    void doSimula(ActionEvent event) 
    {
    	int azioni = -1;
    	
    	try
    	{
    		azioni = Integer.parseInt(this.txtN.getText());
    	}
    	catch (NumberFormatException e)
    	{
    		this.txtResult.setText("Errore!! Inserisci un valore numerico!");
    		return;
    	}
    	
    	OutputSim ret = model.simula(azioni);
    	
    	if(ret == null)
    	{
    		this.txtResult.setText("Errore!! Seleziona prima una partita e crea il grafo!");
    	}
    	
    	this.txtResult.appendText("\n\nPartita: " + ret.getPartita().toString() +"\n");
    	this.txtResult.appendText("Gol casa: " + ret.getGolCasa() +"\n");
    	this.txtResult.appendText("Gol ospiti: " + ret.getGolOspiti() +"\n");
    	this.txtResult.appendText("Espulsi casa: " + ret.getEspCasa() +"\n");
    	this.txtResult.appendText("Espulsi ospiti: " + ret.getEspOspiti() +"\n");
    	this.txtResult.appendText("Totale infortunati: " + ret.getTotInfortuni() +"\n");
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() 
    {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnGiocatoreMigliore != null : "fx:id=\"btnGiocatoreMigliore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbMatch != null : "fx:id=\"cmbMatch\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) 
    {
    	this.model = model;
    	this.cmbMatch.getItems().setAll(model.listAllMatches());
    }
}
