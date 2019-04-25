package boardgame.controller;

import boardgame.gameModel.GameManagerFactory;
import boardgame.gameModel.IGameManager;
import boardgame.gameModel.pieces.IPiece;
import boardgame.gameModel.state.GameContext;
import boardgame.gameModel.state.IdleState;
import boardgame.view.BoardGrid;
import boardgame.view.HexagonTileViewPiece;
import boardgame.view.TileView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static boardgame.util.Constants.TILERADIUS;

/**
 * The Main Controller. This is a JavaFX Controller.
 */
public class MainController implements Initializable {


    @FXML
    private Pane boardPane;

    private RegisterListeners registerListeners;

    @FXML
    private Text pieceSelected;

    @FXML
    private Button moveButton;

    @FXML
    private Button attackButton;

    @FXML
    private Button defendButton;

    private GameContext gameContext;

    /**
     * This is the main entry point after the App class is started. The MainController holds handler methods
     * for input actions. It also registers the listeners for the model pieces. As our application follows
     * an observer pattern these listeners will update the view when triggered.
     * The handle methods call the model through the gameManager interface when responding to user input as per the MVC
     * pattern.
     * The MainController class is the main part of application that currently requires major refactoring as
     * it has a bit too much coupling. Whilst it is reasonably cohesive it is highly coupled.
     * Ideally we would also remove a bit of game logic that is stuck here.
     */
    public MainController() {
        //Get a reference to the game manager. Currently sets up a game with default settings.
        gm = GameManagerFactory.createGameManager();
        gm.defaultGameSetup();

    }

    @FXML
    private Text pieceLocation;

    @FXML
    private Button swapButton;

    @FXML
    private Pane SwapPane;

    @FXML
    private Button Opt_one;

    @FXML
    private Button Opt_two;
    private GameController gameController;

   // private State currentState = State.NONE;

    //Store a reference to the Game manager for main entry point to game.
    private IGameManager gm;

    private BoardGrid boardGrid;

    public Text getPieceSelected() {
        return pieceSelected;
    }

    public Text getPieceLocation() {
        return pieceLocation;
    }

    public RegisterListeners getRegisterListeners() {
        return registerListeners;
    }


    public BoardGrid getBoardGrid() {
        return boardGrid;
    }

    public GameController getGameController() {
        return gameController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        StatusController statusController = new StatusController(gm);

        boardGrid = new BoardGrid(boardPane);
        IdleState idleState = new IdleState();
        gameContext = new GameContext(idleState, boardGrid, gm, this);

        boardGrid.drawBasicGrid(new ArrayList<>(gm.getiBoard().getTiles().values()), TILERADIUS, boardPane);
        registerListeners = RegisterListenerFactory.createRegisterListeners(this, gm, statusController);

        initialiseHandlers();

        registerListeners.registerTileListenersForMove(boardGrid.getHexagonTileViews());
        registerListeners.registerPlayerListeners(gm.getPlayers());
        registerListeners.registerTurnListeners(gm.getTurn());

        gameController = GameControllerFactory.createGameController(gm, boardGrid, this);
        gameController.setUpGame();

        boardPane.getChildren().add(statusController);
        statusController.setLayoutX(800);

        registerListeners.registerPieceListListener();
    }

    private void chooseAttackTargetPiece() {

        gameContext.pressAttack();

        if (boardGrid.getSelectedTilePiece() != null)
            resetTileColors();
    }

    private void resetTileColors() {
        boardGrid.setNeighbourTilesColor(boardGrid.getSelectedTilePiece(), Color.ANTIQUEWHITE);
    }

    //Selects piece.
    public void handlePieceClicked(HexagonTileViewPiece piece) {

        // Reset tiles color
        if (boardGrid.getSelectedTilePiece() != null)
            resetTileColors();

        boardGrid.setSelectedTilePiece(piece);
        boardGrid.setTileSelected(true);


        if (isActivePlayerPiece()) {
            System.out.println("wazzzup");
            gameContext.setOwnPiece(piece);
            gameContext.selectOwnPiece(piece);
        }


//        if (gameContext.getState().getClass().getName().equals(AttackState.class.getName()) && !isActivePlayerPiece()) {
//            System.out.println("trying to attack");
//            gameContext.setEnemyPiece(piece);
//        }

        if (!isActivePlayerPiece()) {
            System.out.println("Selecting opponent piece");
            gameContext.setEnemyPiece(piece);
            gameContext.selectEnemyPiece(piece);
        }


//                if(!isActivePlayerPiece()) {

//                    // get attacked player
//                    gm.getAttackedPlayer(boardGrid.getSelectedTilePiece().getiPiece()).decreaseHealthProperty(boardGrid.getSelectedTilePiece().getiPiece());
//
//                    // end turn
//                    gm.getTurn().nextTurn(gm.getPlayers());
        //       }
//                break;
//            case SPECIAL_ABILITY:
//                break;
//            case DEFENSE:
//                if (isActivePlayerPiece()) {
//                    // Get active player and create shield
//                    boardGrid.getSelectedTilePiece().getiPiece().createShield(gm.getTurn().getTurnNumber());
//
//                    // end turn
//                    gm.getTurn().nextTurn(gm.getPlayers());
//                }
//                break;
//            case SWAP:
//                break;
//        }
    }

    private void chooseDefenseTargetPiece() {
        System.out.println("Clicked defense");
        gameContext.pressDefence(gameContext);
    }

    //Gets input and updates model for piece position.
    public void handleTileClicked(TileView tile) {
        assert tile != null;

        boardGrid.setTargetTile(tile);
        gameContext.clickTile(tile);

    }
    private void initialiseHandlers() {
        // register piece actions
        moveButton.setOnMouseClicked(e -> handleMoveClicked());
        attackButton.setOnAction(e -> chooseAttackTargetPiece());
        swapButton.setOnAction(e -> SwapController.handleSwapAction(SwapPane, gm, Opt_one, Opt_two));
        Opt_one.setOnAction(e -> SwapController.doSwap(gm, SwapPane, Opt_one));
        Opt_two.setOnAction(e -> SwapController.doSwap(gm, SwapPane, Opt_two));
        //defense code
        defendButton.setOnAction(e -> chooseDefenseTargetPiece());
    }

    private void handleMoveClicked() {
        gameContext.pressMove();
    }

    // Checks if selected piece belongs to the active player
    private boolean isActivePlayerPiece() {
        if (boardGrid.getSelectedTilePiece() == null)
            return false;

        for (IPiece piece : gm.getTurn().getActivePlayer().getPieces()) {
            if (piece.getClass().getSimpleName().equals(boardGrid.getSelectedTilePiece().getiPiece().getClass().getSimpleName())) {
                return true;
            }
        }
        return false;
    }

}
