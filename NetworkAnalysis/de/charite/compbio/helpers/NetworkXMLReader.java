package de.charite.compbio.helpers;

import java.io.File;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.SAXParserFactory;  
import javax.xml.parsers.SAXParser;

public class NetworkXMLReader
{

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try
		{
			SAXParserFactory spf = SAXParserFactory.newInstance();
			spf.setNamespaceAware(true);
			SAXParser parser = spf.newSAXParser();	
			NetworkXMLHandler handler = new NetworkXMLHandler();
			parser.parse(new File(args[0]), handler);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

}
