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
	
	private static final int[] OFFSET_X = {-1, -1, -1,  0,  0,  1,  1,  1};
	private static final int[] OFFSET_Y = {-1,  0,  1, -1,  1, -1,  0,  1};

	private int[][] startW = {{3,3}, {4,4}};
	private int[][] startB = {{3,4}, {4,3}};
	
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
	 * Is called when a new game starts.
	 * Decides which player goes first.
	 * Clears the playing board.
	 * 
	 * @throws IllegalStateException if the match is already started or the match has finished.
	 */
	@Override
	public void start() throws IllegalStateException {
		super.start();
		
		if((Math.random() * 10) >= 5)
			nextPlayer = playerOne;
		else
			nextPlayer = playerTwo;
			
		clearBoard();		
		
		if (getPlayerToMove() == playerOne) {
			player1Char = 'B';
			player2Char = 'W';
		} else {
			player2Char = 'W';
			player1Char = 'B';
		}
		
		//starting positions
		board[startW[0][0]][startW[0][1]] = getPlayerNumber();
		board[startW[1][0]][startW[1][1]] = getPlayerNumber();
		board[startB[0][0]][startB[0][1]] = getOpponentNumber();
		board[startB[1][0]][startB[1][1]] = getOpponentNumber();
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
	 * @param move the move that is played. Assumes move is "1,3"
	 * @throws IllegalStateException if the match is not yet started, the match has finished or it is not player's turn.
	 * 
	 */
	@Override
	public void doPlayerMove(String player, String move) throws IllegalStateException {
		super.doPlayerMove(player, move);
		int X; int Y;
		if(!nextPlayer.equals(player)) {
			throw new IllegalStateException("It is not player's turn");
		}
		
		//parse move to X,Y
		try {
			X = Integer.parseInt(move.substring(0, 1));
			Y = Integer.parseInt(move.substring(2, 3));
		} catch (NumberFormatException e) {
			moveDetails = "parse error";
			return;
		}catch (StringIndexOutOfBoundsException e){
			moveDetails = "parse error";
			return;
		}
		
		//checks if move is legal 
		if(!moveIsLegal(X,Y,true)){
			moveDetails = "Illegal move";
			return;
		}
		
		playMove(X,Y);
				
		int moveOutcome = positionValue();
		
		gameView.addText(String.format("%s: %s", player, move));
		
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
			//TODO if no possible moves skip turn
			int[][] temp = getPlayableMoves();
			if(temp != null){
				nextPlayer();
			}else{
				moveDetails +=  getPlayerToMove() + " has no available moves!";
			}
			
			break;
		default:
			moveDetails = "Next move";
			moveDetails += '\n';
			moveDetails += boardToString();
			break;
		}
	}
	
	/**
	 * the Ai of orthello gets the best move
	 * 
	 * 
	 */
	public int[] doAIMove(int side){
		int opp;
		int[] reply = new int[3];
		if (side == PLAYER2){
			reply[2] = PLAYER1_WIN;
			opp = PLAYER1;
		}else{
			reply[2] = PLAYER2_WIN;
			opp = PLAYER2;
		}
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(moveIsLegal(j, i,false)){
					playMove(j, i);
					int[] bestOpp = doAIMove(opp);
					double bestValue;
					if(side == PLAYER2){//maximizing
						bestValue = Double.NEGATIVE_INFINITY;
						//if(reply[2] < bestOpp[2]){
							reply[0] = j;
							reply[1] = i;
							reply[2] = bestOpp[2];
						//}
					}
					else{//minimizing
						if(reply[2] > bestOpp[2]){
							reply[0] = j;
							reply[1] = i;
							reply[2] = bestOpp[2];
						}
					}
					board[j][i] = EMPTY;
					
				}
			}
		}
		return reply;
	}
	
	/**
	 * method that returns the possible playable moves of a player
	 * 
	 * @returns 2D array with possible playable moves
	 */
	private int[][] getPlayableMoves(){
		//TODO change length of array playableMoves
		int[][] playableMoves = new int[15][2];
		int counter = 0;
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(moveIsLegal(j, i,false)){
					playableMoves[counter][0] = i;
					playableMoves[counter][1] = j;
					counter++;
				}
			}
		}
		if(playableMoves[0][0] == 0 && playableMoves[0][1] == 0){
			playableMoves = null;
		}
		
		return playableMoves;
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
			message = "Play a move using row,column E: 3,2";
		} else {
			message = moveDetails;
		}
		
		return message;
	}

	
	/**
	 * Returns the gameView component of the game.
	 * 
	 * @return Component game view.
	 */
	@Override
	public Component getView() {
		return gameView;
	}
	
	
	/**
	 * Sets the next player to the other player.
	 */
	private void nextPlayer() {
		nextPlayer = otherPlayer(nextPlayer);
	}
	
	/**
	 * Returns the other player.
	 * 
	 * @param player that is the current player.
	 * @return the other player.
	 */
	private String otherPlayer(String player) {
		return player.equals(playerOne) ? playerTwo : playerOne;
	}
	
	/**
	 * Plays a move by putting it on the board.
	 * 
	 * @param move integer that represents a position on the board.
	 */
	private void playMove(int X, int Y) {
		board[X][Y] = getPlayerNumber();
	}
	
	/**
	 * Checks if the playing board is full.
	 * 
	 * @return boolean if the board is full.
	 */
	private boolean boardIsFull() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
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
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j <8; j++) {
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
		if(boardIsFull()){
			int player1 = 0;
			int player2 = 0;
			for(int row = 0; row < 8; row++){
				for(int column = 0; column < 8; column++){
					if(board[row][column] == PLAYER1)
						player1++;
					else
						player2++;
				}
			}
			
			return player1 > player2 ? PLAYER1 : PLAYER2;
		}else{
			return UNCLEAR;
		}
	}
	
	/**
	 * Returns the playing board in the form of a String.
	 * 
	 * @return String representation of the board.
	 */
	public String boardToString() {
		String str = "";
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
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
	
	/**
	 * 
	 * @return current player integer
	 */
	private int getPlayerNumber() {
		if(getPlayerToMove() == playerOne) {
			return PLAYER1;
		}
		else {
			return PLAYER2;
		}
	}
	
	/**
	 * @return opponents player integer
	 */
	private int getOpponentNumber() {
		if(getPlayerToMove() != playerOne) {
			return PLAYER1;
		}
		else {
			return PLAYER2;
		}
	}
	
	
	/**
	 * return true move is legal
	 * 
	 * @param X row
	 * @param Y column
	 * @return true if move is legal
	 */
	public boolean moveIsLegal(int X, int Y, boolean update){
		boolean canMove = false;
		
		if(!isValid(X,Y)){
			return false;
		}

		if( board[X][Y] != EMPTY){
			return false;
		}

		for(int i=0; i < 8; i++){
			int xOffset = X + OFFSET_X[i];
			int yOffset = Y + OFFSET_Y[i];
			
			if(!isValid(xOffset, yOffset)) {
				break;
			}
			if(board[xOffset][yOffset] == getOpponentNumber()){
				if(offsetCheck(xOffset, yOffset, i)){
					if(update){
					updateTiles(xOffset, yOffset, i);
					}
					canMove = true;	
				}
			}
		}
		
		return canMove;
	}

	/**
	 * Recursive - checks if theres a currentplayer coin on the other side
	 * 
	 * @param X starting row
	 * @param Y starting column
	 * @param i direction to check
	 * @return true if theres a current player coin on the other side
	 */
	private boolean offsetCheck(int X, int Y, int i) {
		
		int xOffset = X + OFFSET_X[i];
		int yOffset = Y + OFFSET_Y[i];
		
		if(!isValid(xOffset, yOffset)) {
			return false;
		}
		if(board[xOffset][yOffset] == getPlayerNumber()){
			return true;
		}
		if(board[xOffset][yOffset] == EMPTY){
			return false;
		}
		return offsetCheck(xOffset, yOffset, i);
		
	/*	if(offsetCheck(xOffset, yOffset,i)){
			board[xOffset][yOffset] = getPlayerNumber();
			return true;
		}else{
			return false;
		}*/
	
	}
	
	/**
	 * When a move is legal, all the tiles in that row should be switched
	 * 
	 * @param X row
	 * @param Y column
	 * @param i direction
	 */
	private void updateTiles(int X, int Y, int i) {
		board[X][Y] = getPlayerNumber();
		
		int xOffset = X + OFFSET_X[i];
		int yOffset = Y + OFFSET_Y[i];
		
		if(!isValid(xOffset, yOffset)) {
			return;
		}
		if(board[xOffset][yOffset] == getPlayerNumber()){
			return;
		}
		
		if(board[xOffset][yOffset] == getOpponentNumber()){
			updateTiles(xOffset, yOffset, i);
		}
	}
	
	/**
	 * returns true if X and Y are within the board
	 * @param X row
	 * @param Y columns
	 * @return boolean
	 */
	private boolean isValid(int X, int Y){
		if(X >= 0 && X <= 7 && Y >= 0 && Y <= 7) {
			return true;
		}
		return false;
	}
	

}
