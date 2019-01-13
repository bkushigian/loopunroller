# Loop Unroller
This is a toy project using [JavaParser][http://javaparser.org/] to create a
simple loop unroller. Currenlty only for loops and while loops are supported (so
no for-each loops), and `break`s and `continue`s aren't handled either.

## Running
You want to invoke the main method in the `Unroller` class. This expects command
line arguments for `.java` files and will unroll them and print to `stdout`. The
depth is hard coded but it should be simple enough to add basic arg processing
to the CLI.
