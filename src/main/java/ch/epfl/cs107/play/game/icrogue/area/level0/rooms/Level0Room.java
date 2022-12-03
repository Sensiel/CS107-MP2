package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.actor.items.Cherry;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.game.icrogue.area.ConnectorInRoom;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;

import java.util.ArrayList;
import java.util.List;

public class Level0Room extends ICRogueRoom {
    public enum Level0Connectors implements ConnectorInRoom {
        W(new DiscreteCoordinates(0, 4),new DiscreteCoordinates(8, 5),Orientation.LEFT),
        S(new DiscreteCoordinates(4, 0), new DiscreteCoordinates(5, 8),Orientation.DOWN),
        E(new DiscreteCoordinates(9, 4), new DiscreteCoordinates(1, 5),Orientation.RIGHT),
        N(new DiscreteCoordinates(4, 9), new DiscreteCoordinates(5, 1),Orientation.UP) ;

        private DiscreteCoordinates connectorCoordinates;
        private DiscreteCoordinates roomCoordinates;
        private Orientation orientation;


        Level0Connectors(DiscreteCoordinates connectorCoordinates, DiscreteCoordinates roomCoordinates, Orientation orientation) {
            this.connectorCoordinates=connectorCoordinates;
            this.roomCoordinates=roomCoordinates;
            this.orientation=orientation;
        }

        @Override
        public int getIndex() {
            return ordinal();
        }

        @Override
        public DiscreteCoordinates getDestination() {
            return connectorCoordinates;
        }
        public Orientation getOrientation(){
            return orientation;}

        public static List<Orientation> getAllConnectorsOrientation() {
            ArrayList result = new ArrayList<Orientation>();
            result.add(W.getOrientation());
            result.add(S.getOrientation());
            result.add(E.getOrientation());
            result.add(N.getOrientation());
            return result;
            } // c'est merdique comme encapsulation, tu sais comment je peux améliorer ?
        public static List<DiscreteCoordinates> getAllConnectorsPosition(){
            ArrayList result = new ArrayList<Orientation>();
            result.add(W.getDestination());
            result.add(S.getDestination());
            result.add(E.getDestination());
            result.add(N.getDestination());
            return result;
        } // same :/
    }

    public Level0Room(DiscreteCoordinates roomCoordinates){
        super(Level0Connectors.getAllConnectorsPosition(), Level0Connectors.getAllConnectorsOrientation(),"icrogue/Level0Room", roomCoordinates);
    }



    protected void createArea() {
        super.createArea();
        // Base

        registerActor(new Background(this, getBehaviorName()));
        registerActor(new Cherry(this, Orientation.DOWN,new DiscreteCoordinates(6,3)));
        registerActor(new Staff(this,Orientation.DOWN,new DiscreteCoordinates(4,3)));
    }


    public String getTitle(){
        return "icrogue/level0" + getRoomCoordinates().x + getRoomCoordinates().y;
    }
}
