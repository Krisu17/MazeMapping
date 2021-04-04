//Michał Malinowski i Krzysztof Anderson, zespół 4

package mazemapping;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Main {
    public static final String writingPath = "src/resources/maze.txt";
    private static final File fileToWrite = new File(writingPath);
    
    public static void main(String[] args) throws IOException {
        MazeMapper mazeMapper = new MazeMapper(3);
        mazeMapper.explore();
        MazeWriter writer = new MazeWriter(mazeMapper.getMaze(), fileToWrite);
        try {
            writer.Write();
        } catch (FileNotFoundException ex) {
            System.out.println("Nie można uzyskać dostępu do wskazanej ścieżki");
        }
        mazeMapper.sendMaze(writingPath);
    }
    
}
