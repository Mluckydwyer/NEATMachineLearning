package examples.snake;

import java.awt.Graphics;
import java.awt.Color;

import examples.snake.NeatSnake.Direction;

public class Snake {
//snake
	public Direction dir;
	public int[][] gameBoard;
	public int length;
	int pdirection;//0=up,1=right,2=down,3=left
	
	public Snake(){
	gameBoard = new int[NeatSnake.numSquares][NeatSnake.numSquares];
	//0=nothing
	//-1=food
	//1=body
	//2=head
	gameBoard[0][0]=2;//sets head
	gameBoard[gameBoard.length/2][gameBoard[0].length/2]=-1;//sets start food
	pdirection=1;
	}
	
	public int[][] returnboard(){
		return gameBoard;
	}
	
	public void move(int dir){
		if(dir!=pdirection && dir!=pdirection-2 && dir!=pdirection+2){
		for(int i=0;i<gameBoard.length;i++){
			for(int j=0;j<gameBoard[i].length;j++){
				
			}
			}
		}
	}
}
