package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.icrogue.actor.items.Item;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;

public abstract class Level0ItemRoom extends Level0Room {
    public Level0ItemRoom(DiscreteCoordinates roomCoordinates) {
        super(roomCoordinates);
        items = new ArrayList<>();
        collectedItems = new ArrayList<>();
    }

    private final ArrayList<Item> items;

    private ArrayList<Item> collectedItems;

    public void addItem(Item item){
        items.add(item);
    }
    public void addCollectedItems(Item item){collectedItems.add(item);}

    @Override
    protected void createArea() {
        super.createArea();
        for(Item item : items){
            registerActor(item);
        }
    }
    /*
    @Override
    public boolean isOn() {
        super.isOn();
        if(collectedItems.size() == items.size()){
            return true;
        } else { return false; }
    }
    @Override
    public boolean isOff() {
        return (!isOn());
    }

     */
}
