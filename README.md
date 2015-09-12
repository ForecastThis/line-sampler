Intro
=====
Command line Java tool for sampling lines from a file.

Usage
=====
    usage: java -jar line-parser.jar
           [-h] [-H HEADER] [-i INPUT] [-o OUTPUT] [-c OUTPUT_COMPLEMENT] -p PROB [-s SEED]

    Command line Java tool for sampling lines from a file

    optional arguments:
      -h, --help             show this help message and exit
      -H HEADER, --header HEADER
                             number of header lines. These lines will appear in both output and output complement. (default: 0)
      -i INPUT, --input INPUT
                             input file. Reading from standard input stream when not specified.
      -o OUTPUT, --output OUTPUT
                             output file. Writing to standard output stream when not specified.
      -c OUTPUT_COMPLEMENT, --output-complement OUTPUT_COMPLEMENT
                             output complement file. All lines that aren't included in the output.
      -p PROB, --prob PROB   probability of including a line in the output.
      -s SEED, --seed SEED   random number generator seed (default: 0)

Build
=====
    mvn clean package