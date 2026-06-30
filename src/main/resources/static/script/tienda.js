const API_URL = "http://localhost:8080";

function formatearPrecio(numero) {
    return new Intl.NumberFormat('es-AR', {
        style: 'currency',
        currency: 'ARS',
        minimumFractionDigits: 0,
        maximumFractionDigits: 0
    }).format(numero);
}

async function cargarProductos() {
    try {
        const response = await fetch(`${API_URL}/productos/lista`);
        const productos = await response.json();
        const container = document.querySelector('.productos-container');

        container.innerHTML = '';

        if (productos.length === 0) {
            container.innerHTML = '<p>No hay productos disponibles.</p>';
            return;
        }

        productos.forEach(p => {
            container.innerHTML += `
                <div class="producto-card">
                    <img src="${p.imagenUrl || '../img/placeholder.png'}" alt="${p.nombre}">
                    <div class="producto-info">
                        <h4>${p.nombre}</h4>
                        <p>${p.descripcion || ''}</p>
                        <div class="producto-precio">
                            <span>${formatearPrecio(p.precio)}</span>
                        </div>
                        <button onclick="agregarAlCarrito(${p.id})" class="btn-agregar">
                            <i class="fa fa-shopping-cart"></i> Agregar al carrito
                        </button>
                    </div>
                </div>`;
        });

    } catch (error) {
        console.error('Error al cargar productos:', error);
    }
}

async function agregarAlCarrito(productoId) {
    console.log("=== AGREGAR AL CARRITO ===");

    const usuario = JSON.parse(localStorage.getItem('usuario'));
    console.log("✓ Usuario:", usuario);

    if (!usuario) {
        alert('Tenés que iniciar sesión para agregar productos al carrito');
        window.location.href = './login.html';
        return;
    }

    if (!usuario.id) {
        console.error('✗ El objeto usuario no tiene id:', usuario);
        alert('Hubo un problema con tu sesión. Por favor, volvé a iniciar sesión.');
        localStorage.removeItem('usuario');
        window.location.href = './login.html';
        return;
    }

    try {
        let carritoId = localStorage.getItem('carritoId');
        console.log("✓ CarritoId actual:", carritoId);

        if (!carritoId || carritoId === "undefined") {
            console.log("⚠ Buscando carrito existente...");

            const responseExistente = await fetch(`${API_URL}/carritos/usuario/${usuario.id}`);

            if (responseExistente.ok) {
                const carritoExistente = await responseExistente.json();
                carritoId = carritoExistente.id;
                console.log("✓ Carrito existente encontrado:", carritoId);
                localStorage.setItem('carritoId', carritoId);
            } else {
                console.log("⚠ Sin carrito, creando uno nuevo...");

                const responseCarrito = await fetch(`${API_URL}/carritos?usuarioId=${usuario.id}`, {
                    method: 'POST'
                });

                console.log("✓ Status crear carrito:", responseCarrito.status);

                if (!responseCarrito.ok) {
                    const errorText = await responseCarrito.text();
                    console.error("✗ Error crear carrito:", errorText);
                    alert('Error al crear el carrito');
                    return;
                }

                const carrito = await responseCarrito.json();
                carritoId = carrito.id;
                console.log("✓ Nuevo CarritoId creado:", carritoId);
                localStorage.setItem('carritoId', carritoId);
            }
        }

        if (!carritoId || carritoId === "undefined") {
            console.error('✗ carritoId inválido después de intentar obtenerlo:', carritoId);
            alert('Hubo un problema con tu carrito. Intentá de nuevo.');
            return;
        }

        const urlProducto = `${API_URL}/carritos/${carritoId}/productos/${productoId}?cantidad=1`;
        console.log("✓ POST a:", urlProducto);

        const response = await fetch(urlProducto, { method: 'POST' });

        console.log("✓ Status respuesta:", response.status);
        const respuestaTexto = await response.text();
        console.log("✓ Respuesta del servidor:", respuestaTexto);

        if (response.ok) {
            console.log("✓ Producto agregado exitosamente");
            mostrarToast('¡Producto agregado al carrito!');
            actualizarBadgeCarrito();
        } else {
            console.error("✗ Error HTTP:", response.status);
            alert('Error al agregar el producto: ' + respuestaTexto);
        }

    } catch (error) {
        console.error('✗ Error general:', error);
        alert('Error al conectar con el servidor');
    }
}
function mostrarToast(mensaje) {
    let toast = document.getElementById('toast-global');
    if (!toast) {
        toast = document.createElement('div');
        toast.id = 'toast-global';
        toast.className = 'toast-custom';
        toast.innerHTML = `<span id="toast-msg"></span>
            <button onclick="this.parentElement.classList.remove('visible')">✕</button>`;
        document.body.appendChild(toast);
    }
    document.getElementById('toast-msg').textContent = mensaje;
    toast.classList.add('visible');
    setTimeout(() => toast.classList.remove('visible'), 3000);
}

document.addEventListener('DOMContentLoaded', () => {
    console.log("✓ Página tienda cargada");
    cargarProductos();
});