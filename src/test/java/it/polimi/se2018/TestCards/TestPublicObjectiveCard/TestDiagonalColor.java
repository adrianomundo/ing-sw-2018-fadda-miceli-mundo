/*package it.polimi.se2018.TestCards.TestPublicObjectiveCard;

import it.polimi.se2018.model.Cards.PublicObjectiveCard.DiagonalColor;
import it.polimi.se2018.model.Cards.PublicObjectiveCard.PublicObjectiveCard;
import it.polimi.se2018.model.Components.Dice;
import it.polimi.se2018.model.Components.DiceColor;
import it.polimi.se2018.Exceptions.InvalidMoveException;
import it.polimi.se2018.model.Cards.*;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

public class TestDiagonalColor {
   @Test
    public void testRunPublic() throws InvalidMoveException{
        PatternCard pattern = new PatternCard();
        try {
            Dice dice = new Dice(5, DiceColor.BLUE);
            ArrayList<PatternCard> patternList = pattern.loadPatternList();
            pattern = patternList.get(4);
            pattern.putDiceOnPattern(dice, 0, pattern);
            PublicObjectiveCard publicCard = new PublicObjectiveCard(new DiagonalColor());
            assertEquals(0, publicCard.executeEffect(pattern));


        } catch(FileNotFoundException e) {
            fail();
        }

    }
}*/
