import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class GameView extends JPanel {

	private static final long serialVersionUID = 0L;
	
	private JTextArea textArea;
	private JScrollPane scrollPane;
	
	public GameView(String playerOne, String playerTwo) {
		super(new GridLayout(2, 1));
		setVisible(true);
		
		textArea = new JTextArea(10, 20);
		scrollPane = new JScrollPane(textArea);
		add(scrollPane);
		
		JPanel panel = new JPanel(new FlowLayout());
		panel.add(new JLabel("Player 1: " + playerOne));
		panel.add(new JLabel("Player 2: " + playerTwo));
		add(panel);
	}
	
	public void addText(String text) {
		textArea.append(text + "\n");
	}

}
