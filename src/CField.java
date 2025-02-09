import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class CField extends JFrame implements KeyListener{
	private static final long serialVersionUID = 1L;
	CPanel Panel;
	//0 empty, 1 head, 2 tail, 3 food
	
	CField() {
		Panel = new CPanel();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(Panel);
		this.pack();
		this.setLocationRelativeTo(this);
		this.setVisible(true);

		this.addKeyListener(this);
	}
	
	public static void main(String[] args) {
		new CField();
	}
	

	@Override
	public void keyPressed(KeyEvent keyEvt) {
		
		if(!Panel.timerStart && keyEvt.getKeyChar() != 'a' && !Panel.lost) {
			Panel.timer.start();
			Panel.timerStart = true;
		}

		if(!Panel.timerStart && keyEvt.getKeyChar() == 'r') {
			Panel.init();
			repaint();
		}
		
		if (keyEvt.getKeyChar() == 't') {
			Panel.timer.stop();
			Panel.timerStart = false;
		}
		else if (keyEvt.getKeyChar() == 'z') {
			if(!Panel.timerStart)
				Panel.timer.restart();
		}
		
		Panel.Player.moveSnake(keyEvt);
		
	}

	@Override
	public void keyReleased(KeyEvent keyEvt) {
	}

	@Override
	public void keyTyped(KeyEvent keyEvt) {
	}
}
