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
package com.jiwen.dsmservice;

import com.jiwen.data.dsmloader.DesignStructureMatrixLoader;
import com.jiwen.data.nameprocessing.NameProcessor;
import com.jiwen.data.nameprocessing.PrefixRemovalNameProcessor;
import com.jiwen.model.DesignStructureMatrix;
import com.jiwen.model.DesignStructureMatrixRepresentation;
import com.jiwen.model.MatrixElement;
import com.jiwen.model.MatrixElementGroup;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * 
 * Service that handles business logic associated with DSMs
 * 
 * @author humbertocervantes
 *
 */
public class DesignStructureMatrixService {
	private DesignStructureMatrixLoader loader;
	
	/**
	 * Constructor for DesignStructureMatrixService
	 * 
	 * @param loader the loader that will be used by this service
	 */
	public DesignStructureMatrixService(DesignStructureMatrixLoader loader) {
		this.loader = loader;
	}

	/**
	 * Loads a matrix from a JSON file
	 * 
	 * @param filename
	 * @return
	 */
	public DesignStructureMatrix loadMatrixFromJSON(String filename) {
		return loader.loadFromJSON(filename);		
	}
	
	/**
	 * 
	 * 
	 * @param source
	 * @return
	 */
	public DesignStructureMatrix formatNames(DesignStructureMatrix source) {
		
		NameProcessor processor = new PrefixRemovalNameProcessor("..");
		source.renameElements(processor);
		
		return source;		
	}
	
	/**
	 * Create a DSM that groups the elements hierarchically by namespace
	 * 
	 * @param source the original DSM
	 * @param separator the character that separates names
	 * @param concatenate true if groupings with only one child should be concatenated
	 * @return the new DSM
	 */
	public DesignStructureMatrixRepresentation createNamespaceClustering(DesignStructureMatrix source, char separator, boolean concatenate) {
		
		ArrayList <MatrixElement> elements = new ArrayList <MatrixElement>();
						
		int elementCount = source.getElementsCount();
		
		for(int i = 0; i < elementCount ; i++ ) {
			MatrixElement element = source.getElement(i);
			
			// Creates a tokenizer that will separate based on package name
			StringTokenizer tokenizer = new StringTokenizer(element.getName(),Character.toString(separator));
			
			
			if(tokenizer.hasMoreTokens()) {
				String namespace = tokenizer.nextToken();
				//System.out.println(namespace);

				boolean found = false;
				
				// Check if a DesignStructureMatrixElementGroup has been created previously for this namespace
				for(MatrixElement currentElement:elements) {					
					if(currentElement.getName().equals(namespace)) {
						found = true;
						addSubElement(tokenizer, (MatrixElementGroup) currentElement, element);
					}
				}
				
				if(!found) {
					// Check if there are more tokens. In case there aren't this is a leaf element
					if(tokenizer.hasMoreTokens()) {
						MatrixElementGroup newElement = new MatrixElementGroup(namespace);
						elements.add(newElement);
						addSubElement(tokenizer,newElement, element);
					} else {	
						// Leaf element, we copy the original element into the new matrix
						element.setName(namespace);
						elements.add(element);

						// Leaf element, we copy the original element into the new matrix
						//DesignStructureMatrixElement newChild = new DesignStructureMatrixElement(namespace);
						//elements.add(newChild);

					}
				}
			}
		}
		
		if(concatenate = true) {			
			for(MatrixElement currentElement: elements) {
				if(currentElement instanceof MatrixElementGroup) {
					concatenate((MatrixElementGroup) currentElement, separator);
				}
			}
		}
		
		//////
		MatrixElementGroup rootElement = new MatrixElementGroup("root");
		
		for(MatrixElement e:elements) {
			rootElement.addChild(e);
		}

		 elements = new ArrayList <MatrixElement>();
		 elements.add(rootElement);
		 /////
		
		DesignStructureMatrixRepresentation newMatrix = new DesignStructureMatrixRepresentation(source,elements);
		
		return newMatrix;
	}
	
	
	/**
	 * Recursive function to create the hierarchy of DesignStructureMatrixElementGroup
	 * 
	 * @param tokenizer the tokenizer that is parsing the name
	 * @param parent the parent DesignStructureMatrixElementGroup
	 */
	private void addSubElement(StringTokenizer tokenizer, MatrixElementGroup parent, MatrixElement element) {

		ArrayList <MatrixElement> children = parent.getChildren();
		if(tokenizer.hasMoreTokens()) {
			String namespace = tokenizer.nextToken();
			
			// Check if the parent already has a node for this token
			for(MatrixElement child:children) {
				if(child.getName().equals(namespace)) {
					addSubElement(tokenizer, (MatrixElementGroup) child, element);
					return;
				}
			}
			// The parent does not have a node for this token, check if there are more
			// tokens. If not, this is a leaf element
			if(tokenizer.hasMoreTokens() == true) {
				MatrixElementGroup newChild = new MatrixElementGroup(namespace);
				parent.addChild(newChild);
				addSubElement(tokenizer,newChild, element);	
			} else {
				// Leaf element, we copy the original element into the new matrix
				//DesignStructureMatrixElement newChild = new DesignStructureMatrixElement(namespace);
				element.setName(namespace);
				parent.addChild(element);
			}
		}	
	}
	
	/**
	 * Method to concatenate names
	 * 
	 * @param group the parent group
	 * @param separator the character that separates the names
	 */
	private void concatenate(MatrixElementGroup group, char separator) {
		
		// If there is only one child and this child is a DesignStructureMatrixElementGroup we can concatenate
		if(group.getChildren().size()==1) {
			if(group.getChildren().get(0) instanceof MatrixElementGroup) {
				MatrixElementGroup child = (MatrixElementGroup) group.getChildren().get(0);
				group.setName(group.getName()+Character.toString(separator)+child.getName());
				
				// Remove the child of this group and replace them by the child's own childs
				group.removeChild(child);
				// We must first finish adding the children before making the recursive call
				for(MatrixElement childElement:child.getChildren()) {
					group.addChild(childElement);
				}
				for(int i=0;i<child.getChildren().size();i++) {
					concatenate(group, separator);
				}
				
			}
		} else if(group.getChildren().size()>1) {
			// If there are more children, we have to restart the process for each one of the children
			// if they are groupings
			for(MatrixElement childElement:group.getChildren()) {
				if(childElement instanceof MatrixElementGroup)
					concatenate((MatrixElementGroup)childElement, separator);
			}
		}
		
	}
	
	
	/**
	 * Create a new representation for the matrix
	 * 
	 * 
	 * @param source original DSM
	 * @param elements elements to be included in this representation
	 * @return a representation
	 */
	public DesignStructureMatrixRepresentation createRepresentation(DesignStructureMatrix source, ArrayList <MatrixElement> elements) {
		
		return new DesignStructureMatrixRepresentation(source, elements);
	}

}
