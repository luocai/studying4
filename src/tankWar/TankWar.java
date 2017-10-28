package tankWar;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;

import tankWar.Tank.directions;

public class TankWar extends Frame{
	
	public static final int ROWS = 800;
	public static final int COLS = 600;
	
	Image offImage = null;
	
	Tank tank = new Tank(50, 50, true, Color.red, this); 
	//Tank btank = new Tank(100, 100, false, Color.BLUE);
//	Bullet bullet = new Bullet(50, 50, directions.R);
	
	ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	ArrayList<Tank> tanks = new ArrayList<Tank>();
	
	private boolean live = true ;// 记录子弹的消亡，子弹死了就移除
	
	Boom boom = new Boom(false);
	
	
	public Tank getTank ()
	{
		return tank;
	}
	
	public Boom getBoom ()
	{
		return boom;
	}
	
	public void startFrame ()
	{
		
	}
	public TankWar (){
		setLocation(300, 100);
		setSize(ROWS, COLS);
		this.setBackground(Color.green);
		setTitle("tankWar");
		setResizable(false);
		setVisible(true);
		addWindowListener(new WindowAdapter() {
			public void windowClosing (WindowEvent e){
				System.exit(0);
			}
		});
		this.addKeyListener(new KeyListeners());
		
		Thread t = new Thread(new Mythread());
		Thread t1 = new Thread(new EnemyThread());
		t.start();
		t1.start();
	}
	
	public void paint (Graphics g) {
		
//		bullet.draw(g);    
		if (bullets.size() > 0)
		{
			for (int i = 0; i < bullets.size(); i++) 
			{
				Bullet b = bullets.get(i);
				isLive(b);
				b.hitTanks(tanks);
		//		System.out.print(this.live);
				if (live == false)
					bullets.remove(i);
				b.draw(g);
			}
		}
		if (boom.getGo() == true)
			boom.draw(g);
		if (tank.isLive() == true)
			tank.draw(g);
		for (int i = 0; i < tanks.size(); i++)
			tanks.get(i).draw(g);
		g.drawString("bullets count:" + bullets.size(), 10, 50);
		g.drawString("enemy counts:" + tanks.size(), 10, 60);
	} 

	public void update (Graphics g){
		if (offImage == null)
			offImage = createImage(ROWS, COLS);
		Graphics goffImage = offImage.getGraphics();
		Color c = goffImage.getColor();
		goffImage.setColor(Color.GREEN);
		goffImage.fillRect(0, 0, ROWS, COLS);
		goffImage.setColor(c);
		paint (goffImage);
		g.drawImage(offImage, 0, 0, null);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TankWar tc = new TankWar();
	}
	
	//
	public void isLive(Bullet b) {
		if (b.getX() < 0 || b.getX() > ROWS || b.getY() < 0 || b.getY() > COLS)
			live = false;
		else
			live = true;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
	

	private class Mythread implements Runnable {

		@Override
		public void run() {
			while (true)
			{
				
				repaint();
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private class EnemyThread implements Runnable {
		TankWar tankw = TankWar.this;
		Random rd = new Random();
		int a, b;
		public void run() {
			while (true)
			{
				//System.out.println("jinlaila");
				a = rd.nextInt(600) + 50;
				b = rd.nextInt(500) + 50;
				if (tankw.tanks.size() <= 5)
				{
					Tank temptank = new Tank(a, b, false, Color.blue,TankWar.this);
					tankw.tanks.add(temptank);
				}
				try {
					Thread.sleep(5000);
				}catch (InterruptedException e){
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
	
	
	private class KeyListeners implements KeyListener{

		@Override
		public void keyPressed(KeyEvent e) {
			tank.keyPressed(e);
			int key = e.getKeyCode();
			if (key == e.VK_SPACE)
			{
				Bullet bullet = new Bullet(tank.getX() + tank.WIDTH / 2 - Bullet.WIDTH / 2, tank.getY() + tank.HEIGHT / 2 - Bullet.HEIGHT / 2, tank.getPtDir(), TankWar.this);
				bullets.add(bullet);                                                                                                               //内部类中获得对外部类的访问
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			tank.keyReleased(e);
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
