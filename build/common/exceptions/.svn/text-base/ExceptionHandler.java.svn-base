
package common.exceptions;


/**
 * 
 * @author Davide Roveran
 * @version 1.0
 * 
 * Defines the main component used to handle exception
 */
public class ExceptionHandler 
{
	
	/**
	 * Private Constructor
	 */
	private ExceptionHandler()
	{}
	
	/**
	 * Lazy initialization of ExceptionHandler instance, used
	 * to achieve thread-safety without using explicit synchronization
	 * constructs
	 */
	private static class ExceptionHandlerContainer
	{
		private final static ExceptionHandler instance = new ExceptionHandler();
	}
	
	/**
	 * 
	 * @return Singleton Instance of ExceptionHandler
	 */
	public static ExceptionHandler getInstance()
	{
		return ExceptionHandlerContainer.instance;
	}
	
	
	/**
	 * implements different exception handling procedure depeding
	 * of user configuration settings
	 * 
	 */
	public void handleException(Exception handledEx)
	{
	
		try
		{
			/*
			 * read exception_handling_mode parameter from ConfigurationManager
			if (ConfigurationManager.getInstance().
								getParameter("exception_handling_mode").equals("Console"))
			{
			 */

				System.out.println("EXCEPTION NAME: " + handledEx.getClass().toString());
				System.out.println("EXCEPTION MESSAGE: " + handledEx.getMessage());
				System.out.println("EXCEPTION STACKTRACE: " + this.stackTraceToString(handledEx.getStackTrace()));
				System.out.println("");

			/*
			}
			else if (ConfigurationManager.getInstance().
					getParameter("exception_handling_mode").equals("file"))
			{
				
				// andrebbe stampato su file e non su Console
				
				System.out.println("EXCEPTION NAME: " + handledEx.getClass().toString());
				System.out.println("EXCEPTION MESSAGE: " + handledEx.getMessage());
				System.out.println("EXCEPTION STACKTRACE: " + this.stackTraceToString(handledEx.getStackTrace()));
				System.out.println("");
			}
			*/
				
		}
		catch (Exception ex)
		{
			System.out.println("Exception occured while loading configuration parameters: " + 
							    ex.getMessage() + "; StackTrace:" + ex.getStackTrace().toString());
			
		}
		
	}
	
	private String stackTraceToString(StackTraceElement[] stackTraceElements)
	{
		String strStackTrace = new String("");
		
		for (int iCont =0; iCont < stackTraceElements.length; iCont++)
			strStackTrace = strStackTrace.concat("\n" + stackTraceElements[iCont].toString());			
		
		return strStackTrace;
		
	}
	
	

}
