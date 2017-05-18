package greycat.samples;


import greycat.*;

public class Index {

    public static void main(String[] args) {

        Graph g = new GraphBuilder().build();
        g.connect(isConnected -> {

            Node sensor0 = g.newNode(0, 0);
            sensor0.set("sensorId", Type.STRING, "4494F");
            sensor0.set("name", Type.STRING, "sensor0");
            sensor0.set("value", Type.DOUBLE, 26.2); //set the value of the sensor

            Node kitchenNode = g.newNode(0, 0);
            kitchenNode.set("name", Type.STRING, "kitchen");
            kitchenNode.addToRelation("sensors", sensor0);

            Node LivingNode = g.newNode(0, 0);
            LivingNode.set("name", Type.STRING, "living");

            //Get from the graph an index called roomIndex
            g.index(0, 0, "rooms", roomIndex -> {
                //Add a room to the index of rooms to be able to retrieve it fast by its name
                roomIndex.addToIndex(kitchenNode, "name");
                roomIndex.addToIndex(LivingNode, "name");

                roomIndex.find(rooms -> {
                    for (Node room : rooms) {
                        System.out.println(room);
                    }
                });

                roomIndex.find(rooms -> {
                    System.out.println("found: "+rooms.length+" node!");
                    for (Node room : rooms) {
                        System.out.println(room);
                    }
                },"name","kitchen"); //here we ask to filter only the node with name = kitchen.

            });


        });
    }

}
