package com.example.solveit;

class MathEvaluation {

    private String expression;
    private int ch, pos = -1;

    MathEvaluation(String expression){
        this.expression = expression;
    }

    double parse() {
        nextChar();
        double x = parseLowArithmetic();
        if (pos < expression.length())
            System.out.println("Unexpected: " + (char)ch);
        return x;
    }

    private double parseLowArithmetic() {
        double x = parseMediumArithmetic();
        for (;;) {
            if (evaluate('+'))
                // addition
                x += parseMediumArithmetic();
            else if (evaluate('-'))
                // subtraction
                x -= parseMediumArithmetic();
            else
                return x;
        }
    }

    private double parseMediumArithmetic() {
        double x = parseHighArithmetic();
        for (;;) {
            if (evaluate('*'))
                // multiplication
                x *= parseHighArithmetic();
            else if (evaluate('/'))
                // division
                x /= parseHighArithmetic();
            else if (evaluate('%'))
                // modulus
                x %= parseHighArithmetic();
            else
                return x;
        }
    }

    private double parseHighArithmetic() {
        if (evaluate('-'))
            // unary minus
            return -parseHighArithmetic();

        double x;
        int startPos = this.pos;
        if (evaluate('(')) {
            // parentheses
            x = parseLowArithmetic();
            evaluate(')');
        } else if ((ch >= '0' && ch <= '9') || ch == '.') {
            // numbers
            while ((ch >= '0' && ch <= '9') || ch == '.')
                nextChar();
            x = Double.parseDouble(expression.substring(startPos, this.pos));
        } else if (ch >= 'a' && ch <= 'z') {
            // functions
            while (ch >= 'a' && ch <= 'z') nextChar();
            String func = expression.substring(startPos, this.pos);
            x = parseHighArithmetic();
            switch (func) {
                case "sqrt":
                    x = Math.sqrt(x);
                    break;
                case "sin":
                    x = Math.sin(Math.toRadians(x));
                    break;
                case "cos":
                    x = Math.cos(Math.toRadians(x));
                    break;
                case "tg":
                    x = Math.tan(Math.toRadians(x));
                    break;
                default:
                    System.out.println("Unknown function: " + func);
                    break;
            }
        } else {
            System.out.println("Unexpected: " + (char)ch);
            throw new RuntimeException("Unexpected: " + (char)ch);
        }

        if (evaluate('^')) x = Math.pow(x, parseHighArithmetic());

        return x;
    }

    // get next char element
    private void nextChar() {
        ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
    }

    private boolean evaluate(int charToEvaluate) {
        while (ch == ' ')
            nextChar();
        if (ch == charToEvaluate) {
            nextChar();
            return true;
        }
        return false;
    }
}
