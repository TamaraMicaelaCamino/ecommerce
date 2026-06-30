const API_URL = "http://localhost:8080";

async function login() {
    const emailInput = document.getElementById('email').value.trim();
    const passwordInput = document.getElementById('password').value;
    const mensaje = document.getElementById('mensajeError');

    if (!emailInput || !passwordInput) {
        mensaje.textContent = 'Completá todos los campos';
        return;
    }

    try {
        // 1. Valida el login en el backend
        const loginResponse = await fetch(
            `${API_URL}/usuarios/login?email=${encodeURIComponent(emailInput)}&password=${encodeURIComponent(passwordInput)}`,
            { method: 'POST' }
        );

        if (!loginResponse.ok) {
            mensaje.textContent = 'Usuario no encontrado';
            return;
        }

        const esValido = await loginResponse.json();

        if (!esValido) {
            mensaje.textContent = 'Contraseña incorrecta';
            return;
        }

        // 2. Si el login es válido, busca los datos del usuario (sin password)
        const usuarioResponse = await fetch(`${API_URL}/usuarios/email/${encodeURIComponent(emailInput)}`);
        const usuario = await usuarioResponse.json();

        localStorage.setItem('usuario', JSON.stringify(usuario));

        const esAdmin = usuario.roles && usuario.roles.some(r => r.nombre === 'ROLE_ADMIN');
        if (esAdmin) {
            window.location.href = 'admin.html';
        } else {
            window.location.href = 'tienda.html';
        }

    } catch (error) {
        mensaje.textContent = 'Error al conectar con el servidor';
        console.error(error);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const bar = document.getElementById('bar');
    const close = document.getElementById('close');
    const navbar = document.getElementById('navbar');
    if (bar && close && navbar) {
        bar.addEventListener('click', () => navbar.classList.add('active'));
        close.addEventListener('click', () => navbar.classList.remove('active'));
    }
});


