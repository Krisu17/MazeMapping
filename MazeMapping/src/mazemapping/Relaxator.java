//Michał Malinowski i Krzysztof Anderson, zespół 4

package mazemapping;

import static mazemapping.DirectionType.*;
import static mazemapping.MazeMapper.nil;

public class Relaxator {

    public void checkRelaxation(Node node) {
        Node neighbour;
        if (node.getNeighbour(up) != nil) {
            neighbour = node.getNeighbour(up);
            if (neighbour.getDistance() < node.getDistance()) {
                fixRelaxation(node, up);
            }
        }
        if (node.getNeighbour(down) != nil) {
            neighbour = node.getNeighbour(down);
            if (neighbour.getDistance() < node.getDistance()) {
                fixRelaxation(node, down);
            }
        }
        if (node.getNeighbour(left) != nil) {
            neighbour = node.getNeighbour(left);
            if (neighbour.getDistance() > node.getDistance()) {
                fixRelaxation(node, down);
            }
        }
        if (node.getNeighbour(right) != nil) {
            neighbour = node.getNeighbour(right);
            if (neighbour.getDistance() > node.getDistance()) {
                fixRelaxation(node, down);
            }
        }

    }

    private void fixRelaxation(Node node, DirectionType direction) {
        if (node == nil || node.getNeighbour(direction) == nil) {
            return;
        }
        Node neighbour;
        neighbour = node.getNeighbour(direction);
        if (neighbour.getDistance() < node.getDistance()) {
            node.setDistance(neighbour.getDistance() + 1);
            switch (direction) {
                case up:
                    fixRelaxation(node.getNeighbour(left), right);
                    fixRelaxation(node.getNeighbour(right), left);
                    fixRelaxation(node.getNeighbour(down), up);
                    break;
                case down:
                    fixRelaxation(node.getNeighbour(left), right);
                    fixRelaxation(node.getNeighbour(right), left);
                    fixRelaxation(node.getNeighbour(up), down);
                    break;
                case left:
                    fixRelaxation(node.getNeighbour(up), down);
                    fixRelaxation(node.getNeighbour(right), left);
                    fixRelaxation(node.getNeighbour(down), up);
                    break;
                case right:
                    fixRelaxation(node.getNeighbour(left), right);
                    fixRelaxation(node.getNeighbour(up), down);
                    fixRelaxation(node.getNeighbour(down), up);
                    break;
                default:
                    break;
            }
        }
    }
}
