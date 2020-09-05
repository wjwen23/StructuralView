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
 * An element of the design structure matrix which is a source of dependencies
 * 
 * @author humbertocervantes
 *
 */
public class MatrixElement {
	
	// Name of the element
	private String name;
	private int groupid;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	private int index;
	
	// Dependencies for which this element is source
	private ArrayList <MatrixDependencyGroup> dependencies = new ArrayList <MatrixDependencyGroup>();
			
	// The group this belongs to (in case it does)
	private MatrixElementGroup group;
	
	/**
	 * Constructor
	 * 
	 * @param name the name of the element
	 */
	public MatrixElement(String name) {
		this.name = name;		
	}

	public MatrixElement(String name, int index) {
		this.name = name;
		this.index = index;
	}

	
	/**
	 * Get the name of the element
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * Setter to change the name of the element
	 * 
	 * @param newName the new name for the element
	 * @return
	 */
	public boolean setName(String newName) {
		name = newName;
		return true;
	}
	
	/**
	 * Add a dependency for this element
	 * 
	 * @param dependency where the source must be this element
	 * @return true if added successfully, false if this element is not the source
	 */
	public boolean addDependency(MatrixDependencyGroup dependency) {
		
		if(dependency.getSource() != this) {
			System.out.println("wrong addition");
			return false;
		}
		
		//System.out.println("adding dependency from "+getName()+ "to "+dependency.getDestination().getName());
		return dependencies.add(dependency);
	}
	
	/**
	 * Retrieve this element's dependencies
	 * 
	 * @return an arraylist with the dependencies
	 */
	public ArrayList <MatrixDependencyGroup> getDependencies() {
		return dependencies;
	}

	/**
	 * Sets the group of the element. This is normally called by DesignStructureMatrixElementGroup when
	 * the element is added to a group.
	 * 
	 * @param group
	 */
	void setGroup(MatrixElementGroup group) {
		this.group = group;
	}
	
	/**
	 * Get the group this element belongs to
	 * 
	 * @return
	 */
	public MatrixElementGroup getGroup() {
		return group;
	}
	
	public String toString() {
		return getName();
	}


    public int getGroupid() {
        return groupid;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }
}
