package com.pointed5th.toolchain;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class GenerateAST {
    private static String packageName = "package com.pointed5th.ion;";

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: gen-ast <output_directory>");
            System.exit(64);
        }

        String outputDir = args[0];

        defineAST(outputDir, "Expr", List.of(
            "Assign   : Token name, Expr value",
            "Binary   : Expr left, Token operator, Expr right",
            "Grouping : Expr expression",
            "Literal  : Object value",
            "Unary    : Token operator, Expr right",
            "Variable : Token name"
        ));

        defineAST(outputDir, "Stmt", List.of(
                "Expression : Expr expression",
                "Var        : Token name, Expr initializer",
                "Print      : Expr expression"
        ));
    }

    private static void defineAST(String outputDir, String baseName, List<String> types) throws IOException {
        String path = outputDir + "/" + baseName + ".java";

        PrintWriter writer = new PrintWriter(path, "UTF-8");

        writer.println(packageName);
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseName + " {");

        defineVisitor(writer, baseName, types);

        for (String tt: types) {
            String className = tt.split(":")[0].trim();
            String fields = tt.split(":")[1].trim();
            defineType(writer, baseName, className, fields);
        }

        writer.println();
        writer.println(" abstract <R> R accept(Visitor<R> visitor);");

        writer.println("}");
        writer.close();
    }

    private static void defineVisitor(PrintWriter writer, String baseName, List<String> types) {
        writer.println(" interface Visitor<R> {");

        for (String type: types) {
            String typeName = type.split(":")[0].trim();
            writer.println("    R visit" + typeName + baseName + "(" + typeName + " " + baseName.toLowerCase() + ");");
        }
        writer.println(" }");
    }

    private static void defineType(PrintWriter writer, String baseName, String className, String fieldList) {
        writer.println("    static class " + className + " extends " + baseName + " {");

        // constructor
        writer.println("    " + className + "(" + fieldList + ") {");

        String[] fields = fieldList.split(", ");
        for (String field: fields) {
            String name = field.split(" ")[1];
            writer.println("     this." + name + " = " + name + ";");
        }

        writer.println("    }");

        writer.println();
        writer.println("    @Override");
        writer.println("    <R> R accept(Visitor<R> visitor) {");
        writer.println("     return visitor.visit" + className + baseName + "(this);");
        writer.println("    }");

        // fields
        for (String field: fields) {
            writer.println("    final " + field + ";");
        }
        writer.println("  }");
    }
}
