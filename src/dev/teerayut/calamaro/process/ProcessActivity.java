package dev.teerayut.calamaro.process;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import dev.teerayut.calamaro.main.MainActivity;
import dev.teerayut.calamaro.model.CalculateModel;
import dev.teerayut.calamaro.model.CurrencyItem;
import dev.teerayut.calamaro.utils.Convert;
import dev.teerayut.calamaro.utils.DateFormate;
import dev.teerayut.calamaro.utils.ScreenCenter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;

public class ProcessActivity extends JDialog implements ProcessInterface.View{

	private int exitCode = ID_CANCEL;
	public static final int ID_OK = 1;
    public static final int ID_CANCEL = 0;
    
    private DefaultTableModel model;
    
    private Object[][] data;
    private Object[] columName = {"สกุลเงิน", "เรท", "จำนวน", "รวม"};
	
    private int columns;
    private String currecyType;
    private String currencyCode;
    private List<CurrencyItem> currencyItemList = new ArrayList<>();
    
    private CalculateModel calculateModel;
    private List<CalculateModel> calculateModelList = new ArrayList<CalculateModel>();
    
    private static final String prefixName = "CMR";
	private String receiptNumber = null;
    private ProcessInterface.Presenter presenter;
    
    public ProcessActivity(Frame owner) {
        super(owner);
        createGUI();
    }

    /**
     * @wbp.parser.constructor
     */
    public ProcessActivity(Dialog owner) {
        super(owner);        
        addWindowListener(new WindowAdapter() {
        	@Override
        	public void windowOpened(WindowEvent e) {
                table.changeSelection(0, 2, false, false);
            	table.editCellAt(0, 2);
            	table.setValueAt("0", 0, 2);
        	}
        });
        setFocusableWindowState(false);
        setFocusable(false);
        createGUI();
    }
    
    private javax.swing.JPanel topPanel;
    private javax.swing.JPanel centerPanel;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JPanel panelDown;
    private javax.swing.JPanel panelUp;
    private javax.swing.JButton button;
    private javax.swing.JScrollPane tableScroll;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotalAmount;
    javax.swing.JTable table;
    private void iniWidget() {
    	presenter = new ProcessPresenter(this);
    	topPanel = new javax.swing.JPanel();
    	centerPanel = new javax.swing.JPanel();
    	bottomPanel = new javax.swing.JPanel();
    	panelDown = new javax.swing.JPanel();
    	panelUp = new javax.swing.JPanel();
    	leftPanel = new javax.swing.JPanel();
    	rightPanel = new javax.swing.JPanel();
    	button = new javax.swing.JButton("OK");
    	button.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			try {
	    			int row = table.getRowCount();
					for (int i = 0; i < row; i++) {
						if (!table.getValueAt(i, 2).toString().isEmpty()) {
							calculateModel = new CalculateModel();
							calculateModel.setReportNumber(receiptNumber);
							calculateModel.setReportDate(new DateFormate().getDate());
							calculateModel.setReportType(currecyType);
							calculateModel.setReportCurrency(table.getValueAt(i, 0).toString());
							calculateModel.setReportBuyRate(table.getValueAt(i, 1).toString());
							calculateModel.setReportSellRate(currencyItemList.get(i).getSellRate());
							calculateModel.setReportAmount(table.getValueAt(i, 2).toString());
							calculateModel.setReportTotal(table.getValueAt(i, 3).toString().replaceAll(",", ""));
							calculateModelList.add(calculateModel);
						}
					}
					presenter.insertReceipt(calculateModelList);
    			} catch(Exception ex) {
    				System.out.print(ex.getMessage());
    			}
    		}
    	});
    	
    	button.setHorizontalTextPosition(SwingConstants.CENTER);
    	button.addKeyListener(new KeyAdapter() {
    		@Override
    		public void keyPressed(KeyEvent e) {
    			/*try {
	    			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						int row = table.getRowCount();
						for (int i = 0; i < row; i++) {
							calculateModel = new CalculateModel()
									.setReportNumber(receiptNumber)
									.setReportType(currecyType)
									.setReportCurrency(table.getValueAt(i, 0).toString())
									.setReportBuyRate(table.getValueAt(i, 1).toString())
									.setReportSellRate(currencyItemList.get(i).getSellRate())
									.setReportAmount(table.getValueAt(i, 2).toString())
									.setReportTotal(table.getValueAt(i, 3).toString().replaceAll(",", ""));
						}
						calculateModelList.add(calculateModel);
						presenter.insertReceipt(calculateModelList);
					}
    			} catch (Exception ex) {
    				System.out.println("Error : " + ex.getMessage());
    			}*/
    		}
    	});
    	button.setFont(new Font("Tahoma", Font.PLAIN, 18));
    	tableScroll = new javax.swing.JScrollPane();
    	lblTotal = new JLabel("รวม");
    	lblTotalAmount = new JLabel("");
    }
    
    private void createGUI() {
    	iniWidget();
    	int width = 760;
    	int height = 410;
    	setBounds(0, 0, 760, 320);
    	setTitle("CALAMARO");
    	setIconImage(new ImageIcon(getClass().getResource("/ic_calamaro.png")).getImage());
    	new ScreenCenter().centreWindow(this);
    	getContentPane().setLayout(new java.awt.BorderLayout());   	
    	
    	topPanel.setPreferredSize(new java.awt.Dimension(750, 25));
    	getContentPane().add(topPanel, java.awt.BorderLayout.NORTH);
    	
    	centerPanel.setPreferredSize(new java.awt.Dimension(750, 200));
    	getContentPane().add(centerPanel, java.awt.BorderLayout.CENTER);
    	
    	leftPanel.setPreferredSize(new java.awt.Dimension(10, 200));
    	getContentPane().add(leftPanel, java.awt.BorderLayout.WEST);
    	
    	rightPanel.setPreferredSize(new java.awt.Dimension(10, 200));
    	getContentPane().add(rightPanel, java.awt.BorderLayout.EAST);
    	
    	bottomPanel.setPreferredSize(new java.awt.Dimension(750, 60));
    	getContentPane().add(bottomPanel, java.awt.BorderLayout.SOUTH);
    	bottomPanel.setLayout(new BorderLayout(0, 0));
    	
    	panelUp.setPreferredSize(new java.awt.Dimension(width, 20));
    	bottomPanel.add(panelUp, java.awt.BorderLayout.NORTH);
    	
    	panelDown.setPreferredSize(new java.awt.Dimension(width, 10));
    	bottomPanel.add(panelDown, java.awt.BorderLayout.SOUTH);
    	
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
    	lblTotalAmount.setText("0.00");
    	calculateModelList.clear();
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
	            if (isColumnSelected(2)) {
	            	column = 2;
	            	jc.requestFocus();
	            	jc.requestFocusInWindow();
	            } else if (isColumnSelected(3)) {
	            	button.requestFocus();
					button.requestFocusInWindow();
	            }
	            return c;
	        }
		};
		table.getModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				try {
					if(e.getType()==(TableModelEvent.UPDATE)) {
						int col = e.getColumn();
						int row = e.getFirstRow();
						float rate = Float.parseFloat(table.getValueAt(row, 1).toString());
						float amount = Float.parseFloat(table.getValueAt(row, 2).toString());
						float sum = (rate * amount);
						
						calculate(table, lblTotalAmount);
						if (col == 2) {
							table.setValueAt(new Convert().formatDecimal(sum), row, 3);
			            } else if (col == 3) {
			            	button.requestFocus();
							button.requestFocusInWindow();
							//button.doClick();
			            }
					}
				} catch (Exception ex) {
					System.out.println("Error : " + ex.getMessage());
				}
			}
		});
		
		table.requestFocus();
		table.setColumnSelectionAllowed(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.editCellAt(0, 2);
		table.changeSelection(0, 2, true, false);
		tableScroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		tableScroll.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		centerPanel.add(tableScroll);
		
		int tableWidth = (750 / 4);
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

		/*Action handleEnter = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                table.getCellEditor().stopCellEditing();
                int row = table.getSelectedRow();
                int col = table.getSelectedColumn();
                if (row == 0 && col == 2) {
                	button.requestFocus();
					button.requestFocusInWindow();
					button.doClick();
                } else {
                	table.changeSelection(row, col, false, false);
                	table.editCellAt(row, col);
                	table.getCellEditor().stopCellEditing();
                }
            }
        };
		
		table.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "handleEnter");
        table.getActionMap().put("handleEnter", handleEnter);*/
		
		getItem();
		presenter.getLastKey();
    }
    
    private void getItem() {
    	this.currencyItemList = MainActivity.getCurrencyItem();
    	this.currencyCode = MainActivity.getCurrencyCode();
    	setItemToTable();
    }
    
    private void setItemToTable() {
    	for (int i = 0; i < currencyItemList.size(); i++) {
    		CurrencyItem item = currencyItemList.get(i);
    		model.addRow(new Object[] {
    				item.getName(), 
    				new Convert().formatDecimal(
    						Float.parseFloat(
    								(currencyCode.equals(item.getBuyCode())) ? item.getBuyRate() : item.getSellRate())), "", "0.00"}
    		);
    		currencyCode = String.valueOf(Integer.parseInt(currencyCode) + 1);
    		if (currencyCode.equals(item.getBuyCode())) {
    			this.currecyType = "Buy";
    		} else if (currencyCode.equals(item.getSellCode())) {
    			this.currecyType = "Sell";
    		}
    	}
    }
    
    public void calculate(JTable table, JLabel labelParameter) {
		int rowCounth = table.getRowCount();
	    float sum = 0;
	    float value = 0;
	    for (int i = 0; i < rowCounth; i++) {
	    	value = Float.parseFloat(table.getValueAt(i, 3).toString().replaceAll(",", ""));
	    	sum += value;
	    }
	    labelParameter.setText(String.valueOf(new Convert().formatDecimal(sum)));
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

	@Override
	public void onSuccess(String success) {
		final ImageIcon icon = new ImageIcon(getClass().getResource("/success32.png"));
		JOptionPane.showMessageDialog(null, success, "Success", JOptionPane.INFORMATION_MESSAGE, icon);
		//createTable();
		this.dispose();
	}

	@Override
	public void onFail(String fail) {
		final ImageIcon icon = new ImageIcon(getClass().getResource("/fail32.png"));
        JOptionPane.showMessageDialog(null, fail, "Alert", JOptionPane.ERROR_MESSAGE, icon);
	}

	@Override
	public void onGenerateKey(ResultSet result) {
		try {
			if (result.next()) {
				receiptNumber = generateKey(result.getString("report_number"));
			} else {
				receiptNumber = generateKey(null);
			}
		} catch (Exception e) {
			receiptNumber = generateKey(null);
			System.out.println("No number!" + receiptNumber);
		}
	}
	
	private String generateKey(String lastkey) {
		String[] key;
		String prefixKey;
		String runningFormat = null;
		int running;
		String generateNumber = null;
		
		if (lastkey != null) {
			key = lastkey.split("-");
			//prefixKey = key[0];
			running = Integer.parseInt(key[1]);
			running++;
			//System.out.println("Running: " + running);
			if (running < 10) {
				runningFormat = "000" + running;
				//runningFormat = String.format("%04d", running);
			} else if (running >= 10 && running < 100) {
				runningFormat = "00" + running;
				//runningFormat = String.format("%03d", running);
			} else if (running >= 100 && running < 1000) {
				runningFormat = "0" + running;
				//runningFormat = String.format("%02d", running);
			} else if (running >= 1000 && running < 10000) {
				runningFormat = "" + running;
			} else {
				runningFormat = "0001";
			}
			generateNumber = prefixName + new DateFormate().getDateForBill() + "-" + runningFormat;
		} else {
			/*running = 0000;
			running++;*/
			generateNumber = prefixName + new DateFormate().getDateForBill() + "-0001";
		}
		return generateNumber;
	}

}

