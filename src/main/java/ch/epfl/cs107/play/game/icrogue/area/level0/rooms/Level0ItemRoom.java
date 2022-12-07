package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.icrogue.actor.items.Item;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;

public abstract class Level0ItemRoom extends Level0Room {
    public Level0ItemRoom(DiscreteCoordinates roomCoordinates) {
        super(roomCoordinates);
        items = new ArrayList<>();
    }

    private final ArrayList<Item> items;

    public void addItem(Item item){
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
        if(!super.isOn()) // isOff marche pas pck ça boucle à l'infini
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
