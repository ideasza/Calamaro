package dev.teerayut.calamaro.process;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import dev.teerayut.calamaro.main.MainActivity;
import dev.teerayut.calamaro.model.CurrencyItem;
import dev.teerayut.calamaro.utils.Convert;
import dev.teerayut.calamaro.utils.ScreenCenter;

import java.awt.Font;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ListSelectionModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ProcessActivity extends JDialog{

	private int exitCode = ID_CANCEL;
	public static final int ID_OK = 1;
    public static final int ID_CANCEL = 0;
    
    private DefaultTableModel model;
    
    private Object[][] data;
    private Object[] columName = {"สกุลเงิน", "เรท", "จำนวน", "รวม"};
	
    private int columns;
    private String currencyCode;
    private List<CurrencyItem> currencyItemList = new ArrayList<CurrencyItem>();
    
    public ProcessActivity(Frame owner) {
        super(owner);
        createGUI();
    }

    /**
     * @wbp.parser.constructor
     */
    public ProcessActivity(Dialog owner) {
        super(owner);        
        setFocusableWindowState(false);
        setFocusable(false);
        createGUI();
    }
    
    private javax.swing.JPanel centerPanel;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JButton button;
    private javax.swing.JScrollPane tableScroll;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotalAmount;
    javax.swing.JTable table;
    private void iniWidget() {
    	centerPanel = new javax.swing.JPanel();
    	bottomPanel = new javax.swing.JPanel();
    	leftPanel = new javax.swing.JPanel();
    	rightPanel = new javax.swing.JPanel();
    	button = new javax.swing.JButton("OK");
    	button.setHorizontalTextPosition(SwingConstants.CENTER);
    	button.addKeyListener(new KeyAdapter() {
    		@Override
    		public void keyPressed(KeyEvent e) {
    			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					JOptionPane.showMessageDialog(ProcessActivity.this, lblTotalAmount.getText().toString());
				}
    		}
    	});
    	button.setFont(new Font("Tahoma", Font.PLAIN, 18));
    	tableScroll = new javax.swing.JScrollPane();
    	lblTotal = new JLabel("รวม");
    	lblTotalAmount = new JLabel("");
    }
    
    private void createGUI() {
    	iniWidget();
    	//getItem();
    	int width = 840;
    	int height = 410;
    	setBounds(0, 0, 680, 320);
    	setTitle("CALAMARO");
    	setIconImage(new ImageIcon(getClass().getResource("/ic_calamaro.png")).getImage());
    	new ScreenCenter().centreWindow(this);
    	getContentPane().setLayout(new java.awt.BorderLayout());
    	
    	centerPanel.setPreferredSize(new java.awt.Dimension(800, 250));
    	getContentPane().add(centerPanel, java.awt.BorderLayout.CENTER);
    	
    	leftPanel.setPreferredSize(new java.awt.Dimension(10, 250));
    	getContentPane().add(leftPanel, java.awt.BorderLayout.WEST);
    	
    	rightPanel.setPreferredSize(new java.awt.Dimension(10, 250));
    	getContentPane().add(rightPanel, java.awt.BorderLayout.EAST);
    	
    	bottomPanel.setPreferredSize(new java.awt.Dimension(800, 35));
    	getContentPane().add(bottomPanel, java.awt.BorderLayout.SOUTH);
    	bottomPanel.setLayout(new BorderLayout(0, 0));
    	
    	javax.swing.JPanel panelLeft = new javax.swing.JPanel();
    	panelLeft.setPreferredSize(new java.awt.Dimension(10, 40));
    	bottomPanel.add(panelLeft, java.awt.BorderLayout.WEST);
    	
    	javax.swing.JPanel panelCenter = new javax.swing.JPanel();
    	bottomPanel.add(panelCenter, java.awt.BorderLayout.CENTER);
    	panelCenter.setLayout(new java.awt.BorderLayout());
    	
    	lblTotal.setFocusable(false);
    	lblTotal.setFont(new Font("Tahoma", Font.PLAIN, 18));
    	panelCenter.add(lblTotal, java.awt.BorderLayout.WEST);
    	
    	lblTotalAmount.setFocusable(false);
    	lblTotalAmount.setForeground(new Color(0, 0, 51));
    	lblTotalAmount.setHorizontalAlignment(SwingConstants.CENTER);
    	lblTotalAmount.setFont(new Font("Tahoma", Font.BOLD, 26));
    	panelCenter.add(lblTotalAmount, java.awt.BorderLayout.CENTER);    	
    	
    	panelCenter.add(button, BorderLayout.EAST);
    	
    	javax.swing.JPanel panelRight = new javax.swing.JPanel();
    	panelRight.setPreferredSize(new java.awt.Dimension(10, 40));
    	bottomPanel.add(panelRight, java.awt.BorderLayout.LINE_END);
    	
    	createTable();
    }
    
    private void createTable() {
    	model = new DefaultTableModel(data, columName);
		centerPanel.setLayout(new BorderLayout(0, 0));
		table = new javax.swing.JTable(model) {
			public boolean isCellEditable(int rowIndex, int colIndex) {
				if(colIndex == 0) {
		    		return false;
		    	} else if (colIndex == 1) {
		    		return false;
		    	} else if (colIndex == 2) {
		    		return true;
		    	} else {
		    		return false;
		    	}
		    }
			
			@SuppressWarnings("deprecation")
			@Override
	        public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
	            Component c = super.prepareRenderer(renderer, row, column);
	            JComponent jc = (JComponent) c;
	            /*if (isRowSelected(row)) {
	                jc.setBackground(Color.orange);
	                jc.setBorder(highlight);
	            } else {
	                jc.setBackground(Color.white);
	            }*/
	            if (isColumnSelected(2)) {
	            	column = 2;
	            	jc.requestFocus();
	            	jc.requestFocusInWindow();
	            	button.requestFocus();
					button.requestFocusInWindow();
	            }
	            return c;
	        }
		};
				
		table.getModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				if(e.getType()==(TableModelEvent.UPDATE)) {
					int col = e.getColumn();
					int row = e.getFirstRow();
					float rate = Float.parseFloat(table.getValueAt(row, 1).toString());
					float amount = Float.parseFloat(table.getValueAt(row, 2).toString());
					float sum = (rate * amount);
					
					calculate(table, lblTotalAmount);
					if (col == 2) {
						table.setValueAt(new Convert().formatDecimal(sum), row, 3);
		            }
				}
			}
		});
		
		table.requestFocus();
		table.editCellAt(0, 2);
		table.setColumnSelectionAllowed(true);		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.changeSelection(0, 2, true, false);
		tableScroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		tableScroll.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		centerPanel.add(tableScroll);
		
		int tableWidth = (800 / 4);
		JTableHeader header = table.getTableHeader();
    	header.setPreferredSize(new Dimension(tableWidth, 40));
		table.getTableHeader().setFont(new Font("Angsana New", Font.BOLD, 24));
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		table.setFillsViewportHeight(true); 
		table.setRowSelectionAllowed(false);
		table.getPreferredScrollableViewportSize();
		table.getColumnModel().getColumn(0).setPreferredWidth(tableWidth);
		table.getColumnModel().getColumn(1).setPreferredWidth(tableWidth);
		table.getColumnModel().getColumn(2).setPreferredWidth(tableWidth);
		table.getColumnModel().getColumn(3).setPreferredWidth(tableWidth);
		table.setRowHeight(45);
		table.setFont(new Font("Angsana New", Font.BOLD, 20));
		tableScroll.setViewportView(table);
		
		DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
		centerRender.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(0).setCellRenderer(centerRender);
		
		DefaultTableCellRenderer rightRender = new DefaultTableCellRenderer();
		rightRender.setHorizontalAlignment(JLabel.RIGHT);
		table.getColumnModel().getColumn(1).setCellRenderer(rightRender);
		table.getColumnModel().getColumn(2).setCellRenderer(rightRender);
		table.getColumnModel().getColumn(3).setCellRenderer(rightRender);
		table.requestFocusInWindow();
		
		getItem();
    }
    
    private void getItem() {
    	this.currencyItemList = MainActivity.getCurrencyItem();
    	this.currencyCode = MainActivity.getCurrencyCode();
    	setItemToTable();
    }
    
    private void setItemToTable() {
    	for (CurrencyItem item : currencyItemList) {
    		if (currencyCode.equals(item.getBuyCode())) {
    			model.addRow(new Object[] {item.getName(), new Convert().formatDecimal(Float.parseFloat(item.getBuyRate())), "", "0.00"});
    		} else if (currencyCode.equals(item.getSellCode())) {
    			model.addRow(new Object[] {item.getName(), new Convert().formatDecimal(Float.parseFloat(item.getSellRate())), "", "0.00"});
    		}
    	}
    }
    
    public void calculate(JTable table, JLabel lblTotal) {
		int rowCounth = table.getRowCount();
	    float sum = 0;
	    float value = 0;
	    for (int i = 0; i < rowCounth; i++) {
	    	value = Float.parseFloat(table.getValueAt(i, 3).toString().replaceAll(",", ""));
	    	sum += value;
	    }
	    lblTotal.setText(String.valueOf(new Convert().formatDecimal(sum)));
	}
    
    @Override
    public void dispose() {
        super.dispose();
    }

    public int doModal() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModal(true);
        setVisible(true);
        return exitCode;
    }
}

