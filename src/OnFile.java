import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


/**
 * It manages all the functions related to the file that has to be created.
 */
public class OnFile {

    private File file;
    //private File url;



    /**
     * Creates the file with a respective name in local path.
     *
     * @param name
     * @param type
     */
    public void createFile(String name, String type) {
        try {
            if(!type.isEmpty())
                file = new File(".//" + name + "." + type);
            else
                file = new File(".//" + name);
            if (file.createNewFile()) {
                System.out.println("Archivo " + name + "." + type + " creado exitosamente");
            } else {
                System.out.println("Error al crear el archivo " + name + "." + type);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }



    /**
     * It's only used for write in a file.
     *
     * @param data
     */
    private void writeInFile(String data, String filename) {
        try {
            file = new File(".//" + filename);
            FileWriter writer = new FileWriter(file, true);
            writer.write(data);
            writer.flush();
            writer.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }


    /**
     * Writes a line in a specific file.
     *
     * @param data
     */
    public void writeLineInFile(String data, String filename) {
        data = data + "\n";
        Charset.forName("UTF-8").encode(data);              //Sets the string to UTF-8.
        writeInFile(data, filename);
    }

    /**
     * Reads a file and put line by line in a list.
     *
     * @param name
     * @return
     */
    public List<String> readFile(String name) {

        List<String> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(name))) {
            String line;
            while ((line = br.readLine()) != null) {
                // process the line.
                list.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean clearFile(String name, String type) {

        file = new File(".//" + name + "." + type);
        if(file.delete()) {

        }

        return true;
    }


}
