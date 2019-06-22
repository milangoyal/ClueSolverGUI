package application;

import java.io.IOException;

import clue.ClueSolver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class StartController {
	
	private ClueSolver game;
	private PathInfo paths;

	@FXML private TextField input1;
	
	@FXML private TextField main_path;
	
	@FXML private TextField binary_path;
	
	@FXML private Label label1;
	
	@FXML private Button button1;
	
	@FXML protected void createGame(ActionEvent event) throws IOException{
        int numberPlayers = Integer.valueOf(input1.getText());
        game = new ClueSolver(numberPlayers);
        game.startGame();
        paths = new PathInfo(binary_path.getText(), main_path.getText());
        
        //input1.setText(String.valueOf(game.getNumberPlaces()));
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
