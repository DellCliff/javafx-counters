package facilities;

public interface Updater<State, Message> {
    State update(final Message message, final State state);
}
