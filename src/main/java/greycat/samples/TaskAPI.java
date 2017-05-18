package greycat.samples;

import greycat.Graph;
import greycat.GraphBuilder;
import greycat.Node;
import greycat.Type;

public class TaskAPI {

    public static void main(String[] args) {

        Graph g = new GraphBuilder().build();
        g.connect(isConnected -> {

            Node sensor0 = g.newNode(0, 0);
            sensor0.set("id", Type.STRING, "4494F");
            sensor0.set("name", Type.STRING, "sensor0");
            sensor0.set("value", Type.DOUBLE, 26.2); //set the value of the sensor

            Node room0 = g.newNode(0, 0);
            room0.set("name", Type.STRING, "room0");
            room0.addToRelation("sensors", sensor0);


//            g.index("rooms", room0, "name", (r) -> {
//                fromindex("rooms")
//                        .selectWith("name", "room0")
//                        .then(ctx -> {
//                            //print {"world":0,"time":0,"id":2,"name":"room0","sensors":[1]}
//                            System.out.println(ctx.result());
//                        }).execute(g, null);
//
//                fromIndex("rooms", "name=room0")
//                        .get("name")
//                        .then(ctx -> {
//                            //print room0
//                            System.out.println(ctx.result());
//                        }).execute(g, null);

                /*
                fromIndexAll("rooms")
                        //select none empty sensors
                        .select((n, c) -> ((long[]) n.get("sensors")).length > 0)
                        .then(ctx -> {
                            Object[] currentResult = (Object[]) ctx.result();
                            //print {"world":0,"time":0,"id":2,"name":"room0","sensors":[1]}
                            System.out.println(currentResult[0]);
                        }).execute(g, null);
                        */
//            });


        });

    }

}
