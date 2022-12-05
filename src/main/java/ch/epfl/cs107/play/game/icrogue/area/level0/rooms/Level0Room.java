package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.items.Cherry;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.game.icrogue.area.ConnectorInRoom;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.List;

public class Level0Room extends ICRogueRoom {


    public enum Level0Connectors implements ConnectorInRoom {
        W(new DiscreteCoordinates(0, 4), new DiscreteCoordinates(8, 5),Orientation.LEFT),
        S(new DiscreteCoordinates(4, 0), new DiscreteCoordinates(5, 8),Orientation.DOWN),
        E(new DiscreteCoordinates(9, 4), new DiscreteCoordinates(1, 5),Orientation.RIGHT),
        N(new DiscreteCoordinates(4, 9), new DiscreteCoordinates(5, 1),Orientation.UP) ;

        private DiscreteCoordinates connectorCoordinates;
        private DiscreteCoordinates destCoordinates;
        private Orientation orientation;


        Level0Connectors(DiscreteCoordinates connectorCoordinates, DiscreteCoordinates destCoordinates, Orientation orientation) {
            this.connectorCoordinates = connectorCoordinates;
            this.destCoordinates = destCoordinates;
            this.orientation = orientation;
        }

        public int getIndex() {
            return ordinal();
        }

        public DiscreteCoordinates getDestination() {
            return destCoordinates;
        }
        public Orientation getOrientation(){
            return orientation;
        }

        public DiscreteCoordinates getConnectorCoordinates() {
            return connectorCoordinates;
        }

        public static List<Orientation> getAllConnectorsOrientation() {
            ArrayList<Orientation> result = new ArrayList<>();
            for(Level0Connectors connectors : values()){
                result.add(connectors.getOrientation());
            }
            return result;
        }

        public static List<DiscreteCoordinates> getAllConnectorsPosition(){
            ArrayList<DiscreteCoordinates> result = new ArrayList<>();
            for(Level0Connectors connectors : values()){
                result.add(connectors.getConnectorCoordinates());
            }
            return result;
        }


    }

    public Level0Room(DiscreteCoordinates roomCoordinates){
        super(Level0Connectors.getAllConnectorsPosition(), Level0Connectors.getAllConnectorsOrientation(),"icrogue/Level0Room", roomCoordinates);
    }



    protected void createArea() {
        super.createArea();
        // Base

        registerActor(new Background(this, getBehaviorName()));
        /*registerActor(new Cherry(this, Orientation.DOWN,new DiscreteCoordinates(6,3)));
        registerActor(new Staff(this,Orientation.DOWN,new DiscreteCoordinates(4,3)));*/
    }


    public String getTitle(){
        return "icrogue/level0" + getRoomCoordinates().x + getRoomCoordinates().y;
    }
}
