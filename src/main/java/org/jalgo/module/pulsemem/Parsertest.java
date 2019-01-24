package org.jalgo.module.pulsemem;


import o3b.antlr.runtime.RecognitionException;
import org.jalgo.module.pulsemem.core.CodeGenerator;
import org.jalgo.module.pulsemem.core.PulsMemLine;
import org.jalgo.module.pulsemem.core.bufferedIOSimulation;
import org.jalgo.module.pulsemem.core.myInterpreter;
import c00.AST;

/**
 * This class is for testing purpose only
 *
 * @author Joachim Protze
 */
public class Parsertest {

	public static void main(String[] args) {
		/*		CodeGenerator i = new CodeGenerator();
		try {
			AST.Program prog = (AST.Program)AST.parseFile("/home/jooschi/testdir/example.c", "Program");
			i.SwitchProgram(prog);
			System.out.print(i.getOutput());
		} catch (RecognitionException e) {
			e.printStackTrace();
		}
*/
		CodeGenerator cg = new CodeGenerator();
		myInterpreter i = new myInterpreter();

		bufferedIOSimulation bios = new bufferedIOSimulation();
		bios.addInputBuffer(1);
		bios.addInputBuffer(2);
		bios.addInputBuffer(3);
		bios.addInputBuffer(1);
		bios.addInputBuffer(2);
		bios.addInputBuffer(3);

		i.setIOSimulation(bios);
		AST.Program prog=null;
		try {
			prog = (AST.Program)AST.parseFile("/home/jooschi/swt-praktikum/cvs-resp/j-Algo/examples/pulsmem/dyn02.c", "Program");
		} catch (RecognitionException e) {
			e.printStackTrace();
		}
		cg.SwitchProgram(prog);
		try {
			prog = (AST.Program)AST.parseString(cg.getOutput(), "Program");
		} catch (RecognitionException e) {
			e.printStackTrace();
		}
		cg = new CodeGenerator();
		cg.SwitchProgram(prog);
		System.out.print(cg.getNumberedOutputWithRM());
//		System.out.print(cg.getOutput());
		i.runInterpreter(prog);
		for (PulsMemLine pml : i.getPm()){
//			if (cg.getBreakpoints().contains(pml.getCodeLine()))
				System.out.print(pml.toString());
		}
		for (String integ : bios.getOutputBuffer()){
			System.out.println("printf out: "+integ);
		}
		bios.clearOutput();
		i.runInterpreter(prog);
		for (PulsMemLine pml : i.getPm()){
//			if (cg.getBreakpoints().contains(pml.getCodeLine()))
				System.out.print(pml.toString());
		}
		for (String integ : bios.getOutputBuffer()){
			System.out.println("printf out: "+integ);
		}
		bios.clearOutput();
		i.runInterpreter(prog);
		for (PulsMemLine pml : i.getPm()){
//			if (cg.getBreakpoints().contains(pml.getCodeLine()))
				System.out.print(pml.toString());
		}
		for (String integ : bios.getOutputBuffer()){
			System.out.println("printf out: "+integ);
		}
	}
}
