package com.example.minesweeper.board;

import com.example.minesweeper.enums.TileStatus;
import com.example.minesweeper.tile.*;

import java.util.ArrayList;
import java.util.Random;

public class Board {

    private Tile[][] tiles;
    private int row;
    private int column;
    private int mineNum;

    private int score;
    private boolean shielded;
    private boolean gotMinus;
    private int minusVal;

    private ArrayList<Integer> mineXList;
    private ArrayList<Integer> mineYList;

    private Board()
    {
        row = 0;
        column = 0;
        mineNum = 0;

        score = 0;
        shielded = false;

        gotMinus = false;
        minusVal = 10;

        mineXList = new ArrayList<Integer>();
        mineYList = new ArrayList<Integer>();

        createBoard(8,8,10);
    }

    public void createBoard(int row,int column,int mineNum)
    {
        this.score = 0;
        this.shielded = false;
        gotMinus = false;
        minusVal = 10;

        this.row = row;
        this.column = column;
        this.mineNum = mineNum;
        this.tiles = new Tile[this.row][this.column];

        mineXList = new ArrayList<Integer>();
        mineYList = new ArrayList<Integer>();

        //fill in empty tiles
        for(int i = 0; i < row; i++){
            for(int j = 0; j < column; j++){
                tiles[i][j] = new Tile(i,j);
            }
        }
    }

    public void startGame(int x,int y) {

        mineXList = new ArrayList<Integer>();
        mineYList = new ArrayList<Integer>();
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
                mineXList.add(randX);
                mineYList.add(randY);

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
        while(!(randX != x && randY != y && isMine[randX][randY] != 1 && isMine[randX][randY] != 2))
        {
            randX = random.nextInt(this.row);
            randY = random.nextInt(this.column);
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
            randX = random.nextInt(this.row);
            randY = random.nextInt(this.column);
            tiles[randX][randY] = new Radar(randX,randY);
            isMine[randX][randY] = 4;
            for(int i = randX - 1; i <= randX + 1; i++){
                for(int j = randY - 1; j <= randY + 1; j++){
                    if( !(i == randX && j == randY) && this.getTileAt(i, j) != null){
                        if(isMine[i][j] == 1) tiles[randX][randY].addMinesAround();
                    }
                }
            }
        }

        //System.out.println("Debug pos 2");
        this.print();
    }




    public void print()
    {
        for(int i = 0; i < row; i++){
            for(int j = 0; j < column; j++){
                if(tiles[i][j].getStatus() == TileStatus.closed) System.out.print("#" + " ");
                else if(tiles[i][j].getStatus() == TileStatus.flagged) System.out.print("F" + " ");
                else if(tiles[i][j].getStatus() == TileStatus.triggered) System.out.print("?" + " ");
                else if(tiles[i][j].getStatus() == TileStatus.opened) System.out.print(tiles[i][j].getMinesAround() + " ");
                else if(tiles[i][j].getStatus() == TileStatus.exploded) System.out.print("X" + " ");
                else if(tiles[i][j].getStatus() == TileStatus.minetriggered) System.out.print("B" + " ");
            }
            System.out.println();
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
        this.score-=minusVal;
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

    public void makeGettingMinus(){
        this.gotMinus = true;
    }

    public void makeFlagRandom(){
        Random random = new Random();
        int rand = random.nextInt(this.mineXList.size());
        int randomMineX = mineXList.get(rand);
        int randomMineY = mineYList.get(rand);
        while(this.getTileAt(randomMineX, randomMineY).getStatus() != TileStatus.flagged){
            rand = random.nextInt(this.mineXList.size());
            randomMineX = mineXList.get(rand);
            randomMineY = mineYList.get(rand);
            this.getTileAt(randomMineX, randomMineY).flag();
        }
    }

    public boolean win(){
        if(!this.gotMinus) return this.score == row * column - mineNum - 1;
        else return this.score == row * column - mineNum - minusVal;
    }

}
