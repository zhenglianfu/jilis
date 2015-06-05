package fe.test;

import java.io.IOException;

import fe.toolkit.Main;

public class TestRefactorHTML {
	
	public static void main(String[] args) throws IOException{
		Main process = new Main();
		process = process.readProperties();
		System.out.println(process.scannerHtml()); 
		process.printResource();
		process.refactorHtml();
	}
}
