import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class tTest {
    private BufferedImage image;

    public tTest(BufferedImage image) {
        this.image = image;
    }

    public void doTest(char round)throws IOException {
        Vector<Integer> A = new Vector<>();
        Vector<Integer> B = new Vector<>();
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                if (i % 2 == 0 && j % 2 == 0) {
                    A.add(new Color(image.getRGB(i, j)).getRed());
                } else if(i % 2 == 1 && j % 2 == 1){
                    B.add(new Color(image.getRGB(i, j)).getRed());
                }
            }
        }
        toFile(String.valueOf(round)+"A.txt",A);
        toFile(String.valueOf(round)+"B.txt",B);
    }

    public void toFile(String name, Vector<Integer> rec) throws IOException {
        File f = new File(name);
        FileWriter fw = new FileWriter(f);
        for (Integer aInteger : rec) {
            fw.append(aInteger.toString()).append("\n");
        }
        fw.close();

    }
}
