# Performance Notes

The README table uses warm 10-run averages for each part because that makes solver-to-solver changes easier to compare. A single `MasterSolver` run is the better number for "I cloned the repo and ran all 50 parts once"; on this branch that is roughly 224ms.

The first invocation of a Java solver is often slower because the JVM is still loading classes, verifying bytecode, linking methods, compiling hot paths with the JIT, filling CPU caches, and sometimes paying one-time allocation or GC costs. These costs are real for a one-shot command, so the warm table should not be read as the literal end-to-end startup experience.

## Visual Examples

Bars are scaled to combined part 1 + part 2 time. Lower is better.

### Day 7: Bridge Repair

The solver still searches left-to-right operator choices, but the optimized version parses into primitive arrays and prunes recursive branches without extra collections.

```text
Before   9.4 ms | #########
After    4.4 ms | ####
```

### Day 14: Restroom Redoubt

The solver still projects robot positions on the toroidal grid, but the optimized version keeps positions and velocities in arrays and avoids rebuilding intermediate objects during the search.

```text
Before  10.1 ms | ##########
After    7.4 ms | #######
```

### Day 16: Reindeer Maze

The solver still performs the same weighted path search, but the optimized version flattens position/direction state into primitive arrays.

```text
Before  17.0 ms | #################
After   10.3 ms | ##########
```

### Day 22: Monkey Market

The solver still evaluates each buyer's secret sequence, but it now stores four price changes as base-19 digits, shrinking the sequence arrays from 1,048,576 sparse slots to 130,321 dense slots.

```text
Before  31.4 ms | ###############################
After   30.7 ms | ###############################
```
