package boardgame.gameModel;

import boardgame.gameModel.pieces.IPiece;
import javafx.collections.ObservableList;

public class MonsterPlayer extends Player {

    public MonsterPlayer(int playerID, String playerName, int _health, ObservableList<IPiece> pieces) {
        super(playerID, playerName, _health, pieces);
    }
}
