# HexOust

A minimalist two‑player capture game on a hexagonal board, built with Java/Swing. HexOust blends clean UI with concise game logic and thoughtful unit tests — a compact showcase of OO design, hex‑grid math, and interactive rendering.

> TL;DR: Click to place pieces. Capture smaller adjacent enemy groups. Keep your turn on captures. Win when your opponent has no pieces left.

## Features

- Modern Swing UI: anti‑aliased hex grid with valid‑move highlighting.
- Clear UX: turn indicator with color swatch; transient error/pass messages.
- Interesting ruleset: group‑size capture mechanic and repeat‑turn on capture.
- Robust core: cube coordinates (q+r+s=0), axial→pixel conversion, flood‑fill groups.
- Unit tested: board generation, hex math, controller flows (JUnit 5).

## How To Play

- Two players: RED goes first, then BLUE.
- Place a piece on any highlighted cell. You cannot place next to your own piece unless the move captures.
- When you place a piece, it forms or joins your connected group G. Any adjacent opponent group smaller than |G| is captured (removed). If any adjacent opponent group has size ≥ |G|, the move is invalid.
- Capturing lets you play again immediately. If you have no valid moves, your turn automatically passes.
- Win when your opponent owns no cells.

## Run It

Option A — Run the prebuilt JAR:

```
java -jar out/artifacts/HexOust43_jar/HexOust43.jar
```

Option B — Run from source (IDE-friendly):

- Open the project in IntelliJ IDEA.
- Run the `View.GUI` main class, or use the preconfigured Artifact (Build > Build Artifacts > HexOust43_jar > Build, then run the JAR).

Option C — Compile via CLI:

```
javac -d out $(find src -name "*.java")
java -cp out View.GUI
```

## Tech Stack

- Java (Swing) for the desktop UI
- JUnit 5 for unit tests
- Simple MVC-style separation: Model (Board/Hexagon), Controller (rules), View (GUI)

## Project Structure

- `src/View/GUI.java`: Anti‑aliased hex rendering, valid‑move shading, turn/win messaging; click handling wired to the controller.
- `src/Controller/Controller.java`: Turn tracking, move validation, capture logic, pass handling, win detection.
- `src/Model/Board.java`: Generates a base‑7 hexagon board (127 cells) and maps mouse clicks to hexes.
- `src/Model/Hexagon.java`: Cube coordinates, neighbor/direction math, group helpers.
- `src/Test/*.java`: JUnit tests for model and controller behavior.
- `src/META-INF/MANIFEST.MF`: Main‑Class = `View.GUI` for runnable JARs.

## Design Notes

- Coordinates: Uses cube coordinates with `q+r+s=0`; neighbor steps are predefined direction vectors.
- Rendering: Axial→pixel conversion to center and draw a hex‑shaped grid; per‑cell fills and outlines for clarity.
- Validation: Legal moves are precomputed; invalid placements show a concise message and do not alter state.
- Captures: Flood‑fill gathers connected groups; adjacent opponent groups smaller than the placed group are removed. Captures retain the current turn; otherwise the turn switches.
- UX details: Pass‑turn notices auto‑clear after a short delay; preventing accidental double‑placement on owned cells.

## Testing

- Open `src/Test` in your IDE and run with JUnit 5.
- Coverage highlights:
  - Board size and coordinate invariants (127 cells, q+r+s=0)
  - Hex math (add/subtract, length, distance, directions/neighbors)
  - Controller flows (turn switching, valid/invalid moves, capture + repeat turn, pass, win state)

## Roadmap Ideas

- AI opponent (greedy or Monte Carlo) with difficulty levels
- Undo/redo and move history with PGN‑style export
- Animated placements/captures and subtle sound effects
- Variable board sizes and themes (color‑blind friendly palettes)
- Online multiplayer (WebSocket) or local hot‑seat customizations



## License

No license specified. If you intend to use or distribute this code, consider adding a license (MIT, Apache‑2.0, etc.).
