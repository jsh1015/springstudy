<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>패스워드변경</title>
<script>
	function inchk(check){	//변경비밀번호 일치여부
		if(f.pass.value == ""){
			alert("현재 비밀번호를 입력하세요")
			f.pass.focus();
			return false;
		}
		if(f.chgpass.value == ""){
			alert("변경 비밀번호를 입력하세요")
			f.chgpass.focus();
			return false;
		}
		if(f.chgpass.value != f.chgpass2.value){
			alert("변경 비밀번호가 일치하지 않습니다.")
			f.chgpass2.value=""
			f.chgpass2.focus()
			return false
		}
		return true
	}
</script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
	<form action="password.shop" method="post" name="f" onsubmit="return inchk(this)">
	<table>
		<caption>비밀번호 변경</caption>
		<tr><th>현재 비밀번호</th><td><input type="password" name="pass"></td></tr>
		<tr><th>변경 비밀번호</th><td><input type="password" name="chgpass"></td></tr>
		<tr><th>변경 비밀번호 재입력</th><td><input type="password" name="chgpass2"></td></tr>
		<tr><td colspan="2" align="center"><input type="submit" value="비밀번호 변경"></td></tr>
	</table>
	</form>
</body>
</html>