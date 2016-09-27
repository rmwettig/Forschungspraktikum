package de.charite.compbio.ppi;

import java.util.HashMap;
import java.util.HashSet;

import junit.framework.TestCase;
import sonumina.math.graph.DirectedGraph;
import sonumina.math.graph.Edge;

public class GraphMeasureTest extends TestCase {



	public void testGetAllPaths_G1()
	{
		DirectedGraph<VertexData> G = new DirectedGraph<VertexData>();

		VertexData a = new VertexData("a");
		VertexData b = new VertexData("b");
		VertexData c = new VertexData("c");
		VertexData d = new VertexData("d");
		VertexData e = new VertexData("e");

		G.addVertex(a);
		G.addVertex(b);
		G.addVertex(c);
		G.addVertex(d);
		G.addVertex(e);

		G.addEdge( new Edge<VertexData>(a, b) );

		G.addEdge( new Edge<VertexData>(b, a) );
		G.addEdge( new Edge<VertexData>(b, c) );
		G.addEdge( new Edge<VertexData>(b, d) );

		G.addEdge( new Edge<VertexData>(c, b) );
		G.addEdge( new Edge<VertexData>(c, e) );
		G.addEdge( new Edge<VertexData>(c, d) );

		G.addEdge( new Edge<VertexData>(d, e) );
		G.addEdge( new Edge<VertexData>(d, b) );
		G.addEdge( new Edge<VertexData>(d, c) );

		G.addEdge( new Edge<VertexData>(e, c) );
		G.addEdge( new Edge<VertexData>(e, d) );

		/*		   c
		 * 		 / |  \
		 * a - b   |   e
		 * 		 \ |  /
		 *         d
		 */	

		HashSet<VertexData> subgraphNodes = G.getAllPaths(b, e);
		assertTrue("c not found.", subgraphNodes.contains(c));
		assertTrue("d not found.", subgraphNodes.contains(d));
		assertTrue("b not found.", subgraphNodes.contains(b));
		assertTrue("e not found.", subgraphNodes.contains(e));
		assertFalse("a found.", subgraphNodes.contains(a));
		for(VertexData v: subgraphNodes)
			System.out.println(v);
	}
	public void testGetAllPaths_G2()
	{
		VertexData a = new VertexData("a");
		VertexData b = new VertexData("b");
		VertexData c = new VertexData("c");
		VertexData d = new VertexData("d");
		VertexData e = new VertexData("e");

		DirectedGraph<VertexData> g2 = new DirectedGraph<VertexData>();

		g2.addVertex(a);
		g2.addVertex(b);
		g2.addVertex(c);
		g2.addVertex(d);
		g2.addVertex(e);

		/*
		 *  a \	  / d
		 *  |	c 	|
		 *  b /	  \ e
		 */
		g2.addEdge( new Edge<VertexData>(a, b) );
		g2.addEdge( new Edge<VertexData>(b, a) );

		g2.addEdge( new Edge<VertexData>(a, c) );
		g2.addEdge( new Edge<VertexData>(c, a) );

		g2.addEdge( new Edge<VertexData>(b, c) );
		g2.addEdge( new Edge<VertexData>(c, b) );

		g2.addEdge( new Edge<VertexData>(c, d) );
		g2.addEdge( new Edge<VertexData>(d, c) );

		g2.addEdge( new Edge<VertexData>(c, e) );
		g2.addEdge( new Edge<VertexData>(e, c) );

		g2.addEdge( new Edge<VertexData>(d, e) );
		g2.addEdge( new Edge<VertexData>(e, d) ) ;

		HashSet<VertexData> subgraphNodes = g2.getAllPaths(b, e);
		assertTrue("c not found.", subgraphNodes.contains(c));
		assertTrue("d not found.", subgraphNodes.contains(d));
		assertTrue("b not found.", subgraphNodes.contains(b));
		assertTrue("e not found.", subgraphNodes.contains(e));
		assertTrue("a not found.", subgraphNodes.contains(a));
	}
	public void testGetAllPaths_G3()
	{
		VertexData a = new VertexData("a");
		VertexData b = new VertexData("b");
		VertexData c = new VertexData("c");
		VertexData d = new VertexData("d");
		VertexData e = new VertexData("e");

		DirectedGraph<VertexData> g3 = new DirectedGraph<VertexData>();

		g3.addVertex(a);
		g3.addVertex(b);
		g3.addVertex(c);
		g3.addVertex(d);
		g3.addVertex(e);

		// a - b - c - d - e
		g3.addEdge( new Edge<VertexData>(a, b) );
		g3.addEdge( new Edge<VertexData>(b, a) );
		g3.addEdge( new Edge<VertexData>(b, c) );
		g3.addEdge( new Edge<VertexData>(c, b) );
		g3.addEdge( new Edge<VertexData>(c, d) );
		g3.addEdge( new Edge<VertexData>(d, c) );
		g3.addEdge( new Edge<VertexData>(d, e) );
		g3.addEdge( new Edge<VertexData>(e, d) );

		HashSet<VertexData> subgraphNodes = g3.getAllPaths(b, e);
		assertTrue("c not found.", subgraphNodes.contains(c));
		assertTrue("d not found.", subgraphNodes.contains(d));
		assertTrue("b not found.", subgraphNodes.contains(b));
		assertTrue("e not found.", subgraphNodes.contains(e));
		assertFalse("a found.", subgraphNodes.contains(a));
	}

	public void testGetAllPath_G4()
	{
		VertexData a = new VertexData("a");
		VertexData b = new VertexData("b");
		VertexData c = new VertexData("c");
		VertexData d = new VertexData("d");
		VertexData e = new VertexData("e");

		// a \
		// |  c - d
		// | /
		// b - -  e

		DirectedGraph<VertexData> g4 = new DirectedGraph<VertexData>();

		g4.addVertex(a);
		g4.addVertex(b);
		g4.addVertex(c);
		g4.addVertex(d);
		g4.addVertex(e);

		g4.addEdge( new Edge<VertexData>(a, b) );
		g4.addEdge( new Edge<VertexData>(b, a) );

		g4.addEdge( new Edge<VertexData>(a, c) );
		g4.addEdge( new Edge<VertexData>(c, a) );

		g4.addEdge( new Edge<VertexData>(b, c) );
		g4.addEdge( new Edge<VertexData>(c, b) );

		g4.addEdge( new Edge<VertexData>(c, d) );
		g4.addEdge( new Edge<VertexData>(d, c) );

		g4.addEdge( new Edge<VertexData>(b, e) );
		g4.addEdge( new Edge<VertexData>(e, b) );

		HashSet<VertexData> subgraphNodes = g4.getAllPaths(b, e);

		assertTrue("b not found.", subgraphNodes.contains(b));
		assertTrue("e not found.", subgraphNodes.contains(e));
		assertFalse("a found.", subgraphNodes.contains(a));
		assertFalse("c found.", subgraphNodes.contains(c));
		assertFalse("d found.", subgraphNodes.contains(d));
	}

	public void testGetAllPaths_G5()
	{
		DirectedGraph<VertexData> g5 = new DirectedGraph<VertexData>();

		VertexData a = new VertexData("a");
		VertexData b = new VertexData("b");
		VertexData c = new VertexData("c");
		VertexData d = new VertexData("d");
		VertexData e = new VertexData("e");

		g5.addVertex(a);
		g5.addVertex(b);
		g5.addVertex(c);
		g5.addVertex(d);
		g5.addVertex(e);

		//     e
		//   /   \
		// a - c - b
		//   \   /
		//     d

		g5.addEdge( new Edge<VertexData>(a, c) );
		g5.addEdge( new Edge<VertexData>(c, a) );
		g5.addEdge( new Edge<VertexData>(a, d) );
		g5.addEdge( new Edge<VertexData>(d, a) );
		g5.addEdge( new Edge<VertexData>(a, e) );
		g5.addEdge( new Edge<VertexData>(e, a) );

		g5.addEdge( new Edge<VertexData>(b, c) );
		g5.addEdge( new Edge<VertexData>(c, b) );
		g5.addEdge( new Edge<VertexData>(b, d) );
		g5.addEdge( new Edge<VertexData>(d, b) );
		g5.addEdge( new Edge<VertexData>(b, e) );
		g5.addEdge( new Edge<VertexData>(e, b) );

		HashSet<VertexData> subgraphNodes = g5.getAllPaths(b, e);
		assertTrue("c not found.", subgraphNodes.contains(c));
		assertTrue("d not found.", subgraphNodes.contains(d));
		assertTrue("b not found.", subgraphNodes.contains(b));
		assertTrue("e not found.", subgraphNodes.contains(e));
		assertTrue("a not found.", subgraphNodes.contains(a));

		for(VertexData v: subgraphNodes)
			System.out.println(v);
	}

	public void testGetAllPaths_G6()
	{
		VertexData a = new VertexData("a");
		VertexData b = new VertexData("b");
		VertexData c = new VertexData("c");
		VertexData d = new VertexData("d");
		VertexData e = new VertexData("e");

		DirectedGraph<VertexData> g6 = new DirectedGraph<VertexData>();

		g6.addVertex(a);
		g6.addVertex(b);
		g6.addVertex(c);
		g6.addVertex(d);
		g6.addVertex(e);

		// a - b - c - e
		//      \  |  /
		//         d

		g6.addEdge( new Edge<VertexData>(a, b) );

		g6.addEdge( new Edge<VertexData>(b, a) );
		g6.addEdge( new Edge<VertexData>(b, c) );
		g6.addEdge( new Edge<VertexData>(b, d) );

		g6.addEdge( new Edge<VertexData>(c, b) );
		g6.addEdge( new Edge<VertexData>(c, e) );
		g6.addEdge( new Edge<VertexData>(c, d) );

		g6.addEdge( new Edge<VertexData>(d, e) );
		g6.addEdge( new Edge<VertexData>(d, b) );
		g6.addEdge( new Edge<VertexData>(d, c) );

		g6.addEdge( new Edge<VertexData>(e, c) );
		g6.addEdge( new Edge<VertexData>(e, d) );

		HashSet<VertexData> subgraphNodes = g6.getAllPaths(c, e);

		assertTrue("c not found.", subgraphNodes.contains(c));
		assertTrue("d not found.", subgraphNodes.contains(d));
		assertTrue("b not found.", subgraphNodes.contains(b));
		assertTrue("e not found.", subgraphNodes.contains(e));
		assertFalse("a found.", subgraphNodes.contains(a));
	}

	public void testGetAllPaths_G7()
	{
		VertexData a = new VertexData("a");
		VertexData b = new VertexData("b");
		VertexData c = new VertexData("c");
		VertexData d = new VertexData("d");
		VertexData e = new VertexData("e");
		VertexData f = new VertexData("f");
		VertexData g = new VertexData("g");

		// a - - 
		// |    \
		// |     c - d
		// |    /    |
		// b - f  -  e
		// |
		// g

		DirectedGraph<VertexData> g7 = new DirectedGraph<VertexData>();

		g7.addVertex(a);
		g7.addVertex(b);
		g7.addVertex(c);
		g7.addVertex(d);
		g7.addVertex(e);
		g7.addVertex(f);
		g7.addVertex(g);

		g7.addEdge( new Edge<VertexData>(a, b) );
		g7.addEdge( new Edge<VertexData>(b, a) );

		g7.addEdge( new Edge<VertexData>(a, c) );
		g7.addEdge( new Edge<VertexData>(c, a) );

		g7.addEdge( new Edge<VertexData>(b, f) );
		g7.addEdge( new Edge<VertexData>(f, b) );
		g7.addEdge( new Edge<VertexData>(b, g) );
		g7.addEdge( new Edge<VertexData>(g, b) );

		g7.addEdge( new Edge<VertexData>(c, d) );
		g7.addEdge( new Edge<VertexData>(d, c) );
		g7.addEdge( new Edge<VertexData>(c, f) );
		g7.addEdge( new Edge<VertexData>(f, c) );

		g7.addEdge( new Edge<VertexData>(d, e) );
		g7.addEdge( new Edge<VertexData>(e, d) );

		g7.addEdge( new Edge<VertexData>(f, e) );
		g7.addEdge( new Edge<VertexData>(e, f) );

		HashSet<VertexData> subgraphNodes = g7.getAllPaths(b, e);

		assertTrue("b not found.", subgraphNodes.contains(b));
		assertTrue("e not found.", subgraphNodes.contains(e));
		assertTrue("a not found.", subgraphNodes.contains(a));
		assertTrue("c not found.", subgraphNodes.contains(c));
		assertTrue("d not found.", subgraphNodes.contains(d));
		assertTrue("f not found.", subgraphNodes.contains(f));
		assertFalse("g found.", subgraphNodes.contains(g));
	}

	public void testGetAllPaths_G8()
	{
		VertexData a = new VertexData("a");
		VertexData b = new VertexData("b");
		VertexData c = new VertexData("c");
		VertexData d = new VertexData("d");
		VertexData e = new VertexData("e");
		VertexData f = new VertexData("f");

		DirectedGraph<VertexData> g8 = new DirectedGraph<VertexData>();

		g8.addVertex(a);
		g8.addVertex(b);
		g8.addVertex(c);
		g8.addVertex(d);
		g8.addVertex(e);
		g8.addVertex(f);

		//       c --
		//      /     \
		// a - b - e - f
		//      \     / 
		//       d --

		g8.addEdge( new Edge<VertexData>(a, b) );

		g8.addEdge( new Edge<VertexData>(b, a) );
		g8.addEdge( new Edge<VertexData>(b, c) );
		g8.addEdge( new Edge<VertexData>(b, d) );
		g8.addEdge( new Edge<VertexData>(b, e) );

		g8.addEdge( new Edge<VertexData>(c, b) );
		g8.addEdge( new Edge<VertexData>(c, f) );

		g8.addEdge( new Edge<VertexData>(d, b) );
		g8.addEdge( new Edge<VertexData>(d, f) );

		g8.addEdge( new Edge<VertexData>(e, b) );
		g8.addEdge( new Edge<VertexData>(e, f) );

		g8.addEdge( new Edge<VertexData>(f, d) );
		g8.addEdge( new Edge<VertexData>(f, e) );
		g8.addEdge( new Edge<VertexData>(f, c) );

		HashSet<VertexData> subgraphNodes = g8.getAllPaths(a, e);

		assertTrue("c not found.", subgraphNodes.contains(c));
		assertTrue("d not found.", subgraphNodes.contains(d));
		assertTrue("b not found.", subgraphNodes.contains(b));
		assertTrue("e not found.", subgraphNodes.contains(e));
		assertTrue("a not found.", subgraphNodes.contains(a));
		assertTrue("f not found", subgraphNodes.contains(f));
	}

	public void testGetAllPaths_G9()
	{
		VertexData a = new VertexData("a");
		VertexData b = new VertexData("b");
		VertexData c = new VertexData("c");
		VertexData d = new VertexData("d");
		VertexData e = new VertexData("e");
		VertexData f = new VertexData("f");
		VertexData g = new VertexData("g");

		DirectedGraph<VertexData> g9 = new DirectedGraph<VertexData>();

		g9.addVertex(a);
		g9.addVertex(b);
		g9.addVertex(c);
		g9.addVertex(d);
		g9.addVertex(e);
		g9.addVertex(f);
		g9.addVertex(g);

		//       c 
		//      /     
		// a - b - e - f
		//      \   \ 
		//       d   g

		g9.addEdge( new Edge<VertexData>(a, b) );

		g9.addEdge( new Edge<VertexData>(b, a) );
		g9.addEdge( new Edge<VertexData>(b, c) );
		g9.addEdge( new Edge<VertexData>(b, d) );
		g9.addEdge( new Edge<VertexData>(b, e) );

		g9.addEdge( new Edge<VertexData>(c, b) );

		g9.addEdge( new Edge<VertexData>(d, b) );

		g9.addEdge( new Edge<VertexData>(e, b) );
		g9.addEdge( new Edge<VertexData>(e, f) );
		g9.addEdge( new Edge<VertexData>(e, g) );

		g9.addEdge( new Edge<VertexData>(f, e) );

		g9.addEdge( new Edge<VertexData>(g, e) );

		HashSet<VertexData> subgraphNodes = g9.getAllPaths(a, f);

		assertTrue("b not found.", subgraphNodes.contains(b));
		assertTrue("e not found.", subgraphNodes.contains(e));
		assertTrue("a not found.", subgraphNodes.contains(a));
		assertTrue("f not found", subgraphNodes.contains(f));

		assertFalse("c found.", subgraphNodes.contains(c));
		assertFalse("d found.", subgraphNodes.contains(d));
		assertFalse("g found.", subgraphNodes.contains(g));

		VertexData[] v2i = new VertexData[]{a,b,c,d,e,f,g};
		for(int i = 0; i < v2i.length; i++)
		{
			StringBuilder sb = new StringBuilder(v2i[i].toString() +": ");
			for(int j = 0; j < v2i.length; j++)
			{
				if(g9.hasEdge(v2i[i], v2i[j]))
				{sb.append(1); sb.append(" ");}
				else
				{sb.append(0); sb.append(" ");}
			}
			System.out.println(sb.toString());
		}
	}

	public void testGetAllPaths_G10()
	{
		VertexData a = new VertexData("a");
		VertexData b = new VertexData("b");
		VertexData c = new VertexData("c");
		VertexData d = new VertexData("d");
		VertexData e = new VertexData("e");

		DirectedGraph<VertexData> g10 = new DirectedGraph<VertexData>();

		g10.addVertex(a);
		g10.addVertex(b);
		g10.addVertex(c);
		g10.addVertex(d);
		g10.addVertex(e);


		//       c 
		//      /     
		// a - b   e
		//      \    
		//       d   

		g10.addEdge( new Edge<VertexData>(a, b) );

		g10.addEdge( new Edge<VertexData>(b, a) );
		g10.addEdge( new Edge<VertexData>(b, c) );
		g10.addEdge( new Edge<VertexData>(b, d) );


		g10.addEdge( new Edge<VertexData>(c, b) );

		g10.addEdge( new Edge<VertexData>(d, b) );

		HashSet<VertexData> subgraphNodes = g10.getAllPaths(a, e);

		assertTrue("a not found.", subgraphNodes.contains(a));
		assertTrue("e not found.", subgraphNodes.contains(e));

		assertFalse("b found.", subgraphNodes.contains(b));
		assertFalse("c found.", subgraphNodes.contains(c));
		assertFalse("d found.", subgraphNodes.contains(d));

		System.out.println("G10:");
		VertexData[] v2i = new VertexData[]{a,b,c,d,e};
		for(int i = 0; i < v2i.length; i++)
		{
			StringBuilder sb = new StringBuilder(v2i[i].toString() +": ");
			for(int j = 0; j < v2i.length; j++)
			{
				if(g10.hasEdge(v2i[i], v2i[j]))
				{sb.append(1); sb.append(" ");}
				else
				{sb.append(0); sb.append(" ");}
			}
			System.out.println(sb.toString());
		}

	}

	public void testGetNeighbourhoodConnectivity_G1()
	{
		DirectedGraph<VertexData> G = new DirectedGraph<VertexData>();

		VertexData a = new VertexData("a");
		VertexData b = new VertexData("b");
		VertexData c = new VertexData("c");
		VertexData d = new VertexData("d");
		VertexData e = new VertexData("e");

		G.addVertex(a);
		G.addVertex(b);
		G.addVertex(c);
		G.addVertex(d);
		G.addVertex(e);

		G.addEdge( new Edge<VertexData>(a, b) );

		G.addEdge( new Edge<VertexData>(b, a) );
		G.addEdge( new Edge<VertexData>(b, c) );
		G.addEdge( new Edge<VertexData>(b, d) );

		G.addEdge( new Edge<VertexData>(c, b) );
		G.addEdge( new Edge<VertexData>(c, e) );
		G.addEdge( new Edge<VertexData>(c, d) );

		G.addEdge( new Edge<VertexData>(d, e) );
		G.addEdge( new Edge<VertexData>(d, b) );
		G.addEdge( new Edge<VertexData>(d, c) );

		G.addEdge( new Edge<VertexData>(e, c) );
		G.addEdge( new Edge<VertexData>(e, d) );

		/*		   c
		 * 		 / |  \
		 * a - b   |   e
		 * 		 \ |  /
		 *         d
		 */	

		double ac = G.getNeighbourhoodConnectivity(a);
		double bc = G.getNeighbourhoodConnectivity(b);
		double cc = G.getNeighbourhoodConnectivity(c);
		double dc = G.getNeighbourhoodConnectivity(d);
		double ec = G.getNeighbourhoodConnectivity(e);

		System.out.println("A " + ac);
		System.out.println("B " + bc);
		System.out.println("C " + cc);
		System.out.println("D " + dc);
		System.out.println("E " + ec);

		assertEquals(3.0, ac, 0.0);
		assertEquals(7.0/3.0, bc, 0.0);
		assertEquals(8.0/3.0, cc, 0.0);
		assertEquals(8.0/3.0, dc, 0.0);
		assertEquals(6.0/2.0, ec, 0.0);
	}
	public void testGetNeighbourhoodConnectivity_G2()
	{
		DirectedGraph<VertexData> g2 = new DirectedGraph<VertexData>();

		VertexData a = new VertexData("a");
		VertexData b = new VertexData("b");
		VertexData c = new VertexData("c");
		VertexData d = new VertexData("d");
		VertexData e = new VertexData("e");

		g2.addVertex(a);
		g2.addVertex(b);
		g2.addVertex(c);
		g2.addVertex(d);
		g2.addVertex(e);

		/*
		 *  a \	  / d
		 *  |	c 	|
		 *  b /	  \ e
		 */
		g2.addEdge( new Edge<VertexData>(a, b) );
		g2.addEdge( new Edge<VertexData>(b, a) );

		g2.addEdge( new Edge<VertexData>(a, c) );
		g2.addEdge( new Edge<VertexData>(c, a) );

		g2.addEdge( new Edge<VertexData>(b, c) );
		g2.addEdge( new Edge<VertexData>(c, b) );

		g2.addEdge( new Edge<VertexData>(c, d) );
		g2.addEdge( new Edge<VertexData>(d, c) );

		g2.addEdge( new Edge<VertexData>(c, e) );
		g2.addEdge( new Edge<VertexData>(e, c) );

		g2.addEdge( new Edge<VertexData>(d, e) );
		g2.addEdge( new Edge<VertexData>(e, d) ) ;

		double ac = g2.getNeighbourhoodConnectivity(a);
		double bc = g2.getNeighbourhoodConnectivity(b);
		double cc = g2.getNeighbourhoodConnectivity(c);
		double dc = g2.getNeighbourhoodConnectivity(d);
		double ec = g2.getNeighbourhoodConnectivity(e);

		System.out.println();
		System.out.println("A2 " + ac);
		System.out.println("B2 " + bc);
		System.out.println("C2 " + cc);
		System.out.println("D2 " + dc);
		System.out.println("E2 " + ec);

		assertEquals(6.0/2.0, ac, 0.0);
		assertEquals(6.0/2.0, bc, 0.0);
		assertEquals(8.0/4.0, cc, 0.0);
		assertEquals(6.0/2.0, dc, 0.0);
		assertEquals(6.0/2.0, ec, 0.0);
	}
	public void testGetNeighbourhoodConnectivity_G3()
	{
		DirectedGraph<VertexData> g3 = new DirectedGraph<VertexData>();

		VertexData a = new VertexData("a");
		VertexData b = new VertexData("b");
		VertexData c = new VertexData("c");
		VertexData d = new VertexData("d");
		VertexData e = new VertexData("e");

		g3.addVertex(a);
		g3.addVertex(b);
		g3.addVertex(c);
		g3.addVertex(d);
		g3.addVertex(e);

		// a - b - c - d - e
		g3.addEdge( new Edge<VertexData>(a, b) );
		g3.addEdge( new Edge<VertexData>(b, a) );
		g3.addEdge( new Edge<VertexData>(b, c) );
		g3.addEdge( new Edge<VertexData>(c, b) );
		g3.addEdge( new Edge<VertexData>(c, d) );
		g3.addEdge( new Edge<VertexData>(d, c) );
		g3.addEdge( new Edge<VertexData>(d, e) );
		g3.addEdge( new Edge<VertexData>(e, d) );

		assertEquals(2, g3.getNeighbourhoodConnectivity(a), 0.0);
		assertEquals(2.0, g3.getNeighbourhoodConnectivity(c), 0.0);
	}
	public void testGetNeighbourhoodConnectivity_G4()
	{
		DirectedGraph<VertexData> g4 = new DirectedGraph<VertexData>();

		VertexData a = new VertexData("a");
		VertexData b = new VertexData("b");
		VertexData c = new VertexData("c");
		VertexData d = new VertexData("d");
		VertexData e = new VertexData("e");

		// a \
		// |  c - d
		// | /
		// b - -  e

		g4.addVertex(a);
		g4.addVertex(b);
		g4.addVertex(c);
		g4.addVertex(d);
		g4.addVertex(e);

		g4.addEdge( new Edge<VertexData>(a, b) );
		g4.addEdge( new Edge<VertexData>(b, a) );

		g4.addEdge( new Edge<VertexData>(a, c) );
		g4.addEdge( new Edge<VertexData>(c, a) );

		g4.addEdge( new Edge<VertexData>(b, c) );
		g4.addEdge( new Edge<VertexData>(c, b) );

		g4.addEdge( new Edge<VertexData>(c, d) );
		g4.addEdge( new Edge<VertexData>(d, c) );

		g4.addEdge( new Edge<VertexData>(b, e) );
		g4.addEdge( new Edge<VertexData>(e, b) );

		assertEquals(6.0/2.0, g4.getNeighbourhoodConnectivity(a), 0.0);
		assertEquals(6.0/3.0, g4.getNeighbourhoodConnectivity(b), 0.0);
		assertEquals(6.0/3.0, g4.getNeighbourhoodConnectivity(c), 0.0);
		assertEquals(3.0, g4.getNeighbourhoodConnectivity(d), 0.0);
		assertEquals(3.0, g4.getNeighbourhoodConnectivity(e), 0.0);
	}

	public void testGetSharedNeighbours_G1()
	{
		DirectedGraph<VertexData> G = new DirectedGraph<VertexData>();

		VertexData a = new VertexData("a");
		VertexData b = new VertexData("b");
		VertexData c = new VertexData("c");
		VertexData d = new VertexData("d");
		VertexData e = new VertexData("e");

		G.addVertex(a);
		G.addVertex(b);
		G.addVertex(c);
		G.addVertex(d);
		G.addVertex(e);

		G.addEdge( new Edge<VertexData>(a, b) );

		G.addEdge( new Edge<VertexData>(b, a) );
		G.addEdge( new Edge<VertexData>(b, c) );
		G.addEdge( new Edge<VertexData>(b, d) );

		G.addEdge( new Edge<VertexData>(c, b) );
		G.addEdge( new Edge<VertexData>(c, e) );
		G.addEdge( new Edge<VertexData>(c, d) );

		G.addEdge( new Edge<VertexData>(d, e) );
		G.addEdge( new Edge<VertexData>(d, b) );
		G.addEdge( new Edge<VertexData>(d, c) );

		G.addEdge( new Edge<VertexData>(e, c) );
		G.addEdge( new Edge<VertexData>(e, d) );

		/*		   c
		 * 		 / |  \
		 * a - b   |   e
		 * 		 \ |  /
		 *         d
		 */	

		assertEquals(0, G.getSharedNeighbours(a, b).size());
		assertEquals(2, G.getSharedNeighbours(b, e).size());
		assertEquals(2, G.getSharedNeighbours(d, c).size());
	}

	public void testGetSharedNeighbours_G2()
	{
		DirectedGraph<VertexData> g2 = new DirectedGraph<VertexData>();

		VertexData a = new VertexData("a");
		VertexData b = new VertexData("b");
		VertexData c = new VertexData("c");
		VertexData d = new VertexData("d");
		VertexData e = new VertexData("e");

		g2.addVertex(a);
		g2.addVertex(b);
		g2.addVertex(c);
		g2.addVertex(d);
		g2.addVertex(e);

		/*
		 *  a \	  / d
		 *  |	c 	|
		 *  b /	  \ e
		 */
		g2.addEdge( new Edge<VertexData>(a, b) );
		g2.addEdge( new Edge<VertexData>(b, a) );

		g2.addEdge( new Edge<VertexData>(a, c) );
		g2.addEdge( new Edge<VertexData>(c, a) );

		g2.addEdge( new Edge<VertexData>(b, c) );
		g2.addEdge( new Edge<VertexData>(c, b) );

		g2.addEdge( new Edge<VertexData>(c, d) );
		g2.addEdge( new Edge<VertexData>(d, c) );

		g2.addEdge( new Edge<VertexData>(c, e) );
		g2.addEdge( new Edge<VertexData>(e, c) );

		g2.addEdge( new Edge<VertexData>(d, e) );
		g2.addEdge( new Edge<VertexData>(e, d) ) ;

		assertEquals(1, g2.getSharedNeighbours(a, b).size());
		assertEquals(1, g2.getSharedNeighbours(c, e).size());
		assertEquals(1, g2.getSharedNeighbours(b, e).size());
	}

	public void testGetSharedNeighbours_G3()
	{
		DirectedGraph<VertexData> g3 = new DirectedGraph<VertexData>();

		VertexData a = new VertexData("a");
		VertexData b = new VertexData("b");
		VertexData c = new VertexData("c");
		VertexData d = new VertexData("d");
		VertexData e = new VertexData("e");

		g3.addVertex(a);
		g3.addVertex(b);
		g3.addVertex(c);
		g3.addVertex(d);
		g3.addVertex(e);

		// a - b - c - d - e
		g3.addEdge( new Edge<VertexData>(a, b) );
		g3.addEdge( new Edge<VertexData>(b, a) );
		g3.addEdge( new Edge<VertexData>(b, c) );
		g3.addEdge( new Edge<VertexData>(c, b) );
		g3.addEdge( new Edge<VertexData>(c, d) );
		g3.addEdge( new Edge<VertexData>(d, c) );
		g3.addEdge( new Edge<VertexData>(d, e) );
		g3.addEdge( new Edge<VertexData>(e, d) );

		assertEquals(1, g3.getSharedNeighbours(c, e).size());
		assertEquals(0,  g3.getSharedNeighbours(c, d).size());
	}

	public void testGetSharedNeighbours_G4()
	{
		// a \
		// |  c - d
		// | /
		// b - -  e

		DirectedGraph<VertexData> g4 = new DirectedGraph<VertexData>();

		VertexData a = new VertexData("a");
		VertexData b = new VertexData("b");
		VertexData c = new VertexData("c");
		VertexData d = new VertexData("d");
		VertexData e = new VertexData("e");

		g4.addVertex(a);
		g4.addVertex(b);
		g4.addVertex(c);
		g4.addVertex(d);
		g4.addVertex(e);

		g4.addEdge( new Edge<VertexData>(a, b) );
		g4.addEdge( new Edge<VertexData>(b, a) );

		g4.addEdge( new Edge<VertexData>(a, c) );
		g4.addEdge( new Edge<VertexData>(c, a) );

		g4.addEdge( new Edge<VertexData>(b, c) );
		g4.addEdge( new Edge<VertexData>(c, b) );

		g4.addEdge( new Edge<VertexData>(c, d) );
		g4.addEdge( new Edge<VertexData>(d, c) );

		g4.addEdge( new Edge<VertexData>(b, e) );
		g4.addEdge( new Edge<VertexData>(e, b) );

		assertEquals(1, g4.getSharedNeighbours(b, c).size());
		assertEquals(0, g4.getSharedNeighbours(d, c).size());
	}

	public void testGetSharedNeighbours_G5()
	{
		DirectedGraph<VertexData> g5 = new DirectedGraph<VertexData>();

		VertexData a = new VertexData("a");
		VertexData b = new VertexData("b");
		VertexData c = new VertexData("c");
		VertexData d = new VertexData("d");
		VertexData e = new VertexData("e");

		g5.addVertex(a);
		g5.addVertex(b);
		g5.addVertex(c);
		g5.addVertex(d);
		g5.addVertex(e);

		//     e
		//   /   \
		// a - c - b
		//   \   /
		//     d

		g5.addEdge( new Edge<VertexData>(a, c) );
		g5.addEdge( new Edge<VertexData>(c, a) );
		g5.addEdge( new Edge<VertexData>(a, d) );
		g5.addEdge( new Edge<VertexData>(d, a) );
		g5.addEdge( new Edge<VertexData>(a, e) );
		g5.addEdge( new Edge<VertexData>(e, a) );

		g5.addEdge( new Edge<VertexData>(b, c) );
		g5.addEdge( new Edge<VertexData>(c, b) );
		g5.addEdge( new Edge<VertexData>(b, d) );
		g5.addEdge( new Edge<VertexData>(d, b) );
		g5.addEdge( new Edge<VertexData>(b, e) );
		g5.addEdge( new Edge<VertexData>(e, b) );

		assertEquals(3, g5.getSharedNeighbours(a, b).size());
	}

	public void testGetTopologicalCoefficient_G1()
	{
		DirectedGraph<VertexData> G = new DirectedGraph<VertexData>();

		VertexData a = new VertexData("a");
		VertexData b = new VertexData("b");
		VertexData c = new VertexData("c");
		VertexData d = new VertexData("d");
		VertexData e = new VertexData("e");

		G.addVertex(a);
		G.addVertex(b);
		G.addVertex(c);
		G.addVertex(d);
		G.addVertex(e);

		G.addEdge( new Edge<VertexData>(a, b) );

		G.addEdge( new Edge<VertexData>(b, a) );
		G.addEdge( new Edge<VertexData>(b, c) );
		G.addEdge( new Edge<VertexData>(b, d) );

		G.addEdge( new Edge<VertexData>(c, b) );
		G.addEdge( new Edge<VertexData>(c, e) );
		G.addEdge( new Edge<VertexData>(c, d) );

		G.addEdge( new Edge<VertexData>(d, e) );
		G.addEdge( new Edge<VertexData>(d, b) );
		G.addEdge( new Edge<VertexData>(d, c) );

		G.addEdge( new Edge<VertexData>(e, c) );
		G.addEdge( new Edge<VertexData>(e, d) );

		/*		   c
		 * 		 / |  \
		 * a - b   |   e
		 * 		 \ |  /
		 *         d
		 */	

		assertEquals(1.0, G.getTopologicalCoefficient(a), 0.0);
		assertEquals((double) 2/3, G.getTopologicalCoefficient(b), 0.0);
		assertEquals((double) 2/3, G.getTopologicalCoefficient(c), 0.0);
		assertEquals((double) 2/3, G.getTopologicalCoefficient(d), 0.0);
		assertEquals(1.0, G.getTopologicalCoefficient(e), 0.0);
	}
	public void testGetTopologicalCoefficient_G2()
	{
		DirectedGraph<VertexData> g2 = new DirectedGraph<VertexData>();

		VertexData a = new VertexData("a");
		VertexData b = new VertexData("b");
		VertexData c = new VertexData("c");
		VertexData d = new VertexData("d");
		VertexData e = new VertexData("e");

		g2.addVertex(a);
		g2.addVertex(b);
		g2.addVertex(c);
		g2.addVertex(d);
		g2.addVertex(e);

		/*
		 *  a \	  / d
		 *  |	c 	|
		 *  b /	  \ e
		 */
		g2.addEdge( new Edge<VertexData>(a, b) );
		g2.addEdge( new Edge<VertexData>(b, a) );

		g2.addEdge( new Edge<VertexData>(a, c) );
		g2.addEdge( new Edge<VertexData>(c, a) );

		g2.addEdge( new Edge<VertexData>(b, c) );
		g2.addEdge( new Edge<VertexData>(c, b) );

		g2.addEdge( new Edge<VertexData>(c, d) );
		g2.addEdge( new Edge<VertexData>(d, c) );

		g2.addEdge( new Edge<VertexData>(c, e) );
		g2.addEdge( new Edge<VertexData>(e, c) );

		g2.addEdge( new Edge<VertexData>(d, e) );
		g2.addEdge( new Edge<VertexData>(e, d) ) ;

		assertEquals(0.75, g2.getTopologicalCoefficient(a), 0.0);
		assertEquals(0.75, g2.getTopologicalCoefficient(b), 0.0);
		assertEquals(0.5, g2.getTopologicalCoefficient(c), 0.0);
		assertEquals(0.75, g2.getTopologicalCoefficient(d), 0.0);
		assertEquals(0.75, g2.getTopologicalCoefficient(e), 0.0);
	}
	public void testGetTopologicalCoefficient_G3()
	{
		DirectedGraph<VertexData> g3 = new DirectedGraph<VertexData>();

		VertexData a = new VertexData("a");
		VertexData b = new VertexData("b");
		VertexData c = new VertexData("c");
		VertexData d = new VertexData("d");
		VertexData e = new VertexData("e");

		g3.addVertex(a);
		g3.addVertex(b);
		g3.addVertex(c);
		g3.addVertex(d);
		g3.addVertex(e);

		// a - b - c - d - e
		g3.addEdge( new Edge<VertexData>(a, b) );
		g3.addEdge( new Edge<VertexData>(b, a) );
		g3.addEdge( new Edge<VertexData>(b, c) );
		g3.addEdge( new Edge<VertexData>(c, b) );
		g3.addEdge( new Edge<VertexData>(c, d) );
		g3.addEdge( new Edge<VertexData>(d, c) );
		g3.addEdge( new Edge<VertexData>(d, e) );
		g3.addEdge( new Edge<VertexData>(e, d) );

		assertEquals(1.0, g3.getTopologicalCoefficient(a), 0.0);
		assertEquals(0.5, g3.getTopologicalCoefficient(c), 0.0);
	}
	public void testGetTopologicalCoefficient_G4()
	{
		// a \
		// |  c - d
		// | /
		// b - -  e

		DirectedGraph<VertexData> g4 = new DirectedGraph<VertexData>();

		VertexData a = new VertexData("a");
		VertexData b = new VertexData("b");
		VertexData c = new VertexData("c");
		VertexData d = new VertexData("d");
		VertexData e = new VertexData("e");

		g4.addVertex(a);
		g4.addVertex(b);
		g4.addVertex(c);
		g4.addVertex(d);
		g4.addVertex(e);

		g4.addEdge( new Edge<VertexData>(a, b) );
		g4.addEdge( new Edge<VertexData>(b, a) );

		g4.addEdge( new Edge<VertexData>(a, c) );
		g4.addEdge( new Edge<VertexData>(c, a) );

		g4.addEdge( new Edge<VertexData>(b, c) );
		g4.addEdge( new Edge<VertexData>(c, b) );

		g4.addEdge( new Edge<VertexData>(c, d) );
		g4.addEdge( new Edge<VertexData>(d, c) );

		g4.addEdge( new Edge<VertexData>(b, e) );
		g4.addEdge( new Edge<VertexData>(e, b) );

		assertEquals(0.75, g4.getTopologicalCoefficient(a), 0.0);
		assertEquals((double) 5/9, g4.getTopologicalCoefficient(b), 0.0); //(5/3) / 3
		assertEquals((double) 5/9, g4.getTopologicalCoefficient(c), 0.0);
		assertEquals(1.0, g4.getTopologicalCoefficient(d), 0.0);
		assertEquals(1.0, g4.getTopologicalCoefficient(e), 0.0);
	}
	public void testGetTopologicalCoefficient_G5()
	{
		DirectedGraph<VertexData> g5 = new DirectedGraph<VertexData>();

		VertexData a = new VertexData("a");
		VertexData b = new VertexData("b");
		VertexData c = new VertexData("c");
		VertexData d = new VertexData("d");
		VertexData e = new VertexData("e");

		g5.addVertex(a);
		g5.addVertex(b);
		g5.addVertex(c);
		g5.addVertex(d);
		g5.addVertex(e);

		//     e
		//   /   \
		// a - c - b
		//   \   /
		//     d

		g5.addEdge( new Edge<VertexData>(a, c) );
		g5.addEdge( new Edge<VertexData>(c, a) );
		g5.addEdge( new Edge<VertexData>(a, d) );
		g5.addEdge( new Edge<VertexData>(d, a) );
		g5.addEdge( new Edge<VertexData>(a, e) );
		g5.addEdge( new Edge<VertexData>(e, a) );

		g5.addEdge( new Edge<VertexData>(b, c) );
		g5.addEdge( new Edge<VertexData>(c, b) );
		g5.addEdge( new Edge<VertexData>(b, d) );
		g5.addEdge( new Edge<VertexData>(d, b) );
		g5.addEdge( new Edge<VertexData>(b, e) );
		g5.addEdge( new Edge<VertexData>(e, b) );

		assertEquals(1.0, g5.getTopologicalCoefficient(a), 0.0);
		assertEquals(1.0, g5.getTopologicalCoefficient(c), 0.0);
	}
	public void testGetTopologicalCoefficient_G6()
	{
		DirectedGraph<VertexData> g6 = new DirectedGraph<VertexData>();

		VertexData a = new VertexData("a");
		VertexData b = new VertexData("b");
		VertexData c = new VertexData("c");
		VertexData d = new VertexData("d");
		VertexData e = new VertexData("e");

		g6.addVertex(a);
		g6.addVertex(b);
		g6.addVertex(c);
		g6.addVertex(d);
		g6.addVertex(e);

		// a - b - c - e
		//      \  |  /
		//         d

		g6.addEdge( new Edge<VertexData>(a, b) );

		g6.addEdge( new Edge<VertexData>(b, a) );
		g6.addEdge( new Edge<VertexData>(b, c) );
		g6.addEdge( new Edge<VertexData>(b, d) );

		g6.addEdge( new Edge<VertexData>(c, b) );
		g6.addEdge( new Edge<VertexData>(c, e) );
		g6.addEdge( new Edge<VertexData>(c, d) );

		g6.addEdge( new Edge<VertexData>(d, e) );
		g6.addEdge( new Edge<VertexData>(d, b) );
		g6.addEdge( new Edge<VertexData>(d, c) );

		g6.addEdge( new Edge<VertexData>(e, c) );
		g6.addEdge( new Edge<VertexData>(e, d) );

		assertEquals(1.0, g6.getTopologicalCoefficient(a), 0.0);
		assertEquals((double) 2/3, g6.getTopologicalCoefficient(b), 0.0);
		assertEquals((double) 2/3, g6.getTopologicalCoefficient(c), 0.0);
		assertEquals((double) 2/3, g6.getTopologicalCoefficient(d), 0.0);
		assertEquals(1.0, g6.getTopologicalCoefficient(e), 0.0);
	}

	public void testDegreeDistributions_G1()
	{
		DirectedGraph<VertexData> G = new DirectedGraph<VertexData>();

		VertexData a = new VertexData("a");
		VertexData b = new VertexData("b");
		VertexData c = new VertexData("c");
		VertexData d = new VertexData("d");
		VertexData e = new VertexData("e");
		VertexData f = new VertexData("f");
		VertexData g = new VertexData("g");

		G.addVertex(a);
		G.addVertex(b);
		G.addVertex(c);
		G.addVertex(d);
		G.addVertex(e);
		G.addVertex(f);
		G.addVertex(g);

		G.addEdge( new Edge<VertexData>(a, b) );
		G.addEdge( new Edge<VertexData>(a, c) );

		G.addEdge( new Edge<VertexData>(b, a) );
		G.addEdge( new Edge<VertexData>(b, c) );
		G.addEdge( new Edge<VertexData>(b, d) );
		G.addEdge( new Edge<VertexData>(b, e) );
		G.addEdge( new Edge<VertexData>(b, f) );
		G.addEdge( new Edge<VertexData>(b, g) );

		G.addEdge( new Edge<VertexData>(c, a) );
		G.addEdge( new Edge<VertexData>(c, b) );

		G.addEdge( new Edge<VertexData>(d, b) );
		G.addEdge( new Edge<VertexData>(d, e) );

		G.addEdge( new Edge<VertexData>(e, b) );
		G.addEdge( new Edge<VertexData>(e, d) );

		G.addEdge( new Edge<VertexData>(f, b) );
		G.addEdge( new Edge<VertexData>(g, b) );

		//deg: a = c = d = e = 2; f = g = 1; b = 6;
		int chk = 0; //target value 3 if all degrees are correct
		for(int key : G.getDegreeDistribution().keySet())
		{
			int val = G.getDegreeDistribution().get(key);
			if(key == 2 && val == 4)
				++chk;
			else if(key == 1 && val == 2)
				++chk;
			else if(key == 6 && val == 1)
				++chk;
		}
		assertEquals(3, chk);

	}
	public void testDegreeDistributions_G4()
	{
		// a \
		// |  c - d
		// | /
		// b - -  e

		DirectedGraph<VertexData> g4 = new DirectedGraph<VertexData>();


		VertexData a = new VertexData("a");
		VertexData b = new VertexData("b");
		VertexData c = new VertexData("c");
		VertexData d = new VertexData("d");
		VertexData e = new VertexData("e");
		VertexData f = new VertexData("f");
		VertexData g = new VertexData("g");

		g4.addVertex(a);
		g4.addVertex(b);
		g4.addVertex(c);
		g4.addVertex(d);
		g4.addVertex(e);

		g4.addEdge( new Edge<VertexData>(a, b) );
		g4.addEdge( new Edge<VertexData>(b, a) );

		g4.addEdge( new Edge<VertexData>(a, c) );
		g4.addEdge( new Edge<VertexData>(c, a) );

		g4.addEdge( new Edge<VertexData>(b, c) );
		g4.addEdge( new Edge<VertexData>(c, b) );

		g4.addEdge( new Edge<VertexData>(c, d) );
		g4.addEdge( new Edge<VertexData>(d, c) );

		g4.addEdge( new Edge<VertexData>(b, e) );
		g4.addEdge( new Edge<VertexData>(e, b) );

		HashMap<Integer, Integer> map = g4.getDegreeDistribution();
		assertEquals(2, (int) map.get(3)); //2 nodes w/ degree 3
		assertEquals(1, (int) map.get(2));
		assertEquals(2, (int) map.get(1));
	}

}
