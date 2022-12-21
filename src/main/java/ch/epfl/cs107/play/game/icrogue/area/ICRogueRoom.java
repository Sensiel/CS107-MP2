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
    //The coordinates of the room
    private final DiscreteCoordinates roomCoordinates;

    // The name of the file containing the behavior of the room
    private final String behaviorName;

    //The list of Connector present in this room
    private final List<Connector> tab;

    //The room's behavior
    private ICRogueBehavior behavior;

    //Boolean indicating if the room has been visited already
    private boolean isRoomVisited;

    /**
     * Default ICRogueRoom constructor
     * @param connectorsCoordinates  (List<DiscreteCoordinates>) : The coordinates of the connectors in the room
     * @param orientations (List<Orientation>) : The orientation of the connectors in the room
     * @param behaviorName (String) : The behavior of the room
     * @param roomCoordinates (DiscreteCoordinates) : the coordinates of the room in the level
     */
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

    /**
     * @return the behavior name
     */
    protected String getBehaviorName(){
        return behaviorName;
    }

    /**
     * Set isRoomVisited to a certain value
     * @param value : boolean telling if the room is visited or not
     */
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


    /**
     * Method registering and configuring every actor of the room
     */
    protected void createArea() {
        for (Connector connector : tab) {
            registerActor(connector);
        }
    }

    /**
     * @return the Camera scale factor
     */
    public final float getCameraScaleFactor() {
        return ICRogue.CAMERA_SCALE_FACTOR;
    }

    /**
     * @return the room's coordinates
     */
    public DiscreteCoordinates getRoomCoordinates() {
        return roomCoordinates;
    }

    /**
     * Set a connector's destination ( room and position in the room ) given its type and destination
     * @param connector (ConnectorInRoom) : The type of connector
     * @param dest (String) : The destination title
     */
    public void setConnectorDestination(ConnectorInRoom connector, String dest){
        tab.get(connector.getIndex()).setDestTitle(dest);
        tab.get(connector.getIndex()).setPosDest(connector.getDestination());
    }

    /**
     * Set a connector's state given its type and state
     * @param connector (ConnectorInRoom): The type of connector
     * @param state (Connector.State) : The connector's state
     */
    public void setConnectorState(ConnectorInRoom connector, Connector.State state){
        tab.get(connector.getIndex()).setState(state);
    }

    /**
     * Set the key required for a specific connector given its type and key id
     * @param connector (ConnectorInRoom): The type of connector
     * @param keyID (int) : The key id
     */
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

