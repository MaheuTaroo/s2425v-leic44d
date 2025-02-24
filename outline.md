
## Week 1
### 17/02/2025 - Course introduction
* Syllabus, teaching methodology and bibliography.
  * Evaluation
  * Resources
* Course information on Moodle (for authenticated users only)
  * [General information](https://2425moodle.isel.pt/course/view.php?id=9166)
  * [LEIC44D course section](https://2425moodle.isel.pt/course/view.php?id=9362)
  * [Playlist with the course's lectures](https://www.youtube.com/playlist?list=PL8XxoCaL3dBhX9Kqt_BfAE23D4zYqgLdN)

### 18/02/2025 - Threading on the JVM: Introduction
* Threading on the JVM
  * Purpose and motivation
  * Platform threads: thread creation and execution
* Concurrent access to shared mutable state: consequences
  * The need for synchronization: introduction
  * Threading hazards: lost updates
    * Example: loss of increments

For reference: 
  * [Lecture video (in Portuguese)](https://www.youtube.com/watch?v=wGNDe1QB0LM&list=PL8XxoCaL3dBhX9Kqt_BfAE23D4zYqgLdN&index=1)

### 24/02/2025 - Threading on the JVM: Thread States
* Classifying state: 
  * Private - exclusive to the thread (e.g. in its stack)
  * Shared - stored globally or accessible through a closure
* Threading on the JVM
  * Thread states and lifecycle (introduction)
  * Synchronization mechanisms (introduction)
    * Coordinating threads: sincronization with thread termination `thread.join()`

For reference: 
  * [Lecture video (in Portuguese)](https://www.youtube.com/watch?v=hWzWrN7sejc&list=PL8XxoCaL3dBhX9Kqt_BfAE23D4zYqgLdN&index=2)