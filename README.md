# Plants vs Zombies - Java Edition

## Details

**Created by:
[Nguyen Tri Nhan](https://github.com/Teru127), [Nguyen Le Hoang Long](https://github.com/longnguyen9030) and [Thai Nhat Anh](https://github.com/thainhatanh61-lang)**

This is a clone of the strategy video game, [Plants vs. Zombies](https://en.wikipedia.org/wiki/Plants_vs._Zombies), originally developed by PopCap Games.

Made as a part of final project in Object-oriented Programming course at International University - VNUHCM.

A classic "Plants vs Zombies" tower defense game developed in "Java Swing".

## Features

- Beautiful "Main Menu" with Play and Exit buttons
- "In-game MENU button" (top right) to return to main menu anytime
- 4 Types of Plants:
  - Peashooter (shoots peas)
  - Sunflower (produces sun)
  - Wall-nut (high durability defender)
  - Potato Mine (explosive trap)
- 4 Types of Zombies:
  - Normal Zombie
  - Conehead Zombie
  - Buckethead Zombie
  - Flag Zombie (faster)
- Automatic Lawn Mowers
- Sun collection system
- Smooth frame-based animations
- Shovel tool to remove plants
- Game Over with option to return to menu

## How to Play
### Executing the JAR File

Controls

To play the game, the following steps can be followed:
1. Clone this repository using the command: `git clone https://github.com/thainhatanh61-lang/PlantsVsZombies.git` 
2. cd into the directory `PlantVsZombies` using: `cd PlantsVsZombies`
3. Download the **JDK 8** or higher for your platform from [Oracle's website](https://www.oracle.com/java/technologies/downloads/). Install it and verify with:
```bash
   java -version
```
4. Make the script executable and run it:
```bash
   chmod +x PlantsVsZombies.sh
   ./PlantsVsZombies.sh
```
   Alternatively, run directly with Java:
```bash
   java --module-path javafx-sdk-26.0.1/lib --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.media -jar PlantVsZombies.jar
```
   > **Note:** `--module-path` specifies the path to your JavaFX SDK `lib` folder. The variable `LIB_PATH` in `PlantsVsZombies.sh` can be updated if your SDK is stored in a different location.



