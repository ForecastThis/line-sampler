package com.forecastthis.linesampler;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.annotation.Arg;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;

import java.io.File;
import java.util.Optional;

public class Arguments {

	@Arg
	private int header;

	@Arg
	private String input;

	@Arg
	private String output;

	@Arg(dest = "output_complement")
	private String outputComplement;

	@Arg
	private double prob;

	@Arg
	private int seed;


	private Arguments() {

	}

	public static Arguments parse(String[] args) {

		ArgumentParser argumentParser = ArgumentParsers.newArgumentParser("java -jar line-parser.jar")
				.defaultHelp(true)
				.description("Command line Java tool for sampling lines from a file");

		argumentParser.addArgument("-H", "--header")
				.help("number of header lines. These lines will appear in both output and output complement.")
				.type(Integer.class)
				.setDefault(0)
				.required(false);

		argumentParser.addArgument("-i", "--input")
				.help("input file. Reading from standard input stream when not specified.")
				.type(String.class)
				.required(false);

		argumentParser.addArgument("-o", "--output")
				.help("output file. Writing to standard output stream when not specified.")
				.type(String.class)
				.required(false);

		argumentParser.addArgument("-c", "--output-complement")
				.help("output complement file. All lines that aren't included in the output.")
				.type(String.class)
				.required(false);

		argumentParser.addArgument("-p", "--prob")
				.help("probability of including a line in the output.")
				.type(Double.class)
				.required(true);

		argumentParser.addArgument("-s", "--seed")
				.help("random number generator seed")
				.type(Integer.class)
				.required(false)
				.setDefault(0);

		Arguments arguments = new Arguments();

		try {
			argumentParser.parseArgs(args, arguments);
		}
		catch(ArgumentParserException e) {
			argumentParser.handleError(e);
			System.exit(1);
		}

		return arguments;
	}

	public int getHeaderSize() {
		return header;
	}

	public Optional<File> getInputFile() {
		return Optional.ofNullable(input)
				.map(File::new);
	}

	public Optional<File> getOutputFile() {
		return Optional.ofNullable(output)
				.map(File::new);
	}

	public Optional<File> getOutputComplementFile() {
		return Optional.ofNullable(outputComplement)
				.map(File::new);
	}

	public double getProb() {
		return prob;
	}

	public int getSeed() {
		return seed;
	}
}
