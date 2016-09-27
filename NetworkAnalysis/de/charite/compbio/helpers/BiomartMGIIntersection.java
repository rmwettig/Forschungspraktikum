package de.charite.compbio.helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class BiomartMGIIntersection 
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		ArrayList<String> biom = readBiomart(args[0]);
		ArrayList<String> mgi = readMGI(args[1]);
		int numOfContSnps = 0;
		System.out.println(String.format("#Biom: %d\t#MGI: %d", biom.size(), mgi.size()));
		ArrayList<String> alreadySeen = new ArrayList<String>();
		for(String s : mgi)
			if(biom.contains(s.toLowerCase()) || biom.contains(s.toUpperCase()))
			{
				if(!alreadySeen.contains(s.toLowerCase()))
				{
					System.out.println(String.format("Found %s in both biom and mgi.", s));
					++numOfContSnps;
					alreadySeen.add(s.toLowerCase());
				}
			}
			else
				System.out.println(String.format("Found %s not in biom.", s));
		System.out.println("total shared: " + numOfContSnps);
//		for(String s : biom)
//			System.out.println(s);
	}
	
	public static ArrayList<String> readBiomart(String biomFile)
	{
		ArrayList<String> res = new ArrayList<String>();
		try
		{
			BufferedReader reader = new BufferedReader( new FileReader(biomFile) );
			reader.readLine();
			String line = "";
			String[] data;
			while( (line = reader.readLine()) != null )
			{
				if(!line.equals(""))
				{
					data = line.split("\t");
					if(data.length > 1)
						res.add(data[1]);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return res;
	}
	
	public static ArrayList<String> readMGI(String mgiFile)
	{
		ArrayList<String> res = new ArrayList<String>();
		try
		{
			BufferedReader reader = new BufferedReader( new FileReader(mgiFile) );
			reader.readLine();
			String line = "";
			String[] data;
			while( (line = reader.readLine()) != null )
			{
				if(!line.equals(""))
				{
					data = line.split("\t")[3].split(":");
					if(data.length > 1)
						res.add(data[0].trim().toUpperCase());
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return res;
	}
}
