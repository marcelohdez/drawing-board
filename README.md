# Drawing Board
A simple drawing application created purely in Java to challenge myself,
all the visuals are done using Java's Swing framework.

## Building
Being a Gradle project, Drawing Board can be built using gradle's
```build``` command. For the specifics on how to do this, keep reading,
otherwise you may skip to the
[Usage section](https://github.com/marcelohdez/drawing-board/#usage).

Download the source code and extract the folder inside, then open the 
extracted folder with your Terminal/Command Prompt.

* **On Windows** cd into the folder and run ```gradlew build``` in 
Command Prompt or Terminal.
* **On macOS** or other **unix-like OS's** cd into the folder and (you
may need to run ```chmod +x gradlew``` before this works) run
```./gradlew build``` in the terminal.

Once finished, the resulting files will be in the ```build``` folder.
The .jar will be in ```build > libs``` and gradle's default run scripts
will be in ```build > bin```.

## Usage
Upon opening the program, on the left will be your drawing tools, and
on the right will be the canvas/image tools. You may change your brush
size by 1 pixel by ```holding Shift + scrolling```, or by 10% by
```holding ctrl + scrolling```.

## License
This project is licensed under the GPLv3, a free and open source
license. More information can be found in the
[LICENSE file](https://github.com/marcelohdez/drawing-board/blob/master/LICENSE)