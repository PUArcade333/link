package puArcade.princetonTD.grid;

import org.jgrapht.EdgeFactory;

public class ArcGenerator implements EdgeFactory<Node, Arc> {

    public Arc createEdge(Node s, Node t)
    {
            return new Arc(s, t);
    }


}
