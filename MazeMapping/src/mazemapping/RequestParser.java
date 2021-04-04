//Michał Malinowski i Krzysztof Anderson, zespół 4

package mazemapping;

public class RequestParser {

    private HttpConnector connector = new HttpConnector();
    private int mapNumber;
    private String response;

    public RequestParser(int mapNumber) {
        this.mapNumber = mapNumber;
        if(connector.post(Integer.toString(mapNumber), "reset") != 0) {
            System.out.println("Podczas resetowania wystąpił błąd.");
        }
    }

    public void setNodePaths(Node node) {
        response = connector.get(Integer.toString(mapNumber), "possibilities");//9 20 32 41
        node.setD(response.charAt(9));
        node.setL(response.charAt(20));
        node.setR(response.charAt(32));
        node.setU(response.charAt(41));
    }

    public Maze prepareMaze() {
        response = connector.get(Integer.toString(mapNumber), "size");
        int width;
        int height;
        int x = 0;
        for (int i = 0; i < response.length(); i++) {
            if (response.charAt(i) == 'x') {
                x = i;
            }
        }
        width = Integer.valueOf(response.substring(0, x));
        height = Integer.valueOf(response.substring(x + 1, response.length()));
        System.out.println("Wymiary "+ width + "x" + height);
        return new Maze(width, height);
    }

    public Node initWalker(Maze maze) {
        response = connector.get(Integer.toString(mapNumber), "startposition");
        int startX, startY, x = 0;
        for (int i = 0; i < response.length(); i++) {
            if (response.charAt(i) == ',') {
                x = i;
            }
        }
        startX = Integer.valueOf(response.substring(0, x)) - 1;
        startY = Integer.valueOf(response.substring(x + 1, response.length())) - 1;
        setNodePaths(maze.getNode(startX,startY)); 
        maze.getNode(startX,startY).setDistance(0);
        maze.getNode(startX,startY).setVisited(true);
        return maze.getNode(startX,startY); 
    }

    public void move(DirectionType direction) {
        if(connector.post(Integer.toString(mapNumber), "move/" + direction.name()) != 0) {
            System.out.println("Podczas poruszania wystąpił błąd.");
        }
    }
    
    public void printMoves() {
        System.out.println("Liczba kroków: " + connector.get(Integer.toString(mapNumber), "moves"));
    }
    
    public void sendMap(String path) {
        if(connector.sendMap(Integer.toString(mapNumber), path) != 0) {
            System.out.println("Podczas wysyłania pliku wystąpił błąd.");
        }
    }
}
