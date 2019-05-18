package boardgame.gameModel.pieces;

import boardgame.util.Location;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import static org.valid4j.Assertive.require;

/**
    This piece represents a character on the map.
 */
public abstract class Piece implements IPiece {

    private int moveSpeed;
    private boolean isShielded;
    private int shieldTurn;
    private final ObjectProperty<Location> locationProperty;
    private String specialAbilityType;

    //Used to make piece location observable.
    public ObjectProperty<Location> locationPropertyProperty() {
        return locationProperty;
    }


    public Piece(int moveSpeed, Location location, String abilityType) {
        require(moveSpeed >= 0);
        this.moveSpeed = moveSpeed;
        locationProperty = new SimpleObjectProperty<>(location);
        this.specialAbilityType = abilityType;
        this.isShielded = false;
    }

    @Override
    public int getMoveSpeed() {
        return moveSpeed;
    }

    @Override
    public void setMoveSpeed(int moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    @Override
    public String getSpecialType() { return specialAbilityType; }

    @Override
    public void setSpecialType(String specialAbilityType){ this.specialAbilityType = specialAbilityType; }

    @Override
    public Location getLocation() {
        //return this.location;
        return locationProperty.get();
    }

    @Override
    public Location getLocationProperty() {

        return locationProperty.get();
    }

    //Doesn't allow a new location to be passed. Just take the coordinates to avoid affecting the observable.
    @Override
    public void setLocation(Location location) {
        //this.location.changeLocation(location.getX(), location.getY());
        locationProperty.setValue(location);
    }

    //region Defense methods

    @Override
    public boolean getIsShielded() {
        return isShielded;
    }

    @Override
    public void setIsShielded(boolean isShielded) {
        this.isShielded = isShielded;
    }

    @Override
    public void createShield(int turnNumber) {
        this.setIsShielded(true);

        // Set which turn shield has been activated
        this.shieldTurn = turnNumber;
    }

    // Checks if shield is activated and expires if shield lasted for more than one turn
    public void checkShieldTurn(int turnNumber) {
        if (turnNumber >= this.shieldTurn + 2)
            this.setIsShielded(false);
    }

    //endregion
}
