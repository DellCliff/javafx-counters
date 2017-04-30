package facilities;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class App {

    public static <View extends Renderable<State> & YieldsMessages<Message>, State, Message> AutoCloseable
    bootstrap(State model, Updater<State, Message> updater, View... view) {
        return bootstrap(model, updater, Arrays.asList(view));
    }

    public static <View extends Renderable<State> & YieldsMessages<Message>, State, Message> AutoCloseable
    bootstrap(State model, Updater<State, Message> updater, Iterable<View> views) {
        return new ClosableBootstrap<>(model, updater, views);
    }

    private static class ClosableBootstrap<View extends Renderable<State> & YieldsMessages<Message>, State, Message> implements AutoCloseable {

        private CompletableFuture<Void> queue = CompletableFuture.completedFuture(null);
        private State currentState;
        private final ArrayList<View> viewList;
        private final ChangeListener<Message> listener;

        public ClosableBootstrap(State model, Updater<State, Message> updater, Iterable<View> views) {
            currentState = model;

            viewList = new ArrayList<>();
            views.forEach(viewList::add);

            final Object queueLock = new Object();
            final Object stateLock = new Object();
            listener = (o, oV, nV) -> {
                synchronized (queueLock) {
                    queue = queue.thenRunAsync(() -> {
                        synchronized (stateLock) {
                            final State newState = updater.update(nV, currentState);
                            currentState = newState;
                            Platform.runLater(() -> viewList.forEach(view -> view.render(newState)));
                        }
                    });
                }
            };

            viewList.forEach(view -> {
                view.messagesOut().addListener(listener);
                view.render(model);
            });
        }

        @Override
        public void close() throws Exception {
            viewList.forEach(view -> view.messagesOut().removeListener(listener));
        }
    }
}
