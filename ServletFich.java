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

		if (request.getParameter("accion").equals("escritura")) {
			if (request.getParameter("dato1").isEmpty() && request.getParameter("dato2").isEmpty()
					&& request.getParameter("dato3").isEmpty() && request.getParameter("dato4").isEmpty()
					&& request.getParameter("dato5").isEmpty() && request.getParameter("dato6").isEmpty()) {
				mensaje = "(*) Los campos no pueden estar vacíos";
				envio = "TratamientoFich.jsp";
				request.setAttribute("mensaje", mensaje);
				request.getRequestDispatcher(envio).forward(request, response);
			}
		} else {
			mensaje = "Lectura del fichero correcta";
			request.setAttribute("mensaje", mensaje);
		}

		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().append("<html><head><title>Resultado</title></head><body>")
							.append("<h2>" + mensaje + "</h2>")
							.append("</body></html>");
		response.getWriter().close();

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

	}

}
