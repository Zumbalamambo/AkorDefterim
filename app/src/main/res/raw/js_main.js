$(function(){
	$('#btnGonder').click(function () {
		MesajGonder($('#txtMesajIcerik').val());
	});
	
	function MesajGonder(MesajIcerik) {
		$.ajax({
			type: "POST",
			url: "/mesaj?icerik=" + MesajIcerik,
			contentType: "application/json; charset=utf-8",
			dataType: "json",
			success: function (data) {
				if (data.Sonuc) {
					alert("Mesaj�n�z g�nderildi..\n\nG�nderilen mesaj: " + data.Icerik);
				}
				else {
					alert("Mesaj�n�z g�nderilemedi..");
				}
			},
			error: function (xmlHttpRequest, status, err) {
				alert(xmlHttpRequest.statusText + ' ' + xmlHttpRequest.status + ' : ' + xmlHttpRequest.responseText);
			}
		});
	}
});