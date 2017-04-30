package facilities;

import javafx.beans.value.ObservableValue;

public interface YieldsMessages<Message> {
    ObservableValue<Message> messagesOut();
}
