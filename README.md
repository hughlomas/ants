ants
====

https://www.youtube.com/watch?v=VsHc91IhzdI

The ants have no conception of where their home was located, their behavior is entirely pheromone driven. They can "read" pheromones from any adjacent cell.
They leave a pheromone every movement depending on which state they are in, either "find food" or "return food".

The pheromones have a strength. Whenever an ant touches the nest, his "nest pheromone" is at full strength (255). Each movement decreases the strength by 1. This sets up a natural gradient of nest strength for ants to follow. They also "deteriorate" each round.

A similar setup is used with food, such that a high food scent is created when they pick up food, and decreases in strength as they return to the nest.

So to find food, they try to move up a food gradient or down the nest gradient (away from the nest), while to return food they move up the nest gradient (towards the nest).

Their searching consists of a random walking into cells that have no nest scent, or following a food trail.

They have an energy cost for moving, and if it's too low they have to eat a piece of food. Returning a piece of food to the hill spawns a new ant. The lines on the right side show the population of each colony and the total food on the board.

From these extremely basic rules, they are very efficient at clearing the "world" of food.

You'll need the Processing libraries to build this (http://processing.org/tutorials/eclipse/)
