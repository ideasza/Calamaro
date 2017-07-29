package dev.teerayut.calamaro.update;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import dev.teerayut.calamaro.model.CurrencyItem;
import dev.teerayut.calamaro.utils.ScreenCenter;
import dev.teerayut.calamaro.utils.tableImageRenderrer;


public class UpdateActivity extends JDialog implements UpdateInterface.View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int ID_OK = 1;
    public static final int ID_CANCEL = 0;
    private int exitCode = ID_CANCEL;
    
    private UpdateInterface.Presenter presenter;
    
    private DefaultTableModel model;
	
	private Object[] objs;
	private Object[] columName = {"No.", "Flag", "Currency", "Buy", "Sell"};
    
    public UpdateActivity(Frame owner) {
        super(owner);
        createGUI();
    }
	
	/**
     * @wbp.parser.constructor
     */
    public UpdateActivity(Dialog owner) {
        super(owner);        
        createGUI();
    }
    
    private javax.swing.JPanel topPanel;
    private javax.swing.JPanel centerPanel;
    private javax.swing.JPanel bottomPanel;
	
	private javax.swing.JTable table;
	private javax.swing.JScrollPane scrollPane;
    private void initWidget() {
		topPanel = new javax.swing.JPanel();
		bottomPanel = new javax.swing.JPanel();
		centerPanel = new javax.swing.JPanel();
		scrollPane = new javax.swing.JScrollPane();
	}
    
    /**
	 * Create the frame.
	 */
	public void createGUI() {
		initWidget();
		presenter = new UpdatePresenter(this);
		new ScreenCenter().centreWindow(this);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
    	int height = screenSize.height;
    	GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int h = gd.getDisplayMode().getHeight();
		int w = gd.getDisplayMode().getHeight();
        setPreferredSize(new Dimension(width - 200, height - 200));
        setTitle("CALAMARO - Update");
        setIconImage(new ImageIcon(getClass().getResource("/ic_calamaro.png")).getImage());
        
        topPanel.setLayout(new java.awt.BorderLayout());
        topPanel.setPreferredSize(new java.awt.Dimension(w, 20));
		getContentPane().add(topPanel, java.awt.BorderLayout.NORTH);
		
		bottomPanel.setPreferredSize(new java.awt.Dimension(w, 20));
		bottomPanel.setLayout(new java.awt.BorderLayout());
		getContentPane().add(bottomPanel, java.awt.BorderLayout.SOUTH);
		
		centerPanel.setLayout(new java.awt.BorderLayout());
		centerPanel.setBorder(BorderFactory.createTitledBorder("Update price"));
		getContentPane().add(centerPanel, java.awt.BorderLayout.CENTER);
		
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerifyInputWhenFocusTarget(false);
		scrollPane.setRequestFocusEnabled(false);
		centerPanel.add(scrollPane, java.awt.BorderLayout.CENTER);
		
		table =  new javax.swing.JTable() {
		    public boolean isCellEditable(int rowIndex, int colIndex) {
		    	if(colIndex == 0) {
		    		return false; // Disallow Column 0
		    	} else if (colIndex == 1) {
		    		return false;   // Allow the editing 
		    	} else if (colIndex == 2) {
		    		return false;   // Allow the editing 
		    	} else if (colIndex == 3) {
		    		return true;
		    	} else {
		    		return true;
		    	}
		    }
		    
		    @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return Integer.class;
                    case 1:
                        return ImageIcon.class;
                    case 2:
                        return String.class;
                    case 3:
                        return Float.class;
                    default:
                        return Float.class;
                }
            }
		};
		table.setFocusable(false);
		table.setForeground(Color.BLACK);
		table.setFillsViewportHeight(true); 
		table.setRowSelectionAllowed(false);
		table.setEnabled(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		presenter.LoadCurrency();
		
		scrollPane.setViewportView(table);
		pack();
        setLocationRelativeTo(getParent());
		
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
	public void showCurrency(List<CurrencyItem> modelList) {
		model = new DefaultTableModel(new Object[0][0], columName);
		for (int i = 0; i < modelList.size(); i++) {
			CurrencyItem m = modelList.get(i);
			model.addRow(new Object[] {(i + 1), "/flag/" + m.getImage().trim().toLowerCase(), m.getName().trim(), 
					Float.parseFloat(m.getBuyRate().trim()), 
					Float.parseFloat(m.getSellRate().trim())
			});
		}
		table.setModel(model);
		table.setFont(new Font("Angsana New", Font.BOLD, 48));
		JTableHeader header = table.getTableHeader();
		header.setPreferredSize(new Dimension(50, 60));
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		
		table.getTableHeader().setFont(new Font("Angsana New", Font.BOLD, 50));
		table.getTableHeader().setForeground(Color.BLACK);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		int centerPanelWidth = (int) screenSize.getWidth();
		int tableWidth = (centerPanelWidth / 4);
		table.getColumnModel().getColumn(0).setPreferredWidth(tableWidth / 5);
		table.getColumnModel().getColumn(1).setPreferredWidth(tableWidth / 2);
		table.getColumnModel().getColumn(2).setPreferredWidth(tableWidth - 50);
		table.getColumnModel().getColumn(3).setPreferredWidth(tableWidth);
		table.getColumnModel().getColumn(4).setPreferredWidth(tableWidth);
		table.setRowHeight(70);
		
		DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
		centerRender.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(0).setCellRenderer(centerRender);
		
		DefaultTableCellRenderer renderrerG = new DefaultTableCellRenderer();
		renderrerG.setHorizontalAlignment(JLabel.CENTER);
		renderrerG.setForeground(Color.GREEN);
		table.getColumnModel().getColumn(3).setCellRenderer(renderrerG);
		
		DefaultTableCellRenderer renderrerR = new DefaultTableCellRenderer();
		renderrerR.setHorizontalAlignment(JLabel.CENTER);
		renderrerR.setForeground(Color.RED);
		table.getColumnModel().getColumn(4).setCellRenderer(renderrerR);
		
		DefaultTableCellRenderer leftRender = new DefaultTableCellRenderer();
		leftRender.setHorizontalAlignment(JLabel.LEFT);
		table.getColumnModel().getColumn(2).setCellRenderer(leftRender);
		
		table.getColumnModel().getColumn(1).setCellRenderer(new tableImageRenderrer());
		
		table.getModel().addTableModelListener(new TableModelListener() {
			
			@Override
			public void tableChanged(TableModelEvent e) {
				if(e.getType()==(TableModelEvent.UPDATE)) {
					int col = e.getColumn();
					int row = e.getFirstRow();
					presenter.updateCurrency(Integer.parseInt(table.getValueAt(row, 0).toString()), table.getValueAt(row, 2).toString(),
							table.getValueAt(row, 3).toString(), table.getValueAt(row, 4).toString());
				}
			}
		});
	}

	@Override
	public void onFail(String fail) {
		final ImageIcon icon = new ImageIcon(getClass().getResource("/fail32.png"));
        JOptionPane.showMessageDialog(null, fail, "Alert", JOptionPane.ERROR_MESSAGE, icon);
	}

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UpdateActivity frame = new UpdateActivity();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 */
	/*public UpdateActivity() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setIconImage(new ImageIcon(getClass().getResource("/ic_calamaro.png")).getImage());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}*/

}
