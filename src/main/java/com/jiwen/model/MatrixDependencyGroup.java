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

import java.util.HashMap;
import java.util.Map;

/**
 * A cell of the Design Structure Matrix
 * 
 * @author humbertocervantes
 *
 */
public class MatrixDependencyGroup {
	
	// number that identifies the source of the dependency
	private MatrixElement source;
	
	// number that identifies the destination of the dependency
	private MatrixElement destination;
		
	// A hashmap that contains dependencies the string is the type the integer is the number of occurrences
	private Map <String, Integer> dependencies = new HashMap <String, Integer>();
	
	/**
	 * Consctructor
	 * 
	 * @param source the source element
	 * @param destination the destination element
	 */
	public MatrixDependencyGroup(MatrixElement source, MatrixElement destination) {
		this.source = source;
		this.destination = destination;
	}
	
	/**
	 * Add a new dependency. In case this type already existed, the occurrences are incremented
	 * 
	 * @param type A string that describes the type of dependency
	 * @param value An integer that represents the number of occurrences
	 */
	public void addDependency(String type, int occurrences) {
		int oldOccurrences = 0;
		
		if(dependencies.containsKey(type)) {
			oldOccurrences = dependencies.get(type);
			dependencies.remove(type);
		}

		dependencies.put(type, occurrences+oldOccurrences);
	}
	
	/**
	 * Returns the index of the source of the dependency
	 * 
	 * @return an integer with the index
	 */
	public MatrixElement getSource() {
		return source;
	}

	/**
	 * Returns the index of the destination of the dependency
	 * 
	 * @return an integer with the index
	 */
	public MatrixElement getDestination() {
		return destination;
	}
	
	/**
	 * Returns the total number of dependencies
	 * 
	 * @return an integer with the total number of dependencies
	 */
	public int getTotalDependencies() {
		int total = 0;
				
		for (int a:dependencies.values()) {
			total += a;
		}
		return total;
	}
	
	/**
	 * Returns the map of dependencies <type, occurrences>
	 * 
	 * @return the map of dependencies
	 */
	public Map <String,Integer> getDependencies() {
		return dependencies;
	}
	

}
