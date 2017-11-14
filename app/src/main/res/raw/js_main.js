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
					alert("Mesajýnýz gönderildi..\n\nGönderilen mesaj: " + data.Icerik);
				}
				else {
					alert("Mesajýnýz gönderilemedi..");
				}
			},
			error: function (xmlHttpRequest, status, err) {
				alert(xmlHttpRequest.statusText + ' ' + xmlHttpRequest.status + ' : ' + xmlHttpRequest.responseText);
			}
		});
	}
});