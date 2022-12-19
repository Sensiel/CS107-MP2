package ch.epfl.cs107.play.game.icrogue.area.level0;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.area.Level;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0KeyRoom;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0StaffRoom;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0TurretRoom;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level0 extends Level {
    public enum RoomType {
        TURRET_ROOM(10),
        STAFF_ROOM(4),
        BOSS_KEY(2),
        SPAWN(1),
        NORMAL(15);

        private final int nbRoom;

        RoomType(int nbRoom){
            this.nbRoom = nbRoom;
        }

        public int getNbRoom() {
            return nbRoom;
        }

        public static int[] getRoomDistribution(){
            int[] result = new int[RoomType.values().length];
            for(int iValue = 0; iValue < result.length; iValue++){
                result[iValue] = RoomType.values()[iValue].getNbRoom();
            }
            return result;
        }
    }
    private final static DiscreteCoordinates defaultStartRoomCoord = new DiscreteCoordinates(0,0);
    private final static DiscreteCoordinates defaultPlayerStartingPos = new DiscreteCoordinates(6,3);

    private final int PART_1_KEY_ID = 1;
    private final int BOSS_KEY_ID = 2;

    public Level0(boolean randomMap) {
        super(randomMap, defaultPlayerStartingPos, RoomType.getRoomDistribution(), 4,2);
        if(!randomMap)
            setStartRoomCoord(defaultStartRoomCoord);
        setStartRoomTitle(getStartRoomCoord());
    }

    public Level0(){
        super(true, defaultPlayerStartingPos, RoomType.getRoomDistribution(), 4,2);
        setStartRoomTitle(getStartRoomCoord());
    }

    @Override
    protected void generateFixedMap() {
        generateFinalMap();
    }

    @Override
    protected void createRoom(int type, DiscreteCoordinates coord) {
        RoomType currentType = RoomType.values()[type];
        ICRogueRoom currentRoom;
        switch (currentType) {
            case TURRET_ROOM -> currentRoom = new Level0TurretRoom(coord);
            case SPAWN -> {
                currentRoom = new Level0Room(coord);
                setStartRoomCoord(coord);
            }
            case NORMAL -> currentRoom = new Level0Room(coord);
            case BOSS_KEY -> currentRoom = new Level0KeyRoom(coord, BOSS_KEY_ID);
            case STAFF_ROOM -> currentRoom = new Level0StaffRoom(coord);
            default -> currentRoom = new Level0Room(coord);
        }
        System.out.println(coord);
        setRoom(coord, currentRoom);
    }

    @Override
    protected void setUpConnector(MapState[][] roomsPlacement, ICRogueRoom room) {
        DiscreteCoordinates coord = room.getRoomCoordinates();
        int[] connectorIndexByOrientationOrdinal = {3, 2, 1, 0};//the index of the ConnectorInRoom based on his Orientation ordinal
        for(Orientation orientation : Orientation.values()){
            DiscreteCoordinates neighbour = coord.jump(orientation.toVector());
            if(isInBound(neighbour) && !roomsPlacement[neighbour.x][neighbour.y].equals(MapState.NULL)){
                Orientation connectorOrientation = orientation.opposite();
                int connectorIndex = connectorIndexByOrientationOrdinal[connectorOrientation.ordinal()];
                setRoomConnector(neighbour, room.getTitle(), Level0Room.Level0Connectors.values()[connectorIndex]);
            }
        }
    }

    @Override
    protected void setUpLockedConnector(MapState[][] roomsPlacement, ICRogueRoom room){ // Change it to make it for any locked door
        DiscreteCoordinates coord = room.getRoomCoordinates();
        int[] connectorIndexByOrientationOrdinal = {3, 2, 1, 0};//the index of the ConnectorInRoom based on his Orientation ordinal
        for(Orientation orientation : Orientation.values()){
            DiscreteCoordinates neighbour = coord.jump(orientation.toVector());
            if(isInBound(neighbour) && !roomsPlacement[neighbour.x][neighbour.y].equals(MapState.NULL)){
                Orientation connectorOrientation = orientation.opposite();
                int connectorIndex = connectorIndexByOrientationOrdinal[connectorOrientation.ordinal()];
                setRoomConnector(neighbour, room.getTitle(), Level0Room.Level0Connectors.values()[connectorIndex]);
                lockRoomConnector(neighbour, Level0Room.Level0Connectors.values()[connectorIndex], BOSS_KEY_ID);
            }
        }
    }

    private void generateMap1() {
        DiscreteCoordinates room00 = new DiscreteCoordinates(0, 0);
        setRoom(room00, new Level0KeyRoom(room00, PART_1_KEY_ID));
        setRoomConnector(room00, "icrogue/level010", Level0Room.Level0Connectors.E);
        lockRoomConnector(room00, Level0Room.Level0Connectors.E,  PART_1_KEY_ID);

        DiscreteCoordinates room10 = new DiscreteCoordinates(1, 0);
        setRoom(room10, new Level0Room(room10));
        setRoomConnector(room10, "icrogue/level000", Level0Room.Level0Connectors.W);
    }

    private void generateFinalMap() {
        DiscreteCoordinates room00 = new DiscreteCoordinates(0, 0);
        setRoom(room00, new Level0TurretRoom(room00));
        setRoomConnector(room00, "icrogue/level010", Level0Room.Level0Connectors.E);

        DiscreteCoordinates room10 = new DiscreteCoordinates(1,0);
        setRoom(room10, new Level0Room(room10));
        setRoomConnector(room10, "icrogue/level011", Level0Room.Level0Connectors.S);
        setRoomConnector(room10, "icrogue/level020", Level0Room.Level0Connectors.E);

        lockRoomConnector(room10, Level0Room.Level0Connectors.W,  BOSS_KEY_ID);
        setRoomConnectorDestination(room10, "icrogue/level000", Level0Room.Level0Connectors.W);

        DiscreteCoordinates room20 = new DiscreteCoordinates(2,0);
        setRoom(room20,  new Level0StaffRoom(room20));
        setRoomConnector(room20, "icrogue/level010", Level0Room.Level0Connectors.W);
        setRoomConnector(room20, "icrogue/level030", Level0Room.Level0Connectors.E);

        DiscreteCoordinates room30 = new DiscreteCoordinates(3,0);
        setRoom(room30, new Level0KeyRoom(room30, BOSS_KEY_ID));
        setRoomConnector(room30, "icrogue/level020", Level0Room.Level0Connectors.W);

        DiscreteCoordinates room11 = new DiscreteCoordinates(1, 1);
        setRoom (room11, new Level0Room(room11));
        setRoomConnector(room11, "icrogue/level010", Level0Room.Level0Connectors.N);
    }
}
