package com.forecastthis.linesampler;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class LineSampler implements Closeable {

	public static void main(String[] args) {

		Arguments arguments = Arguments.parse(args);

		try(LineSampler sampler = new LineSampler(arguments)) {

			sampler.run();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	final Arguments arguments;

	final List<Closeable> resources = new ArrayList<>();

	LineSampler(Arguments arguments) {
		this.arguments = arguments;
	}

	@Override
	public void close() throws IOException {
		for(Closeable resource : resources) {
			resource.close();
		}
	}

	void run() throws IOException {

		OutputStream os = arguments.getOutputFile()
				.map(f -> openOutput(f))
				.orElse(System.out);
		Writer output = writer(os);

		Optional<Writer> outputComplement = arguments.getOutputComplementFile()
				.map(f -> openOutput(f))
				.map(s -> writer(s));

		InputStream is = arguments.getInputFile()
				.map(f -> openInput(f))
				.orElse(System.in);
		Reader input = reader(is);

		Random r = new Random(arguments.getSeed());

		try(BufferedReader reader = new BufferedReader(input)) {

			String line;
			for (int index=0; (line = reader.readLine()) != null; index++) {
				if(index < arguments.getHeaderSize()) {
					write(output, line);
					write(outputComplement, line);
				}
				else {
					if(r.nextDouble() < arguments.getProb()) {
						write(output, line);
					}
					else {
						write(outputComplement, line);
					}
				}
			}
		}

		output.flush();
		if(outputComplement.isPresent()) outputComplement.get().flush();
	}

	InputStream openInput(File file) {
		try {
			InputStream stream = new FileInputStream(file);
			resources.add(stream);
			return stream;
		}
		catch(FileNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}

	private static Reader reader(InputStream stream) {
		return new InputStreamReader(stream, Charset.forName("UTF-8"));
	}

	OutputStream openOutput(File file) {
		try {
			OutputStream stream = new FileOutputStream(file);
			resources.add(stream);
			return stream;
		}
		catch(FileNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}

	Writer writer(OutputStream stream) {
		return new OutputStreamWriter(stream, Charset.forName("UTF-8"));
	}

	void write(Writer writer, String line) throws IOException {
		writer.write(String.format("%s\n", line));
	}

	void write(Optional<Writer> writer, String line) throws IOException {
		if(writer.isPresent()) {
			write(writer.get(), line);
		}
	}
}