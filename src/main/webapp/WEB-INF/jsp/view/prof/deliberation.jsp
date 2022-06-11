<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>


<%--<s:authorize access="isAuthenticated()">
<jsp:include page="../fragments/userheader.jsp" />
	You are connected with:
	<s:authentication property="principal.username" /> <br>
	Your Email : <s:authentication property="principal.email" /><br>
	Your First Name : <s:authentication property="principal.firstName" /><br>
	Your Last name : <s:authentication property="principal.LastName" /><br>
	<jsp:include page="../fragments/userfooter.jsp" />
	<section class="home-section">
		<div class="home-content">
			<i class='bx bx-menu' ></i>
			<span class="text">Welcome Prof <s:authentication property="principal.firstName" /></span>
		</div>

</s:authorize>--%>

<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style1.css">
	<!-- Fontawesome CDN Link -->
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>

<body>
<div class="container">
	<div class="forms">
		<div class="form-content">
			<div class="login-form">
				<div class="title add-margin">Delibration Form</div>
				<form action="${pageContext.request.contextPath}/prof/exportfile" method="get">
					<div class="input-boxes">
						<div class="input-box">
							<box-icon name='select-multiple' animation='tada' color='#0bad39' ></box-icon>
							<select class="combobox2" name="niveau">
								<option value="#">Choose level</option>
								<option value="GI3">GI3</option>
								<option value="GI2">GI2</option>
								<option value="GI1">GI1</option>
								<option value="GC1">GC1</option>
								<option value="GC2">GC2</option>
								<option value="GC3">GC3</option>
							</select>
						</div>
						<div class="input-box">
							<box-icon name='calendar-event' animation='tada' color='#0bad39' ></box-icon>
							<select class="combobox" name="year">
								<option value="#">Année Scolaire</option>
								<option value="2021/2022">2021/2022</option>
								<option value="2020/2021">2020/2021</option>
								<option value="2019/2020">2019/2020</option>
								<option value="2018/2019">2018/2019</option>
								<option value="2017/2018">2017/2018</option>
							</select>
						</div>
						<div class="button input-box">
							<input type="submit" class="btn" value="Export File">
						</div>
						<div class="text sign-up-text">You ll get an excel file contains what you requested !</div>
					</div>
				</form>
			</div>
			</form>
		</div>
	</div>


	<input type="checkbox" id="flip">
	<div class="cover">
		<div class="front">
			<img class="diceImg" src="${pageContext.request.contextPath}/resources/img/img.jpg" alt="">
		</div>
		<div class="back">
			<img class="backImg" src="" alt="">
			<div class="text">
				<span class="text-1">Complete miles of journey <br> with one step</span>
				<span class="text-2">Let's get started</span>
			</div>
		</div>
	</div>
</div>
</div>



<script src="https://unpkg.com/boxicons@2.1.2/dist/boxicons.js"></script>
</body>
</html>







