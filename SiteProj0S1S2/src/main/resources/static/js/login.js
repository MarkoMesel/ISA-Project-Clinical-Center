if(localStorage.getItem('token')!=null){
	window.location.href = "../home";
}

$(document).on('submit', '*[name="loginForm"]',function(e){
	e.preventDefault();
	const form = document.forms['loginForm'];
	const data = formToJSON(form.elements);
	$.ajax({
		type : 'POST',
		url : "/login",
		contentType : 'application/json',
		dataType : "json",
		data : data,
		success : function(successData) {
			localStorage.setItem('token', successData.token);
			window.location.href = "../home";
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			window.location.replace("../login?error");
		}
	});	
});

function formToJSON(form) {
	return JSON.stringify({		
		"email" : form[0].value,
		"password" : form[1].value
	});
}