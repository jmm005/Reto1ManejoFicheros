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

				} else if ("JSON".equalsIgnoreCase(formato)) {
					escribirJSON(request);
					
					// Pasar datos al request para visualizarlos en AccesoDatosA.jsp 
					request.setAttribute("dato1", dato1);
					request.setAttribute("dato2", dato2);
					request.setAttribute("dato3", dato3);
					request.setAttribute("dato4", dato4);
					request.setAttribute("dato5", dato5);
					request.setAttribute("dato6", dato6);

					request.setAttribute("mensaje", "Escritura en JSON realizada correctamente");
					request.getRequestDispatcher("AccesoDatosA.jsp").forward(request, response);
				} else {
					// Escritura RDF --> Juan
					// Si el formato elegido es RDF
					if("RDF".equalsIgnoreCase(formato)) {
						// Crea un modelo RDF por defecto usando Jena
						Model model = ModelFactory.createDefaultModel();
						// Obtiene la ruta del fichero RDF
						String rutaRDF = "C:/ficheros/parking-movilidad-reducida.rdf";
						
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
					} else if ("CSV".equalsIgnoreCase(formato)) {

    				// Escritura CSV
   					 escribirCSV(request);

    					request.setAttribute("dato1", dato1);
    					request.setAttribute("dato2", dato2);
    					request.setAttribute("dato3", dato3);
    					request.setAttribute("dato4", dato4);
    					request.setAttribute("dato5", dato5);
  						request.setAttribute("dato6", dato6);

    					mensaje = "Escritura en CSV realizada correctamente";
    					request.setAttribute("mensaje", mensaje);

   						envio = "AccesoDatosA.jsp";
    					request.getRequestDispatcher(envio).forward(request, response);

					
					} else if ("JSON".equalsIgnoreCase(formato)) {
    					// Llama al método leerJSON 
    					leerJSON(request, response);
    					return;
					} else {

					    // Otros formatos (aún no implementados)
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

	    String rutaCSV = "C:/ficheros/parking-movilidad-reducida.csv";
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
	
	// Escritura de CSV --> Sandra
	private void escribirCSV(HttpServletRequest request)
        throws IOException {

	    String rutaCSV = "C:/ficheros/parking-movilidad-reducida.csv";
	
	    File fichero = new File(rutaCSV);
	    File carpeta = fichero.getParentFile();
	
	    if (!carpeta.exists()) {
	        carpeta.mkdirs();
	    }
	
	    if (!fichero.exists()) {
	        fichero.createNewFile();
	    }
	
	    try (FileOutputStream fos = new FileOutputStream(fichero, true)) {
	        String linea = String.join(",",
	                request.getParameter("dato1"),
	                request.getParameter("dato2"),
	                request.getParameter("dato3"),
	                request.getParameter("dato4"),
	                request.getParameter("dato5"),
	                request.getParameter("dato6")
	        );
	
	        fos.write((linea + System.lineSeparator()).getBytes());
	    }
	}

	
	// Lectura de RDF --> Juan
	private void leerRDF(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// Obtiene la ruta del fichero RDF 
		String rutaRDF = "C:/ficheros/parking-movilidad-reducida.rdf";
		
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
		// Lectura de JSON --> [Iván]
		private void leerJSON(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {

			String rutaJSON = "C:/ficheros/parking-movilidad-reducida.json";
			File fichero = new File(rutaJSON);
			List<String[]> datos = new ArrayList<>();

			// 1. Verificar si el fichero existe
			if (!fichero.exists()) {
				request.setAttribute("mensaje", "El fichero JSON no existe.");
				request.getRequestDispatcher("TratamientoFich.jsp").forward(request, response);
				return;
			}

			try (FileReader reader = new FileReader(fichero)) {
				// Usamos Gson para parsear el archivo a una lista de arrays de Strings
				// Importante: Asegúrate de tener gson-2.x.x.jar en tu carpeta lib
				com.google.gson.Gson gson = new com.google.gson.Gson();
				java.lang.reflect.Type listType = new com.google.gson.reflect.TypeToken<ArrayList<String[]>>(){}.getType();
				
				datos = gson.fromJson(reader, listType);

				if (datos == null) {
					datos = new ArrayList<>();
				}

				// 2. Pasar los datos a la vista según el flujo solicitado 
				request.setAttribute("mensaje", "Lectura del fichero JSON correcta");
				request.setAttribute("datosJSON", datos);

				// 3. Redirigir a la página de tratamiento 
				request.getRequestDispatcher("TratamientoFich.jsp").forward(request, response);

			} catch (Exception e) {
				// En caso de error, enviar a la página de error del flujo 
				request.setAttribute("mensajeError", "Error procesando JSON: " + e.getMessage());
				request.getRequestDispatcher("Error.jsp").forward(request, response);
			}
		}
		// Escritura de JSON --> [Iván]
			private void escribirJSON(HttpServletRequest request) throws IOException {
				String rutaJSON = "C:/ficheros/parking-movilidad-reducida.json";
				File fichero = new File(rutaJSON);
				com.google.gson.Gson gson = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
				List<String[]> listaDatos;

				// 1. Leer datos existentes para no sobrescribir todo el fichero
				if (fichero.exists() && fichero.length() > 0) {
					try (FileReader reader = new FileReader(fichero)) {
						java.lang.reflect.Type listType = new com.google.gson.reflect.TypeToken<ArrayList<String[]>>(){}.getType();
						listaDatos = gson.fromJson(reader, listType);
						if (listaDatos == null) listaDatos = new ArrayList<>();
					}
				} else {
					listaDatos = new ArrayList<>();
					fichero.getParentFile().mkdirs(); // Crear carpetas si no existen 
				}

				// 2. Crear el nuevo registro con los datos del formulario
				String[] nuevoRegistro = {
					request.getParameter("dato1"),
					request.getParameter("dato2"),
					request.getParameter("dato3"),
					request.getParameter("dato4"),
					request.getParameter("dato5"),
					request.getParameter("dato6")
				};
				listaDatos.add(nuevoRegistro);

				// 3. Guardar la lista actualizada en el fichero
				try (FileWriter writer = new FileWriter(fichero)) {
					gson.toJson(listaDatos, writer);
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

