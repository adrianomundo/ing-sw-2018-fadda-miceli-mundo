package it.polimi.se2018.server.controller;


import it.polimi.se2018.server.VirtualView;
import it.polimi.se2018.server.model.Cards.PatternCard;
import it.polimi.se2018.server.model.Components.Model;
import it.polimi.se2018.server.model.Components.Player;
import it.polimi.se2018.server.model.Events.ClientServer.PlayerNameEvent;
import it.polimi.se2018.server.model.Events.ClientServer.PlayerPatternEvent;
import it.polimi.se2018.server.model.Events.ServerClient.ControllerView.GameStartedEvent;
import it.polimi.se2018.server.model.Events.ServerClient.ControllerView.RollDraftPoolEvent;
import it.polimi.se2018.server.model.Events.ServerClient.ControllerView.StartRoundEvent;
import it.polimi.se2018.server.model.Events.ServerClient.ControllerView.StartTurnEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Game implements Observer {

    private static final int STARTTURN = 0;
    private static final int START = 1;
    private Model model;
    private List<Player> playerList;
    private List<VirtualView> viewGame;
    private GameSetup setup;
    private static int turn = STARTTURN;
    private static int round = START;
    // add timer

    public Game(List<VirtualView> viewList) {

        this.model = new Model();
        this.viewGame = new ArrayList<>(viewList);
        this.playerList = new ArrayList<>();
        this.setup = new GameSetup(this);

        for (VirtualView view: viewGame) {
            Player player = new Player(view.getPlayerID());
            System.out.println("Player id in new Game" + player.getPlayerID());
            playerList.add(player);
        }

        model.setPlayerList(playerList);

        for (VirtualView view : viewGame) {
            view.addObserver(this);
            view.setModel(model);
            model.addObserver(view);
        }

        startGame();



    }


    // ogni metodo che modifica il model deve essere gestito da update (unico metodo pubblico), e chiamare il metodo
    // protected relativo al cambiamento
    // update gestisce

    protected Model getModel(){
        return model;
    }

    protected List<VirtualView> getViewGame(){
        return viewGame;
    }






    //------------------- update in base alla notify della view-------------------
    @Override
    public void update(Observable o, Object arg){

        VirtualView virtualView = (VirtualView) o;

        if (arg instanceof PlayerNameEvent) {
            setPlayerNameModel(virtualView, ((PlayerNameEvent) arg).getName());
        }

        if (arg instanceof PlayerPatternEvent){
            setPatternCardModel(virtualView, ((PlayerPatternEvent) arg).getCard());
        }



    }
















    //--------metodi che modificano model e mandano la notify alla view----------
    protected void setPlayerNameModel(VirtualView view, String name) {

        //model.getPlayerFromID(view.getPlayerID()).setPlayerName(name);
        model.setPlayerAndNotify((view.getPlayerID()), name);

        if(model.getNumberPlayer() == (viewGame.size())){
            startCard();
        }
        //System.out.println("Sto modificando model" + view.getPlayerID() + "name" + name);
    }

    protected void setPatternCardModel(VirtualView view, PatternCard pattern){

        model.setPatternAndNotify(view.getPlayerID(), pattern);

        setTokensModel(view);

        if(model.getNumberPlayer() == (getViewGame().size())){
            startTurn();
        }

    }

    protected void setTokensModel(VirtualView view) {

        model.setTokenAndNotify(view.getPlayerID());


    }



    //---------------------------------logica applicativa---------------------------

    private void startGame() {

        model.setNumberPlayer(0);
        for (VirtualView view : viewGame) {
            view.sendEvent(new GameStartedEvent(true));
        }

    }

    private void startCard(){

        model.setNumberPlayer(0);
        setup.setPublicCardModel();
        for(VirtualView view : viewGame){
            setup.setPrivateCardModel(view);
            setup.startPatternCard(view);
        }

    }

    private void startTurn(){
        if(turn == 0){
            for (VirtualView view : viewGame) {
                view.sendEvent(new StartRoundEvent(round));
                int currID = setup.calculatePlayerTurn(turn, viewGame.size());
                view.sendEvent(new StartTurnEvent(currID, this.model.getPlayerFromID(currID).getPlayerName()));
                view.sendEvent(new RollDraftPoolEvent(setup.calculatePlayerTurn(turn, viewGame.size())));

            }

        }
    }

    private void Move(){

    }

    private void Tool(){

    }

    private void endRound(){


    }

    private void endMatch(){

    }
}