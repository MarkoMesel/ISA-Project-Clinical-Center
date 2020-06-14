var url = document.URL;
var uId = url.substring(url.lastIndexOf('/') + 1);

if(localStorage.getItem('token')==null){
	window.location.href = "../whatAreYou";
}
else {
	var role = '';
	var verified = '';
	$.ajax({
		type : 'GET',
		url : "/checkIfDoctorCanViewMedicalRecord/"+uId,
		dataType : "text",
		headers:{
			'token':localStorage.getItem('token')
		},
		success : function(successData) {
			var isInUse = successData;
			if(isInUse == 'false')  {
				window.location.href = "../notAuthorized";
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(textStatus=="401"){			
				window.location.href = "../whatAreYou";
			} else {
				window.location.href = "../notAuthorized";
			}
		}
	});
}
