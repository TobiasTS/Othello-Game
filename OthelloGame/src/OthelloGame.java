import java.awt.Component;
import java.util.Arrays;
import java.util.HashMap;

import nl.hanze.t23i.gamemodule.extern.AbstractGameModule;

/**
 * OthelloGame class for playing Othello.
 * Extends AbstractGameModule to run in the GameServer.
 * 
 * @author Tobias Schlichter
 */
public class OthelloGame extends AbstractGameModule {

	public static final String GAME_TYPE = Game.GAME_TYPE;
	
	private GameView gameView;
	private String nextPlayer;
	private String moveDetails;
	private HashMap<String, Integer> playerResults;
	
	// The board
	private int[][] board = new int[8][8];
	private char player1Char, player2Char;
	
	// Variables for on the board
	private static final int PLAYER1 = 0;
	private static final int PLAYER2 = 1;
	private static final int EMPTY = 2;

	// States of the game
	private static final int PLAYER1_WIN = 0;
	private static final int DRAW = 1;
	private static final int UNCLEAR = 2;
	private static final int PLAYER2_WIN = 3;

	
	/**
	 * Constructor of a new OthelloGame object.
	 * 
	 * @param playerOne player one of the game.
	 * @param playerTwo player two of the game.
	 */
	public OthelloGame(String playerOne, String playerTwo) {
		super(playerOne, playerTwo);
		
		moveDetails = null;
		playerResults = new HashMap<String, Integer>();
		gameView = new GameView(playerOne, playerTwo);
	}

	/**
	 * Method that handles and checks a move that is set by a player.
	 * Checks if it is the players turn.
	 * Checks if the move fits on the board.
	 * Checks if the place of the move is empty on the board.
	 * Plays the move if all checks are correct.
	 * Tests if the game is finished after the move is played.
	 * 
	 * @param player the player that sets the move.
	 * @param move the move that is played.
	 * @throws IllegalStateException if the match is not yet started, the match has finished or it is not player's turn.
	 * 
	 */
	@Override
	public void doPlayerMove(String player, String move) throws IllegalStateException {
		super.doPlayerMove(player, move);
		
		if(!nextPlayer.equals(player)) {
			throw new IllegalStateException("It is not player's turn");
		}
		
		gameView.addText(String.format("%s: %s", player, move));
		
		int moveInt = -1;
		
		try {
			moveInt = Integer.parseInt(move);
		} catch (NumberFormatException e) {
			illegalPlayerMove(player);
			return;
		}
		
		if(moveInt < 0 || moveInt > 63 || board[moveInt / 8][moveInt % 8] != EMPTY) {
			illegalPlayerMove(player);
			return;
		}
		
		// TODO: Check if move is legal Othello move
		
		playMove(moveInt);
		
		// TODO: Change the other values of the board
		
		int moveOutcome = positionValue();
		
		switch(moveOutcome) {
		case PLAYER1_WIN:
			matchStatus = MATCH_FINISHED;
			moveDetails = "Won";
			playerResults.put(player, PLAYER_WIN);
			playerResults.put(otherPlayer(player), PLAYER_LOSS);
			break;
		case PLAYER2_WIN:
			matchStatus = MATCH_FINISHED;
			moveDetails = "Won";
			playerResults.put(player, PLAYER_WIN);
			playerResults.put(otherPlayer(player), PLAYER_LOSS);
			break;
		case DRAW:
			matchStatus = MATCH_FINISHED;
			moveDetails = "Draw";
			playerResults.put(player, PLAYER_LOSS);
			playerResults.put(otherPlayer(player), PLAYER_LOSS);
			break;
		case UNCLEAR:
			moveDetails = "Next move";
			moveDetails += '\n';
			moveDetails += boardToString();
			nextPlayer();
			break;
		default:
			moveDetails = "Next move";
			moveDetails += '\n';
			moveDetails += boardToString();
			break;
		}
	}

	/**
	 * METHOD FROM THE GUESSGAME CODE
	 * Method that is called when an illegal move is played.
	 * 
	 * @param player the player that placed the illegal move.
	 */
	private void illegalPlayerMove(String player) {
		matchStatus = MATCH_FINISHED;
		moveDetails = "Illegal move";
		playerResults.put(otherPlayer(player), PLAYER_WIN);
		playerResults.put(player, PLAYER_LOSS);
	}
	
	/**
	 * METHOD FROM THE GUESSGAME CODE
	 * Returns the String commenting on the result of the match.
	 * 
	 * @return String with a comment on the match.
	 * @throws IllegalStateException if the match has not yet finished.
	 */
	@Override
	public String getMatchResultComment() throws IllegalStateException {
		super.getMatchResultComment();
		
		int moveOutcome = positionValue();
		
		if(moveOutcome == DRAW) {
			return "Draw";
		}
		else if(moveOutcome == PLAYER1_WIN || moveOutcome == PLAYER2_WIN) { 
			return "Win";
		}
		else {
			return "DEFAULT";
		}
	}

	/**
	 * METHOD FROM THE GUESSGAME CODE
	 * Returns the match status.
	 * 
	 * @return int of the match status.
	 */
	@Override
	public int getMatchStatus() {
		return matchStatus;
	}

	/**
	 * METHOD FROM THE GUESSGAME CODE
	 * Returns the details of the last move done during this match. 
	 * 
	 * @return String with the details of the move.
	 * @throws IllegalStateException if no move has been done during this match.
	 */
	@Override
	public String getMoveDetails() throws IllegalStateException {
		super.getMoveDetails();
		
		if(moveDetails == null) {
			throw new IllegalStateException("No move has been done during this match");
		}
		
		return moveDetails;
	}

	/**
	 * METHOD FROM THE GUESSGAME CODE
	 * Returns the name of the player who is to move next.
	 * 
	 * @return String player who has to move next.
	 * @throws IllegalStateException if the match has not yet finished.
	 */
	@Override
	public String getPlayerToMove() throws IllegalStateException {
		super.getPlayerToMove();
		
		return nextPlayer;
	}

	/**
	 * METHOD FROM THE GUESSGAME CODE
	 * Returns the result of a player.
	 * 
	 * @param player String of the player.
	 * @return int result of the player.
	 * @throws IllegalStateException if the match has not yet finished.
	 */
	@Override
	public int getPlayerResult(String player) throws IllegalStateException {
		super.getPlayerResult(player);
		
		return playerResults.get(player);
	}
	
	/**
	 * METHOD FROM THE GUESSGAME CODE
	 * Returns the score of a player.
	 * 
	 * @param player String of the player.
	 * @return the score of the player in as an int.
	 * @throws IllegalStateException if the match has not yet finished.
	 */
	@Override
	public int getPlayerScore(String player) throws IllegalStateException {
		super.getPlayerResult(player);
		
		return playerResults.get(player);
	}

	/**
	 * METHOD FROM THE GUESSGAME CODE
	 * Returns the message of the move.
	 * 
	 * @return String with the move message.
	 * @throws IllegalStateException if the match has not yet been started or the match has finished 
	 */
	@Override
	public String getTurnMessage() throws IllegalStateException {
		super.getTurnMessage();
		
		String message = null;
		
		if(moveDetails == null) {
			message = "Play a move using the numbers 0 till 8";
		} else {
			message = moveDetails;
		}
		
		return message;
	}

	
	/**
	 * METHOD FROM THE GUESSGAME CODE
	 * Returns the gameView component of the game.
	 * 
	 * @return Component game view.
	 */
	@Override
	public Component getView() {
		return gameView;
	}
	
	/**
	 * Is called when a new game starts.
	 * Decides which player goes first.
	 * Clears the playing board.
	 * 
	 * @throws IllegalStateException if the match is already started or the match has finished.
	 */
	@Override
	public void start() throws IllegalStateException {
		super.start();
		
		// TODO: Implementation of random player starts.
		
		clearBoard();
		nextPlayer = playerOne;
		if (getPlayerToMove() == playerOne) {
			player1Char = 'B';
			player2Char = 'W';
		} else {
			player2Char = 'W';
			player1Char = 'B';
		}
	}
	
	/**
	 * METHOD FROM THE GUESSGAME CODE
	 * Sets the next player to the other player.
	 */
	private void nextPlayer() {
		nextPlayer = otherPlayer(nextPlayer);
	}
	
	/**
	 * METHOD FROM THE GUESSGAME CODE
	 * Returns the other player.
	 * 
	 * @param player that is the current player.
	 * @return the other player.
	 */
	private String otherPlayer(String player) {
		return player.equals(playerOne) ? playerTwo : playerOne;
	}
	
	// Methods for Tic Tac Toe
	
	/**
	 * Plays a move by putting it on the board.
	 * 
	 * @param move integer that represents a position on the board.
	 */
	private void playMove(int move) {
		board[move / 3][move % 3] = getPlayerNumber();
	}
	
	/**
	 * Checks if the playing board is full.
	 * 
	 * @return boolean if the board is full.
	 */
	private boolean boardIsFull() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				// Return false if there's an empty spot
				if (board[i][j] == EMPTY) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Clears the playing board.
	 */
	private void clearBoard() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				board[i][j] = EMPTY;
			}
		}
	}

	/**
	 * Decides what the value of the game is at the moment.
	 * The value can be PLAYER1_WIN, PLAYER2_WIN, DRAW of UNCLEAR.
	 * 
	 * @return the value of the game.
	 */
	private int positionValue() {
		// Initialize arrays for the sums of rows, columns and diagonals
		String[] columns = new String[3];
		String[] rows = new String[3];
		String[] diagonals = new String[2];

		// Fill the arrays with an empty string
		Arrays.fill(columns, "");
		Arrays.fill(rows, "");
		Arrays.fill(diagonals, "");
		
		// Check column and row wins
		for (int i = 0; i < 3; i++) {
			diagonals[0] += board[i][i]+"";
		    diagonals[1] += board[i][2-i]+ "";
			for (int j = 0; j < 3; j++) {
				columns[i] += board[j][i];
				rows[i] += board[i][j];
			}
		}
		
		// Check for column wins
		for (int i = 0; i < 3; i++) {
			if (rows[i].equals("000") || columns[i].equals("000")) {
				return PLAYER1_WIN;
			} else if (rows[i].equals("111") || columns[i].equals("111")) {
				return PLAYER2_WIN;
			}
		}
		
		// Check for diagonal wins
		for (int i = 0; i < 2; i++) {
			if (diagonals[i].equals("000")) {
				return PLAYER1_WIN;
			} else if (diagonals[i].equals("111")) {
				return PLAYER2_WIN;
			}
		}

		// Check if board is full (draw)
		if (boardIsFull()) {
			return DRAW;
		}

		return UNCLEAR;
	}
	
	/**
	 * Returns the playing board in the form of a String.
	 * 
	 * @return String representation of the board.
	 */
	private String boardToString() {
		String str = "";
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[i][j] == PLAYER2)
					str += player2Char;
				else if (board[i][j] == PLAYER1)
					str += player1Char;
				else
					str += ".";
			}
			// Start new row
			str += '\n';
		}
		return str;
	}
	
	private int getPlayerNumber() {
		if(getPlayerToMove() == playerOne) {
			return PLAYER1;
		}
		else {
			return PLAYER2;
		}
	}
}
