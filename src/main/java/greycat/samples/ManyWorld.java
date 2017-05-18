package greycat.samples;

import greycat.*;

/**
 * Created by assaad on 17/03/2017.
 */
public class ManyWorld {
    public static void main(String[] args) {
        Graph g = new GraphBuilder().build();
        g.connect(isConnected -> {
            long timepoint_0 = 0;
            long world_0 = 0; //identifies the main world 0;

            Node sensor0 = g.newNode(world_0, timepoint_0); //the first param is the world
            sensor0.set("id", Type.STRING, "4494F");
            sensor0.set("name", Type.STRING, "sensor0");
            sensor0.set("value", Type.DOUBLE, 0.5); //set the value of the sensor

            long world_1 = g.fork(world_0);

            sensor0.travelInWorld(world_1, new Callback<Node>() {
                @Override
                public void on(Node sensorW1T0) {
                    sensorW1T0.set("value", Type.DOUBLE, 4.6);

                    long timepoint_1=10;
                    sensorW1T0.travel(world_0, timepoint_1, new Callback<Node>() {
                        @Override
                        public void on(Node sensorW0T1) {
                            System.out.println(sensorW0T1); // print {"world":0,"time":10,"id":1,"id":"4494F","name":"sensor0","value":0.5}
                            sensorW0T1.travel(world_1, timepoint_1, new Callback<Node>() {
                                @Override
                                public void on(Node sensorW1T1) {
                                    System.out.println(sensorW1T1); //print {"world":1,"time":10,"id":1,"id":"4494F","name":"sensor0","value":4.6}

                                }
                            });
                        }
                    });

                }
            });


        });
    }
}
