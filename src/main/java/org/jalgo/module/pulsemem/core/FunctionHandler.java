package org.jalgo.module.pulsemem.core;

import java.util.ArrayList;
import org.jalgo.module.pulsemem.core.exceptions.EFunctionNotImplemented;
import org.jalgo.module.pulsemem.core.exceptions.EIncompatibleParameterCount;
import org.jalgo.module.pulsemem.core.exceptions.EIncompatibleParameterTypes;
import org.jalgo.module.pulsemem.core.exceptions.EReturnException;
import c00.AST;
import c00.AST.ASTTree;
import c00.AST.Block;
import c00.AST.FunctionHeading;
import c00.AST.IntFuncHead;
import c00.AST.VoidFuncHead;


/**
 * This class holds all the information about a function.
 *
 * @author Joachim Protze
**/
public class FunctionHandler {
	private AST.FunctionHeading head;
	private AST.Block block;
	private Boolean returns=false;
	private myInterpreter interpreter;
	private String name;
	private int [] scope;

	public String getName() {
		return name;
	}

	public int [] getScope(){
		return scope;
	}

	public FunctionHeading getFunctionHeading() {
		return head;
	}

	/**
	 * Handles a C00-Function. Instanciated at declarationtime. Invoked at functioncalls.
	 * @param head is the functionHead, as declared in the functiondeclaration.
	 * @param block is the function-body, as declared in the functiondeclaration.
	 * @param interpreter is the instanciating Interpreter, to interprete the block.
	 */
	public FunctionHandler(FunctionHeading head, Block block, myInterpreter interpreter){
		this.head=head;
		this.block=block;
		if (head instanceof AST.IntFuncHead){
			returns=true;
			name=((IntFuncHead)head).ident;
		}else{
			name=((VoidFuncHead)head).ident;
		}
		this.interpreter=interpreter;
		scope=new int [] {head.startLine,getTreeRoot(head).endLine};
	}

	/**
	 * Handles a C00-Function. Instanciated w/o body(block) as predeclaration.
	 * @param head is the functionHead, as declared in the functiondeclaration.
	 * @param interpreter is the instanciating Interpreter, to interprete the block.
	 */
	public FunctionHandler(FunctionHeading head, myInterpreter interpreter){
		this.head=head;
		this.block=null;
		if (head instanceof AST.IntFuncHead){
			returns=true;
			name=((IntFuncHead)head).ident;
		}else{
			name=((VoidFuncHead)head).ident;
		}
		this.interpreter=interpreter;
		scope=new int [] {head.startLine,getTreeRoot(head).endLine};
	}

	/**
	 * returns the TreeRoot
	 * @param var
	 * @return the TreeRoot
	 */
	private ASTTree getTreeRoot(ASTTree ast) {
		if (ast.parent == null )
			return ast;
		return getTreeRoot(ast.parent);
	}

	/**
	 * Handle a C00-functioncall. returns the returnvalue of the C00-Function
	 * @param paramList
	 * @return return-value
	 */
	public Object callFunction(ArrayList<Object> paramList){
		if (block==null)
			throw new EFunctionNotImplemented(head.startLine);
		ArrayList<Variable> list=(ArrayList<Variable>)interpreter.interpret(head);
		if (paramList.size()!=list.size()){
			throw new EIncompatibleParameterCount(""+list.size(),""+paramList.size());
		}
/*		for (Object i: paramList){
			if (i instanceof Variable){
				System.out.println(((Variable)i).getName()+"("+((Variable)i).getVisibilityLevel()+")");
			}else{
				System.out.println((Integer)i);
			}
		}*/
		for (Variable i: list){
			if (i instanceof Zeiger){
				if (paramList.get(list.indexOf(i)) instanceof Variable)
					((Zeiger)i).setTarget((Variable)paramList.get(list.indexOf(i)));
				else
					throw new EIncompatibleParameterTypes("Pointer","int",this.name,list.indexOf(i));
			}else{
				if (paramList.get(list.indexOf(i)) instanceof Integer)
					i.setValue((Integer)paramList.get(list.indexOf(i)));
				else
					throw new EIncompatibleParameterTypes("int","Pointer",this.name,list.indexOf(i));
			}
		}
		Object ret= interpreter.interpret(block);
		interpreter.setReturnFlag(false);
		if (returns)
			if(ret instanceof Integer){
				return ret;
			}else{
				throw new EReturnException("void","int",block.endLine);
			}
		else
			if(ret instanceof Integer){
				throw new EReturnException("int","void",block.endLine);
			}else{
				return null;
			}
	}

	/**
	 * set functionBlock - used for predeclared functions
	 * @param block
	 */
	public void setBlock(AST.Block block) {
		this.block = block;
	}

	/**
	 * compares provided functionhead for lexicaly equality to the local functionhead
	 * intended for checking equality of predeclared functionhead and functionimplementationhead
	 * @param funcHead
	 * @return true on equality
	 */
	public Boolean equalHeads(FunctionHeading funcHead) {
		CodeGenerator cg1 = new CodeGenerator();
		CodeGenerator cg2 = new CodeGenerator();
		cg1.SwitchFunctionHeading(funcHead);
		cg2.SwitchFunctionHeading(this.head);
		if (cg1.getOutput().equals(cg2.getOutput())) return true;
		return cg1.getOutput().equals(cg2.getOutput());
	}


}
