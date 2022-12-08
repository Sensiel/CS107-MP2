package ch.epfl.cs107.play.game.icrogue.actor.items;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public abstract class Item extends CollectableAreaEntity {

    //Sprite of the item
    private Sprite item;

    /**
     * Default Item constructor
     * @param area (Area): Owner area. Not null
     * @param position (Coordinate): Initial position of the item. Not null
     * @param orientation (Orientation): Initial orientation of the item. Not null
     */
    public Item(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
    }

    /**
     * set a Sprite to represent the item
     * @param sprite (Sprite) : The sprite that represents the item. Not null
     */
    public void setSprite(Sprite sprite){
        item = sprite;
    }


    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        if(!isCollected()){
            item.draw(canvas);
        }
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }
}
