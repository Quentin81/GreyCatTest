package greycat.samples.gps;

import greycat.*;
import greycat.Type;
import greycat.Graph;
import greycat.GraphBuilder;
import greycat.Node;
import greycat.internal.task.math.MathExpressionEngine;
import greycat.internal.task.math.CoreMathExpressionEngine;
import static greycat.Tasks.*;
import static greycat.internal.task.CoreActions.*;

import static greycat.Tasks.newTask;

/**
 * Created by Quentin on 07/06/2017.
 */
public class Data {


    public static void main(String[] args) {

        //Create a minimal graph with the default configuration
        Graph g = new GraphBuilder().build();

        //Connect the graph
        g.connect(isConnected -> {
            //Display that the graph database is connected!
            System.out.println("Connected : " + isConnected);


            //TRYING HERE !!!


            long timepoint_0 = 0;  //the timestamp is a long and represents the time concept



            String tankarray[] = {"tank1", "tank2", "tank3" , "tank4", "tank5", "tank6"};

            String targetarray[] = {"target1", "target2", "target3" , "target4"};

            double j = 1.0;

            for(int i = 0; i < targetarray.length; i++) {

                System.out.println("À l'emplacement " + i + " du tableau nous avons = " + targetarray[i]);


                Node temp = g.newNode(0, timepoint_0); //the second param is the time
                temp.set("type", Type.STRING, "target");
                temp.set("name", Type.STRING, targetarray[i]);
                temp.set("Resilience", Type.DOUBLE, j); //set the value of the sensor
                System.out.println(temp.toString());
                j = j + 5.0;

            }




            double k = 0.0;

            for(int i = 0; i < tankarray.length; i++){


                System.out.println("À l'emplacement " + i + " du tableau nous avons = " + tankarray[i]);
                Node temp2 = g.newNode(0, timepoint_0); //the second param is the time
                temp2.set("type", Type.STRING,"tank" );
                temp2.set("name", Type.STRING, tankarray[i]);
                temp2.set("Power", Type.DOUBLE, k); //set the value of the sensor
                System.out.println(temp2.toString());
                k=k+5.0;



            }


            long timepoint_1 = 100;

        });
    }




}
