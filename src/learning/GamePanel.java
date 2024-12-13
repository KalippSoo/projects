package learning;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

import learning.location.Location;

public class GamePanel extends JPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;
		
	static final int SCREEN_WIDTH = 800;
	static final int SCREEN_HEIGHT = 800;
	public static final int UNIT_SIZE = 40;
	static final int GAME_UNIT = (SCREEN_WIDTH * SCREEN_WIDTH) / UNIT_SIZE;
	static int DELAY = 100;
	final int x[] = new int[GAME_UNIT];
	final int y[] = new int[GAME_UNIT];
	
	Location apple;
	Location snake;
	
	int maxBodyParts = 15;
	int bodyParts = maxBodyParts;
	int appleEaten;
	int appleX;
	int appleY;
	
	int xTable = 0;
	int yTable = 0;
	
	char direction = 'R';
	boolean running = false;
	boolean ai = false;
	Timer timer;
	Random random;
	int time = 0, maxTime = 1;
	
	//containers
	JButton btn;
	
	JButton play;
	JButton exit;
	
	public GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(new Color(5, 5, 0));
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		this.setLayout(null);
		
		startGame();
		
	}
	
	public void startGame() {
		
		snake = new Location(x[0], y[0]);
		apple = new Location(appleX, appleY);
		
		newApple();
		
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
		
	    btn = new JButton("Replay");
	    btn.addActionListener(this);
	    btn.setFocusable(false);
	    btn.setBounds(SCREEN_WIDTH/2, SCREEN_HEIGHT/2, 200, 150);
	    btn.setVisible(false);
	    btn.setLocation(SCREEN_WIDTH/2, SCREEN_HEIGHT/2 + SCREEN_HEIGHT/6);
	    this.add(btn);
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (running){

			draw(g);
			if (time > 0) {
				time--;
			}
			snake.set(x[0], y[0]);
			
			System.out.println("x:" + snake.getX() + " y:" + snake.getY());
			System.out.println(snake.Distance(apple));
		}else {
			gameOver(g);
		}
	}
	
	public void draw(Graphics g) {
		
		if (running) {
			
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
			for (int i = 0; i < bodyParts; i++) {
				g.setColor(new Color(5, (200 + -(i*5)), (200 + (-i*5))));
				g.fillOval(x[i], y[i], (UNIT_SIZE), (UNIT_SIZE));
			}
			
			g.setColor(Color.white);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = g.getFontMetrics(g.getFont());
			g.drawString("Score: " + appleEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + appleEaten))/2, g.getFont().getSize());
			

		}
		else {
			gameOver(g);
		}
		
	}
	
	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE;
		
		apple.set(appleX, appleY);
		
		for (int i = bodyParts; i>0; i--) {
			
			if ((x[i] == appleX && y[i] == appleY)) {
				//apple spawn at body parts !
				newApple();
				break;
			}
			
		}
		
	}
	
	public void move() {
		
		for (int i = bodyParts; i>0; i--) {
			x[i] = x[i -1];
			y[i] = y[i -1];
		}
		
		switch (direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':

			x[0] = x[0] + UNIT_SIZE;
			break;
		default:
			break;
		}
		
	}
	
	public void checkApple() {
		
		if ((x[0] == appleX) && y[0] == appleY) {
			bodyParts++;
			appleEaten++;
			newApple();
		}
		
	}
	
	public void checkCollision() {
		//this check if the head collided with body
		for (int i = (bodyParts - 1); i>0; i--) {
			
			if ((x[0] == x[i]) && y[0] == y[i]) {
				running = false;
			}
			
		}
		//check if the head touched border
		//left
		if (x[0] < 0) {
			running = false;
		}
		if (x[0] > (SCREEN_WIDTH - UNIT_SIZE)) {
			running = false;
		}
		if (y[0] < 0) {
			running = false;
		}
		if (y[0] > (SCREEN_HEIGHT - UNIT_SIZE)) {
			running = false;
		}
		
		if (!running)
			timer.stop();
	}
	
	public void restartGame() {
		
		this.timer.start();
		this.bodyParts = maxBodyParts;
		for (int x = bodyParts; x >= 0; x--) {
			this.x[x] = 0;
			this.y[x] = 0;
		}
		this.newApple();
		this.appleEaten = 0;
		this.direction = 'R';
		this.running = true;
		this.btn.setVisible(false);

	}
	
	public void gameOver(Graphics g) {
		//GameOver Text
		
		g.setColor(Color.white);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics1 = g.getFontMetrics(g.getFont());
		g.drawString("Score: " + appleEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + appleEaten))/2, g.getFont().getSize());
		
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
		
	    //Créer le bouton
		btn.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {
			move();
			checkApple();
			checkCollision();
			
			
		}else {
			
			if (e.getSource() == btn) {
				restartGame();
			}
		}
		repaint();
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		
		@Override
		public void keyPressed(KeyEvent e) {
			
			if (time == 0) {
				switch(e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					
					directionMethod('R', 'L');
					break;
				case KeyEvent.VK_RIGHT:
					
					directionMethod('L', 'R');
					break;
				case KeyEvent.VK_UP:
					
					directionMethod('D', 'U');
					break;
				case KeyEvent.VK_DOWN:
					
					directionMethod('U', 'D');
					break;
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				
				Point point = MouseInfo.getPointerInfo().getLocation();
				
				xTable = (int) (point.getX());	
				yTable = (int) (point.getY());
				
				
				System.out.println(point.getX());
				System.out.println(point.getY());
			}
		}
		
	}
	void directionMethod(char a, char b) {
		if (direction != a) {
			direction = b;
		}
		time = maxTime;
	}

}


















