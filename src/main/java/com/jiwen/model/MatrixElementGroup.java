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

/**
 * This subclass of DesignStructureMatrixElement is to create virtual elements that serve
 * as grouping of other elements, to create groups or clusterings
 * 
 * @author humbertocervantes
 *
 */
public class MatrixElementGroup extends MatrixElement {
	
	/**
	 * The elements that are part of this group
	 * 
	 */
	private ArrayList <MatrixElement> children = new ArrayList <MatrixElement>();
	
	/**
	 * Constructor with the name of the group
	 * 
	 * @param name
	 */
	public MatrixElementGroup(String name) {
		super(name);
	}

	/**
	 * Add a child to the group
	 * 
	 * @param child
	 * @return true if added successfully
	 */
	public boolean addChild(MatrixElement child) {
		child.setGroup(this);
		return children.add(child);
	}
	
	/**
	 * Remove a child
	 * 
	 * @param child
	 * @return
	 */
	public boolean removeChild(MatrixElement child) {
		child.setGroup(null);
		return children.remove(child);
	}
	
	/**
	 * Returns the array of children
	 * 
	 * @return the children
	 */
	public ArrayList <MatrixElement> getChildren() {
		return children;
	}
	
	
	/**
	 * The dependencies for a group must be obtained through its representation
	 * 
	 * @return null
	 */
	public ArrayList <MatrixDependencyGroup> getDependencies() {
		return null;
	}
	
	
	/**
	 * Returns the depth level
	 * 
	 * @return
	 */
	public int getDepth() {
		if(getGroup() == null) {
			return 0;
		}
		return getGroup().getDepth()+1;
	}

	
	/**
	 * 
	 * 
	 * @param elementGroup
	 * @return
	 */
	public MatrixElementGroup findLastCommonAncestor(MatrixElementGroup elementGroup) {
		
		// The element is part of this group
		if(this  == elementGroup) {
			return this;
		}

		// The element is part of a subgroup of this group
		if(isIndirectChild(elementGroup)) {
			return this;
		}
		
		if(getGroup() != null) {
			return getGroup().findLastCommonAncestor(elementGroup);
		}
		
		return null;
	}
	
	private boolean isIndirectChild(MatrixElementGroup elementGroup) {
		if(this  == elementGroup) {
			return true;
		}

		for(MatrixElement e:children) {
			if(e instanceof MatrixElementGroup) {
				if(((MatrixElementGroup)e).isIndirectChild(elementGroup)) {
					return true;
				}
			}			
		}		
		return false;
	}
	
	
}
