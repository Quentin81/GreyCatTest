package greycat.samples.Battleground;

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
 * Created by Quentin on 18/05/2017.
 */
public class MinimalClasses {


    public static void main(String[] args) {

        //Create a minimal graph with the default configuration
        Graph g = new GraphBuilder().build();

        //Connect the graph
        g.connect(isConnected -> {
            //Display that the graph database is connected!
            System.out.println("Connected : " + isConnected);



            //TRYING HERE !!!
            newTask()
                    .loop("1","10",
                            newTask()
                                    ///Trying to math i
                                   /*.thenDo(new ActionFunction() {
                                        @Override
                                        public void eval(TaskContext taskContext) {
                                            MathExpressionEngine engine = CoreMathExpressionEngine.parse("5*4");
                                            double res = engine.eval(null,null,null);
                                            taskContext.continueTask();
                                        }
                                    })*/
                                    //.then(inject(10))
                                    //.then(defineAsVar("it"))
                                    //.then(print("{{it}}"))
                                    //.inject("{{=4*i}}")
                                    .thenDo(new ActionFunction() {
                                        @Override
                                        public void eval(TaskContext ctx) {
                                            ctx.continueWith(ctx.wrap(ctx.template("{{=4*i}}")).clone());
                                        }
                                    })
                                    //.log("{{result}}")
                                    // .then(inject(CoreMathExpressionEngine.parse("4*{{i}}").eval(null,null,null)))
                                    .then(defineAsVar("res"))
                                    .then(println("{{res}}"))
                                    .then(createNode())
                                    .then(setAttribute("name",Type.STRING,"node_{{i}}"))
                                    .then(setAttribute("type",Type.STRING,"Tank"))
                                    .then(setAttribute("power",Type.DOUBLE,"{{res}}"))
                                    .then(addVarToRelation("can_destroy", "test", "node_{{i}}"))
                                    .then(travelInTime("0"))
                                    .then(println("{{result}}")))

                    .execute(g,null);

            newTask()
                    .loop("1","14",
                            newTask()
                                    .then(createNode())
                                    .then(setAttribute("name",Type.STRING,"node_{{i}}"))
                                    .then(setAttribute("type",Type.STRING,"Target"))
                                    .then(setAttribute("resilience",Type.DOUBLE,"{{i}}"))
                                    .then(travelInTime("0"))
                                    .then(println("{{result}}")))

                    .execute(g,null);






            long timepoint_0 = 0;  //the timestamp is a long and represents the time concept



            Node target1 = g.newNode(0, timepoint_0); //the second param is the time
            target1.set("id", Type.STRING, "2");
            target1.set("name", Type.STRING, "Target1");
            target1.set("Resilience", Type.DOUBLE, 5.0); //set the value of the sensor


            Node tank1 = g.newNode(0, timepoint_0); //the second param is the time
            tank1.set("id", Type.STRING, "1");
            tank1.set("name", Type.STRING, "Tank1");
            tank1.set("Power", Type.DOUBLE, 3.0); //set the value of the sensor



            long timepoint_1 = 100;
            tank1.travelInTime(timepoint_1, (Node tank1T1) -> {
                tank1T1.set("Power", Type.DOUBLE, 8.0); //update the value of the time now
                //Display the value at time 0
                System.out.println("T0:" + tank1.toString()); //print T0:{"world":0,"time":0,"id":1,"id":"4494F","name":"sensor0","value":0.5}
                System.out.println("T0:" + target1.toString());

                //Display the value at time now
                System.out.println("T1:" + tank1T1.toString()); //print T1:{"world":0,"time":100,"id":1,"id":"4494F","name":"sensor0","value":21.3}

                tank1T1.addToRelation("can_destroy", target1); //add the sensor0 to the relation sensors of room0
                System.out.println(tank1T1.toString());



                long timepoint_2= 200;
                tank1.travelInTime(timepoint_2, (Node tank1T2) ->{

                    tank1T2.set("Power", Type.DOUBLE, 10.0); //update the value of the time now
                    System.out.println("T2:" + tank1T2.toString()); //prints T2:{"world":0,"time":50,"id":1,"id":"4494F","name":"sensor0","value":0.5}
                });



            });

        });
    }




}
