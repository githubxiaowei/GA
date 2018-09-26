package view;

import java.awt.Color;
import java.awt.Graphics;

interface Drawable{
	public void draw(Graphics g);
}
class Grid implements Drawable{
	private int rowNum,colNum;
	public Grid(int r, int c) {
		rowNum = r;
		colNum = c;
	}
	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.BLACK);
		
		for(int i=0; i<=rowNum; ++i) {
			g.drawLine(10, 20*i+10, 20*colNum+10, 20*i+10);
		}
		for(int i=0; i<=colNum; ++i) {
			g.drawLine(20*i+10, 10, 20*i+10, 20*rowNum+10);
		}
		g.setColor(c);
	}
}
class Robot implements Drawable{
	private int x,y;
	public Robot(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.BLACK);
		g.drawOval((y+1)*20-5,(x+1)*20-5,10,10);
		g.setColor(c);
	}
	public Robot moveTo(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}
}
class Fruit implements Drawable{
	private int x,y;
	public Fruit(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.RED);
		g.fillOval((y+1)*20-5,(x+1)*20-5,10,10);
		g.setColor(c);
	}
}