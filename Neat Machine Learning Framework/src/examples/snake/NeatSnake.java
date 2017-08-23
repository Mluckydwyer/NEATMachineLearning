package examples.snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;

import core.Neat;
import core.Objective;
import core.hierarchy.Species;

public class NeatSnake implements Objective, KeyListener {

	private static final long serialVersionUID = 8208701794491690571L;

	public enum Direction {
		UP, DOWN, LEFT, RIGHT
	}

	// neat
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
	private JPanel mainPanel;

	public NeatSnake() {
		snakes = new ArrayList<>();
		mainFrame = new JFrame();
		mainPanel = new JPanel(true) {
			
			@Override
			public void paintComponent(Graphics g) {
				draw();
				super.paint(g);

			}
			
		};
		
		if (AI)
			neat = new Neat(this, numSquares * numSquares, Direction.values().length, targetFitness, populationSize);
		else
			snakes.add(new Snake());

		random = new Random(12345); // test seed
		running = true;
		center = new Point((int) Math.ceil(numSquares / 2), (int) Math.ceil(numSquares / 2));

		//mainFrame.getContentPane().setPreferredSize(new Dimension(numSquares * squareSize, numSquares * squareSize));
		mainPanel.setMinimumSize(new Dimension(numSquares * squareSize, numSquares * squareSize));
		mainPanel.setPreferredSize(new Dimension(numSquares * squareSize, numSquares * squareSize));
		mainFrame.add(mainPanel);
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setResizable(false);
		//mainFrame.setIgnoreRepaint(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//mainFrame.setUndecorated(true);
		//mainFrame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
		mainFrame.setVisible(true);
		
		//ticker();
	}

	private void ticker() {
		while (running) {

			if (AI) {

			}

			//draw();
		}
		System.exit(0);
	}

	private void draw() {
		BufferStrategy bs = mainFrame.getBufferStrategy();
		int titleBarDiff = 0;//mainFrame.getHeight() - mainFrame.getContentPane().getHeight();

		if (bs == null) {
			mainFrame.createBufferStrategy(2);
			return;
		}

		Graphics g = bs.getDrawGraphics();

		g.setColor(Color.GRAY);
		g.fillRect(0, 0, mainFrame.getWidth(), mainFrame.getHeight());
		g.setColor(Color.BLACK);

		for (int i = 0; i < numSquares; i++) {
			g.drawLine(i * squareSize, titleBarDiff, i * squareSize, mainFrame.getHeight());
			g.drawLine(0, i * squareSize + titleBarDiff, mainFrame.getWidth(), i * squareSize + titleBarDiff);
			
			for (int j = 1; j < numSquares; j++) {
				//g.fillRect(i * squareSize, j * squareSize, squareSize, squareSize);
				
			}
		}

		for (int i = squareSize - 1; i < numSquares; i += squareSize) {
			g.drawLine(i, 0, i, mainFrame.getHeight());
			g.drawLine(0, i, mainFrame.getWidth(), i);
		}

		System.out.println("TEST");

		g.dispose();
		bs.show();
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
