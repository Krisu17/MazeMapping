//Michał Malinowski i Krzysztof Anderson, zespół 4

package mazemapping;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MazeWriter {

    char[][] wMaze;
    String readyMaze;
    Maze maze;
    int k;
    int l;
    File file;

    public MazeWriter(Maze maze, File file) {
        this.maze = maze;
        wMaze = new char[2 + 2 * maze.getHeight() - 1][2 + 2 * maze.getWidth() - 1];
        this.file = file;
    }

    public void Write() throws IOException {
        for (int i = 0; i < 2 * maze.getHeight() + 1; i++) {
            for (int j = 0; j < 2 * maze.getWidth(); j++) {
                wMaze[i][j] = '+';
            }
        }
        for (int i = 0; i < maze.getWidth(); i++) {
            k = 2 * i + 1;
            for (int j = 0; j < maze.getHeight(); j++) {
                l = 2 * j + 1;
                wMaze[l - 1][k + 1] = '+';
                wMaze[l + 1][k + 1] = '+';
                wMaze[l - 1][k - 1] = '+';
                wMaze[l + 1][k - 1] = '+';
                
                if(maze.getNode(i, j).isVisited())
                    wMaze[l][k] = '0';
                wMaze[l - 1][k] = maze.getNode(i, j).getU();
                wMaze[l + 1][k] = maze.getNode(i, j).getD();
                wMaze[l][k + 1] = maze.getNode(i, j).getR();
                wMaze[l][k - 1] = maze.getNode(i, j).getL();
            }
        }
        readyMaze = buildString(wMaze);
        usingBufferedWriter(readyMaze, file);
    }
    
    //tworzę stringa do zapisu z tablicy dwuwymiarowej czarów
    private String buildString(char[][] arrayOfCharArray) {
        StringBuilder sb = new StringBuilder();
        for (char[] subArray : arrayOfCharArray) {
            sb.append(subArray);
            sb.append('\n');
        }
        return sb.toString();
    }
    
    //zapisuję do pliku
    public static void usingBufferedWriter(String write, File file) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(write);
        writer.close();
    }
}
