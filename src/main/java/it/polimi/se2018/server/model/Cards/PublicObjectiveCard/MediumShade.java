package it.polimi.se2018.server.model.Cards.PublicObjectiveCard;

import it.polimi.se2018.server.model.Cards.PatternCard;
import it.polimi.se2018.server.model.Components.GlassBox;
import static java.lang.Math.min;
import java.util.Iterator;

/**
 * class used to count the number of points
 * @author Dario Miceli
 */
public class MediumShade implements PublicEffects {
    private final static int VPOINTS = 2;

    @Override
    public int RunPublic(PatternCard pattern){
        int points;
        int setOf3= 0;
        int setOf4 = 0;
        GlassBox box;

        Iterator<GlassBox> it = pattern.getPattern().iterator();
        while (it.hasNext()) {
            box = it.next();
            if(!box.isBoxEmpty()) {
                if (box.getDice().getValue() == 3) {
                    setOf3++;
                }
                if (box.getDice().getValue() == 4) {
                    setOf4++;
                }
            }
        }
        points = min(setOf3, setOf4);
        points = points * VPOINTS;

        return points;
    }

}

