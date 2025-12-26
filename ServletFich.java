package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class ServletFich
 */
@WebServlet("/ServletFich")
public class ServletFich extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletFich() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String mensaje = "";
		String envio = "";
		String dato1 = request.getParameter("dato1");
		String dato2 = request.getParameter("dato2");
		String dato3 = request.getParameter("dato3");
		String dato4 = request.getParameter("dato4");
		String dato5 = request.getParameter("dato5");
		String dato6 = request.getParameter("dato6");

		if (request.getParameter("accion").equals("escritura")) {
			if (dato1.isEmpty() || dato2.isEmpty() || dato3.isEmpty() || dato4.isEmpty() || dato5.isEmpty()
					|| dato6.isEmpty()) {
				mensaje = "(*) Los campos no pueden estar vacíos";
				envio = "TratamientoFich.jsp";
				request.setAttribute("mensaje", mensaje);
				request.getRequestDispatcher(envio).forward(request, response);
			}else {
				request.setAttribute("dato1", dato1);
				request.setAttribute("dato2", dato2);
				request.setAttribute("dato3", dato3);
				request.setAttribute("dato4", dato4);
				request.setAttribute("dato5", dato5);
				request.setAttribute("dato6", dato6);
				envio = "AccesoDatosA.jsp";
				request.getRequestDispatcher(envio).forward(request, response);
			}
		} else {
			mensaje = "Lectura del fichero correcta";
			request.setAttribute("mensaje", mensaje);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

	}

}
