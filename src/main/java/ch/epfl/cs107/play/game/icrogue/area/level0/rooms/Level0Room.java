package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.area.ConnectorInRoom;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.List;

public class Level0Room extends ICRogueRoom {

    /**
     * Enum representing the different connector's type in a level0 room
     */
    public enum Level0Connectors implements ConnectorInRoom {
        W(new DiscreteCoordinates(0, 4), new DiscreteCoordinates(8, 5),Orientation.LEFT),
        S(new DiscreteCoordinates(4, 0), new DiscreteCoordinates(5, 8),Orientation.DOWN),
        E(new DiscreteCoordinates(9, 4), new DiscreteCoordinates(1, 5),Orientation.RIGHT),
        N(new DiscreteCoordinates(4, 9), new DiscreteCoordinates(5, 1),Orientation.UP) ;

        //The connector's coordinates
        private final DiscreteCoordinates connectorCoordinates;

        //The connector's destination coordinate
        private final DiscreteCoordinates destCoordinates;

        //The connector orientation
        private final Orientation orientation;


        /**
         * Default Level0Connectors constructor
         * @param connectorCoordinates (DiscreteCoordinates) : The connector's coordinates
         * @param destCoordinates (DiscreteCoordinates) : The connector's destination coordinate
         * @param orientation (Orientation) : The connector orientation
         */
        Level0Connectors(DiscreteCoordinates connectorCoordinates, DiscreteCoordinates destCoordinates, Orientation orientation) {
            this.connectorCoordinates = connectorCoordinates;
            this.destCoordinates = destCoordinates;
            this.orientation = orientation;
        }


        /**
         * @return the ordinal of the connector's type
         */
        public int getIndex() {
            return ordinal();
        }

        /**
         * @return the destination of the connector's type
         */
        public DiscreteCoordinates getDestination() {
            return destCoordinates;
        }

        /**
         * @return the Orientation of the connector's type
         */
        private Orientation getOrientation(){
            return orientation;
        }

        /**
         * @return the Coordinates of the connector's type
         */
        private DiscreteCoordinates getConnectorCoordinates() {
            return connectorCoordinates;
        }

        /**
         * @return every orientation of the connectors, sorted by ordinal
         */
        private static List<Orientation> getAllConnectorsOrientation() {
            List<Orientation> result = new ArrayList<>();
            for(Level0Connectors connectors : values()){
                result.add(connectors.getOrientation());
            }
            return result;
        }

        /**
         * @return every position of the connectors, sorted by ordinal
         */
        private static List<DiscreteCoordinates> getAllConnectorsPosition(){
            List<DiscreteCoordinates> result = new ArrayList<>();
            for(Level0Connectors connectors : values()){
                result.add(connectors.getConnectorCoordinates());
            }
            return result;
        }
    }

    /**
     * Default Level0Room constructor
     * @param roomCoordinates (DiscreteCoordinates): the room coordinates
     */
    public Level0Room(DiscreteCoordinates roomCoordinates){
        super(Level0Connectors.getAllConnectorsPosition(), Level0Connectors.getAllConnectorsOrientation(),"icrogue/Level0Room", roomCoordinates);
    }

    @Override
    protected void createArea() {
        super.createArea();
        registerActor(new Background(this, getBehaviorName()));
    }


    @Override
    public String getTitle(){
        return "icrogue/level0" + getRoomCoordinates().x + getRoomCoordinates().y;
    }
}
