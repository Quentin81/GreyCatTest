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


            newTask()     //add node train
                    .loop("0", "5",
                            newTask()
                                    .then(createNode())
                                    .then(travelInTime("{{i}}"))
                                    .then(setAttribute("longitude", Type.DOUBLE, "{{i}}"))
                                    .then(setAttribute("latitude", Type.DOUBLE, "{{i}}"))
                                    .then(println("{{result}}"))
                                    .defineAsVar("data_gps")

                                    .then(createNode())
                                    .then(setAttribute("name", Type.STRING, "TGV_{{i}}"))
                                    .then(setAttribute("type", Type.STRING, "Train"))
                                    .defineAsVar("nodes_train")
                                    //.then(println("{{result}}"))

                                    .then(addVarToRelation("has_gps", "data_gps"))
                                    .then(println("{{result}}"))

                                    .then(readVar("data_gps"))
                                    .then(travelInTime("{{=4*i}}"))
                                    .then(setAttribute("longitude", Type.DOUBLE, "{{=i+2}}"))
                                    .then(setAttribute("latitude", Type.DOUBLE, "{{=i-2}}"))
                                    .then(println("{{result}}"))

                                    .then(readVar("nodes_train"))
                                    .then(addToGlobalIndex("index_vehicle","type")))


                    .execute(g, null);

            newTask()    //add node car
                    .loop("0", "4",
                            newTask()
                                    .then(createNode())
                                    .then(travelInTime("{{=2*i}}"))
                                    .then(setAttribute("longitude", Type.DOUBLE, "{{i}}"))
                                    .then(setAttribute("latitude", Type.DOUBLE, "{{i}}"))
                                    .then(println("{{result}}"))
                                    .defineAsVar("data_gps")

                                    .then(createNode())
                                    .then(setAttribute("name", Type.STRING, "Audi_{{i}}"))
                                    .then(setAttribute("type", Type.STRING, "Car"))
                                    .defineAsVar("nodes_car")
                                    //.then(println("{{result}}"))

                                    .then(addVarToRelation("has_gps", "data_gps"))
                                    .then(println("{{result}}"))

                                    .then(readVar("data_gps"))
                                    .then(travelInTime("{{=3*i}}"))
                                    .then(setAttribute("longitude", Type.DOUBLE, "{{=i+1}}"))
                                    .then(setAttribute("latitude", Type.DOUBLE, "{{=i-2}}"))

                                    .then(println("{{result}}"))
                                    .then(readVar("nodes_car"))
                                    .then(addToGlobalIndex("index_vehicle","type")))


                    .execute(g, null);

            newTask()    //reading index
                    .then(travelInTime("5"))
                    .then(indexNames())
                    .then(readGlobalIndex("index_vehicle"))
                    .then(println("{{result}}"))

                    .execute(g, null);

        });
    }
}