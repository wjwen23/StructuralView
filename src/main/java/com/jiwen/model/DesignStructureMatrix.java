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
package com.jiwen.model;

import com.jiwen.data.nameprocessing.NameProcessor;

import java.util.ArrayList;

/**
 * 
 * Class that represents a DesignStructureMatrix
 * 
 * @author humbertocervantes
 *
 */
public class DesignStructureMatrix {

	private String name;
	
	// The element names
	private ArrayList <MatrixElement> elements = new ArrayList<MatrixElement>();
	
	/**
	 * 
	 * Create a DesignStructureMatrix with an array of element names and an array of 
	 * DesignStructureMatrixCells which contain information about the dependencies
	 * 
	 * @param elements an array with the names of the elements
	 * @param dependencies a square array with DesignStructureMatrixCells which contain the details of the dependencies
	 */
	public DesignStructureMatrix(String name, ArrayList <MatrixElement> elements) {
		
		this.name = name;		
		this.elements = elements;

	}
	
	/**
	 * 
	 * Returns the name of an element
	 * 
	 * @param row row of the element (starting with 0)
	 * @return the name of the element
	 */
	public MatrixElement getElement(int row) {
		return elements.get(row);
	}
		
	/**
	 * Returns the number of elements in the matrix
	 * 
	 * @return an integer with the number of elements
	 */
	public int getElementsCount() {
		return elements.size();
	}
	
	
	/**
	 * Rename the elements using a chain of name processors
	 * 
	 * @param processor the first processor in the chain
	 */
	public void renameElements(NameProcessor processor) {
		processor.processNames(elements);
	}
	
	/**
	 * 
	 * @param element
	 * @return
	 */
	public int getIndexOfElement(MatrixElement element) {
		return elements.indexOf(element);
	}

	
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	public String toString() {
		return name;
	}
	
}
