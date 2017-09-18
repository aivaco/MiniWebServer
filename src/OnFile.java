import org.apache.tika.Tika;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    /***
     * Takes a path directory and verifies if each subfolder exists before check file existence.
     * @param urlParts
     * @return
     */
    public boolean fileExists(String[] urlParts){
        String urlPart = ".//";
        boolean exists = true;

//        if(urlParts[0].isEmpty()){
//            return exists;
//        }

        for(int i = 0 ; i<urlParts.length; ++i){
            urlPart = urlPart + urlParts[i];
            file = new File(urlPart);

            if(i == urlParts.length - 1 && exists){

                if(file.exists() && !file.isDirectory()){
                    exists = true;
                }
                else{
                    exists = false;
                }
            }
            else if(exists && file.exists() && file.isDirectory()){
                exists = true;
            }
            else{
                exists = false;
            }
        }

        return exists;
    }

    public byte[] readBytesFromFile(String name){
        file = new File(".//" +name);
        try {
            return org.apache.commons.io.FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String checkMIME(String name){
        String MIME = "";
        MIME = System.getProperty("user.dir")+"\\"+name;
        //Path path = Paths.get(".//"+name);
        Tika tika = new Tika();
        file = new File(System.getProperty("user.dir")+"\\"+name);

        try {
            MIME = tika.detect(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return MIME;
    }

}
