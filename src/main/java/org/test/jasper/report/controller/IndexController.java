package org.test.jasper.report.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsMultiFormatView;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.view.JasperViewer;

@Controller
public class IndexController {
	
	@Autowired 
	private ApplicationContext appContext;
	
	@RequestMapping(value="report/{type}")
	public ModelAndView index(@PathVariable(value="type") String type, HttpServletResponse response){
		ModelAndView mav =  new ModelAndView("");
		try{
//			JRDataSource JRdataSource = new JRBeanCollectionDataSource(usersList);
			
			String report="Report1.jrxml";
			Map<String, String> parameters = new HashMap<String, String>();
			Properties headers = new Properties();
			String fileName = "Report";
		    headers.put("Content-Disposition", "attachment; filename=" + fileName + "." + type);
		    JasperReportsMultiFormatView view = new JasperReportsMultiFormatView();
		    view.setJdbcDataSource(getMySQLDataSource());
		    view.setUrl("classpath:" + report);
		    view.setHeaders(headers);
		    parameters.put("format", type);
		    view.setApplicationContext(appContext);
		    return new ModelAndView(view, parameters);
			 
//			if (type.equals("html")){
//				HtmlExporter exporter = new HtmlExporter(DefaultJasperReportsContext.getInstance());
//				exporter.setExporterInput(new SimpleExporterInput(jp));
//				exporter.setExporterOutput(new SimpleHtmlExporterOutput(response.getWriter()));
//				exporter.exportReport();
////				JasperViewer.viewReport(jp);
//			}else if(type.equals("pdf")){
//			    final OutputStream outStream = response.getOutputStream();
//			    JasperExportManager.exportReportToPdfStream(jp, outStream);
//				response.setContentType("application/x-pdf");
//			    response.setHeader("Content-disposition", "inline; filename=report.pdf");
//			}else if(type.equals("xlsx")){
//				JRXlsxExporter exporter = new JRXlsxExporter();
//		        exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jp);
//		        exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, "report.xlsx");
//		        exporter.exportReport();
//		        exporter.setExporterOutput(response.getOutputStream());
//			    final OutputStream outStream = response.getOutputStream();
//			    JasperExportManager.export(jp, outStream);
//				response.setContentType("application/x-pdf");
//			    response.setHeader("Content-disposition", "inline; filename=helloWorldReport.pdf");
//			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return mav;
	}
	
	public static DataSource getMySQLDataSource() {
		MysqlDataSource mysqlDS = null;
		mysqlDS = new MysqlDataSource();
		mysqlDS.setURL("jdbc:mysql://localhost:3306/jasper");
		mysqlDS.setUser("sandeep");
		mysqlDS.setPassword("sandeep");
		return mysqlDS;
	}
	
	@RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public void home( HttpServletResponse response){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jasper", 
					"sandeep" ,"sandeep");
			String report="/Users/sandeep/eclipse-workspace/jasper-reporting/src/main/resources/Report1.jrxml";
			JasperReport jasperreport = JasperCompileManager.compileReport(report);
			JasperPrint jp = JasperFillManager.fillReport(jasperreport, null,conn);
			HtmlExporter exporter = new HtmlExporter(DefaultJasperReportsContext.getInstance());
			exporter.setExporterInput(new SimpleExporterInput(jp));
			exporter.setExporterOutput(new SimpleHtmlExporterOutput(response.getWriter()));
			exporter.exportReport();
		}catch(Exception ex){
			ex.printStackTrace();
		}
    }

}
