<%@page language="java" contentType="application/x-msdownload"
	import="java.net.URLEncoder,java.io.*" pageEncoding="UTF-8"%>
<%@page import="com.rskytech.ComacConstants"%>
  
<%
	//关于文件下载时采用文件流输出的方式处理：    
	//加上response.reset()，并且所有的％>后面不要换行，包括最后一个；   
	request.setCharacterEncoding("UTF-8");
	response.setCharacterEncoding("UTF-8");
	java.io.OutputStream outp = null;
	java.io.FileInputStream in = null;
	String path = request.getParameter("path");
	String language = request.getParameter("language");
	String filedownload = new String(path.getBytes("ISO-8859-1"),
			"utf-8");
	

	// System.out.println(filedownload);
	try {
		File file = new File(filedownload);
		if (!file.exists()) {
			out.write("<script>alert('文件已被删除!')</script>");
			out.write("<script>window.history.go(-1);</script>");
			return;
		}
		in = new FileInputStream(filedownload);

	} catch (Exception e) {
		out.write("<script>alert('文件已被删除!')</script>");
		out.write("<script>window.history.go(-1);</script>");
		return;
	}

	try {
		response.reset();//可以加也可以不加    
		response.setContentType("application/x-download");

		//application.getRealPath("/main/mvplayer/CapSetup.msi");获取的物理路径
		String filedisplay = path.substring(filedownload
				.lastIndexOf("/"));
		String filedisplay1 = URLEncoder.encode(filedisplay,
				"ISO-8859-1");
		//String filedisplay1 = new String(filedisplay.getBytes("ISO-8859-1"), "utf-8");
		filedisplay1 = filedisplay1.substring(3);
		response.setHeader("Content-Disposition",
				"attachment;filename=" + filedisplay1);

		outp = response.getOutputStream();

		byte[] b = new byte[1024];
		int i = 0;

		while ((i = in.read(b)) > 0) {
			outp.write(b, 0, i);
		}
		//      
		outp.flush();
		//要加以下两句话，否则会报错    
		//java.lang.IllegalStateException: getOutputStream() has already been called for //this response      
		out.clear();
		out = pageContext.pushBody();
	} catch (Exception e) {
		System.out
				.println("111111111111111111111111111111111111111111111111111111111111111111111!");

		return;
	} finally {
		if (in != null) {
			in.close();
			in = null;
		}

	}
%>

