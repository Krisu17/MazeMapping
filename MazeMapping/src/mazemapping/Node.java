//Michał Malinowski i Krzysztof Anderson, zespół 4

package mazemapping;

import static mazemapping.DirectionType.*;
import static mazemapping.MazeMapper.nil;

public class Node {

    private int x;
    private int y;
    private char l, r, u, d;
    private Node leftNeighbour, rightNeighbour, upNeighbour, downNeighbour;
    private Node parent;
    private boolean visited;
    private int distance;
    private DirectionType cameFrom;

    public Node() {
        this.distance = -1;
        this.leftNeighbour = nil;
        this.rightNeighbour = nil;
        this.upNeighbour = nil;
        this.downNeighbour = nil;
        this.parent = nil;
        this.visited = false;
        this.l = '+';
        this.r = '+';
        this.u = '+';
        this.d = '+';
    }

    public Node getNeighbour(DirectionType direction) {
        switch (direction) {
            case up:
                return upNeighbour;
            case down:
                return downNeighbour;
            case left:
                return leftNeighbour;
            case right:
                return rightNeighbour;
            default:
                return nil;
        }
    }

    public void setParent(Node parent, DirectionType cameFrom) {
        this.parent = parent;
        this.cameFrom = cameFrom;
    }

    public int countPaths() {
        int count = 0;
        if (l == '0') {
            count++;
        }
        if (r == '0') {
            count++;
        }
        if (u == '0') {
            count++;
        }
        if (d == '0') {
            count++;
        }
        return count;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public char getL() {
        return l;
    }

    public void setL(char l) {
        this.l = l;
    }

    public char getR() {
        return r;
    }

    public void setR(char r) {
        this.r = r;
    }

    public char getU() {
        return u;
    }

    public void setU(char u) {
        this.u = u;
    }

    public char getD() {
        return d;
    }

    public void setD(char d) {
        this.d = d;
    }

    public void setLeftNeighbour(Node leftNeighbour) {
        this.leftNeighbour = leftNeighbour;
    }

    public void setRightNeighbour(Node rightNeighbour) {
        this.rightNeighbour = rightNeighbour;
    }

    public void setUpNeighbour(Node upNeighbour) {
        this.upNeighbour = upNeighbour;
    }

    public void setDownNeighbour(Node downNeighbour) {
        this.downNeighbour = downNeighbour;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

}
