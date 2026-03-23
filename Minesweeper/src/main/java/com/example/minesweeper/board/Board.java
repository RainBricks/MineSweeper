package com.example.minesweeper.board;

import com.example.minesweeper.enums.TileStatus;
import com.example.minesweeper.tile.*;

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

    private boolean hasMineClearance;
    private boolean hasMiniMine;
    private boolean hasRadar;

    private Board()
    {
        createBoard(8,8,10);
    }

    public void createBoard(int row,int column,int mineNum)
    {
        Random random = new Random();
        this.hasMineClearance = random.nextBoolean();
        this.hasMiniMine = random.nextBoolean();
        this.hasRadar = random.nextBoolean();
        initBoard(row,column,mineNum);
    }

    public void createBoard(int row,int column,int mineNum,boolean hasMineClearance,boolean hasMiniMine,boolean hasRadar)
    {
        this.hasMineClearance = hasMineClearance;
        this.hasMiniMine = hasMiniMine;
        this.hasRadar = hasRadar;
        initBoard(row,column,mineNum);
    }

    private void initBoard(int row,int column,int mineNum)
    {
        this.score = 0;
        this.shielded = false;
        this.gotMinus = false;
        this.minusVal = 10;
        this.row = row;
        this.column = column;
        this.mineNum = mineNum;
        this.tiles = new Tile[this.row][this.column];
        //fill in empty tiles
        for(int i = 0; i < row; i++){
            for(int j = 0; j < column; j++){
                this.tiles[i][j] = new Tile(i,j);
            }
        }
    }

    public void startGame(int x,int y) {

        Random random = new Random();
        int randX = 0,randY = 0;//random number

        int[][] isMine = new int[row][column]; // 1 = Mine, 2 = Clearance, 3 = MiniMine, 4 = Radar

        //System.out.println("Debug pos 1");

        //generate mines
        for(int i = 0;i < this.mineNum;i++)
        {
            randX = random.nextInt(this.row);
            randY = random.nextInt(this.column);
            if(randX != x && randY != y && isMine[randX][randY] != 1)
            {
                this.tiles[randX][randY] = new Mine(randX,randY);
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

        Tile tempTile;

        //generates Radar
        if(hasRadar)
        {
            int minex = randX,miney = randY;

            //search for next valid position
            do {
                randX = random.nextInt(this.row);
                randY = random.nextInt(this.column);
            } while (!(randX != x && randY != y && isMine[randX][randY] != 1 && isMine[randX][randY] != 2 && isMine[randX][randY] != 3));

            tempTile = new Radar(randX, randY, minex, miney);
            tempTile.setMinesAround(this.tiles[randX][randY].getMinesAround());
            this.tiles[randX][randY] = tempTile;
            isMine[randX][randY] = 4;

        }


        //generates MineClearance
        if(hasMineClearance)
        {
            do {
                randX = random.nextInt(this.row);
                randY = random.nextInt(this.column);
            }
            while(!(randX != x && randY != y && isMine[randX][randY] != 1));

            tempTile = new MineClearance(randX, randY);
            tempTile.setMinesAround(this.tiles[randX][randY].getMinesAround());
            this.tiles[randX][randY] = tempTile;
            isMine[randX][randY] = 2;

        }


        //generates MiniMine
        if(hasMiniMine) {
            do{
                randX = random.nextInt(this.row);
                randY = random.nextInt(this.column);
            }while(!(randX != x && randY != y && isMine[randX][randY] != 1 && isMine[randX][randY] != 2));

            tempTile = new MiniMine(randX, randY);
            tempTile.setMinesAround(this.tiles[randX][randY].getMinesAround());
            this.tiles[randX][randY] = tempTile;
            isMine[randX][randY] = 3;

        }

        //System.out.println("Debug pos 2");
        this.print();
    }

    public void print()
    {
        for(int i = 0; i < row; i++){
            for(int j = 0; j < column; j++){
                if(this.tiles[i][j].getStatus() == TileStatus.closed) System.out.print("#" + " ");
                else if(this.tiles[i][j].getStatus() == TileStatus.flagged) System.out.print("F" + " ");
                else if(this.tiles[i][j].getStatus() == TileStatus.triggered) System.out.print("?" + " ");
                else if(this.tiles[i][j].getStatus() == TileStatus.opened) System.out.print(tiles[i][j].getMinesAround() + " ");
                else if(this.tiles[i][j].getStatus() == TileStatus.exploded) System.out.print("X" + " ");
                else if(this.tiles[i][j].getStatus() == TileStatus.minetriggered) System.out.print("B" + " ");
            }
            System.out.println();
        }
    }

    public Tile getTileAt(int x,int y)
    {
        try {
            return this.tiles[x][y];
        }catch (ArrayIndexOutOfBoundsException e)
        {
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

    public boolean win(){
        if(!this.hasMiniMine){
            return this.score == this.row * this.column - this.mineNum;
        }else{
            if(!this.gotMinus) return this.score == this.row * this.column - this.mineNum ;
            else return this.score == this.row * this.column - this.mineNum - this.minusVal;
        }
    }

}
