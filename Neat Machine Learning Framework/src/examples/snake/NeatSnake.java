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

import javax.swing.JApplet;
import javax.swing.Timer;

import core.Neat;
import core.Objective;
import core.Species;

public class NeatSnake extends JApplet implements Objective, KeyListener {

	private static final long serialVersionUID = 8208701794491690571L;
	public enum Direction {
		UP, DOWN, LEFT, RIGHT
	}
	
	private Neat neat;
	private Random random;
	private ArrayList<Snake> snakes;
	private int[][][] gameBoards; // snake #, x, y | 0 = empty, -1 = food, 1-... = snake parts in numbered order
	public static final int numSquares = 15;
	private final int squareSize = 50;
	private boolean AI = true; // Toggles Neat Genetic Learning Framework
	private boolean running;
	private Point center;

	public NeatSnake() {		
		snakes = new ArrayList<>();
		
		if (AI) neat = new Neat(this);
		else snakes.add(new Snake());

		
		random = new Random(12345); // test seed
		running = true;
		center = new Point((int) Math.ceil(numSquares / 2), (int) Math.ceil(numSquares / 2));
		
		
		setSize(numSquares * squareSize, numSquares * squareSize); // 15 x 15 squares each 50 pixels
		setBackground(Color.gray);

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
		Graphics g = getGraphics();

		for (int i = squareSize - 1; i < numSquares; i += squareSize) {
			g.drawLine(i, 0, i, getHeight());
			g.drawLine(0, i, getWidth(), i);
		}
	}

	@Override
	public int[] calculateFitness(ArrayList<Species> s) {
		// TODO Auto-generated method stub
		return null;
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

}
