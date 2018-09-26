
package view;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

import model.Model;

public class Display extends JFrame{
	private int rowNum;
	private int colNum;
	private MyPanel panel;
	private Model innerModel;
	
	Display(Model m){
		super("GA");
		
		innerModel = m;
		rowNum = m.getRowNum();
		colNum = m.getColNum();
		setSize(colNum*20+40,rowNum*20+60);
		panel = new MyPanel(m);
		add(panel);
		
		this.setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void runModel(int steps, int mili) {
		innerModel.initGrid();
		int score = 0;
		int intent = -1;
		for(int j=0; j<steps; ++j){
			intent = innerModel.getIntent(innerModel.getState());
			score += innerModel.checkAndMove(intent);
			panel.repaint();
			System.out.println("Score: "+score);
			try {
				Thread.sleep(mili);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
	
	public static void main(String[] args) {
		Model m = new Model(10,10,0.5);
		String aGoodStrategy = 
			"022123536350061526531401432405205643253305204241542462310255024261115352610442521530561250612663003656066366045656646466322622066566666546646666446222656666366666352060205242565352222522222065321161122231245541341341654313111125552522044343345";
		m.setStrategy(aGoodStrategy);
		Display display = new Display(m);
		display.runModel(1000, 100);
	}
	
}
class MyPanel extends JPanel{
	private Model innerModel;
	private Grid grid;
	private Robot robot;

	public MyPanel(Model m) {
		innerModel = m;
		grid = new Grid(m.getRowNum(),m.getColNum());
		robot = new Robot(m.getX(),m.getY());
		setBackground(Color.WHITE);
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g); // 清除上一幀畫面
		grid.draw(g);
		robot.moveTo(innerModel.getX(), innerModel.getY()).draw(g);
		for(int i=0; i<innerModel.getRowNum(); ++i) {
			for(int j=0; j<innerModel.getColNum(); ++j) {
				if(innerModel.getFruit(i, j)) {
					new Fruit(i,j).draw(g);
				}
			}
		}
	}
}


