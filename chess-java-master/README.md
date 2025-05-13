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

To run the tests, you'll need JUnit 4:

1. Download JUnit JAR files:
   - junit-4.13.2.jar
   - hamcrest-core-1.3.jar

2. Add them to the classpath when compiling and running tests:
   ```
   javac -cp .:./test/lib/junit-4.13.2.jar:./test/lib/hamcrest-core-1.3.jar -d bin src/*.java test/*.java
   java -cp .:./bin:./test/lib/junit-4.13.2.jar:./test/lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore [TestClassName]
   ```
   (Use ; instead of : as the path separator on Windows)

## Running

Compile the project into an executable .jar file by running the following ANT build script on the command line. Make sure jar-in-jar-loader.zip in this repository is in the folder.

```
ant -f build.xml
```

Then, run the executable .jar file, named _chess-java.jar_ to play.