import java.io.BufferedReader;
import java.io.FileReader;

import com.kmuench.brainf.parser.BrainfInterpreter;

public class App {
    public static void main(String[] args) throws Exception {

        String pathFile = "resources\\printnumbers.bf";
        String fileContent = "";
        try(FileReader fs = new FileReader(pathFile); BufferedReader br = new BufferedReader(fs)) {
            String str;
            while ((str = br.readLine()) != null) {
                fileContent += str + "\n";
            }
        }


        BrainfInterpreter interpreter = new BrainfInterpreter(fileContent);
        interpreter.dumpProgram();
        interpreter.run();
    }
}
