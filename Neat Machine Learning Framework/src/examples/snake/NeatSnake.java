package examples.snake;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

import core.Neat;
import core.Objective;
import core.hierarchy.Species;

public class NeatSnake implements Objective, KeyListener {

	private static final long serialVersionUID = 8208701794491690571L;
	public enum Direction {
		UP, DOWN, LEFT, RIGHT
	}
	//neat
	private Neat neat;
	private Random random;
	private ArrayList<Snake> snakes;
	public static final int numSquares = 15;
	private final int squareSize = 50;
	private boolean AI = true; // Toggles Neat Genetic Learning Framework
	private double targetFitness = 10000; // 10,000?
	private int populationSize = 100;
	private boolean running;
	private Point center;
	private JFrame mainFrame;
	Graphics graphic;

	public NeatSnake() {		
		snakes = new ArrayList<>();
		mainFrame=new JFrame();
		graphic = mainFrame.getGraphics();
		if (AI) neat = new Neat(this, numSquares * numSquares, Direction.values().length, targetFitness, populationSize);
		else snakes.add(new Snake());

		
		random = new Random(12345); // test seed
		running = true;
		center = new Point((int) Math.ceil(numSquares / 2), (int) Math.ceil(numSquares / 2));
		
		mainFrame.setVisible(true);
		mainFrame.setSize(numSquares * squareSize, numSquares * squareSize); // 15 x 15 squares each 50 pixels
		mainFrame.getContentPane().setBackground(Color.WHITE);
		Timer t = new Timer(1000 / 60, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tick();
			}
		});
		
		reset();
		t.start();
	}

	private void reset() {
		
	}

	private void tick() {
		if (running) {
			
			if (AI) {
				
			}
			
			draw();
		}
		else System.exit(0);
	}

	private void draw() {		
		for(Snake a:snakes){
			for(int i=0;i<numSquares;i++){
				for(int j=0;j<numSquares;j++){
					
					graphic.setColor(Color.BLACK);
					graphic.fillRect(i*squareSize,j*squareSize,squareSize,squareSize);
				}
			}
		}
		for (int i = squareSize - 1; i < numSquares; i += squareSize) {
			graphic.drawLine(i, 0, i, mainFrame.getHeight());
			graphic.drawLine(0, i, mainFrame.getWidth(), i);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int[] calculateFitness(ArrayList<Species> s) {
		// TODO Auto-generated method stub
		return null;
	}

}
