package io.jenkins.plugins.sample;

import hudson.model.Action;
import hudson.model.Run;
import jenkins.model.RunAction2;

public class OutputAction implements RunAction2 {
	
	private String output;
	private transient Run run;
	
	public OutputAction(String output) {
		this.output = output;
	}

	public String getOutput() {
		return output;
	}
	
	@Override
	public String getDisplayName() {
		return "Test Comparison";
	}

	@Override
	public String getIconFileName() {
		return "document.png";
	}

	@Override
	public String getUrlName() {
		return "TestComparison";
	}

	@Override
	public void onAttached(Run<?, ?> run) {
		this.run = run;
	}

	@Override
	public void onLoad(Run<?, ?> run) {
		this.run = run;
	}
	
	public Run getRun() {
		return run; 
	}

}
