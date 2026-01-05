<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    String accion = request.getParameter("accion");
    if (accion == null) {
        accion = "lectura";
    }
    boolean habilitado = "escritura".equals(accion);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Tratamiento Ficheros</title>
</head>
<body>
    <form action="ServletFich" method="get">
        <h1 align="center">TRATAMIENTO FICHEROS</h1><br>

        <label>Formato del fichero:</label>
        <select name="formato">
            <option value="RDF">RDF</option>
            <option value="XLS">XLS</option>
            <option value="CSV">CSV</option>
            <option value="JSON">JSON</option>
            <option value="XML">XML</option>
        </select><br><br>

        <label>¿Qué quiere hacer con el fichero?</label><br>
        <input type="radio" name="accion" value="lectura" <%= "lectura".equals(accion) ? "checked" : "" %>> Lectura<br>
        <input type="radio" name="accion" value="escritura" <%= "escritura".equals(accion) ? "checked" : "" %>> Escritura<br><br>

        <label>DATO 1:</label><input type="text" name="dato1" <%= habilitado ? "" : "disabled" %>><br>
        <label>DATO 2:</label><input type="text" name="dato2" <%= habilitado ? "" : "disabled" %>><br>
        <label>DATO 3:</label><input type="text" name="dato3" <%= habilitado ? "" : "disabled" %>><br>
        <label>DATO 4:</label><input type="text" name="dato4" <%= habilitado ? "" : "disabled" %>><br>
        <label>DATO 5:</label><textarea cols="10" rows="3" name="dato5" <%= habilitado ? "" : "disabled" %>></textarea><br>
        <label>DATO 6:</label><input type="text" name="dato6" <%= habilitado ? "" : "disabled" %>><br><br>

        <input type="submit" name="boton" value="Enviar">
    </form>
    
    <%
        String mensaje = (String) request.getAttribute("mensaje");
        if (mensaje != null) {
    %>
       		<p style="color:red; text-align:center;"><%= mensaje %></p>
    <%
        }
    %>
    
    <script>
 		// Obtiene todos los radio buttons con nombre "accion"
		const radios = document.getElementsByName("accion");
 		
		// Obtiene todos los campos de texto y textarea del formulario
		const campos = document.querySelectorAll('input[type="text"], textarea');
		
		// Función que habilita o deshabilita los campos según la acción seleccionada
		function actualizarCampos() {
			// Comprueba si el radio seleccionado tiene valor "escritura"
		    const esEscritura = Array.from(radios).find(r => r.checked).value === "escritura";
			
		 	// Recorre todos los campos y los habilita o deshabilita según la acción
		    campos.forEach(c => c.disabled = !esEscritura);
		}
		
		// Añade un listener a cada radio para actualizar los campos cuando cambie la selección
		radios.forEach(r => r.addEventListener("change", actualizarCampos));
		
		// Llama a la función al cargar la página para inicializar el estado de los campos
		actualizarCampos(); 
	</script>
</body>
</html>
