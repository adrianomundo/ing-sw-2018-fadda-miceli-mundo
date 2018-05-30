package it.polimi.se2018.server.model.Components;

/**
 * Class Dice: the die
 * @author Salvatrore Fadda
 */
public class Dice {

    private final int DEFAULT = 0;
    private int value;
    private DiceColor color;

    /**
     * Default class constructor
     */
    public Dice() {
        this.value = DEFAULT;
        this.color = null;
    }

    /**
     * Class constructor, create a die with face number and colour specified
     * @param value die face number
     * @param color die colour
     */
    public Dice (int value, DiceColor color) {
        this.value = value;
        this.color = color;
    }

    /**
     * Get die colour
     * @return die colour
     */
    public DiceColor getColor ()
    {
        return color;
    }

    /**
     * Get die face number
     * @return die face number
     */
    public int getValue ()
    {
        return value;
    }

    /**
     * Set die face number
     * @param value die face number
     */
    public void setValue(int value)
    {
            this.value = value;
    }

    /**
     * Set die colour
     * @param color  die colour
     */
    public void setColor(DiceColor color)
    {
        this.color = color;
    }


    @Override
    public String toString() {
        System.out.println("Dice Color: " + getColor().toString());
        System.out.println("Dice value: " + getValue());
        return "";
    }
    public static void main(String args[])  {
        Dice dice = new Dice();
        dice.setColor(DiceColor.YELLOW);
        dice.setValue(4);
        dice.toString();
    }
}
