package greycat.samples.Battleground;

import greycat.Type;
import greycat.Graph;
import greycat.GraphBuilder;
import greycat.Node;

/**
 * Created by Quentin on 18/05/2017.
 */
public class MinimalClasses {


    public static void main(String[] args) {

        //Create a minimal graph with the default configuration
        Graph g = new GraphBuilder().build();

        //Connect the graph
        g.connect(isConnected -> {
            //Display that the graph database is connected!
            System.out.println("Connected : " + isConnected);

            long timepoint_0 = 0;  //the timestamp is a long and represents the time concept



            Node target1 = g.newNode(0, timepoint_0); //the second param is the time
            target1.set("id", Type.STRING, "2");
            target1.set("name", Type.STRING, "Target1");
            target1.set("Resilience", Type.DOUBLE, 5.0); //set the value of the sensor


            Node tank1 = g.newNode(0, timepoint_0); //the second param is the time
            tank1.set("id", Type.STRING, "1");
            tank1.set("name", Type.STRING, "Tank1");
            tank1.set("Power", Type.DOUBLE, 3.0); //set the value of the sensor



            long timepoint_1 = 100;
            tank1.travelInTime(timepoint_1, (Node tank1T1) -> {
                tank1T1.set("Power", Type.DOUBLE, 8.0); //update the value of the time now
                //Display the value at time 0
                System.out.println("T0:" + tank1.toString()); //print T0:{"world":0,"time":0,"id":1,"id":"4494F","name":"sensor0","value":0.5}
                System.out.println("T0:" + target1.toString());

                //Display the value at time now
                System.out.println("T1:" + tank1T1.toString()); //print T1:{"world":0,"time":100,"id":1,"id":"4494F","name":"sensor0","value":21.3}

                tank1.addToRelation("can_destroy", target1); //add the sensor0 to the relation sensors of room0

                tank1.relation("can_destroy", (Node[] can_destroy) -> {
                    System.out.println("Relationship can destroy :");
                    for (Node can_des : can_destroy) {
                        System.out.println("\t" + can_des.toString());
                    }
                });



                long timepoint_2= 200;
                tank1.travelInTime(timepoint_2, (Node tank1T2) ->{

                    tank1T2.set("Power", Type.DOUBLE, 10.0); //update the value of the time now
                    System.out.println("T2:" + tank1T2.toString()); //prints T2:{"world":0,"time":50,"id":1,"id":"4494F","name":"sensor0","value":0.5}
                });



            });

        });
    }




}
