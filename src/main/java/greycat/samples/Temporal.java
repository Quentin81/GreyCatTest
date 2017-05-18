package greycat.samples;

import greycat.Graph;
import greycat.GraphBuilder;
import greycat.Node;
import greycat.Type;

public class Temporal {

    public static void main(String[] args) {

        //Create a minimal graph with the default configuration
        Graph g = new GraphBuilder().build();
        //Connect the graph
        g.connect(isConnected -> {
            //Display that the graph database is connected!
            System.out.println("Connected : " + isConnected);

            long timepoint_0 = 0;  //the timestamp is a long and represents the time concept

            Node sensor0 = g.newNode(0, timepoint_0); //the second param is the time
            sensor0.set("id", Type.STRING, "4494F");
            sensor0.set("name", Type.STRING, "sensor0");
            sensor0.set("value", Type.DOUBLE, 0.5); //set the value of the sensor


            long timepoint_1 = 100;
            sensor0.travelInTime(timepoint_1, (Node sensor0T1) -> {
                sensor0T1.set("value", Type.DOUBLE, 21.3); //update the value of the time now
                //Display the value at time 0
                System.out.println("T0:" + sensor0.toString()); //print T0:{"world":0,"time":0,"id":1,"id":"4494F","name":"sensor0","value":0.5}
                //Display the value at time now
                System.out.println("T1:" + sensor0T1.toString()); //print T1:{"world":0,"time":100,"id":1,"id":"4494F","name":"sensor0","value":21.3}


                long timepoint_2= 50;
                sensor0.travelInTime(timepoint_2, (Node sensor0T2) ->{
                    System.out.println("T2:" + sensor0T2.toString()); //prints T2:{"world":0,"time":50,"id":1,"id":"4494F","name":"sensor0","value":0.5}
                });



            });

        });
    }

}
