import static org.junit.Assert.*;

import org.junit.Test;

public class OthelloGameTest {
	
	@Test
	public void testStart(){
		OthelloGame game = new OthelloGame("Piet","Klaas");
		game.start();
		
		assertEquals(game.board[0][0], OthelloGame.EMPTY);
		
		assertEquals(game.board[3][3], OthelloGame.PLAYER1);
		assertEquals(game.board[3][4], OthelloGame.PLAYER2);
		assertEquals(game.board[4][4], OthelloGame.PLAYER1);
		assertEquals(game.board[4][3], OthelloGame.PLAYER2);
	}
	
	@Test
	public void testUpdateTiles(){
		OthelloGame game = new OthelloGame("Piet","Klaas");
		game.start();
		//System.out.println(game.getPlayerToMove());
		//System.out.println(game.getMoveDetails());
		///System.out.println(game.boardToString());
		
		game.doPlayerMove(game.getPlayerToMove(),"4,5");
		assertEquals(game.board[4][3], OthelloGame.PLAYER2);
		assertEquals(game.board[4][4], OthelloGame.PLAYER2);
		assertEquals(game.board[4][5], OthelloGame.PLAYER2);
		
		assertEquals(game.board[3][4], OthelloGame.PLAYER2);
		
		assertEquals(game.board[3][3], OthelloGame.PLAYER1);

/*		System.out.println("--------------------------");
		System.out.println(game.getPlayerToMove());
		System.out.println(game.getMoveDetails());
		System.out.println(game.boardToString());*/
	}
	

	/*@Test
	public void test() {
		OthelloGame game = new OthelloGame("BBB","WWW");
		game.start();
		
		System.out.println(game.boardToString());
		
		//BBB
		game.doPlayerMove(game.getPlayerToMove(),"4,2");
		System.out.println(game.getMoveDetails());
		System.out.println(game.boardToString());
		
		
		//WWW
		game.doPlayerMove(game.getPlayerToMove(),"3,2");
		System.out.println(game.getMoveDetails());
		System.out.println(game.boardToString());
		
		//BBB
		game.doPlayerMove(game.getPlayerToMove(),"2,2");
		System.out.println(game.getMoveDetails());
		System.out.println(game.boardToString());
		
		//WWW
		game.doPlayerMove(game.getPlayerToMove(),"3,1");
		System.out.println(game.getMoveDetails());
		System.out.println(game.boardToString());
		
		//BBB
		game.doPlayerMove(game.getPlayerToMove(),"2,1");
		System.out.println(game.getMoveDetails());
		System.out.println(game.boardToString());
		
		//WWW
		game.doPlayerMove(game.getPlayerToMove(),"5,1");
		System.out.println(game.getMoveDetails());
		System.out.println(game.boardToString());
		
		//BBB
		game.doPlayerMove(game.getPlayerToMove(),"3,0");
		System.out.println(game.getMoveDetails());
		System.out.println(game.boardToString());
		
		//WWW
		game.doPlayerMove(game.getPlayerToMove(),"2,0");
		System.out.println(game.getMoveDetails());
		System.out.println(game.boardToString());
		
		//BBB
		game.doPlayerMove(game.getPlayerToMove(),"3,5");
		System.out.println(game.getMoveDetails());
		System.out.println(game.boardToString());
		
		//WWW
		game.doPlayerMove(game.getPlayerToMove(),"3,6");
		System.out.println(game.getMoveDetails());
		System.out.println(game.boardToString());
		
		//BBB
		game.doPlayerMove(game.getPlayerToMove(),"4,1");
		System.out.println(game.getMoveDetails());
		System.out.println(game.boardToString());
		
		//WWW
		game.doPlayerMove(game.getPlayerToMove(),"5,4");
		System.out.println(game.getMoveDetails());
		System.out.println(game.boardToString());
		
		//BBB
		game.doPlayerMove(game.getPlayerToMove(),"3,7");
		System.out.println(game.getMoveDetails());
		System.out.println(game.boardToString());
		
		//WWW
		game.doPlayerMove(game.getPlayerToMove(),"1,0");
		System.out.println(game.getMoveDetails());
		System.out.println(game.boardToString());
		
		//BBB
		game.doPlayerMove(game.getPlayerToMove(),"2.3");
		System.out.println(game.getMoveDetails());
		System.out.println(game.boardToString());
		
		//WWW
		game.doPlayerMove(game.getPlayerToMove(),"4,0");
		System.out.println(game.getMoveDetails());
		System.out.println(game.boardToString());
		
	}*/

}
