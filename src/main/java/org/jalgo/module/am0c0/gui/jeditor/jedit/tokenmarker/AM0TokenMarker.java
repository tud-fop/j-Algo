/*
 * CTokenMarker.java - C token marker
 * Copyright (C) 1998, 1999 Slava Pestov
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */

package org.jalgo.module.am0c0.gui.jeditor.jedit.tokenmarker;

import javax.swing.text.Segment;

import org.jalgo.module.am0c0.gui.jeditor.jedit.KeywordMap;

/**
 * AM0 token marker.
 * 
 * @author Felix Schmitt
 */
public class AM0TokenMarker extends TokenMarker {
	public AM0TokenMarker() {
		this(getKeywords());
	}

	public AM0TokenMarker(KeywordMap keywords) {
		this.keywords = keywords;
	}

	public byte markTokensImpl(byte token, Segment line, int lineIndex) {
		char[] array = line.array;
		int offset = line.offset;
		lastOffset = offset;
		lastKeyword = offset;
		int length = line.count + offset;

		for (int i = offset; i < length; i++) {
			char c = array[i];

			switch (token) {
			case Token.NULL:
				switch (c) {
				default:
					if (!Character.isLetterOrDigit(c) && c != '_')
						doKeyword(line, i, c);
					break;
				}
				break;
			default:
				throw new InternalError("Invalid state: " + token);
			}
		}

		if (token == Token.NULL)
			doKeyword(line, length, '\0');

		switch (token) {
		default:
			addToken(length - lastOffset, token);
			break;
		}

		return token;
	}

	public static KeywordMap getKeywords() {
		if (cKeywords == null) {
			cKeywords = new KeywordMap(false);
			cKeywords.add("ADD", Token.KEYWORD1);
			cKeywords.add("MUL", Token.KEYWORD1);
			cKeywords.add("SUB", Token.KEYWORD1);
			cKeywords.add("DIV", Token.KEYWORD1);
			cKeywords.add("MOD", Token.KEYWORD1);
			cKeywords.add("LT", Token.KEYWORD1);
			cKeywords.add("EQ", Token.KEYWORD1);
			cKeywords.add("NE", Token.KEYWORD1);
			cKeywords.add("GT", Token.KEYWORD1);
			cKeywords.add("LE", Token.KEYWORD1);
			cKeywords.add("GE", Token.KEYWORD1);
			cKeywords.add("LOAD", Token.KEYWORD1);
			cKeywords.add("LIT", Token.KEYWORD1);
			cKeywords.add("STORE", Token.KEYWORD1);
			cKeywords.add("JMP", Token.KEYWORD1);
			cKeywords.add("JMC", Token.KEYWORD1);
			cKeywords.add("READ", Token.KEYWORD1);
			cKeywords.add("WRITE", Token.KEYWORD1);
		}
		return cKeywords;
	}

	// private members
	private static KeywordMap cKeywords;

	private KeywordMap keywords;
	private int lastOffset;
	private int lastKeyword;

	private boolean doKeyword(Segment line, int i, char c) {
		int i1 = i + 1;

		int len = i - lastKeyword;
		byte id = keywords.lookup(line, lastKeyword, len);
		if (id != Token.NULL) {
			if (lastKeyword != lastOffset)
				addToken(lastKeyword - lastOffset, Token.NULL);
			addToken(len, id);
			lastOffset = i;
		}
		lastKeyword = i1;
		return false;
	}
}
