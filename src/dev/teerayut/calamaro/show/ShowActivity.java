package dev.teerayut.calamaro.show;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import dev.teerayut.calamaro.model.CurrencyItem;
import dev.teerayut.calamaro.tableheader.ColumnGroup;
import dev.teerayut.calamaro.tableheader.GroupableTableHeader;
import dev.teerayut.calamaro.utils.tableImageRenderrer;

public class ShowActivity extends JFrame implements ShowInterface.View {

	private DefaultTableModel model;
	
	private Object[] objs;
	private Object[] columName = {"", "", "", ""};
	
	private ShowInterface.Presenter presenter;
	
	private static final int pageSize = 14;
	private int numOfpage = 0;
	private int page = 0;
	private int startIndex;
	
	private int width, height;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ShowActivity frame = new ShowActivity();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	private javax.swing.JPanel topPanel;
	private javax.swing.JPanel Leftpanel;
	private javax.swing.JPanel Rightpanel;
	private javax.swing.JPanel Bottompanel;
	
	private javax.swing.JLabel lblCompanyName;
	
	private javax.swing.JTable table;
	private javax.swing.JScrollPane scrollPane;
	private void initWidget() {
		topPanel = new javax.swing.JPanel();
		Leftpanel = new javax.swing.JPanel();
		Rightpanel = new javax.swing.JPanel();
		Bottompanel = new javax.swing.JPanel();
		scrollPane = new javax.swing.JScrollPane();
		table = new javax.swing.JTable() {
			 protected JTableHeader createDefaultTableHeader() {
		          return new GroupableTableHeader(columnModel);
		      }
		};
		
		lblCompanyName = new javax.swing.JLabel();
	}
	
	public ShowActivity() {
		presenter = new ShowPresenter(this);
		setResizable(false);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		width = (int) dimension.getWidth();
		height = (int) dimension.getHeight();
		setExtendedState(MAXIMIZED_BOTH);
		setUndecorated(true);
		setPreferredSize(new java.awt.Dimension(width, height));
		setBounds(0, 0, width, height);
		setIconImage(new ImageIcon(getClass().getResource("/ic_calamaro.png")).getImage());
		getContentPane().setBackground(new Color(255, 204, 102));
		getContentPane().setLayout(new java.awt.BorderLayout());
		initWidget();

		topPanel.setBackground(new Color(255, 204, 102));
		topPanel.setPreferredSize(new java.awt.Dimension(width, 5));
		getContentPane().add(topPanel, java.awt.BorderLayout.NORTH);
		
		Leftpanel.setBackground(new Color(255, 204, 102));
		Leftpanel.setPreferredSize(new java.awt.Dimension(15, height));
		getContentPane().add(Leftpanel, java.awt.BorderLayout.WEST);
		
		Rightpanel.setBackground(new Color(255, 204, 102));
		Rightpanel.setPreferredSize(new java.awt.Dimension(15, height));
		getContentPane().add(Rightpanel, java.awt.BorderLayout.EAST);
		
		getContentPane().add(scrollPane, java.awt.BorderLayout.CENTER);
		
		Bottompanel.setPreferredSize(new java.awt.Dimension(width, 200));
		getContentPane().add(Bottompanel, java.awt.BorderLayout.SOUTH);
		Bottompanel.setBackground(new Color(255, 204, 102));
		
		lblCompanyName.setForeground(new Color(0, 0, 0));
		lblCompanyName.setFont(new Font("Angsana New", Font.BOLD, 70));
		lblCompanyName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		Bottompanel.add(lblCompanyName, java.awt.BorderLayout.CENTER);
		lblCompanyName.setIcon(new ImageIcon(getClass().getResource("/ic_calamaro.png")));
		lblCompanyName.setText("CALAMARO EXCHANGE CO.,LTD.");
		
		initTableShowCurrency();
	}
	
	private void initTableShowCurrency() {
		scrollPane.setBackground(new Color(51, 102, 255));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerifyInputWhenFocusTarget(false);
		scrollPane.setRequestFocusEnabled(false);
		getContentPane().add(scrollPane, java.awt.BorderLayout.CENTER);
		table.setFocusable(false);
		table.setForeground(Color.WHITE);
		table.setBackground(Color.BLACK);
		table.setFillsViewportHeight(true); 
		table.setRowSelectionAllowed(false);
		table.setEnabled(false);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
            	getCurrency();
            }
        }, 0, 8000);
        
		scrollPane.setViewportView(table);
	}
	
	private void getCurrency() {
		if (page == 0) {
			startIndex = 0;
			page++;
		} else if (page == 1) {
			startIndex = startIndex + pageSize;
			page = 0;
		}
		presenter.LoadCurrencyOffset(pageSize, startIndex);
	}

	@Override
	public void showCurrency(List<CurrencyItem> modelList) {
		model = new DefaultTableModel(new Object[0][0], columName);
		for (int i = 0; i < modelList.size(); i++) {
			CurrencyItem m = modelList.get(i);
			model.addRow(new Object[] {"/flag/" + m.getImage().trim().toLowerCase(), m.getName().trim(), 
					Float.parseFloat(m.getBuyRate().trim()), 
					Float.parseFloat(m.getSellRate().trim())
			});
		}
		table.setModel(model);
		table.setFont(new Font("Angsana New", Font.BOLD, 50));
		
		int screenHeight = (int) this.getSize().getHeight();
		int bottomHeight = (int) Bottompanel.getSize().getHeight();
		int scrollWidth = (int) this.getSize().getWidth();
		int colWidth = (scrollWidth - 80);
		int columnsWidth = (width / 4);;
		columnsWidth = (colWidth / 5);
		int smCol = (columnsWidth / 4);
		int mdCol = (columnsWidth / 2);
		int rowHeight = (screenHeight - 200) + 5;
		int columnsW = (columnsWidth / 2);
    	JTableHeader header = table.getTableHeader();
    	header.setPreferredSize(new Dimension(columnsWidth, (rowHeight / 13)));
    	
    	TableColumnModel cm = table.getColumnModel();
        ColumnGroup g_name = new ColumnGroup("Currency");
        g_name.add(cm.getColumn(0));
        g_name.add(cm.getColumn(1));
       
        ColumnGroup g_buyrate = new ColumnGroup("Buy");
        g_buyrate.add(cm.getColumn(2));
        ColumnGroup g_sellrate = new ColumnGroup("Sell");
        g_sellrate.add(cm.getColumn(3));
        
        GroupableTableHeader groupHeader = (GroupableTableHeader)table.getTableHeader();
        groupHeader.addColumnGroup(g_name);
        groupHeader.addColumnGroup(g_buyrate);
        groupHeader.addColumnGroup(g_sellrate);
    	
		table.getTableHeader().setFont(new Font("Angsana New", Font.BOLD, 60));
		//table.getTableHeader().setBackground(Color.WHITE);
		table.getTableHeader().setForeground(new Color(0, 51, 102));
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		
		table.getColumnModel().getColumn(0).setPreferredWidth(columnsWidth - mdCol);
		table.getColumnModel().getColumn(1).setPreferredWidth(columnsWidth + smCol);
		table.getColumnModel().getColumn(2).setPreferredWidth(columnsWidth + mdCol);
		table.getColumnModel().getColumn(3).setPreferredWidth(columnsWidth + mdCol);
		table.setRowHeight(rowHeight / 14);
		
		DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
		centerRender.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(2).setCellRenderer(centerRender);
		table.getColumnModel().getColumn(3).setCellRenderer(centerRender);
		
		DefaultTableCellRenderer leftRender = new DefaultTableCellRenderer();
		leftRender.setHorizontalAlignment(JLabel.LEFT);
		table.getColumnModel().getColumn(1).setCellRenderer(leftRender);
		
		table.getColumnModel().getColumn(0).setCellRenderer(new tableImageRenderrer());
	}

}
