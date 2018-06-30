package it.polimi.se2018.TestComponents;

import it.polimi.se2018.exceptions.InvalidMoveException;
import it.polimi.se2018.server.model.Cards.PatternCard;
import it.polimi.se2018.server.model.Cards.PrivateObjectiveCard;
import it.polimi.se2018.server.model.Cards.PublicObjectiveCard.DiagonalColor;
import it.polimi.se2018.server.model.Cards.PublicObjectiveCard.DifferentColorColumn;
import it.polimi.se2018.server.model.Cards.PublicObjectiveCard.PublicEffects;
import it.polimi.se2018.server.model.Cards.PublicObjectiveCard.PublicObjectiveCard;
import it.polimi.se2018.server.model.Components.*;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestModel {

    @Test
    public void testConstructor() {
        Model model = new Model();

        assertEquals(0, model.getNumberPlayer());
        assertEquals(20, model.getTimeToPlay());
    }

    @Test
    public void testUpdate() {
        Model model = new Model();

        ArrayList<Player> playerArrayList = new ArrayList<>();
        Player player = new Player();

        Player player1  = new Player();
        player.setPlayerID(0);
        player.setPlayerID(1);
        playerArrayList.add(player);
        playerArrayList.add(player1);
        model.getPlayerFromID(0);
        model.setPlayerList(playerArrayList);
        model.getPlayerFromID(1);
        model.setNumberPlayer(5);
        model.updateBoardAndNotify();
        model.updateTokenAndNotify(1);
        model.updatePatternAndNotify(0);
        model.updatePoolAndNotify();
    }

    @Test
    public void testGetter(){
        Model model = new Model();

       // model.getPlayer();
        model.getDraftPool();
        model.getRoundTracker();
        model.getPlayerList();
        model.getNumberPlayer();
        model.getDiceBag();
        model.getPublicList();
        model.getPlayerFromID(0);
        ArrayList<Player> playerArrayList = new ArrayList<>();
        Player player = new Player();
        Player player1  = new Player();
        player.setPlayerID(0);
        player.setPlayerID(1);
        playerArrayList.add(player);
        playerArrayList.add(player1);
        model.getPlayerFromID(0);
        model.setPlayerList(playerArrayList);
        model.getPlayerFromID(1);

        ArrayList<PublicObjectiveCard> publicObjectiveCards = new ArrayList<>();
        PublicObjectiveCard publicObjectiveCard = new PublicObjectiveCard(new DiagonalColor(), "Color Diagonals");
        PublicObjectiveCard publicObjectiveCard1 = new PublicObjectiveCard(new DifferentColorColumn(), "Column Color Variety");
        publicObjectiveCards.add(publicObjectiveCard);
        publicObjectiveCards.add(publicObjectiveCard1);

        model.setPublicAndNotify(publicObjectiveCards);
        model.getPublicList();

    }

    @Test
    public void testNotify() throws FileNotFoundException, InvalidMoveException {
        Model model = new Model();
        ArrayList<Player> playerArrayList = new ArrayList<>();
        Player player = new Player();
        Player player1  = new Player();

        PrivateObjectiveCard privateObjectiveCard = new PrivateObjectiveCard(DiceColor.RED);
        PrivateObjectiveCard privateObjectiveCard1 = new PrivateObjectiveCard(DiceColor.RED);
        ArrayList<PrivateObjectiveCard> privateObjectiveCardArrayList = new ArrayList<>();
        privateObjectiveCardArrayList.add(privateObjectiveCard);
        privateObjectiveCardArrayList.add(privateObjectiveCard1);

        PatternCard patternCard = new PatternCard();
        patternCard = patternCard.loadPatternList().get(0);

        Dice dice = new Dice(3, DiceColor.YELLOW);
        Dice dice1 = new Dice(3, DiceColor.YELLOW);
        //patternCard.putDiceOnPattern(dice, 0, patternCard);
        player1.setPattern(patternCard);

        ArrayList<Dice> diceArrayList = new ArrayList<>();
        diceArrayList.add(dice);
        diceArrayList.add(dice1);
        DiceBag diceBag = new DiceBag();
        DraftPool draftPool = new DraftPool(diceBag);
        //draftPool.setDraftPool(diceArrayList);

        player.setPlayerID(0);
        player1.setPlayerID(1);
        playerArrayList.add(player);
        playerArrayList.add(player1);
        model.setPlayerList(playerArrayList);


        model.setPlayerAndNotify(1, "ciao");
        //model.setPatternAndNotify(1, patternCard);
        model.setPrivateAndNotify(1, privateObjectiveCard);
        model.setTokenAndNotify(1);
        //model.setDraftPoolAndNotify(false);
        //model.setDraftPoolAndNotify(true);
        model.setEndRoundAndNotify();
        //model.setPatternAndNotify(1, 0);
        model.setFinalPointsAndNotify(playerArrayList);
        model.setPrivateSinglePlayerAndNotify(privateObjectiveCardArrayList);
        //model.setMoveAndNotify(1,0,0);

    }

    @Test
    public void testSetPatternAndMoveNotify() throws FileNotFoundException, InvalidMoveException {
        ArrayList<Player> playerArrayList = new ArrayList<>();
        Player player = new Player();
        Player player1  = new Player();
        playerArrayList.add(player);
        playerArrayList.add(player1);
        player.setPlayerID(0);
        PatternCard patternCard = new PatternCard();
        ArrayList<PatternCard> patternCards = patternCard.loadPatternList();

        player.setPatterChooseList(patternCards);

        Model model = new Model();
        model.setPlayerList(playerArrayList);
        model.setPatternAndNotify(0, 1);

        //assertEquals("Kaleidoscopic Dream", player.getPatterChooseList().get(0).getName() );


        ArrayList<Dice> diceArrayList = new ArrayList<>();
        Dice dice = new Dice(4, DiceColor.YELLOW);
        diceArrayList.add(dice);
        DraftPool draftPool = new DraftPool();
        //draftPool.setDraftPool(diceArrayList);
        model.getDraftPool().setDraftPool(diceArrayList);

        model.setMoveAndNotify(0,0,0);
        PatternCard patternCard1 = patternCards.get(0);
        model.setCustomPatternAndNotify(0, patternCard1);

    }
}
