# Predator-Prey Ecosystem

A Java Swing ecosystem simulation using standard Java libraries.
The simulation contains prey, predators, food and meat, with
entities interacting to create changing population behaviour.

## Requirements

- JDK 17 or newer
- Visual Studio Code
- Extension Pack for Java (Microsoft)

## Setting Up in VS Code

1. Install JDK 17 or newer.
2. Install Visual Studio Code.
3. Open VS Code and install the Extension Pack for Java.
4. Extract the project folder.
5. In VS Code, select File > Open Folder.
6. Select the PredatorPreySimulation folder.
7. Open `src/Main.java`.

## Running the Simulation

The easiest way to run the simulation is to open `Main.java`
and select the Run button above the `main()` method.

Alternatively, open the VS Code terminal and run:

```bash
javac -d out src/*.java
java -cp out Main