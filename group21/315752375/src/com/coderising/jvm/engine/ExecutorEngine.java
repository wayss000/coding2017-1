package com.coderising.jvm.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.coderising.jvm.attr.CodeAttr;
import com.coderising.jvm.clz.ClassFile;
import com.coderising.jvm.cmd.ByteCodeCommand;
import com.coderising.jvm.constant.MethodRefInfo;
import com.coderising.jvm.method.Method;

public class ExecutorEngine {

	private Stack<StackFrame> stack = new Stack<StackFrame>();
	
	public ExecutorEngine() {
		
	}
	
	public void execute(Method mainMethod){
		StackFrame mainFrame=StackFrame.create(mainMethod);
		stack.push(mainFrame);
		
		while(!stack.isEmpty()){
			StackFrame frame=stack.peek();
			ExecutionResult result=frame.execute();
			if(result.isPauseAndRunNewFrame()){
				Method nextMethod=result.getNextMethod();
				StackFrame nextFrame =StackFrame.create(nextMethod);
				setupFunctionCallParams(frame, nextFrame);
				stack.push(nextFrame);
			}else {
				stack.pop();
			}
		}
		
		
	}
	
	
	
	private void setupFunctionCallParams(StackFrame currentFrame,StackFrame nextFrame) {
		Method nextMethod=nextFrame.getMethod();
		List<String>paramList=nextMethod.getParameterList();
		
		int paramNum=paramList.size()+1;
		List<JavaObject>values=new ArrayList<>();
		 while(paramNum>0){
			 values.add(currentFrame.getOprandStack().pop());
			 paramNum--;
		 }
		 List<JavaObject> params=new ArrayList<>();
		  for(int i=values.size()-1;i>=0;i--){
			  params.add(values.get(i));
		  }
		nextFrame.setLocalVariableTable(params);
		
	}
	
}
