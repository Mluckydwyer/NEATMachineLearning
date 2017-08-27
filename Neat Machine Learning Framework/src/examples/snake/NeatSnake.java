package examples.snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import core.Neat;
import core.NeatObjective;
import core.hierarchy.Genome;
import core.hierarchy.Species;

public class NeatSnake implements NeatObjective, KeyListener {

	public enum Direction {
		UP, DOWN, LEFT, RIGHT
	}

	// neat
	private Neat neat;
	private Random random;
	private ArrayList<Snake> snakes;
	public static final int numSquares = 15;
	private final int squareSize = 50;
	private boolean AI = false; // Toggles Neat Genetic Learning Framework
	private double targetFitness = 10000; // 10,000?
	private int populationSize = 100;
	private boolean running;
	private Point center;
	private JFrame mainFrame;
	private JPanel mainPanel;
	public int leftcomp = 8;
	public int upcomp = 30;

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
			neat = new Neat("NEAT Snake", this, numSquares * numSquares, Direction.values().length, populationSize, targetFitness);
		else snakes.add(new Snake());

		random = new Random(12345); // test seed
		running = true;
		center = new Point((int) Math.ceil(numSquares / 2), (int) Math.ceil(numSquares / 2));

		// mainFrame.getContentPane().setPreferredSize(new Dimension(numSquares
		// * squareSize, numSquares * squareSize));
		mainPanel.setMinimumSize(new Dimension(numSquares * squareSize, numSquares * squareSize));
		mainPanel.setPreferredSize(new Dimension(numSquares * squareSize, numSquares * squareSize));
		mainFrame.add(mainPanel);
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setResizable(false);
		// mainFrame.setIgnoreRepaint(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// mainFrame.setUndecorated(true);
		// mainFrame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
		mainFrame.setVisible(true);

		// ticker();
	}

	private void ticker() {
		while (running) {

			if (AI) {

			}

			// draw();
		}
		System.exit(0);
	}

	private void draw() {
		BufferStrategy bs = mainFrame.getBufferStrategy();
		if (bs == null) {
			mainFrame.createBufferStrategy(2);
			return;
		}

		Graphics g = bs.getDrawGraphics();

		g.setColor(Color.GRAY);
		g.fillRect(0, 0, mainFrame.getWidth(), mainFrame.getHeight());
		g.setColor(Color.BLACK);
		for (Snake s : snakes) {
			int[][] tempsnake = s.gameBoard;
			System.out.println(tempsnake[0][0]);
			for (int i = 0; i < numSquares; i++) {
				for (int j = 0; j < numSquares; j++) {
					if (tempsnake[i][j] == -1) {
						g.setColor(Color.CYAN);
					}
					else if (tempsnake[i][j] == 1) {
						g.setColor(Color.BLACK);
					}
					else if (tempsnake[i][j] == 2) {
						g.setColor(Color.BLACK);
					}
					else {
						g.setColor(Color.WHITE);
					}
					g.fillRect(i * squareSize + leftcomp, j * squareSize + upcomp, squareSize + leftcomp,
							squareSize + upcomp);
				}
			}
		}
		g.setColor(Color.BLACK);
		for (int i = 0; i < numSquares; i++) {
			g.drawLine(i * squareSize + leftcomp, upcomp, i * squareSize + leftcomp, mainFrame.getHeight() + upcomp);
			g.drawLine(0 + leftcomp, i * squareSize + upcomp, mainFrame.getWidth() + leftcomp, i * squareSize + upcomp);

		}

		g.dispose();
		bs.show();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == 38) {// UP
			snakes.get(0).move(0);
			System.out.println("UP");// prints 2?
		}
		if (e.getKeyCode() == 39) {// RIGHT
			snakes.get(0).move(1);
		}
		if (e.getKeyCode() == 40) {// DOWN
			snakes.get(0).move(2);
		}
		if (e.getKeyCode() == 41) {// LEFT
			snakes.get(0).move(3);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void calculateFitness(Genome genome) {
		// TODO Auto-generated method stub
	}

}
