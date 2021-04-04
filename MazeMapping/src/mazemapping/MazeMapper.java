//Michał Malinowski i Krzysztof Anderson, zespół 4

package mazemapping;

import static java.lang.Math.abs;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;

public class MazeMapper {

    public static Node nil = null;
    private final Maze maze;
    private final int map;
    private final Node startNode;
    private final RequestParser parser;
    private static final SecureRandom random = new SecureRandom();
    private ArrayList<Node> junctions = new ArrayList<>();
    private Relaxator relaxator;

    public MazeMapper(int map) {
        this.map = map;
        parser = new RequestParser(map);
        maze = parser.prepareMaze();
        startNode = parser.initWalker(maze);
        relaxator = new Relaxator();
    }

    public void explore() {
        Node walker = startNode;
        setPaths(walker);
        Node previous;
        DirectionType next = null;
        do {
            if (walker.countPaths() != 0) {
                if (countUnvisitedPaths(walker) > 1) {
                    junctions.add(maze.getNode(walker.getX(), walker.getY()));
                }
                if (countUnvisitedPaths(walker) != 0) {
                    next = randomEnum(DirectionType.class);
                    while (walker.getNeighbour(next) == nil
                            || walker.getNeighbour(next).isVisited() == true) {
                        next = randomEnum(DirectionType.class);
                    }
                    parser.move(next);
                    previous = walker;
                    walker = walker.getNeighbour(next);
                    walker.setVisited(true);
                    if (walker.getParent() == nil) {
                        walker.setParent(previous, next.getOppositeDirection());
                    }
                    walker.setDistance(previous.getDistance() + 1);
                    relaxator.checkRelaxation(walker);
                    checkIfNeighbourIsJunction();
                } else { // jeżeli jest ślepym zaułkiem
                    Node squire = walker;
                    while (junctions.get(junctions.size() - 1).getDistance() != squire.getDistance()) {
                        next = chooseDirectionWithClosestDistanceToLastJunction(squire);
                        squire = squire.getNeighbour(next);
                    }
                    if (junctions.get(junctions.size() - 1).getX() == squire.getX()
                            || junctions.get(junctions.size() - 1).getY() == squire.getY()) {
                        while (junctions.get(junctions.size() - 1).getDistance() != walker.getDistance()) {
                            next = chooseDirectionWithClosestDistanceToLastJunction(walker);
                            parser.move(next);
                            walker = walker.getNeighbour(next);
                            walker.setVisited(true);
                        }
                    } else { // kiedy najbliższy Node o poszukiwanym Distance nie jest tym, którego szukamy
                        while (junctions.get(junctions.size() - 1).getX() != walker.getX()
                                || junctions.get(junctions.size() - 1).getY() != walker.getY()) { 
                            next = returnParentsDirection(walker);
                            parser.move(next);
                            walker = walker.getParent();
                        }
                    }
                }
            } else { // jeżeli labirynt ma tylko 1 pole
                System.out.println("Labirynt ma tylko 1 pole");
                break;
            }
            setPaths(walker);
        } while (!junctions.isEmpty() || countUnvisitedPaths(walker) > 0);
        parser.printMoves();

    }

    private void setPaths(Node node) {
        parser.setNodePaths(node);
        int x = node.getX();
        int y = node.getY();
        if (node.getL() == '0') {
            node.setLeftNeighbour(maze.getNode(x - 1, y));
        }
        if (node.getR() == '0') {
            node.setRightNeighbour(maze.getNode(x + 1, y));
        }
        if (node.getU() == '0') {
            node.setUpNeighbour(maze.getNode(x, y - 1));
        }
        if (node.getD() == '0') {
            node.setDownNeighbour(maze.getNode(x, y + 1));
        }
    }

    private static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    private int countUnvisitedPaths(Node node) {
        int count = 0;
        if (node.getNeighbour(DirectionType.left) != nil
                && !node.getNeighbour(DirectionType.left).isVisited()) {
            count++;
        }
        if (node.getNeighbour(DirectionType.right) != nil
                && !node.getNeighbour(DirectionType.right).isVisited()) {
            count++;
        }
        if (node.getNeighbour(DirectionType.up) != nil
                && !node.getNeighbour(DirectionType.up).isVisited()) {
            count++;
        }
        if (node.getNeighbour(DirectionType.down) != nil
                && !node.getNeighbour(DirectionType.down).isVisited()) {
            count++;
        }
        return count;
    }

    private void checkIfNeighbourIsJunction() {
        Iterator i = junctions.iterator();
        while (i.hasNext()) {
            if (countUnvisitedPaths((Node) i.next()) == 0) {
                i.remove();
            }
        }
    }

    private DirectionType returnParentsDirection(Node node) {
        if (node.getParent() == node.getNeighbour(DirectionType.left)) {
            return DirectionType.left;
        } else if (node.getParent() == node.getNeighbour(DirectionType.right)) {
            return DirectionType.right;
        } else if (node.getParent() == node.getNeighbour(DirectionType.up)) {
            return DirectionType.up;
        } else if (node.getParent() == node.getNeighbour(DirectionType.down)) {
            return DirectionType.down;
        } else {
            System.out.println("Nie ma rodzica w pobliżu :(");
            System.exit(0);
            return null;
        }
    }

    public Maze getMaze() {
        return maze;
    }

    public void sendMaze(String path) {
        parser.sendMap(path);
    }

    private DirectionType chooseDirectionWithClosestDistanceToLastJunction(Node node) {
        int shortestDistance = Integer.MAX_VALUE;
        DirectionType next = null;
        if (node.getL() == '0') {
            if (abs(node.getNeighbour(DirectionType.left).getDistance()
                    - junctions.get(junctions.size() - 1).getDistance())
                    < shortestDistance) {
                shortestDistance = abs(node.getNeighbour(DirectionType.left).getDistance()
                        - junctions.get(junctions.size() - 1).getDistance());
                next = DirectionType.left;
            }
        }
        if (node.getR() == '0') {
            if (abs(node.getNeighbour(DirectionType.right).getDistance()
                    - junctions.get(junctions.size() - 1).getDistance())
                    < shortestDistance) {
                shortestDistance = abs(node.getNeighbour(DirectionType.right).getDistance()
                        - junctions.get(junctions.size() - 1).getDistance());
                next = DirectionType.right;
            }
        }
        if (node.getU() == '0') {
            if (abs(node.getNeighbour(DirectionType.up).getDistance()
                    - junctions.get(junctions.size() - 1).getDistance())
                    < shortestDistance) {
                shortestDistance = abs(node.getNeighbour(DirectionType.up).getDistance()
                        - junctions.get(junctions.size() - 1).getDistance());
                next = DirectionType.up;
            }
        }
        if (node.getD() == '0') {
            if (abs(node.getNeighbour(DirectionType.down).getDistance()
                    - junctions.get(junctions.size() - 1).getDistance())
                    < shortestDistance) {
                shortestDistance = abs(node.getNeighbour(DirectionType.down).getDistance()
                        - junctions.get(junctions.size() - 1).getDistance());
                next = DirectionType.down;
            }
        }
        return next;
    }
}
