var clinicId = '';
$.ajax({
	type : 'GET',
	url : "/getClinicInfo",
	dataType : "json",
	headers:{
		'token':localStorage.getItem('token')
	},
	success : function(successData) {
		clinicId = successData.id;
		$("#clinicId").val(clinicId);
	},
	error : function(XMLHttpRequest, textStatus, errorThrown) {
		if(textStatus=="401"){			
			window.location.href = "../whatAreYou";
		}
	}
});