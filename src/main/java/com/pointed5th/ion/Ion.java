package com.pointed5th.ion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Ion {
    private static final Interpreter interpreter = new Interpreter();
    static boolean hasError = false;
    static boolean hasRuntimeError = false;
    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: java -jar ion.jar <input file>");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
        if (hasError) System.exit(65);
        if (hasRuntimeError) System.exit(70);
    }

    private static void runPrompt() {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        System.out.println("Welcome to the ion REPL. Press Ctrl+C or type '.exit' to exit.");

        while (true) {
            System.out.print("> ");
            try {
                String line = reader.readLine();
                switch (line) {
                    case ".exit" -> System.exit(0);
                    case null -> {
                        System.out.println();
                        System.exit(0);
                    }
                    default -> {
                        run(line);
                        hasError = false;
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void run(String source) {
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.lex();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        if (hasError) return;

        interpreter.interpret(statements);
    }

    static void error(int line, String message) {
        report(line, "", message);
    }

    static void error(Token token, String msg) {
        if (token.type == TokenType.EOF) {
            report(token.line, "at end", msg);
        } else {
            report(token.line, "at '" + token.lexeme + "'", msg);
        }
    }

    private static void report(int line, String where, String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
    }

    static void runtimeError(RuntimeError error) {
        System.err.println(error.getMessage() +
                "\n[line " + error.token.line + "]");
        hasRuntimeError = true;
    }
}