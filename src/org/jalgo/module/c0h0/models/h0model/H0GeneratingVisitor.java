package org.jalgo.module.c0h0.models.h0model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.jalgo.module.c0h0.models.ast.ASTVisitor;
import org.jalgo.module.c0h0.models.ast.Assignment;
import org.jalgo.module.c0h0.models.ast.Block;
import org.jalgo.module.c0h0.models.ast.If;
import org.jalgo.module.c0h0.models.ast.Symbol;
import org.jalgo.module.c0h0.models.ast.Term;
import org.jalgo.module.c0h0.models.ast.While;

/**
 * Generates Haskell<sub>0</sub>-Code. Has to be used with BFSIterator.
 * 
 * This works like a automaton with memory and look-ahead marking
 * 
 * @author Peter Schwede
 * 
 */
public class H0GeneratingVisitor implements ASTVisitor {
	private List<String> plain = new ArrayList<String>(); // plain code
	private List<String> html = new ArrayList<String>(); // HTML code
	private List<Symbol> symbols = new ArrayList<Symbol>();; // Line <->
	// Symbol
	private List<Integer> steps = new ArrayList<Integer>(); // step <->
	// line
	private HashMap<Symbol, String> awaited = new LinkedHashMap<Symbol, String>();

	private int m, k, i, n; // s. script

	private int numberOfSymbols = 0;
	private int numberOfLines = 0;

	/**
	 * Constructor
	 * 
	 * sets m=1, k=1, i=1
	 */
	public H0GeneratingVisitor() {
		init(1, 1, 1);
	}

	/**
	 * Constructor
	 * 
	 * @param m
	 * @param k
	 * @param i
	 */
	public H0GeneratingVisitor(final int m, final int k, final int i) {
		init(m, k, i);
	}

	/**
	 * Sets the Haskell<sub>0</sub>-parameters m, k and i
	 * 
	 * @param m
	 * @param k
	 * @param i
	 */
	private void init(final int m, final int k, final int i) {
		n = 1;
		setMKI(m, k, i);
	}

	/**
	 * Makes setting parameters after construction possible
	 * 
	 * Also generates the head of the code. This should be called on any
	 * construction or reset of this visitor.
	 * 
	 * @param m
	 * @param k
	 * @param i
	 */
	public void setMKI(final int m, final int k, final int i) {
		clear();

		this.m = m;
		this.k = k;
		this.i = i;

		String line = "module Main where";
		addLine(line, null);
		addEmptyLine(null);
		addStep();
	}

	/**
	 * Generates the bottom part of the Haskell<sub>0</sub> code.
	 * 
	 * @return Code foot string
	 */
	public String foot() {
		StringBuilder foot = new StringBuilder();
		String line = "";

		// f(n) :: Int -> .. Int
		line = funcHead("f" + n);
		foot.append(line + "\n");

		// f(n) x1 .. xm
		line = "f" + n;
		line += varChain(m);
		line += " = x" + i;
		foot.append(line + "\n");

		// main = do x1 <- readln
		//           ..
		//           xk <- readln
		line = "main = do\n";
		for (int j = 1; j <= k; j++) {
			if(j > 1) {
				line = "    x" + j + " <- readLn\n";
				foot.append(line);
			} else {
				line += "    x" + j + " <- readLn\n";
				foot.append(line);
			}
		}
		

		// print (f1 x1 .. xk 0 .. 0);
		line = "    ";
		line += "print (f1";
		line += varChain(k);
		for (int j = 1; j <= m - k; j++) {
			line += " 0";
		}
		line += ")";
		foot.append(line + "\n");

		return foot.toString();
	}

	/**
	 * Generates Haskell<sub>0</sub>-foot with HMTL formatting
	 * 
	 * @return the html foot
	 */
	public String htmlFoot() {
		StringBuilder foot = new StringBuilder();
		String line = "<br>";

		//
		foot.append("<tr><td>&nbsp;</td></tr>");

		// f(n) :: Int -> .. Int
		line = htmlFuncHead("f" + n);
		foot.append("<tr><td class=\"f" + n + "\">");
		foot.append("<table><tr><td class=\"greyed\"><a href=\"f" + n
				+ "\" name=\"f" + n + "\">" + line + "</a></td></tr>");

		// f(n) x1 .. xm
		line = "f" + n;
		line += varChain(m);
		line += "&nbsp;=&nbsp;x" + i;
		foot.append("<tr><td><a href=\"f" + n + "\" name=\"f" + n + "\">"
				+ line + "</a></td></tr>");
		foot.append("</table>");

		foot.append("<tr><td>&nbsp;</td></tr>");

		// main = do x1 <- readln
		// ..
		// xk <- readln
		line = "main&nbsp;=&nbsp;do&nbsp;";
		line += "x" + 1 + "&nbsp;&lt;-&nbsp;readLn";
		foot.append("<tr><td class=\"foot\">" + line + "</td></tr>");
		for (int j = 2; j <= k; j++) {
			line = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
			line += "x" + j + "&nbsp;&lt;-&nbsp;readLn";
			foot.append("<tr><td class=\"foot\">" + line + "</td></tr>");
		}

		// print (f1 x1 .. xk 0 .. 0);
		line = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
		line += "&nbsp;print&nbsp;(f1";
		line += varChain(k);
		for (int j = 1; j <= m - k; j++) {
			line += "&nbsp;0";
		}
		line += ")";
		foot.append("<tr><td class=\"foot\">" + line + "</td></tr>");

		return foot.toString();
	}

	/**
	 * Generates Haskell<sub>0</sub> function head.
	 * 
	 * @param addr
	 *            Address string of which the increment shall be returned
	 * @param m
	 *            Number of defined varables
	 * @return f[addr] x1 .. xm
	 */
	private String funcHead(String addr) {
		String line = "";
		line += addr + " ::";
		for (int i = 0; i < m; i++) {
			line += " Int ->";
		}
		line += " Int";
		return line;
	}

	/**
	 * Generates Haskell<sub>0</sub> function head in HTML
	 * 
	 * @param address
	 * @return
	 */
	private String htmlFuncHead(String address) {
		String line = "";
		line += address + "&nbsp;::";
		for (int i = 0; i < m; i++) {
			line += "&nbsp;Int&nbsp;-&gt;";
		}
		line += " Int";
		return line;
	}

	/**
	 * Calls toString() on Term and transforms it to Haskell<sub>0</sub>-Syntax
	 * 
	 * @param termFromAst
	 * @return String representation.
	 */
	private String termTrans(Term termFromAst) {
		return termFromAst.toString().replaceAll("/", "`div`").replaceAll("!=",
				"/=").replaceAll("%", "`mod`");
	}

	/**
	 * Generates a string of 1..m variables
	 * 
	 * @see varString(int from, int to)
	 * 
	 * @param m
	 *            Number of Variables
	 * @return
	 */
	private String varChain(final int m) {
		return varChain(1, m);
	}

	/**
	 * Generates a string of from..to variables.
	 * 
	 * Attention: first character is a ' ' which is needed in most of the cases.
	 * 
	 * E.g.: to = 3, from = 1 --> " x1 x2 x3".
	 * 
	 * @param from
	 *            (usuall 1)
	 * @param to
	 *            (usually m)
	 * @return
	 */
	private String varChain(final int from, final int to) {
		String res = "";
		for (int j = from; j <= to; j++) {
			res += " x" + j;
		}
		return res;
	}

	/**
	 * Adds a line of code
	 * 
	 * Makes sure, that each one is added to both, <tt>plain text</tt> and
	 * <tt>html code</tt>. Also <tt>numberOfLines</tt> is increased accurately.
	 * 
	 * @param string
	 *            String to be added
	 * @param associate
	 *            Symbol to be associated with string
	 * @return
	 */
	private void addLine(String string, Symbol associate) {
		if (!symbols.contains(associate)) {
			numberOfSymbols++;
		}

		symbols.add(associate);
		plain.add(string + "\n");

		string = string.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		string = string.replaceAll(" ", "&nbsp;");

		String classStr = "";
		String addr = "none";
		if (associate != null) {
			classStr += associate.getAddress();
			addr = classStr;
		}
		boolean isFuncHead = string.contains("::");
		boolean isHead = string.contains("module");

		String line = "<tr><td class=\"" + addr + "\">";
		if (isFuncHead)
			line += "<table><tr><td class=\"greyed\">";
		if (isHead)
			line += "<table><tr><td class=\"head\">";
		if (!string.contains("module")) {
			if(isFuncHead)
				line += "<a name=\"" + addr + "\" href=\"" + addr + "\">";
			else
				line += "<a href=\"" + addr + "\">";
			line += string;
			line += "</a>";
		} else {
			line += string;
		}
		if (isHead)
			line += "</td></tr></table>";
		if (isFuncHead)
			line += "</td></tr></table>";
		line += "</td></tr>";
		html.add(line);

		numberOfLines++;
	}

	private void addEmptyLine(Symbol associate) {
		if (!symbols.contains(associate)) {
			numberOfSymbols++;
		}

		symbols.add(associate);
		plain.add("\n");
		html.add("<tr><td>&nbsp;</td></tr>");

		numberOfLines++;
	}

	/**
	 * Increments addresses on same level. E.g.: f212 --> f213
	 * 
	 * Also increases n if Address length equals 2. Therefore, n is always the
	 * number of the greatest Address. f2 --> n = 2
	 * 
	 * Attention: This method does <b>not</b> work properly for Addresses like
	 * <tt>f[1-9]*9</tt>
	 * 
	 * Please use it where you can!
	 * 
	 * @param address
	 *            address string
	 * @return new address string
	 */
	private String incAddr(String address) {
		int increment = Integer.valueOf(""
				+ address.charAt(address.length() - 1)) + 1;
		if (address.length() == 2 && increment > n)
			n = increment;
		String res = address.substring(0, address.length() - 1) + increment;
		return res;
	}

	/**
	 * Remembers line of code for later step performing
	 * 
	 * @param lineOfCode
	 */
	private void addStep() {
		steps.add(numberOfLines - 1);
	}

	private void beginFunction(String addr) {
		// html.add("<td class=\""+addr+"\"><table>");
	}

	private void endFunction() {
		// html.add("</table></td>");
	}

	/**
	 * Makes sure, a symbol is awaited only once.
	 * 
	 * @param s
	 * @param address
	 */
	private void awaitOnce(Symbol s, String address) {
		if (!awaited.containsKey(s)) {
			awaited.put(s, address);
		}
	}

	/*
	 * (non-Javadoc) Visits assignment
	 * 
	 * Generates code like follows with transTerm =
	 * transTerm(assignment.getTerm()):
	 * 
	 * <code>
	 * 
	 * f(n) :: Int .. -> Int (m-times)
	 * 
	 * f(n) x1 .. xm = f(n+1) x1 .. x(v-1) transTerm x(v+1) .. xm
	 * 
	 * </code>
	 * 
	 * Each line is added with <tt>addLine()</tt> to make sure some things. If
	 * the visitor has been waiting for this assignment, it uses it's designated
	 * address.
	 * 
	 * @see
	 * org.jalgo.module.c0h0.models.ast.ASTVisitor#visitAssignment(org.jalgo
	 * .module.c0h0.models.ast.Assignment)
	 */
	public void visitAssignment(Assignment assignment) {
		String addr = assignment.getAddress();
		String addr2 = "";
		if (awaited.containsKey(assignment)) {
			addr2 = awaited.get(assignment);
			awaited.remove(assignment);
		} else {
			addr2 = incAddr(addr);
		}
		int v = assignment.getVar().getIndex();

		beginFunction(addr);

		String line = "";

		// f(n) :: Int .. -> Int
		line += funcHead(addr);
		addLine(line, assignment);

		// f(n) x1 .. xm
		line = addr;
		line += varChain(m);
		// ... = f(n+1) x1 .. x(v-1) term x(v+1) .. xm
		line += " = " + addr2;
		if (v >= 1 && m >= 1 && v <= m)
			line += varChain(v - 1);
		line += " (" + termTrans(assignment.getTerm()) + ")";
		if (v >= 1 && m >= 1 && v <= m)
			line += varChain(v + 1, m);
		addLine(line, assignment);
		addEmptyLine(assignment);

		endFunction();

		addStep();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * Generates Haskell<sub>0</sub> code as follows:
	 * 
	 * <code>
	 * 
	 * f(a) x1 .. xm = f(a)1 x1 .. xm
	 * 
	 * </code>
	 * 
	 * Each line is added with <tt>addLine()</tt>.
	 * 
	 * This method also makes sure, the visitor awaits the last statement of the
	 * block. Note, that the same last statement will get the address, the block
	 * had been assigned to before.
	 * 
	 * @see
	 * org.jalgo.module.c0h0.models.ast.ASTVisitor#visitBlock(org.jalgo.module
	 * .c0h0.models.ast.Block)
	 */
	public void visitBlock(Block block) {
		if (block.isALie() || block.getStatementList().isEmpty()) { // ignore
			// empty
			// blocks
			if (awaited.containsKey(block))
				awaited.remove(block);
			return;
		}

		String addr = block.getAddress();
		if (awaited.containsKey(block)) {
			String addr2 = awaited.get(block);
			awaited.remove(block);
			awaitOnce(block.getLastStatement(), addr2);
		}

		beginFunction(addr);

		String line = "";

		line += funcHead(addr);
		addLine(line, block);

		line = addr + varChain(m);
		line += " = ";
		line += addr + "1" + varChain(m);
		addLine(line, block);
		addEmptyLine(block);

		endFunction();

		addStep();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * Generates code as follows with transTerm = transformed relation:
	 * 
	 * f(a) x1 .. xm = if (transTerm) then f(a)1 x1 .. xm
	 * 
	 * (if there's one:) else f(a)2 x1 .. xm
	 * 
	 * (if there's none:) else f(a+1) x1 .. xm
	 * 
	 * Each line is added with <tt>addLine()</tt>. If the visitor has been
	 * waiting for this if-statement, this statement sets it's address to it's
	 * destined one. After this, the visitor will look forward to visit the if's
	 * if-block and else-block (if there's any) with a wide grin on his face.
	 * 
	 * @see
	 * org.jalgo.module.c0h0.models.ast.ASTVisitor#visitIf(org.jalgo.module.
	 * c0h0.models.ast.If)
	 */
	public void visitIf(If ifStatement) {
		String addr = ifStatement.getAddress();
		String addr2;
		if (awaited.containsKey(ifStatement)) {
			addr2 = awaited.get(ifStatement);
			awaited.remove(ifStatement);
		} else {
			addr2 = incAddr(addr);
		}

		beginFunction(addr);

		String line = "";
		// check if If has defined else block
		line += funcHead(addr);
		addLine(line, ifStatement);

		// fa x1 ... xm = if (be) then fa1 x1 .. xm
		line = addr;
		line += varChain(m);
		line += " = if";
		line += " (" + termTrans(ifStatement.getRelation()) + ")";
		int indentLength = line.length(); // for pretty formating

		line += " then ";
		if (ifStatement.getIfSequence().getStatementList().isEmpty()) {
			line += addr2;
		} else {
			line += addr + "1";
			awaitOnce(ifStatement.getIfSequence().getLastStatement(), addr2);
		}
		line += varChain(m);
		addLine(line, ifStatement);

		line = "";
		// add some spaces
		for (int j = 0; j < indentLength; j++) {
			line += " ";
		}

		line += " else ";
		if (ifStatement.getElseSequence().getSequence().isEmpty()) {
			line += addr2;
		} else {
			line += addr + "2";
			awaitOnce(ifStatement.getElseSequence().getLastStatement(), addr2);
		}
		line += varChain(m);
		addLine(line, ifStatement);
		addEmptyLine(ifStatement);

		endFunction();

		addStep();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * Generates code as follows:
	 * 
	 * <code>
	 * 
	 * f(a) x1 .. xm = if (transTerm) f(a)1 x1 .. xm
	 * 
	 * else f(a+1) x1 .. xm
	 * 
	 * </code>
	 * 
	 * If the visitor has waited for this while statement, this particular while
	 * statement will await it's last statement with it's destined address. Just
	 * like and if, else do
	 * 
	 * @see
	 * org.jalgo.module.c0h0.models.ast.ASTVisitor#visitWhile(org.jalgo.module
	 * .c0h0.models.ast.While)
	 */
	public void visitWhile(While whileStatement) {
		String addr = whileStatement.getAddress();
		String addr2;
		if (awaited.containsKey(whileStatement)) {
			addr2 = awaited.get(whileStatement);
			awaited.remove(whileStatement);
		} else {
			addr2 = incAddr(addr);
		}

		beginFunction(addr);

		String line = "";
		line += funcHead(addr);
		addLine(line, whileStatement);

		line = addr;
		line += varChain(m);
		line += " = if";
		line += " (" + termTrans(whileStatement.getRelation()) + ")";
		int indentLength = line.length(); // for pretty plain text formation

		if (whileStatement.getBlock().getStatementList().isEmpty()) {
			// empty block
			line += " then " + incAddr(addr);
		} else {
			line += " then " + addr + "1";
		}
		line += varChain(m);
		awaitOnce(whileStatement.getBlock().getLastStatement(), addr); // the
		// return
		addLine(line, whileStatement);

		line = "";
		// add some spaces
		for (int j = 0; j < indentLength; j++) {
			line += " ";
		}
		line += " else " + addr2;
		line += varChain(m);
		addLine(line, whileStatement);
		awaitOnce(whileStatement.getBlock(), addr2);
		addEmptyLine(whileStatement);

		endFunction();

		addStep();
	}

	/**
	 * Returns plain Haskell<sub>0</sub> code. (E.g. for saving)
	 * 
	 * @return Complete Haskell<sub>0</sub>-Code with head, body and foot.
	 */
	public String getPlain() {
		StringBuilder bob = new StringBuilder();
		for (String line : plain)
			bob.append(line);
		bob.append(foot());
		return bob.toString();
	}

	/**
	 * @return HTML formatted Haskell<sub>0</sub>-Code. (E.g. for viewing)
	 */
	public String getHTML() {
		return getHTML(0, html.size() - 1, true);
	}

	/**
	 * @return HTML formatted Haskell<sub>0</sub>-Code
	 */
	public String getHTML(final int from, final int to, boolean withFoot) {
		StringBuilder bob = new StringBuilder();
		bob.append("<table>");
		if (to < html.size()) {
			for (String line : html.subList(from, to)) {
				bob.append(line);
			}
			if (withFoot) {
				bob.append(htmlFoot());
			}
		}
		bob.append("</table>");
		bob.append("<table><tr><td>&nbsp;</td></tr></table>");
		return bob.toString();
	}

	/**
	 * @return A List of Symbols so that each generated code-line is associated
	 *         to anc AST-symbol
	 */
	public List<Symbol> getSymbols() {
		return symbols;
	}

	/**
	 * Counts Symbols and returns
	 */
	public int getNumberOfSymbols() {
		return numberOfSymbols;
	}

	/**
	 * Address of the Print
	 */
	public String getPrintAddress() {
		return "f" + n;
	}

	/**
	 * Returns the number of Lines
	 */
	public int getNumberOfLines() {
		return numberOfLines;
	}

	/**
	 * Returns the lines associated to steps
	 */
	public List<Integer> getSteps() {
		return steps;
	}
	
	/**
	 * Returns the Number of foot-lines that print results in H0
	 * @return k
	 */
	public int getNumberOfOutputLines() {
		return k;
	}

	/**
	 * Resets this Visitor
	 * 
	 * To reuse an Object is faster! (s. Effective Java)
	 */
	public void clear() {
		plain.clear();
		html.clear();
		symbols.clear();
		steps.clear();
		awaited.clear();
		m = 0;
		k = 0;
		i = 0;
		n = 1;
		numberOfSymbols = 0;
		numberOfLines = 0;
	}
}
