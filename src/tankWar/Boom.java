package tankWar;

import java.awt.Color;
import java.awt.Graphics;

public class Boom {
	private int x, y;
	int[] diameter = {3, 10, 23, 35, 50, 35,23,10, 3}; 
	
	private int step = 8;
	
	boolean go = false;
	
	public Boom (int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Boom (boolean go)
	{
		this.go = go;
	}
	
	public void setStep ()
	{
		step = 8;
	}
	public void setGo (boolean go)
	{
		this.go = go;
	}
	
	public boolean getGo ()
	{
		return go;
	}
	
	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void draw (Graphics g)
	{
		if (step < 0 )
		{
			go = false; // Ò»´Î±¬Õ¨½áÊø
			return ;
		}
		Color c = g.getColor();
		g.setColor(Color.yellow);
		g.fillOval( x, y, diameter[step], diameter[step]);
		g.setColor(c);
		step--;
		//System.out.println(step);
	}
}
