package greycat.samples.gps;

import greycat.*;

import static greycat.Tasks.newTask;
import static greycat.internal.task.CoreActions.*;
import static greycat.internal.task.CoreActions.addToGlobalIndex;

/**
 * Created by Quentin on 04/07/2017.
 */
public class TestTimePoint {

    public static void main(String[] args) {


        Graph g = new GraphBuilder().build();

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

                                    .then(readVar("data_gps"))
                                    .then(travelInTime("{{=6*i}}"))
                                    .then(setAttribute("longitude", Type.DOUBLE, "{{=i+12}}"))
                                    .then(setAttribute("latitude", Type.DOUBLE, "{{=i-22}}"))
                                    .then(println("{{result}}"))

                                    .then(readVar("data_gps"))
                                    .then(travelInTime("{{=8*i}}"))
                                    .then(setAttribute("longitude", Type.DOUBLE, "{{=i+2}}"))
                                    .then(setAttribute("latitude", Type.DOUBLE, "{{=i-2}}"))
                                    .then(println("{{result}}"))

                                    .then(readVar("data_gps"))
                                    .then(travelInTime("{{=10*i}}"))
                                    .then(setAttribute("longitude", Type.DOUBLE, "{{=i+32}}"))
                                    .then(setAttribute("latitude", Type.DOUBLE, "{{=i-35}}"))
                                    .then(println("{{result}}"))


                                    .then(readVar("nodes_train"))
                                    .then(addToGlobalIndex("index_vehicle", "type"))
                                    .then(addToGlobalIndex("index_vehicle_name", "name")))

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
                                    .then(addToGlobalIndex("index_vehicle", "type"))
                                    .then(addToGlobalIndex("index_vehicle_name", "name")))

                    .execute(g, null);

            String travel_time = "13";

            newTask()    //reading index
                    .then(travelInTime(travel_time))
                    .then(indexNames())
                    .then(readGlobalIndex("index_vehicle_name","name","TGV_2"))
                    .then(println("{{result}}"))
                    .then(traverse("has_gps"))
                    .then(println("{{result}}"))
                    .defineAsVar("node_gps")
                    .then(timepoints("-10" , travel_time))
                    .then(println("{{result}}"))
                    .then(readVar("node_gps"))
                    .then(println("{{result}}"))
                    .then(timepoints(travel_time, "200"))
                    .then(println("{{result}}"))
                    .execute(g, null);



            System.out.println("At time " + travel_time + " the following vehicles have been created :");


            g.disconnect(result -> {
                System.out.println("Goodbye !");
            });

        });
    }
}
