package com.pointed5th.ion;

import org.apache.commons.cli.*;

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
    static boolean debugMode = false;

    public static void main(String[] args) throws ParseException, IOException {
        Options options = new Options();
        options.addOption("d", "debug", false, "Prints token and AST info");
        options.addOption("h", "help", false, "Prints usage info");
        options.addOption("v", "version", false, "Prints version info");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        HelpFormatter formatter = new HelpFormatter();

        if (cmd.hasOption("h")) {
            formatter.printHelp("ion", options);
            System.exit(0);
        }

        if (cmd.hasOption("v")) {
            System.out.println("ion v1.0");
            System.exit(0);
        }

        if (args.length == 0) {
            runPrompt();
        }

        if (args.length > 1) {
            formatter.printHelp("ion", options);
            System.exit(64);
        } else {
            runFile(args[0]);
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

        if (debugMode) {
            for (Token token : tokens) {
                System.out.println(token);
            }
        }
    }

    static void error(int line, String message) {
        report(line, "", message);
    }

    static void error(Token token, String msg) {
        if (token.type == TokenType.EOF) {
            report(token.loc.line(), "at end", msg);
        } else {
            report(token.loc.line(), "at '" + token.lexeme + "'", msg);
        }
    }

    private static void report(int line, String where, String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
    }

    static void runtimeError(RuntimeError error) {
        System.err.println(error.getMessage() +
                "\n[line " + error.token.loc.line() + "]");
        hasRuntimeError = true;
    }
}