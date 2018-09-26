package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Model {
	private int rowNum,colNum;
	private int currX,currY;
	private boolean fruits[][];
	private double percent;
	private String strategy;
	public static final int STRATEGY_LEN = 243;
	public static final int ACTION_NUM = 7;
	public static final int DIRECTION[][] = {{0,0},{0,1},{0,-1},{1,0},{-1,0}};
	public Model(int r, int c, double p) {
		rowNum = r;
		colNum = c;
		percent = p;
		initGrid();
		strategy = genStrategy();
	}
	public void initGrid() {
		currX = 0;
		currY = 0;
		int totalNum = rowNum*colNum;
		int fruitNum = (int)(totalNum*percent);
		fruits = new boolean[rowNum][colNum];
		Random rand = new Random();
		int temp,row,col;
		for(int i=0; i<fruitNum; ++i){
			do{
				temp = rand.nextInt(totalNum);
				row = temp/colNum;
				col = temp%colNum;
			}while(fruits[row][col]);
			fruits[row][col] = true;
		}
	}
	
	public int getX() {
		return currX;
	}
	public int getY() {
		return currY;
	}
	public int getRowNum() {
		return rowNum;
	}
	public int getColNum() {
		return colNum;
	}
	public boolean getFruit(int i, int j) {
		return fruits[i][j];
	}
	public String getStrategy() {
		return strategy;
	}
	public void setStrategy(String s) {
		strategy = s;
	}
	
	public static String genStrategy() {
		StringBuilder sb = new StringBuilder();
		Random rand = new Random();
		for(int i=0; i<STRATEGY_LEN; ++i) {
			sb.append((char)(rand.nextInt(ACTION_NUM)+'0'));
		}
		return sb.toString();
	}
	
	public static String combineStrategy(String a,String b) {
		StringBuilder sb = new StringBuilder();
		Random rand = new Random();
		if(rand.nextInt(10)<1) {  //隨機劈腿
			b = genStrategy();
		}
		for(int i=0; i<STRATEGY_LEN; ++i) {
			if(rand.nextInt(20)<1) { //雜交出錯
				sb.append((char)(rand.nextInt(ACTION_NUM)+'0'));
			}else {
				sb.append(rand.nextBoolean()?a.charAt(i):b.charAt(i));
			}
		}
		return sb.toString();
	}
	
	public int getIntent(int state) {
		return strategy.charAt(state)-'0';
	}
	
	public int getState(){
		int tempX,tempY;
		int state,wholeState=0;
		for(int i=0; i<5;++i) {
			tempX = currX + DIRECTION[i][0];
			tempY = currY + DIRECTION[i][1];
			if(tempX<0 || tempY<0 || tempX>=rowNum || tempY>=colNum) {
				state = 0;
			}else {
				if(fruits[tempX][tempY]) {
					state = 1;
				}else {
					state = 2;
				}
			}
			wholeState = 3*wholeState+state;
		}
		return wholeState;
	}
	public int checkAndMove(int intent) {
		int score = 0;
		int tempX,tempY;
		Random rand = new Random();
		if(intent == 5) { //自由移动
			intent = rand.nextInt(4)+1; // 5==>{1,2,3,4}
		}
		if(intent == 6) { //吃水果
			if(fruits[currX][currY]) { 
				score = 10;  
				fruits[currX][currY] = false;
			}else {       // 没有水果
				score = -1;
			}
		}else{
			tempX = currX + DIRECTION[intent][0];
			tempY = currY + DIRECTION[intent][1];
			if(tempX<0 || tempY<0 || tempX>=rowNum || tempY>=colNum) {
				score = -5;
			}else {
				currX = tempX;
				currY = tempY;
			}
		}
		return score;
	}
	public double averageScore(int steps, int times) {
		double average = 0.0;
		for(int i=0; i<times; ++i) {
			initGrid();
			int score = 0;
			int intent = -1;
			for(int j=0; j<steps; ++j){
				intent = getIntent(getState());
				score += checkAndMove(intent);
			}
			average += score;
		}
		return average/times;
	}
	
	public static void train(int generations) {
		int MODEL_NUM = 100;
		int GRID_ROW = 10;
		int GRID_COL = 10;
		double FRUIT_PER = 0.5;
		int MODEL_STEPS = 200;
		int MODEL_TIMES = 10;
		
		
		Map<String,Double> map= new HashMap<>();
		List<String> sortedStrategy = new ArrayList<>();
		List<String> newStrategy = new ArrayList<>();
		
		for(int i=0; i<MODEL_NUM; ++i) {
			Model m = new Model(GRID_ROW,GRID_COL,FRUIT_PER);
			map.put(m.getStrategy(), m.averageScore(MODEL_STEPS,MODEL_TIMES));
		}
		for(int g=1; g<=generations; ++g) {
			System.out.println("generation "+g);
			
			map = MapUtil.sortByValue(map);
			sortedStrategy.clear();
			
			int top10 = 10;
			for(String key: map.keySet()) {
				if(top10-->0) {
					System.out.println("Value = "+map.get(key)+", Key = "+key);
					newStrategy.add(key);
				}
				sortedStrategy.add(key);
			}
			
			outloop:for(int k=0; k<MODEL_NUM; ++k) {
				for(int i=0; i<=k-i; ++i){
					newStrategy.add(
						Model.combineStrategy(sortedStrategy.get(i),sortedStrategy.get(k-i))
					);
					if(newStrategy.size()==MODEL_NUM) {
						break outloop;
					}
				}
			}
			
			for(int i=0; i<newStrategy.size(); ++i) {
				Model m = new Model(GRID_ROW,GRID_COL,FRUIT_PER);
				m.setStrategy(newStrategy.get(i));
				map.put(m.getStrategy(), m.averageScore(MODEL_STEPS,MODEL_TIMES));
			}
			newStrategy.clear();
		}
	}
	
	public static void main(String[] args) {
		Model.train(10000);
	}

}

