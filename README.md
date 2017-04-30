# javafx-counters

This is a small test to see if a more Elm-like architecture could be possible with JavaFx.
For this purpose this project mimics the Elm counters example found here: https://github.com/debois/elm-parts/blob/master/examples/2-counter-list.elm.
I am aware of reduxfx (https://github.com/netopyr/reduxfx), though this test follows different goals.

1. Goals
   * Minimal setup. Should not be a framework you can't escape.
   * Not a DSL.
   * No immutable versions of all nodes to be able to diff them and only push changes to the screen. Stay with JavaFx nodes.
   * Immutable state.
   * No classes you have to extend, only interfaces you can mix in.
  
2. Results

   I think this test didn't go too bad for a first try.
   
   Since we use mutable JavaFx nodes, there is some housekeeping you have to take care of manually.
   Looking at Counters.CountersView.render(...) you can see that we have to manage the identity of our counters manually.
   In addition we have to forward each counters' messages explicitly to the parent component, which is definitelly solved more elegantly in Elm.
   
   Overall I prefer this setup to the usual way to code JavaFx applications.
   Only very few interfaces and one helper class are used, so this is a pretty light setup.
   The state and messages are seperated nicely from the view which improves the predictability and readability of your application.
   
3. Future Work
   
   Port further ELm examples to see if this pattern holds up or gets too unmanagable.
