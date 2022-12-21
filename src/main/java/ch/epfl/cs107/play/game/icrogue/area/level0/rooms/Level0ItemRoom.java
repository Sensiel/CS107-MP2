package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.icrogue.actor.items.Item;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.List;

public abstract class Level0ItemRoom extends Level0Room {
    //list of the items presents in the room
    private final List<Item> items;

    /**
     * Default Level0ItemRoom constructor
     * @param roomCoordinates (DiscreteCoordinates) : the room's coordinates
     */
    protected Level0ItemRoom(DiscreteCoordinates roomCoordinates) {
        super(roomCoordinates);
        items = new ArrayList<>();
    }


    /**
     * Add an item to the room
     * @param item (Item): the item to add
     */
    protected void addItem(Item item){
        items.add(item);
    }

    @Override
    protected void createArea() {
        super.createArea();
        for(Item item : items) {
            if (!item.isCollected())
                registerActor(item);
        }
    }

    @Override
    public boolean isOn() {
        if(!super.isOn())
            return false;
        for(Item item : items) {
            if (!item.isCollected())
                return false;
        }
        return true;
    }
    @Override
    public boolean isOff() {
        return (!isOn());
    }
}
