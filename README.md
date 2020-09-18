# The Senate Bus Problem

This is done for lab in CS4532 Concurrent Programming regarding the mutexes and semaphores.

This problem was originally based on the Senate bus at Wellesley College. Riders come to a bus stop and wait for a bus. When the bus arrives, all the waiting riders invoke boardBus, but anyone who arrives while the bus is boarding has to wait for the next bus. The capacity of the bus is 50 people; if there are more than 50 people waiting, some will have to wait for the next bus. When all the waiting riders have boarded, the bus can invoke depart. If the bus arrives when there are no riders, it should depart immediately.

### Prerequisites

These are the prerequisites that you need to set up the project.(Make sure you have installed all before running the simulation)
* Java 1.8+
* Maven 3.6+

### Run the simulation

* From the root directory run
```sh
mvn exec:java -Dexec.mainClass=SenateBusProblem

