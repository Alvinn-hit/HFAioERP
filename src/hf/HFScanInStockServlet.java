package hf;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class HFScanInStockServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public HFScanInStockServlet() {
		super();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			HFData hfdata = HFData.newInstance();
			response.setContentType("text/json; charset=utf-8");
			PrintWriter out = response.getWriter();
			out = response.getWriter();
			out.println(hfdata.exec(request));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		 this.doGet(request, response);
	}

	

}
