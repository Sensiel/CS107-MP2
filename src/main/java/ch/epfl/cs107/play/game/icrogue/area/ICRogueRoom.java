package ch.epfl.cs107.play.game.icrogue.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public abstract class ICRogueRoom extends Area {
    private DiscreteCoordinates roomCoordinates;
    private String behaviorName;

    private ICRogueBehavior behavior;
    public ICRogueRoom(String behaviorName, DiscreteCoordinates roomCoordinates) {
        this.behaviorName = behaviorName;
        this.roomCoordinates = roomCoordinates;
    }
    public String getBehaviorName(){return behaviorName;}

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            // Set the behavior map
            behavior = new ICRogueBehavior(window, getBehaviorName());
            setBehavior(behavior);
            createArea();
            return true;
        }
        return false;
    }
    protected abstract void createArea();


    public final float getCameraScaleFactor() {
        return ICRogue.CAMERA_SCALE_FACTOR;
    }
}

