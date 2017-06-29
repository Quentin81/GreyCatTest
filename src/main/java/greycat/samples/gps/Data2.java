package greycat.samples.gps;

import com.sun.org.apache.bcel.internal.generic.Select;
import greycat.*;
import greycat.Type;
import greycat.Graph;
import greycat.GraphBuilder;
import greycat.Node;
import greycat.internal.task.math.MathExpressionEngine;
import greycat.internal.task.math.CoreMathExpressionEngine;
import greycat.leveldb.LevelDBStorage;

import java.util.ArrayList;
import java.util.List;

import static greycat.Tasks.*;
import static greycat.internal.task.CoreActions.*;

import static greycat.Tasks.newTask;


/**
 * Created by Quentin on 08/06/2017.
 */


    public class Data2 {


    public static void main(String[] args) {

        //Create a minimal graph with the default configuration
        Graph g = new GraphBuilder()
                .withMemorySize(10000) //cache size before sync to disk
                .withStorage(new LevelDBStorage("greycat_db_test")) //location to store on disc
                .build();

        //Connect the graph
        g.connect(isConnected -> {
            //Display that the graph database is connected!
            System.out.println("Connected : " + isConnected);


            newTask()     //add node train
                    .loop("0", "10",
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
                                    .then(addToGlobalIndex("index_vehicle", "type")))


                    .execute(g, null);

            newTask()    //add node car
                    .loop("0", "10",
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
                                    .then(addToGlobalIndex("index_vehicle", "type")))


                    .execute(g, null);


            //test
            g.disconnect(result -> {
                System.out.println("Goodbye !");
            });
        });
    }
}