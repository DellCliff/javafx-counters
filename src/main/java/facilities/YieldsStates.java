package facilities;

import javafx.beans.value.ObservableValue;

public interface YieldsStates<State> {
    ObservableValue<State> statesOut();
}
