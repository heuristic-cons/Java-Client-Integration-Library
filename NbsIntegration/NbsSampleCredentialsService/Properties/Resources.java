package NbsSampleCredentialsService.Properties;

import NbsSampleCredentialsService.*;

//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//     Runtime Version:4.0.30319.42000
//
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------




/** 
   A strongly-typed resource class, for looking up localized strings, etc.
*/
// This class was auto-generated by the StronglyTypedResourceBuilder
// class via a tool like ResGen or Visual Studio.
// To add or remove a member, edit your .ResX file then rerun ResGen
// with the /str option, or rebuild your VS project.
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [System.CodeDom.Compiler.GeneratedCodeAttribute("System.Resources.Tools.StronglyTypedResourceBuilder", "4.0.0.0")][System.Diagnostics.DebuggerNonUserCodeAttribute()][System.Runtime.CompilerServices.CompilerGeneratedAttribute()] internal class Resources
public class Resources
{

	private static System.Resources.ResourceManager resourceMan;

	private static System.Globalization.CultureInfo resourceCulture;

//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1811:AvoidUncalledPrivateCode")] internal Resources()
	public Resources()
	{
	}

	/** 
	   Returns the cached ResourceManager instance used by this class.
	*/
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)] internal static System.Resources.ResourceManager ResourceManager
	public static System.Resources.ResourceManager getResourceManager()
	{
		if (resourceMan == null)
		{
			System.Resources.ResourceManager temp = new System.Resources.ResourceManager("NbsSampleCredentialsService.Properties.Resources", Resources.class.GetTypeInfo().Assembly);
			resourceMan = temp;
		}
		return resourceMan;
	}

	/** 
	   Overrides the current thread's CurrentUICulture property for all
	   resource lookups using this strongly typed resource class.
	*/
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)] internal static System.Globalization.CultureInfo Culture
	public static System.Globalization.CultureInfo getCulture()
	{
		return resourceCulture;
	}
	public static void setCulture(System.Globalization.CultureInfo value)
	{
		resourceCulture = value;
	}

	/** 
	   Looks up a localized string similar to The service cannot be null..
	*/
	public static String getDataTable_DataManagementServiceCannotBeNull()
	{
		return getResourceManager().GetString("DataTable_DataManagementServiceCannotBeNull", resourceCulture);
	}

	/** 
	   Looks up a localized string similar to The service name is invalid..
	*/
	public static String getDataTable_DataManagementServiceNameInvalid()
	{
		return getResourceManager().GetString("DataTable_DataManagementServiceNameInvalid", resourceCulture);
	}

	/** 
	   Looks up a localized string similar to The table name is invalid..
	*/
	public static String getDataTable_DataTableNameInvalid()
	{
		return getResourceManager().GetString("DataTable_DataTableNameInvalid", resourceCulture);
	}

	/** 
	   Looks up a localized string similar to BulkRequest type must be {0}..
	*/
	public static String getDataTable_RequestTypeException()
	{
		return getResourceManager().GetString("DataTable_RequestTypeException", resourceCulture);
	}

	/** 
	   Looks up a localized string similar to The transaction context must be initialized over an instance of a resource manager..
	*/
	public static String getTransactionManager_TContextInitializationError()
	{
		return getResourceManager().GetString("TransactionManager_TContextInitializationError", resourceCulture);
	}
}