package greycat.samples.gps;

import com.sun.org.apache.bcel.internal.generic.Select;
import greycat.*;
import greycat.Type;
import greycat.Graph;
import greycat.GraphBuilder;
import greycat.Node;
import greycat.internal.task.math.MathExpressionEngine;
import greycat.internal.task.math.CoreMathExpressionEngine;

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
                                    .then(addToGlobalIndex("index_vehicle", "type")))


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
                                    .then(addToGlobalIndex("index_vehicle", "type")))


                    .execute(g, null);



            List<Vehicle_GPS> coord = new ArrayList();
            String travel_time = "5";

            newTask()    //reading index
                    .then(travelInTime(travel_time))
                    .then(indexNames())
                    //.then(readGlobalIndex("index_vehicle", "type", "Car"))
                    .then(readGlobalIndex("index_vehicle"))
                    .then(println("{{result}}"))
                    .pipe(  newTask().attribute("name"),
                            newTask().attribute("type"),
                            newTask().traverse("has_gps").attribute("longitude"),
                            newTask().traverse("has_gps").attribute("latitude"))
                    .flat()
                    //.traverse("has_gps","latitude","2.0")
                    .execute(g, new Callback<TaskResult>() {
                        @Override
                        public void on(TaskResult taskResult) {
                            for (int i = 0; i < taskResult.size()/4; i++) {
                                int j = taskResult.size()/4;
                                Vehicle_GPS data = new Vehicle_GPS((String) taskResult.get(i), (String) taskResult.get(i+j), (Double) taskResult.get(i+2*j) ,(Double) taskResult.get(i+3*j));
                                //System.out.println(data);
                                coord.add(data);
                            }
                         }
                    });
                    //.executeSync(g,g );
                    //.execute(g, null);

            System.out.println(coord);

            System.out.println("At time " + travel_time + " the following vehicles are created :");

            for (int i = 0; i < coord.size(); i++) {
                System.out.println(coord.get(i).name + " type " + coord.get(i).type + " with a longitude of " + coord.get(i).longitude + " and a latitude of " + coord.get(i).latitude + ".");
            }

            //test
            g.disconnect(result -> {
                System.out.println("Goodbye !");
            });
        });
    }
}