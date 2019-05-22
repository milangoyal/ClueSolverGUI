package application;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import clue.ClueFileWriter;
import clue.ClueSolver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class HomeController {
	private ClueSolver game;
	
	@FXML private TextField turnsInput;
	
	@FXML private Label label1;
	
	@FXML private Button simulateButton;
	
	@FXML private Button gameStateButton;
	
	
	@FXML private TextArea textArea;
	
	@FXML protected void simulate(ActionEvent event){
        int numberTurns = Integer.valueOf(turnsInput.getText());
        String message = game.simulateTurns(numberTurns);
        textArea.setText(message);
    }
	
	public void initData(ClueSolver game) {
		this.game = game;
	}
	
	@FXML protected void goToGameStateScene(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("GameState.fxml"));        
        Parent gameStateParent = loader.load();
        GameStateController controller = loader.getController();
        controller.initData(game);
        
        Scene homeScene = new Scene(gameStateParent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(homeScene);
    }
	
	@FXML protected void goToGameSolutionScene(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("GameSolution.fxml"));        
        Parent gameStateParent = loader.load();
        GameSolutionController controller = loader.getController();
        controller.initData(game);
        
        Scene homeScene = new Scene(gameStateParent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(homeScene);
    }
	
	@FXML protected void writeToFile(ActionEvent event) throws IOException{
		ClueFileWriter cFileWriter = new ClueFileWriter(game);
		String content = cFileWriter.getInputString();
	    BufferedWriter writer;
		writer = new BufferedWriter(new FileWriter("ClueSolverInput", false));
		writer.write(content);
		writer.flush();
		writer.close();
		
//		ProcessBuilder builder = new ProcessBuilder("./wcsp -L ClueSolverInput >> ClueSoverOutput");
//		builder.start();
		textArea.setText("WCSP input data written to file called 'ClueSolverInput'");

	}
	
	@FXML protected void readFromFile(ActionEvent event) throws IOException{
		BufferedReader reader;
		reader = new BufferedReader(new FileReader("ClueSolverOutput"));
		String line="";
		while (!line.contains("ID")) {
			line = reader.readLine();
		}
		int numberOfLines = (game.getNumberPlayers()+3)*21;
		List<HashSet<Integer>> solutionData = new ArrayList<HashSet<Integer>>();
		for (int i = 0; i < (game.getNumberPlayers()+3); i++) {
			solutionData.add(new HashSet<Integer>());
		}
		
		for (int i = 0; i < numberOfLines; i++) {
			line = reader.readLine();
			String[] splitLine = line.split("\\s+");
			if (splitLine[1].equals("1")) {
				int varNumber = Integer.parseInt(splitLine[0]);
				int cardNumber = varNumber / (game.getNumberPlayers()+3);
				int location = varNumber - cardNumber*(game.getNumberPlayers()+3);
				solutionData.get(location).add(cardNumber);
			}
		}

		reader.close();
		
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("SolverSolution.fxml"));        
        Parent gameStateParent = loader.load();
        SolverSolutionController controller = loader.getController();
        controller.initData(game, solutionData);
        
        Scene homeScene = new Scene(gameStateParent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(homeScene);
	}

}
