package com.forecastthis.linesampler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

public class LineSampler {
  
  public static void main(String[] args) {
  
    ArgumentParser parser = ArgumentParsers.newArgumentParser("Line sampler").defaultHelp(true).description("Select line subset from a file");
    parser.addArgument("-i", "--input").type(String.class).help("input file").nargs(1).required(true).setDefault("");
    parser.addArgument("-o", "--output").help("output file").type(String.class).nargs(1).required(true);
    parser.addArgument("-c", "--complement").help("path to save complement (not selected lines in").type(String.class).nargs(1);
    parser.addArgument("-p", "--prob").help("Probability of selecting a line. Value from range [0, 100].").type(String.class).nargs(1).required(true);
    parser.addArgument("-H", "--header").help("number of header lines that should always be selected. default 0").type(Integer.class).nargs(1).setDefault(0);
    parser.addArgument("-s", "--seed").help("random number generator seed").type(Integer.class).nargs(1).setDefault(1);
    
    Namespace namespace = null;
    
    try {
      namespace = parser.parseArgs(args);
    } catch (ArgumentParserException e) {
      System.err.println(e.getMessage());
      System.exit(1);
    }
    
    Map<String, Object> m = namespace.getAttrs();
    
    String inputPath = ((List<?>)m.get("input")).get(0).toString();
    String outputPath = ((List<?>)m.get("output")).get(0).toString();
    String complementPath = ((List<?>)m.get("complement")).get(0).toString();
    double p = Double.parseDouble(((List<?>)m.get("prob")).get(0).toString());
    int h = Integer.parseInt(((List<?>)m.get("header")).get(0).toString());
    int seed = Integer.parseInt(((List<?>)m.get("seed")).get(0).toString());
    
    
    File input = new File(inputPath);
    File output = new File(outputPath);
    File complement = new File(complementPath);
    Random r = new Random(seed);
    
    
    
    FileWriter fw = null;
    if(output != null) {
      try {
        fw = new FileWriter(output);
      } catch (IOException e) {
        e.printStackTrace(System.err);
        System.exit(-1);
      }
    }
    
    FileWriter cfw = null;
    if(complement != null) {
      try {
        cfw = new FileWriter(complement);
      } catch (IOException e) {
        e.printStackTrace(System.err);
        System.exit(-1);
      }
    }
    
    try(FileReader fr = new FileReader(input)) {
      try(BufferedReader br = new BufferedReader(fr)) {
        String line;
        for (int index=0; (line = br.readLine()) != null; index++) {
        
          String l = String.format("%s\n", line);
          
          if(index < h) {
            fw.write(l);
            cfw.write(l);
          }
          else {
            if(r.nextDouble() < p) {
                  fw.write(l);   
             }
            else {
              cfw.write(l);
            }
          }
          
           
        }
      }
    }
    catch(Exception e) {
      e.printStackTrace(System.err);
      System.exit(-1);
    }
    
    if(fw != null) {
      try {
        fw.close();
      } catch (IOException e) {
        e.printStackTrace(System.err);
        System.exit(-1);
      }
    }
    
    if(cfw != null) {
      try {
        cfw.close();
      } catch (IOException e) {
        e.printStackTrace(System.err);
        System.exit(-1);
      }
    }
  }
}
