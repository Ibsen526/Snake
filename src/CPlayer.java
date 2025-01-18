
import java.awt.event.KeyEvent;
import java.util.Vector;

public class CPlayer {


	private int direction; //0 rechts, 1 links, 2 oben, 3 unten
	public int getDirection() { return direction; }
	public void setDirection(int input) { direction = input; }
	private int dirLock;
	public int getDirLock() { return dirLock; }
	public void setDirLock(int input) { dirLock = input; }
	private int headX, headY;
	//public void setHeadPos(int X, int Y) { headX = X; headY = Y; }
	public void setHeadX(int X) { headX = X; }
	public void setHeadY(int Y) { headY = Y; }
	public int getHeadX() { return headX; }
	public int getHeadY() { return headY; }
	public Vector<Integer> vecTailX, vecTailY;
	
	CPlayer() {

	}

	public void moveSnake(KeyEvent keyEvt) { //, CPanel Panel
		if(keyEvt.getKeyChar() == 'w' && dirLock != 3) //Panel.getDirLock()
			direction = 2; //Panel.setDirection(2)
		else if(keyEvt.getKeyChar() == 'a' && dirLock != 0)
			direction = 1;
		else if(keyEvt.getKeyChar() == 's' && dirLock != 2)
			direction = 3;
		else if(keyEvt.getKeyChar() == 'd' && dirLock != 1)
			direction = 0;
	}

}
