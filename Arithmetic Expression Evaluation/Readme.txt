
Algorithm analysis and data structures
Project 1: Applications of Queues and Stacks



In this project, stacks are used to parse/evaluate arithmetic expressions.

Porject Descriptio : Algorithms to parse infix expressions, expression trees and evaluating postfix expressions.
Given invalid expressions, the program can return wrong values, print error messages, or throw exceptions.

Method to convert String to a Token:
	The string holds exactly one token, with no extra spaces before or after the token.
	Possible tokens are:
    Operators: PLUS ("+"), TIMES ("*"), MINUS ("-"), DIV ("/"), MOD ("%"), POWER ("^")
    Parentheses: OPEN ("("), CLOSE (")")
    Number: NUMBER (any valid string that represents an integer)
    	    Assume that if a token is not an operator or a parenthesis, then it is a number.
	    Use Long.parseLong(tok) to convert string to long.

    Signature: static Token getToken(String tok) {  ... }

    Tokens have a field "priority" that can be used to store precedence of operators during parsing.
    Precedence: {^} > {*, /, %} > {+, -}.  Assume that all operators are left associative.
    Assign your own values to priority of tokens.  Field "number" is used to store
    the value of NUMBER token.  A token "NIL" is defined for internal use and
    it does not correspond to any token in the expressions.  It is a convenient token to
    mark bottom of stack.

Method to convert an infix expression given as a list of tokens into an expression tree:
    Given an infix expression as a list of tokens, return its corresponding expression tree.
    Signature: public static Expression infixToExpression(List<Token> exp) { ... }


Method to convert an infix expression into a postfix expression:
    Given an infix expression as a list of tokens, return its equivalent postfix expression.
    Signature: public static List<Token> infixToPostfix(List<Token> exp) { ... }

Method to evaluate a postfix expression:
    Given a postfix expression, evaluate it and return its value.
    Signature: public static long evaluatePostfix(List<Token> exp) { ... }


Method to evaluate an expression tree:
    Given an expression tree, evaluate it and return its value.
    Signature: public static long evaluateExpression(Expression tree) { ... }
