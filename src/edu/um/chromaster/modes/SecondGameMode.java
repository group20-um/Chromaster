package edu.um.chromaster.modes;

import edu.um.chromaster.Game;
import edu.um.chromaster.event.Subscribe;
import edu.um.chromaster.event.events.GameEndEvent;
import edu.um.chromaster.event.events.NodeClickedEvent;
import edu.um.chromaster.event.events.SelectColourEvent;
import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.Node;
import edu.um.chromaster.gui.ColorList;
import edu.um.chromaster.gui.game.GraphElement;
import edu.um.chromaster.gui.menu.boxes.WarningBox;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;


public class SecondGameMode extends GameMode {

    private long startTime = 0;
    private long penaltyTime = 0;

    public SecondGameMode(Graph graph, GraphElement.Difficulty difficulty, long time) {
        super(graph, difficulty, time, true);
        Game.getInstance().getEventHandler().registerListener(this);
    }


    @Override
    public long getTimeLeft() {
        return Math.max(0, (this.getTime() - penaltyTime) + startTime - System.currentTimeMillis());
    }

    public long getUsedColours() {
        return getGraph().getNodes().values().stream().mapToInt(Node::getValue).distinct().sum();
    }

    @Override
    public void start() {
        this.startTime = System.currentTimeMillis();
        getGraph().getNodes().forEach((id, node) -> node.getMeta().setAllowedToChangeColour(true));
        AtomicReference<ScheduledFuture<?>> future = new AtomicReference<>();
        future.set(Game.getInstance().getSchedule().scheduleAtFixedRate(() -> {
            if(getTimeLeft() <= 0) {

                while (future.get() == null);
                future.get().cancel(false);

                getGraph().getNodes().forEach((id, node) -> node.getMeta().setAllowedToChangeColour(false));

                if (!gameWon()) {
                    Game.getInstance().getEventHandler().trigger(new GameEndEvent("You lost", false));
                }
            }
        }, 0L, 200L, TimeUnit.MILLISECONDS));
    }

    @Override
    public boolean gameWon() {
        return isValidColoured(false);
    }

    @Subscribe
    public void onNodeClicked(NodeClickedEvent event) {
        if(this.getSelectedColour() != null && event.getNode().getMeta().isAllowedToChangeColour()) {

            // uncolour node
            if(event.getNode().getMeta().colour().equals(this.getSelectedColour())) {
                event.getNode().getMeta().colour(ColorList.NODE_INNER_DEFAULT);
                return;
            }

            // change colour
            if(hasNeighbourColour(event.getNode(), this.getSelectedColour())) {
                if(getDifficulty() != GraphElement.Difficulty.EASY) {
                    this.penaltyTime += TimeUnit.SECONDS.toMillis(getDifficulty() == GraphElement.Difficulty.MEDIUM ? 1 : 2);
                    System.out.println(penaltyTime);
                }
                WarningBox.display();
            } else {
                event.getNode().getMeta().colour(this.getSelectedColour());
                event.getNode().setValue(this.getSelectedColour().hashCode());
            }


            if(gameWon()) {
                Game.getInstance().getEventHandler().trigger(new GameEndEvent("You won", true));
            }
        }

    }

    @Subscribe
    public void onSelectColour(SelectColourEvent event) {
        this.setSelectedColour(event.getColor());
    }

}
