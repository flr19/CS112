 package lse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
public class driver {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub

        LittleSearchEngine engine= new LittleSearchEngine();
		/*ArrayList<Occurrence>occs = new ArrayList<Occurrence>();
		Occurrence a = 12;
		occs.add(0,12);*/
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter document file name => ");//docs.txt
        String docsFile="docs.txt";
        System.out.print("Enter noise words file name => ");//noisewords.txt
        String noiseWordsFile="noisewords.txt";
        engine.makeIndex(docsFile, noiseWordsFile);
//        System.out.println(engine.top5search("red", "car"));
//        System.out.println(engine.getKeyword("sWorld"));
//        System.out.println(engine.getKeyword("paraphrase"));
//        System.out.println(engine.getKeyword("really?!?!"));
//        System.out.println(engine.getKeyword("Between,"));
//        System.out.println(engine.getKeyword("either:or"));
    }
}
