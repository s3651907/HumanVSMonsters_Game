package boardgame.gameModel.pieces;

import boardgame.gameModel.Location;

public class Medusa extends Monster {
    public Medusa(int _health, int moveSpeed, Location location) {
        super(_health, moveSpeed, location);
    }

    public void basicAttack(){}
    public void summonSnakes(){}
}
