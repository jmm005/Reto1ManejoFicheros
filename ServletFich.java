package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;

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
		
		// Variables donde se guardan el mensaje y a dónde se envía la información
		String mensaje = "";
		String envio = "";

		// Variables donde se guardan los parámetros accion y formato de 'TratamientoFich.jsp'
		String accion = request.getParameter("accion");
		String formato = request.getParameter("formato");

		// Variables donde se guardan los datos de los inputs y el textarea de 'TratamientoFich.jsp'
		String dato1 = request.getParameter("dato1");
		String dato2 = request.getParameter("dato2");
		String dato3 = request.getParameter("dato3");
		String dato4 = request.getParameter("dato4");
		String dato5 = request.getParameter("dato5");
		String dato6 = request.getParameter("dato6");

		// Si el código se ejecuta
		try {

			//ESCRITURA
			if ("escritura".equals(accion)) {

				// Si las variables donde se guardan los datos están vacías
				if (dato1.isEmpty() || dato2.isEmpty() || dato3.isEmpty()
						|| dato4.isEmpty() || dato5.isEmpty() || dato6.isEmpty()) {

					// Mensaje de error
					mensaje = "(*) Los campos no pueden estar vacíos";
					// Valor de la variable "envio"
					envio = "TratamientoFich.jsp";
					// Da el valor de "mensaje" al atributo "mensaje" del request
					request.setAttribute("mensaje", mensaje);
					// Redirige a la página de la variable envio
					request.getRequestDispatcher(envio).forward(request, response);

				} else {
					// Escritura RDF --> Juan
					// Si el formato elegido es RDF
					if("RDF".equalsIgnoreCase(formato)) {
						// Crea un modelo RDF por defecto usando Jena
						Model model = ModelFactory.createDefaultModel();
						// Obtiene la ruta del fichero RDF
						String rutaRDF = "C:/Reto1ManejoFicheros/datos.rdf";
						
						// Variable donde se guarda la ruta del fichero
						File fichero = new File(rutaRDF);
						
						// Variable donde se guarda la ruta de la carpeta donde está el fichero
					    File carpeta = fichero.getParentFile();

						// Si la carpeta no existe
						if (!carpeta.exists()) {
							// Crea la carpeta
						    carpeta.mkdirs(); 
						}
						
						// Si existe, lee el contenido RDF/XML en el modelo
						if(fichero.exists()) {
						    try(FileInputStream fis = new FileInputStream(fichero)) {
						        model.read(fis, null, "RDF/XML");
						    // Si hay error al leer el fichero
						    } catch(IOException e) {
						    	// Se muestra un mensaje de error en 'Error.jsp'
						    	request.setAttribute("mensaje", e.getMessage());
						    	// Valor de la variable "envio"
								envio = "Error.jsp";
								// Redirige a la página cuyo valor es el valor de "envio"
								request.getRequestDispatcher(envio).forward(request, response);
						    }
						}
						
						// Define un namespace para los recursos RDF
						String ns = "https://reto1ManejoFicheros/";
						
						// Crea un nuevo recurso RDF único usando la marca de tiempo
						Resource r = model.createResource(ns + "registro_" + System.currentTimeMillis());
						
						// Añade propiedades al recurso con los valores recibidos
						r.addProperty(model.createProperty(ns, "dato1"), dato1);
						r.addProperty(model.createProperty(ns, "dato2"), dato2);
						r.addProperty(model.createProperty(ns, "dato3"), dato3);
						r.addProperty(model.createProperty(ns, "dato4"), dato4);
						r.addProperty(model.createProperty(ns, "dato5"), dato5);
						r.addProperty(model.createProperty(ns, "dato6"), dato6);
						
						// Guarda el modelo actualizado de nuevo en el fichero RDF
						try(FileOutputStream fos =  new FileOutputStream(rutaRDF)) {
							// Escritura en formato RDF/XML
							model.write(fos, "RDF/XML");
						// Si hay problemas al guardar el fichero
						}catch(IOException e) {
							// Se muestra un mensaje de error en 'Error.jsp'
							request.setAttribute("mensajeError", e.getMessage());
							// Valor de la variable envio
							envio = "Error.jsp";
							// Redirige a la página cuyo valor es el valor de "envio"
							request.getRequestDispatcher(envio).forward(request, response);
						}
						
						// Guarda el valor de los datos en atributos del request
						request.setAttribute("dato1", dato1);
						request.setAttribute("dato2", dato2);
						request.setAttribute("dato3", dato3);
						request.setAttribute("dato4", dato4);
						request.setAttribute("dato5", dato5);
						request.setAttribute("dato6", dato6);
						
						// Valor de la variable "envio"
						envio = "AccesoDatosA.jsp";
						// Redirige a la página cuyo valor es el valor de "envio"
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
					

				// Si el formato elegido es RDF
				} else if ("RDF".equalsIgnoreCase(formato)) {
					// Llama al método leerRDF
					leerRDF(request, response);
					return;
				} else {
					throw new Exception("Formato no reconocido");
				}
				
				//Esto es para que funcione a pesar de no haber rellenado todos los formatos
				//todavia, una vez rellenados, esto se elimina
				mensaje = "Formato seleccionado pero no implementado todavía";
				request.setAttribute("mensaje", mensaje);
				request.getRequestDispatcher("TratamientoFich.jsp").forward(request, response);
			}

		// Si el código no se ejecuta
		} catch (Exception e) {
			// Se muestra un mensaje en 'Error.jsp' con el error
			request.setAttribute("mensajeError", e.getMessage());
			// Valor de la variable "envio"
			envio = "Error.jsp";
			// Redirige a la página cuyo valor es el valor de "envio"
			request.getRequestDispatcher(envio).forward(request, response);
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
	
	// Lectura de RDF --> Juan
	private void leerRDF(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// Obtiene la ruta del fichero RDF 
		String rutaRDF = "C:/Reto1ManejoFicheros/datos.rdf";
		
		// Variable donde se guarda la ruta del fichero
		File fichero = new File(rutaRDF);
		
		// Variable donde se guarda la ruta de la carpeta donde está el fichero
	    File carpeta = fichero.getParentFile();
	    
	    // Crea la carpeta si no existe
	    if (!carpeta.exists()) {
	        carpeta.mkdirs();
	    }

	    // Crea el fichero si no existe
	    if (!fichero.exists()) {
	    	// Crea el fichero con lo mínimo de RDF si no existe
	    	try (FileOutputStream fos = new FileOutputStream(fichero)) {
	            fos.write((
	                "<?xml version=\"1.0\"?>\n" +
	                "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n" +
	                "         xmlns:ex=\"https://reto1ManejoFicheros/\">\n" +
	                "</rdf:RDF>"
	            ).getBytes());
	        }
	    }

		// Crea un modelo RDF por defecto usando Jena
		Model model = ModelFactory.createDefaultModel();

		// Abre el fichero RDF y carga su contenido en el modelo
		try(FileInputStream fis = new FileInputStream(rutaRDF)) {
			// Lee el RDF en formato XML
		    model.read(fis, null, "RDF/XML");
		// Si hay problemas al leer el fichero
		} catch(IOException e) {
			// Se muestra un mensaje de error en 'Error.jsp'
			request.setAttribute("mensajeError", e.getMessage());
			// Redirigo a 'Error.jsp'
			request.getRequestDispatcher("Error.jsp").forward(request, response);
		}
		
		// Crea una lista para almacenar los datos de cada recurso RDF
		List<String[]> datos = new ArrayList<>();

		// Define un namespace para los recursos RDF
		String ns = "https://reto1ManejoFicheros/";

		// Extrae dato1 a dato6 de todos los registros en el modelo RDF
		ResIterator resIt = model.listSubjects();
		
		// Recorre todos los recursos encontrados
		while(resIt.hasNext()) {
			// Obtiene el siguiente recurso
		    Resource res = resIt.nextResource();
		    
		    // Obtiene las propiedades de los recursos y si no existen se asigna una cadena vacía
		    String d1 = res.getProperty(model.createProperty(ns, "dato1")) != null ? res.getProperty(model.createProperty(ns, "dato1")).getString() : "";
		    String d2 = res.getProperty(model.createProperty(ns, "dato2")) != null ? res.getProperty(model.createProperty(ns, "dato2")).getString() : "";
		    String d3 = res.getProperty(model.createProperty(ns, "dato3")) != null ? res.getProperty(model.createProperty(ns, "dato3")).getString() : "";
		    String d4 = res.getProperty(model.createProperty(ns, "dato4")) != null ? res.getProperty(model.createProperty(ns, "dato4")).getString() : "";
		    String d5 = res.getProperty(model.createProperty(ns, "dato5")) != null ? res.getProperty(model.createProperty(ns, "dato5")).getString() : "";
		    String d6 = res.getProperty(model.createProperty(ns, "dato6")) != null ? res.getProperty(model.createProperty(ns, "dato6")).getString() : "";

		    // Añade los datos del registro a la lista 'datos'
		    datos.add(new String[]{d1, d2, d3, d4, d5, d6});
		}
		
		// Da el valor a los atributos "datosRDF" y "mensaje" de la request
		request.setAttribute("mensaje", "Lectura del fichero RDF correcta");
		request.setAttribute("datosRDF", datos);

		// Valor de la variable "envio"
		String envio = "TratamientoFich.jsp"; 
		// Redirige a la página cuyo valor es el valor de "envio"
		request.getRequestDispatcher(envio).forward(request, response);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
