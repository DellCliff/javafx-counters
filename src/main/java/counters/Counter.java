package counters;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import facilities.Renderable;
import facilities.Updater;
import facilities.YieldsMessages;

public class Counter {

    public static class CounterView extends HBox implements Renderable<Integer>, YieldsMessages<CounterMessage> {

        private final Label label = new Label();
        private final SimpleObjectProperty<CounterMessage> messagesOut = new SimpleObjectProperty<>();

        public CounterView() {
            setStyle("-fx-background-color: white;");
            hoverProperty().addListener((o, oV, nV) -> {
                if (nV) setStyle("-fx-background-color: yellow;");
                else setStyle("-fx-background-color: white;");
            });
            Button m = new Button("-");
            m.setOnAction(event -> messagesOut.setValue(new CounterMessage.Decreased()));
            Button p = new Button("+");
            p.setOnAction(event -> messagesOut.setValue(new CounterMessage.Increased()));
            getChildren().addAll(m, label, p);
        }

        @Override
        public void render(Integer state) { label.setText(state.toString()); }

        @Override
        public ObservableValue<CounterMessage> messagesOut() { return messagesOut; }
    }

    public abstract static class CounterMessage {
        private CounterMessage() {}

        public static class Increased extends CounterMessage {}

        public static class Decreased extends CounterMessage {}
    }

    public static final Updater<Integer, CounterMessage> updater = (message, state) -> {
        if (message instanceof CounterMessage.Increased) return state + 1;
        else if (message instanceof CounterMessage.Decreased) return state - 1;
        return state;
    };
}
