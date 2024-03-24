package com.pointed5th.ion;


public class ASTPrinter implements Expr.Visitor<String>{
    String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitAssignExpr(Expr.Assign expr) {
        return null;
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value == null) return "nil";
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return parenthesize(expr.operator.lexeme, expr.right);
    }

    @Override
    public String visitVariableExpr(Expr.Variable expr) {
        return null;
    }

    private String parenthesize(String name, Expr... exprs) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        for (Expr e : exprs) {
            builder.append(" ");
            builder.append(e.accept(this));
        }
        builder.append(")");

        return builder.toString();

    }

    public static void main(String[] args) {
        // (* (- 123) (group 45.67))

        Loc loc1 = new Loc(1, 1, 1);
        Loc loc2 = new Loc(1, 2, 2);

        Expr expr  = new Expr.Binary(
                new Expr.Unary(
                        new Token(TokenType.MINUS, "-", null, loc1),
                        new Expr.Literal(123)),
                new Token(TokenType.STAR, "*", null, loc2),
                new Expr.Grouping(new Expr.Literal(45.67))
        );

        ASTPrinter printer = new ASTPrinter();
        System.out.println(printer.print(expr));
    }
}
