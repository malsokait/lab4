import java.util.Random;

/**
 * Created by Mohammed on 09/11/2015.
 */
public class Node {
    private int id;

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    private float weight;
    private int neighbourId;
    private Message message;

    public Node(int id, int neighbourId) {
        this.id = id;
        this.weight = (new Random()).nextFloat();
        this.neighbourId = neighbourId;
        this.message = new Message(weight);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNeighbourId() {
        return neighbourId;
    }

    public void setNeighbourId(int neighbourId) {
        this.neighbourId = neighbourId;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Message processMessage(Message message){
        String response = "MSG#" + message.getId() + " ";
        if(this.id == message.getElectedId()){
            response += "All nodes agreed to elect me!";
            System.out.println("N#" + Integer.toString(this.id)+ " "+ response);
            return null;
        }

        else if(this.message.getWeight() < message.getWeight()){
            response += "Received higher weight, electing node " + message.getElectedId();
        }
        else {
            response += "Received lower weight, electing myself.";
            message.setElectedId(this.id);
            message.setWeight(this.weight);
        }

        System.out.println("N#" + Integer.toString(this.id)+ " "+ response);
        message.setId(message.getId()+1);
        message.setType("send");
        this.message = message;

        return this.message;
    }
}
