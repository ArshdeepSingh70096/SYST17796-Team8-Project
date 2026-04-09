package ca.sheridancollege.project;

public class UnoCard extends Card {

    private String color;
    private String type;
    private int number;

    public UnoCard(String color, String type, int number) {
        this.color = color;
        this.type = type;
        this.number = number;
    }

    public String getColor() {
        return color;
    }

    public String getType() {
        return type;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        if (type.equals("Number")) {
            return color + " " + number;
        } else {
            return color + " " + type;
        }
    }
}