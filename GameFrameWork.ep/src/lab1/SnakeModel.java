package lab1;
//importerar de paket som fanns i GoldModel
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import lab1.GoldModel.Directions;
/** 
 * @author Group 107: Benjamin Lindberg and Rasti Tengman 
 * 
 * <p><b>A simple snake game made by using the framework provided</b></p>
 *  
 */
public class SnakeModel extends GameModel{
	
	/** An enum with the four possible directions needed in the game  
	 *  every direction needs two arguments (int x, int y).
	 *  You need to look at it as a coordinate system with x and y meaning, 
	 *  positive y is upwards and negative downwards
	 *  positive x right and negative x is left.
	 */
	
	public enum Directions{
	
		
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
	 
	/** Graphical representation of an apple. */
	private static final GameTile APPLE_TILE = new RoundTile( new Color(54,124,43), new Color(114,244,83), 2.0); 
	
	/** Graphical representation of the head */
	private static final GameTile HEAD_TILE = new RoundTile(Color.BLACK, Color.RED, 2.0);
	
	/** Graphical representation of a part of the body*/
	private static final GameTile BODY_TILE = new RoundTile(Color.BLACK);
	
	/** Graphical representation of a blank tile. */
	private static final GameTile BLANK_TILE = new GameTile();
	
	/** The position of the apple that the snake wants to eat. */
	private Position applePos;
	
	/** The position of the body of the snake. */
	private final List<Position> bodyPos = new ArrayList<Position>();
	
	
	
	/** The direction of the head with initial value. */
	private Directions direction = Directions.NORTH;	
	
	
	
	
	public SnakeModel(){
		Dimension size = getGameboardSize();

		// Blank out the whole gameboard
		
	
		for (int i = 0; i < size.width; i++) {
			for (int j = 0; j < size.height; j++) {
				setGameboardState(i, j, BLANK_TILE);
			}
		}

		// Insert the head in the middle of the gameboard.
		this.bodyPos.add(new Position(size.width / 2, size.height / 2));
		setGameboardState(this.bodyPos.get(0), HEAD_TILE);
		// Insert a body
		this.bodyPos.add(this.bodyPos.get(0));
		
		// Insert an apple into the gameboard.
		addApple();
	}
	
	/**
	 * Update the direction of the snake
	 * according to the user's keypress.
	 */
	
	// VK_direction Is pre-programed keyboardbuttons for the arrow keys in java. 
	// Directions.direction it's our own programed directions.
	private void updateDirection(final int key) {
		switch (key) {
			case KeyEvent.VK_LEFT:
				if(this.direction == Directions.EAST){
					break;
				}else{
					this.direction = Directions.WEST;
					break;
				}
				
			case KeyEvent.VK_UP:
				if(this.direction == Directions.SOUTH){
					break;
				}else{
					this.direction = Directions.NORTH;
					break;
				}
			case KeyEvent.VK_RIGHT:
				if(this.direction == Directions.WEST){
					break;
				}else{
					this.direction = Directions.EAST;
					break;
				}
			case KeyEvent.VK_DOWN:
				if(this.direction == Directions.NORTH){
					break;
				}else{
					this.direction = Directions.SOUTH;
					break;
				}
			default:
				// Won't change direction if another key is pressed
				break;
		}
	}
	
	/**
	 * Insert a new apple into the gameboard.
	 */
	
	private void addApple() {
		Position newApplePos;
		Dimension size = getGameboardSize();
		// Loop until a blank position is found and ...
		do {
			newApplePos = new Position((int) (Math.random() * size.width),
										(int) (Math.random() * size.height));
		} while (!isPositionEmpty (newApplePos));

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
				this.bodyPos.get(this.bodyPos.size() - 1).getX() + this.direction.getXDelta(),
				this.bodyPos.get(this.bodyPos.size() - 1).getY() + this.direction.getYDelta());
	}

	 
	public void gameUpdate(int lastKey) throws GameOverException {
		updateDirection(lastKey);
		
		Position lastBodyPos=this.bodyPos.get(0);
		
		// Add body at the head 
		setGameboardState(this.bodyPos.get(this.bodyPos.size() - 1), BODY_TILE);
		
		// Remove last body tile of the snake
		if (!getGameboardState(lastBodyPos).equals(APPLE_TILE)) setGameboardState(lastBodyPos, BLANK_TILE);
		this.bodyPos.remove(0);
		
		// Change head position.
		this.bodyPos.add(getNextHeadPos());
		
		// Check for game over criterias.  
		if (isOutOfBounds(this.bodyPos.get(this.bodyPos.size() - 1)) || getGameboardState(this.bodyPos.get(this.bodyPos.size() - 1)) == BODY_TILE) {
			throw new GameOverException(this.bodyPos.size() - 1);
			
		}
		
		// Draw head at new position.
		setGameboardState(this.bodyPos.get(this.bodyPos.size() - 1), HEAD_TILE);
		
		if (getGameboardState(applePos) == HEAD_TILE) {
			this.bodyPos.add(0,lastBodyPos);
			addApple();
		}
	}
	
	/**
	 * Return wether the specified position is out of bounds.
	 * 
	 * @param pos The position to test
	 *  
	 * @return True if position is out ouf bounds
	 */
	private boolean isOutOfBounds(Position pos) {
		return pos.getX() < 0 || pos.getX() >= getGameboardSize().width
				|| pos.getY() < 0 || pos.getY() >= getGameboardSize().height;
	}
	
}