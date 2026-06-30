// ==================== NAVBAR Y SESIÓN ====================

document.addEventListener('DOMContentLoaded', () => {
    const bar = document.getElementById('bar');
    const close = document.getElementById('close');
    const navbar = document.getElementById('navbar');

    if (bar && close && navbar) {
        bar.addEventListener('click', () => navbar.classList.add('active'));
        close.addEventListener('click', () => navbar.classList.remove('active'));
    }
    actualizarBadgeCarrito(); 
    mostrarUsuario();

    // Marcar link activo del navbar según la página actual
    const currentPath = window.location.pathname;
    document.querySelectorAll('.navbar a').forEach(link => {
        const linkPath = link.getAttribute('href');
        if (linkPath && currentPath.endsWith(linkPath.replace('../', '').replace('./', ''))) {
            link.classList.add('nav-activo');
        }
    });
});

function mostrarUsuario() {
    const usuario = JSON.parse(localStorage.getItem('usuario'));
    const navbar = document.getElementById('navbar');
    if (!navbar) return;

    let usuarioItem = navbar.querySelector('.usuario-item');

    if (usuario) {
        ocultarLinksAuth(navbar);

        if (!usuarioItem) {
            usuarioItem = document.createElement('li');
            usuarioItem.className = 'usuario-item';
            const carritoItem = navbar.querySelector('li:has(i.fa-shopping-cart), li:has(i.fa-shopping-bag)');
            if (carritoItem) {
                carritoItem.parentNode.insertBefore(usuarioItem, carritoItem);
            } else {
                navbar.appendChild(usuarioItem);
            }
        }

       const enSubcarpeta = window.location.pathname.includes('/pages/');
const rutaAdmin = enSubcarpeta ? 'admin.html' : 'pages/admin.html';

usuarioItem.innerHTML = `
    <div class="usuario-dropdown">
        <button class="usuario-btn">
            <i class="fas fa-user-circle"></i>
            <span>${usuario.nombre || usuario.email}</span>
            <i class="fas fa-chevron-down usuario-chevron"></i>
        </button>
        <div class="usuario-popover">
    ${usuario.roles && usuario.roles.some(r => r.nombre === 'ROLE_ADMIN') ? `
            <a href="${rutaAdmin}" class="popover-admin">
               <i class="fas fa-cog"></i> Panel admin
            </a>` : ''}
            <button class="popover-logout">
                <i class="fas fa-sign-out-alt"></i> Cerrar sesión
            </button>
        </div>
    </div>
`;
        // Eventos directos, sin onclick en el HTML
        usuarioItem.querySelector('.usuario-btn').addEventListener('click', (e) => {
            e.stopPropagation();
            usuarioItem.querySelector('.usuario-dropdown').classList.toggle('open');
        });

        usuarioItem.querySelector('.popover-logout').addEventListener('click', () => {
            logout();
        });

    } else {
        if (usuarioItem) usuarioItem.remove();
        mostrarLinksAuth(navbar);
    }
}

function ocultarLinksAuth(navbar) {
    navbar.querySelectorAll('a').forEach(link => {
        if (link.textContent.includes('Iniciar Sesión') ||
            link.textContent.includes('Registrarse') ||
            link.textContent.includes('Login') ||
            link.textContent.includes('Registro')) {
            link.parentElement.style.display = 'none';
        }
    });
}

function mostrarLinksAuth(navbar) {
    navbar.querySelectorAll('a').forEach(link => {
        if (link.textContent.includes('Iniciar Sesión') ||
            link.textContent.includes('Registrarse') ||
            link.textContent.includes('Login') ||
            link.textContent.includes('Registro')) {
            link.parentElement.style.display = 'block';
        }
    });
}

function logout() {
    localStorage.removeItem('usuario');
    localStorage.removeItem('carritoId');
    mostrarUsuario();

    const currentPage = window.location.pathname;
    if (currentPage.includes('/pages/')) {
        window.location.href = '../index.html';
    } else {
        window.location.href = 'index.html';
    }
}

// Cerrar popover al clickear afuera
document.addEventListener('click', () => {
    document.querySelectorAll('.usuario-dropdown.open').forEach(d => {
        d.classList.remove('open');
    });
});


function actualizarBadgeCarrito() {
    const carritoId = localStorage.getItem('carritoId');
    if (!carritoId) return;

    fetch(`http://localhost:8080/carritos/${carritoId}`)
        .then(res => res.ok ? res.json() : null)
        .then(carrito => {
            if (!carrito) return;
            const lineas = carrito.lineaCarrito || [];
            const total = lineas.length;
            ['carrito-badge', 'carrito-badge-mobile'].forEach(id => {
                const badge = document.getElementById(id);
                if (!badge) return;
                if (total > 0) {
                    badge.textContent = total;
                    badge.style.display = 'flex';
                } else {
                    badge.style.display = 'none';
                }
            });
        })
        .catch(() => {});
}