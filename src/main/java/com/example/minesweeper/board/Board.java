package com.example.minesweeper.board;

import com.example.minesweeper.enums.GameStatus;
import com.example.minesweeper.enums.TileStatus;
import com.example.minesweeper.tile.*;
import com.example.minesweeper.tileview.TileView;

import java.util.Random;

public class Board {

    private Tile[][] tiles;
    private int row;
    private int column;
    private int mineNum;

    private int score;
    private boolean shielded;
    private int minusVal;

    private boolean hasMineClearance;
    private boolean hasMiniMine;
    private boolean hasRadar;

    private GameStatus status;

    private Board() {
    }

    private static class BoardHolder {
        private static final Board board = new Board();
    }

    public static Board getBoard() {
        return BoardHolder.board;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void createBoard(int row, int column, int mineNum, boolean hasMineClearance, boolean hasMiniMine, boolean hasRadar) {
        this.hasMineClearance = hasMineClearance;
        this.hasMiniMine = hasMiniMine;
        this.hasRadar = hasRadar;
        this.status = GameStatus.idle;
        initBoard(row, column, mineNum);
        print();
    }

    private void initBoard(int row, int column, int mineNum) {
        this.score = 0;
        this.shielded = false;
        this.minusVal = 0;
        this.row = row;
        this.column = column;
        this.mineNum = mineNum;
        this.tiles = new Tile[this.row][this.column];
        //fill in empty tiles
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                this.tiles[i][j] = new Tile(i, j);
            }
        }
    }

    //return value indicates if the game is still running
    public void clickAt(int x, int y) {
        if (status == GameStatus.gameLose || status == GameStatus.gameWin) {
            return;
        }

        //init mines on first click
        if (status == GameStatus.idle) {
            startGame(x, y);
            status = GameStatus.gameRunning;
        }

        Tile target = getTileAt(x, y);
        if (target == null) return;

        if(target.getStatus() == TileStatus.opened){
            if(target.getMinesAround() != 0 && target.getMinesAround() == getNumbersOfFlagsAround(x,y)){
                clickAround(x,y);
            }
            return;
        }

        if (!target.click()) {
            //clicked a mine
            this.status = GameStatus.gameLose;
            revealAll();
            System.out.println("You Lose!");

        } else {
            //safe
            if (win()) {
                this.status = GameStatus.gameWin;
                revealAll();
                System.out.println("You Win!");
            }
        }
        print();
    }

    public void flagAt(int x, int y) {
        if (status == GameStatus.gameWin || status == GameStatus.gameLose || status == GameStatus.idle) {
            return;
        }
        Tile tile = getTileAt(x, y);
        if(tile.getStatus() == TileStatus.opened) return;
        if (tile != null) {
            tile.flag();
        }
        print();
    }

    private void revealAll() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                tiles[i][j].endgameReveal();
            }
        }
        print();
    }

    private void startGame(int x, int y) {
        Random random = new Random();
        int randX = 0, randY = 0;//random number

        int[][] isMine = new int[row][column]; // 1 = Mine, 2 = Clearance, 3 = MiniMine, 4 = Radar

        //generate mines
        for (int i = 0; i < this.mineNum; i++) {
            randX = random.nextInt(this.row);
            randY = random.nextInt(this.column);
            if (randX != x && randY != y && isMine[randX][randY] != 1) {
                this.tiles[randX][randY] = new Mine(randX, randY);
                isMine[randX][randY] = 1;
                for (int j = randX - 1; j <= randX + 1; j++) {
                    for (int k = randY - 1; k <= randY + 1; k++) {
                        if (this.getTileAt(j, k) != null && !(j == randX && k == randY)) tiles[j][k].addMinesAround();
                    }
                }
            } else i--;
        }

        Tile tempTile;

        //generates Radar
        if (hasRadar) {
            int minex = randX, miney = randY;

            //search for next valid position
            do {
                randX = random.nextInt(this.row);
                randY = random.nextInt(this.column);
            } while (!(randX != x && randY != y && isMine[randX][randY] != 1 && isMine[randX][randY] != 2 && isMine[randX][randY] != 3));

            tempTile = new Radar(randX, randY, this.tiles[randX][randY].getMinesAround(), minex, miney);
            this.tiles[randX][randY] = tempTile;
            isMine[randX][randY] = 4;
        }

        //generates MineClearance
        if (hasMineClearance) {
            do {
                randX = random.nextInt(this.row);
                randY = random.nextInt(this.column);
            }
            while (!(randX != x && randY != y && isMine[randX][randY] != 1));

            tempTile = new MineClearance(randX, randY, this.tiles[randX][randY].getMinesAround());
            this.tiles[randX][randY] = tempTile;
            isMine[randX][randY] = 2;
        }

        //generates MiniMine
        if (hasMiniMine) {
            do {
                randX = random.nextInt(this.row);
                randY = random.nextInt(this.column);
            } while (!(randX != x && randY != y && isMine[randX][randY] != 1 && isMine[randX][randY] != 2));

            tempTile = new MiniMine(randX, randY, this.tiles[randX][randY].getMinesAround());
            this.tiles[randX][randY] = tempTile;
            isMine[randX][randY] = 3;
        }

    }

    private void print() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (this.tiles[i][j].getStatus() == TileStatus.closed) System.out.print("#" + " ");
                else if (this.tiles[i][j].getStatus() == TileStatus.flagged) System.out.print("F" + " ");
                else if (this.tiles[i][j].getStatus() == TileStatus.triggered) System.out.print("?" + " ");
                else if (this.tiles[i][j].getStatus() == TileStatus.opened) System.out.print(tiles[i][j].getMinesAround() + " ");
                else if (this.tiles[i][j].getStatus() == TileStatus.exploded) System.out.print("X" + " ");
                else if (this.tiles[i][j].getStatus() == TileStatus.minetriggered) System.out.print("B" + " ");
                else if (this.tiles[i][j].getStatus() == TileStatus.endShield) System.out.print("S" + " ");
                else if (this.tiles[i][j].getStatus() == TileStatus.endMinus) System.out.print("M" + " ");
                else if (this.tiles[i][j].getStatus() == TileStatus.endRadar) System.out.print("R" + " ");
            }
            System.out.println();
        }
    }

    private Tile getTileAt(int x, int y) {
        try {
            return this.tiles[x][y];
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            return null;
        }
    }

    public TileView getTileViewAt(int x,int y){
        if (getTileAt(x, y) != null) {
            return getTileAt(x,y).getTileView();
        }else {
            return null;
        }
    }

    public void triggerMinesAround(int x,int y) {
        for(int i = x - 1;i <= x + 1;i++)
        {
            for(int j = y - 1;j <= y + 1;j++)
            {
                if(getTileAt(i,j) != null && !(i == x && j == y))getTileAt(i,j).trigger();
            }
        }
    }

    public int getNumbersOfFlagsAround(int x,int y){
        int num = 0;
        for(int i = x - 1;i <= x + 1;i++)
        {
            for(int j = y - 1;j <= y + 1;j++)
            {
                if(getTileAt(i,j) != null && !(i == x && j == y)){
                    if(getTileAt(i,j).getStatus() == TileStatus.flagged)
                    {
                        num++;
                    }
                }
            }
        }
        return num;
    }

    public void clickAround(int x,int y){
        for(int i = x - 1;i <= x + 1;i++)
        {
            for(int j = y - 1;j <= y + 1;j++)
            {
                if(getTileAt(i,j) != null && !(i == x && j == y))
                {
                    if (!getTileAt(i,j).click()) {
                        //clicked a mine
                        this.status = GameStatus.gameLose;
                        revealAll();
                        System.out.println("You Lose!");
                        print();
                        return;
                    } else {
                        //safe
                        if (win()) {
                            this.status = GameStatus.gameWin;
                            revealAll();
                            System.out.println("You Win!");
                            print();
                            return;
                        }
                    }
                }
            }
        }
        print();
    }

    public void incScore() {
        this.score++;
    }

    public int getScore() {
        return Math.max(this.score - this.minusVal,0);
    }

    public void makeShielded() {
        this.shielded = true;
    }

    public void useShield() {
        this.shielded = false;
    }

    public boolean isShielded() {
        return this.shielded;
    }

    public void makeGettingMinus() {
        this.minusVal = 10;
    }

    private boolean win() {
        return this.score == this.row * this.column - this.mineNum;
    }
}