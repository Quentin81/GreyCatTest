package greycat.samples;


import greycat.Constants;
import greycat.Graph;
import greycat.GraphBuilder;
import greycat.Type;
import greycat.ml.MLPlugin;
import greycat.ml.regression.PolynomialNode;

public class Polynomial {

    public static void main(String[] args) {
        Graph g = new GraphBuilder().withPlugin(new MLPlugin()).build();
        g.connect(isConnected -> {
            PolynomialNode node = (PolynomialNode) g.newTypedNode(0, 0, PolynomialNode.NAME);

            for (int i = 0; i < 100; i+=10) {
                final int finalI = i;
                final double fv= i*2.0;
                node.travelInTime(finalI, polynomial -> {
                    polynomial.set("value", Type.DOUBLE, fv);
                });
            }

            node.timepoints(Constants.BEGINNING_OF_TIME, Constants.END_OF_TIME, (long[] times) -> {
                System.out.println("Nb of times:" + times.length);
            });

            //print 50.0
            node.travelInTime(55, polynomial->{
                System.out.println(polynomial.get("value"));
            });

            //print {"world":0,"time":0,"id":1"polynomial": "-0.0+(1.0*t)"}
            System.out.println(node.toString());

        });
    }

}
