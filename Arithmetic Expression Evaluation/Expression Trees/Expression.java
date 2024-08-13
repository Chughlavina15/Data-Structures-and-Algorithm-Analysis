package LXC220045;

import java.util.List;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;
import java.io.FileNotFoundException;
import java.io.File;
import java.lang.Math;

/** Class to store a node of expression tree
    For each internal node, element contains a binary operator
    List of operators: +|*|-|/|%|^
    Other tokens: (|)
    Each leaf node contains an operand (long integer)
*/

public class Expression {
    public enum TokenType {  // NIL is a special token that can be used to mark bottom of stack
	PLUS, TIMES, MINUS, DIV, MOD, POWER, OPEN, CLOSE, NIL, NUMBER
    }
    
    public static class Token {
	TokenType token;
	int priority; // for precedence of operator
	Long number;  // used to store number of token = NUMBER
	String string;

	Token(TokenType op, int pri, String tok) {
	    token = op;
	    priority = pri;
	    number = null;
	    string = tok;
	}

	// Constructor for number.  To be called when other options have been exhausted.
	Token(String tok) {
	    token = TokenType.NUMBER;
	    number = Long.parseLong(tok);
	    string = tok;
	}
	
	boolean isOperand() { return token == TokenType.NUMBER; }

	public long getValue() {
	    return isOperand() ? number : 0;
	}

	public String toString() { return string; }
    }

    Token element;
    Expression left, right;

    // Create token corresponding to a string
    // tok is "+" | "*" | "-" | "/" | "%" | "^" | "(" | ")"| NUMBER
    // NUMBER is either "0" or "[-]?[1-9][0-9]*
    static Token getToken(String tok) {  // To do
	Token result;
	switch(tok) {
	case "+":
	    result = new Token(TokenType.PLUS, 1, tok);  // modify if priority of "+" is not 1
	    break;
    case "-":
	    result = new Token(TokenType.MINUS, 1, tok);  // modify if priority of "-" is not 1
	    break;
    case "*":
	    result = new Token(TokenType.TIMES, 2, tok);  // modify if priority of "*" is not 2
	    break;
    case "/":
	    result = new Token(TokenType.DIV, 2, tok);  // modify if priority of "/" is not 2
	    break;
    case "%":
	    result = new Token(TokenType.MOD, 2, tok);  // modify if priority of "%" is not 2
	    break;
    case "^":
	    result = new Token(TokenType.POWER, 3, tok);  // modify if priority of "^" is not 3
	    break;
    case "(":
	    result = new Token(TokenType.OPEN, -1, tok);  // modify if priority of "(" is not -1 (not an operator)
	    break;
    case ")":
	    result = new Token(TokenType.CLOSE,-1, tok);  // modify if priority of ")" is not -1 (not an operator)
	    break;
	default:
	    result = new Token(tok); // for any other operator which is not included in {+, - , *, /, %, ^}
	    break;
	}
	return result;
    }
    
    private Expression() {
	element = null;
    }
    
    private Expression(Token oper, Expression left, Expression right) {
	this.element = oper;
	this.left = left;
	this.right = right;
    }

    private Expression(Token num) {
	this.element = num;
	this.left = null;
	this.right = null;
    }

    // Given a list of tokens corresponding to an infix expression,
    // return the expression tree corresponding to it.
    public static Expression infixToExpression(List<Token> exp) {  // To do
		Deque<Expression> exprtree_stack = new ArrayDeque<>(); // stack for storing expression trees
		Deque<Token> operators_stack = new ArrayDeque<>(); // stack for operators
		for(Token t : exp){ // iterating on the list of input list of tokens
			if(t.number != null) // token is a number
			{	
				Expression leaf_node = new Expression(t); // if token is a number, initializing it as a leaf node
				exprtree_stack.push(leaf_node); // pushing into the stack
			}
			// if the token is any operator from {+, - , *, /, %, ^}
			else if((t.token == TokenType.PLUS || t.token == TokenType.MINUS || t.token == TokenType.TIMES ||
			 t.token == TokenType.DIV || t.token == TokenType.MOD || t.token == TokenType.POWER))
			 {
				while(operators_stack.peek() != null && operators_stack.peek().token != TokenType.OPEN && operators_stack.peek().priority >= t.priority)
				{
					Expression right_child =  exprtree_stack.pop();
					Expression left_child =  exprtree_stack.pop();
					Token opr = operators_stack.pop();
					Expression sub_tree = new Expression(opr, left_child, right_child);
					exprtree_stack.push(sub_tree);

				}
				//else if(st_op.peek()==null){
				//else{
					operators_stack.push(t);
				//}
				//}
			}
			else if(t.token == TokenType.OPEN){
				operators_stack.push(t);
			}
			else if(t.token == TokenType.CLOSE){
				while(exprtree_stack.peek()!=null && operators_stack.peek()!=null && operators_stack.peek().token != TokenType.OPEN){
					Expression right_child =  exprtree_stack.pop();
					Expression left_child = exprtree_stack.pop();
					Token opr = operators_stack.pop();
					Expression sub_tree = new Expression(opr, left_child, right_child);
					exprtree_stack.push(sub_tree);
				}
				operators_stack.pop();
				//String to_be_discarded = st.pop().string; 
				//System.out.println("discarded "+to_be_discarded);   1  2  3 *+ 4  5 %- 6  7 *+ 8  9 / 10 *- 11 + 12  13 *- 14  15 /+ 16 - 17  18 *+ 19  20 *-
			}
		}
		// popping all elements from stack until we come across a ( parenthesis
		while(!operators_stack.isEmpty() && operators_stack.peek().token!=TokenType.OPEN){
			Token opr = operators_stack.pop();
			Expression right_child =  exprtree_stack.pop();
			Expression left_child =  exprtree_stack.pop();
			Expression sub_tree = new Expression(opr, left_child, right_child);
			exprtree_stack.push(sub_tree);
		}
		return exprtree_stack.peek();
    }

    // Given a list of tokens corresponding to an infix expression,
    // return its equivalent postfix expression as a list of tokens.
    public static List<Token> infixToPostfix(List<Token> exp) {  // To do
		Deque<Token> st = new ArrayDeque<>();
		List<Token> fin = new LinkedList<>();;
		for(Token t : exp){ // token is a number
			if(t.number != null){
				fin.add(t);
			}
			// token is an operator
			else if(t.token==TokenType.PLUS || t.token==TokenType.MINUS || t.token==TokenType.TIMES || t.token==TokenType.DIV
			|| t.token==TokenType.MOD || t.token==TokenType.POWER){
				if(st.peek() == null){
					st.push(t);
					continue;
				}
				// before pushing in the stack we check priority of the operator which is already in the stack
				while(st.peek().token != TokenType.OPEN && st.peek().priority>=t.priority){
					//System.out.println("peek"+ st.peek());
					fin.add(st.pop());
					if(st.peek() == null){
						break;
					}
				}
				st.push(t);
			}
			// if its an ( parethsis we push in the stack
			else if(t.token == TokenType.OPEN){
				st.push(t);
			}
			// and pop elements from stack until we come across ( in the stack
			else if(t.token == TokenType.CLOSE){
				while(st.peek()!=null && st.peek().token != TokenType.OPEN){
					fin.add(st.pop());
				}
				st.pop();
				//String to_be_discarded = st.pop().string; 
				//System.out.println("discarded "+to_be_discarded);   1  2  3 *+ 4  5 %- 6  7 *+ 8  9 / 10 *- 11 + 12  13 *- 14  15 /+ 16 - 17  18 *+ 19  20 *-
			}
		}
		// emptying the stack
		while(st.peek()!=null){
			fin.add(st.pop());
		}
		return fin;
    }

	public static long evaluate(long op1, long op2, Token opr){
	long result;
	switch(opr.token) {
	case TokenType.PLUS:
	    result = op1 + op2;  // modify if priority of "+" is not 1
	    break;
    case TokenType.TIMES:
		result = op1 * op2;  // modify if priority of "*" is not 1
	    break;
    case TokenType.MINUS:
		result = op1 - op2;  // modify if priority of "-" is not 1
	    break;
    case TokenType.DIV:
		result = op1 / op2;  // modify if priority of "/" is not 1
	    break;
    case TokenType.MOD:
		result = op1 % op2;  // modify if priority of "%" is not 1
	    break;
    case TokenType.POWER:
		result = (long)Math.pow(op1, op2);  // modify if priority of "+" is not 1
	    break;
	    // Complete rest of this method
	default:
	    result = 0;
	    break;
	}
	return result;

	}

    // Given a postfix expression, evaluate it and return its value.
    public static long evaluatePostfix(List<Token> exp) {  // To do
		Deque<Token> st = new ArrayDeque<>();
		for(Token t : exp){
			// if token is an operator
			if(t.token==TokenType.PLUS || t.token==TokenType.MINUS || t.token==TokenType.TIMES || t.token==TokenType.DIV
			|| t.token==TokenType.MOD || t.token==TokenType.POWER){
				if(st.peek() != null){
					Token op2 = st.pop();
					Token op1 = st.pop();
					Long val = evaluate(op1.number, op2.number, t);
					Token token_val = new Token(String.valueOf(val)); 
					st.push(token_val);
				}
			}
			// if its anything apart from operators, (, )
			else if(t.token!=TokenType.OPEN && t.token!=TokenType.CLOSE){
				st.push(t);
			}
		}
		// storing result which is stored in the stack top
		long res = st.peek().number;
		return res;
    }

    // Given an expression tree, evaluate it and return its value.
    public static long evaluateExpression(Expression tree) {  // To do
		if(tree.left == null && tree.right == null){
			// it is a number
			return tree.element.getValue();
		}
		else{ // it is an operator
			long l = evaluateExpression(tree.left);
			long r = evaluateExpression(tree.right);
			long ans = evaluate(l, r, tree.element);
			return ans;
		}
    }
	// printing the inorder traversal of the tree to check the tree
	public static void print(Expression tree){
		if(tree!= null){
			print(tree.left);
			System.out.print(tree.element + " ");
			print(tree.right);
	}
}

    // sample main program for testing
    public static void main(String[] args) throws FileNotFoundException {
	Scanner in;
	
	if (args.length > 0) {
	    File inputFile = new File(args[0]);
	    in = new Scanner(inputFile);
	} else {
	    in = new Scanner(System.in);
	}

	int count = 0;
	while(in.hasNext()) {
	    String s = in.nextLine();
	    List<Token> infix = new LinkedList<>();
	    Scanner sscan = new Scanner(s);
	    int len = 0;
	    while(sscan.hasNext()) {
		infix.add(getToken(sscan.next()));
		len++;
	    }
	    if(len > 0) {
		count++;
		System.out.println("Expression number: " + count);
		System.out.println("Infix expression: " + infix);
		Expression exp = infixToExpression(infix);
		List<Token> post = infixToPostfix(infix);
		System.out.println("printing tree");
		print(exp);
		System.out.println("Postfix expression: " + post);
		double pval = evaluatePostfix(post);
		double eval = evaluateExpression(exp);
		System.out.println("Postfix eval: " + pval + " Exp eval: " + eval + "\n");
	    }
	}
    }
}