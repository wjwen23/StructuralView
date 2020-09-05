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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class supports the creation of a representation of a DSM
 * A representation is a "view" of the DSM which only shows certain
 * elements of the DSM. Some of the elements can be groups or they
 * can be the actual elements of the DSM
 * 
 * @author humbertocervantes
 *
 */
public class DesignStructureMatrixRepresentation {
	
	// THe original matrix
	private DesignStructureMatrix dsm;
	
	// The element names
	private ArrayList <MatrixElement> elements;
	
	// The cells of the matrix
	private MatrixDependencyGroup dependencies [][];

	
	
	/**
	 * 
	 * Create a represe
	 * 
	 * @param source the original matrix
	 * @param elements the elements that will be part of this representation
	 */
	public DesignStructureMatrixRepresentation(DesignStructureMatrix source, ArrayList <MatrixElement> elements) {
		this.dsm = source;
		this.elements = elements;
				
		recalculateDependencies();	
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
	 * Returns a DesignStructureMatrixCell for the row and column
	 * 
	 * @param row the row of the cell (starts with 0)
	 * @param column the column of the cell (starts with 0)
	 * @return a DesignStructureMatrixCell
	 */
	public MatrixDependencyGroup getDependencyGroup(int row, int column) {
		return dependencies[row][column];
	}


	/**
	 * Updates the dependency matrix
	 * 
	 */
	private void recalculateDependencies() {
		
		dependencies = new MatrixDependencyGroup[elements.size()][elements.size()];
		
		int row = 0;
		
		// Fill the matrix
		for(MatrixElement e:elements) { // row
			
			ArrayList <MatrixDependencyGroup> elementDependencies = getDependencies(e);
						
			for(MatrixDependencyGroup originalDependencies:elementDependencies) { // columns
							
				int column = elements.indexOf(originalDependencies.getDestination());

				dependencies [row][column] = originalDependencies;				
			}
			for (int column = 0; column <elements.size(); column++) {
				if(dependencies [row][column] == null) {
					MatrixDependencyGroup dependencyGroup = new MatrixDependencyGroup(e, elements.get(column));
					dependencies [row][column] = dependencyGroup;
				}
			}
			
			row++;
		}				
	}


	
	/**
	 * Retrieve this element's dependencies
	 * 
	 * @return an arraylist with the dependencies
	 */
	 private ArrayList <MatrixDependencyGroup> getDependencies(MatrixElement element) {
		 
		// We need a map as a destination may occur in more than one dependency, to aggregate numbers
		Map <MatrixElement, MatrixDependencyGroup> depsMap = new HashMap <MatrixElement, MatrixDependencyGroup>();
		
		if(!(element instanceof MatrixElementGroup)) {
			// The element is not a group

			ArrayList <MatrixDependencyGroup> elementDependencies = element.getDependencies();
			
			for(MatrixDependencyGroup currentDependencyGroup:elementDependencies) { // columns
				
				MatrixElement newDestination = currentDependencyGroup.getDestination();
				if (elements.contains(newDestination)) {
					// This occurs when a leaf element points towards another one
					depsMap.put(newDestination, currentDependencyGroup);

				} else {
					// The dependency points to a group, it is necessary to find its destination
					newDestination = recalculateDestination(currentDependencyGroup.getDestination().getGroup());
					
					// Check if there was already a dependency to this destination in the cache
					MatrixDependencyGroup newDependencies = depsMap.get(newDestination);
					if(newDependencies == null) {
						// Create the dependency and put it in the cache
						newDependencies = new MatrixDependencyGroup(element, newDestination);
						depsMap.put(newDestination, newDependencies);
					} 
					
					// Copy the original dependencies or increment the existing dependencies
					// addDependency does not replace existing occurrences if the type has
					// been added previously
					for(String type:currentDependencyGroup.getDependencies().keySet()) {
						newDependencies.addDependency(type, currentDependencyGroup.getDependencies().get(type));
					}				
				} 
			}
			
		} else { 
			
			// This is a group
			MatrixElementGroup group = (MatrixElementGroup) element;
			
			// All the children of this group will be grouped under the same source, which is the group
			for(MatrixElement child:group.getChildren()) {
							
				ArrayList <MatrixDependencyGroup> elementDependencies = getDependencies(child);
				
				
				for(MatrixDependencyGroup currentDependencyGroup:elementDependencies) {
					// This is the destination, but we have to see if it is currently in the matrix
					
					MatrixElement newDestination;
					if(elements.contains(currentDependencyGroup.getDestination())) {
						newDestination = currentDependencyGroup.getDestination();
					} else {
						newDestination = recalculateDestination(currentDependencyGroup.getDestination().getGroup());
					}
					
					// Add to the map cache
					MatrixDependencyGroup newDependencies = depsMap.get(newDestination);
					if(newDependencies == null) {
						newDependencies = new MatrixDependencyGroup(group, newDestination);
						depsMap.put(newDestination, newDependencies);
					} 
					
					// Copy the original dependencies or increment the existing dependencies
					// addDependency does not replace existing occurrences if the type has
					// been added previously
					for(String type:currentDependencyGroup.getDependencies().keySet()) {
						newDependencies.addDependency(type, currentDependencyGroup.getDependencies().get(type));
					}				
				}
			}
		}
		
		return new ArrayList <MatrixDependencyGroup>(depsMap.values());

	}
	
	/**
	 * Recalculate a destination for an element which is not in the matrix
	 * 
	 * @param element original element
	 * @return new destination
	 */
	private MatrixElement recalculateDestination(MatrixElement element) {
		
		// Check first if the element already exists in the matrix
		if(elements.contains(element)) {
			return element;
		} else if(element.getGroup() != null)
		{
			return recalculateDestination(element.getGroup());
		}
		
		// This should never occur
		return null;
	}
	
	/**
	 * 
	 * @param element
	 * @return
	 */
	public boolean containsElement(MatrixElement element) {
		return elements.contains(element);
	}

	/**
	 * 
	 * @param element
	 * @return
	 */
	public int getIndexOfElement(MatrixElement element) {
		return elements.indexOf(element);
	}
	
	public DesignStructureMatrix getDesignStructureMatrix() {
		return dsm;
	}
	
	public String toString() 
	{
		return dsm.getName();
	}
	
}
