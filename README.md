# Minecraft Mod - Creation Command Line

Minecraft Mod for Forge (Minecraft 1.12.1 - Forge Mdk 14.22.1.2485)

It adds command like "Copy/Paste/Replace/Save/Load/Clear"

## Table of contents

  * [Usage](#usage)
  * [Installation](#installation)
    * [Prerequisites](#prerequisites)
    * [Download Forge](#download-forge)
    * [Run Minecraft](#run-minecraft)
    * [Download The Mod](#download-the-mod)
  * [Development](#development)
    * [Development Prerequisites](#development-prerequisites)
    * [Setup Workspace](#setup-workspace)
    * [Cleanup](#cleanup)
    * [Download Sources](#download-sources)
    * [Create a Release](#create-a-release)

## Usage

```
Most of the bellow commands need that you select an area. To select an area you need to select two blocks.
To select a block you have two options:

* Place a torch
* Or break a block with a torch (the block will not disappear, only the selection will occur) 

Do it two times and a 3D area is selected even if you see nothing except in the chat.
Then open the chat (press 'T') and type one of the following commands (they all start with a dot):

**Command List**
  .torch   : Add 64 torches to the player inventory. ex: .torch
  .box     : Create a box around the selected area with block held in the main hand. ex: .box
  .clear   : Replace the selected area by air. ex: .clear
  .copy    : Copy the selected area in the clipboard. ex: .copy
  .day     : Change time to day time. ex: .day
  .delete  : Delete the saved areas. ex: .delete my-house
  .expand  : Expand the selected area. ex: .expand y 50
  .fill    : Fill the selected area with block held in the main hand. ex: .fill
  .help    : Display this help. ex: .help
  .list    : List the saved areas. ex: .list
  .load    : Load the saved area from a file to clipboard and paste it. ex: .load my-house
  .merge   : Expand the selected area by using more than the two last selected point. ex: .merge 3
  .mirror  : Mirror the copied area in the clipboard. ex: .mirror
  .nowater : Remove the water in the selected area. ex: .nowater
  .paste   : Paste the copied area from the clipboard. ex: .paste
  .r       : Redo the last undone block command. ex: .r
  .rec     : Create a rectangle around the selected area with block held in the main hand. ex: .rec
  .replace : Replace blocks in the selected area that match the one held in the off hand by the one held main hand. ex: .replace
  .rot     : Rotate clockwise the copied area in the clipboard. ex: .rot
  .save    : Save the selected area in the clipboard and a file. ex: .save my-house
  .shift   : Shift the previously selected position by a value. ex: .shift x 2
  .shrink  : Shrink the selected area. ex: .shrink x 3
  .u       : Undo the last block command. ex: .u
  .wall    : Create a wall around the selected area with block held in the main hand. ex .wall
```

## Installation

### Prerequisites

To use Forge you need **java** and **gradle**. But I will not explain how to install them, ask google for that.
Usually it's better to install the latest version. (currently I have 'java 1.8.0_144' and 'gradle 3.4')

### Download Forge

This mod has been created with a specific version of Forge.
If you don't yet have forge, it's safer to download the same version.

Open the page http://files.minecraftforge.net/ .
Scroll to the "All Versions" table.
For the version **14.22.1.2485** click on the **Mdk** link.
(then an ugly advertising page appears, you have to click on "SKIP" in the upper right corner)
Save the file `forge-1.12.1-14.22.1.2485-mdk.zip` and unzip it anywhere, we will call this folder the `Forge Folder`.

### Run Minecraft

Even before installing the mod, you can test that Minecraft is working properly.
Use the command line, go into the `Forge Folder` and test minecraft by running the command:

On Windows:
```bash
gradlew runClient
```
On Linux/Mac OS:
```bash
./gradlew runClient
```

### Download The Mod

Open the page of the latest release:
https://github.com/alban-auzeill/minecraft-creation-command-line-mod/releases/latest .
Download the `creation-command-line-mod-XX.XX.jar` in the `run/mods` subfolder folder of `Forge Folder`.
Relaunch Minecraft (see previous paragraph).
Create or open a world, then open the chat (press 'T') and type the command `.help` to check if the mod is running.
Now you can play this all the commands described in the [Usage](#usage) paragraph.

## Development

If you are a developer and want to contribute to this mod.

### Development Prerequisites

* Intellij Idea OR Eclipse
* git

### Setup Workspace

In the `Forge Folder` follow the instructions in `README.txt` (not README.md)
Mainly, you will have to run the command: `gradlew setupDecompWorkspace`, and for eclipse user `gradlew eclipse` (I'm using Intellij Idea).

### Cleanup

Then delete the provided mod example by deleting the folder `src/main` and the existing `.gitignore` file.
If you have previously downloaded this mod in the `run/mods` folder, then delete the downloaded jar.

### Download Sources

In the `Forge Folder` execute:
```bash
git init
```
Select the remote format that you prefer:
```bash
git remote add origin https://github.com/alban-auzeill/minecraft-creation-command-line-mod.git
```
Or
```bash
git remote add origin git@github.com:alban-auzeill/minecraft-creation-command-line-mod.git
```
Then
```bash
git fetch origin
git checkout -b master --track origin/master
```

Now, you can open Intellij Idea or Eclipse, and change the code in the `src` folder.

### Create a Release

In the `build.gradle` file, change `version = "1.0"` by the according version and set the following property values:
```properties
group = "com.auzeill.minecraft.mod.ccl"
archivesBaseName = "creation-command-line-mod"
```

In the `src/main/java/com/auzeill/minecraft/mod/ccl/CreationCommandLineMod.java`
change `version = "1.0"` by the according version.

```bash
git add build.gradle src/main/java/com/auzeill/minecraft/mod/ccl/CreationCommandLineMod.java
git commit -m "bump version"
git tag release-1.0
git push --tags
```

Create the binary
```bash
./gradlew processResources
./gradlew build
```

Create a github release and attach the `build/libs/creation-command-line-mod-XX.XX.jar` file to it.
