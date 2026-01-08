<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Datos</title>
</head>
<%
	String dato1 = (String) request.getAttribute("dato1");
	String dato2 = (String) request.getAttribute("dato2");
	String dato3 = (String) request.getAttribute("dato3");
	String dato4 = (String) request.getAttribute("dato4");
	String dato5 = (String) request.getAttribute("dato5");
	String dato6 = (String) request.getAttribute("dato6");
%>
<body>
	<form action="TratamientoFich.jsp">
		<table border="1">
			<tr>
				<td>DATO 1</td>
				<td>DATO 2</td>
				<td>DATO 3</td>
				<td>DATO 4</td>
				<td>DATO 5</td>
				<td>DATO 6</td>
			</tr>
			<tr>
				<td><%= dato1 %></td>
				<td><%= dato2 %></td>
				<td><%= dato3 %></td>
				<td><%= dato4 %></td>
				<td><%= dato5 %></td>
				<td><%= dato6 %></td>
			</tr>
		</table>
		<input type="submit" name="boton" value="Volver">
	</form>
</body>
</html>