package application;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import clue.ClueFileWriter;
import clue.ClueSolver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class HomeController {
	private ClueSolver game;
	
	private PathInfo paths;
	
	@FXML private TextField turnsInput;
	
	@FXML private Label label1;
	
	@FXML private ChoiceBox<String> suspectChoice;
	
	@FXML private ChoiceBox<String> placeChoice;
	
	@FXML private ChoiceBox<String> weaponChoice;
	
	@FXML private Button suggestButton;
	
	@FXML private Button accuseButton;
	
	@FXML private Button gameStateButton;
	
	@FXML private Spinner<Integer> spinner1;
	
	
	@FXML private TextArea textArea;

	
	@FXML protected void suggest(ActionEvent event) {
		String suspectName = suspectChoice.getValue();
		String placeName = placeChoice.getValue();
		String weaponName = weaponChoice.getValue();
		
		int suspect = -1;
		int place = -1;
		int weapon = -1;
		for (int i = 0; i < ClueSolver.cardNames.length; i++) {
			if (suspectName.equals(ClueSolver.cardNames[i])) {
				suspect = i;
			}
			if (placeName.equals(ClueSolver.cardNames[i])) {
				place = i;
			}
			if (weaponName.equals(ClueSolver.cardNames[i])) {
				weapon = i;
			}
		}
		
		if (suspect != -1 && place != -1 && weapon != -1) {
			game.enterPlayer1Turn(suspect, place, weapon);
			textArea.setText(game.getGameMessages());
			//Makes text area to scroll to updated bottom
			textArea.appendText("");
		}
		
	}
	
	@FXML protected void endTurn(ActionEvent event) {
		game.endPlayer1Turn();
		game.simulateOpenentTurns();
		textArea.setText(game.getGameMessages());
		//Makes text area to scroll to updated bottom
		textArea.appendText("");
	}
	
	@FXML protected void accuse(ActionEvent event) {
		String suspectName = suspectChoice.getValue();
		String placeName = placeChoice.getValue();
		String weaponName = weaponChoice.getValue();
		
		int suspect = -1;
		int place = -1;
		int weapon = -1;
		for (int i = 0; i < ClueSolver.cardNames.length; i++) {
			if (suspectName.equals(ClueSolver.cardNames[i])) {
				suspect = i;
			}
			if (placeName.equals(ClueSolver.cardNames[i])) {
				place = i;
			}
			if (weaponName.equals(ClueSolver.cardNames[i])) {
				weapon = i;
			}
		}
		
		if (suspect != -1 && place != -1 && weapon != -1) {
			game.accuse(suspect, place, weapon);
			textArea.setText(game.getGameMessages());
			//Makes text area to scroll to updated bottom
			textArea.appendText("");
		}
	}
	
	public void initData(ClueSolver game, PathInfo paths) {
		this.game = game;
		this.paths = paths;
		
		for (int i = 0; i < game.getNumberSuspects(); i++) {
			suspectChoice.getItems().add(ClueSolver.cardNames[i]);
		}
		suspectChoice.setValue(ClueSolver.cardNames[0]);
		
		for (int i = game.getNumberSuspects(); i < game.getNumberSuspects() + game.getNumberPlaces(); i++) {
			placeChoice.getItems().add(ClueSolver.cardNames[i]);
		}
		placeChoice.setValue(ClueSolver.cardNames[game.getNumberSuspects()]);
		
		for (int i = game.getNumberSuspects() + game.getNumberPlaces(); i < game.getNumberSuspects() + game.getNumberPlaces() + game.getNumberWeapons(); i++) {
			weaponChoice.getItems().add(ClueSolver.cardNames[i]);
		}
		weaponChoice.setValue(ClueSolver.cardNames[game.getNumberSuspects() + game.getNumberPlaces()]);
		
		
		textArea.setText(game.getGameMessages());
		textArea.appendText("");
	}
	
	@FXML private void initialize() {
		spinner1.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
	}
	
	@FXML protected void goToGameStateScene(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("GameState.fxml"));        
        Parent gameStateParent = loader.load();
        GameStateController controller = loader.getController();
        controller.initData(game, paths);
        
        Scene homeScene = new Scene(gameStateParent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(homeScene);
    }
	
	@FXML protected void goToGameSolutionScene(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("GameSolution.fxml"));        
        Parent gameStateParent = loader.load();
        GameSolutionController controller = loader.getController();
        controller.initData(game, paths);
        
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
		int k = spinner1.getValue();
		String command = "python3 " + paths.topK + " -L -K " + k + " -f ClueSolverInput -p " + paths.wcsp + " -O ClueSolverOutput"; 
		try {
			Process p = Runtime.getRuntime().exec(command);
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			p.waitFor(180, TimeUnit.SECONDS);
			String output = "";
			String line = "";
			while ( (line = in.readLine()) != null) {
				output += line;
			}
			in.close();
			textArea.setText(game.getGameMessages());
			//Makes text area to scroll to updated bottom
			textArea.appendText(output);
		} catch (Exception e) {
			textArea.setText(e.getMessage());
			return;
		}

		//System.out.println(output);
		//textArea.setText("WCSP input data written to file called 'ClueSolverInput'");
	}
	
	@FXML protected void readFromFile(ActionEvent event) {
		BufferedReader reader;
		List<List<HashSet<Integer>>> solutionData = new ArrayList<List<HashSet<Integer>>>();
		try {
			reader = new BufferedReader(new FileReader("ClueSolverOutput"));
			while (true) {
				String line="";
				while (!line.contains("Weight")) {
					line = reader.readLine();
					if (line == null) {
						break;
					}
				}
				if (line == null) {
					break;
				}
				int numberOfLines = (game.getNumberPlayers()+3)*21;
				solutionData.add(new ArrayList<HashSet<Integer>>());
				for (int i = 0; i < (game.getNumberPlayers()+3); i++) {
					solutionData.get(solutionData.size()-1).add(new HashSet<Integer>());
				}
				for (int i = 0; i < numberOfLines; i++) {
					line = reader.readLine();
					String[] splitLine = line.split("\\s+");
					if (splitLine[1].equals("1")) {
						int varNumber = Integer.parseInt(splitLine[0]);
						int cardNumber = varNumber / (game.getNumberPlayers()+3);
						int location = varNumber - cardNumber*(game.getNumberPlayers()+3);
						solutionData.get(solutionData.size()-1).get(location).add(cardNumber);
					}
				}
			}
			
			reader.close();
			
		} catch (Exception e) {
			textArea.setText(e.getMessage());
			return;
		}
		
		
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("SolverSolution.fxml"));        
        Parent gameStateParent;
		try {
			gameStateParent = loader.load();
	        SolverSolutionController controller = loader.getController();
	        controller.initData(game, solutionData, paths);
	        
	        Scene homeScene = new Scene(gameStateParent);
	        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
	        window.setScene(homeScene);
		} catch (IOException e) {
			textArea.setText(e.getMessage());
		}

	}

}
