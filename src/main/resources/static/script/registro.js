const API_URL = "http://localhost:8080";

function onRolChange() {
    const rol = document.getElementById('rol').value;
    document.getElementById('adminWarning').classList.toggle('show', rol === 'ROLE_ADMIN');
}

async function registrar() {
    const nombre    = document.getElementById('nombre').value.trim();
    const email     = document.getElementById('email').value.trim();
    const password  = document.getElementById('password').value;
    const confirmar = document.getElementById('confirmar').value;
    const rol       = document.getElementById('rol').value;
    const error     = document.getElementById('mensajeError');
    const exito     = document.getElementById('mensajeExito');

    error.textContent = '';
    exito.textContent = '';

    // Validaciones
    if (!nombre || !email || !password || !confirmar || !rol) {
        error.textContent = 'Completá todos los campos';
        return;
    }
    if (password.length < 8) {
        error.textContent = 'La contraseña debe tener al menos 8 caracteres';
        return;
    }
    if (password !== confirmar) {
        error.textContent = 'Las contraseñas no coinciden';
        return;
    }

    const nuevoUsuario = {
        nombre,
        email,
        password,
        roles: [{ nombre: rol }]
    };

    try {
        const response = await fetch(`${API_URL}/usuarios`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(nuevoUsuario)
        });

        if (!response.ok) {
            const msg = await response.text();
            error.textContent = msg || 'Error al registrar. Intentá con otro email.';
            return;
        }

        exito.textContent = '¡Cuenta creada exitosamente! Redirigiendo...';
        setTimeout(() => window.location.href = 'login.html', 1500);

    } catch (err) {
        error.textContent = 'No se pudo conectar con el servidor';
        console.error(err);
    }
}


function togglePasswordVisibility(inputId, iconId) {
    const input = document.getElementById(inputId);
    const icon = document.getElementById(iconId);

    if (input.type === 'password') {
        input.type = 'text';
        icon.classList.remove('fa-eye');
        icon.classList.add('fa-eye-slash');
    } else {
        input.type = 'password';
        icon.classList.remove('fa-eye-slash');
        icon.classList.add('fa-eye');
    }
}