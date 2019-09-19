<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">

function validate_required(field,alerttxt){
	with (field){
  		if (value==null||value==""){
  			alert(alerttxt);return false;
    	}
  		else{
    		return true;
    	}
  	}
}

function validate_form(thisform){
	with (thisform){
  		if (validate_required(url,"Please enter URL!")==false){
  	  		url.focus();return false;
  	  	}
 	 }
}

</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Web Page Classification</title>
</head>
<body>
<form action="WebPageClassification" onsubmit="return validate_form(this)" method="post">
<h2>Web Page Classification!</h2>
Enter URL: <input type="text" name="url" id="urlid"></input>
<button type="submit">Submit</button> <br>
(e.g. http://www.xyz.com)<br><br><br>

<% 
if(request.getAttribute("wikiPage") != null){
	String wikiPage = request.getAttribute("wikiPage").toString();
	out.println("Wikipedia Page:    " + wikiPage);
} %>
<br>
<% 
if(request.getAttribute("wikiCategory") != null){
	String wikiCategory = request.getAttribute("wikiCategory").toString();
	out.println("Wikipedia Category: " + wikiCategory);
}

%>
</form>
</body>
</html>