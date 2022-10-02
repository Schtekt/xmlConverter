import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;

public class App {
    public static void main(String[] args) throws ParserConfigurationException, Exception {
        System.out.println("Please enter file to read:");
        Scanner consoleReader = new Scanner(System.in);
        String srcFile = consoleReader.nextLine();

        String data = "";

        try {
            File myObj = new File(srcFile);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
              data += myReader.nextLine() + "\n";
            }
            myReader.close();
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred. Did you enter the correct file?");
            e.printStackTrace();
            consoleReader.close();
            return;
          }
          
          Register reg = new Register();
          if(!reg.ReadRowBased(data)) {
              System.out.println("There was an error while reading the file. Please make sure that the format is correct!");
              consoleReader.close();
              return;
            }
        System.out.println("\"" + srcFile + "\"" + " was found and read!");

        System.out.println("Please enter output file:");
        String dstFile = consoleReader.nextLine();
        consoleReader.close();

        try {
            FileWriter myWriter = new FileWriter(dstFile);
            myWriter.write(reg.OutputInXML());
            myWriter.close();
        } catch(IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return;
        }

        System.out.println("\"" + dstFile + "\"" + " was created (or overwritten)!");
    }
}