var clinicName = '';
var clinicDescription = '';
var clinicLocation = '';
$.ajax({
	type : 'GET',
	url : "/getClinicInfo",
	dataType : "json",
	headers:{
		'token':localStorage.getItem('token')
	},
	success : function(successData) {
		clinicName = successData.name;
		clinicDescription = successData.description;
		clinicLocation = successData.address;
		$("#yourClinic").html("Your Clinic is <b>"+ clinicName + "</b>");
		$("#clinicDescription").html("Description: <b>" + clinicDescription + "</b>");
		$("#clinicLocation").html("Its address is <b>"+ clinicLocation + "</b>");
	},
	error : function(XMLHttpRequest, textStatus, errorThrown) {
		if(textStatus=="401"){			
			window.location.href = "../whatAreYou";
		}
	}
});