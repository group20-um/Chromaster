package edu.um.chromaster.gui;

import edu.um.chromaster.graph.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class NodeMan   {

    private final Node node;

    private boolean partyMode = false;

    public NodeMan(Node node) {
        this.node = node;
    }

    //
    public void draw(GraphicsContext g) {
        Node.Meta m = node.getMeta();


        if(partyMode) {
            g.setFill(Color.YELLOW);
            g.fillOval(m.x() - m.radius(), m.y() - m.radius(), m.radius() * 2, m.radius() * 2);
            g.setFill(Color.BLACK);
            g.fillOval(m.x() - m.radius() / 2 - 2, m.y() - m.radius() / 2 - 2, 8, 8);
            g.fillOval(m.x() + m.radius() / 2 - 2, m.y() - m.radius() / 2 - 2, 8, 8);
        } else {
            g.setStroke(Color.WHITE);
            g.strokeText(m.text(), (m.x() - (int) (m.radius() * 0.6)) + g.getFont().getSize() / 2, (node.getMeta().y() - (int) (m.radius() * 0.6)) + g.getFont().getSize(), m.radius() * 2);
        }
    }

}
