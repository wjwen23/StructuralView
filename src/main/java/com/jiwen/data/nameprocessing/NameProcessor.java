/*
MIT License

Copyright (c) 2018-2019 Humberto Cervantes

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.jiwen.data.nameprocessing;

import com.jiwen.model.MatrixElement;

import java.util.ArrayList;

/**
 * Interface form modules that perform processing on the names of MatrixElements
 * 
 * @author humbertocervantes
 *
 */
public abstract class NameProcessor {
	private NameProcessor next = null;
	
	public NameProcessor() {
	}
	
	/**
	 * Successor for the name processor, to create chains of processing
	 * 
	 * @param next
	 */
	public NameProcessor(NameProcessor next) {
		this.next = next;
	}
	
	/**
	 * Process the names
	 * 
	 * @param elements the elements of the matrix
	 * @return an array with the elements with their names processed
	 */
	public ArrayList <MatrixElement> processNames(ArrayList <MatrixElement> elements) {
		ArrayList <MatrixElement> processedNames = performProcessing(elements);
		if(next != null) {
			next.processNames(processedNames);
		}
		
		return processedNames;
		
	}
	
	/**
	 * This abstract method is where the processing takes place. It must be implemented by
	 * specific processors
	 * 
	 * @param elements  elements the elements of the matrix
	 * @return
	 */
	abstract protected ArrayList <MatrixElement> performProcessing(ArrayList <MatrixElement> elements);

}
