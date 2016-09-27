import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.SAXParserFactory;  
import javax.xml.parsers.SAXParser;

public class MascotXMLReader {

	/**
	 * @param args
	 */

	public static void SaveData(File file, String header, ArrayList<String> data)
	{
		try
		{
			//File targetFile = new File(filename);
			FileWriter writer = new FileWriter(file);

			if(!header.equals(""))
			{
				writer.write(header+"\n");
				for(String s : data)
				{
					writer.write(s);
					writer.write("\n");
				}
			}
			else
			{
				for(String s : data)
				{
					writer.write(s);
					writer.write("\n");
				}
			}
			writer.close();
		}
		catch(Exception e)
		{
			//System.out.println(e.getMessage());
			System.err.println(e.getMessage());
		}
	}

	public static void main(String[] args)
	{
		if(args.length < 1)
		{
			System.err.println("Error! Incorrect number of arguments.\nUsage: <Search path> <Filename (will be created inside the search path)>");

			return;
		}
		
		//ToDo: check if search path exists
		
		try
		{	
			//long start = System.currentTimeMillis();

			SAXParserFactory spf = SAXParserFactory.newInstance();
			spf.setNamespaceAware(true);
			SAXParser parser = spf.newSAXParser();

			MascotXMLHandler handler = new MascotXMLHandler();

			//access to the file system
			FileSystemView fsv = FileSystemView.getFileSystemView();
			//get all files within the given directory
			File[] fa = fsv.getFiles(new File(args[0]), false);
			String header = "Filename\tProbe\tScore\tSeqCov\tEmpai\tmatPep\tmatQry\tMass\tpI\tAccession\tName\tThreshold\tURL\tInput_Data_File";
			//System.out.println(header);

			//output file handling
			//prevents unnecessary work if output file exists
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String input;
			File file = new File(args[1]);
			if(file.exists())
			{
				boolean validInput = false;
				System.out.println(String.format("%s exists already. No files were processed.\nDo you want to overwrite %s? Enter y to overwrite, n to change the output filename and q to quit.", args[1], args[1]));
				
				do //assure valid input, namely y, n and q as options
				{
					input = in.readLine();
					
					if (input.equals("q"))
					{
						System.out.println("Program aborted.");
						return;
					}
					else if (input.equals("y"))
					{
						System.out.println(String.format("%s will be overwritten.", file.getName()));
						validInput = true;
					}
					else if(input.equals("n"))
					{
						do
						{
							System.out.println("Enter a new output filename: ");
							input = in.readLine();
							if(input.equals("q"))
							{
								System.out.println("Program aborted.");
								return;
							}
							else
							{
								if(!input.contains(".xls"))
									input += ".xls";
								file = new File(input);
							}
						}
						while(file.exists());
						validInput = true;
					}
					else
						System.out.println(String.format("Invalid input. %s was entered, y | n | q was expected.", input));
				}
				while(!validInput);
			}


			//parsing section
			for (File f : fa) 
			{
				//skip non-xml files
				if(f.getName().contains(".xml"))
				{
					System.out.println("Reading " + f.getName());
					parser.parse(f, handler);
				}
				else
					continue;
			}

			SaveData(file, header, handler.GetDataList());

			System.out.println("Finished.");
			//System.out.println("Finished in "+ (System.currentTimeMillis() - start)/1000.0f +" s.");
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

}
