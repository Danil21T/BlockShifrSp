import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        int rounds = 4;
        SP s = new SP(ImageIO.read(new File("oogvei.png")),"Случайности не случайны");
        s.S(rounds);
        s.P(rounds);
        s.cipher(rounds,"kodim");
        CorrelationProperties cp;
        cp = new CorrelationProperties(ImageIO.read(new File("oogvei.png")));
        cp.autoCorrelation('r', "Orig");
        for(int i=0;i<=rounds;i++){
            cp = new CorrelationProperties(ImageIO.read(new File("kodim"+i+".bmp")));
            cp.autoCorrelation('r',""+i);
        }
        tTest tt=new tTest(ImageIO.read(new File("oogvei.png")));
        tt.doTest('-');
        for(int i=0;i<=rounds;i++){
            tt = new tTest(ImageIO.read(new File("kodim"+i+".bmp")));
            tt.doTest(String.valueOf(i).charAt(0));
        }
    }
}
