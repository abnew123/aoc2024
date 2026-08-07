2024 AoC repo. For more detailed thoughts about the problems, see https://abnew123.substack.com/

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=abnew123_aoc2024&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=abnew123_aoc2024)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=abnew123_aoc2024&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=abnew123_aoc2024)

## Performance

Current 25-day timing from 100 randomized-order fresh-JVM pairs; values are arithmetic means in milliseconds. See [PERFORMANCE.md](PERFORMANCE.md) for definitions and A/B evidence.

| Wall | Main | Solver | Startup | Harness |
|---:|---:|---:|---:|---:|
| 163.906 | 138.841 | 108.243 | 21.525 | 30.597 |

| Days | 01 | 02 | 03 | 04 | 05 |
|---|---:|---:|---:|---:|---:|
| 01–05 | 3.670268 | 1.556294 | 3.189186 | 3.099610 | 5.585248 |
| 06–10 | 4.681773 | 4.108645 | 1.052860 | 4.445775 | 0.526812 |
| 11–15 | 3.316880 | 4.509493 | 4.775636 | 2.532025 | 3.382906 |
| 16–20 | 7.068682 | 1.415020 | 4.750022 | 3.950844 | 13.348550 |
| 21–25 | 2.748222 | 16.833820 | 4.139096 | 2.024542 | 1.531022 |

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


<!-- CHAR COUNTS BEGIN -->

## Character counts

The table below counts non-whitespace characters in each normal solution file and its golfed sibling. The golfed files are intentionally separate from the readable/performance-oriented `DayXX.java` files so the PR tracks can merge independently. Every golfed solver receives the same neutral `String[]` of logical input lines from `MasterSolver`; input parsing and all puzzle logic remain in its `DayXXGolfed.java` file.

| Day | Solution | Golfed solution | Source chars | Golfed chars |
| --- | --- | --- |-------------:|-------------:|
| 1 | [Source](src/solutions/Day01.java) | [Golfed](src/solutions/Day01Golfed.java) | 2,697 | 302 |
| 2 | [Source](src/solutions/Day02.java) | [Golfed](src/solutions/Day02Golfed.java) | 1,645 | 243 |
| 3 | [Source](src/solutions/Day03.java) | [Golfed](src/solutions/Day03Golfed.java) | 1,487 | 273 |
| 4 | [Source](src/solutions/Day04.java) | [Golfed](src/solutions/Day04Golfed.java) | 2,224 | 336 |
| 5 | [Source](src/solutions/Day05.java) | [Golfed](src/solutions/Day05Golfed.java) | 4,192 | 341 |
| 6 | [Source](src/solutions/Day06.java) | [Golfed](src/solutions/Day06Golfed.java) | 5,505 | 381 |
| 7 | [Source](src/solutions/Day07.java) | [Golfed](src/solutions/Day07Golfed.java) | 6,079 | 303 |
| 8 | [Source](src/solutions/Day08.java) | [Golfed](src/solutions/Day08Golfed.java) | 2,089 | 378 |
| 9 | [Source](src/solutions/Day09.java) | [Golfed](src/solutions/Day09Golfed.java) | 4,520 | 426 |
| 10 | [Source](src/solutions/Day10.java) | [Golfed](src/solutions/Day10Golfed.java) | 2,808 | 339 |
| 11 | [Source](src/solutions/Day11.java) | [Golfed](src/solutions/Day11Golfed.java) | 6,391 | 335 |
| 12 | [Source](src/solutions/Day12.java) | [Golfed](src/solutions/Day12Golfed.java) | 2,792 | 435 |
| 13 | [Source](src/solutions/Day13.java) | [Golfed](src/solutions/Day13Golfed.java) | 5,487 | 280 |
| 14 | [Source](src/solutions/Day14.java) | [Golfed](src/solutions/Day14Golfed.java) | 3,710 | 426 |
| 15 | [Source](src/solutions/Day15.java) | [Golfed](src/solutions/Day15Golfed.java) | 5,059 | 481 |
| 16 | [Source](src/solutions/Day16.java) | [Golfed](src/solutions/Day16Golfed.java) | 5,501 | 443 |
| 17 | [Source](src/solutions/Day17.java) | [Golfed](src/solutions/Day17Golfed.java) | 2,948 | 554 |
| 18 | [Source](src/solutions/Day18.java) | [Golfed](src/solutions/Day18Golfed.java) | 4,930 | 366 |
| 19 | [Source](src/solutions/Day19.java) | [Golfed](src/solutions/Day19Golfed.java) | 4,549 | 241 |
| 20 | [Source](src/solutions/Day20.java) | [Golfed](src/solutions/Day20Golfed.java) | 4,507 | 369 |
| 21 | [Source](src/solutions/Day21.java) | [Golfed](src/solutions/Day21Golfed.java) | 4,955 | 497 |
| 22 | [Source](src/solutions/Day22.java) | [Golfed](src/solutions/Day22Golfed.java) | 2,079 | 391 |
| 23 | [Source](src/solutions/Day23.java) | [Golfed](src/solutions/Day23Golfed.java) | 3,761 | 492 |
| 24 | [Source](src/solutions/Day24.java) | [Golfed](src/solutions/Day24Golfed.java) | 5,682 | 608 |
| 25 | [Source](src/solutions/Day25.java) | [Golfed](src/solutions/Day25Golfed.java) | 2,321 | 183 |
| Total |  |  | 97,918 | 9,423 |

<!-- CHAR COUNTS END -->
