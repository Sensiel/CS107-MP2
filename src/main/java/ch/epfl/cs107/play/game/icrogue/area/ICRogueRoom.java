package ch.epfl.cs107.play.game.icrogue.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;

public abstract class ICRogueRoom extends Area implements Logic {
    private final DiscreteCoordinates roomCoordinates;
    private final String behaviorName;
    private final List<Connector> tab;
    private ICRogueBehavior behavior;
    private boolean isRoomVisited;

    protected ICRogueRoom(List<DiscreteCoordinates> connectorsCoordinates, List<Orientation> orientations,
                       String behaviorName, DiscreteCoordinates roomCoordinates){
        tab = new ArrayList<>();
        for(int i = 0; i< connectorsCoordinates.size(); ++i){
            tab.add(new Connector(this, orientations.get(i).opposite(), connectorsCoordinates.get(i)));
        }
        this.behaviorName = behaviorName;
        this.roomCoordinates = roomCoordinates;
        isRoomVisited = false ;
    }
    protected String getBehaviorName(){
        return behaviorName;
    }

    public void setRoomVisited(boolean value){
        this.isRoomVisited = value;
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
        for (Connector connector : tab) {
            registerActor(connector);
        }
    }
    public final float getCameraScaleFactor() {
        return ICRogue.CAMERA_SCALE_FACTOR;
    }

    public DiscreteCoordinates getRoomCoordinates() {
        return roomCoordinates;
    }

    public void setConnectorDestination(ConnectorInRoom connector, String dest){
        tab.get(connector.getIndex()).setDestTitle(dest);
        tab.get(connector.getIndex()).setPosDest(connector.getDestination());
    }

    public void setConnectorState(ConnectorInRoom connector, Connector.State state){
        tab.get(connector.getIndex()).setState(state);
    }

    public void setConnectorKeyID(ConnectorInRoom connector, int keyID){
        tab.get(connector.getIndex()).setKeyID(keyID);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (isOn()) {
            for (Connector connector : tab) {
                if (connector.getState().equals(Connector.State.CLOSED)) {
                    connector.setState(Connector.State.OPEN);
                }
            }
        }

        /*
        Keyboard keyboard = this.getKeyboard();

        if (keyboard.get(Keyboard.O).isPressed()) {
            for (Connector connector : tab) {
                connector.setState(Connector.State.OPEN);
            }
        }
        if (keyboard.get(Keyboard.L).isPressed()) {
            tab.get(0).setState(Connector.State.LOCKED);
            tab.get(0).setKeyID(1);
        }

         */
    }

    @Override
    public boolean isOn() {
        return isRoomVisited;
    }

    @Override
    public boolean isOff() {
        return (!isOn());
    }

    @Override
    public float getIntensity() {
        return 0;
    }
}

