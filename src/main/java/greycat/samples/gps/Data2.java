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
 * Created by Quentin on 08/06/2017.
 */
public class Data2 {


    public static void main(String[] args) {

        //Create a minimal graph with the default configuration
        Graph g = new GraphBuilder().build();

        //Connect the graph
        g.connect(isConnected -> {
            //Display that the graph database is connected!
            System.out.println("Connected : " + isConnected);



            String vehicle_array[] = {"vehicle1", "vehicle2", "vehicle3" , "vehicle4", "vehicle5", "vehicle6"};

            String type_vehicle_array[] = {"Plane", "Car", "Train", "Plane", "Car", "Train"};

            Double gps_array_vehicle[][] = {{0.0, 0.0, 5.0 , 5.0},{1.5, 0.0, 2.0 , 3.0},{0.0, 1.5, 4.0 , 1.0},{5.0, 2.0, 4.5 , 3.0},{3.0, 3.5, 0.0 , 0.0},{1.0, 2.0, 3.0 , 4.5}};

            int timepoint_array[] ={0,1,2,3,4,5,6};  //the timestamp is a long and represents the time concept



            newTask()
                    .loop("0", "5",
                            newTask()
                                    .then(createNode())
                                    .then(travelInTime("0"))
                                    .then(setAttribute("longitude", Type.DOUBLE, "{{i}}"))
                                    .then(setAttribute("latitude", Type.DOUBLE, "{{i}}"))
                                    .then(println("{{result}}"))
                                    .defineAsVar("data_gps")

                                    .then(createNode())
                                    .then(setAttribute("name", Type.STRING, "TGV_{{i}}"))
                                    .then(setAttribute("type", Type.STRING, "Train"))
                                    //.then(println("{{result}}"))

                                    .then(addVarToRelation("has_gps", "data_gps"))
                                    .then(println("{{result}}"))

                                    .then(readVar("data_gps"))
                                    .then(travelInTime("{{=4*i}}"))
                                    .then(setAttribute("longitude", Type.DOUBLE, "{{=i+2}}"))
                                    .then(setAttribute("latitude", Type.DOUBLE, "{{=i-2}}"))
                                    .then(println("{{result}}")))
                    .execute(g, null);

            newTask()
                    .loop("0", "4",
                            newTask()
                                    .then(createNode())
                                    .then(travelInTime("0"))
                                    .then(setAttribute("longitude", Type.DOUBLE, "{{i}}"))
                                    .then(setAttribute("latitude", Type.DOUBLE, "{{i}}"))
                                    .then(println("{{result}}"))
                                    .defineAsVar("data_gps")

                                    .then(createNode())
                                    .then(setAttribute("name", Type.STRING, "Audi_{{i}}"))
                                    .then(setAttribute("type", Type.STRING, "Car"))
                                    //.then(println("{{result}}"))

                                    .then(addVarToRelation("has_gps", "data_gps"))
                                    .then(println("{{result}}"))

                                    .then(readVar("data_gps"))
                                    .then(travelInTime("{{=3*i}}"))
                                    .then(setAttribute("longitude", Type.DOUBLE, "{{=i+1}}"))
                                    .then(setAttribute("latitude", Type.DOUBLE, "{{=i-2}}"))
                                    .then(println("{{result}}")))
                    .execute(g, null);




        });
    }
}