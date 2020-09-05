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

import com.jiwen.dsmservice.DesignStructureMatrixService;
import com.jiwen.model.DesignStructureMatrix;
import com.jiwen.model.DesignStructureMatrixRepresentation;
import com.jiwen.model.MatrixElement;

import java.util.ArrayList;

/**
 * Controller for the main window.
 * 
 * @author humbertocervantes
 *
 */
public class MainWindowController {
	
	// The main view
	private MainWindow view;
	
	// the service
	DesignStructureMatrixService dsmService;
	
	// Current representation
	private DesignStructureMatrixRepresentation matrixRepresentation;
	
	int columnSize = 10;
	
	int rowSize = 10;
	
	/**
	 * Constructor 
	 * 
	 * @param view the view that is connected to the controller via IoC
	 * @param dsmService the DesignStructureMatrixService that is connected to the controller via IoC
	 */
	public MainWindowController (MainWindow view, DesignStructureMatrixService dsmService) {
		this.view = view;
		this.dsmService = dsmService;
	}
	
	/**
	 * retrieve the current representation
	 * 
	 * @return DesignStructureMatrixRepresentation for the currrent representation
	 */
	public DesignStructureMatrixRepresentation getDesignStructureMatrixRepresentation() {
		return matrixRepresentation;
	}

	
	/**
	 * Start the main use case
	 */
	public void start() {
		
		// We create an empty matrix initially
		ArrayList<MatrixElement> elements = new ArrayList<MatrixElement>();
		
		DesignStructureMatrix matrix = new DesignStructureMatrix("", elements);
		
		DesignStructureMatrixRepresentation matrixRepresentation = new DesignStructureMatrixRepresentation(matrix, elements);

		DSMColumnModel columnModel = new DSMColumnModel(matrixRepresentation);
		DSMListModel listModel = new DSMListModel(matrixRepresentation);
		DSMTableModel tableModel = new DSMTableModel(matrixRepresentation);
		DSMTreeModel treeModel = new DSMTreeModel(matrixRepresentation);

		view.setModels(columnModel, listModel, tableModel);
		view.setTreeModel(treeModel);

		view.initialize();
		
	}
	
	/**
	 * Controller method for the loadMatrix use case
	 * 
	 * @param filename the name of the file
	 */
	public void loadMatrix(String filename) {
		
		DesignStructureMatrix matrix = dsmService.loadMatrixFromJSON(filename);
		
		dsmService.formatNames(matrix);
		
		matrixRepresentation = dsmService.createNamespaceClustering(matrix,'.',true);
		
		System.out.println("Loaded file: "+filename+" with: "+matrix.getElementsCount()+ " elements");
		
		DSMColumnModel columnModel = new DSMColumnModel(matrixRepresentation);
		DSMListModel listModel = new DSMListModel(matrixRepresentation);
		DSMTableModel tableModel = new DSMTableModel(matrixRepresentation);
		DSMTreeModel treeModel = new DSMTreeModel(matrixRepresentation);

		view.setModels(columnModel, listModel, tableModel);
		view.setTreeModel(treeModel);
		
	}
	
	/**
	 * Controller method for the updateVisualization use case
	 * 
	 * @param originalMatrix
	 * @param nodes
	 */
	public void updateVisualization(DesignStructureMatrixRepresentation originalMatrix,ArrayList <MatrixElement> nodes) {
				
		matrixRepresentation = dsmService.createRepresentation(originalMatrix.getDesignStructureMatrix(), nodes);
				
		DSMColumnModel columnModel = new DSMColumnModel(matrixRepresentation);
		DSMListModel listModel = new DSMListModel(matrixRepresentation);
		DSMTableModel tableModel = new DSMTableModel(matrixRepresentation);

		view.setModels(columnModel, listModel, tableModel);
		
		// The tree model does no need to be updated as this is the same matrix
	}


}
