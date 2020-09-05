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

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;

/**
 * The table model for the DSM
 * 
 * @author humbertocervantes
 *
 */
public class DSMTableModel  implements TableModel {
	
	private DesignStructureMatrixRepresentation dsm;
	private ArrayList <TableModelListener> listeners = new ArrayList <TableModelListener>();
	
	public DSMTableModel(DesignStructureMatrixRepresentation dsm) 
	{
		this.dsm = dsm;
	}
	
	public int getRowCount() {
		return dsm.getElementsCount();
	}
	
	public Object getValueAt(int row, int column) {
		//System.out.println("Getting row "+ row+" col "+column);
		//int value = dsm.getDependencies(row, column).getTotalDependencies();
		return dsm.getDependencyGroup(row,column);
	}

	@Override
	public int getColumnCount() {
		return dsm.getElementsCount();
	}

	@Override
	public String getColumnName(int columnIndex) {
		// TODO Auto-generated method stub
		return dsm.getElement(columnIndex).getName();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return dsm.getElement(columnIndex).getName().getClass();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		listeners.add(l);
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		listeners.remove(l);
	}
	
}
