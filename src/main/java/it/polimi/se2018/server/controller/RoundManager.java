package it.polimi.se2018.server.controller;

import it.polimi.se2018.server.model.Cards.PatternCard;
import it.polimi.se2018.server.model.Cards.PrivateObjectiveCard;
import it.polimi.se2018.server.model.Cards.PublicObjectiveCard.PublicObjectiveCard;
import it.polimi.se2018.server.model.Components.DraftPool;
import it.polimi.se2018.server.model.Components.Player;
import it.polimi.se2018.server.model.Components.RoundTracker;

import java.util.*;

public class RoundManager  {




    public ArrayList<Integer> calculatePrivate(ArrayList<Player> playerArrayList) {
        ArrayList<Integer> results = new ArrayList<>();

        for (int i = 0; i < playerArrayList.size(); i++) {
            int result;
            result = playerArrayList.get(i).getPrivate().RunPrivate(playerArrayList.get(i).getPattern());
            playerArrayList.get(i).setPrivatePoints(result);
            results.add(result);
        }
        return results;

    }

    public ArrayList<Integer> calculateTokens(ArrayList<Player> playerArrayList) {
        ArrayList<Integer> results = new ArrayList<>();

        for (int i = 0; i < playerArrayList.size(); i++) {
            int result;
            result = playerArrayList.get(i).getTokensNumber();
            results.add(result);
        }
        return results;

    }

    public ArrayList<Integer> calculateEmptyBox(ArrayList<Player> playerArrayList) {
        PatternCard result;
        ArrayList<Integer> results = new ArrayList<>();

        for (int i = 0; i < playerArrayList.size(); i++) {
            result = playerArrayList.get(i).getPattern();
            int boxEmptyCounter=0;

            for (int j = 0; j < result.getPattern().size(); j++) {
                if(result.getPattern().get(j).isBoxEmpty()) {
                    boxEmptyCounter++;
                }

            }

            results.add(boxEmptyCounter);
        }
        return results;

    }

    public ArrayList<Integer> calculatePublic(ArrayList<Player> playerArrayList, ArrayList<PublicObjectiveCard> publicObjectiveCardArrayList) {
        ArrayList<Integer> results = new ArrayList<>();

        for (int i = 0; i < playerArrayList.size(); i++) {
            PatternCard result;
            int personalResult=0;
            result = playerArrayList.get(i).getPattern();
            for (int j = 0; j < publicObjectiveCardArrayList.size(); j++) {
                personalResult = personalResult + publicObjectiveCardArrayList.get(j).executeEffect(result);

            }
            results.add(personalResult);

            }
        return results;

    }

 public ArrayList<Player> calculatePoints (ArrayList<Player> playerArrayList, ArrayList<PublicObjectiveCard> publicObjectiveCardArrayList ) {
        ArrayList<Integer> result1;
        ArrayList<Integer> result2;
        ArrayList<Integer> result3;
        ArrayList<Integer> result4;



        result1 = calculateEmptyBox(playerArrayList);
        result2 = calculatePrivate(playerArrayList);
        result3 = calculateTokens(playerArrayList);
        result4 = calculatePublic(playerArrayList, publicObjectiveCardArrayList );

        for (int i=0; i < result1.size(); i++) {
            int sum;
            sum = result4.get(i) + result2.get(i) + result3.get(i) - result1.get(i);
            playerArrayList.get(i).setFinalPoints(sum);
        }

        return playerArrayList;

     }
        //TODO aggiungere ultima funzionalità dopo aver creato round
 public ArrayList<Player> checkPoints (ArrayList<Player> playerArrayList) {
        for (int k=0; k < playerArrayList.size(); k++) {
            for (int i = 0; i < playerArrayList.size() - 1; i++) {
                if (playerArrayList.get(i).getFinalPoints() == playerArrayList.get(i + 1).getFinalPoints()) {

                    if (playerArrayList.get(i).getPrivatePoints() < playerArrayList.get(i + 1).getPrivatePoints()) {

                        Player player = new Player(playerArrayList.get(i));

                        playerArrayList.set(i, playerArrayList.get(i + 1));
                        playerArrayList.set(i + 1, player);


                    }
                }
            }
        }
     for (int k=0; k < playerArrayList.size(); k++) {
            for (int i = 0; i < playerArrayList.size() - 1; i++) {
                if (playerArrayList.get(i).getPrivatePoints() == playerArrayList.get(i + 1).getPrivatePoints()) {

                    if (playerArrayList.get(i).getTokensNumber() < playerArrayList.get(i + 1).getTokensNumber()) {

                        Player player = new Player(playerArrayList.get(i));

                        playerArrayList.set(i, playerArrayList.get(i + 1));
                        playerArrayList.set(i + 1, player);


                    }
                }
            }

        }
     return playerArrayList;
 }

 public ArrayList<Player> calculateWinner (ArrayList<Player> playerArrayList, ArrayList<PublicObjectiveCard> publicObjectiveCardArrayList) {
        ArrayList<Player> unsortedPlayers;
        ArrayList<Player> sortedPlayers;


        unsortedPlayers = calculatePoints( playerArrayList, publicObjectiveCardArrayList );


        Collections.sort(unsortedPlayers, new PointsComparator());
        sortedPlayers = checkPoints(unsortedPlayers);


        return  sortedPlayers;
    }




    public int calculatePrivateSinglePlayer(Player player, ArrayList<PrivateObjectiveCard> privateObjectiveCards) {
        int results=0;

        for (int i = 0; i <privateObjectiveCards.size(); i++) {
            int result;
            result = privateObjectiveCards.get(i).RunPrivate(player.getPattern());
            results = results + result;

        }
        player.setPrivatePoints(results);
        return results;

    }

    public int calculatePublicSinglePlayer(Player player, ArrayList<PublicObjectiveCard> publicObjectiveCardArrayList) {
        int results=0;

            for (int j = 0; j < publicObjectiveCardArrayList.size(); j++) {
                int result;
                result = publicObjectiveCardArrayList.get(j).executeEffect(player.getPattern());
                results = results + result;
            }

        return results;

    }

    public int calculateEmptyBoxSinglePLayer(Player player) {
        int boxEmptyCounter=0;
        for (int i=0; i < player.getPattern().getPattern().size(); i++) {

            if (player.getPattern().getPattern().get(i).isBoxEmpty()) {
                boxEmptyCounter++;
            }
        }
        boxEmptyCounter = boxEmptyCounter * 3;

        return boxEmptyCounter;
    }

    public  int calculatePointsRoundTrackerSinglePlayer(RoundTracker roundTracker) {
        int sum=0;
        int finalSum=0;

        for (int i=0; i < 10; i++) {
            for (int j=0; j< roundTracker.getRoundDice(i).size(); j++ ) {
                sum = roundTracker.getRoundDice(i).get(j).getValue();
                finalSum = finalSum + sum;
            }
        }
        return finalSum;
    }

    public int calculateWinnerSinglePlayer(Player player, ArrayList<PublicObjectiveCard> publicObjectiveCardArrayList, ArrayList<PrivateObjectiveCard> privateObjectiveCardArrayList, RoundTracker roundTracker ){
        int i = calculateEmptyBoxSinglePLayer(player);
        int j = calculatePrivateSinglePlayer(player, privateObjectiveCardArrayList);
        int k = calculatePublicSinglePlayer(player, publicObjectiveCardArrayList);
        int roundTrackerPoints = calculatePointsRoundTrackerSinglePlayer(roundTracker);

        int totalPoints = j + k - i;

        if (totalPoints > roundTrackerPoints ) {
            return 1;
        }
        else return 0;

    }


}



