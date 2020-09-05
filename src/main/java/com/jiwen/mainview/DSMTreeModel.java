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
package com.jiwen.mainview;

import com.jiwen.model.DesignStructureMatrixRepresentation;
import com.jiwen.model.MatrixElementGroup;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class DSMTreeModel implements TreeModel {
	
	private DesignStructureMatrixRepresentation dsm;

	public DSMTreeModel(DesignStructureMatrixRepresentation dsm) {
		this.dsm = dsm;
	}

	@Override
	public Object getRoot() {
		return dsm;
	}

	@Override
	public Object getChild(Object parent, int index) {
		if(parent instanceof DesignStructureMatrixRepresentation) {
			return dsm.getElement(index);
		} 
		return ((MatrixElementGroup)parent).getChildren().get(index);
	}

	@Override
	public int getChildCount(Object parent) {
		if(parent instanceof DesignStructureMatrixRepresentation) {
			return dsm.getElementsCount();
		} 
		return ((MatrixElementGroup)parent).getChildren().size();

	}

	@Override
	public boolean isLeaf(Object node) {
		if((node instanceof MatrixElementGroup == false) && (node instanceof DesignStructureMatrixRepresentation == false))
			return true;
		else
			return false;
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		// TODO Auto-generated method stub

	}

}
