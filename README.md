<div align="center">

# HexOust ▷ A Minimalist Hex Capture Game

[![Java](https://img.shields.io/badge/Java-11%2B-red?logo=java&logoColor=white)](#)
[![Swing](https://img.shields.io/badge/UI-Swing-blue)](#)
[![JUnit 5](https://img.shields.io/badge/Tests-JUnit%205-25a162?logo=junit5&logoColor=white)](#)
[![MVC](https://img.shields.io/badge/Architecture-MVC-6f42c1)](#)

<br>

HexOust blends a sleek hex‑grid UI with tight, well‑tested game logic. Click to place, capture smaller adjacent enemy groups, keep your turn on capture, and win when your opponent has no pieces left.

<br>



</div>

## Why It Stands Out

- Bold but compact: small codebase, clear responsibilities, readable logic.
- Hex‑grid math: cube coordinates, neighbor vectors, axial→pixel layout.
- Strong UX touches: valid‑move highlighting, crisp anti‑aliasing, subtle status/errors.
- Tested behavior: controller flows, hex math, board invariants with JUnit 5.
- Portfolio‑ready: showcases UI, algorithms, and design thinking in one place.

## One‑Minute Tour

1) RED starts. Valid cells glow subtly.  
2) Place next to your own group only if the move captures.  
3) Your group size is |G|. Any adjacent enemy group with size < |G| is captured (removed).  
4) Capturing keeps your turn; otherwise turns alternate.  
5) Win when your opponent has no owned cells.

Tip: Play to create large connected shapes that can eat smaller neighboring groups.

## Quick Start

Run the prebuilt JAR:

```
java -jar out/artifacts/HexOust43_jar/HexOust43.jar
```

Run from source (IDE):

- Open in IntelliJ IDEA.
- Run main class: `View.GUI`.
- Or build artifact: Build > Build Artifacts > `HexOust43_jar` > Build, then run the JAR above.

Run from CLI:

```
javac -d out $(find src -name "*.java")
java -cp out View.GUI
```

## Tech Highlights

- `GUI.java`: Anti‑aliased rendering of a centered hex grid; valid‑move shading; turn/win messaging; click → controller pipeline.
- `Controller.java`: Turn logic, move validation, size‑based group capture, pass handling, win detection.
- `Board.java`: Generates a base‑7 hex board (127 cells) and maps pixel clicks to cells.
- `Hexagon.java`: Cube coords `(q,r,s)`, neighbor/direction vectors, distance/length helpers.
- Tests: Board shape invariants, hex math, and controller scenarios (JUnit 5).

## Architecture Sketch

```
[ GUI (Swing) ]  ⇄  [ Controller (rules/state) ]  ⇄  [ Model (Board, Hexagon) ]
     |                         |                               |
  Mouse clicks            Valid moves,                   Grid generation,
  + rendering            captures, turn                cube coords, neighbors
```

## Design Notes

- Math: cube coordinates enforce `q+r+s=0`; neighbor steps are predefined, making BFS/flood‑fill straightforward.
- Rendering: axial→pixel conversion centers the grid; each cell is filled and outlined for contrast.
- Validation: GUI queries `getValidMoves()`; invalid placements raise a concise message without mutating state.
- Captures: flood‑fill collects your placed group; adjacent enemy groups smaller than your group are removed; capture repeats your turn.
- UX: pass‑turn notices auto‑clear after a short delay to keep the UI calm and readable.

## Testing

- Open `src/Test` and run with JUnit 5 (IDE recommended).
- Coverage highlights:
  - Board size and coordinate invariants (127 cells, `q+r+s=0`)
  - Hex math (add/subtract, length, distance, directions/neighbors)
  - Controller flows (valid/invalid moves, captures with repeat turn, pass, win state)

## Contributors & Roles

- Tim: Backend setup & hex‑grid math —  established cube coordinates, neighbor vectors, axial→pixel mapping, and core model/controller primitives.
- Hubert: Testing— authored JUnit 5 tests covering board invariants, hex math, and controller flows.
- Dan: Remaining implementation & polish  — UI (Swing), integration, packaging, and documentation touch‑ups.



