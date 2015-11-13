import java.io.Serializable;

/**
 * Created by Mohammed on 09/11/2015.
 */
public class Message implements Serializable{
    private int id;
    private String type;

    public int getElectedId() {
        return electedId;
    }

    public void setElectedId(int electedId) {
        this.electedId = electedId;
    }

    private int electedId;
    private float weight;

    public Message(int id, String type, float weight, int electedId) {
        this.id = id;
        this.type = type;
        this.weight = weight;
        this.electedId = electedId;
    }

    public Message(float weight){
        this.id = -1;
        this.type = "";
        this.weight = weight;
        this.electedId = -1;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }


    public boolean isSent( ){
        return (type == "send");
    }

    public boolean isReceived(){
        return (type == "receive");
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", electedId=" + electedId +
                ", weight=" + weight +
                '}';
    }
}
