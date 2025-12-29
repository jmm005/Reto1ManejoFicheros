package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String mensaje = "";
		String envio = "";

		String accion = request.getParameter("accion");
		String formato = request.getParameter("formato");

		String dato1 = request.getParameter("dato1");
		String dato2 = request.getParameter("dato2");
		String dato3 = request.getParameter("dato3");
		String dato4 = request.getParameter("dato4");
		String dato5 = request.getParameter("dato5");
		String dato6 = request.getParameter("dato6");

		try {

			//ESCRITURA
			if ("escritura".equals(accion)) {

				if (dato1.isEmpty() || dato2.isEmpty() || dato3.isEmpty()
						|| dato4.isEmpty() || dato5.isEmpty() || dato6.isEmpty()) {

					mensaje = "(*) Los campos no pueden estar vacíos";
					envio = "TratamientoFich.jsp";
					request.setAttribute("mensaje", mensaje);
					request.getRequestDispatcher(envio).forward(request, response);

				} else {
					request.setAttribute("dato1", dato1);
					request.setAttribute("dato2", dato2);
					request.setAttribute("dato3", dato3);
					request.setAttribute("dato4", dato4);
					request.setAttribute("dato5", dato5);
					request.setAttribute("dato6", dato6);

					envio = "AccesoDatosA.jsp";
					request.getRequestDispatcher(envio).forward(request, response);
				}

			}
			//LECTURA
			else {

				if ("CSV".equalsIgnoreCase(formato)) {
					leerCSV(request, response);
					return;
				//EL resto de formatos a rellenar
				} else if ("XLS".equalsIgnoreCase(formato)) {
					

				} else if ("JSON".equalsIgnoreCase(formato)) {
					

				} else if ("XML".equalsIgnoreCase(formato)) {
					

				} else if ("RDF".equalsIgnoreCase(formato)) {
					

				} else {
					throw new Exception("Formato no reconocido");
				}
				
				//Esto es para que funcione a pesar de no haber rellenado todos los formatos
				//todavia, una vez rellenados, esto se elimina
				mensaje = "Formato seleccionado pero no implementado todavía";
				request.setAttribute("mensaje", mensaje);
				request.getRequestDispatcher("TratamientoFich.jsp").forward(request, response);
			}

		} catch (Exception e) {
			request.setAttribute("mensajeError", e.getMessage());
			request.getRequestDispatcher("Error.jsp").forward(request, response);
		}
	}
	
	
	
	//Lectura de CSV --> Sandra
	private void leerCSV(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String rutaCSV = getServletContext().getRealPath("/datos/datos.csv");
		List<String[]> datos = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(rutaCSV))) {
			String linea;

			while ((linea = br.readLine()) != null) {
				datos.add(linea.split(","));
			}

			request.setAttribute("mensaje", "Lectura del fichero CSV correcta");
			request.setAttribute("datosCSV", datos);

			request.getRequestDispatcher("TratamientoFich.jsp").forward(request, response);

		} catch (Exception e) {
			throw new ServletException("Error leyendo el fichero CSV");
		}
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
