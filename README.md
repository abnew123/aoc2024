2024 AoC repo. For more detailed thoughts about the problems, see https://abnew123.substack.com/

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=abnew123_aoc2024&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=abnew123_aoc2024)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=abnew123_aoc2024&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=abnew123_aoc2024)

<!-- AOC TILES BEGIN -->
<h1 align="center">
  2024 - 50 ⭐ - Java
</h1>
<a href="src/solutions/Day01.java">
  <img src=".aoc_tiles/tiles/2024/01.png" width="161px">
</a>
<a href="src/solutions/Day02.java">
  <img src=".aoc_tiles/tiles/2024/02.png" width="161px">
</a>
<a href="src/solutions/Day03.java">
  <img src=".aoc_tiles/tiles/2024/03.png" width="161px">
</a>
<a href="src/solutions/Day04.java">
  <img src=".aoc_tiles/tiles/2024/04.png" width="161px">
</a>
<a href="src/solutions/Day05.java">
  <img src=".aoc_tiles/tiles/2024/05.png" width="161px">
</a>
<a href="src/solutions/Day06.java">
  <img src=".aoc_tiles/tiles/2024/06.png" width="161px">
</a>
<a href="src/solutions/Day07.java">
  <img src=".aoc_tiles/tiles/2024/07.png" width="161px">
</a>
<a href="src/solutions/Day08.java">
  <img src=".aoc_tiles/tiles/2024/08.png" width="161px">
</a>
<a href="src/solutions/Day09.java">
  <img src=".aoc_tiles/tiles/2024/09.png" width="161px">
</a>
<a href="src/solutions/Day10.java">
  <img src=".aoc_tiles/tiles/2024/10.png" width="161px">
</a>
<a href="src/solutions/Day11.java">
  <img src=".aoc_tiles/tiles/2024/11.png" width="161px">
</a>
<a href="src/solutions/Day12.java">
  <img src=".aoc_tiles/tiles/2024/12.png" width="161px">
</a>
<a href="src/solutions/Day13.java">
  <img src=".aoc_tiles/tiles/2024/13.png" width="161px">
</a>
<a href="src/solutions/Day14.java">
  <img src=".aoc_tiles/tiles/2024/14.png" width="161px">
</a>
<a href="src/solutions/Day15.java">
  <img src=".aoc_tiles/tiles/2024/15.png" width="161px">
</a>
<a href="src/solutions/Day16.java">
  <img src=".aoc_tiles/tiles/2024/16.png" width="161px">
</a>
<a href="src/solutions/Day17.java">
  <img src=".aoc_tiles/tiles/2024/17.png" width="161px">
</a>
<a href="src/solutions/Day18.java">
  <img src=".aoc_tiles/tiles/2024/18.png" width="161px">
</a>
<a href="src/solutions/Day19.java">
  <img src=".aoc_tiles/tiles/2024/19.png" width="161px">
</a>
<a href="src/solutions/Day20.java">
  <img src=".aoc_tiles/tiles/2024/20.png" width="161px">
</a>
<a href="src/solutions/Day21.java">
  <img src=".aoc_tiles/tiles/2024/21.png" width="161px">
</a>
<a href="src/solutions/Day22.java">
  <img src=".aoc_tiles/tiles/2024/22.png" width="161px">
</a>
<a href="src/solutions/Day23.java">
  <img src=".aoc_tiles/tiles/2024/23.png" width="161px">
</a>
<a href="src/solutions/Day24.java">
  <img src=".aoc_tiles/tiles/2024/24.png" width="161px">
</a>
<a href="src/solutions/Day25.java">
  <img src=".aoc_tiles/tiles/2024/25.png" width="161px">
</a>
<!-- AOC TILES END -->

Current one-pass timing for `MasterSolver` is roughly 224ms on a 2024 MacBook Pro. The table below shows warm 10-run averages per part using the existing `DayTemplate.timer` convention; those numbers are useful for comparing individual solver changes, but they understate the first-run cost someone sees when running all 50 parts once.

See [performance notes](PERFORMANCE.md) for visual before/after examples and benchmark caveats.

| Day | Problem | Solution | Part 1 (ms) | Part 2 (ms) |
| --- | --- | --- |------------:|------------:|
| 1 | [Historian Hysteria](https://adventofcode.com/2024/day/1) | [Source](src/solutions/Day01.java) | 1.996 | 1.674 |
| 2 | [Red-Nosed Reports](https://adventofcode.com/2024/day/2) | [Source](src/solutions/Day02.java) | 1.888 | 2.953 |
| 3 | [Mull It Over](https://adventofcode.com/2024/day/3) | [Source](src/solutions/Day03.java) | 1.914 | 2.226 |
| 4 | [Ceres Search](https://adventofcode.com/2024/day/4) | [Source](src/solutions/Day04.java) | 1.793 | 1.304 |
| 5 | [Print Queue](https://adventofcode.com/2024/day/5) | [Source](src/solutions/Day05.java) | 2.794 | 3.055 |
| 6 | [Guard Gallivant](https://adventofcode.com/2024/day/6) | [Source](src/solutions/Day06.java) | 1.408 | 10.201 |
| 7 | [Bridge Repair](https://adventofcode.com/2024/day/7) | [Source](src/solutions/Day07.java) | 2.142 | 2.216 |
| 8 | [Resonant Collinearity](https://adventofcode.com/2024/day/8) | [Source](src/solutions/Day08.java) | 0.913 | 3.578 |
| 9 | [Disk Fragmenter](https://adventofcode.com/2024/day/9) | [Source](src/solutions/Day09.java) | 2.860 | 3.263 |
| 10 | [Hoof It](https://adventofcode.com/2024/day/10) | [Source](src/solutions/Day10.java) | 1.514 | 1.344 |
| 11 | [Plutonian Pebbles](https://adventofcode.com/2024/day/11) | [Source](src/solutions/Day11.java) | 1.722 | 9.535 |
| 12 | [Garden Groups](https://adventofcode.com/2024/day/12) | [Source](src/solutions/Day12.java) | 1.703 | 2.575 |
| 13 | [Claw Contraption](https://adventofcode.com/2024/day/13) | [Source](src/solutions/Day13.java) | 2.524 | 2.572 |
| 14 | [Restroom Redoubt](https://adventofcode.com/2024/day/14) | [Source](src/solutions/Day14.java) | 2.396 | 4.969 |
| 15 | [Warehouse Woes](https://adventofcode.com/2024/day/15) | [Source](src/solutions/Day15.java) | 1.724 | 2.872 |
| 16 | [Reindeer Maze](https://adventofcode.com/2024/day/16) | [Source](src/solutions/Day16.java) | 3.931 | 6.352 |
| 17 | [Chronospatial Computer](https://adventofcode.com/2024/day/17) | [Source](src/solutions/Day17.java) | 0.363 | 1.992 |
| 18 | [RAM Run](https://adventofcode.com/2024/day/18) | [Source](src/solutions/Day18.java) | 2.545 | 3.138 |
| 19 | [Linen Layout](https://adventofcode.com/2024/day/19) | [Source](src/solutions/Day19.java) | 2.035 | 2.705 |
| 20 | [Race Condition](https://adventofcode.com/2024/day/20) | [Source](src/solutions/Day20.java) | 2.444 | 17.196 |
| 21 | [Keypad Conundrum](https://adventofcode.com/2024/day/21) | [Source](src/solutions/Day21.java) | 1.991 | 2.814 |
| 22 | [Monkey Market](https://adventofcode.com/2024/day/22) | [Source](src/solutions/Day22.java) | 15.592 | 15.070 |
| 23 | [LAN Party](https://adventofcode.com/2024/day/23) | [Source](src/solutions/Day23.java) | 3.077 | 2.886 |
| 24 | [Crossed Wires](https://adventofcode.com/2024/day/24) | [Source](src/solutions/Day24.java) | 2.145 | 1.906 |
| 25 | [Code Chronicle](https://adventofcode.com/2024/day/25) | [Source](src/solutions/Day25.java) | 7.522 | 0.002 |
