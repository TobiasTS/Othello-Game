import static org.junit.Assert.*;

import org.junit.Test;

public class OthelloGameTest {
	
	@Test
	public void testStart(){
		OthelloGame game = new OthelloGame("Piet","Klaas");
		game.start();
		
		assertEquals(OthelloGame, game.positionValue([3][3]));
	}
	
	@Test
	public void testUpdateTiles(){
		OthelloGame game = new OthelloGame("Piet","Klaas");
		game.start();
		
		System.out.println(game.boardToString());
		
		game.doPlayerMove(game.getPlayerToMove(),"4,2");
		System.out.println(game.boardToString());
		//assertEquals();
		//TODO: test afmaken methode updateTiles
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
