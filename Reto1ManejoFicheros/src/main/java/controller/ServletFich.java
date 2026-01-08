package controller; //ARREGLAME LOS ERRORES 

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
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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
						    } catch(IOException e) {
						    	request.setAttribute("mensaje", e.getMessage());
								envio = "Error.jsp";
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
							model.write(fos, "RDF/XML");
						}catch(IOException e) {
							request.setAttribute("mensajeError", e.getMessage());
							envio = "Error.jsp";
							request.getRequestDispatcher(envio).forward(request, response);
						}
						
						request.setAttribute("dato1", dato1);
						request.setAttribute("dato2", dato2);
						request.setAttribute("dato3", dato3);
						request.setAttribute("dato4", dato4);
						request.setAttribute("dato5", dato5);
						request.setAttribute("dato6", dato6);
						
						envio = "AccesoDatosA.jsp";
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
					}
				}
			}

			//LECTURA
			else {

				if ("CSV".equalsIgnoreCase(formato)) {
					leerCSV(request, response);
					return;

				} else if ("JSON".equalsIgnoreCase(formato)) {
					leerJSON(request, response);
					return;

				} else if ("RDF".equalsIgnoreCase(formato)) {
					leerRDF(request, response);
					return;

				} else {
					throw new Exception("Formato no reconocido");
				}
			}

		} catch (Exception e) {
			request.setAttribute("mensajeError", e.getMessage());
			envio = "Error.jsp";
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

	// Lectura de JSON --> Iván
	private void leerJSON(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    String rutaJSON = "C:/ficheros/parking-movilidad-reducida.json";
	    File fichero = new File(rutaJSON);

	    if (!fichero.exists()) {
	        request.setAttribute("mensaje", "El fichero JSON no existe.");
	        request.getRequestDispatcher("TratamientoFich.jsp").forward(request, response);
	        return;
	    }

	    try (FileReader reader = new FileReader(fichero)) {

	        com.google.gson.Gson gson = new com.google.gson.Gson();

	        com.google.gson.JsonArray jsonArray = gson.fromJson(reader, com.google.gson.JsonArray.class);

	        List<com.google.gson.JsonObject> lista = new ArrayList<>();
	        for (int i = 0; i < jsonArray.size(); i++) {
	            lista.add(jsonArray.get(i).getAsJsonObject());
	        }

	        request.setAttribute("datosJSON", lista);
	        request.setAttribute("mensaje", "Lectura del fichero JSON correcta");
	        request.getRequestDispatcher("TratamientoFich.jsp").forward(request, response);

	    } catch (Exception e) {
	        request.setAttribute("mensajeError", "Error procesando JSON: " + e.getMessage());
	        request.getRequestDispatcher("Error.jsp").forward(request, response);
	    }
	}

	// Escritura de JSON --> Iván
	private void escribirJSON(HttpServletRequest request) throws IOException {

	    String rutaJSON = "C:/ficheros/parking-movilidad-reducida.json";
	    File fichero = new File(rutaJSON);
	    com.google.gson.Gson gson = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
	    com.google.gson.JsonArray jsonArray;

	    if (fichero.exists() && fichero.length() > 0) {
	        try (FileReader reader = new FileReader(fichero)) {
	            jsonArray = gson.fromJson(reader, com.google.gson.JsonArray.class);
	            if (jsonArray == null) jsonArray = new com.google.gson.JsonArray();
	        }
	    } else {
	        jsonArray = new com.google.gson.JsonArray();
	        fichero.getParentFile().mkdirs(); // crear carpetas si no existen
	    }

	    com.google.gson.JsonObject nuevoRegistro = new com.google.gson.JsonObject();
	    nuevoRegistro.addProperty("direccion", request.getParameter("dato1"));
	    nuevoRegistro.addProperty("foto", request.getParameter("dato2"));
	    nuevoRegistro.addProperty("longitud",request.getParameter("dato3"));
	    nuevoRegistro.addProperty("latitud",request.getParameter("dato4"));

	    nuevoRegistro.addProperty("dato5", request.getParameter("dato5"));
	    nuevoRegistro.addProperty("dato6", request.getParameter("dato6"));

	    jsonArray.add(nuevoRegistro);

	    try (FileWriter writer = new FileWriter(fichero)) {
	        gson.toJson(jsonArray, writer);
	    }
	}

	// Lectura de RDF --> Juan
	private void leerRDF(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String rutaRDF = "C:/ficheros/parking-movilidad-reducida.rdf";
		Model model = ModelFactory.createDefaultModel();

		try (FileInputStream fis = new FileInputStream(rutaRDF)) {
			model.read(fis, null, "RDF/XML");
		}

		List<String[]> datos = new ArrayList<>();
		String ns = "https://reto1ManejoFicheros/";

		ResIterator resIt = model.listSubjects();
		while (resIt.hasNext()) {
			Resource res = resIt.nextResource();

			String d1 = res.getProperty(model.createProperty(ns, "dato1")) != null ?
					res.getProperty(model.createProperty(ns, "dato1")).getString() : "";

			String d2 = res.getProperty(model.createProperty(ns, "dato2")) != null ?
					res.getProperty(model.createProperty(ns, "dato2")).getString() : "";

			String d3 = res.getProperty(model.createProperty(ns, "dato3")) != null ?
					res.getProperty(model.createProperty(ns, "dato3")).getString() : "";

			String d4 = res.getProperty(model.createProperty(ns, "dato4")) != null ?
					res.getProperty(model.createProperty(ns, "dato4")).getString() : "";

			String d5 = res.getProperty(model.createProperty(ns, "dato5")) != null ?
					res.getProperty(model.createProperty(ns, "dato5")).getString() : "";

			String d6 = res.getProperty(model.createProperty(ns, "dato6")) != null ?
					res.getProperty(model.createProperty(ns, "dato6")).getString() : "";

			datos.add(new String[]{d1, d2, d3, d4, d5, d6});
		}

		request.setAttribute("mensaje", "Lectura del fichero RDF correcta");
		request.setAttribute("datosRDF", datos);
		request.getRequestDispatcher("TratamientoFich.jsp").forward(request, response);
	}
	
//	
//	
//    //Lectura de XML --> Jaime
//				private void leerXML(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
//					String rutaXML = "C:/ficheros/parking-movilidad-reducida.xml";
//				    File fichero = new File(rutaXML);
//				    List<String[]> datos = new ArrayList<>();
//				    
//				    if (fichero.exists()) {
//				        try {
//				            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//				            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//				            Document doc = (Document) dBuilder.parse(fichero);
//				            doc.getDocumentElement().normalize();
//				            
//				            org.w3c.dom.NodeList nList= doc.getElementsByTagName("parking");
//				            
//				            for (int i = 0; i < nList.getLength(); i++) {
//				            	org.w3c.dom.Node nNode= nList.item(i);
//				            	if(nNode.getNodeType()== org.w3c.dom.Node.ELEMENT_NODE) {
//				            		Element e= (Element) nNode;
//				            		String [] fila= new String[6];
//				            		for(int j=0; j<6;j++) {
//				            			fila[j]= e.getElementsByTagName("dato" + (j + 1)).item(0).getTextContent();
//				            		}
//				            		datos.add(fila);
//				            	}
//				            }
//				}catch (Exception e) {
//					request.setAttribute("mensajeError", e.getMessage());
//					request.getRequestDispatcher("Error.jsp").forward(request, response);
//					return;
//				}
//			}
//				    request.setAttribute("mensaje", "Lectura del fichero XML correcta");
//				    request.setAttribute("datosXML", datos);
//				    request.getRequestDispatcher("TratamientoFich.jsp").forward(request, response);   
//				}
//
//
//
//
//
////Escritura de XML Jaime
//				private void escribirXML(HttpServletRequest request) throws Exception {
//				    String rutaXML = "C:/ficheros/parking-movilidad-reducida.xml";
//				    File fichero = new File(rutaXML);
//				    
//				    
//				    if (!fichero.getParentFile().exists()) {
//				        fichero.getParentFile().mkdirs();
//				    }
//
//				    Document doc;
//				    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//
//				    
//				    if (fichero.exists() && fichero.length() > 0) {
//				        doc = builder.parse(fichero);
//				    } else {
//				        doc = builder.newDocument();
//				        doc.appendChild(doc.createElement("parking-movilidad-reducida"));
//				    }
//
//				    
//				    Element parking = doc.createElement("parking");
//				    doc.getDocumentElement().appendChild(parking);
//
//				    for (int i = 1; i <= 6; i++) {
//				        Element dato = doc.createElement("dato" + i);
//				        dato.setTextContent(request.getParameter("dato" + i));
//				        parking.appendChild(dato);
//				    }
//
//				 
//				    javax.xml.transform.Transformer transformer = javax.xml.transform.TransformerFactory.newInstance().newTransformer();
//				    transformer.transform(new javax.xml.transform.dom.DOMSource(doc), new javax.xml.transform.stream.StreamResult(fichero));
//				}
//
//
//
//	

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}

