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


            String vehicle_array[] = {"vehicle1", "vehicle2", "vehicle3" , "vehicle4", "vehicle5", "vehicle6"};

            String type_vehicle_array[] = {"Plane", "Car", "Train", "Plane", "Car", "Train"};

            Double gps_array_vehicle[][] = {{0.0, 0.0, 5.0 , 5.0},{1.5, 0.0, 2.0 , 3.0},{0.0, 1.5, 4.0 , 1.0},{5.0, 2.0, 4.5 , 3.0},{3.0, 3.5, 0.0 , 0.0},{1.0, 2.0, 3.0 , 4.5}};

            int timepoint_array[] ={0,1,2,3,4,5,6};  //the timestamp is a long and represents the time concept

            for(int i=0; i < vehicle_array.length; i++) {

                Node data_gps = g.newNode(0, timepoint_array[0]); //the second param is the time
                data_gps.set("longitude", Type.DOUBLE, gps_array_vehicle[i][0]); //set the value of the sensor
                data_gps.set("latitude", Type.DOUBLE, gps_array_vehicle[i][1]); //set the value of the sensor

                Node vehicle = g.newNode(0, timepoint_array[0]); //the second param is the time
                vehicle.set("type", Type.STRING, type_vehicle_array[i]);
                vehicle.set("name", Type.STRING, vehicle_array[i]);

                vehicle.addToRelation("has_gps", data_gps); //add the sensor0 to the relation sensors of room0

                System.out.println(vehicle.toString());
                System.out.println(data_gps.toString());


                data_gps.travelInTime(timepoint_array[i+1], (Node data_gps_Ti) -> {

                    int j=0;
                    data_gps_Ti.set("longitude", Type.DOUBLE, gps_array_vehicle[j][2]);
                    data_gps_Ti.set("latitude", Type.DOUBLE, gps_array_vehicle[j][3]);
                    j=j+1;
                    System.out.println(data_gps_Ti.toString());
                });


                g.index(0,0,"vehicles", vehicleIndex -> {
                    vehicleIndex.addToIndex(vehicle, "type");

                    vehicleIndex.find(vehicles -> {
                        System.out.println("found: "+vehicles.length+" node!");
                        for (Node vehi : vehicles) {
                            System.out.println(vehi);
                        }
                    },"type","Train");
                });
            }
        });

        g.disconnect(result -> {
            System.out.println("Goodbye !");
        });

    }
}
