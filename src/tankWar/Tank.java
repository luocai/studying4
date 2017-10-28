package tankWar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import tankWar.Tank.directions;

public class Tank {
	
	public static final int XSPEED = 5;
	public static final int YSPEED = 5;
	
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	
	private int x;
	private int y;
	
	enum directions { U, D, L, R , STOP};
	directions direction = directions.STOP;
	directions ptdir = directions.R;
	
	boolean bU = false, bD = false, bL = false, bR = false;
	
	private boolean good = true;
	private Color color = Color.red;
	private boolean live = true;
	
	directions dir[] = {directions.U, directions.D, directions.L, directions.R};
	
	ArrayList<Bullet> bullets = new ArrayList<>();
	
	TankWar tk ;
	
	Boom boom = new Boom(x, y);
	
	public Tank(int x, int y, boolean good, Color color, TankWar tk) {
		this.x = x;
		this.y = y;
		this.good = good;
		this.color = color;
		this.tk = tk;
		
		Thread t2 = new Thread(new enemyMove());
		t2.start();
		
		Thread t3 = new Thread(new enemyAttack());
		t3.start();
	}
	

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public directions getPtDir() {
		return ptdir;
	}
	
	public boolean isLive()
	{
		return live;
	}
	
	public void setLive (boolean live)
	{
		this.live = live;
	}

	public void draw (Graphics g){
		if (live != true)
			return ;
		Color c = g.getColor();
		g.setColor(color);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		move();
		
		for (int i = 0; i < bullets.size(); i++)
		{
			Bullet temp = bullets.get(i);
			temp.draw(g);
		//	temp.hitTank(tk.getTank());
			if (tk.getTank().isLive() == false)
			{
				System.exit(0);
			}
		}
		
		switch (ptdir)
		{
		case U :
			g.drawLine(x + WIDTH / 2, y + HEIGHT / 2, x + WIDTH / 2, y);
			break;
		case D :
			g.drawLine(x + WIDTH / 2, y + HEIGHT / 2, x + WIDTH / 2, y + HEIGHT);
			break;
		case L :
			g.drawLine(x + WIDTH / 2, y + HEIGHT / 2, x, y + HEIGHT / 2);
			break;
		case R :
			g.drawLine(x + WIDTH / 2, y + HEIGHT / 2, x + WIDTH, y + HEIGHT / 2);
			break;
		}
	}
	
	public void move (){
		switch (direction)
		{
		case U :
			if (y >= 25)
				y -= YSPEED;
			break;
		case D :
			if (y < TankWar.COLS-25) // 这里有疑问，为什么cols不可以呢
				y += YSPEED;
			break;
		case L :
			if (x >= 0)
				x -= XSPEED;
			break;
		case R :
			if (x <= TankWar.ROWS - 40) // 马克
				x += XSPEED;
			break;
		case STOP :
			break;
		}
	}
	
	public void location (){
		if (bU && !bD && !bL && !bR)
			ptdir = direction = directions.U;
		else if (!bU && bD && !bL && !bR)
			ptdir = direction = directions.D;
		else if (!bU && !bD && bL && !bR)
			ptdir = direction = directions.L;
		else if (!bU && !bD && !bL && bR)
			ptdir = direction = directions.R;
		else if (!bU && !bD && !bL && !bR)
			direction = directions.STOP;
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key)
		{
		case KeyEvent.VK_UP :
		//	if (y >= 0)
				bU = true;
			break;
		case KeyEvent.VK_DOWN :
		//	if (y <= TankWar.COLS)
		    	bD = true;
			break;
		case KeyEvent.VK_RIGHT:
		//	if (x < TankWar.ROWS)
				bR = true;
			break;
		case KeyEvent.VK_LEFT:
		//	if (x > 0)
			bL = true;
			break;
		case KeyEvent.VK_ALT :
			superAttack();
		}
		location();
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key)
		{
		case KeyEvent.VK_UP :
			bU = false;
			break;
		case KeyEvent.VK_DOWN :
			bD = false;
			break;
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;
		case KeyEvent.VK_LEFT:
			bL = false;
			break;
		}
		location ();
	}
	
	
	public Rectangle getRec ()
	{
		return new Rectangle (x, y, WIDTH, HEIGHT);
	}
	
	private class enemyMove implements Runnable {

		Random r = new Random();
		directions store  ; // 记录方向改变前的方向，防止坦克一直往一个方向走
		public void run() {
			// TODO Auto-generated method stub
			while (true)
			{
				if (Tank.this.good == false)
				{
					store = direction;
					directions d = dir[r.nextInt(4)];
					while (d == store)
						d = dir[r.nextInt(4)]; // 每次改变方向
					direction = d;
					ptdir = direction;
					try {
						Thread.sleep(666);
					}catch (InterruptedException e){
						e.printStackTrace();
					}
				}
				else
					break;
			}
		}
		
	}
	
	public void superAttack ()
	{
		for (int i = 0; i < 4; i++)
		{
			Bullet b = new Bullet(x+ WIDTH / 2 - Bullet.WIDTH / 2, y + HEIGHT / 2 - Bullet.HEIGHT / 2, directions.values()[i], tk );
			tk.bullets.add(b);                                                                       //注意tk要传进去
		}
	}
	
	private class enemyAttack implements Runnable {
		Random r = new Random();
		Tank temp = Tank.this;
		public void run() {
			// TODO Auto-generated method stub
			while (true)
			{
				if (temp.good == false)
				{
					Bullet bullet = new Bullet(temp.x + WIDTH / 2 - Bullet.WIDTH / 2, temp.y + HEIGHT / 2 - Bullet.HEIGHT / 2, temp.ptdir);
					temp.bullets.add(bullet);
				}
				try {
					Thread.sleep(r.nextInt(500) + 150);
				}catch (InterruptedException e){
					e.printStackTrace();
				}
			}
		}
		
	} 
//	public boolean outOfLine ()
//	{
//		if (x < 0 || x > TankWar.ROWS || y < 0 || y > TankWar.COLS)
//		{
//			
//		}
//	}
//	
}
