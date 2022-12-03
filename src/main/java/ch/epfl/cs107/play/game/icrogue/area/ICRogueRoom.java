package ch.epfl.cs107.play.game.icrogue.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;

public abstract class ICRogueRoom extends Area {
    private DiscreteCoordinates roomCoordinates;
    private String behaviorName;
    private ICRogueBehavior behavior;
    private ArrayList<Connector> tab;

    public ICRogueRoom(List<DiscreteCoordinates> connectorsCoordinates, List<Orientation> orientations,
                       String behaviorName, DiscreteCoordinates roomCoordinates){
        tab = new ArrayList<Connector>();
        for(int i = 0; i< connectorsCoordinates.size(); ++i){
            tab.add(new Connector(this, orientations.get(i), connectorsCoordinates.get(i)));
        }
        this.behaviorName = behaviorName;
        this.roomCoordinates = roomCoordinates;
    }



    public String getBehaviorName(){
        return behaviorName;
    }

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

    protected void createArea() {
        for(int i = 0; i < tab.size(); ++i) {
            registerActor(tab.get(i));
        }
    }
    public final float getCameraScaleFactor() {
        return ICRogue.CAMERA_SCALE_FACTOR;
    }

    public DiscreteCoordinates getRoomCoordinates() {
        return roomCoordinates;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Keyboard keyboard = this.getKeyboard();

        if (keyboard.get(Keyboard.O).isPressed()) {
            for (Connector connector : tab) {
                connector.setState(Connector.State.OPEN);
                connector.setKeyID(Connector.NO_KEY_ID);
            }
        }
        if(keyboard.get(Keyboard.L).isPressed()){
            tab.get(0).setState(Connector.State.LOCKED);
            tab.get(0).setKeyID(1);
        }
        if(keyboard.get(Keyboard.T).isPressed()) {
            for (Connector connector : tab) {
                if (connector.getState().equals(Connector.State.CLOSED)) {
                    connector.setState(Connector.State.OPEN);

                } else if (connector.getState().equals(Connector.State.OPEN)) {
                    connector.setState(Connector.State.CLOSED);
                }
            }
        }
    }
}

