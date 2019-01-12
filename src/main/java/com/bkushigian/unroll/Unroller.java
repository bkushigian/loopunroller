package com.bkushigian.unroll;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Unroller {
  private final UnrollVisitor vis = new UnrollVisitor(6);

  public static void main(String[] args) {
    Unroller unroller = new Unroller();
    for (String arg : args) {
      System.out.println(unroller.unroll(arg));
    }
  }

  /**
   * @param file input program to be unrolled
   * @return null if IOException, contents of unrolled program otherwise.
   */
  String unroll(String file) {
    try {
      final String prog = readFile(file);
      CompilationUnit cunit = JavaParser.parse(prog);
      return unroll(cunit);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Unroll all methods in a compilation unit
   * @param cunit
   * @return
   */
  String unroll(CompilationUnit cunit) {
    cunit.accept(vis, null);
    return cunit.toString();
  }

  String readFile(String file) throws IOException {
    try(BufferedReader br = new BufferedReader(new FileReader(file))) {
      StringBuilder sb = new StringBuilder();
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
