package greycat.samples.gps;

import greycat.Callback;
import greycat.Graph;
import greycat.GraphBuilder;
import greycat.TaskResult;

import java.util.ArrayList;
import java.util.List;


import greycat.*;
import greycat.Type;
import greycat.Graph;
import greycat.GraphBuilder;
import greycat.Node;
import greycat.internal.task.math.MathExpressionEngine;
import greycat.internal.task.math.CoreMathExpressionEngine;
import greycat.leveldb.LevelDBStorage;

import static greycat.Tasks.*;
import static greycat.internal.task.CoreActions.*;

import static greycat.Tasks.newTask;

/**
 * Created by Quentin on 29/06/2017.
 */
public class Request {

    public static void main(String[] args) {

        Graph g = new GraphBuilder()
                .withMemorySize(10000) //cache size before sync to disk
                .withStorage(new LevelDBStorage("greycat_db_test")) //location to store on disc
                .build();



        g.connect(isConnected ->

    {
        //Display that the graph database is connected!
        System.out.println("Connected : " + isConnected);

        List<Vehicle_GPS> coord = new ArrayList();
        String travel_time = "5";

        newTask()    //reading index
                .then(travelInTime(travel_time))
                .then(indexNames())
                //.then(readGlobalIndex("index_vehicle", "type", "Car"))
                .then(readGlobalIndex("index_vehicle"))
                .then(println("{{result}}"))
                .pipe(newTask().attribute("name"),
                        newTask().attribute("type"),
                        newTask().traverse("has_gps").attribute("longitude"),
                        newTask().traverse("has_gps").attribute("latitude"))
                .flat()
                //.traverse("has_gps","latitude","2.0")


                .execute(g, new Callback<TaskResult>() {
                    @Override
                    public void on(TaskResult taskResult) {
                        for (int i = 0; i < taskResult.size() / 4; i++) {
                            int j = taskResult.size() / 4;
                            Vehicle_GPS data = new Vehicle_GPS((String) taskResult.get(i), (String) taskResult.get(i + j), (Double) taskResult.get(i + 2 * j), (Double) taskResult.get(i + 3 * j));
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

        g.disconnect(result -> {
            System.out.println("Goodbye !");
        });
    });

}
}