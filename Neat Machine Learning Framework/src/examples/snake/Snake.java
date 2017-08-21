package examples.snake;

import java.awt.Graphics;
import java.awt.Color;

import examples.snake.NeatSnake.Direction;

public class Snake {
//snake
	public Direction dir;
	public int[][] gameBoard;
	public int length;
	
	public Snake(){
	gameBoard = new int[NeatSnake.numSquares][NeatSnake.numSquares];
	}
	
	public int[][] returnboard(){
		return gameBoard;
	}
}
