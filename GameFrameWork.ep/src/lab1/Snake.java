package lab1;
//importerar de paket som fanns i GoldModel
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import lab1.GoldModel.Directions;
/** 
 * Ett spel för att testa lite idéer som förhoppningsvis
 * blir ett fint spel att senare lämna in.
 * 
 */
public class Snake extends GameModel{
	public enum Directions{
		// hmm, vet inte riktigt vad detta är eller hur den används.. :( Den fanns i GoldModel.java (ska kolla videon)
		EAST(1, 0),
		WEST(-1, 0),
		NORTH(0, -1),
		SOUTH(0, 1),
		NONE(0, 0);

		private final int xDelta;
		private final int yDelta;

		Directions(final int xDelta, final int yDelta) {
			this.xDelta = xDelta;
			this.yDelta = yDelta;
		}

		public int getXDelta() {
			return this.xDelta;
		}

		public int getYDelta() {
			return this.yDelta;
		}
	}
	
	// TODO Definiera hur de olika spelobjekten ska se ut. Går att göra finare!  
	/** Graphical representation of a coin. */
	private static final GameTile APPLE_TILE = new RoundTile(Color.GREEN, Color.GREEN, 2.0);
	
	/** Graphical representation of the collector */
	private static final GameTile HEAD_TILE = new RoundTile(Color.BLACK, Color.RED, 2.0);
	
	/** Graphical representation of a part of the body*/
	private static final GameTile BODY_TILE = new RoundTile(Color.BLACK, Color.BLACK, 2.0);
	
	/** Graphical representation of a blank tile. */
	private static final GameTile BLANK_TILE = new GameTile();
			
	/** The position of the collector. */
	private Position headPos;
	
	/** The position of the collector. */
	private Position applePos;
	
	/** The position of the body. */
	private final List<Position> bodyPos = new ArrayList<Position>();;
	
	/** The direction of the head with initial value. */
	private Directions direction = Directions.NORTH;	
	
	/** The number of coins found. */
	private int score;
	
	public Snake(){
		// TODO En constuctor för Snake. 
		Dimension size = getGameboardSize();

		// Blank out the whole gameboard
		for (int i = 0; i < size.width; i++) {
			for (int j = 0; j < size.height; j++) {
				setGameboardState(i, j, BLANK_TILE);
			}
		}

		// Insert the collector in the middle of the gameboard.
		this.headPos = new Position(size.width / 2, size.height / 2);
		setGameboardState(this.headPos, HEAD_TILE);
		// Insert a body
		this.bodyPos.add(new Position(this.headPos.getX(), this.headPos.getY() - 1));
		
		// Insert coins into the gameboard.
		addApple();
	}
	
	/**
	 * Update the direction of the collector
	 * according to the user's keypress.
	 */
	private void updateDirection(final int key) {
		switch (key) {
			case KeyEvent.VK_LEFT:
				this.direction = Directions.WEST;
				break;
			case KeyEvent.VK_UP:
				this.direction = Directions.NORTH;
				break;
			case KeyEvent.VK_RIGHT:
				this.direction = Directions.EAST;
				break;
			case KeyEvent.VK_DOWN:
				this.direction = Directions.SOUTH;
				break;
			default:
				// Don't change direction if another key is pressed
				break;
		}
	}
	/**
	 * Insert another coin into the gameboard.
	 */
	private void addApple() {
		Position newApplePos;
		Dimension size = getGameboardSize();
		// Loop until a blank position is found and ...
		do {
			newApplePos = new Position((int) (Math.random() * size.width),
										(int) (Math.random() * size.height));
		} while (!isPositionEmpty(newApplePos));

		// ... add the apple to the empty tile.
		setGameboardState(newApplePos, APPLE_TILE);
		applePos = newApplePos;
	}
	
	/**
	 * Return whether the specified position is empty.
	 * 
	 * @param pos
	 *            The position to test.
	 * @return true if position is empty.
	 */
	private boolean isPositionEmpty(final Position pos) {
		return (getGameboardState(pos) == BLANK_TILE);
	}
	
	private Position getNextHeadPos() {
		return new Position(
				this.headPos.getX() + this.direction.getXDelta(),
				this.headPos.getY() + this.direction.getYDelta());
	}

	 
	public void gameUpdate(int lastKey) throws GameOverException {
		updateDirection(lastKey);
		
		Position lastBodyPos=this.bodyPos.get(0);
		
		// Add body at the head 
		setGameboardState(this.headPos, BODY_TILE);
		this.bodyPos.add(this.headPos);
		
		// Remove last body tile of the snake
		setGameboardState(lastBodyPos, BLANK_TILE);
		this.bodyPos.remove(0);
		
		// Change head position.
		this.headPos = getNextHeadPos();
		
		// Check for game over criterias
		if (isOutOfBounds(this.headPos) || getGameboardState(this.headPos) == BODY_TILE) {
			throw new GameOverException(this.bodyPos.size() - 1);
		}
		
		// Draw collector at new position.
		setGameboardState(this.headPos, HEAD_TILE);
		
		if (this.applePos.getX() == this.headPos.getX() && this.applePos.getY() == this.headPos.getY()) {
			addApple();
			this.bodyPos.add(lastBodyPos);
		}
	}
	
	private boolean isOutOfBounds(Position pos) {
		return pos.getX() < 0 || pos.getX() >= getGameboardSize().width
				|| pos.getY() < 0 || pos.getY() >= getGameboardSize().height;
	}
	
}