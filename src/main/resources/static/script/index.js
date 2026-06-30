const API_URL = "http://localhost:8080";
const contenedor = document.querySelector('.productos-container');
let productosGlobales = [];

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
        productosGlobales = productos;

        const cardsHTML = productos.map(({ id, nombre, precio, imagenUrl, stock }) => {
            return `
                <div class="producto">
                    <img src="${imagenUrl || 'img/productos/calular.jpg'}" alt="${nombre}">
                    <div class="producto-descripcion">
                        <h5>${nombre}</h5>
                        <h4>${formatearPrecio(precio)}</h4>
                        <p>Stock: ${stock}</p>
                    </div>
                    <a id="btn-agregar-${id}" class="carrito"
                       style="${stock === 0 ? 'pointer-events:none; opacity:0.5;' : ''}">
                        <i class="fal fa-shopping-cart"></i>
                        ${stock === 0 ? 'Sin stock' : 'Agregar'}
                    </a>
                </div>
            `;
        });

        contenedor.innerHTML = cardsHTML.join('');
        adjuntarEventos();

    } catch (error) {
        console.error("Error al cargar productos:", error);
        contenedor.innerHTML = `
            <p style="text-align:center; padding:20px;">
                Error al cargar los productos.
            </p>`;
    }
}

async function agregarAlCarrito(producto) {
    try {
        const carritoId = await obtenerCarritoId();
        const response = await fetch(
            `${API_URL}/carritos/${carritoId}/productos/${producto.id}?cantidad=1`,
            { method: "POST" }
        );

        if (response.ok) {
            alert(`${producto.nombre} agregado al carrito!`);
        } else {
            const error = await response.text();
            alert(`Error: ${error}`);
        }
    } catch (error) {
        console.error("Error al agregar al carrito:", error);
    }
}

async function obtenerCarritoId() {
    const carritoId = localStorage.getItem('carritoId');
    if (carritoId) return carritoId;

    const response = await fetch(`${API_URL}/carritos`, { method: "POST" });
    const carrito = await response.json();
    localStorage.setItem('carritoId', carrito.id);
    return carrito.id;
}

function adjuntarEventos() {
    productosGlobales.forEach(producto => {
        const boton = document.getElementById(`btn-agregar-${producto.id}`);
        if (boton && producto.stock > 0) {
            boton.addEventListener('click', () => {
                agregarAlCarrito(producto);
            });
        }
    });
}

document.addEventListener('DOMContentLoaded', () => {
    const bar = document.getElementById('bar');
    const close = document.getElementById('close');
    const navbar = document.getElementById('navbar');

    bar.addEventListener('click', () => navbar.classList.add('active'));
    close.addEventListener('click', () => navbar.classList.remove('active'));

    cargarProductos();
});