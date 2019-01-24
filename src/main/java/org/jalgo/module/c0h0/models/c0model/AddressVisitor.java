package org.jalgo.module.c0h0.models.c0model;

import java.util.LinkedHashMap;
import java.util.Stack;

import org.jalgo.module.c0h0.models.ast.ASTVisitor;
import org.jalgo.module.c0h0.models.ast.Assignment;
import org.jalgo.module.c0h0.models.ast.Block;
import org.jalgo.module.c0h0.models.ast.If;
import org.jalgo.module.c0h0.models.ast.Symbol;
import org.jalgo.module.c0h0.models.ast.While;

/**
 * Assigns addresses to AST-Symbols. This is to be used with BFSIterator.
 * 
 * @author Peter Schwede
 * 
 */
public class AddressVisitor implements ASTVisitor {
	private class AddrStack extends Stack<Integer> {
		/**
		 * Internal class used for address generation
		 */
		private static final long serialVersionUID = -3589885062259573874L;

		public String toString() {
			String s = "f";
			for (Integer i : this) {
				s += i;
			}
			return s;
		}

		/**
		 * When calling AddrStack.clone() only the Stack head is cloned, not the
		 * contained list. This should do the work around.
		 * 
		 * @return
		 */
		public AddrStack deepClone() {
			AddrStack clone = new AddrStack();
			for (Integer item : this) {
				clone.push(item.intValue());
			}
			return clone;
		}
	}

	private AddrStack addr = new AddrStack();
	private LinkedHashMap<Symbol, AddrStack> awaitedIterables = new LinkedHashMap<Symbol, AddrStack>();

	public AddressVisitor() {
		addr.push(0);
	}

	private boolean isAwaited(Symbol s) {
		if (awaitedIterables.isEmpty() || !awaitedIterables.containsKey(s)) {
			return false;
		}
		addr = awaitedIterables.get(s);
		awaitedIterables.remove(s);
		return true;
	}

	private void await(Symbol s, AddrStack newAddr) {
		awaitedIterables.put(s, newAddr);
	}

	public void visitAssignment(Assignment assignment) {
		if (!isAwaited(assignment)) {
			addr.push(addr.pop() + 1);
		}
		assignment.setAddress(addr.toString());
	}

	public void visitBlock(Block block) {
		if (!isAwaited(block)) {
			addr.push(addr.pop() + 1);
		}
		if (block.isALie()) {
			block.setAddress("s" + addr.toString());
		} else {
			block.setAddress(addr.toString());
		}

		Symbol s = block.getStatement(0);
		if (block.isBlock()) {
			if (s != null) {
				AddrStack newAddr = addr.deepClone();
				newAddr.push(1);
				await(s, newAddr);
			}
		} else {
			if (addr.pop() == 1) {
				AddrStack newAddr = addr.deepClone();
				newAddr.push(1);
				await(s, newAddr);
			} else {
				AddrStack newAddr = addr.deepClone();
				newAddr.push(2);
				await(s, newAddr);
			}
		}
	}

	public void visitIf(If ifStatement) {
		if (!isAwaited(ifStatement)) {
			addr.push(addr.pop() + 1);
		}
		ifStatement.setAddress(addr.toString());

		Block i = ifStatement.getIfSequence();
		if (i != null) {
			AddrStack newAddr = addr.deepClone();
			newAddr.push(1);
			await(i, newAddr);
		}

		Block e = ifStatement.getElseSequence();
		if (e != null) {
			AddrStack newAddr = addr.deepClone();
			newAddr.push(2);
			await(e, newAddr);
		}
	}

	public void visitWhile(While whileStatement) {
		if (!isAwaited(whileStatement)) {
			addr.push(addr.pop() + 1);
		}
		whileStatement.setAddress(addr.toString());

		Block b = whileStatement.getBlock();
		if (b != null) {
			AddrStack newAddr = addr.deepClone();
			newAddr.push(1);
			await(b, newAddr);
		}
	}
}
