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
 * Removes a prefix from an arraylist of element names
 * 
 * @author humbertocervantes
 *
 */
public class PrefixRemovalNameProcessor extends NameProcessor {
	
	String prefix;
	
	/**
	 * Constructor that receives the prefix to remove
	 * 
	 * @param prefix
	 */
	public PrefixRemovalNameProcessor(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Removes the prefixes
	 * 
	 */
	protected ArrayList<MatrixElement> performProcessing(ArrayList<MatrixElement> elements) {
				
		for(MatrixElement element:elements) {
			if(element.getName().startsWith(prefix)) {
				String newName = element.getName().substring(prefix.length());
				element.setName(newName);
			}
		}		
		return elements;
		
	}

}
