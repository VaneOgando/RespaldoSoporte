
function completarGestion(data){

    var status = data.status;

    switch (status) {
        case "start":
            alert("comienza");
            break;
        case "complete":
            //var descargar = document.getElementById("gestionarRecurso:descargar");
            //descargar.click();
            alert("completa")
            break;
        case "success":
            alert("success")
            break;
    }


}