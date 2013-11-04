package lab1;
//importerar de paket som fanns i GoldModel
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import lab1.GoldModel.Directions;
/** 
 * A game to test some different ideas and hopefully 
 * turns in to a nice looking game to hand-in.
 * 
 */
public class Snake extends GameModel{
	public enum Directions{
		/* An enum with the four possible directions needed in the game  
		 *  every direction needs two arguments (int x, int y).
		 *  You need to look at it as a coordinate system with x and y meaning, 
		 *  positive y is upwards and negative downwards
		 *  positive x right and negative x is left.
		 */
		
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
	
	// TODO Define how the different game objects shall look like. Possible to do in a nicer way!  
	/** Graphical representation of a coin. */
	
	// FIXME Benjamin vad gör 2,0? Det funkade ju lika bra utan?
	
	private static final GameTile APPLE_TILE = new RoundTile( new Color(114,244,83)); //testar rgb
	
	/** Graphical representation of the collector */
	
	//FIXME Benjamin varör är det två färger?
	private static final GameTile HEAD_TILE = new RoundTile(Color.BLACK, Color.RED, 2.0);
	
	/** Graphical representation of a part of the body*/
	private static final GameTile BODY_TILE = new RoundTile(Color.BLACK, Color.BLACK, 2.0);
	
	/** Graphical representation of a blank tile. */
	private static final GameTile BLANK_TILE = new GameTile();
			
	/** The position of the collector. */
	private Position headPos;
	
	/** The position of the apple that the snake wants to eat (collect). */
	private Position applePos;
	
	/** The position of the body of the snake. */
	private final List<Position> bodyPos = new ArrayList<Position>();
	
	
	
	/** The direction of the head with initial value. */
	//Sets the initial value to north, meaning that the snake moves upwards when the game begins.
	private Directions direction = Directions.NORTH;	
	
	/** The number of coins found. */
	private int score;
	
	public Snake(){
		Dimension size = getGameboardSize();

		// Blank out the whole gameboard
		
		//FIXME Benjamin är detta den första spelrutan som kommer upp? 
		//FIXME Vad exakt är de som händer här?
		for (int i = 0; i < size.width; i++) {
			for (int j = 0; j < size.height; j++) {
				setGameboardState(i, j, BLANK_TILE);
			}
		}

		// Insert the collector in the middle of the gameboard.
		this.headPos = new Position(size.width / 2, size.height / 2);
		setGameboardState(this.headPos, HEAD_TILE);
		// Insert a body
		this.bodyPos.add(new Position(this.headPos.getX(), this.headPos.getY() - 1)); // Vad gör -1?
		
		// Insert coins into the gameboard.
		addApple();
	}
	
	/**
	 * Update the direction of the collector
	 * according to the user's keypress.
	 */
	
	// VK_direction Is pre-programed keyboardbuttons for the arrow keys in java. 
	// Directions.direction it's our own programed directions.
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
				// Won't change direction if another key is pressed
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
		//FIXME Varför?
		setGameboardState(lastBodyPos, BLANK_TILE);
		this.bodyPos.remove(0);
		
		// Change head position.
		this.headPos = getNextHeadPos();
		
		// Check for game-over criterias.  
		if (isOutOfBounds(this.headPos) || getGameboardState(this.headPos) == BODY_TILE) {
			throw new GameOverException(this.bodyPos.size() - 1);
			//FIXME Varför - 1 på den här ovan? Är detta för att man skall kunna returnera spelarens poäng?
		}
		
		// Draw head (collector) at new position.
		setGameboardState(this.headPos, HEAD_TILE);
		
		if (this.applePos.getX() == this.headPos.getX() && this.applePos.getY() == this.headPos.getY()) {
			addApple();
			this.bodyPos.add(0,lastBodyPos);
		}
	}
	
	//Checks that the snake hasen't crashed with walls.
	private boolean isOutOfBounds(Position pos) {
		return pos.getX() < 0 || pos.getX() >= getGameboardSize().width
				|| pos.getY() < 0 || pos.getY() >= getGameboardSize().height;
	}
	
}