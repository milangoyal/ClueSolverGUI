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

public class GameStateController {
	private ClueSolver game;
	private ObservableList<LocationData> tableData = FXCollections.observableArrayList();

	@FXML private TableView<LocationData> table1;
	//@FXML private TableColumn<LocationData, String> location;
	@FXML private TableColumn<LocationData, String> cards;
	@FXML private TableColumn<LocationData, String> locations;
	
	public void initData(ClueSolver game) {
		this.game = game;
		List<HashSet<Integer>> playerHands = game.getHands();
		for (int i = 0; i < playerHands.size(); i++) {
			String location = "Player " + Integer.toString(i+1);
			tableData.add(new LocationData(location, playerHands.get(i)));
		}
	}
	
	@FXML private void initialize() {
		locations.setCellValueFactory(new PropertyValueFactory<>("location"));
		cards.setCellValueFactory(new PropertyValueFactory<>("cards"));
		table1.setItems(tableData);
	}
	
	@FXML protected void goToHomeScene(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Home.fxml"));        
        Parent homeParent = loader.load();
        HomeController controller = loader.getController();
        controller.initData(game);
        
        Scene homeScene = new Scene(homeParent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(homeScene);
    }
}
