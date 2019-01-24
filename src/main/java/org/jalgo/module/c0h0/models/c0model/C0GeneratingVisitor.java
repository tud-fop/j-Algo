package org.jalgo.module.c0h0.models.c0model;

import java.util.LinkedList;

import org.jalgo.module.c0h0.models.ast.ASTVisitor;
import org.jalgo.module.c0h0.models.ast.Assignment;
import org.jalgo.module.c0h0.models.ast.Block;
import org.jalgo.module.c0h0.models.ast.If;
import org.jalgo.module.c0h0.models.ast.Symbol;
import org.jalgo.module.c0h0.models.ast.While;

/**
 * Generates C<sub>0</sub>-Code. This is to be used with DFSIterator.
 * 
 */
public class C0GeneratingVisitor implements ASTVisitor {
	private StringBuilder code;
	private int addressCount, statementblockdepth;
	private LinkedList<String> addressStack;
	private LinkedList<String> addressListOutput;

	public C0GeneratingVisitor() {
		statementblockdepth = 0;
		code = new StringBuilder();
		code.append("<table>");
		addressStack = new LinkedList<String>();
		addressListOutput = new LinkedList<String>();
	}

	/**
	 * Closes all While/else-Blocks
	 */
	private void closeBlocks() {
		// Wir muessen nur alle while- und else-Bloecke schliessen, weil wir in
		// jedem Fall den then-Block im visitBlock() abfangen koennen
		while (
		// Erster Fall: f1, sf11, f11, sf111, f111, sf1111, f1111, f12 =>
		// bekommen f12 => Resultat f1, sf11, f11, f12
		(addressStack.size() > 3
				&& addressStack.getLast().length() < addressStack.get(
						addressStack.size() - 2).length() &&
		// Obriges Bsp: aber diesemal bekommen wir z.B. f1111 => erste
		// Bedingungen waere ok => falsch
		!addressStack.get(addressStack.size() - 2).equals(
				"s" + addressStack.getLast()))
				|| (
				// Zweiter Fall: f1, sf11, f11, sf111, f111, sf1111, f1111, sf12
				// => bekommen sf12 => Resultat f1, sf11, f11, sf12
				String.valueOf(addressStack.getLast().charAt(0)).equals("s") && (addressStack
						.getLast().length() - 1) < addressStack.get(
						addressStack.size() - 2).length())) {

			// Algo zum Abbauen von while jeglicher Form(=> f- oder s-Adresse)
			// Bekannt hierfuer ist aber bereits, dass dieser Block abgebaut
			// werden muss

			if (
			// Erster Fall: f1, sf11, f11, xx => nur ein Statement und Block
			// abbauen
			addressStack.get(addressStack.size() - 3).equals(
					"s" + addressStack.get(addressStack.size() - 2))) {
				addressStack.remove(addressStack.size() - 2);
				addressStack.remove(addressStack.size() - 2);

				// Nun bauen wir die table vom while/else ab
				code.append("</table></td></tr>");

				statementblockdepth--;
			} else {
				// Zweiter Fall: f1, f11, f111, f112, ..., f119, xx => Block und
				// alle Statements in diesem abbauen
				int end = addressStack.size() - 1;
				int extra = addressStack.get(addressStack.size() - 2)
						.substring(
								0,
								addressStack.get(addressStack.size() - 2)
										.length() - 1).length()
						- addressStack.getLast().length();
				for (int i = addressStack
						.indexOf(addressStack.get(addressStack.size() - 2)
								.substring(
										0,
										addressStack.get(
												addressStack.size() - 2)
												.length() - 1)); i < end; i++) {
					addressStack.remove(addressStack.size() - 2);
				}
				// Neues Statement oeffnen
				code.append("<tr><td>");
				// Wir haengen die schliessende Klammer an
				code.append("<table><tr><td>");
				// Die Tiefe besteht aus abgezaehlt vielen erzwungenen
				// Leerzeichen
				addSpaces(extra);
				code.append("</td><td>");
				// Nun noch das Token selbst mit dem Link hinzufuegen
				code.append("}");
				// Das erste Statement wird natuerlich direkt geschlossen
				code.append("</td></tr></table>");
				// Nun schliessen wir das Statement
				code.append("</td></tr>");
				// Zuletzt bauen wir die table vom while/else ab
				code.append("</table></td></tr>");

				// Adresse fuer die linke Seite hinzufuegen
				addressListOutput.add("br");
			}
		}
	}

	/**
	 * Initializes Statements: while/if/Assignment
	 * 
	 * @param curdepth
	 *            Integer with the current depth of the Symbol
	 * @param symbol
	 *            Symbol to be displayed
	 */
	private void initStatement(int curdepth, Symbol symbol) {
		// Als erstes bauen wir unseren Stack weiter auf
		addressStack.add(symbol.getAddress());

		// Nun schliessen wir alle Bloecke
		closeBlocks();

		// Anschliessend brauchen wir jedoch unser Statement auf der richtigen
		// Hoehe
		code.append("<tr>");
		code.append("<td class=\"" + symbol.getAddress() + "\">");
		code.append("<table><tr><td>");
		int bonus = 0;
		for (String s : addressStack) {
			if (String.valueOf(s.charAt(0)).equals("s")) {
				bonus++;
			}
		}
		for (int i = 0; i < addressStack.getLast().length() + bonus; i++) {
			code.append("&nbsp;");
		}
		code.append("</td>");
	}

	/**
	 * Creates a HTML-Tag with an attribute "token"
	 * 
	 * @param symbol
	 *            Symbol to be displayed
	 * @param content
	 *            String with the content(for instance: while/if)
	 */
	private void createToken(Symbol symbol, String content) {
		initStatement(symbol.getAddress().substring(1).length(), symbol);
		code.append("<td>");
		code.append("<a class=\"token\" name=\"" + symbol.getAddress()
				+ "\" href=\"" + symbol.getAddress() + "\">");
		code.append(content + "</a></td><td>");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jalgo.module.c0h0.models.ast.ASTVisitor#visitAssignment(org.jalgo
	 * .module.c0h0.models.ast.Assignment)
	 */
	public void visitAssignment(Assignment assignment) {
		initStatement(assignment.getAddress().substring(1).length(), assignment);
		code.append("<td>");
		code.append("<a name=\"" + assignment.getAddress() + "\" href=\""
				+ assignment.getAddress() + "\">");
		code.append(assignment.getVar()
				+ " = "
				+ assignment.getTerm().toString().replaceAll("<", "&lt;")
						.replaceAll("<=", "&lt;=") + ";");
		code.append("</a></td></tr></table></td></tr>");
		addressListOutput.add(assignment.getAddress());
		addressCount++;
	}

	/**
	 * Adds the amount of spaces which is needed to line up the code formatted
	 * 
	 * @param extra
	 *            Integer with an extra amount of Spaces
	 */
	private void addSpaces(int extra) {
		int bonus = 0;
		if (String.valueOf(addressStack.getLast().charAt(0)).equals("s")) {
			bonus += 2;
		}
		for (int i = 0; i < addressStack.getLast().length() - bonus + extra
				+ statementblockdepth - 1; i++) {
			code.append("&nbsp;");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jalgo.module.c0h0.models.ast.ASTVisitor#visitBlock(org.jalgo.module
	 * .c0h0.models.ast.Block)
	 */
	public void visitBlock(Block block) {
		/*
		 * Notizen: Block.isBlock()mit leerer StatementList muss gezeichnet
		 * werden Block.isALie() mit leerer StatementList ist auf jeden Fall ein
		 * nicht vorhandenes else!
		 */

		// Aufteilung des Blocks in die unterschiedlichen Moeglichkeiten
		if (block.isBlock()) {
			// Block mit Klammern
			if (addressStack.contains(block.getAddress().substring(0,
					block.getAddress().length() - 1)
					+ "1")) {
				// Erster Block hat eine Klammer

				// Um doppelten Code zu ersparen:
				addressStack.add(block.getAddress().substring(0,
						block.getAddress().length() - 1));
				closeBlocks();
				addressStack.removeLast();
				addressStack.add(block.getAddress());

				// Neue Reihe
				code.append("<tr>");
				// Alles in diesem Block hat die oberste Adresse
				code.append("<td class=\"" + block.getAddress() + "\">");
				// Der Block besteht aus einer table mit allen Statements in
				// dieser
				code.append("<table><tr><td>");
				// Das erste Statement der table ist die oeffnende Klammer
				code.append("<table><tr><td>");
				// Die Tiefe besteht aus abgezaehlt vielen erzwungenen
				// Leerzeichen
				addSpaces(0);
				code.append("</td><td>");
				// Nun noch das Token selbst mit dem Link hinzufuegen
				code.append("<a class=\"token\" href=\"" + block.getAddress()
						+ "\">");
				code.append("else");
				code.append("</a>");
				// Die interne table des Statements wird natuerlich direkt
				// geschlossen
				code.append("</td></tr></table>");
				// Das Statement abschliessen
				code.append("</td></tr>");

				// Adresse fuer die linke Seite hinzufuegen
				addressListOutput.add("br");

				// Neues Statement
				code.append("<tr><td>");
				// Das erste Statement der table ist die oeffnende Klammer
				code.append("<table><tr><td>");
				// Die Tiefe besteht aus abgezaehlt vielen erzwungenen
				// Leerzeichen
				addSpaces(0);
				code.append("</td><td>");
				// Nun noch das Token selbst mit dem Link hinzufuegen
				code.append("<a href=\"" + block.getAddress() + "\">");
				code.append("{");
				code.append("</a>");
				// Die interne table des Statements wird natuerlich direkt
				// geschlossen
				code.append("</td></tr></table>");
				// Das Statement abschliessen
				code.append("</td></tr>");

				// Adresse fuer die linke Seite hinzufuegen
				addressListOutput.add(block.getAddress());
				addressCount++;
			} else if (addressStack.contains("s"
					+ block.getAddress().substring(0,
							block.getAddress().length() - 1) + "1")) {
				// Erster Block ist kein Block

				// Um doppelten Code zu ersparen:
				addressStack.add(block.getAddress().substring(0,
						block.getAddress().length() - 1));
				closeBlocks();
				addressStack.removeLast();
				addressStack.add(block.getAddress());

				// Neue Reihe
				code.append("<tr>");
				// Alles in diesem Block hat die oberste Adresse
				code.append("<td class=\"" + block.getAddress() + "\">");
				// Der Block besteht aus einer table mit allen Statements in
				// dieser
				code.append("<table><tr><td>");
				// Das erste Statement der table ist die oeffnende Klammer
				code.append("<table><tr><td>");
				// Die Tiefe besteht aus abgezaehlt vielen erzwungenen
				// Leerzeichen
				addSpaces(0);
				code.append("</td><td>");
				// Nun noch das Token selbst mit dem Link hinzufuegen
				code.append("<a class=\"token\" href=\"" + block.getAddress()
						+ "\">");
				code.append("else");
				code.append("</a>");
				// Die interne table des Statements wird natuerlich direkt
				// geschlossen
				code.append("</td></tr></table>");
				// Das Statement abschliessen
				code.append("</td></tr>");

				// Adresse fuer die linke Seite hinzufuegen
				addressListOutput.add("br");

				// Neues Statement
				code.append("<tr><td>");
				// Das erste Statement der table ist die oeffnende Klammer
				code.append("<table><tr><td>");
				// Die Tiefe besteht aus abgezaehlt vielen erzwungenen
				// Leerzeichen
				addSpaces(0);
				code.append("</td><td>");
				// Nun noch das Token selbst mit dem Link hinzufuegen
				code.append("<a href=\"" + block.getAddress() + "\">");
				code.append("{");
				code.append("</a>");
				// Die interne table des Statements wird natuerlich direkt
				// geschlossen
				code.append("</td></tr></table>");
				// Das Statement abschliessen
				code.append("</td></tr>");

				// Adresse fuer die linke Seite hinzufuegen
				addressListOutput.add(block.getAddress());
				addressCount++;
			} else {
				// Wir haben gerade einen einwandfreien neuen Block
				// reinbekommen(while oder then-Block)

				// Zuerst fuellen wir unseren Stack auf
				addressStack.add(block.getAddress());

				// Neue Reihe
				code.append("<tr>");
				// Alles in diesem Block hat die oberste Adresse
				code.append("<td class=\"" + block.getAddress() + "\">");
				// Der Block besteht aus einer table mit allen Statements in
				// dieser
				code.append("<table><tr><td>");
				// Das erste Statement der table ist die oeffnende Klammer
				code.append("<table><tr><td>");
				// Die Tiefe besteht aus abgezaehlt vielen erzwungenen
				// Leerzeichen
				addSpaces(0);
				code.append("</td><td>");
				// Nun noch das Token selbst mit dem Link hinzufuegen
				code.append("<a href=\"" + block.getAddress() + "\">");
				code.append("{");
				code.append("</a>");
				// Die interne table des Statements wird natuerlich direkt
				// geschlossen
				code.append("</td></tr></table>");
				// Das Statement abschliessen
				code.append("</td></tr>");

				// Adresse fuer die linke Seite hinzufuegen
				addressListOutput.add(block.getAddress());
				addressCount++;

				if (block.getStatementList().isEmpty()) {
					// Der Block ist leer => wir sollten ein PseudoStatment in
					// den addressStack einbauen
					addressStack.add(block.getAddress() + "1");
				}
			}
		} else {
			// Block ohne Klammern
			if (addressStack.contains(block.getAddress().substring(0,
					block.getAddress().length() - 1)
					+ "1")) {
				// Erster Block ist kein Block
				if (block.getStatementList().isEmpty()) {
					// Else-Block ist nicht vorhanden:
					// Wir machen in diesem Fall einfach nichts, sodass es
					// spaeter als while erkannt wird
				} else {
					/*
					 * Bsp.:
					 * 
					 * if(x1<x2) while(x1 > 2) { if(x1 > 10) x2 = 3; if(x1 < 10)
					 * x2 = 4; else x2 = 5; while(x1 > 2) x1 = x1 - 1; } else
					 * x3=x2;
					 */

					// Um doppelten Code zu ersparen:
					addressStack.add(block.getAddress().substring(1,
							block.getAddress().length() - 1));
					closeBlocks();
					addressStack.removeLast();
					addressStack.add(block.getAddress());

					// Es existiert ein weiterer Statement-Block in der Tiefe
					statementblockdepth++;

					// Neue Reihe
					code.append("<tr>");
					// Alles in diesem Block hat die oberste Adresse
					code.append("<td class=\"" + block.getAddress() + "\">");
					// Der Block besteht aus einer table mit allen Statements in
					// dieser
					code.append("<table><tr><td>");
					// Das erste Statement der table ist die oeffnende Klammer
					code.append("<table><tr><td>");
					// Die Tiefe besteht aus abgezaehlt vielen erzwungenen
					// Leerzeichen
					addSpaces(0);
					code.append("</td><td class=\"token\">");
					// Nun noch das Token selbst hinzufuegen
					code.append("else");
					// Die interne table des Statements wird natuerlich direkt
					// geschlossen
					code.append("</td></tr></table>");
					// Das Statement abschliessen
					code.append("</td></tr>");

					// Adresse fuer die linke Seite hinzufuegen
					addressListOutput.add("br");
				}
			} else if (addressStack.contains(block.getAddress().substring(1,
					block.getAddress().length() - 1)
					+ "1")) {
				// Erster Block hat eine Klammer
				if (block.getStatementList().isEmpty()) {
					// Else-Block ist nicht vorhanden
					// Wir machen in diesem Fall einfach nichts, sodass es
					// spaeter als while erkannt wird
				} else {
					// Um doppelten Code zu ersparen:
					addressStack.add(block.getAddress().substring(0,
							block.getAddress().length() - 1));
					closeBlocks();
					addressStack.removeLast();
					addressStack.add(block.getAddress());

					// Es existiert ein weiterer Statement-Block in der Tiefe
					statementblockdepth++;

					// Neue Reihe
					code.append("<tr>");
					// Alles in diesem Block hat die oberste Adresse
					code.append("<td class=\"" + block.getAddress() + "\">");
					// Der Block besteht aus einer table mit allen Statements in
					// dieser
					code.append("<table><tr><td>");
					// Das erste Statement der table ist die oeffnende Klammer
					code.append("<table><tr><td>");
					// Die Tiefe besteht aus abgezaehlt vielen erzwungenen
					// Leerzeichen
					addSpaces(0);
					code.append("</td><td class=\"token\">");
					// Nun noch das Token selbst hinzufuegen
					code.append("else");
					// Die interne table des Statements wird natuerlich direkt
					// geschlossen
					code.append("</td></tr></table>");
					// Das Statement abschliessen
					code.append("</td></tr>");

					// Adresse fuer die linke Seite hinzufuegen
					addressListOutput.add("br");
				}
			} else {
				// Wir haben gerade einen einwandfreien neuen Block
				// reinbekommen(while oder then-Block)

				// Zuerst fuellen wir unseren Stack auf
				addressStack.add(block.getAddress());

				// Es existiert ein weiterer Statement-Block in der Tiefe
				statementblockdepth++;

				// Neue Reihe
				code.append("<tr>");
				// Alles in diesem Block hat die oberste Adresse
				code.append("<td class=\"" + block.getAddress() + "\">");
				// Der Block besteht aus einer table mit allen Statements in
				// dieser
				code.append("<table>");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jalgo.module.c0h0.models.ast.ASTVisitor#visitIf(org.jalgo.module.
	 * c0h0.models.ast.If)
	 */
	public void visitIf(If ifStatement) {
		createToken(ifStatement, "if");
		code.append("<a href=\"" + ifStatement.getAddress() + "\">");
		code.append("&nbsp;("
				+ ifStatement.getRelation().toString().replaceAll("<", "&lt;")
						.replaceAll("<=", "&lt;=") + ")");
		code.append("</a></td></tr></table></td></tr>");
		addressListOutput.add(ifStatement.getAddress());
		addressCount++;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jalgo.module.c0h0.models.ast.ASTVisitor#visitWhile(org.jalgo.module
	 * .c0h0.models.ast.While)
	 */
	public void visitWhile(While whileStatement) {
		createToken(whileStatement, "while");
		code.append("<a href=\"" + whileStatement.getAddress() + "\">");
		code.append("&nbsp;("
				+ whileStatement.getRelation().toString().replaceAll("<",
						"&lt;").replaceAll("<=", "&lt;=") + ")");
		code.append("</a></td></tr></table></td></tr>");
		addressListOutput.add(whileStatement.getAddress());
		addressCount++;
	}

	/**
	 * Returns the created code
	 * 
	 * @return code String with HTML-formatted C0-Code
	 */
	public String getCode() {
		// Wir muessen sicherstellen, dass es noch ein letztes Element in der
		// niedrigsten Ebene gibt
		addressStack.add("e1");
		closeBlocks();
		return code.toString();
	}

	/**
	 * Returns a LinkedList with all Addresses
	 * 
	 * @return addressListOutput LinkedList with all Addresses
	 */
	public LinkedList<String> getAddressList() {
		return addressListOutput;
	}

	/**
	 * Returns an Integer how many addresses are displayed
	 * 
	 * @return addressCount Integer which sums up all addresses
	 */
	public int getAddressCount() {
		return addressCount;
	}
}
