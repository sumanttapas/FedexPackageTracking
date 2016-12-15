package tracking.item;

import java.awt.EventQueue;
import java.sql.*;
import java.util.*;

import javax.swing.*;

import java.awt.BorderLayout;

import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Font;

public class QueryEnter {

	public JFrame frmEnterTrackingId;
	public JTextField textTrackingID;
	static Connection con = null;
	static QueryEnter window = null;
	static Thread t = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					
					String conUrl = "jdbc:sqlserver://localhost; databaseName=PackageTracking; user=sa; password=admin_123;";
					try 
					{
						con = DriverManager.getConnection(conUrl);	 
					}
					catch (Exception e) 
					{ 
						e.printStackTrace(); 
					}
					Graph g = new Graph(con);
					ArrayList<Integer> list = new ArrayList<Integer>();
					Statement stmt = con.createStatement();
					String query = "select top 1000 tracking_id from package_status order by id";
					stmt.executeQuery(query);
					ResultSet rs = stmt.getResultSet();
					while(rs.next())
					{
						list.add(rs.getInt(1));
					}
					
					
					Simulation sim = new Simulation(con, list, g);
					Thread t = new Thread(sim);
					t.start();
					
					
					
					window = new QueryEnter(con,t);
					window.frmEnterTrackingId.setVisible(true);
					
					
					//t.join();
					
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public QueryEnter() {
		initialize();
	}
	public QueryEnter(Connection c, Thread t) {
		QueryEnter.con = c;
		QueryEnter.t = t;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmEnterTrackingId = new JFrame();
		textTrackingID = new JTextField();
		JButton btnTrack = new JButton("Track");
		
		frmEnterTrackingId.setTitle("Enter Tracking ID");
		frmEnterTrackingId.getContentPane().setLayout(null);
		//frmEnterTrackingId.pack();
		
		JLabel lblEnterQuery = new JLabel("Tracking ID:");
		lblEnterQuery.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblEnterQuery.setBounds(51, 11, 75, 24);
		frmEnterTrackingId.getContentPane().add(lblEnterQuery);
		
		
		textTrackingID.setToolTipText("Enter the tracking ID");
		textTrackingID.setBounds(113, 13, 138, 20);
		frmEnterTrackingId.getContentPane().add(textTrackingID);
		textTrackingID.setColumns(10);
		textTrackingID.addKeyListener(new KeyAdapter() {
	        @Override
	        public void keyTyped(KeyEvent e) {
	            if (textTrackingID.getText().length() >= 8 ) // limit to 3 characters
	                e.consume();
	        }
	    });
		
		
		btnTrack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{
				QueryOutput qo = null;
				try {
					
					
					if(textTrackingID.getText().length() < 8)
					{
						Alert.infoBox("Tracking ID should be 8 digits.", "Error");
					}
					else if(!textTrackingID.getText().equals(""))
					{
						qo = new QueryOutput(Integer.parseInt(textTrackingID.getText()),con,window);
						frmEnterTrackingId.setVisible(false);
						qo.frmTrackingDetails.setVisible(true);
					}
					
				} catch (NumberFormatException  e) {
					Alert.infoBox("Only Enter Numeric values in tracking ID", "Error");					
				}catch(SQLException e1)
				{
					Alert.infoBox("Invalid Tracking ID.\nTracking ID are from 10000001 to 10001000", "Error");
				}
				
			}
		});
		
		btnTrack.setBounds(162, 44, 89, 23);
		frmEnterTrackingId.getContentPane().add(btnTrack);
		frmEnterTrackingId.setSize(300, 120);
		frmEnterTrackingId.setLocationRelativeTo(null);
		frmEnterTrackingId.addWindowListener(new WindowAdapter()
		{
		    @SuppressWarnings("deprecation")
			public void windowClosing(WindowEvent e)
		    {
		    	try 
				{
					con.close();
					t.stop();
					System.exit(0);
				} catch (SQLException e1) 
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    }
		});
	}
}



