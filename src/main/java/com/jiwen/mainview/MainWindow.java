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

import com.jiwen.model.MatrixElement;
import com.jiwen.model.MatrixElementGroup;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("serial")
public class MainWindow extends JFrame{

	private JTableDesignStructureMatrix jdsm;
	
	private JTree elementsTree = new JTree();
	
	private MainWindowController controller;
	
	private JSplitPane splitPane;

	// Array which contains the current leaves of the tree
	private ArrayList <MatrixElement> treeLeaves  = new ArrayList <MatrixElement>();
	
	// Size of rows in matrix
	private int rowSize = 20;
	
	// Size of columns in matrix
	private int columnSize = 20;	
	
	// Color map
	private Map <MatrixElementGroup, Color> colorMap  = new HashMap <MatrixElementGroup, Color>();
	
	/**
	 * Setter for the controller to support IoC
	 * 
	 * @param controller the controller
	 */
	public void setController(MainWindowController controller) {
		this.controller = controller;
		
	}

	/**
	 * Sets the models used by the matrix
	 * 
	 * @param headerModel model for the header
	 * @param rowHeaderModel model for the row header
	 * @param matrixModel model for the matrix
	 */
	public void setModels(TableColumnModel headerModel, ListModel<MatrixElement> rowHeaderModel, TableModel matrixModel) {

		// when the models are set, a new matrix is created, we first remove the old matrix
		// jdsm may be null the first time
		if(jdsm != null) {
			splitPane.remove(jdsm.getJComponent());
		}
			
		// create and add new matrix
		jdsm = new JTableDesignStructureMatrix(headerModel, rowHeaderModel, matrixModel, rowSize, columnSize, colorMap);
		
		if(splitPane!=null) {
			splitPane.add(jdsm.getJComponent(),1);
		}
	}
	
	/**
	 * Sets the tree model. This is separate of the other model settings because
	 * the representation of the matrix may change and we don't need to change
	 * the tree representation
	 * 
	 * @param treeModel the tree model
	 */
	public void setTreeModel(TreeModel treeModel) {
		elementsTree.setModel(treeModel);
		treeLeaves = updateTreeLeavesArray();
	}
	
	/**
	 * Initialize the swing components
	 * 
	 */
	public void initialize() 
	{
		//Create the JFrame and the various JPanels
		JPanel panelCanvas = new JPanel(new BorderLayout());
		JPanel panelOptions = new JPanel(new FlowLayout());

		//Create the components of the option bar
		JButton openFileButton = new JButton("Load DSM");
		JButton zoomInButton = new JButton("+");
		JButton zoomOutButton = new JButton("-");

		//Validate the JTextField data
		panelOptions.add(openFileButton);
		panelOptions.add(zoomInButton);
		panelOptions.add(zoomOutButton);

		setMinimumSize(new Dimension(800,600));	

		elementsTree.setDragEnabled(false);
		elementsTree.setRootVisible(true);
				
		//Add the components to the main frame
		splitPane=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,new JScrollPane(elementsTree),jdsm.getJComponent());
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(250);
		
		panelCanvas.add("North",panelOptions);
		panelCanvas.add("Center",splitPane);
		this.add(panelCanvas);

		// Listeners for the buttons
		zoomOutButton.addActionListener((ActionListener) new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(rowSize > 4) {
					rowSize -= 2;
					columnSize -= 2;
					jdsm.changeRowAndColumnSize(rowSize, columnSize);
				}
			}
		});

		zoomInButton.addActionListener((ActionListener) new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(rowSize < 30) {
					rowSize += 2;
					columnSize += 2;
					jdsm.changeRowAndColumnSize(rowSize, columnSize);					
				} 
			}
		});

		openFileButton.addActionListener((ActionListener) new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();

				
				int returnVal = fc.showOpenDialog(MainWindow.this);
				 
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = fc.getSelectedFile();
	                controller.loadMatrix(file.getAbsolutePath());
	            } 
			}
		});
		
		elementsTree.addTreeExpansionListener(new TreeExpansionListener() {

			@Override
			public void treeExpanded(TreeExpansionEvent event) {
				treeLeaves = updateTreeLeavesArray();
				controller.updateVisualization(controller.getDesignStructureMatrixRepresentation(), treeLeaves);
				elementsTree.repaint();
			}

			@Override
			public void treeCollapsed(TreeExpansionEvent event) {
				treeLeaves = updateTreeLeavesArray();
				controller.updateVisualization(controller.getDesignStructureMatrixRepresentation(), treeLeaves);
				elementsTree.repaint();
			}});

		
		elementsTree.setCellRenderer(new TreeCellRenderer() {

			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
					boolean leaf, int row, boolean hasFocus) {
					//System.out.println("getting renderer for "+value+" is leaf ="+leaf);
					
					JLabel jlabel;
				
					int index = treeLeaves.indexOf(value);
					String label = "<html>";
					if(selected) {
						label += "<b>";
					}
					if(index >= 0) {
						label += (index + 1)  + " - " + value.toString();
					} else {
						label += value.toString();
					}
					label += "</html>";
					jlabel = new JLabel(label);

					jlabel.setPreferredSize(new Dimension(label.length()*10,10));
					return jlabel;
			}});

		
		elementsTree.addMouseListener(new MouseAdapter() {
		     public void mousePressed(MouseEvent e) {
		    	 		    	 
		         int selRow = elementsTree.getRowForLocation(e.getX(), e.getY());
		         TreePath selPath = elementsTree.getPathForLocation(e.getX(), e.getY());
		         
		         if(selRow != -1) {
		        	 
		        	 elementsTree.setSelectionPath(selPath);

		             if(e.getButton() == MouseEvent.BUTTON1) {
		            	 
		            	// Left button
		            	 
		            	// We search the index of the highlighted element of the tree in the matrix representation
		         		int index = controller.getDesignStructureMatrixRepresentation().getIndexOfElement((MatrixElement)selPath.getLastPathComponent());
		        				         		
	        			jdsm.highlightElement(index,index);

		             }
		             if(e.getButton() == MouseEvent.BUTTON3) 
		             {
		            	 // Right button
			            
			        	 JPopupMenu popupMenu = new JPopupMenu();
			        	 JMenuItem expand = new JMenuItem("Expand");
			        	 JMenuItem contract = new JMenuItem("Contract");
			        	 popupMenu.add(new JLabel(selPath.getLastPathComponent().toString()));
			        	 popupMenu.add(expand);
			        	 
			        	 expand.addActionListener((ActionListener) new ActionListener() {
			        		 @Override
			        		 public void actionPerformed(ActionEvent e) {
			        			 long initialTime = System.currentTimeMillis();
			        			 expandAll(selPath, true);
			        			 System.out.println("Expand "+(System.currentTimeMillis() - initialTime));
			        			 initialTime = System.currentTimeMillis();
			     				 treeLeaves = updateTreeLeavesArray();
			        			 System.out.println("UpdateLeaves "+(System.currentTimeMillis() - initialTime));
			        			 initialTime = System.currentTimeMillis();
			        			 controller.updateVisualization(controller.getDesignStructureMatrixRepresentation(), treeLeaves);
			     				 System.out.println("updateVisualization "+(System.currentTimeMillis() - initialTime));
	
			        		 }});
			        		 
			        	 popupMenu.add(contract);
	
			        	 contract.addActionListener((ActionListener) new ActionListener() {
			        		 @Override
			        		 public void actionPerformed(ActionEvent e) {
			        			 expandAll(selPath, false);
			        			 controller.updateVisualization(controller.getDesignStructureMatrixRepresentation(), updateTreeLeavesArray());
	
			        		 }});
	
			        	 
			        	 popupMenu.show(e.getComponent(), e.getX(), e.getY());
		             }
		         }
		     }
		 });
		
		//Finalize Setup
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		pack();
		
		setVisible(true);
	}
	
	/**
	 * Expands all of the children of a partcular element in the tree
	 * 
	 * @param tree the JTree
	 * @param path
	 * @param expand
	 */
	private void expandAll(TreePath path, boolean expand) {
        if(path.getLastPathComponent() instanceof MatrixElementGroup) {
        	MatrixElementGroup group = (MatrixElementGroup)path.getLastPathComponent();
            for(MatrixElement element:group.getChildren()) {
                	TreePath p = path.pathByAddingChild(element);
                	
                    expandAll(p, expand);
                }
            }


        if (expand) {
        	elementsTree.expandPath(path);
        } else {
        	elementsTree.collapsePath(path);
        }
    }
	
	/**
	 * Updates the array of visible leaves
	 * 
	 * @return
	 */
	private ArrayList <MatrixElement> updateTreeLeavesArray() {

		ArrayList <MatrixElement> visibleRows = new ArrayList <MatrixElement>();
		// We start from one to skip the root
		for(int i=1;i<elementsTree.getRowCount();i++) {
				
			visibleRows.add((MatrixElement)elementsTree.getPathForRow(i).getLastPathComponent());
		}
		
		Enumeration en = elementsTree.getExpandedDescendants(elementsTree.getPathForRow(0));
		
		if(en != null) {
			while(en.hasMoreElements()) {
				TreePath element = (TreePath) en.nextElement();
				visibleRows.remove(element.getLastPathComponent());
			}
		}
		
		return visibleRows;
	}
	
	

}