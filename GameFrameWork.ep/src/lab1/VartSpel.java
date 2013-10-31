package lab1;
//importerar de paket som fanns i GoldModel
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
/** 
 * Ett spel för att testa lite idéer som förhoppningsvis
 * blir ett fint spel att senare lämna in.
 * 
 */
public class VartSpel extends GameModel{
	public enum Directions{
		// hmm, vet inte riktigt vad detta är eller hur den används.. :( Den fanns i GoldModel.java (ska kolla videon)
		
	}
	
	// TODO Definiera hur de olika spelobjekten ska se ut.
	// t.ex "private static final GameTile COLLECTOR_TILE = new RoundTile(Color.BLACK,Color.RED, 2.0)"  
	
	public VartSpel(){
		// TODO En constuctor för VartSpel. 
	}
	
	// TODO Andra trevliga metoder :) Mer avancerade saker som ska hända i gameUpdate som är snyggare att ha i en metod t.ex. moveObj newObj 
	public void gameUpdate(int lastKey) throws GameOverException {
		// TODO use the lastKey variable to tell what the player wants to do
		
		// TODO If game should progress when player is not doing anything make sure it happens (undra varför jag bytte till engelska...)
		
		// TODO if gameoverstate is fullfilled throw gameoverexception
		throw new GameOverException(0);
	}

	
}