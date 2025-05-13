# Chess for Java

In the Spring of 2014, I created a two-player Chess game, with checkmate detection and a chess clock as a part of a Programming course at Penn. Our objective was to develop and test a bug-free standalone game in Java, complete with a GUI and game logic components.

I developed a bug-free, fast and well-designed product with a clean user interface and received the highest possible score in the assignment. The source code is in this repository.

## Technology

This game is built using core Java, Java Swing GUI libraries and the jUnit test suite. It uses custom drawing for game components and self-programmed logic for checkmate detection. The code is modular, standalone and object oriented, which was a grading criteria for the assignment.

## Recent Improvements

The codebase has been refactored to improve code quality and maintainability:

- Eliminated code duplication through helper methods
- Improved modularization of piece initialization
- Added symbolic constants for better readability
- Enhanced object-oriented design principles
- Improved code organization and structure
- Added comprehensive unit tests for chess piece movement rules
- Added fallback image handling for tests
- Updated build process to support testing

These changes maintain the original functionality while making the code more maintainable and easier to understand.

## Code Organization

The code is organized as follows:

1. **Core Game Classes:**
   - `Board.java` - Main board representation and interaction logic
   - `GameWindow.java` - UI container for the chess board
   - `Square.java` - Individual square on the chess board
   - `CheckmateDetector.java` - Logic for detecting check and checkmate conditions

2. **Chess Piece Classes:**
   - `Piece.java` - Abstract base class for all chess pieces
   - `Pawn.java`, `Rook.java`, `Knight.java`, `Bishop.java`, `Queen.java`, `King.java` - Specific piece implementations

3. **Test Classes:**
   - `ChessTestBase.java` - Base class with common testing utilities
   - `PawnTest.java`, `RookTest.java`, etc. - Piece-specific test classes
   - `CheckmateTest.java` - Tests for check, checkmate, and stalemate scenarios

## Unit Tests

The project includes a comprehensive suite of unit tests to verify the correctness of the chess rules model. The tests cover:

1. **Piece Movement Rules**
   - Valid and invalid moves for each piece type
   - Capture mechanics
   - Movement restrictions (blocking, etc.)

2. **Check and Checkmate Detection**
   - Detecting when a king is in check
   - Verifying checkmate conditions
   - Testing for stalemate scenarios
   - Verifying check avoidance rules

### Running Tests

To run the tests, you need JUnit 4:

1. Make sure JUnit JAR files are in place:
   ```
   test/lib/junit-4.13.2.jar
   test/lib/hamcrest-core-1.3.jar
   ```

2. Use the Ant build script to run tests:
   ```
   ant -f build.xml run_tests
   ```

3. To download required test dependencies if missing:
   ```
   mkdir -p test/lib
   # On Windows
   Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/junit/junit/4.13.2/junit-4.13.2.jar" -OutFile "test/lib/junit-4.13.2.jar"
   Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar" -OutFile "test/lib/hamcrest-core-1.3.jar"
   
   # On Unix/Linux/MacOS
   curl -o test/lib/junit-4.13.2.jar "https://repo1.maven.org/maven2/junit/junit/4.13.2/junit-4.13.2.jar"
   curl -o test/lib/hamcrest-core-1.3.jar "https://repo1.maven.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar"
   ```

## Build and Run Instructions

### Building the Project

Compile the project into an executable .jar file by running the Ant build script:

```bash
# Build executable jar
ant -f build.xml

# Build and run tests
ant -f build.xml build_and_test

# Clean the project
ant -f build.xml clean
```

### Running the Game

After building, run the executable .jar file:

```bash
java -jar chess-java.jar
```

## Project Structure

```
chess-java-master/
├── src/                # Source code
├── resources/          # Game images and resources
├── test/               # Unit tests
│   └── lib/            # Test dependencies
├── bin/                # Compiled classes
├── build.xml           # Ant build script
├── CHANGES.md          # Detailed list of changes
└── README.md           # This file
```

## Further Improvements

Potential future enhancements:
- Add en passant and castling special moves
- Implement a simple chess AI
- Add game history tracking
- Add network play capability
- Improve UI for different screen sizes