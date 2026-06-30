const API_URL = "http://localhost:8080";

async function cargarCarrito() {
    const carritoId = localStorage.getItem('carritoId');
    const tbody = document.getElementById('tabla_carrito');

    console.log("=== CARGAR CARRITO ===");
    console.log("CarritoId:", carritoId);

    if (!carritoId) {
        tbody.innerHTML = '<tr><td colspan="6" style="text-align:center; padding:20px;">Tu carrito está vacío</td></tr>';
        actualizarTotal(0);
        return;
    }

    try {
        const response = await fetch(`${API_URL}/carritos/${carritoId}`);
        console.log("Status:", response.status);

        if (!response.ok) {
            tbody.innerHTML = '<tr><td colspan="6" style="text-align:center; padding:20px;">Tu carrito está vacío</td></tr>';
            actualizarTotal(0);
            return;
        }

        const carrito = await response.json();
        console.log("✓ Carrito cargado:", carrito);

        // Los productos están en lineaCarrito
        const lineas = carrito.lineaCarrito || [];
        console.log("✓ Líneas del carrito:", lineas.length);

        tbody.innerHTML = '';

        if (lineas.length === 0) {
            console.log("Sin productos en el carrito");
            tbody.innerHTML = '<tr><td colspan="6" style="text-align:center; padding:20px;">Tu carrito está vacío</td></tr>';
            actualizarTotal(0);
            return;
        }

        lineas.forEach(linea => {
            const p = linea.producto;
            const cantidad = linea.cantidad;
            const subtotal = linea.precioUnitario * cantidad;

            console.log(`✓ Producto: ${p.nombre}, Cantidad: ${cantidad}, ID: ${p.id}`);
            
            tbody.innerHTML += `
                <tr>
                    <td>
                        <button onclick="quitarProducto(${p.id})"
                            style="background:none; border:none; cursor:pointer; color:#d9534f; font-size:18px;">
                            <i class="far fa-times-circle"></i>
                        </button>
                    </td>
                    <td>
                        <img src="${p.imagenUrl || '../img/placeholder.png'}"
                             alt="${p.nombre}" width="70" style="border-radius:4px;">
                    </td>
                    <td>${p.nombre}</td>
                    <td>${formatearPrecio(linea.precioUnitario)}</td>
                    <td>
                        <input type="number" min="1" value="${cantidad}" 
                               onchange="actualizarCantidad(${p.id}, this.value)"
                               style="width:50px; padding:5px;">
                    </td>
                    <td>${formatearPrecio(subtotal)}</td>
                </tr>`;
        });

        actualizarTotal(carrito.costoTotal);

    } catch (error) {
        console.error('Error al cargar carrito:', error);
    }
}

function formatearPrecio(numero) {
    return new Intl.NumberFormat('es-AR', {
        style: 'currency',
        currency: 'ARS',
        minimumFractionDigits: 0,
        maximumFractionDigits: 0
    }).format(numero);
}

function actualizarTotal(total) {
    const totalFormateado = formatearPrecio(total);
    document.getElementById('total-subtotal').textContent = totalFormateado;
    document.getElementById('total-final').textContent = totalFormateado;
}

async function actualizarCantidad(productoId, nuevaCantidad) {
    const carritoId = localStorage.getItem('carritoId');
    if (!carritoId) return;

    if (nuevaCantidad < 1) {
        alert('La cantidad debe ser mayor a 0');
        cargarCarrito();
        return;
    }

    try {
        const response = await fetch(`${API_URL}/carritos/${carritoId}/productos/${productoId}?cantidad=${nuevaCantidad}`, {
            method: 'PATCH'  // ← cambiado de PUT a PATCH
        });

        if (response.ok) {
            cargarCarrito();
        } else {
            alert('Error al actualizar la cantidad');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error al actualizar la cantidad');
    }
}

async function quitarProducto(productoId) {
    const carritoId = localStorage.getItem('carritoId');
    if (!carritoId) return;

    if (!confirm('¿Querés quitar este producto del carrito?')) return;

    try {
        const response = await fetch(`${API_URL}/carritos/${carritoId}/productos/${productoId}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            console.log("✓ Producto eliminado");
            cargarCarrito();
        } else {
            const error = await response.text();
            console.error("✗ Error al quitar:", error);
            alert('Error al quitar el producto: ' + error);
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error al quitar el producto');
    }
}

async function vaciarCarrito() {
    const carritoId = localStorage.getItem('carritoId');
    if (!carritoId) return;

    if (!confirm('¿Querés vaciar todo el carrito?')) return;

    try {
        const response = await fetch(`${API_URL}/carritos/${carritoId}/vaciar`, {
            method: 'DELETE'
        });

        if (response.ok) {
            cargarCarrito();
        } else {
            alert('Error al vaciar el carrito');
        }
    } catch (error) {
        console.error('Error al vaciar el carrito:', error);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    console.log("✓ Página carrito cargada");
    cargarCarrito();

    const bar = document.getElementById('bar');
    const close = document.getElementById('close');
    const navbar = document.getElementById('navbar');
    if (bar && close && navbar) {
        bar.addEventListener('click', () => navbar.classList.add('active'));
        close.addEventListener('click', () => navbar.classList.remove('active'));
    }
});