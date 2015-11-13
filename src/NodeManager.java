import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Mohammed on 09/11/2015.
 */
public class NodeManager implements Runnable {
    public static final int PORT_NUMBER_START = 7788;
    public static final String HOST_NAME = "localhost";

    private int portNumber;
    private int numOfNodes;
    private String broadcastHeader = "N#";
    private Node node;
    private ServerSocket serverSocket;
    private Socket listeningSocket;
    private Socket broadcastingSocket;
    private ObjectOutputStream serverOutput;
    private ObjectInputStream clientInput;

    public NodeManager(int portNumber, int numOfNodes, int nodeId) throws IOException{
        this.portNumber = portNumber;
        this.serverSocket = new ServerSocket(portNumber);
        //this.serverSocket.setSoTimeout(1000);
        this.broadcastHeader += Integer.toString(nodeId) + ": ";

        if(nodeId == numOfNodes)
            this.node = new Node(nodeId, 1);
        else
            this.node = new Node(nodeId, nodeId+1);

    }

    @Override
    public void run() {
        boolean run = true;
        boolean firstMsg = false;
        boolean serverConnected = false;
        boolean clientConnected = false;
        Message message = null;
        while (true){
            try {
                if(node.getId() == 1) {
                    while (!serverConnected) {
                        broadcastingSocket = serverSocket.accept();
                        serverConnected = true;
                    }
                }
                while (!clientConnected){
                    if (node.getNeighbourId() == 1)
                        listeningSocket = new Socket(HOST_NAME, PORT_NUMBER_START);

                    else
                        listeningSocket = new Socket(HOST_NAME, (PORT_NUMBER_START + node.getNeighbourId() - 1));

                    if (listeningSocket.isConnected()){
                        System.out.println(broadcastHeader + "connected to N#" + node.getNeighbourId() + ".");

                        clientConnected = true;
                    }
                }
                if (!serverConnected) {
                    broadcastingSocket = serverSocket.accept();
                    if (broadcastingSocket.isConnected()) {
                        System.out.println(broadcastHeader + "node N#" + node.getNeighbourId() + " connected.");
                        serverConnected = true;
                    }
                }


                if(serverConnected && clientConnected){
                    if(node.getId() == 1){
                        if (!firstMsg){
                            serverOutput = new ObjectOutputStream(broadcastingSocket.getOutputStream());
                            serverOutput.writeObject(new Message(1, "send", node.getWeight(), node.getId()));
                            firstMsg = true;
                        }
                    }
                    if(listeningSocket.getInputStream().available() > 0){
                        clientInput = new ObjectInputStream(listeningSocket.getInputStream());
                        message = (Message) clientInput.readObject();

                    }


                    if(!(message == null)){
                        //System.out.println(broadcastHeader + "received message with id: " + message.getId() + "***" + message.toString());
                        Message processedMessage = this.node.processMessage(message);
                        message = null;
                        if(!(processedMessage == null)){
                            serverOutput = new ObjectOutputStream(broadcastingSocket.getOutputStream());
                            serverOutput.writeObject(processedMessage);
                        }
                    }

                }

            }catch (IOException | ClassNotFoundException | NullPointerException e){
                System.out.println(broadcastHeader + e.toString());
            }
        }
    }


    public static void main(String[] args) throws InterruptedException{
        int numbOfNodes = Integer.parseInt(args[0]);
        ArrayList<Thread> threads = new ArrayList<>();

        for (int i =1; i <= numbOfNodes; i++){
            try {
                if(i == 1)
                    threads.add(new Thread(new NodeManager(PORT_NUMBER_START, numbOfNodes, i)));
                else
                    threads.add(new Thread(new NodeManager((PORT_NUMBER_START+i-1), numbOfNodes, i)));
            }catch (IOException e){
                e.printStackTrace();
                System.out.println("1" + e.toString());
            }
        }

        for(Thread thread: threads){
            thread.start();
            Thread.sleep(300);
        }


    }
}
