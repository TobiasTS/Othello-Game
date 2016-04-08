
public class OthelloGameUI {
	
	
	public OthelloGameUI(){
		OthelloGame game = new OthelloGame("piet", "klaas");
		game.start();
		System.out.print(game.boardToString());
			
		for(int i = 0; i < 61; i++){
			if(game.positionValue(game.board) == 2){
				double[] aiMove = game.doAIMove(game.getCurrentPlayer(),5,game.copyBoard(game.board));
				game.doPlayerMove(game.getCurrentPlayer(), ""+(int) aiMove[0]+","+(int) aiMove[1]);;
				System.out.println(game.boardToString());
			}
		}
	}
	
	public static void main(String[] args){
		new OthelloGameUI();
	}
	
}
