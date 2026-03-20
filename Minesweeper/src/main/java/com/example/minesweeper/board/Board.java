package com.example.minesweeper.board;

import com.example.minesweeper.tile.*;

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

        int[][] isMine = new int[row][column]; // 1 = Mine, 2 = Clearance, 3 = MiniMine, 4 = Radar

        //System.out.println("Debug pos 1");
        //generate mines
        for(int i = 0;i < this.mineNum;i++)
        {
            randX = random.nextInt(this.row);
            randY = random.nextInt(this.column);
            if(randX != x && randY != y && isMine[randX][randY] != 1)
            {
                tiles[randX][randY] = new Mine(randX,randY);
                isMine[randX][randY] = 1;

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

        //generates MineClearance
        randX = random.nextInt(this.row);
        randY = random.nextInt(this.column);
        if(randX != x && randY != y && isMine[randX][randY] != 1) {
            tiles[randX][randY] = new MineClearance(randX, randY);
            isMine[randX][randY] = 2;

            for(int i = randX - 1; i <= randX + 1; i++){
                for(int j = randY - 1; j <= randY + 1; j++){
                    if( !(i == randX && j == randY) && this.getTileAt(i, j) != null){
                        if(isMine[i][j] == 1) tiles[randX][randY].addMinesAround();
                    }
                }
            }

        }

        //generates MiniMine
        randX = random.nextInt(this.row);
        randY = random.nextInt(this.column);
        if(randX != x && randY != y && isMine[randX][randY] != 1 && isMine[randX][randY] != 2)
        {
            tiles[randX][randY] = new MiniMine(randX,randY);
            isMine[randX][randY] = 3;
            for(int i = randX - 1; i <= randX + 1; i++){
                for(int j = randY - 1; j <= randY + 1; j++){
                    if( !(i == randX && j == randY) && this.getTileAt(i, j) != null){
                        if(isMine[i][j] == 1) tiles[randX][randY].addMinesAround();
                    }
                }
            }
        }

        //generates Radar
        randX = random.nextInt(this.row);
        randY = random.nextInt(this.column);
        if(randX != x && randY != y && isMine[randX][randY] != 1 && isMine[randX][randY] != 2 && isMine[randX][randY] != 3)
        {
            tiles[randX][randY] = new Radar(randX,randY);
            isMine[randX][randY] = 4;
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
    public void decScore(){
        this.score-=10;
    }

    public int getScore(){
        return this.score;
    }

    public void makeShielded(){
        this.shielded = true;
    }

    public void useShield(){
        this.shielded = false;
    }

    public boolean isShielded(){
        return this.shielded;
    }

    public boolean win(){
        return this.score == row * column - mineNum;
    }

}
