package org.flowable;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class CallRestApi implements JavaDelegate {

	public void execute(DelegateExecution execution) {
		String var = (String) execution.getVariable("surname");
		execution.setVariable("surname", var.toUpperCase());
		execution.setVariable("name", "VALUEFROMJAVAAAA");
	}
	    public static void main(String[] args) {
        System.out.println("Hello World!");
    }

}
