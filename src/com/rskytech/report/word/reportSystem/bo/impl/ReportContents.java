package com.rskytech.report.word.reportSystem.bo.impl;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class ReportContents {

	/**
	 * @param args
	 */
	
    public void addTablesOfContents()    
    {    
       
    }  
  
	public static void main(String[] args) {
	}
     public  void  CreateContents(String  docfile){
			  
		
		// TODO Auto-generated method stub
		/**启动word进程*/
    		try {
		ActiveXComponent word  = new ActiveXComponent("Word.Application");
		 
		word .setProperty("Visible", new Variant(false));    
		Dispatch docs = word.getProperty("Documents").toDispatch();    
		 
		/**打开word文档*/
	   // String  	docfile  = "e:/aaa1.docx";
	  //  String  	toFile  = "e:/aaab22.docx";
		//Dispatch doc = Dispatch.invoke(docs, "Open", Dispatch.Method, new Object[] {docfile, new Variant(false),new Variant(true) }, new int[1]).toDispatch(); 
		//Dispatch activeDocument = word .getProperty("ActiveDocument").toDispatch();
	    Dispatch  doc = Dispatch.call(docs, "Open", docfile).toDispatch();  
	       // selection = Dispatch.get(word, "Selection").toDispatch();  
	        Dispatch activeDocument = word .getProperty("ActiveDocument").toDispatch();
		
		/**获取目录*/
		Dispatch tablesOfContents = Dispatch.get(activeDocument,"TablesOfContents").toDispatch();
		//stablesOfContents.call(tablesOfContents, "Add");
	
		   /* 取得ActiveDocument、TablesOfContents、range对象 */   
	     
	      Dispatch  selection = word.getProperty("Selection").toDispatch(); 
	      //插入点下移
	      for (int i = 0; i < 24; i++)  
	            Dispatch.call(selection, "MoveDown");  
	      Dispatch range = Dispatch.get(selection, "Range").toDispatch();    
	      /* 增加目录 */     
	      Dispatch.call(tablesOfContents,"Add",range,new Variant(true),new Variant(1),new Variant(3),
	    		  new Variant(true),new Variant(""),new Variant(true),new Variant(true));   

	   
		/**获取第一个目录。若有多个目录，则传递对应的参数*/
		//Variant tablesOfContent = Dispatch.call(tablesOfContents, "Item", new Variant(1)); 
	//	Variant tablesOfContent =  new Variant();
		
		// Dispatch range = Dispatch.get(this.selection, RANGE).toDispatch();
	     //   Dispatch fields = Dispatch.call(this.selection, FIELDS).toDispatch();
	        
	      //  Dispatch.call(fields,ADD, range, new Variant(-1), new Variant(TOC),new Variant(true));
		 
		/**更新目录，有两个方法：Update　更新域，UpdatePageNumbers　只更新页码*/
		//Dispatch toc = tablesOfContent.toDispatch();
		//toc.call(tablesOfContents, "Update");
		 
		/**保存*/
		  // Dispatch.call(doc,   "FileSaveAs", toFile);  
	   // Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[] {filePath, new Variant(0)}                      , new int[1]);
	    Dispatch.call(doc, "Save");  
	    /**关闭word文档*/
		Dispatch.call(doc, "Close", new Variant(false));
		 
		/**退出word进程*/
		word.invoke("Quit", new Variant[] {});
		//Dispatch.invoke(doc, "FileSaveAs", Dispatch.Method, new Object[] {    toFile, new Variant(false) }, new int[1]);
    		}catch (Exception e) {
    			e.printStackTrace();
    		}
		 
		
	}

}
