/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2010 j-Algo-Team, j-algo-development@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jalgo.module.am0c0.model.c0.trans;

import java.util.List;
import java.util.Map;

import org.jalgo.main.util.Messages;
import org.jalgo.module.am0c0.model.AddressException;
import org.jalgo.module.am0c0.model.LineAddress;
import org.jalgo.module.am0c0.model.TreeAddress;
import org.jalgo.module.am0c0.model.am0.*;
import org.jalgo.module.am0c0.model.c0.ast.C0AST;

/**
 * An atomic c0 statement (which can be used to generate an AM0
 * SimulationStatement)
 * 
 * @author Felix Schmitt
 * 
 */
public class AtomicTrans extends TransformFunction {

	public static enum AtomicType {
		ADD, DIV, EQ, GE, GT, JMC, JMP, LE, LT, LIT, LOAD, MOD, MUL, NE, READ, STORE, SUB, WRITE
	}

	protected AtomicType atomicType;
	protected int value;
	protected TreeAddress jumpAddress;
	protected String codePreview;

	/**
	 * creates a {@link SimulationStatement} from this {@link AtomicTrans}
	 * 
	 * @param addressTable
	 *            the table created by {@link AddressSolver} used for
	 *            translation
	 */
	protected SimulationStatement createSimulationStatement(LineAddress lineAddress,
			Map<String, LineAddress> addressTable) throws IllegalArgumentException {

		switch (atomicType) {
		case ADD:
			return new Add(lineAddress);
		case DIV:
			return new Div(lineAddress);
		case EQ:
			return new Equal(lineAddress);
		case GE:
			return new GreaterEqual(lineAddress);
		case GT:
			return new GreaterThen(lineAddress);
		case JMC:
			return new Jmc(lineAddress, addressTable.get(jumpAddress.toString()).getLine());
		case JMP:
			return new Jmp(lineAddress, addressTable.get(jumpAddress.toString()).getLine());
		case LE:
			return new LesserEqual(lineAddress);
		case LT:
			return new LesserThen(lineAddress);
		case LIT:
			return new Lit(lineAddress, value);
		case LOAD:
			return new Load(lineAddress, value);
		case MOD:
			return new Mod(lineAddress);
		case MUL:
			return new Mul(lineAddress);
		case NE:
			return new NotEqual(lineAddress);
		case READ:
			return new Read(lineAddress, value);
		case STORE:
			return new Store(lineAddress, value);
		case SUB:
			return new Sub(lineAddress);
		case WRITE:
			return new Write(lineAddress, value);
		default:
			throw new IllegalArgumentException(Messages.getString("am0c0", "AtomicTrans.0")); //$NON-NLS-1$
		}
	}

	/**
	 * returns the preview String for this AtomicTrans
	 * 
	 * @return the preview String
	 * @throws TransException
	 */
	protected String getPreview() throws TransException {
		switch (atomicType) {
		case JMC:
			return "JMC " + jumpAddress.toString() + ";"; //$NON-NLS-1$ //$NON-NLS-2$
		case JMP:
			return "JMP " + jumpAddress.toString() + ";"; //$NON-NLS-1$ //$NON-NLS-2$
		default:
			try {
				return createSimulationStatement(new LineAddress(0), null).getCodeText();
			} catch (IllegalArgumentException e) {
				throw new TransException(e.getMessage());
			} catch (AddressException e) {
				// there should be no AddressException here at all
				e.printStackTrace();
			}
			return "";
		}
	}

	/**
	 * constructor jumpAddress is set to null
	 * 
	 * @param token
	 *            must be null
	 * @param address
	 *            the TreeAddress for this TransformFunction
	 * @param atomicType
	 *            the AM0 type it represents
	 * @param value
	 *            for AM0 commands with fixed values (like LOAD,STORE or LIT),
	 *            set their value here for other statements, it will be ignored
	 */
	public AtomicTrans(C0AST token, TreeAddress address, AtomicType atomicType, int value) {
		this(token, address, atomicType, value, null);
	}

	/**
	 * constructor
	 * 
	 * @param token
	 *            must be null
	 * @param address
	 *            the TreeAddress for this TransformFunction
	 * @param atomicType
	 *            the AM0 type it represents
	 * @param value
	 *            for AM0 commands with fixed values (like LOAD,STORE or LIT),
	 *            set their value here. for other statements it will be ignored
	 * @param jumpAddress
	 *            target address for jump statements. null for other statements
	 */
	public AtomicTrans(C0AST token, TreeAddress address, AtomicType atomicType, int value,
			TreeAddress jumpAddress) throws IllegalArgumentException {
		super(token, address);
		this.atomicType = atomicType;
		this.value = value;
		this.jumpAddress = jumpAddress;

		if (token != null)
			throw new IllegalArgumentException(Messages.getString("am0c0", "AtomicTrans.5")); //$NON-NLS-1$

		if ((atomicType == AtomicType.JMC || atomicType == AtomicType.JMP) && (jumpAddress == null))
			throw new IllegalArgumentException(Messages.getString("am0c0", "AtomicTrans.6")); //$NON-NLS-1$

		try {
			codePreview = getPreview();
		} catch (TransException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	@Override
	/**
	 * see {@link TransformFunction#apply}
	 * this must not be called
	 */
	public List<TransformFunction> apply(SymbolTable symbolTable) {
		throw new IllegalAccessError(Messages.getString("am0c0", "AtomicTrans.7")); //$NON-NLS-1$
	}

	@Override
	/**
	 * see {@link TransformFunction#getCodeText}
	 */
	public String getCodeText() {
		return codePreview;
	}

	/**
	 * returns the {@link SimulationStatement} generated by this
	 * {@link AtomicTrans} function
	 * 
	 * @param lineAddress
	 *            the target {@link LineAddress} for the generated
	 *            {@link SimulationStatement}
	 * @param addressTable
	 *            the solved address table created by {@link AddressSolver}
	 * 
	 * @return the generated {@link SimulationStatement}
	 */
	public SimulationStatement getStatement(LineAddress lineAddress,
			Map<String, LineAddress> addressTable) throws IllegalArgumentException {
		return createSimulationStatement(lineAddress, addressTable);
	}

}
