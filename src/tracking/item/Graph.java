/**
 * 
 */
package tracking.item;

//Graph.java
//Graph code, modified from code by Mark A Weiss.
//Computes Unweighted shortest paths.


import java.io.FileReader;
import java.sql.*;
import java.io.IOException;
import java.util.*;



//Used to signal violations of preconditions for
//various shortest path algorithms.
class GraphException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public GraphException( String name )
	{
		super( name );
	}
}

//Represents a vertex in the graph.
class Vertex implements Comparable <Vertex>
{
	public String     name;   // Vertex name
	public List<Edge> adj;    // Adjacent vertices
	public Vertex     prev;   // Previous vertex on shortest path
	public int        dist;   // Distance of path

	public Vertex( String nm )
	{ 
		name = nm; 
		adj = new LinkedList<Edge>( ); 
		reset(); 
	}
	public void reset()
	{
		dist = Graph.INFINITY; 
		prev = null; 
	}    
	
	@Override
	public int compareTo(Vertex arg0) 
	{
		// TODO Auto-generated method stub
		int dist = arg0.dist;
		return this.dist-dist;
	}
}

class Edge
{
	public Vertex dest;
	public int weight;
	
	public Edge(Vertex dest, int weight)
	{
		this.dest = dest;
		this.weight = weight;
	}

	public Vertex getDest() {
		return dest;
	}

	public int getWeight() {
		return weight;
	}
	
	
}
//Graph class: evaluate shortest paths.
//
//CONSTRUCTION: with no parameters.
//
//******************PUBLIC OPERATIONS**********************
//void addEdge( String v, String w )
//                           --> Add additional edge
//void printPath( String w )   --> Print path after alg is run
//void unweighted( String s )  --> Single-source unweighted
//******************ERRORS*********************************
//Some error checking is performed to make sure graph is ok,
//and to make sure graph satisfies properties needed by each
//algorithm.  Exceptions are thrown if errors are detected.

public class Graph 
{
	public static final int INFINITY = Integer.MAX_VALUE;
 
	public Map<String,Vertex> vertexMap = new HashMap<String,Vertex>( );
	public Connection con;
	public ArrayList<String> nodes = new ArrayList<String>();
	
	public Graph(Connection con)
	{
		this.con = con;
		starup(con);
	}
	public Graph()
	{
		
	}

 /**
  * Add a new edge to the graph.
  */
	 public void addEdge( String sourceName, String destName,int dist )
	 {
	     Vertex v = getVertex( sourceName );
	     Vertex w = getVertex( destName );
	     Edge e = new Edge(w,dist);
	     
	     v.adj.add( e );
	 }

 /**
  * Driver routine to print total distance.
  * It calls recursive routine to print shortest path to
  * destNode after a shortest path algorithm has run.
  */
	 public void printPath( String destName )
	 {
		 Vertex w = vertexMap.get( destName );
	     if( w == null )
	         throw new NoSuchElementException( "Destination vertex not found" );
	     else if( w.dist == INFINITY )
	    	 System.out.println( destName + " is unreachable" );
	     else
	     {
	    	 //System.out.print( "(Distance is: " + w.dist + ") " );
	    	 printPath( w );
	    	// System.out.println( );
	     }
	 }

 /**
  * If vertexName is not present, add it to vertexMap.
  * In either case, return the Vertex.
  */
	private Vertex getVertex( String vertexName )
	{
	     Vertex v = vertexMap.get( vertexName );
	     if( v == null )
	     {
	         v = new Vertex( vertexName );
	         vertexMap.put( vertexName, v );
	     }
	     return v;
	}

 /**
  * Recursive routine to print shortest path to dest
  * after running shortest path algorithm. The path
  * is known to exist.
  */
	 private void printPath( Vertex dest )
	 {
	     if( dest.prev != null )
	     {
	         printPath( dest.prev );
	         //System.out.print( " to " );
	     }
	    // System.out.print( dest.name );
	     nodes.add(dest.name);
	 }
 
 /**
  * Initializes the vertex output info prior to running
  * any shortest path algorithm.
  */
	 private void clearAll( )
	 {
	     for( Vertex v : vertexMap.values( ) )
	         v.reset( );
	 }

 

	 public ArrayList<String> shortestPath(String source,String destname)
	 {
		 clearAll();
		 Vertex start = vertexMap.get(source);
		 if( start == null )
	         throw new NoSuchElementException( "Start vertex not found" );
	
	     ArrayList<Vertex> q = new ArrayList<Vertex>( );
	     q.add(start);
	     start.dist = 0;
	     for(Map.Entry<String, Vertex> w : vertexMap.entrySet())
	     {
	    	 q.add(w.getValue());
	     }
	     Collections.sort(q);
	     ArrayList<Vertex> s = new ArrayList<Vertex>( );
	     while( !q.isEmpty( ) )
	     {
	    	 Vertex v = q.remove(0);
	    	 s.add(v);
	    	 for(Edge e : v.adj)
	    	 {
	    		 if(!s.contains(e.getDest()))
	    		 {
	    			 if(e.dest.dist > v.dist + e.weight)
	    			 {
	    				 e.dest.prev = v;
	    				 e.dest.dist = v.dist + e.weight;
	    				 Collections.sort(q);
	    			 }
	    		 }
	    	 }
	     }
	     printPath( destname );
	     return nodes;
	     
	 }


 /**
  * Process a request; return false if end of file.
  */
	 public boolean processRequest( Scanner in, Graph g )
	 {
	     try
	     {
	         System.out.print( "Enter start node: " );
	         String startName = in.nextLine( );
	
	         System.out.print( "Enter destination node: " );
	         String destName = in.nextLine( );
	
	        // g.shortestPath( startName );
	         g.printPath( destName );
	     }
	     catch( NoSuchElementException e )
	       { return false; }
	     catch( GraphException e )
	       { System.err.println( e ); }
	     return true;
	 }

	 public void starup(Connection con)
	 {
		 try
		 {
			 Statement stmt = con.createStatement();
			 ResultSet rs = stmt.executeQuery("select * from distance");
			 String line = null;
			 ResultSetMetaData md = rs.getMetaData();
			 int columnCount = md.getColumnCount();
			 while(rs.next())
			 {
				 line = "";
				 for (int i = 2; i <= columnCount; i++) 
				 {
					 line += (" "+rs.getString(i));
				 }
				 StringTokenizer st = new StringTokenizer( line );
				 try
				 {
					 if( st.countTokens( ) != 3 )
					 {
						 System.err.println( "Skipping ill-formatted line " + line );
						 continue;
					 }
					 String source  = st.nextToken( );
					 String dest    = st.nextToken( );
					 int weight = Integer.parseInt(st.nextToken());
					 addEdge( source, dest, weight );
		         	}
		         catch( NumberFormatException e )
		         {	 
		        	 System.err.println( "Skipping ill-formatted line " + line ); 
		         }		
			}
			System.out.println( "File read..." );
		    System.out.println( vertexMap.size( ) + " vertices" );
		
		    /*Scanner in = new Scanner( System.in );
		    while( g.processRequest( in, g ) );*/
	    }catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
	 }


}
