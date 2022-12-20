package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
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

        private final DiscreteCoordinates connectorCoordinates;
        private final DiscreteCoordinates destCoordinates;
        private final Orientation orientation;


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
        private Orientation getOrientation(){
            return orientation;
        }

        private DiscreteCoordinates getConnectorCoordinates() {
            return connectorCoordinates;
        }

        private static List<Orientation> getAllConnectorsOrientation() {
            List<Orientation> result = new ArrayList<>();
            for(Level0Connectors connectors : values()){
                result.add(connectors.getOrientation());
            }
            return result;
        }

        private static List<DiscreteCoordinates> getAllConnectorsPosition(){
            List<DiscreteCoordinates> result = new ArrayList<>();
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
        registerActor(new Background(this, getBehaviorName()));
    }


    public String getTitle(){
        return "icrogue/level0" + getRoomCoordinates().x + getRoomCoordinates().y;
    }
}
