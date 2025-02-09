
import java.awt.event.KeyEvent;
import java.util.Vector;

public class CPlayer {
	private int direction; //0 right, 1 left, 2 up, 3 down
	public int getDirection() { return direction; }
	public void setDirection(int input) { direction = input; }
	
	private int dirLock;
	public int getDirLock() { return dirLock; }
	public void setDirLock(int input) { dirLock = input; }
	
	private int headX, headY;
	public void setHeadX(int X) { headX = X; }
	public void setHeadY(int Y) { headY = Y; }
	public int getHeadX() { return headX; }
	public int getHeadY() { return headY; }
	
	public Vector<Integer> vecTailX;
	public Vector<Integer> vecTailY;
	
	CPlayer() {		
		vecTailX = new Vector<Integer>();
		vecTailY = new Vector<Integer>();

	}

	public void moveSnake(KeyEvent keyEvt) {
		if(keyEvt.getKeyChar() == 'w' && dirLock != 3)
			direction = 2;
		else if(keyEvt.getKeyChar() == 'a' && dirLock != 0)
			direction = 1;
		else if(keyEvt.getKeyChar() == 's' && dirLock != 2)
			direction = 3;
		else if(keyEvt.getKeyChar() == 'd' && dirLock != 1)
			direction = 0;
	}

}
