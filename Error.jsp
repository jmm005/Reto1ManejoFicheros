<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2 style="color:red; text-align:center;">❌ Error en el tratamiento del fichero</h2>

<p style="text-align:center;">
    <%= request.getAttribute("mensajeError") %>
</p>

<div style="text-align:center;">
    <a href="TratamientoFich.jsp">Volver</a>
</div>
</body>
</html>