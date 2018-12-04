package edu.um.chromaster.gui;

import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;

public class ColorList {

    public final static Color NODE_HIGHLIGHTED = Color.YELLOW;
    public final static Color NODE_OUTER_DEFAULT = Color.WHITE;
    public final static Color NODE_INNER_DEFAULT = Color.BLACK;

    public final static Color EDGE_DEFAULT = Color.WHITESMOKE;
    public final static Color EDGE_HOVER = Color.ORANGE;

    public final static Color NODE_TEXT_DEFAULT = Color.WHITE;


    public final static Color HINT_CLIQUE = Color.GREEN;
    public final static Color HINT_HIGHEST_DEGREE = Color.RED;
    public final static Color HINT_MAX_NEIGHBOURS = Color.PINK;

    public final static List<Color> GRAPH_USER_COLOURS = new LinkedList<>(Arrays.asList(Color.rgb(255, 168, 103),
            Color.rgb(218, 114, 126),
            Color.rgb(69, 92, 123),
            Color.rgb(51, 1, 54),
            Color.rgb(0, 173, 181),
            Color.rgb(121, 189, 143),
            Color.rgb(44, 62, 80)));

}
