/**
 * It is used to manage the log file.
 */
public class Log {

    //private ArrayList<EntryLog> entries;
    private OnFile file;
    private String log_name = "log";
    private String file_extension = "txt";
    private String filename;

    public Log(){
        this.file = new OnFile();
        file.createFile(log_name,file_extension);
        filename = log_name+"."+file_extension;
        file.writeLineInFile("Método\tEstampilla de Tiempo\tServidor\tRefiere\tURL\tDatos",this.filename);
    }

    public void writeAnEntry(EntryLog entry){
        String fileEntry = "";
        fileEntry = entry.getMethod() + "\t" + entry.getTimeStamp() + "\t" + entry.getServer()  + "\t" + entry.getRefer()  + "\t" + entry.getUrl() + "\t" + entry.getDatos();
        this.file.writeLineInFile(fileEntry,this.filename);
    }
}
