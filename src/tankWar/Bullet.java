package tankWar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import tankWar.Tank.directions;

public class Bullet {
	
	public static final int XSPEED = 10;
	public static final int YSPEED = 10;
	
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	
	private int x;
	private int y;
	directions dir = directions.R;
	
	TankWar tk;
	
	public Bullet (int x, int y, directions dir,TankWar tk){
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.tk = tk; //持有对方引用
	}
	
	public Bullet (int x, int y, directions dir){
		this.x = x;
		this.y = y;
		this.dir = dir;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public directions getDir() {
		return dir;
	}
	
	public void draw (Graphics g){
		Color c = g.getColor();
		g.setColor(Color.black);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		move();
		
	}
	
	public void move (){
		switch (dir)
		{
		case U :
			y -= YSPEED;
			break;
		case D :
			y += YSPEED;
			break;
		case L :
			x -= XSPEED;
			break;
		case R :
			x += XSPEED;
			break;
		case STOP :
			break;
		}
	}
	
	public Rectangle getrec()
	{
		return new Rectangle(x , y ,WIDTH, HEIGHT);
	}
	
	public void hitTank (Tank t)
	{
		if (this.getrec().intersects(t.getRec()) && t.isLive() == true)
		{
			t.setLive(false);
			tk.setLive(false);
			// 设置爆炸
			tk.getBoom().setGo(true);
			tk.getBoom().setX(x);
			tk.getBoom().setY(y);
			tk.getBoom().setStep();
		}
	}
	
	public void hitTanks (ArrayList<Tank> tanks){
		for (int i = 0; i < tanks.size(); i++)
		{
			hitTank(tanks.get(i));
			if (tanks.get(i).isLive() == false)
				tanks.remove(tanks.get(i));
		}
			
	}
}
