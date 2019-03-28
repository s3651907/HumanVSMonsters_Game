package boardgame.gameModel;

import boardgame.gameModel.board.Board2DHex;
import boardgame.gameModel.board.IBoard;
import boardgame.gameModel.pieces.*;
import boardgame.gameModel.tiles.HexagonalTile;
import boardgame.gameModel.tiles.ITile;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GameManager {
    private ArrayList<IPlayer> players;
    private IBoard iBoard;
    private IPiece pieces;
    private Turn turn;
    private Stack<Turn> turnStack;
    private List<Human> humanPieces;
    private List<Monster> monsterPieces;

    //Default constructor
    public GameManager(){

        //Add default players etc.
        defaultGameSetup();

        //Set up default board.
        iBoard = setUpBoard();

        turn = new Turn();

    }

    public IBoard setUpBoard(){
        Board2DHex board2DHex = new Board2DHex();
        board2DHex.setUpTiles(10, 10);
        return board2DHex;
    }

    public List<Monster> setUpMonsterPieces() {
        List<Monster> monsters = new ArrayList<>();
        monsters.add(new Griffin(10, 5));
        monsters.add(new Medusa(10, 5));
        monsters.add(new Minotaur(10, 5));
        return monsters;
    }

    public List<Human> setUpHumanPieces() {

        ArrayList<Human> humans = new ArrayList<>();
        humans.add(new Warrior(10, 5));
        humans.add(new Priest(10, 5));
        humans.add(new Archer(10, 5));

        return humans;
    }

    private void defaultGameSetup(){
        players = new ArrayList<>();
        players.add(new HumanPlayer(1, "Gandalf"));
        players.add(new MonsterPlayer(2, "Sauron"));

        //Add default 3 human pieces
        humanPieces = setUpHumanPieces();

        //Add default 3 monster pieces
        monsterPieces = setUpMonsterPieces();
    }


    //Only allow movement to a neighbouring square/hexagon.
    private boolean checkValidMove(HexagonalTile piece, Location mapLocation) {
        List<ITile> neighbours =getiBoard().getTiles().get(piece.getGridPosition()).getNeighbours();
        for (ITile tile: neighbours) {
            if (tile.getGridPosition().equals(mapLocation)){
                System.out.println("Valid move!");
                return true;
            }
        }
        System.out.println("Invalid move!");
        return false;
    }

    public IBoard getiBoard() {
        return iBoard;
    }

    public ArrayList<IPlayer> getPlayers() {
        return players;
    }

    public void setiBoard(IBoard iBoard) {
        this.iBoard = iBoard;
    }
}
