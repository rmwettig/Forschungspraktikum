package de.charite.compbio.ppi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import de.charite.compbio.helpers.GraphIO;

import sonumina.math.graph.DirectedGraph;
import sonumina.math.graph.Edge;
import junit.framework.TestCase;

public class NetworkSavingLoadingTest extends TestCase {

	public void testSaveNetworkToFile() 
	{
		DirectedGraph<Protein> g = new DirectedGraph<Protein>();
		Protein a = new Protein();
		Protein b = new Protein();
		Protein c = new Protein();
		Protein d = new Protein();
		Protein e = new Protein();
		a.setEnsemblProteinId("P1");
		a.setEnsemblGeneId("g1");
		
		b.setEnsemblProteinId("P2");
		b.setEnsemblGeneId("g2");
		
		c.setEnsemblProteinId("P3");
		c.setEnsemblGeneId("g3");
		
		d.setEnsemblProteinId("P4");
		d.setEnsemblGeneId("g4");
		
		e.setEnsemblProteinId("P5");
		e.setEnsemblGeneId("g5");

		g.addVertex(a);
		g.addVertex(b);
		g.addVertex(c);
		g.addVertex(d);
		g.addVertex(e);
		
		//   3c     2b
		//  /  \ /
		// 5e   1a
		//   \ /
		//    4d
		g.addEdge( new Edge<Protein>(a,b) );
		g.addEdge( new Edge<Protein>(a,c) );
		g.addEdge( new Edge<Protein>(a,d) );
		
		g.addEdge( new Edge<Protein>(b,a) );
		
		g.addEdge( new Edge<Protein>(c,a) );
		g.addEdge( new Edge<Protein>(c,e) );
		
		g.addEdge( new Edge<Protein>(d,a) );
		g.addEdge( new Edge<Protein>(d,e) );
		
		g.addEdge( new Edge<Protein>(e,c) );
		g.addEdge( new Edge<Protein>(e,d) );
		
		Gene up1 = new Gene();
		up1.setName("up1");
		up1.setChromosome("1");
		SNP snp = new SNP();
		snp.setId("1");
		snp.setPositionOnChromosome(5738);
		snp.setFunctionClass("NonSynonymus");
		snp.setFromNT("T");
		snp.setToNT("C");
		
		up1.getSnps().add( snp );
		a.setGene(up1);
		
		a.setGelData(new HashMap<String, ArrayList<TwoDGelData>>());
		TwoDGelData geld = new TwoDGelData();
		geld.setChangeDirection('d');
		geld.setRatio(0.52);
		geld.setConsomic("C12");
		geld.setSpotNr(4217);
		geld.setTranslationHasChanged(true);
		ArrayList<TwoDGelData> lis = new ArrayList<TwoDGelData>();
		lis.add(geld);
		a.getGelData().put("C12", lis);

		Gene up3 = new Gene();
		up3.setName("up3");
		up3.setChromosome("3");
		snp = new SNP();
		snp.setId("2");
		snp.setPositionOnChromosome(5738);
		snp.setFunctionClass("NonSynonymus");
		snp.setFromNT("T");
		snp.setToNT("C");
		
		up3.getSnps().add( snp );
		c.setGene(up3);
		
		c.setGelData(new HashMap<String, ArrayList<TwoDGelData>>());
		geld = new TwoDGelData();
		geld.setChangeDirection('d');
		geld.setRatio(0.79);
		geld.setConsomic("C12");
		geld.setSpotNr(3956);
		geld.setTranslationHasChanged(true);
		lis = new ArrayList<TwoDGelData>();
		lis.add(geld);
		c.getGelData().put("C12", lis);
		
		for(Protein p :  g.getDescendantVertices(a))
			System.out.println(p.getEnsemblProteinId());
		GraphIO.saveNetworkToFile(g, "data\\test_saved_network.txt");	
	}
	

	public void testLoadNetworkFromFile() {
		DirectedGraph<Protein> g = GraphIO.loadNetworkFromFile("data\\test_saved_network.txt");
		
		Protein a = new Protein();
		Protein b = new Protein();
		Protein c = new Protein();
		Protein d = new Protein();
		Protein e = new Protein();
		a.setEnsemblProteinId("P1");
		b.setEnsemblProteinId("P2");
		c.setEnsemblProteinId("P3");
		d.setEnsemblProteinId("P4");
		e.setEnsemblProteinId("P5");
		
		for(Protein p : g.getVertices())
		{
			if(p.getEnsemblProteinId().equals("P1"))
			{
				assertEquals("g1", p.getEnsemblGeneId());
				assertTrue(g.hasEdge(p, b));
				assertTrue(g.hasEdge(p, c));
				assertTrue(g.hasEdge(p, d));
				
				assertTrue(p.hasGene());
				assertEquals("up1", p.getGene().getName());
				assertTrue(p.getGene().hasSNP());
				assertTrue(p.has2DGelData());
				assertTrue(p.getGelData().containsKey("C12"));
				assertEquals(4217, p.getGelData().get("C12").get(0).getSpotNr());
				continue;
			}
			if(p.getEnsemblProteinId().equals("P2"))
			{
				assertEquals("g2", p.getEnsemblGeneId());
				assertTrue(g.hasEdge(p, a));
				
				assertFalse(p.hasGene());				
				assertFalse(p.getGene().hasSNP());
				assertFalse(p.has2DGelData());		
				continue;
			}
			if(p.getEnsemblProteinId().equals("P3"))
			{
				assertEquals("g1", p.getEnsemblGeneId());
				assertTrue(g.hasEdge(p, a));
				assertTrue(g.hasEdge(p, e));
				
				assertTrue(p.hasGene());
				assertEquals("g3", p.getGene().getName());
				assertEquals("3", p.getGene().getChromosome());
				assertEquals("up3", p.getGene().getName());
				
				assertTrue(p.getGene().hasSNP());
				assertEquals("2", p.getGene().getSnps().get(0).getId());
				assertEquals("T", p.getGene().getSnps().get(0).getFromNT());
				assertEquals("C", p.getGene().getSnps().get(0).getToNT());
				
				assertTrue(p.has2DGelData());
				assertTrue(p.getGelData().containsKey("C12"));
				assertEquals(3956, p.getGelData().get("C12").get(0).getSpotNr());
				assertTrue(p.getGelData().get("C12").get(0).hasChanged());
				assertEquals('d', p.getGelData().get("C12").get(0).getChangeDirection());
				assertEquals(0.79, p.getGelData().get("C12").get(0).getRatio());		
				continue;
			}
			if(p.getEnsemblProteinId().equals("P4"))
			{
				assertEquals("g4", p.getEnsemblGeneId());
				assertTrue(g.hasEdge(p, a));
				assertTrue(g.hasEdge(p, e));
				
				assertFalse(p.hasGene());
				assertFalse(p.getGene().hasSNP());
				assertFalse(p.has2DGelData());		
				continue;
			}
			
			if(p.getEnsemblProteinId().equals("P5"))
			{
				assertEquals("g4", p.getEnsemblGeneId());
				assertTrue(g.hasEdge(p, c));
				assertTrue(g.hasEdge(p, d));
				
				assertFalse(p.hasGene());
				assertFalse(p.getGene().hasSNP());
				assertFalse(p.has2DGelData());		
				continue;
			}
		}
	}

}
