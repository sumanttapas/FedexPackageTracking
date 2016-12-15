package tracking.item;

import java.awt.Component;
import java.awt.EventQueue;
import java.sql.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class QueryOutput {

	public JFrame frmTrackingDetails;
	private JTable tableTrackingDetails;
	//private JTable table=null;
	private JTextField textField;
	public int trackingID;
	Connection con;
	QueryEnter qe;
	//public JLabel lblSource;
//	private  JTextField textFieldSource;
	private JLabel lblDestination;
	private JTextField txtTime;
//	private  JTextField textFieldDestination;
//	private JTextField txtCurrentnode;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					QueryOutput window = new QueryOutput();
					window.frmTrackingDetails.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public static DefaultTableModel buildTableModel(ResultSet rs)
	        throws SQLException {

	    ResultSetMetaData metaData = rs.getMetaData();
	   /* ResultSet tmpRs = rs;
	    tmpRs.first();
	    //textFieldSource.setText(rs.getString(3));
		//textFieldDestination.setText(rs.getString(4));*/
	    // names of columns
	    Vector<String> columnNames = new Vector<String>();
	    int columnCount = metaData.getColumnCount();
	    for (int column = 3; column <= columnCount; column++) {
	        //columnNames.add(metaData.getColumnName(column));
	    	//columnNames.add("Source");
	    }
	    columnNames.add("Source");
	    columnNames.add("Destination");
	    columnNames.add("Previous Stops");
	    columnNames.add("Time Stamp");

	    // data of the table
	    Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	    while (rs.next()) {
	        Vector<Object> vector = new Vector<Object>();
	        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
	            vector.add(rs.getObject(columnIndex));
	        }
	        data.add(vector);
	    }

	    return new DefaultTableModel(data, columnNames);

	}
	
	public void resizeColumnWidth(JTable table) 
	{
	    final TableColumnModel columnModel = table.getColumnModel();
	    for (int column = 0; column < table.getColumnCount(); column++) {
	        int width = 15; // Min width
	        for (int row = 0; row < table.getRowCount(); row++) {
	            TableCellRenderer renderer = table.getCellRenderer(row, column);
	            Component comp = table.prepareRenderer(renderer, row, column);
	            width = Math.max(comp.getPreferredSize().width +1 , width);
	        }
	        if(width > 300)
	            width=300;
	        columnModel.getColumn(column).setPreferredWidth(width);
	    }
	}

	/**
	 * Create the application.
	 * @throws SQLException 
	 */
	public QueryOutput() throws SQLException 
	{
		//this.trackingID = trackingid;
		initialize();
	}
	public QueryOutput(int trackingid, Connection con, QueryEnter qe) throws SQLException 
	{
		this.trackingID = trackingid;
		this.con = con;
		this.qe = qe;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws SQLException 
	 */
	private void initialize() throws SQLException {
		frmTrackingDetails = new JFrame();
		frmTrackingDetails.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 10));
		frmTrackingDetails.setTitle("Tracking Details");
		frmTrackingDetails.setBounds(100, 100, 600, 372);
		frmTrackingDetails.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTrackingDetails.getContentPane().setLayout(null);
		
		JLabel lblSource = new JLabel("Source:");
		lblSource.setBounds(167, 11, 48, 20);
		frmTrackingDetails.getContentPane().add(lblSource);
		
		JTextField textFieldSource = new JTextField();
		textFieldSource.setFont(new Font("Tahoma", Font.PLAIN, 10));
		textFieldSource.setEditable(false);
		textFieldSource.setBounds(252, 11, 109, 20);
		frmTrackingDetails.getContentPane().add(textFieldSource);
		textFieldSource.setColumns(10);
		
		lblDestination = new JLabel("Destination:");
		lblDestination.setBounds(167, 32, 86, 24);
		frmTrackingDetails.getContentPane().add(lblDestination);
		
		JTextField textFieldDestination = new JTextField();
		textFieldDestination.setFont(new Font("Tahoma", Font.PLAIN, 10));
		textFieldDestination.setEditable(false);
		textFieldDestination.setBounds(252, 34, 109, 20);
		frmTrackingDetails.getContentPane().add(textFieldDestination);
		textFieldDestination.setColumns(10);
		
		
		JLabel lblCurrent = new JLabel("Current Location:");
		lblCurrent.setBounds(371, 14, 101, 14);
		frmTrackingDetails.getContentPane().add(lblCurrent);
		
		JTextField txtCurrentnode = new JTextField();
		txtCurrentnode.setFont(new Font("Tahoma", Font.PLAIN, 10));
		txtCurrentnode.setEditable(false);
		txtCurrentnode.setBounds(475, 11, 109, 20);
		frmTrackingDetails.getContentPane().add(txtCurrentnode);
		txtCurrentnode.setColumns(10);
		
		txtTime = new JTextField();
		txtTime.setFont(new Font("Tahoma", Font.PLAIN, 10));
		txtTime.setEditable(false);
		txtTime.setBounds(429, 34, 145, 20);
		frmTrackingDetails.getContentPane().add(txtTime);
		txtTime.setColumns(10);
		
		JTextField textField = new JTextField();
		textField.setEditable(false);
		textField.setBounds(71, 11, 86, 20);
		frmTrackingDetails.getContentPane().add(textField);
		textField.setColumns(10);
		textField.setText(Integer.toString(trackingID));
		
		Statement stmt = con.createStatement();
		String query = "select source,destination,current_node,time_stamp from package_status where tracking_id =  "+Integer.toString(trackingID)+" order by time_stamp DESC";
		stmt.executeQuery(query);
		ResultSet rs = stmt.getResultSet();
		DefaultTableModel model = null;
		try
		{
			rs.next();
		}catch(Exception e)
		{
			Alert.infoBox("Invalid Tracking ID", "Error");
		}
		textFieldSource.setText(rs.getString(1));
		textFieldDestination.setText(rs.getString(2));
		txtCurrentnode.setText(rs.getString(3));
		txtTime.setText(rs.getString(4));
		/*while(rs.next())
		{
			textFieldSource.setText(rs.getString(3));
			textFieldDestination.setText(rs.getString(4));
			break;
		}*/
		rs = stmt.getResultSet();
		model = buildTableModel(rs);
		
		/*if(rs.isBeforeFirst())
		{
			textFieldSource.setText(rs.getString(3));
			textFieldDestination.setText(rs.getString(4));
			model = buildTableModel(rs);
		}
		else
			JOptionPane.showMessageDialog(null, "Not found", "Yay, java", JOptionPane.PLAIN_MESSAGE);*/
		JScrollPane scrollPane=null;
		scrollPane = new JScrollPane();
		scrollPane.setBounds(24, 67, 531, 219);
		frmTrackingDetails.getContentPane().add(scrollPane);
		
		JTable table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(model);
		resizeColumnWidth(table);
		
		JLabel lblTrackingId = new JLabel("Tracking Id:");
		lblTrackingId.setBounds(0, 11, 78, 20);
		frmTrackingDetails.getContentPane().add(lblTrackingId);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmTrackingDetails.dispose();
				//qe.textTrackingID.setText("");
				qe.frmEnterTrackingId.setVisible(true);
			}
		});
		btnBack.setBounds(466, 300, 89, 23);
		frmTrackingDetails.getContentPane().add(btnBack);
		
		JLabel lblTime = new JLabel("Time:");
		lblTime.setBounds(371, 37, 48, 14);
		frmTrackingDetails.getContentPane().add(lblTime);
		
		frmTrackingDetails.setLocationRelativeTo(null);
		
		
		
		
		
		
		
		
		
		
	}
}
