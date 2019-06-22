package application;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import clue.ClueSolver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class SolverSolutionController {
	private ClueSolver game;
	private PathInfo paths;
	private int k_solution;
	private List<List<HashSet<Integer>>> solverSolution;
	private ObservableList<LocationData> tableData = FXCollections.observableArrayList();

	@FXML private TableView<LocationData> table1;
	@FXML private TableColumn<LocationData, String> cards;
	@FXML private TableColumn<LocationData, String> locations;
	@FXML private Label label1;
	
	public void initData(ClueSolver game, List<List<HashSet<Integer>>> solverSolution, PathInfo paths) {
		this.game = game;
		this.paths = paths;
		this.k_solution = 0;
		label1.setText("Solution Number " + k_solution);
		this.solverSolution = solverSolution;
		for (int i = 0; i < solverSolution.get(0).size(); i++) {
			String location;
			if (i >= game.getNumberPlayers()) {
				location = "Case File " + (i - game.getNumberPlayers() + 1);
			}
			else {
				location = "Player " + Integer.toString(i+1);
			}
			tableData.add(new LocationData(location, solverSolution.get(0).get(i)));
		}
	}
	
	@FXML private void initialize() {
		locations.setCellValueFactory(new PropertyValueFactory<>("location"));
		cards.setCellValueFactory(new PropertyValueFactory<>("cards"));
		table1.setItems(tableData);
	}
	
	@FXML protected void prevButtonClick(ActionEvent event) {
		if (k_solution > 0) {
			k_solution -=1;
			label1.setText("Solution Number " + k_solution);
			tableData.clear();
			fillTable(k_solution);
		}
	}
	
	@FXML protected void nextButtonClick(ActionEvent event) {
		if (k_solution < solverSolution.size() - 1) {
			k_solution +=1;
			label1.setText("Solution Number " + k_solution);
			tableData.clear();
			fillTable(k_solution);
		}
	}
	
	private void fillTable(int k) {
		for (int i = 0; i < solverSolution.get(k).size(); i++) {
			String location;
			if (i >= game.getNumberPlayers()) {
				location = "Case File " + (i - game.getNumberPlayers() + 1);
			}
			else {
				location = "Player " + Integer.toString(i+1);
			}
			tableData.add(new LocationData(location, solverSolution.get(k).get(i)));
		}
	}
	
	@FXML protected void goToHomeScene(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Home.fxml"));        
        Parent homeParent = loader.load();
        HomeController controller = loader.getController();
        controller.initData(game, paths);
        
        Scene homeScene = new Scene(homeParent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(homeScene);
    }
}
