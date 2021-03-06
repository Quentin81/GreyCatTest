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

import greycat.*;
import greycat.struct.Buffer;
import greycat.websocket.WSServer;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;

import greycat.chunk.StateChunk;
import greycat.base.BaseNode;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.CountDownLatch;

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

        WSServer graphServer = new WSServer(g, 3000);
        graphServer.addHandler("hello", new ResourceHandler(new ClassPathResourceManager(g.getClass().getClassLoader(), "hello")).addWelcomeFiles("index.html").setDirectoryListingEnabled(true));
        graphServer.start();

        //Display that the graph database is connected!
        System.out.println("Connected : " + isConnected);

        List<Vehicle_GPS> coord = new ArrayList();
        String travel_time = "11";

        newTask()    //reading index
                .then(travelInTime(travel_time))
                .then(indexNames())
                //.then(readGlobalIndex("index_vehicle", "type", "Car"))
                .then(readIndex("index_vehicle"))
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


            List<Long> PreviousNode = new ArrayList();

            newTask()    //reading index
                    .then(travelInTime(travel_time))
                    .then(indexNames())
                    .then(readIndex("index_vehicle_name", coord.get(i).name))
                    //.then(println("{{result}}"))

                    .pipe(
                            newTask().traverse("has_gps").timepoints("0", travel_time))
                    .flat()

                    .execute(g, new Callback<TaskResult>() {
                        @Override
                        public void on(TaskResult taskResult) {
                            int k = taskResult.size();
                            if (k != 0) {
                                Long time = (Long) taskResult.get(0);
                                //System.out.println(data);
                                PreviousNode.add(time);
                            }
                        }
                    });

            if (PreviousNode.size() == 0) {

                Long travel_time_long = Long.parseLong(travel_time);
                PreviousNode.add(travel_time_long);

            }

            System.out.println(coord.get(i).name + " mofidied in " + PreviousNode.get(0));


            List<Long> NextNode = new ArrayList();

            newTask()    //reading index
                    .then(travelInTime(travel_time))
                    .then(indexNames())
                    .then(readIndex("index_vehicle_name", coord.get(i).name))
                    //.then(println("{{result}}"))

                    .pipe(
                            newTask().traverse("has_gps").timepoints(travel_time, "200"))
                    .flat()

                    .execute(g, new Callback<TaskResult>() {
                        @Override
                        public void on(TaskResult taskResult) {
                            int k = taskResult.size();
                            if (k != 0) {
                                Long time = (Long) taskResult.get(0);
                                //System.out.println(data);
                                NextNode.add(time);
                            }
                        }
                    });


            long nextTime = -1;

            if (NextNode.size() != 0) {
                System.out.println(coord.get(i).name + " will be mofidied in " + NextNode.get(0));
                nextTime = NextNode.get(0);
            } else {
                System.out.println(coord.get(i).name + " won't be mofidied");
            }


            List<Double> nextCoord = new ArrayList();

            if (NextNode.size() != 0) {

                String nextTravel = Long.toString(nextTime);


                newTask()    //reading index
                        .then(travelInTime(nextTravel))
                        .then(indexNames())
                        .then(readIndex("index_vehicle_name", coord.get(i).name))
                        //.then(println("{{result}}"))
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

                System.out.println(coord.get(i).name + " will have longitude " + nextCoord.get(0) + " and latitude " + nextCoord.get(1));

            } else {
                System.out.println(coord.get(i).name + " is always at the previous coord");
            }
            //Extrapolation Calcul :

            Double travel_time_double = Double.parseDouble(travel_time);

            double previous_time_double = (double) PreviousNode.get(0);

            double next_time_double = (double) nextTime;


            if ( next_time_double == -1) {


                System.out.println("According our extrapolation, at time = " + travel_time_double + ", the vehicle " + coord.get(i).name + " has a longitude of " + coord.get(i).longitude + " and a latitude of " + coord.get(i).latitude);


            }else if ( previous_time_double == travel_time_double) {

                System.out.println("According our extrapolation, at time = " + travel_time_double + ", the vehicle " + coord.get(i).name + " has a longitude of " + coord.get(i).longitude + " and a latitude of " + coord.get(i).latitude);


            }else {

                double extra_long = ((travel_time_double - previous_time_double) / (next_time_double - previous_time_double)) * (nextCoord.get(0)) + ((next_time_double - travel_time_double) / (next_time_double - previous_time_double)) * (coord.get(i).longitude);


                double extra_lat = ((travel_time_double - previous_time_double) / (next_time_double - previous_time_double)) * (nextCoord.get(1)) + ((next_time_double - travel_time_double) / (next_time_double - previous_time_double)) * (coord.get(i).latitude);


                System.out.println("According our extrapolation, at time = " + travel_time_double +  ", the vehicle " + coord.get(i).name + " has a longitude of " + extra_long + " and a latitude of " + extra_lat );
            }

        }

        g.disconnect(result -> {
            System.out.println("Goodbye !");
        });
    });

}
}