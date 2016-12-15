/**
 * 
 */
package tracking.item;

import java.sql.*;
import java.util.*;

/**
 * @author Sumant
 *
 */
public class Simulation implements Runnable {

	/**
	 * 
	 */
	private Connection con;
	private ArrayList<Integer> list = new ArrayList<Integer>();
	Graph g;
	public Simulation(Connection con,ArrayList list, Graph g) 
	{
		this.con = con;
		this.list = list;
		this.g = g;
	}
	
	public void nextTick()
	{
		try 
		{
			Statement smt = con.createStatement();
			String query = "";
			for(Integer i: list)
			{
				query = "select * from package_status where tracking_id = "+Integer.toString(i)+
						" order by time_stamp DESC";
				smt.executeQuery(query);
				ResultSet rs = smt.getResultSet();
				ArrayList<String> a = new ArrayList<String>();
				while(rs.next())
				{
					String source = rs.getString(3);
					String dest = rs.getString(4);
					String current = rs.getString(5);
					if(source.equals(dest))
					{
						if(a != null)
							a.clear();
					}
					else
					{
						if(!dest.equals(current))
						{
							a.clear();
							a = g.shortestPath(source, dest);
							
							String nextHop = "";
							int k = a.indexOf(current);
							if(k>0)
							{
								nextHop = a.get(k+1);
							}
							else
							{
								nextHop = a.get(1);
								
							}
							a.clear();
							query = "insert into package_status values("+Integer.toString(i)+",'"+source+"','"
									+dest+"','"+nextHop+"',"+"GETDATE())";
							smt.execute(query);
						}
					}
				
					
					break;		
				}
			}
		} catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() 
	{
		while(true)
		{
			nextTick();
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

	}

}
