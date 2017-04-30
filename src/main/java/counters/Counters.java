package counters;

import facilities.Renderable;
import facilities.Updater;
import facilities.YieldsMessages;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Counters {

    public static class CountersView extends VBox implements Renderable<Iterable<Integer>>, YieldsMessages<CountersMessage> {

        private final VBox countersContainer = new VBox();
        private final SimpleObjectProperty<CountersMessage> messagesOut = new SimpleObjectProperty<>();

        public CountersView() {
            setStyle("-fx-background-color: red;");
            setPrefWidth(400);
            setPrefHeight(300);

            HBox buttons = new HBox();
            Button remove = new Button("Remove");
            remove.setOnAction(event -> messagesOut.setValue(new CountersMessage.Remove()));
            Button add = new Button("Add");
            add.setOnAction(event -> messagesOut.setValue(new CountersMessage.Add()));
            buttons.getChildren().addAll(remove, add);

            getChildren().addAll(buttons, countersContainer);
        }

        @Override
        public void render(final Iterable<Integer> state) {
            List<Integer> stateList = new ArrayList<>();
            state.forEach(stateList::add);
            ObservableList<Node> counters = countersContainer.getChildren();
            counters.removeIf(n -> !(n instanceof Counter.CounterView));

            int countersDiff = stateList.size() - counters.size();
            if (countersDiff > 0) {
                counters.addAll(
                    IntStream.range(0, countersDiff)
                        .mapToObj(i -> {
                            final int ownId = counters.size();
                            Counter.CounterView view = new Counter.CounterView();
                            view.messagesOut().addListener((o, oV, nV) ->
                                messagesOut.setValue(new CountersMessage.CounterMessage(ownId, nV))
                            );
                            return view;
                        }).collect(Collectors.toList())
                );
            } else if (countersDiff < 0) {
                counters.remove(counters.size() + countersDiff, counters.size());
            }

            for (int i = 0; i < counters.size(); i += 1) {
                Node n = counters.get(i);
                if (n instanceof Counter.CounterView)
                    ((Counter.CounterView) n).render(stateList.get(i));
            }
        }

        @Override
        public ObservableValue<CountersMessage> messagesOut() { return messagesOut; }
    }

    public static class CountersMessage {
        private CountersMessage() {}

        public static class Add extends CountersMessage {}

        public static class Remove extends CountersMessage {}

        public static class CounterMessage extends CountersMessage {
            public final int id;
            public final Counter.CounterMessage message;

            public CounterMessage(int id, Counter.CounterMessage message) {
                this.id = id;
                this.message = message;
            }
        }
    }

    public static final Updater<Iterable<Integer>, CountersMessage> updater = (message, state) -> {
        if (message instanceof CountersMessage.Add) {
            ArrayList<Integer> newState = new ArrayList<>();
            state.forEach(newState::add);
            newState.add(0);
            return newState;
        } else if (message instanceof CountersMessage.Remove) {
            ArrayList<Integer> newState = new ArrayList<>();
            state.forEach(newState::add);
            if (newState.size() > 0) newState.remove(newState.size() - 1);
            return newState;
        } else if (message instanceof CountersMessage.CounterMessage) {
            final CountersMessage.CounterMessage counterMessage = (CountersMessage.CounterMessage) message;
            ArrayList<Integer> newState = new ArrayList<>();
            state.forEach(newState::add);
            if (newState.size() > counterMessage.id)
                newState.set(counterMessage.id, Counter.updater.update(counterMessage.message, newState.get(counterMessage.id)));
            return newState;
        }
        return state;
    };
}
