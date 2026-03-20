package com.example.minesweeper.board;

import com.example.minesweeper.tile.Mine;
import com.example.minesweeper.tile.Tile;
import java.util.Random;

public class Board {

    private Tile[][] tiles;
    private int row;
    private int column;
    private int mineNum;

    private int score;
    private boolean shielded;

    private Board()
    {
        row = 0;
        column = 0;
        mineNum = 0;

        score = 0;
        shielded = false;

        createBoard(8,8,10);
    }

    public void createBoard(int row,int column,int mineNum)
    {
        this.score = 0;
        this.shielded = false;

        this.row = row;
        this.column = column;
        this.mineNum = mineNum;
        this.tiles = new Tile[this.row][this.column];

        //fill in empty tiles
        for(int i = 0; i < row; i++){
            for(int j = 0; j < column; j++){
                tiles[i][j] = new Tile(i,j);
            }
        }
    }

    public void startGame(int x,int y) {

        Random random = new Random();
        int randX,randY;//random number

        boolean[][] isMine = new boolean[row][column];//temporary store the position of mines for number counting

        //System.out.println("Debug pos 1");
        //generate mines
        for(int i = 0;i < this.mineNum;i++)
        {
            randX = random.nextInt(this.row);
            randY = random.nextInt(this.column);
            if(randX != x && randY != y && !isMine[randX][randY])
            {
                tiles[randX][randY] = new Mine(randX,randY);
                isMine[randX][randY] = true;

                for(int j = randX - 1;j <= randX + 1;j++)
                {
                    for(int k = randY - 1;k <= randY + 1;k++)
                    {
                        if(this.getTileAt(j, k) != null && !(j == randX && k == randY))tiles[j][k].addMinesAround();
                    }
                }

            }
            else i--;
        }
        //System.out.println("Debug pos 2");
        this.print();
    }




    public void print()
    {
        for(int i = 0; i < row; i++){
            for(int j = 0; j < column; j++){
                System.out.println(tiles[i][j].getStatus());
            }
        }
    }

    public Tile getTileAt(int x,int y)
    {
        try {
            return tiles[x][y];
        }catch (ArrayIndexOutOfBoundsException e)
        {
            //System.out.println("Error: invalid location");
            return null;
        }
    }


    private static class BoardHolder{
        private static final Board board = new Board();
    }

    public static Board getBoard()
    {
        return BoardHolder.board;
    }

    public void incScore(){
        this.score++;
    }

    public int getScore(){
        return this.score;
    }

    public boolean win(){
        return this.score == row * column - mineNum;
    }

}
