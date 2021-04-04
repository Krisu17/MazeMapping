//Michał Malinowski i Krzysztof Anderson, zespół 4

package mazemapping;


public enum DirectionType {
    left, right, up, down;
    
    private DirectionType opposite;

    static {
        left.opposite = right;
        right.opposite = left;
        up.opposite = down;
        down.opposite = up;
    }

    public DirectionType getOppositeDirection() {
        return opposite;
    }

}
