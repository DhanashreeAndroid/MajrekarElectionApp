package voterSearch.app.SmdInfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Environment;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Alpha_VoterList_Master {

	private Context context;
	DBAdapter mdbAdapter;
	String PartNo,SrNo, WardNo, AgeRange, VidhansabhaName;
	SharedPreferences pref;
	String strFilter;
	String header = "";
	Boolean isEnglish = true;
	
	Alpha_VoterList_Master(Context _context, String _part, String _srNo, String wardNo, String _strFilter, String _AgeRange, String vidhanName, Boolean isEnglish)
	{
		this.context = _context;
		this.PartNo = _part;
		this.SrNo = _srNo;
		this.WardNo = wardNo;
		this.strFilter = _strFilter;
		this.AgeRange = _AgeRange;
		this.VidhansabhaName = vidhanName;
		this.isEnglish = isEnglish;
	}
	
	public String Get_Display_Format()
	{
		StringBuilder str = new StringBuilder();
		mdbAdapter = new DBAdapter(context);
		mdbAdapter.open();
		Cursor cur;
		String part;
		
		str.append("<html>" +
				"<head>"+
				"<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>"+
				"</head>"+

			"<body> <table id='tbl' border='1' runat='server' width='100%' cellspacing='0' cellpadding='0'>");
		
	    if(PartNo == null){
			cur = mdbAdapter.GetDetails_all_From_Fullvidhansabha();

			str.append("<tr >");
			str.append("<td colspan = '6' align='center'  style = 'font-family: Times New Roman; font-size: 9px;'> " +"Alphabetical Voter List</br>" + VidhansabhaName  + "</br></td>");
			str.append("</tr>");
			str.append("<tr>");
			str.append("<td  align='left' style = 'font-family: Times New Roman; font-size: 9px;'>Part No.</td>");

		}else {
			if (!PartNo.contains("PartNo")) {
				//for combine part No
				cur = mdbAdapter.GetDetails_From_Fullvidhansabha_Combine_PartNo(PartNo, strFilter, AgeRange);

				str.append("<tr >");
				str.append("<td colspan = '6' align='center'  style = 'font-family: Times New Roman; font-size: 9px;'> " + getHeader() + "</br></td>");
				str.append("</tr>");
				str.append("<tr>");
				str.append("<td  align='left' style = 'font-family: Times New Roman; font-size: 9px;'>Part No.</td>");
			} else {
				//for single part No

				part = PartNo.substring(PartNo.indexOf("-") + 1, PartNo.length());
				cur = mdbAdapter.GetDetails_From_Fullvidhansabha_Single_PartNo(part, SrNo, WardNo, strFilter, AgeRange);

				str.append("<tr >");
				str.append("<td colspan = '5' align='center'  style = 'font-family: Times New Roman; font-size: 9px;'> " + getHeader() + "</br></td>");
				str.append("</tr>");
				str.append("<tr>");
			}
		}
	
		str.append("<td  align='left' style = 'font-family: Times New Roman; font-size: 9px;'>Sr. No.</td>");
	    str.append("<td  align='left' style = 'font-family: Times New Roman; font-size: 9px;'>Voter Name</td>");
	    str.append("<td  align='left' style = 'font-family: Times New Roman; font-size: 9px;'>Voter Address</td>");
	    str.append("<td  align='left' style = 'font-family: Times New Roman; font-size: 9px;'>Sex</td>");
	    str.append("<td  align='left' style = 'font-family: Times New Roman; font-size: 9px;'>Age</td>");
	    str.append("</tr>");
	    
		//--------------------------------------------------------------------------
		String Name = "", Address="";    
				
		if(cur != null && cur.moveToFirst())
		{
			do
			{
				if(strFilter.equals("Name"))
				{
					if(isEnglish) {
						Name = cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_FIRST_NAME)) + " " + cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_LASTNAME));
					}else{
						Name = cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_MFIRST_NAME)) + " " + cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_MLASTNAME));
					}
				}
				else
				{
					if(isEnglish) {
						Name = cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_LASTNAME)) + " " + cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_FIRST_NAME));
					}else{
						Name = cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_MLASTNAME)) + " " + cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_MFIRST_NAME));
					}
				}
				if(isEnglish) {
					Address = cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_BUILD_NAME)) + "," +
							cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_AREA_NAME)) + "," +
							cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_HOUSE_NO));
				}else{
					Address = cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_MHOUSE_NO)) + "," +
							cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_MADDRESS));

				}
				str.append("<tr>");
				if (PartNo == null || !PartNo.contains("PartNo"))
				{
					str.append("<td  align='left' style = 'font-family: Times New Roman; font-size: 9px;'>" + cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_PART_NO)) + "</td>");
				}
				str.append("<td  align='left' style = 'font-family: Times New Roman; font-size: 9px;'>" + cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_SR_NO )) + "</td>");
			    str.append("<td  align='left' style = 'font-family: Times New Roman; font-size: 9px;'>"+ Name + "</td>");
			    str.append("<td  align='left' style = 'font-family: Times New Roman; font-size: 9px;'>" + Address +"</td>");
			    str.append("<td  align='left' style = 'font-family: Times New Roman; font-size: 9px;'>" + cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_SEX)) + "</td>");
			    str.append("<td  align='left' style = 'font-family: Times New Roman; font-size: 9px;'>" + cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_AGE)) + "</td>");
			    str.append("</tr>");
						    
			}while (cur.moveToNext());
			
			 str.append("</table> </body></html>");
			 
			cur.close();
			
		}
				
		return str.toString();
	}	
		
	public String getHeader()
	{
		if(strFilter.equals("Surname"))
		{
			header = "Surname wise Alphabetical Voter List</br>" + VidhansabhaName + " (" + PartNo+ ")";
		}
		else if(strFilter.equals("Name"))
		{
			header = "Name wise Alphabetical Voter List</br>" + VidhansabhaName + " (" + PartNo+ ")";
		}
		else if(strFilter.equals("Building"))
		{
			header = "Building wise Alphabetical Voter List</br>" + VidhansabhaName + " (" + PartNo+ ")";
		}
		else if(strFilter.equals("Age"))
		{
			header = "Age wise Alphabetical Voter List</br>" + VidhansabhaName + " (" + PartNo+ ")";
		}
		
		return header;
	}
	
	public String getFileName()
	{
		String Name = null;
		if(strFilter.equals("Surname"))
		{
			Name = "SurnameAVList-" + PartNo;
		}
		else if(strFilter.equals("Name"))
		{
			Name = "NameAVList-" + PartNo;
		}
		else if(strFilter.equals("Building"))
		{
			Name = "BuildingAVList-" + PartNo;
		}
		else if(strFilter.equals("Age"))
		{
			Name = "AgeAVList-" + PartNo;
		}
		
		return Name;
	}
	
	
	//-----------------------------
	
	public String exportDatabaseToCsv() {
		
		 
		 /**First of all we check if the external storage of the device is available for writing.
		  * Remember that the external storage is not necessarily the sd card. Very often it is
		  * the device storage.
		  */
		 String state = Environment.getExternalStorageState();
		 if (!Environment.MEDIA_MOUNTED.equals(state)) { 
		 return "";
		 }
		 else {
		  //We use the Download directory for saving our .csv file.
		     File exportDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);      
		     if (!exportDir.exists()) 
		     {
		         exportDir.mkdirs();
		     }
		     
		     File file = null;
		     PrintWriter printWriter = null;
		     try 
		     {
		      file = new File(exportDir, getFileName() +".csv");
		      file.createNewFile();                
		      printWriter = new PrintWriter(new FileWriter(file));
		               
		         /**This is our database connector class that reads the data from the database.
		          * The code of this class is omitted for brevity.
		          */
		      mdbAdapter = new DBAdapter(context);
				mdbAdapter.open();
				Cursor cur;
				String part;
				
				if (!PartNo.contains("PartNo"))
				{
					//for combine part No
					cur = mdbAdapter.GetDetails_From_Fullvidhansabha_Combine_PartNo(PartNo, strFilter, AgeRange);
				}
				else
				{
					//for single part No
					
					part =  PartNo.substring(PartNo.indexOf("-") + 1, PartNo.length());
					cur = mdbAdapter.GetDetails_From_Fullvidhansabha_Single_PartNo(part, SrNo, WardNo, strFilter, AgeRange);
				}
				
				
			
				//Write the name of the table and the name of the columns (comma separated values) in the .csv file.
		      printWriter.println( getHeader());
		      printWriter.println("Sr. No.,Voter Name,Voter Address,Sex,Age");
		      
		    //--------------------------------------------------------------------------
				String Name = "", Address="";    
						
				if(cur != null && cur.moveToFirst())
				{
					do
					{
						if(strFilter.equals("Name"))
						{
							if(isEnglish) {
								Name = cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_FIRST_NAME)) + " " + cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_LASTNAME));
							}else{
								Name = cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_MFIRST_NAME)) + " " + cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_MLASTNAME));
							}
						}
						else
						{
							if(isEnglish) {
								Name = cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_LASTNAME)) + " " + cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_FIRST_NAME));
							}else{
								Name = cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_MLASTNAME)) + " " + cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_MFIRST_NAME));
							}
						}
						if(isEnglish) {
							Address = cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_BUILD_NAME)) + "," +
									cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_AREA_NAME)) + "," +
									cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_HOUSE_NO));
						}else{
							Address = cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_MHOUSE_NO)) + "," +
									cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_MADDRESS));

						}
					
						//if (!PartNo.contains("PartNo"))
						//{
						//	str.append("<td  align='center' style = 'font-family: Times New Roman; font-size: 9px;'>" + cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_PART_NO)) + "</td>");


						String record =  cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_SR_NO ))
					    				 + "," + Name + "," + Address + "," + cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_SEX)) 
					    				 +"," + cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_AGE)) ;
					   printWriter.println(record); //write the record in the .csv file
					       
					}while (cur.moveToNext());
					
				}
				cur.close();
				mdbAdapter.close();
				
				 
		     }
		    
		    catch(Exception exc) {
		     //if there are any exceptions, return false
		    
		    }
		    finally {
		     if(printWriter != null) printWriter.close();
		    } 
		    
		     return file.getAbsolutePath() ;
		    //If there are no errors, return true.
		   
		 }
		}
	
	//---------------------------
	
	public String exportDatabaseToPdf()  throws FileNotFoundException, DocumentException
	{
		
		
		 String state = Environment.getExternalStorageState();
		 if (!Environment.MEDIA_MOUNTED.equals(state)) { 
		 return "";
		 }
		 else 
		 {
			//We use the Download directory for saving our .csv file.
		     File exportDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);      
		     if (!exportDir.exists()) 
		     {
		         exportDir.mkdirs();
		     }
		    
		     File file = new File(exportDir, getFileName() +".pdf");
		

		     mdbAdapter = new DBAdapter(context);
				mdbAdapter.open();
				Cursor cur;
				String part;
				
				if (!PartNo.contains("PartNo"))
				{
					//for combine part No
					cur = mdbAdapter.GetDetails_From_Fullvidhansabha_Combine_PartNo(PartNo, strFilter, AgeRange);
				}
				else
				{
					//for single part No
					
					part =  PartNo.substring(PartNo.indexOf("-") + 1, PartNo.length());
					cur = mdbAdapter.GetDetails_From_Fullvidhansabha_Single_PartNo(part,SrNo, WardNo, strFilter, AgeRange);
				}
				
				
	    Document document = new Document();  // create the document
	    PdfWriter.getInstance(document, new FileOutputStream(file));
	    document.open();

	    Paragraph p3 = new Paragraph();
	    p3.add( getHeader() +"\n");
	    p3.add("\n");
	    p3.setAlignment(Element.ALIGN_CENTER);
	    document.add(p3);

	   
	    
	    PdfPTable table = new PdfPTable(5);
	    
	    table.addCell("Sr. No");
	    table.addCell("Voter Name               ");
	    table.addCell("Voter Address                                   ");
	    table.addCell("Sex");
	    table.addCell("Age");

	    String Name = "", Address="";    
	    
	    if(cur != null && cur.moveToFirst())
		{
			do
			{
				if(strFilter.equals("Name"))
				{
					if(isEnglish) {
						Name = cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_FIRST_NAME)) + " " + cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_LASTNAME));
					}else{
						Name = cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_MFIRST_NAME)) + " " + cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_MLASTNAME));
					}
				}
				else
				{
					if(isEnglish) {
						Name = cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_LASTNAME)) + " " + cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_FIRST_NAME));
					}else{
						Name = cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_MLASTNAME)) + " " + cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_MFIRST_NAME));
					}
				}
				if(isEnglish) {
					Address = cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_BUILD_NAME)) + "," +
							cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_AREA_NAME)) + "," +
							cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_HOUSE_NO));
				}else{
					Address = cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_MHOUSE_NO)) + "," +
							cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_MADDRESS));

				}
			
				//if (!PartNo.contains("PartNo"))
				//{
				//	str.append("<td  align='center' style = 'font-family: Times New Roman; font-size: 9px;'>" + cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_PART_NO)) + "</td>");


					table.addCell(cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_SR_NO )));
			        table.addCell(Name);
			        table.addCell(Address);
			        table.addCell( cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_SEX)));
			        table.addCell( cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_AGE)));
			        
			}while (cur.moveToNext());
			
		}
	    
	    cur.close();
		mdbAdapter.close();
		
	    document.add(table);
	    document.addCreationDate();
	    document.close(); 
	    
		 return file.getAbsolutePath();
		 }
		
	}
	
	
	//---------------------------
	/*
	public String Get_Data_For_CSV_Format()
	{
		mdbAdapter = new DBAdapter(context);
		mdbAdapter.open();
		Cursor cur;
		StringBuilder sbr = new StringBuilder();
		String part;
		
		if (!PartNo.contains("PartNo"))
		{
			//for combine part No
			cur = mdbAdapter.GetDetails_From_Fullvidhansabha_Combine_PartNo(PartNo, strFilter, AgeRange);	
			sbr.append("PART NO@@@SR_NO@@@Last Name@@@First Name@@@Building Name@@@Area Name@@@House No@@@Sex@@@Age***");
		}
		else
		{
			//for single part No
			part =  PartNo.substring(PartNo.indexOf("-") + 1, PartNo.length());
			cur = mdbAdapter.GetDetails_From_Fullvidhansabha_Single_PartNo(part, strFilter, AgeRange);
			sbr.append("SR_NO@@@Last Name@@@First Name@@@Building Name@@@Area Name@@@House No@@@Sex@@@Age***");
		}
		
		String  Address="";   
			
		
		if(cur != null && cur.moveToFirst())
		{
			do
			{
				if (!PartNo.contains("PartNo"))
				{//for combine part No - part no columan is added
					sbr.append(cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_PART_NO)) + "@@@");
				}
				sbr.append(cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_SR_NO )) + "@@@");
				sbr.append(cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_LASTNAME )) + "@@@");
				sbr.append(cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_FIRST_NAME )) + "@@@");
				Address = cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_BUILD_NAME)) + "," + 
						  cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_AREA_NAME)) + "," +
						  cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_HOUSE_NO));
				sbr.append(Address + "@@@");
				sbr.append( cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_SEX)) + "@@@");
				sbr.append(cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_AGE)) + "***");
			  
			}while (cur.moveToNext());
			
			cur.close();
			
		}
		
		return sbr.toString();
	}
	*/

}
