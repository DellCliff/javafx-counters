package counters;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import facilities.App;

import java.util.ArrayList;

public class FxMain extends Application {

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        final Counters.CountersView c = new Counters.CountersView();

        App.bootstrap(new ArrayList<>(), Counters.updater, c);

        primaryStage.setScene(new Scene(c));
        primaryStage.setTitle("Counters");
        primaryStage.show();
    }
}
