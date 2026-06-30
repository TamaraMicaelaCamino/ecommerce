function mostrarToast(mensaje){
    const toast = document.getElementById('toast');
    const toastMensaje = document.getElementById('toastMensaje');

    toastMensaje.textContent = mensaje;
    toast.classList.add('visible');
    setTimeout(ocultarToast,4500);
}

function ocultarToast(){
    document.getElementById('toast').classList.remove('visible');
}

document.addEventListener('DOMContentLoaded',()=>{
    document.getElementById('btnCerrarToast').addEventListener('click',ocultarToast)
})