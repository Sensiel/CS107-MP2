package ch.epfl.cs107.play.game.icrogue.area;

import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.RandomHelper;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;



public abstract class Level implements Logic {

    // The map of the current level
    private final ICRogueRoom[][] map;

    // The width of the map
    private final int  width;

    // The height of the map
    private final int  height;

    // The starting position of the player ( in the starting room )
    private final DiscreteCoordinates playerStartingPos;

    //the number of room in the level ( only for random generation )
    private int nbRooms;

    //The room distribution of the level ( only for random generation )
    private int[] roomDistribution;

    /**
     * Enum representing the different states of a room in the room placement array
     */
    protected enum MapState {
        NULL,
        PLACED,
        EXPLORED,
        BOSS_ROOM,
        CREATED;
        @Override
        public String toString() {
            return Integer.toString(ordinal());
        }
    }

    //the Room Placement of the level ( only for random generation )
    private MapState[][] roomPlacement;

    // The starting room coordinates
    private DiscreteCoordinates startRoomCoord;

    // The position of the boss room
    private DiscreteCoordinates posBoss;

    // The title of the starting room
    private String startRoomTitle;

    /**
     * Default Level constructor
     * @param randomMap (boolean): is the generation random ?. Not null
     * @param playerStartingPos (DiscreteCoordinates): Initial position of the player in the room. Not null
     * @param roomsDistribution (int[]): Distribution of the room's type in the level. Not null
     * @param width (int): Initial orientation of the projectile. Not null ( but not useful if the generation is random )
     * @param height (int): Initial orientation of the projectile. Not null ( but not useful if the generation is random )
     */
    protected Level(boolean randomMap, DiscreteCoordinates playerStartingPos, int[] roomsDistribution, int width, int height){
        this.playerStartingPos = playerStartingPos;

        if(randomMap){
            // we sum every amount in the room distribution
            nbRooms = Arrays.stream(roomsDistribution).sum();
            this.width = nbRooms;
            this.height = nbRooms;
            this.roomDistribution = roomsDistribution;
            map = new ICRogueRoom[this.width][this.height];
            // we generate the map
            generateRandomMap();
        }
        else{
            //the posBoss is by default (0,0)
            posBoss = new DiscreteCoordinates(0,0);
            this.width = width;
            this.height = height;
            map =  new ICRogueRoom[width][height];
            generateFixedMap();
        }
    }


    /**
     * Set a room to a specific position in the map
     * @param coords : The room coordinates
     * @param room : The room
     */
    protected void setRoom(DiscreteCoordinates coords, ICRogueRoom room){
        map[coords.x][coords.y] = room;
    }


    /**
     * Set the destination of a spectific connector, given its room coordinates, its destination and the type of connector he is.
     * @param coords ( DiscreteCoordinates ) : The room coordinates
     * @param destination ( String ) : The destination of the connector
     * @param connector ( ConnectorInRoom ) : The type of connector
     */
    protected void setRoomConnectorDestination(DiscreteCoordinates coords, String destination, ConnectorInRoom connector){
        map[coords.x][coords.y].setConnectorDestination(connector, destination);
    }

    /**
     * Locks up a specific connector, given its room coordinates, its destination and the type of connector he is.
     * @param coords ( DiscreteCoordinates ) : The room coordinates
     * @param destination ( String ) : The destination of the connector
     * @param connector ( ConnectorInRoom ) : The type of connector
     */
    protected void setRoomConnector(DiscreteCoordinates coords, String destination, ConnectorInRoom connector){
        setRoomConnectorDestination(coords, destination, connector);
        map[coords.x][coords.y].setConnectorState(connector, Connector.State.CLOSED);
    }


    /**
     * Locks up a specific connector, given its room coordinates, the type of connector he is, and the id of the key associated
     * @param coords ( DiscreteCoordinates ) : The room coordinates
     * @param connector ( ConnectorInRoom ) : The type of connector
     * @param keyId ( int ) : The id of the key associated
     */
    protected void lockRoomConnector(DiscreteCoordinates coords, ConnectorInRoom connector, int keyId){
        map[coords.x][coords.y].setConnectorState(connector, Connector.State.LOCKED);
        map[coords.x][coords.y].setConnectorKeyID(connector, keyId);
    }

    /**
     * Return true if the coordinates are valid ( represent a correct room location in the map )
     * @param coord (DiscreteCoordinates): the given coordinates
     * @return true if it is valid, false otherwise.
     */
    protected boolean isInBound(DiscreteCoordinates coord){
        return coord.x >= 0 && coord.x < width && coord.y >= 0 && coord.y < height;
    }

    /**
     * Add every created room to the game
     * @param game ( ICRogue ) : the game
     */
    public void addAreas(ICRogue game){
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                if(map[x][y] != null){
                    game.addArea(map[x][y]);
                }
            }
        }
    }

    /**
     * Set the starting room coordinates
     * @param startRoomCoord (DiscreteCoordinates) : the coordinates to set
     */
    protected void setStartRoomCoord(DiscreteCoordinates startRoomCoord) {
        this.startRoomCoord = startRoomCoord;
    }

    /**
     * @return the coordinates of the starting room
     */
    protected DiscreteCoordinates getStartRoomCoord() {
        return startRoomCoord;
    }


    /**
     * Utility function used by concrete level to create a room, given its type and coordinates
     * @param type (int) : The type of the room
     * @param coord (DiscreteCoordinates) : The coordinates of the room
     */
    protected abstract void createRoom(int type, DiscreteCoordinates coord);

    /**
     * Utility method generating a random room placement
     * @return roomPlacement (MapState[][]): The room placement generated
     */
    private MapState[][] generateRandomRoomPlacement(){
        MapState[][] result = new MapState[width][height];
        //Initialization of the map
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                result[x][y] = MapState.NULL;
            }
        }

        //Calculation of the center
        DiscreteCoordinates center = new DiscreteCoordinates(width/2, height/2);
        LinkedList<DiscreteCoordinates> toExplore = new LinkedList<>();

        //Place the center and add it to the list of cells to explore
        result[center.x][center.y] = MapState.PLACED;
        toExplore.addLast(center);

        // we count the room of the boss in more and the room of the center in less
        int roomToPlace = nbRooms;

        //As long as there are still rooms to be installed (the second condition just avoids exceptions even if it is supposed to never happen)
        while(roomToPlace > 0 && !toExplore.isEmpty()){
            //Take the next cell to explore in the list
            DiscreteCoordinates current = toExplore.removeFirst();

            //The free neighbors are calculated
            List<DiscreteCoordinates> possibleSlot = new ArrayList<>();
            for(DiscreteCoordinates neighbour : current.getNeighbours()){
                if(isInBound(neighbour) && result[neighbour.x][neighbour.y].equals(MapState.NULL)){
                    possibleSlot.add(neighbour);
                }
            }
            int freeSlots = possibleSlot.size();


            if(freeSlots != 0){
                //We take a number of rooms to place that can go from 0 (included) to the minimum between the number of free neighbors and the quota of remaining rooms to place ( included )
                int roomGettingPlaced = RandomHelper.roomGenerator.nextInt(Integer.min(freeSlots, roomToPlace) + 1);

                //We create an array from 0 (included) to the number of neighbors (excluded) representing the indexes of each neighbor in the possibleSlot array
                List<Integer> roomIndex = new ArrayList<>();
                for(int i = 0; i < freeSlots; i++){
                    roomIndex.add(i);
                }

                //For each selected neighbor to place
                for(int index : RandomHelper.chooseKInList(roomGettingPlaced, roomIndex)){
                    //We add it to the list of cells to explore
                    toExplore.addLast(possibleSlot.get(index));

                    if(roomToPlace == 1){ // If the room is the last to be placed, it is the boss' room
                        result[possibleSlot.get(index).x][possibleSlot.get(index).y] = MapState.BOSS_ROOM;
                        posBoss = possibleSlot.get(index);
                    }
                    else{ // Otherwise it is only placed
                        result[possibleSlot.get(index).x][possibleSlot.get(index).y] = MapState.PLACED;
                    }
                    //then the quota is decreased by 1.
                    roomToPlace--;
                }


                /* There could be a probability that we explore all the rooms placed without
                * adding another room, which would cause the end of the creation of the map and therefore a
                * shortage of created rooms.
                */
                if(roomGettingPlaced == 0){
                    toExplore.addLast(current);
                }
                else{
                    result[current.x][current.y] = MapState.EXPLORED;
                }
            }
            //If there are no free neighbors it is set as explored
            else{
                result[current.x][current.y] = MapState.EXPLORED;
            }
        }

        //printMap(result); // Uncomment if you want to print the map
        return result;
    }

    /**
     * Utility function used by concrete level to setup every connector leading to a given room
     * @param roomsPlacement (MapState [][]) : the room placement
     * @param room ( ICRogueRoom ) : the room for which we setup every connector leading to it
     */
    protected abstract void setUpConnector(MapState [][] roomsPlacement , ICRogueRoom room);

    /**
     * Utility function used by concrete level to setup every locked connector leading to a given room
     * @param roomsPlacement (MapState [][]) : the room placement
     * @param room ( ICRogueRoom ) : the room for which we setup every locked connector leading to it
     * @param keyID (int) : the key id required to unlock the connectors
     */
    protected abstract void setUpLockedConnector(MapState [][] roomsPlacement , ICRogueRoom room, int keyID);

    /**
     * Print a given map in the console
     * @param map ( MapState[][] ) : the map to print
     */
    private void printMap(MapState [][] map) {
        System.out.println("Generated map:");
        System.out.print(" | ");
        for (int j = 0; j < map[0]. length; j++) {
            System.out.print(j + " ");
        }
        System.out.println();
        System.out.print("--|-");
        for (int j = 0; j < map[0]. length; j++) {
            System.out.print("--");
        }
        System.out.println();
        for (int i = 0; i < map.length; i++) {
            System.out.print(i + " | ");
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[j][i] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }


    /**
     * Set the startRoomTitle to the title of a certain room, given by its coordinates
     * @param coords (DiscreteCoordinates)
     */
    protected void setStartRoomTitle(DiscreteCoordinates coords) {
        this.startRoomTitle = map[coords.x][coords.y].getTitle();
    }

    /**
     * @return the title of the starting room
     */
    public String getStartRoomTitle() {
        return startRoomTitle;
    }

    /**
     * Utility function used by concrete level to generate specific map
     */
    protected abstract void generateFixedMap();

    /**
     * Utility function for generating a random level
     */
    private void generateRandomMap(){
        // Get the map where there's room to place and where the bossRoom is
        roomPlacement = generateRandomRoomPlacement();

        // For each room type to place
        for(int iType = 0; iType < roomDistribution.length; iType++){
            // We create a list of the k room to place and not already created and a list containing [0 ... k-1]
            List<DiscreteCoordinates> possibleRooms = new ArrayList<>();
            List<Integer> roomIndex = new ArrayList<>();

            //We iterate through the room placement to get every room to place and not already created
            for(int x = 0; x < width; x++){
                for(int y = 0; y < height; y++){
                    if(roomPlacement[x][y].equals(MapState.EXPLORED) || roomPlacement[x][y].equals(MapState.PLACED)){
                        possibleRooms.add(new DiscreteCoordinates(x,y));
                        roomIndex.add(roomIndex.size());
                    }
                }
            }
            //We pick random index for every room of the type we're currently processing and we create the room associated with these indexes
            for(int index : RandomHelper.chooseKInList(roomDistribution[iType], roomIndex)) {
                DiscreteCoordinates currentCoord = possibleRooms.get(index);
                roomPlacement[currentCoord.x][currentCoord.y] = MapState.CREATED;
                createRoom(iType, currentCoord);
            }
        }
        // We create the room of posBoss with the type 0
        createRoom(0, posBoss);

        // We iterate through the map to set up every connector leading to the room created ( except for the bossRoom, which will be dealt with at the end )
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                if(roomPlacement[x][y].equals(MapState.CREATED)){
                    setUpConnector(roomPlacement, map[x][y]);
                }
            }
        }
        // Setting up the bossRoom ( the boss' key ID is by default 2 )
        setUpLockedConnector(roomPlacement, map[posBoss.x][posBoss.y], 2);

        //printMap(roomPlacement); // Uncomment if you want the map to be printed
    }

    /**
     * @return the player starting pos
     */
    public DiscreteCoordinates getPlayerStartingPos() {
        return playerStartingPos;
    }
    @Override
    public boolean isOn() {
        // The level is resolved if the boss Room is resolved or the player is dead
        return (map[posBoss.x][posBoss.y] != null && map[posBoss.x][posBoss.y].isOn());
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
