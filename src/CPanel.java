import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Vector;

import javax.swing.*;

public class CPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	public final int PANEL_WIDTH = 1200; //680 = 17 * 40
	public final int PANEL_HEIGHT = 680;
	public final int FIELD_WIDTH = 30; //17
	public final int FIELD_HEIGHT = 17;
	public int[][] arrField = new int[FIELD_WIDTH][FIELD_HEIGHT];
	public Boolean lost;
	private Random random;
	public int score; 
	private String scoreText = "";
	public Timer timer;
	public Boolean timerStart;
	public Boolean foodSet;
	public int foodX = 0;
	public int foodY = 0;
		
	CPlayer Player;
	
	CPanel () {
		this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		timer = new Timer(100, this);
		timerStart = false;
		
		random = new Random();

		Player = new CPlayer();
				
		init();
	}
	
	public void init() {
		for (int i = 0; i < FIELD_WIDTH; i++) 
			for (int j = 0; j < FIELD_HEIGHT; j++) {
				arrField[i][j] = 0;
			}
		
		Player.vecTailX.clear();
		Player.vecTailY.clear();
		
		Player.setHeadX(3);
		Player.setHeadY(9);
		
		Player.setDirection(0);
		Player.setDirLock(0);
		lost = false;
		score = 0;
		
		Player.vecTailX.add(Player.getHeadX());
		Player.vecTailX.add(Player.getHeadX()-1);
		Player.vecTailX.add(Player.getHeadX()-2);
		Player.vecTailY.add(Player.getHeadY());
		Player.vecTailY.add(Player.getHeadY());
		Player.vecTailY.add(Player.getHeadY());

		arrField[Player.getHeadX()-2][Player.getHeadY()] = 2;
		arrField[Player.getHeadX()-1][Player.getHeadY()] = 2;
		arrField[Player.getHeadX()][Player.getHeadY()] = 1;
		
		setFood();
	}
	
	public void paint(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;
		
		if(!lost) {
			g2D.setColor(Color.black);
			g2D.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
			int d = 6;
			int xd = 0, yd = 0, wd = 0, hd = 0;
			
			for (int i = 0; i < Player.vecTailX.size(); i++) {
				if (i == 0) 
					g2D.setColor(Color.green);
				else if (i > 0) 
					g2D.setColor(Color.blue);

				if(i != 0) {
					xd = 0; yd = 0; wd = 0; hd = 0;
					
					if(Player.vecTailX.get(i) < Player.vecTailX.get(i-1)
							|| Player.vecTailY.get(i) < Player.vecTailY.get(i-1)) { //previous: direction == 1 || direction == 2
						if(Player.vecTailX.get(i) == Player.vecTailX.get(i-1)) {
							yd = 20;
						}
						else if (Player.vecTailY.get(i) == Player.vecTailY.get(i-1)) {
							xd = 20; 
						}
					}
					else {
						if (Player.vecTailX.get(i) == Player.vecTailX.get(i-1)
								&& Player.vecTailY.get(i) == Player.vecTailY.get(i-1)) {
							
							g2D.fillRect(Player.vecTailX.get(i)*PANEL_WIDTH/FIELD_WIDTH+d, Player.vecTailY.get(i)*PANEL_HEIGHT/FIELD_HEIGHT+d, 
									PANEL_WIDTH/FIELD_WIDTH-2*d, PANEL_HEIGHT/FIELD_HEIGHT-2*d);
						}
						else if(Player.vecTailX.get(i) == Player.vecTailX.get(i-1)) {
							hd = 20;
							yd = 20; 
						}
						else if (Player.vecTailY.get(i) == Player.vecTailY.get(i-1)) {
							wd = 20;  
							xd = 20;
						}
					}

					//Draw the body
					g2D.fillRect(Player.vecTailX.get(i)*PANEL_WIDTH/FIELD_WIDTH+d-wd, Player.vecTailY.get(i)*PANEL_HEIGHT/FIELD_HEIGHT+d-hd, 
							PANEL_WIDTH/FIELD_WIDTH-2*d+xd, PANEL_HEIGHT/FIELD_HEIGHT-2*d+yd);
				}
				else {
					//Draw the head
					g2D.fillRect(Player.vecTailX.get(i)*PANEL_WIDTH/FIELD_WIDTH+d, Player.vecTailY.get(i)*PANEL_HEIGHT/FIELD_HEIGHT+d, 
							PANEL_WIDTH/FIELD_WIDTH-2*d, PANEL_HEIGHT/FIELD_HEIGHT-2*d);
				}
			}
			
			//Draw the food on the field
			d = 8;
			g2D.setColor(Color.red);
			g2D.fillRect(foodX*PANEL_WIDTH/FIELD_WIDTH+d, foodY*PANEL_HEIGHT/FIELD_HEIGHT+d, 
					PANEL_WIDTH/FIELD_WIDTH-2*d, PANEL_HEIGHT/FIELD_HEIGHT-2*d);
		}
		else { //Draw the You lose! message, when the game is over
			String loseText = "You lose!";
			
			Font font = g2D.getFont().deriveFont(50.0f);
			g2D.setFont(font);
			FontMetrics metrics = g2D.getFontMetrics(font);

			g2D.setColor(new Color(255,255,255,255));
			
			int fontX = (PANEL_WIDTH/2)-metrics.stringWidth(loseText)/2;
			int fontY = ((PANEL_HEIGHT/2)-metrics.getHeight()/2)+metrics.getAscent(); //17
			
			g2D.drawString(loseText, fontX, fontY);
		}
		
		//Draw the scoreboard in the down right corner
		Font font = g2D.getFont().deriveFont(50.0f);
		g2D.setFont(font);
		FontMetrics metrics = g2D.getFontMetrics(font);
		
		scoreText = Integer.toString(score);

		g2D.setColor(new Color(255,255,255,150));
		
		int fontX = (PANEL_WIDTH-100/2)-metrics.stringWidth(scoreText)/2;
		int fontY = ((PANEL_HEIGHT-100/2)-metrics.getHeight()/2)+metrics.getAscent(); //17
		
		g2D.drawString(scoreText, fontX, fontY);
	}
	
	public void setFood() {
		//Set the food on a random field, but not the snake
		Boolean loop = true;
		
		do {
			foodX = random.nextInt(FIELD_WIDTH);
			foodY = random.nextInt(FIELD_HEIGHT);
			
			if(arrField[foodX][foodY] == 0) { 
				loop = false;
				arrField[foodX][foodY] = 3;
			}
			
			foodSet = false;
		} while(loop);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == timer) {
			try {
				//Lock the direction that the snake has right now
				Player.setDirLock(Player.getDirection()); 
				
				arrField[Player.vecTailX.lastElement()][Player.vecTailY.lastElement()] = 0;

				//Drag the position of the body segments
				for (int i = Player.vecTailX.size()-1; i > 0 ; i--) {
					Player.vecTailX.set(i, Player.vecTailX.get(i-1));
					Player.vecTailY.set(i, Player.vecTailY.get(i-1));
					arrField[Player.vecTailX.get(i)][Player.vecTailY.get(i)] = 2;
				}
				
				//Choose the movement
				if(Player.getDirection() == 0) {
					Player.setHeadX(Player.getHeadX()+1);
				}
				else if(Player.getDirection() == 1) {
					Player.setHeadX(Player.getHeadX()-1);
				}
				else if(Player.getDirection() == 2) {
					Player.setHeadY(Player.getHeadY()-1);
				}
				else if(Player.getDirection() == 3) {
					Player.setHeadY(Player.getHeadY()+1);
				}

				

				//Check if the head is on the food
				if(arrField[Player.getHeadX()][Player.getHeadY()] == 3) { 
					int x = 0; //Displacement of the new element
					int y = 0;
					
					//Check if the last and second last value are the same
					if(Player.vecTailX.lastElement() == Player.vecTailX.lastElement()-1) {
						x = Player.vecTailX.lastElement();
						y = 2 * Player.vecTailY.lastElement() - Player.vecTailY.lastElement()-1;
					}
					else if(Player.vecTailY.lastElement() == Player.vecTailY.lastElement()-1) {
						y = Player.vecTailY.lastElement();
						x = 2 * Player.vecTailX.lastElement() - Player.vecTailX.lastElement()-1;
					}
					
					Player.vecTailX.add(Player.vecTailX.lastElement()+x);
					Player.vecTailY.add(Player.vecTailY.lastElement()+y);
					arrField[Player.vecTailX.lastElement()][Player.vecTailY.lastElement()] = 2;
					
					score++;

					foodSet = true;
					setFood();
				}
				
				//Set the position of the head
				Player.vecTailX.set(0, Player.getHeadX());
				Player.vecTailY.set(0, Player.getHeadY());
				
				//Check if the head is on the tail
				if(arrField[Player.vecTailX.firstElement()][Player.vecTailY.firstElement()] == 2) {
					lose();
				}
				
				arrField[Player.vecTailX.get(0)][Player.vecTailY.get(0)] = 1;
								
				repaint();
			}
			catch (Exception ex) {
				lose();
			}
		}
	}
	
	public void lose() {
		timer.stop();
		timerStart = false;
		lost = true;
		repaint();
	}
}
