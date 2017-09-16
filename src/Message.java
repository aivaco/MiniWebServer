/**
 * Created by aivan on 15/09/2017.
 */
public class Message {

    Type type;

    public Message(int type){
        switch(type){
            case 1:
                this.type = this.type.POST;
                break;
            case 2:
                this.type = this.type.GET;
                break;
            case 3:
                this.type = this.type.HEAD;
                break;
            default:
                this.type = this.type.ERROR;
        }
    }

    public void proccessMessage(){
    }

    public enum Type {
        POST,
        GET,
        HEAD,
        ERROR
    }
}
