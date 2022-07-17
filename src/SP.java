import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SP {
    private BufferedImage image;
    private Vector<Vector<HashMap<String, String>>> S = new Vector<>();
    private Vector<Vector<Integer>> P = new Vector<>();
    private String key;

    public SP(BufferedImage image, String key) {
        this.image = image;
        this.createKey(key);
    }

    public SP(BufferedImage image, int rounds) {
        this.image = image;
        generateKey(rounds);
    }

    public void cipher(int rounds, String path) throws IOException {
        String imageBinary = imageToString(image);
        int k = 0;
        StringBuilder sb;
        for (int round = 0; round < rounds; round++) {
            sb = new StringBuilder();
            String keyR = key.substring(k, k + 8);
            for (int i = 0; i < imageBinary.length(); i += 16) {
                String s = imageBinary.substring(i, i + 16);
                s = XOR(s, keyR);
                s = substitution(s, round);
                s = permutation(s, round);
                sb.append(s);
            }
            createImage(sb.toString(), round, path);
            imageBinary = sb.toString();
            k += 16;
        }
        sb = new StringBuilder();
        String keyR = key.substring(k, k + 8);
        for (int i = 0; i < imageBinary.length(); i += 16) {
            String s = imageBinary.substring(i, i + 16);
            s = XOR(s, keyR);
            s = substitution(s, rounds);
            sb.append(s);
        }
        createImage(sb.toString(), rounds, path);
    }

    public String imageToString(BufferedImage image) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                Color color = new Color(image.getRGB(j, i));
                int tmp = color.getRed();
                String s = Integer.toBinaryString(tmp);
                s = addZero(s, 8);
                sb.append(s);
            }
        }
        return sb.toString();
    }

    public void createImage(String line, int round, String path) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        int t = 0;
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                String s = line.substring(t, t+8);
                int tmp = Integer.parseInt(s, 2);
                Color color = new Color(tmp, tmp, tmp);
                bufferedImage.setRGB(j, i, color.getRGB());
                t += 8;
            }
        }
        ImageIO.write(bufferedImage, "bmp", new File(path + round +
                ".bmp"));
    }

    public String randomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int rnd = random.nextInt(2) ;
            sb.append(rnd);
        }
        return sb.toString();
    }

    public String addZero(String s, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length - s.length(); i++) {
            sb.append(0);
        }
        sb.append(s);
        return sb.toString();
    }

    public void S(int rounds) {
        for(int i=0;i<=rounds;i++) {
            S.add(new Vector<>());
            for (int j = 0; j < 4; j++) {
                S.get(i).add(new HashMap<>());
            }
            for (int k = 0; k < 16; k++) {
                String s = Integer.toBinaryString(k);
                s = addZero(s, 4);
                S.get(i).get(0).put(s, randomString(4));
                S.get(i).get(1).put(s, randomString(4));
                S.get(i).get(2).put(s, randomString(4));
                S.get(i).get(3).put(s, randomString(4));
            }
        }
    }

    public void P(int round) {
        for (int j = 0; j < round; j++) {
            P.add(new Vector<>());
        }
        Random random = new Random();
        for (int j = 0; j < round; j++) {
            Vector<Integer> v = P.get(j);
            for (int i = 0; i < 16; i++) {
                int value;
                do {
                    value = (random.nextInt(16)+random.nextInt(16))%16;
                } while (v.contains(value));
                v.add(value);
            }
            P.set(j, v);
        }
    }

    public static String XOR(String a, String b) {
        StringBuilder sb = new StringBuilder();
        StringBuilder a_right = new StringBuilder();
        for (int i = a.length()/2; i < a.length(); i++) {
            sb.append((a.charAt(i) + b.charAt(i-a.length()/2)) % 2);
            a_right.append(a.charAt(i));
        }
        String right = a_right.toString();
        String res_key = sb.toString();
        sb=new StringBuilder();sb.append(right);
        for(int k=0;k<a.length()/2;k++){
            sb.append((a.charAt(k) + res_key.charAt(k)) % 2);
        }

        return sb.toString();
    }

    public String permutation(String line, int round) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            sb.append(line.charAt(P.get(round).get(i)));
        }
        return sb.toString();
    }

    public String substitution(String plainText, int round) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < plainText.length(); i += 4) {
            String str = plainText.substring(i, i + 4);
            if(S.get(round).get(i/4).containsKey(str)){
                sb.append(S.get(round).get(i/4).get(str));
            }
        }
        return sb.toString();
    }

    public void createKey(String text) {
        byte[] arr = text.getBytes(StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder();
        for (byte b : arr) {
            String s = toBitString(b);
            sb.append(s);
        }
        this.key = sb.toString();
    }

    public void generateKey(int rounds){
        int size = rounds*8+8;
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i=0;i<size;i++){
            sb.append((random.nextInt(size)+random.nextInt(size))%2);
        }
        this.key = sb.toString();
    }

    public String toBitString(final byte val) {
        return String.format("%8s", Integer.toBinaryString(val &
                0xFF)).replace(' ', '0');
    }


}
