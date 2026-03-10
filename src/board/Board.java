package board;

import javafx.util.Pair;
import tile.Mine;
import tile.Tile;


import java.util.Random;

public class Board {

    private Tile[][] tiles;
    private int row;
    private int column;
    private int mineNum;
    private boolean shielded;

    public Board(String difficulty,int x,int y) {
        shielded = false;
        switch(difficulty){
            case "Easy":
                this.row = 8;
                this.column = 8;
                this.mineNum = 10;
                break;
            case "Medium":
                this.row = 16;
                this.column = 16;
                this.mineNum = 40;
                break;
            case "Hard":
                this.row = 16;
                this.column = 30;
                this.mineNum = 99;
                break;
            default:
                this.row = 8;
                this.column = 8;
                this.mineNum = 10;
                break;
        }

        this.tiles = new Tile[row][column];

        Random random = new Random();
        int randX,randY;//random number

        Boolean[][] isMine = new Boolean[row][column];//temporary store the position of mines for number counting

        //fill in empty tiles
        for(int i = 0; i < row; i++){
            for(int j = 0; j < column; j++){
                tiles[i][j] = new Tile(this,i,j);
            }
        }

        //generate mines
        for(int i = 0;i < this.mineNum;i++)
        {
            randX = random.nextInt(this.row);
            randY = random.nextInt(this.column);
            if(randX != x && randY != y && isMine[randX][randY] == true)
            {
                tiles[randX][randY] = new Mine(this,randX,randY);
                isMine[randX][randY] = true;
                if(this.getTileAt(randX - 1, randY) != null)tiles[randX - 1][randY].addMinesAround();
                if(this.getTileAt(randX + 1, randY) != null)tiles[randX + 1][randY].addMinesAround();
                if(this.getTileAt(randX, randY - 1) != null)tiles[randX][randY - 1].addMinesAround();
                if(this.getTileAt(randX, randY + 1) != null)tiles[randX][randY + 1].addMinesAround();
            }
            else i--;
        }

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
        }catch (NullPointerException e)
        {
            return null;
        }
    }


}
