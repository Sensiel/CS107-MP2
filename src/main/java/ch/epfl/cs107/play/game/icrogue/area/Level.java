package ch.epfl.cs107.play.game.icrogue.area;

import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.RandomHelper;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public abstract class Level implements Logic {

    /* GLOBAL MAP ATTRIBUTES */
    private final ICRogueRoom[][] map;
    private final int  width;
    private final int  height;
    private final DiscreteCoordinates playerStartingPos;

    private DiscreteCoordinates startRoomCoord;
    private DiscreteCoordinates posBoss;
    private String startRoomTitle;
    /*-----------------------*/


    /* GLOBAL UTILITY MAP METHODS */
    protected void setRoom(DiscreteCoordinates coords, ICRogueRoom room){
        map[coords.x][coords.y] = room;
    }

    protected void setRoomConnectorDestination(DiscreteCoordinates coords, String destination, ConnectorInRoom connector){
        map[coords.x][coords.y].setConnectorDestination(connector, destination);
    }

    protected void setRoomConnector(DiscreteCoordinates coords, String destination, ConnectorInRoom connector){
        setRoomConnectorDestination(coords, destination, connector);
        map[coords.x][coords.y].setConnectorState(connector, Connector.State.CLOSED);
    }

    protected void lockRoomConnector(DiscreteCoordinates coords, ConnectorInRoom connector, int keyId){
        map[coords.x][coords.y].setConnectorState(connector, Connector.State.LOCKED);
        map[coords.x][coords.y].setConnectorKeyID(connector, keyId);
    }

    protected boolean isInBound(DiscreteCoordinates coord){
        return coord.x >= 0 && coord.x < width && coord.y >= 0 && coord.y < height;
    }

    public void addAreas(ICRogue game){
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                if(map[x][y] != null){
                    game.addArea(map[x][y]);
                }
            }
        }
    }

    public void setStartRoomCoord(DiscreteCoordinates startRoomCoord) {
        this.startRoomCoord = startRoomCoord;
    }

    public DiscreteCoordinates getStartRoomCoord() {
        return startRoomCoord;
    }

    /*----------------------------*/

    /* RANDOM MAP ATTRIBUTES */

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

    private int nbRooms;
    private int[] roomDistribution;

    private MapState[][] roomPlacement;
    /*-----------------------*/

    /* RANDOM UTILITY MAP METHODS */

    protected abstract void createRoom(int type, DiscreteCoordinates coord);

    protected MapState[][] generateRandomRoomPlacement(){
        MapState[][] result = new MapState[width][height];
        //Initialisation de la map
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                result[x][y] = MapState.NULL;
            }
        }

        //Calcul du centre
        DiscreteCoordinates center = new DiscreteCoordinates(width/2, height/2);
        LinkedList<DiscreteCoordinates> toExplore = new LinkedList<>();

        //On place le centre et on l'ajoute à la liste des cellules à explorer
        result[center.x][center.y] = MapState.PLACED;
        toExplore.addLast(center);

        // on compte la salle du boss en plus et la salle du centre en moins
        int roomToPlace = nbRooms;

        //Tant qu'il reste des salles à poser ( la deuxième condition evite juste des exceptions même si c'est censé jamais arriver )
        while(roomToPlace > 0 && !toExplore.isEmpty()){
            //On prend la prochaine cellule à explorer de la liste
            DiscreteCoordinates current = toExplore.removeFirst();

            //On calcule les voisins libres
            ArrayList<DiscreteCoordinates> possibleSlot = new ArrayList<>();
            for(DiscreteCoordinates neighbour : current.getNeighbours()){
                if(isInBound(neighbour) && result[neighbour.x][neighbour.y].equals(MapState.NULL)){
                    possibleSlot.add(neighbour);
                }
            }
            int freeSlots = possibleSlot.size();


            if(freeSlots != 0){
                //On prend un nombre de room à placer pouvant aller de 0 (inclus) au minimum entre le nombre de voisins libre et le quota de room restante à placer ( inclus )
                int roomGettingPlaced = RandomHelper.roomGenerator.nextInt(Integer.min(freeSlots, roomToPlace) + 1);

                //On crée un tableau allant de 0 (inclus) au nombre de voisins (exclus) représentant les index de chaque voisin dans le tableau possibleSlot
                ArrayList<Integer> roomIndex = new ArrayList<>();
                for(int i = 0; i < freeSlots; i++){
                    roomIndex.add(i);
                }

                //Pour chaque voisins choisis à placer
                for(Integer index : RandomHelper.chooseKInList(roomGettingPlaced, roomIndex)){
                    //On l'ajoute à la liste des cellules à explorer
                    toExplore.addLast(possibleSlot.get(index));

                    if(roomToPlace == 1){ // Si la salle est la dernière à être placée il s'agit de celle du boss
                        result[possibleSlot.get(index).x][possibleSlot.get(index).y] = MapState.BOSS_ROOM;
                        posBoss = possibleSlot.get(index);
                    }
                    else{ // Sinon elle est seulement placée
                        result[possibleSlot.get(index).x][possibleSlot.get(index).y] = MapState.PLACED;
                    }
                    //enfin le quota est diminué de 1
                    roomToPlace--;
                }


                // Il pourrait y avoir une probabilité que l'on explore toute les salles placées sans
                // rajouter d'autre salle, ce qui causerait la fin de la création de la map et donc un
                // sous-effectif de salle créées.
                if(roomGettingPlaced == 0){
                    toExplore.addLast(current);
                }
                else{
                    result[current.x][current.y] = MapState.EXPLORED;
                }
            }
            //Si il n'y a aucun voisin libre on la met comme explorée
            else{
                result[current.x][current.y] = MapState.EXPLORED;
            }
        }

        //debug de la map
        printMap(result);
        return result;
    }

    protected abstract void setUpConnector(MapState [][] roomsPlacement , ICRogueRoom room);
    protected abstract void setUpLockedConnector(MapState [][] roomsPlacement , ICRogueRoom room);
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

    /*----------------------------*/


    public void setStartRoomTitle(DiscreteCoordinates coords) {
        this.startRoomTitle = map[coords.x][coords.y].getTitle();
    }
    public String getStartRoomTitle() {
        return startRoomTitle;
    }

    public Level(boolean randomMap, DiscreteCoordinates playerStartingPos, int[] roomsDistribution, int width, int height){
        this.playerStartingPos = playerStartingPos;

        if(randomMap){
            nbRooms = Arrays.stream(roomsDistribution).sum(); // TODO peut être faire un truc soi même
            this.width = nbRooms;
            this.height = nbRooms;
            this.roomDistribution = roomsDistribution;
            map = new ICRogueRoom[this.width][this.height];
            generateRandomMap();
        }
        else{
            posBoss = new DiscreteCoordinates(0,0);
            this.width = width;
            this.height = height;
            map =  new ICRogueRoom[width][height];
            generateFixedMap();
        }
    }

    protected abstract void generateFixedMap();

    protected void generateRandomMap(){
        roomPlacement = generateRandomRoomPlacement();
        for(int iType = 0; iType < roomDistribution.length; iType++){

            ArrayList<DiscreteCoordinates> possibleRooms = new ArrayList<>();
            ArrayList<Integer> roomIndex = new ArrayList<>();
            for(int x = 0; x < width; x++){
                for(int y = 0; y < height; y++){
                    if(roomPlacement[x][y].equals(MapState.EXPLORED) || roomPlacement[x][y].equals(MapState.PLACED)){
                        possibleRooms.add(new DiscreteCoordinates(x,y));
                        roomIndex.add(roomIndex.size());
                    }
                }
            }

            for(Integer index : RandomHelper.chooseKInList(roomDistribution[iType], roomIndex)) {
                DiscreteCoordinates currentCoord = possibleRooms.get(index);
                roomPlacement[currentCoord.x][currentCoord.y] = MapState.CREATED;
                createRoom(iType, currentCoord);
            }
        }

        createRoom(0, posBoss); // TODO changer absolument c'est immonde

        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                if(roomPlacement[x][y].equals(MapState.CREATED)){
                    System.out.println(map[x][y]);
                    setUpConnector(roomPlacement, map[x][y]);
                }
            }
        }

        setUpLockedConnector(roomPlacement, map[posBoss.x][posBoss.y]);

        printMap(roomPlacement);
    }

    public DiscreteCoordinates getPlayerStartingPos() {
        return playerStartingPos;
    }
    @Override
    public boolean isOn() {
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
