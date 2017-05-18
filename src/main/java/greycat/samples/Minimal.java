package greycat.samples;

import greycat.Type;
import greycat.Graph;
import greycat.GraphBuilder;
import greycat.Node;

public class Minimal {

    public static void main(String[] args) {

        //Create a minimal graph with the default configuration
        Graph g = new GraphBuilder().build();

        //Connect the graph
        g.connect(isConnected -> {
            //Display that the graph database is connected!
            System.out.println("Connected : " + isConnected);

            Node sensor0 = g.newNode(0, 0); //create a new node for world 0 and time 0
            sensor0.set("snesorId", Type.INT, 12); //set the id attribute as an integer
            sensor0.set("name",Type.STRING, "sensor0"); //set the name attribute as a string

            //Display the first node we created
            System.out.println(sensor0); //print {"world":0,"time":0,"id":1,"snesorId":12,"name":"sensor0"}

            Node room0 = g.newNode(0, 0); //create new node for world 0 and time 0
            room0.set("name",Type.STRING, "room0"); //set the name attribute
            room0.addToRelation("sensors", sensor0); //add the sensor0 to the relation sensors of room0


            //Let's display the room0 node to see what's inside
            System.out.println(room0); //print {"world":0,"time":0,"id":2,"name":"room0","sensors":[1]}


            Node test0 = g.newNode(0, 0);
            test0.set("name",Type.STRING, "test0");
            test0.addToRelation("rooms", room0);
            System.out.println(test0);

            //iterate over the saved sensors relation from room0
            room0.relation("sensors", (Node[] sensors) -> {
                System.out.println("Relationship Sensors:");
                for (Node sensor : sensors) {
                    System.out.println("\t" + sensor.toString());
                        }
            });



            test0.relation("rooms", (Node[] rooms) -> {
                System.out.println("Relationship rooms:");
                for (Node room : rooms) {
                    System.out.println("\t" + room.toString());
                }
            });

                //Disconnect the database
                g.disconnect(result -> {
                    System.out.println("GoodBye!");

            });

        });
    }

}
