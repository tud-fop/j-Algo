package org.jalgo.module.lambda.controller;

import java.util.Observer;

import org.jalgo.module.lambda.model.*;

public interface IParseUnit {
	
	ITermHandler parseString(String s, Observer o) throws LambdaException;
	
	Term parseString(String s) throws LambdaException;
	
}
