package ch.epfl.cs107.play.game.icrogue.area;

import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public abstract class Level {
    private ICRogueRoom[][] map;
    private final int  width;
    private final int  height;
    private final DiscreteCoordinates globalPosBeginning;
    private final DiscreteCoordinates posBoss;
    private String startRoomTitle;

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

    public void setStartRoomTitle(DiscreteCoordinates coords) {
        this.startRoomTitle = map[coords.x][coords.y].getTitle();
    }
    public String getStartRoomTitle() {
        return startRoomTitle;
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

    public Level(DiscreteCoordinates globalPosBeginning, int width, int height){
        posBoss = new DiscreteCoordinates(0,0);
        this.globalPosBeginning = globalPosBeginning;
        this.width = width;
        this.height = height;
        map =  new ICRogueRoom[width][height];
        generateFixedMap();
    }

    protected abstract void generateFixedMap();


    public DiscreteCoordinates getGlobalPosBeginning() {
        return globalPosBeginning;
    }
}
