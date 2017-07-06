package greycat.samples.gps;

import greycat.Callback;
import greycat.Graph;
import greycat.GraphBuilder;
import greycat.TaskResult;

import java.util.ArrayList;
import java.util.List;



import greycat.leveldb.LevelDBStorage;

import static greycat.Tasks.*;
import static greycat.internal.task.CoreActions.*;

import static greycat.Tasks.newTask;

/**
 * Created by Quentin on 29/06/2017.
 */
public class Query {

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
        String travel_time = "10";

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
                        int j = taskResult.size() / 4;
                        for (int i = 0; i < j; i++) {
                            Vehicle_GPS data = new Vehicle_GPS((String) taskResult.get(i), (String) taskResult.get(i + j), (Double) taskResult.get(i + 2 * j), (Double) taskResult.get(i + 3 * j));
                            //System.out.println(data);
                            coord.add(data);
                        }
                    }
                });
        //.executeSync(g,g );
        //.execute(g, null);

        System.out.println(coord);

        System.out.println("At time " + travel_time + " the following vehicles have been created :");

        for (int i = 0; i < coord.size(); i++) {
            System.out.println(coord.get(i).name + " type " + coord.get(i).type + " with a longitude of " + coord.get(i).longitude + " and a latitude of " + coord.get(i).latitude + ".");
        }





        for (int i = 0; i < coord.size(); i++) {

            List<Long> PreviousNode = new ArrayList();

            newTask()    //reading index
                    .then(travelInTime(travel_time))
                    .then(indexNames())
                    .then(readGlobalIndex("index_vehicle_name","name",coord.get(i).name))
                    .then(println("{{result}}"))

                    .pipe(
                            newTask().traverse("has_gps").timepoints("0",travel_time))
                    .flat()

                    .execute(g, new Callback<TaskResult>() {
                        @Override
                        public void on(TaskResult taskResult) {
                            int k = taskResult.size();
                            if (k != 0 ) {
                                Long time = (Long) taskResult.get(0);
                                //System.out.println(data);
                                PreviousNode.add(time);
                            }
                        }
                    });
            if (PreviousNode.size() !=0) {
                System.out.println(coord.get(i).name + " mofidied in " + PreviousNode.get(0));

            }
        }


        for (int i = 0; i < coord.size(); i++) {

            List<Long> NextNode = new ArrayList();

            newTask()    //reading index
                    .then(travelInTime(travel_time))
                    .then(indexNames())
                    .then(readGlobalIndex("index_vehicle_name","name",coord.get(i).name))
                    .then(println("{{result}}"))

                    .pipe(
                            newTask().traverse("has_gps").timepoints(travel_time, "200"))
                    .flat()

                    .execute(g, new Callback<TaskResult>() {
                        @Override
                        public void on(TaskResult taskResult) {
                            int k = taskResult.size();
                            if (k != 0 ) {
                                Long time = (Long) taskResult.get(k - 1);
                                //System.out.println(data);
                                NextNode.add(time);
                            }
                        }
                    });


            long nextTime = 0;

            if (NextNode.size() !=0) {
               System.out.println(coord.get(i).name + " will be mofidied in " + NextNode.get(0));
               nextTime =  NextNode.get(0);
           }else{
               System.out.println(coord.get(i).name + " won't be mofidied");
           }

           if (NextNode.size() !=0) {

               String nextTravel = Long.toString(nextTime);
               List<Double> nextCoord = new ArrayList();

               newTask()    //reading index
                       .then(travelInTime(nextTravel))
                       .then(indexNames())
                       .then(readGlobalIndex("index_vehicle_name", "name", coord.get(i).name))
                       .then(println("{{result}}"))
                       .pipe(
                               newTask().traverse("has_gps").attribute("longitude"),
                               newTask().traverse("has_gps").attribute("latitude"))
                       .flat()


                       .execute(g, new Callback<TaskResult>() {
                           @Override
                           public void on(TaskResult taskResult) {
                               Double nextLong = (Double) taskResult.get(0);
                               Double nextLat = (Double) taskResult.get(1);
                               //System.out.println(data);
                               nextCoord.add(nextLong);
                               nextCoord.add(nextLat);
                           }
                       });

               System.out.println(coord.get(i).name + " will have longitude" + nextCoord.get(0) + " and latitude " + nextCoord.get(1));
           }
           else {
               System.out.println(coord.get(i).name + " is always at the previous coord");


           }

        }












        g.disconnect(result -> {
            System.out.println("Goodbye !");
        });
    });

}
}