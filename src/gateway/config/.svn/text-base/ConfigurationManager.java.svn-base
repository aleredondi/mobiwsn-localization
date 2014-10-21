
package gateway.config;


import java.io.*;
import java.util.*;

import javax.xml.parsers.*;
import org.w3c.dom.*;


/**
 * @author Davide Roveran
 * @version 1.0
 * 
 * Component used to handle configuration
 * parameters (implements Singleton Pattern)
 */
public class ConfigurationManager 
{
	private Hashtable<String,String> parameterList;
	
	/**
	 * Private Constructor
	 *
	 */
	private ConfigurationManager()
	{
		try
		{
			this.parameterList = new Hashtable<String, String>();
			this.loadParameters();
		}
		catch (Exception ex)
		{
			System.out.println("Exception occured while loading configuration parameters: " + 
							    ex.getMessage() + "; StackTrace:" + ex.getStackTrace().toString());
			
		}
	}
	
	/**
	 * Lazy initialization of ConfigurationManager instance, used
	 * to achieve thread-safety without using explicit synchronization
	 * constructs
	 */
	private static class ConfigurationManagerContainer
	{
		private static final ConfigurationManager instance = new ConfigurationManager();
	}
	
	
	/**
	 * 
	 * @return Singleton Instance of ConfigurationManager
	 */
	public static ConfigurationManager getInstance()
	{
		return ConfigurationManagerContainer.instance;
	}
	
	/**
	 * Loads parameters from a configuration file located in current
	 * directory
	 */
	private void loadParameters() 
	{
		try
		{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			
			Document doc = docBuilder.parse (new File("src/gateway/config/middleware.xml"));
			
			/*
			 * read list of <param name="param_name" value="param_value" />
			 */
			NodeList paramNodeList = doc.getElementsByTagName("param");
			
			for (int iCont =0; iCont < paramNodeList.getLength(); iCont++)
			{
				Node paramNode = paramNodeList.item(iCont);
				if (!this.parameterList.containsKey(
							paramNode.getAttributes().getNamedItem("name").toString()
												   )
					)
						this.parameterList.put(
								paramNode.getAttributes().getNamedItem("name").getTextContent(),
								paramNode.getAttributes().getNamedItem("value").getTextContent()
											  );
				
					
			}
			
		}
		catch (Exception ex)
		{
			System.out.println("Exception occured while loading configuration parameters: " + 
							   ex.getMessage() + "; StackTrace:" + ex.getStackTrace().toString());
			
		}
				
	}
	
	/**
	 * Returns the parameter value corresponding to the specified name
	 * or throws an Exception if parameterName is not found in parameterList
	 * 
	 * @param parameterName name of the parameter to be returned
	 * @return value of the parameter specified 
	 * @throws Exception
	 */
	public String getParameter(String parameterName) throws IllegalArgumentException
	{
		if (this.parameterList.containsKey(parameterName))
			return this.parameterList.get(parameterName);
		else
			throw new IllegalArgumentException("parameter: " + parameterName + " not found!");
	}
	
}
