package com.bkushigian.unroll;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Unroller {
  private final UnrollVisitor vis = new UnrollVisitor(6);

  public static void main(String[] args) {
    Unroller unroller = new Unroller();
    for (String arg : args) {
      File f = new File(arg);
      System.out.println(unroller.unroll(f));
    }
  }

  /**
   * @param file input program to be unrolled
   * @return null if IOException, contents of unrolled program otherwise.
   */
  public CompilationUnit unroll(final File file) {
    try {
      final String prog = readFile(file.toString());
      final CompilationUnit cunit = JavaParser.parse(prog);
      return unroll(cunit);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public CompilationUnit unroll(final String prog) {
    final CompilationUnit cunit = JavaParser.parse(prog);
    return unroll(cunit);
  }

  /**
   * Unroll all methods in a compilation unit
   * @param cunit
   * @return
   */
  public CompilationUnit unroll(CompilationUnit cunit) {
    cunit.accept(vis, null);
    return cunit;
  }

  /**
   * Read a file
   * @param file
   * @return
   * @throws IOException
   */
  String readFile(final String file) throws IOException {
    try(BufferedReader br = new BufferedReader(new FileReader(file))) {
      final StringBuilder sb = new StringBuilder();
      String line = br.readLine();

      while (line != null) {
        sb.append(line);
        sb.append(System.lineSeparator());
        line = br.readLine();
      }
      return sb.toString();
    }
  }
}
