package application;

import java.util.HashSet;
import java.util.Iterator;

import clue.ClueSolver;

/**
 * Class used to format table data showing location of each card in {@link GameSolutionController}
 * and {@link GameStateController}.
 * @author Milan
 *
 */
public class LocationData {
	public String location;
	public String cards;
	
	public LocationData(String location, HashSet<Integer> cards) {
		this.location = location;
		
		String builder = "";
		Iterator<Integer> it = cards.iterator();
		while (it.hasNext()) {
			if (builder.equals("")) {
				builder += ClueSolver.cardNames[it.next()];
			}
			else {
				builder += ", " + ClueSolver.cardNames[it.next()];
			}
		}
		this.cards = builder;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCards() {
		return cards;
	}

	public void setCards(String cards) {
		this.cards = cards;
	}
}
