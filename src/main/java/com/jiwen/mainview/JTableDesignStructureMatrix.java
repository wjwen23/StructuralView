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

import com.jiwen.model.MatrixDependencyGroup;
import com.jiwen.model.MatrixElement;
import com.jiwen.model.MatrixElementGroup;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Component that renders the matrix using a JTable
 * 
 * @author humbertocervantes
 *
 */
public class JTableDesignStructureMatrix {
	
	// Size of row that varies according to zoom
	private int rowSize;
	
	// Size of column that varies according to zoom
	private int columnSize;
	
	// Currently selected row
	private int selectedRow = -1;

	// Currently selected column
	private int selectedColumn = -1;
	
	// The main JTable for the matrix
	private JTable matrixTable;
	
	// The row header is created in another JTable
	private JTable rowHeaderColumnTable;

	private TableColumnModel headerModel;
	private ListModel<MatrixElement> rowHeaderModel;
	private TableModel matrixModel;
	
	// The scroll pane that contains the matrix
	private JScrollPane pane;
	private JViewport viewport;
	
	// A map of element groups to colors
	private Map <MatrixElementGroup, Color> colorMap;

	
	/**
	 * Constructor for JDesignStructureMatrix
	 * 
	 * @param headerModel model for the header
	 * @param rowHeaderModel model for the row header
	 * @param matrixModel model for the matrix
	 */
	public JTableDesignStructureMatrix(TableColumnModel headerModel, ListModel<MatrixElement> rowHeaderModel, TableModel matrixModel, int rowSize, int columnSize, Map <MatrixElementGroup, Color> colorMap) {
		
		// The color map and row and column sizes are received externally because
		// A new instance of this component is created each time the representation changes
		// But we want to maintain the colors and zoom level
		
		this.colorMap = colorMap;		
		this.rowSize = rowSize;
		this.columnSize = columnSize;
				
		/*
		 * TableCellRenderer used to render the header
		 * 
		 */
		TableCellRenderer headerRenderer = new TableCellRenderer() {

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				
				JLabel cell = new JLabel(Integer.toString(column+1), SwingConstants.CENTER);
			    cell.setFont(getFont());
			    cell.setOpaque(true);
			    cell.setBackground(Color.LIGHT_GRAY);
			    cell.setMinimumSize(new Dimension(columnSize,rowSize));
			    cell.setPreferredSize(new Dimension(columnSize,rowSize));
			    
			    // We need to explicitly add a border for the header
			    cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));

				return cell;
			}};
		
		/*
		 * TableCellRenderer used to render the header
		 * 
		 */
		TableCellRenderer rowHeaderRenderer = new TableCellRenderer() {

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				
				JLabel cell = new JLabel(Integer.toString(row+1), SwingConstants.CENTER);
			    cell.setFont(getFont());
			    cell.setOpaque(true);
			    cell.setBackground(Color.LIGHT_GRAY);
			    cell.setMinimumSize(new Dimension(columnSize,rowSize));
			    cell.setPreferredSize(new Dimension(columnSize,rowSize));

				return cell;
			}};

		
		/**
		 * Renderer for the cells
		 * 
		 */
		TableCellRenderer matrixCellRenderer = new TableCellRenderer() {

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				
				
				if(value == null) {
					return null;
				}

				MatrixDependencyGroup dependencyGroup = (MatrixDependencyGroup)value;
				
				JLabel cell;
				
				int total = dependencyGroup.getTotalDependencies();
				
				if (total > 0) {
					// If there are more than 0 dependencies, show a number
					cell = new JLabel(Integer.toString(total), SwingConstants.CENTER);
				} else {
					cell = new JLabel("");
				}
				
				if(row == selectedRow || column == selectedColumn) {
					// Selected element
					cell.setBackground(Color.YELLOW);
				} else 	if(row == column) {
					// Diagonal in the matrix
					cell.setBackground(Color.LIGHT_GRAY);
				} else {
					MatrixElementGroup sourceGroup = dependencyGroup.getSource().getGroup();
					MatrixElementGroup destinationGroup = dependencyGroup.getDestination().getGroup();
					
					if(sourceGroup != null) {
						// We will color this cell using the color assigned to the last common ancestro group
						MatrixElementGroup commonGroup = sourceGroup.findLastCommonAncestor(destinationGroup);					
						
						/*
						 // This code can be used to color the matrix using grey hues
						int greyLevel = 255-(25*commonGroup.getDepth());
						Color col = new Color(greyLevel, greyLevel, greyLevel);
						cell.setBackground(col);
						*/
						
						cell.setBackground(colorMap.get(commonGroup));
						
					}
				}	
				
			    cell.setFont(getFont());
				cell.setOpaque(true);	

				return cell;
			}};	
		
		
		// We create the values for the column header
		Object rowHeaderTableValues [][] = new Object [matrixModel.getColumnCount()][1];
		Object columnNames[] = {""};
	      
		
		for(int i = 0; i < matrixModel.getColumnCount() ; i++) {
			// The values are filled with numbers
			// we could use other values using the headerModel
	    	rowHeaderTableValues[i][0] = rowHeaderModel.getElementAt(i); //(Integer.toString(i+1));
		}

		// the main table
		matrixTable = new JTable(matrixModel);
	      
	    // RowHeader table	      
		rowHeaderColumnTable = new JTable(rowHeaderTableValues, columnNames);

	    // Creates default columns for the table from the data model using the getColumnCount method defined in the TableModel interface.
	    // Clears any existing columns before creating the new columns based on information from the model.
	    matrixTable.createDefaultColumnsFromModel();
	    rowHeaderColumnTable.createDefaultColumnsFromModel();
	    
		// Set the renderers
		matrixTable.setDefaultRenderer(Object.class, matrixCellRenderer);
		matrixTable.getTableHeader().setDefaultRenderer(headerRenderer);
		rowHeaderColumnTable.setDefaultRenderer(Object.class, rowHeaderRenderer);
		
		// Share selection model between the two tables
	    matrixTable.setSelectionModel(rowHeaderColumnTable.getSelectionModel());
	    matrixTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);


	    // Rendering of the rowHeaderColumnTable
	    rowHeaderColumnTable.setBackground(Color.lightGray);
	    rowHeaderColumnTable.setColumnSelectionAllowed(false);
	    
	    // Grids
		matrixTable.setShowGrid(true);
		matrixTable.setGridColor(Color.BLACK);
	    rowHeaderColumnTable.setShowGrid(true);
	    rowHeaderColumnTable.setGridColor(Color.BLACK);

	    viewport = new JViewport();
	    viewport.setView(rowHeaderColumnTable);	    
	    viewport.setPreferredSize(rowHeaderColumnTable.getPreferredSize());


	    pane = new JScrollPane(matrixTable);
	    pane.setRowHeader(viewport);
	    pane.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, rowHeaderColumnTable.getTableHeader());
		
		
		this.headerModel = headerModel;
		this.rowHeaderModel = rowHeaderModel;
		this.matrixModel = matrixModel;

		
        // Add a mouse listener to the matrix table to show up popups
		matrixTable.addMouseListener(new MouseAdapter() {  
            public void mouseClicked(MouseEvent e) {
        		JPopupMenu popupMenu = new JPopupMenu();
        		
        		// Find current row and column
        		int popupCol = matrixTable.columnAtPoint(new Point(e.getX(),e.getY()));
        		int popupRow = matrixTable.rowAtPoint(new Point(e.getX(),e.getY()));
        		        		
        		MatrixDependencyGroup dependencies = (MatrixDependencyGroup) matrixModel.getValueAt(popupRow, popupCol);
        		        		
        		if(dependencies!=null) {
	        		// Create the label for the popup
	        		String label="<html>";
	        		        		
	        		//label += "row,col=("+popupRow+","+popupCol+")<br/>";
	        		if(dependencies.getSource().getGroup() != null && dependencies.getDestination().getGroup() != null) {
	        			MatrixElementGroup lastCommonAncestor = dependencies.getSource().getGroup().findLastCommonAncestor(dependencies.getDestination().getGroup());
	        			label += "group = "+lastCommonAncestor+"<br/>";
	        		}
	        		
	        		label += "source ="+dependencies.getSource().getName()+"<br/>";
	        		label += "destination ="+dependencies.getDestination().getName()+"<br/>";
	        		
	        		// Create the details for the dependencies
	        		Set<String> dependencyTypes = dependencies.getDependencies().keySet();
	
	        		for(String currentType:dependencyTypes) {
	        			int amount = dependencies.getDependencies().get(currentType);
	        			label += amount+" x "+currentType+"<br/>";
	        		}
	        		
	        		label += "</html>";
	        		popupMenu.add(new JLabel(label));
	            	popupMenu.show(matrixTable , e.getX(), e.getY());  
        		}
            }                 
         }); 
		
		createColorMap();
	    resizeComponents();

	}
	
	/**
	 * The color map
	 * 
	 */
	private void createColorMap() {
		
		Random rand = new Random();

		
		int rows = rowHeaderModel.getSize();
		for (int i = 0; i < rows ; i++) {
			MatrixElementGroup group = ((MatrixElement)rowHeaderModel.getElementAt(i)).getGroup();
			if(group != null) {
				if(!colorMap.containsKey(group)) {
					// The color map is created using random colors
					colorMap.put(group, new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));
				} 
			}			
		}
	}
	
	/**
	 * This method is called initially and each time the zoom command is issued
	 * 
	 */
	private  void resizeComponents() {
		
	    matrixTable.setFont(getFont());
	    rowHeaderColumnTable.setFont(getFont());

	    rowHeaderColumnTable.getColumnModel().getColumn(0).setMinWidth(columnSize);
	    rowHeaderColumnTable.getColumnModel().getColumn(0).setMaxWidth(columnSize);
	    rowHeaderColumnTable.getColumnModel().getColumn(0).setPreferredWidth(columnSize);
	    
		int columnCount = matrixTable.getColumnModel().getColumnCount();
		for(int i=0;i< columnCount;i++) {
			matrixTable.getColumnModel().getColumn(i).setMinWidth(columnSize);
			matrixTable.getColumnModel().getColumn(i).setMaxWidth(columnSize);
			matrixTable.getColumnModel().getColumn(i).setPreferredWidth(columnSize);
			
			matrixTable.setRowHeight(rowSize);
			rowHeaderColumnTable.setRowHeight(rowSize);
		}

	    viewport.setPreferredSize(new Dimension(columnSize,viewport.getPreferredSize().height));
	    		
	    rowHeaderColumnTable.repaint();
		matrixTable.repaint();
	}
	
	/**
	 * Get the font that is used in the matrix
	 * 
	 * @return the font
	 */
	private Font getFont() {
		return new Font("Arial", Font.BOLD, rowSize/3);
	}
	
	/**
	 * Returns a JComponent to integrate this matrix into a swing container
	 * 
	 * @return a JComponent
	 */
	public JComponent getJComponent() {
		return pane;
	}

	/**
	 * Method used to change the currently selecter row and column
	 * 
	 * @param selectedRow
	 * @param selectedColumn
	 */
	public void highlightElement(int selectedRow, int selectedColumn) {
		this.selectedRow = selectedRow;
		this.selectedColumn = selectedColumn;
		matrixTable.repaint();
	}

	/**
	 * Method used to change the size of row and column, this is used for zooming
	 * 
	 * @param rowSize
	 * @param columnSize
	 */
	public void changeRowAndColumnSize(int rowSize, int columnSize) { 
		this.columnSize  = rowSize;
		this.rowSize = columnSize;
		
	    resizeComponents();
	}
	
}
