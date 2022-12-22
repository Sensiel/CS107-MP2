package ch.epfl.cs107.play.game.icrogue.area.level0;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.area.Level;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.*;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level0 extends Level {
    /**
     * Enum reprensenting the different room's type and their distribution in the case of a random generation of Level0
     */
    private enum RoomType {
        TURRET_ROOM(3),
        STAFF_ROOM(1),
        BOSS_KEY(1),
        SPAWN(1),
        NORMAL(1),
        ICE_ROOM(0);

        // the number of a certain type of room
        private final int nbRoom;

        /**
         * Default RoomType constructor
         * @param nbRoom : the number of room of that type
         */
        RoomType(int nbRoom){
            this.nbRoom = nbRoom;
        }

        /**
         * @return the number of room of that type
         */
        private int getNbRoom() {
            return nbRoom;
        }

        /**
         * @return the array representing the room distribution in a level0 randomly generated map
         */
        private static int[] getRoomDistribution(){
            int[] result = new int[RoomType.values().length];
            for(int iValue = 0; iValue < result.length; iValue++){
                result[iValue] = RoomType.values()[iValue].getNbRoom();
            }
            return result;
        }
    }

    // The default width of a fixed level0 map
    private final static int DEFAULT_WIDTH = 4;

    // The default height of a fixed level0 map
    private final static int DEFAULT_HEIGHT= 2;

    // The default starting room's coordinates of a fixed level0 map
    private final static DiscreteCoordinates defaultStartRoomCoord = new DiscreteCoordinates(0,0);

    // The default starting player's position in a level0 map
    private final static DiscreteCoordinates defaultPlayerStartingPos = new DiscreteCoordinates(6,3);

    private final int PART_1_KEY_ID = 1;

    // the key id for the boss' room
    private final int BOSS_KEY_ID = 2;

    /**
     * Default Level0 constructor ( random generation by default )
     */
    public Level0(){
        this(true);
    }

    /**
     * Alternative Level0 constructor
     * @param randomMap ( boolean ) : boolean telling if the generation is random or not
     */
    public Level0(boolean randomMap) {
        super(randomMap, defaultPlayerStartingPos, RoomType.getRoomDistribution(), DEFAULT_WIDTH, DEFAULT_HEIGHT);
        if(!randomMap)
            setStartRoomCoord(defaultStartRoomCoord);
        setStartRoomTitle(getStartRoomCoord());
    }

    public Level0(int[] roomDistribution){
        super(true, defaultPlayerStartingPos, roomDistribution, DEFAULT_WIDTH, DEFAULT_HEIGHT);
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
            case ICE_ROOM -> currentRoom = new Level0IceRoom(coord);
            default -> currentRoom = new Level0Room(coord);
        }
        setRoom(coord, currentRoom);
    }

    @Override
    protected void setUpConnector(MapState[][] roomsPlacement, ICRogueRoom room) {
        DiscreteCoordinates coord = room.getRoomCoordinates();

        //the index of the ConnectorInRoom based on his Orientation ordinal
        int[] connectorIndexByOrientationOrdinal = {3, 2, 1, 0};

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
    protected void setUpLockedConnector(MapState[][] roomsPlacement, ICRogueRoom room, int keyID){
        DiscreteCoordinates coord = room.getRoomCoordinates();

        int[] connectorIndexByOrientationOrdinal = {3, 2, 1, 0};//the index of the ConnectorInRoom based on his Orientation ordinal

        for(Orientation orientation : Orientation.values()){

            DiscreteCoordinates neighbour = coord.jump(orientation.toVector());

            if(isInBound(neighbour) && !roomsPlacement[neighbour.x][neighbour.y].equals(MapState.NULL)){

                Orientation connectorOrientation = orientation.opposite();
                int connectorIndex = connectorIndexByOrientationOrdinal[connectorOrientation.ordinal()];
                setRoomConnector(neighbour, room.getTitle(), Level0Room.Level0Connectors.values()[connectorIndex]);
                lockRoomConnector(neighbour, Level0Room.Level0Connectors.values()[connectorIndex], keyID);
            }
        }
    }

    /**
     * Example of fixed generation
     */
    private void generateMap1() {
        DiscreteCoordinates room00 = new DiscreteCoordinates(0, 0);
        setRoom(room00, new Level0KeyRoom(room00, PART_1_KEY_ID));
        setRoomConnector(room00, "icrogue/level010", Level0Room.Level0Connectors.E);
        lockRoomConnector(room00, Level0Room.Level0Connectors.E,  PART_1_KEY_ID);

        DiscreteCoordinates room10 = new DiscreteCoordinates(1, 0);
        setRoom(room10, new Level0Room(room10));
        setRoomConnector(room10, "icrogue/level000", Level0Room.Level0Connectors.W);
    }

    /**
     * Example of fixed generation
     */
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
