package it.polimi.se2018.client.view.gui;


import it.polimi.se2018.client.ClientInterface;
import it.polimi.se2018.client.network.rmi.RmiHandler;
import it.polimi.se2018.client.network.socket.SocketHandler;
import it.polimi.se2018.client.view.View;
import it.polimi.se2018.server.controller.ToolCard;
import it.polimi.se2018.server.model.Cards.PatternCard;
import it.polimi.se2018.server.model.Cards.PrivateObjectiveCard;
import it.polimi.se2018.server.model.Cards.PublicObjectiveCard.PublicObjectiveCard;
import it.polimi.se2018.server.model.Components.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class GuiController extends View {


    @FXML
    private TextField txtName;
    @FXML
    private Button playButton;


    private ClientInterface connection;
    private static final int SOCKETPORT = 8888;
    private static SocketHandler serverSocket;
    private static RmiHandler serverRmi;
    private static String host = "localhost";
    private String name;
    private SimpleBooleanProperty nameSetted = new SimpleBooleanProperty(false);
    private Stage stage;
    private boolean singlePlayer;
    private boolean gameStarted;
    private List<ToolCard>  toolList;
    private List<PatternCard>  patternList;
    private List<PublicObjectiveCard>  publicCardList;
    private PrivateObjectiveCard privateCard;
    private RoundTracker roundTracker;
    private PatternCard patternCurrent;
    private PatternCard patternID0;
    private PatternCard patternID1;
    private PatternCard patternID2;
    private PatternCard patternID3;
    private String nameID0;
    private String nameID1;
    private String nameID2;
    private String nameID3;
    private static BoardController board;

    public static void setBoard(BoardController board) {
        GuiController.board = board;
    }

    //-------------------------gui start-----------------


    public void setConnectionTypeAndStage(String connectionType, Stage primaryStage,boolean singlePlayer) throws IOException{

        this.stage = primaryStage;
        this.singlePlayer = singlePlayer;

        if (connectionType.equalsIgnoreCase("socket")) {

            serverSocket = new SocketHandler(host, SOCKETPORT, this);

            this.setConnection(serverSocket);

            Thread socketThread = new Thread(serverSocket);
            socketThread.start();

            //Thread viewSocketThread = new Thread(this);
            //viewSocketThread.start();

        }

        if (connectionType.equalsIgnoreCase("rmi")) {

            serverRmi = new RmiHandler(this);

            this.setConnection(serverRmi);

            Thread viewRmiThread = new Thread(this);
            viewRmiThread.start();
        }
    }



    //----------------mode.fxml controller----------------


    public void initialize() {

        gameStarted = false;

        playButton.disableProperty().bind(txtName.textProperty().isEmpty().or(nameSetted));

        playButton.disableProperty().addListener(
                new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                        System.out.println(newValue.toString());

                    }
                }

        );

    }

    @FXML
    void handlePlayButton(ActionEvent event) {

        if (gameStarted) {
            this.name = txtName.getText();
            getConnection().setPlayerNameToServer(getName(), getPlayerID());
            nameSetted.setValue(true);
            AlertBox.display("Name Choose", "Name entered, some seconds and will go");
        }

        else {
            AlertBox.display("Name Choose", "You have to wait till the game is started!");
        }

    }

    private void setMode() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (singlePlayer) {
                    getConnection().setSinglePlayerMode(getPlayerID(), true);
                }
                else {
                    getConnection().setSinglePlayerMode(getPlayerID(), false);

                }
            }
        });
    }


    public void setPattern(int indexPattern){

        getConnection().setPatternCardToServer(indexPattern, getPlayerID());
    }


    //--------------getter and setter-------------------

    @Override
    public void setConnection(ClientInterface connection) {
        this.connection = connection;
    }

    @Override
    public ClientInterface getConnection() {
        return connection;
    }

    public String getName() {
        return name;
    }

    public PrivateObjectiveCard getPrivateCard() {
        return privateCard;
    }

    public List<ToolCard> getToolList() {
        return toolList;
    }

    public List<PublicObjectiveCard> getPublicCardList() {
        return publicCardList;
    }

    public List<PatternCard> getPatternList() {
        return patternList;
    }

    public RoundTracker getRoundTracker(){
        return roundTracker;
    }

    public PatternCard getPatternCurrent() {
        return patternCurrent;
    }

    public PatternCard getPatternID0() {
        return patternID0;
    }

    public PatternCard getPatternID1() {
        return patternID1;
    }

    public PatternCard getPatternID2() {
        return patternID2;
    }

    public PatternCard getPatternID3() {
        return patternID3;
    }

    public String getNameID0() {
        return nameID0;
    }

    public String getNameID1() {
        return nameID1;
    }

    public String getNameID2() {
        return nameID2;
    }

    public String getNameID3() {
        return nameID3;
    }

    public void setNameID0(String nameID0) {
        this.nameID0 = nameID0;
    }

    public void setNameID1(String nameID1) {
        this.nameID1 = nameID1;
    }

    public void setNameID2(String nameID2) {
        this.nameID2 = nameID2;
    }

    public void setNameID3(String nameID3) {
        this.nameID3 = nameID3;
    }

    public Stage getStage() {
        return stage;
    }



    //--------------------show methods for update scene-------------------------

    @Override
    public void run() {

    }

    @Override
    public void showID() {

    }

    @Override
    public void showSinglePlayerRequest() {

        setMode();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                AlertBox.display("Name Choose", "Please wait, the game will start in a few seconds");
            }
        });

    }

    @Override
    public void showGameStarted() {

        gameStarted = true;

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                AlertBox.display("Name Choose", "Game is started, enter your name");
            }
        });
    }

    @Override
    public void showNameChoose() {


    }

    @Override
    public void showName() {

        System.out.println("Player name: " + super.getPlayerName());

    }

    @Override
    public void showNameOther(String playerName) {

        /*Platform.runLater(new Runnable() {
            @Override
            public void run() {
                AlertBox.display("Name Choose", "Another player connected with name " + playerName);
            }
        });*/

    }

    @Override
    public void showNameError() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                AlertBox.display("Error", "Name already chosen");
            }
        });

    }

    @Override
    public void showPrivateCard(PrivateObjectiveCard privateObjectiveCard) {

        System.out.println(privateObjectiveCard.getColour().toString());

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                privateCard = privateObjectiveCard;
            }
        });

    }

    @Override
    public void showPublicCard(List<PublicObjectiveCard> publicList) throws IOException{

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                publicCardList = publicList;
            }
        });


    }

    @Override
    public void showPatternList(List<PatternCard> patternCards) throws IOException {


        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                patternList = patternCards;
            try {
                patternScene();
            }catch (IOException e){
                e.printStackTrace();
            }


            }
        });

    }

    @Override
    public void showPattern(PatternCard patternCard) throws IOException {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                patternCurrent = patternCard;
            }
        });

    }

    @Override
    public void showOtherStartPattern(PatternCard patternCard, int ID) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (ID == 0) {
                    patternID0 = patternCard;
                }
                if (ID == 1) {
                    patternID1 = patternCard;
                }
                if (ID == 2) {
                    patternID2 = patternCard;
                }
                if (ID == 3) {
                    patternID3 = patternCard;
                }
            }
        });



    }

    @Override
    public void showOtherPattern(PatternCard patternCard, String playerName, int ID) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                if (ID == 0) {
                    patternID0 = patternCard;
                    setNameID0(playerName);
                }
                if (ID == 1) {
                    patternID1 = patternCard;
                    setNameID1(playerName);
                }
                if (ID  == 2) {
                    patternID2 = patternCard;
                    setNameID2(playerName);
                }
                if (ID == 3) {
                    patternID3 = patternCard;
                    setNameID3(playerName);
                }
            }
        });

    }

    @Override
    public void showPatternUpdate(PatternCard patternCard) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                board.updatePattern(patternCard);
            }
        });

    }

    @Override
    public void showTokens(int tokensNumber) {


        // todo refactor this method for the tokens available at the start
        // todo see when effectively it'sended by the model

        /*Platform.runLater(new Runnable() {
            @Override
            public void run() {
                board.updateToken(tokensNumber);
            }
        });*/

    }

    @Override
    public void showStartScene() throws IOException {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    boardScene();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void showCurrentRound(int round) {


        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                board.updateRound(round);
            }
        });


    }

    @Override
    public void showCurrentTurn() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                board.updateTurn();
            }
        });

    }

    @Override
    public void showOtherCurrentTurn(String username) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                board.updateOtherTurn(username);
            }
        });

    }

    @Override
    public void showRollCommand() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                board.textRollMsg();
            }
        });

    }

    @Override
    public void showDraftPool(DraftPool draftPool) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    board.updateDraftPool(draftPool);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void showChooseCommand() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                board.textChooseMsg();
            }
        });
    }

    @Override
    public void showMoveCommand(int poolSize) {

    }

    @Override
    public void showIndexPoolCommand(int poolsize) {
        //non usare
    }

    @Override
    public void showIndexPatternCommand() {
        //non usare
    }

    @Override
    public void showToolCommand(List<ToolCard> toolCards) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                board.toolMoveMsg();
            }
        });
    }

    @Override
    public void showToolChooseCommand() {
        // non usare

    }

    @Override
    public void showToolCostCommand(List<Integer> toolCost, int indexTool) {
        // non usare

    }

    @Override
    public void showRoundTracker(RoundTracker roundTracker) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                board.updateRoundTracker(roundTracker);
            }
        });


    }

    @Override
    public void showFinalRank(List<Player> playerList) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    getConnection().setEndGameTimer(getPlayerID());
                    board.showRank(playerList);
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void showWinner() {
        //non usare
    }

    @Override
    public void showLosers() {
        // non usare
    }

    @Override
    public void showTimer() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                board.endTimer();
            }
        });

    }

    @Override
    public void showOtherTimer(String playerName) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                board.endOtherTimer(playerName);
            }
        });
    }

    @Override
    public void showToolCards(List<ToolCard> toolCardList) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                toolList = toolCardList;
            }
        });


    }

    @Override
    public void showTokenError() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                AlertBox.display("Error", "You have NOT enough tokens!");
            }
        });

    }

    @Override
    public void showGrozingRequest(int poolSize) {

    }

    @Override
    public void showGrozingCommand() {

    }

    @Override
    public void showEglomiseStart() {

    }

    @Override
    public void showEglomiseEnd() {

    }

    @Override
    public void showCopperFoilStart() {

    }

    @Override
    public void showCopperFoilEnd() {

    }

    @Override
    public void showLathekinStart() {

    }

    @Override
    public void showLathekinStartTwo() {

    }

    @Override
    public void showLathekinEnd() {

    }

    @Override
    public void showLathekinEndTwo() {

    }

    @Override
    public void showLensCutterRequest(int poolSize, List<Integer> round) {

    }

    @Override
    public void showLensCutterRound(List<Integer> round) {

    }

    @Override
    public void showLensCutterDice(List<Integer> round, int roundIndex) {

    }

    @Override
    public void showFluxBrushRequest(int poolSize) {

    }

    @Override
    public void showGlazingHammerRequest() {

    }

    @Override
    public void showRunningPliersPool(int poolSize) {

    }

    @Override
    public void showRunningPliersEnd() {

    }

    @Override
    public void showCorkBackedPool(int poolSize) {

    }

    @Override
    public void showCorkBackedEnd() {

    }

    @Override
    public void showGrindingStoneRequest(int poolSize) {

    }

    @Override
    public void showFluxRemoverPool(DiceColor color, int poolSize) {

    }

    @Override
    public void showFluxRemoverValue() {

    }

    @Override
    public void showTapWheelNumber() {

    }

    @Override
    public void showTapWheelStartOne() {

    }

    @Override
    public void showTapWheelEndOne() {

    }

    @Override
    public void showTapWheelStartTwo() {

    }

    @Override
    public void showTapWheelEndTwo() {

    }

    @Override
    public void showBoard(RoundTracker roundTracker, DraftPool draftPool) {

    }

    @Override
    public void showInvalidMove(String msg) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                AlertBox.display("Error", msg);
                board.textRemoveMsg();
            }
        });
    }




    //------------single  player----------------------------------------------------

    @Override
    public void showDifficultyRequest() {

    }

    @Override
    public void showPrivateSingle(List<PrivateObjectiveCard> publicList) {

    }

    @Override
    public void showToolSingleCommand(List<ToolCard> toolList, int poolSize) {

    }

    @Override
    public void showToolSingleChoose() {

    }

    @Override
    public void showToolSingleDice() {

    }

    @Override
    public void showMatchError() {

    }

    @Override
    public void showEndSinglePlayer(boolean winner, int playerPoints, int gameThreshold) {

    }


    //-------------switch scene method----------


    private void patternScene() throws IOException{
        ChoosePattern.setMainController(this);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CardDraw.fxml"));
        Parent root1 = (Parent) loader.load();

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        Scene scene = new Scene(root1, width, height);
        stage.setTitle("Sagrada Pattern Choose");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();

    }

    private void boardScene() throws IOException {

        BoardController.setMainController(this);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Board.fxml"));
        Parent root2 = (Parent) loader.load();
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        Scene board = new Scene(root2, width, height);
        stage.setTitle("Sagrada Main Board");
        stage.setScene(board);
        stage.setResizable(true);
        stage.show();
    }


}

