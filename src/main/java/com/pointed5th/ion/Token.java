package com.pointed5th.ion;

public class Token {
    final TokenType type;
    final String lexeme;
    final Object literal;
    final Loc loc;

    Token(TokenType type, String lexeme, Object literal, Loc loc) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.loc = loc;
    }

    public String toString() {
        return "type [" + type + "]" + " lexeme " + lexeme;
    }
}
