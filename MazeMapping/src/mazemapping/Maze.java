//Michał Malinowski i Krzysztof Anderson, zespół 4

package mazemapping;

public class Maze {

    private final Node[][] nodes;
    private final int width;
    private final int height;

    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        nodes = new Node[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                nodes[i][j] = new Node();
                nodes[i][j].setX(i);
                nodes[i][j].setY(j);
            }
        }
    }

    public Node getNode(int x, int y) {
        return nodes[x][y];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
}
