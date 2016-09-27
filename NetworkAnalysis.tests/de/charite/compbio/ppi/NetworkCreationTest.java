package de.charite.compbio.ppi;

import sonumina.math.graph.*;
import junit.framework.TestCase;

public class NetworkCreationTest extends TestCase {

	public void testWrapperForTesting() 
	{
		DirectedGraph<Protein> g = null;
		
		for(Protein p : g.getVertices())
		{
			if(p.getEnsemblProteinId().equalsIgnoreCase("ENSMUSP00000021684"))
			{
				assertEquals(1, p.getGene().getSnps().size());
				assertEquals("CYP46A1", p.getGene().getName());
				assertEquals("12", p.getGene().getChromosome());
				assertEquals("16810231", p.getGene().getSnps().get(0).getId());
			}
			else if(p.getEnsemblProteinId().equalsIgnoreCase("ENSMUSP00000022767"))
			{
				assertEquals("METTL3", p.getGene().getName());
				assertEquals("14", p.getGene().getChromosome());
				assertEquals(1, p.getGene().getSnps().size());
				assertEquals("30911720", p.getGene().getSnps().get(0).getId());

			}
			else if(p.getEnsemblProteinId().equalsIgnoreCase("ENSMUSP00000119120"))
			{
				assertEquals("SYNE2", p.getGene().getName());
				assertEquals("12", p.getGene().getChromosome());
				assertEquals(2, p.getGene().getSnps().size());
				assertEquals("6388970", p.getGene().getSnps().get(0).getId());
				assertEquals("29188142", p.getGene().getSnps().get(1).getId());
			}
		}
		

	}

}
