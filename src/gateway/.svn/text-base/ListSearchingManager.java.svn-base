
package gateway;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.ArrayList;


public class ListSearchingManager 
{
	/**
	 * This method compare two object, verifying if all the string that identify the two object are similar
	 * @param input represent the first object of the comparison
	 * @param searched represent the second object of the comparison
	 * @return a boolean that indicates if all the String are similar or not 
	 */
	public static boolean objectComparison(ArrayList<String> input, ArrayList<String> searched)
	{
		boolean found=true;
		for(int i=0;i<input.size();i++)
		{
			String val=input.get(i);
			if(!val.equals(searched.get(i)))
			{
				found=false;
				break;
			}
		}
			
		return found;
		
	}

	
	/**
	 * This method is used for searching a class in a specific arrayList of object
	 * @param objectList list of the object trough which searching
	 * @param inputSearched list of the parameters that identify the class to search
	 * @return an integer that represent the position of the searched object in the arrayList, if this integer is -1, 
	 * the searched object is not in the list
	 */
	public static int searchClassIntoList(ArrayList<Object> objectList, ArrayList<String> inputSearched)
	{
		int result=-1;
		boolean found=false;
		
		Object element=null;
		Method m=null;
		
		for(int i=0; i<objectList.size();i++) //scan all the object of the list
		{
			element=objectList.get(i);
			try 
			{
				m=element.getClass().getDeclaredMethod("getClassIdentificationParameter");
				found=objectComparison((ArrayList<String>)m.invoke(element), inputSearched); // verify parameters
			}  
			catch (SecurityException e) {e.printStackTrace();} 
			catch (NoSuchMethodException e) {e.printStackTrace();} 
			catch (IllegalArgumentException e) {e.printStackTrace();} 
			catch (IllegalAccessException e) {e.printStackTrace();} 
			catch (InvocationTargetException e) {e.printStackTrace();}	
			
			if(found) //the object is the same
			{
				result=i;
				break;
			}
		}
		
		return result;
	}
	
	
	/**
	 * This method is used for verifying a version of a specific object
	 * @param element object of the verify
	 * @param version of the searched object 
	 * @return a boolean, true if the version of the object searched is older or equal than the object 
	 * in the specified position, false instead
	 */
	public static boolean verifyVersion(Object element, int version)
	{
		boolean found=false;
		Method m=null;
		int ver;
		
		try 
		{
			m=element.getClass().getDeclaredMethod("getVersion");
			ver=(Integer)m.invoke(element);
			if(ver>=version)
				found=true;
		} 
		catch (SecurityException e) {e.printStackTrace();} 
		catch (NoSuchMethodException e) {e.printStackTrace();} 
		catch (IllegalArgumentException e) {e.printStackTrace();} 
		catch (IllegalAccessException e) {e.printStackTrace();} 
		catch (InvocationTargetException e) {e.printStackTrace();}
		
		return found;
	}
	
	

	/**
	 * This is a searching method which return only the index of the folder in which the class searched is saved
	 * @param list of the object trough which do the searching
	 * @param param list of the parameters that identify the class to search
	 * @param version version class
	 * @return the index of the folder
	 * @throws RemoteException
	 */
	public static int getFolderIndex(ArrayList<Object> list,ArrayList<String> param, int version) throws RemoteException
	{
		Object element;
		int position;
		boolean found=false;
		
		//define the index of the folder if the file is saved on server,  if this value remain -1 the object is not saved on server 
		int result=-1; 
		
		position=ListSearchingManager.searchClassIntoList(list, param);
		element= list.get(position);
		if(position!=-1)
		{	
			found=ListSearchingManager.verifyVersion(element, version);
			
			//the class in the position i has the same or better version then the searched class 
			if(found)
			{
				result=getPathObject(element);
			}
		}
		
		return result;	
	}
	
	
	/**
	 * Method used for getting the path index saved on a private filed of a specific object
	 * @param element function object to query
	 * @return the index of the folder write on the object
	 */
	public static int getPathObject(Object element)
	{
		int result=-1;
		String path=null;
		Method method=null;
		
		//get the path where the file.class is saved, if this string is void, 
		//means that the object is only loaded but there is'nt a file.class saved on server
		try 
		{
			method=element.getClass().getSuperclass().getDeclaredMethod("getPath");
			path=(String)method.invoke(element);
			if(!path.equals(""))//Object is already saved on server
			{
				//get the last character of the path that correspond at the identification number of the folder
				result=path.charAt(path.length()-1)-'0';
			}
		} 
		catch (SecurityException e) {e.printStackTrace();} 
		catch (NoSuchMethodException e) {e.printStackTrace();} 
		catch (IllegalArgumentException e) {e.printStackTrace();} 
		catch (IllegalAccessException e) {e.printStackTrace();} 
		catch (InvocationTargetException e) {e.printStackTrace();}
		
		return result;
	}



}
