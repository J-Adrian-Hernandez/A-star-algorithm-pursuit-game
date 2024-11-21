# Elude

[![Java](https://img.shields.io/badge/Java-8%2B-orange)](https://www.oracle.com/java/)
[![Gradle](https://img.shields.io/badge/Gradle-4.9-blue)](https://gradle.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

A Java-based pursuit game where AI agents chase the player using A* pathfinding algorithm. Collect gems while evading enemies to achieve the highest score possible!

## 🎮 Features

- A* pathfinding algorithm implementation
- Dynamic enemy AI behavior
- Score-based gem collection system
- Obstacle-rich environment
- Cool soundtrack

## 🚀 Prerequisites

- Java 8 or higher
- Gradle 4.9 or higher (for building)

## 🛠️ Building the Project

### Using Gradle (Recommended)

```bash
# Build the project
gradle assemble

# Run the game
gradle run
```

### Manual Compilation

```bash
# Compile
javac Game.java

# Run
java Game
```

### IDE Support
The project can be imported into major Java IDEs (IntelliJ IDEA, Eclipse, VS Code) that support Gradle projects.

## 🎯 How to Play

1. Use arrow keys to move your character
2. Collect gems to increase your score
3. Avoid enemies that pursue you using A* pathfinding
4. Try to survive as long as possible

## 🏗️ Project Structure

```
elude/
├── src/
│   └── main/
│       └── java/
│           └── Game.java
├── build.gradle
├── settings.gradle
└── README.md
```

## 🎵 Credits

### Music
- Tracks: Hyouhaku + Kokuten
- Artist: Unknown

### Assets
- Game sprites: Pokemon Emerald (Pokemon Company)

## 📝 License

This project is intended as an educational resource for:
- Java game development
- A* algorithm implementation
- Pathfinding in games

## ⚠️ Disclaimer

The game assets are property of their respective owners and are used for educational purposes only. No copyright infringement intended.

## 🤝 Contributing

Feel free to open issues and pull requests for any improvements you want to add.
