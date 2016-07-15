When you have some free time...
=========================

... you might have fun playing Rock-Paper-Scissors against your own computer, or just make your computer battle itself.

Why did i do it this way?
=========================

Because i did not have a lot of time to think about it and wanted to make my life easier, so i decided to have actors shooting messages at each other.

I actually had fun and played around with it on more of a conceptual level, cause i liked the idea.

What does it do?
=========================

The whole concept is pretty simple:
* there's a `KeyMapper` that maps characters to message objects. While the actor system is alive, i keep reading characters from the console and translate them into case objects
* the mapper then sends those objects to a FSM actor(`Controller`) which then creates a `GameActor` and two players. The idea is that Players could in future be created elsewhere on any system, so once a `PlayerActor` is created, it automatically joins a game that was created.
* Once the game is started and Players are created, depending on the `GameType` the controller sends `Move` messages to players, which forward those messages to the GameActor.
* The GameActor keeps state in itself so it knows which player made which move, and is able to decide who actually won.
* All interactions are sent to the `DisplayActor` which can decide what to do there, i'm just `println`-ing it.

The good, the bad and the ugly
=========================

The thing i like about this approach, is that it allows a game to be created anywhere, and two players can be created anywhere just by passing a reference to the game. After that, the game can just sit and look pretty until both players have played their moves. 

Another good thing is that this approach does not need any specific extra logic about who plays first, who is a computer or who is human, we just care about sending messages. Also, some state holding is needed and i personally really like actors for that because they're super lightweight, and the implementation with State Machines is very verbose, so reading the code really can be easy.

Also, another good thing is that with this mechanism, the Player is notified about the result, the Game knows the result and can be used to keep the data between multiple rounds(not implemented, but could be), and the controller knows the result.

The ugly part of this is, that due to lack of time i didn't wrap the entire thing into companion objects where i would expose the entire functionality to some sort of an API, so to complete purists, fire&forget mechanism in AKKA might look ugly, clumsy or whatever.


How do i run it?
=========================

Well, i guess you only need Java. Once you check out the project, it already has the activator binaries and execution libs, so you can just execute `activator run` and follow instructions.

