const API_URL = "http://localhost:8080";

// ==================== CATEGORÍAS ====================

async function cargarCategorias() {
    try {
        const response = await fetch(`${API_URL}/categorias/lista`);
        const categorias = await response.json();
        const select = document.getElementById('categoriaProducto');
        select.innerHTML = '<option value="">Seleccioná una categoría</option>';
        categorias.forEach(cat => {
            select.innerHTML += `<option value="${cat.id}">${cat.nombreCategoria}</option>`;
        });
    } catch (error) {
        console.error('Error al cargar categorías:', error);
    }
}

async function crearCategoria() {
    const nombre = document.getElementById('nombreCategoria').value.trim();
    const descripcion = document.getElementById('descripcionCategoria').value.trim();
    const mensaje = document.getElementById('mensajeCategoria');

    if (!nombre || !descripcion) {
        mensaje.style.color = 'red';
        mensaje.textContent = 'Completá todos los campos';
        return;
    }

    try {
        const response = await fetch(`${API_URL}/categorias`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ nombreCategoria: nombre, descripcionCategoria: descripcion })
        });

        if (!response.ok) {
            mensaje.style.color = 'red';
            mensaje.textContent = 'Error al crear la categoría';
            return;
        }

        mensaje.style.color = 'green';
        mensaje.textContent = '¡Categoría creada exitosamente!';
        setTimeout(() => { mensaje.textContent = ''; }, 3000);
        document.getElementById('nombreCategoria').value = '';
        document.getElementById('descripcionCategoria').value = '';
        cargarCategorias();

    } catch (error) {
        mensaje.style.color = 'red';
        mensaje.textContent = 'Error al conectar con el servidor';
    }
}

// ==================== PRECIO ====================

function parsearPrecio(valor) {
    const str = String(valor).trim();

    if (str.includes(',')) {
        return parseFloat(str.replace(/\./g, '').replace(',', '.'));
    }

    const puntos = (str.match(/\./g) || []).length;

    if (puntos === 0) return parseFloat(str);

    if (puntos >= 2) return parseFloat(str.replace(/\./g, ''));

    const parteDecimal = str.split('.')[1];
    if (parteDecimal.length === 3) return parseFloat(str.replace('.', ''));

    return parseFloat(str);
}

// ==================== PRODUCTOS ====================

async function cargarProductos() {
    try {
        const response = await fetch(`${API_URL}/productos/lista`);
        const productos = await response.json();
        const tbody = document.getElementById('tablaProductos');

        tbody.innerHTML = '';
        productos.forEach(p => {
            const tr = document.createElement('tr');
            tr.style.borderBottom = '1px solid #ddd';
            tr.innerHTML = `
                <td style="padding:10px;">${p.id}</td>
                <td style="padding:10px;">${p.nombre}</td>
                <td style="padding:10px;" title="${p.descripcion || ''}">${p.descripcion ? (p.descripcion.length > 40 ? p.descripcion.substring(0, 40) + '…' : p.descripcion) : '-'}</td>
                <td style="padding:10px;">$${Number(p.precio).toLocaleString('es-AR')}</td>
                <td style="padding:10px;">${p.stock}</td>
                <td style="padding:10px;">
                    <button class="btn-editar"
                        data-id="${p.id}"
                        data-nombre="${p.nombre}"
                        data-descripcion="${(p.descripcion || '').replace(/"/g, '&quot;')}"
                        data-precio="${p.precio}"
                        data-stock="${p.stock}"
                        data-imagen="${p.imagenUrl || ''}"
                        data-categoria="${p.categoriaId || ''}"
                        style="background:#f0ad4e; color:white; border:none; padding:6px 12px; border-radius:4px; cursor:pointer; margin-right:5px;">
                        Editar
                    </button>
                    <button class="btn-eliminar" data-id="${p.id}"
                        style="background:#d9534f; color:white; border:none; padding:6px 12px; border-radius:4px; cursor:pointer;">
                        Eliminar
                    </button>
                </td>`;
            tbody.appendChild(tr);
        });

        tbody.onclick = manejarClickTabla;

    } catch (error) {
        console.error('Error al cargar productos:', error);
    }
}

function manejarClickTabla(e) {
    const btnEditar   = e.target.closest('.btn-editar');
    const btnEliminar = e.target.closest('.btn-eliminar');

    if (btnEditar) {
        abrirEditar(
            btnEditar.dataset.id,
            btnEditar.dataset.nombre,
            btnEditar.dataset.descripcion,
            btnEditar.dataset.precio,
            btnEditar.dataset.stock,
            btnEditar.dataset.imagen,
            btnEditar.dataset.categoria
        );
    }

    if (btnEliminar) {
        eliminarProducto(btnEliminar.dataset.id);
    }
}

async function crearProducto() {
    const nombre      = document.getElementById('nombreProducto').value.trim();
    const descripcion = document.getElementById('descripcionProducto').value.trim();
    const precioRaw   = document.getElementById('precioProducto').value;
    const precio      = parsearPrecio(precioRaw);
    const stock       = parseInt(document.getElementById('stockProducto').value);
    const imagenUrl   = document.getElementById('imagenProducto').value.trim();
    const categoriaId = document.getElementById('categoriaProducto').value;
    const mensaje     = document.getElementById('mensajeProducto');

    // Limpiar mensaje anterior
    mensaje.textContent = '';

    console.log('Precio ingresado:', precioRaw, '→ parseado:', precio);

    if (!nombre || !descripcion || !categoriaId) {
        mensaje.style.color = 'red';
        mensaje.textContent = 'Completá todos los campos obligatorios';
        return;
    }
    if (isNaN(precio) || precio <= 0) {
        mensaje.style.color = 'red';
        mensaje.textContent = 'El precio debe ser mayor a 0';
        return;
    }
    if (isNaN(stock) || stock < 0) {
        mensaje.style.color = 'red';
        mensaje.textContent = 'El stock no puede ser negativo';
        return;
    }

    try {
        const response = await fetch(`${API_URL}/productos`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                nombre, descripcion, precio, stock, imagenUrl,
                categoriaId: parseInt(categoriaId)
            })
        });

        if (!response.ok) {
            const errorText = await response.text();
            mensaje.style.color = 'red';
            mensaje.textContent = errorText || 'Error al crear el producto';
            return;
        }

        mensaje.style.color = 'green';
        mensaje.textContent = '¡Producto creado exitosamente!';
        setTimeout(() => { mensaje.textContent = ''; }, 3000);
        document.getElementById('nombreProducto').value     = '';
        document.getElementById('descripcionProducto').value = '';
        document.getElementById('precioProducto').value     = '';
        document.getElementById('stockProducto').value      = '';
        document.getElementById('imagenProducto').value     = '';
        document.getElementById('categoriaProducto').value  = '';
        cargarProductos();

    } catch (error) {
        mensaje.style.color = 'red';
        mensaje.textContent = 'Error al conectar con el servidor';
    }
}

async function eliminarProducto(id) {
    if (!confirm('¿Estás seguro de eliminar este producto?')) return;

    try {
        const response = await fetch(`${API_URL}/productos/${id}`, { method: 'DELETE' });

        if (response.ok) {
            cargarProductos();
        } else {
            const txt = await response.text();
            alert('Error al eliminar: ' + response.status + ' ' + txt);
        }
    } catch (error) {
        console.error('Error al eliminar:', error);
        alert('Error al conectar con el servidor: ' + error.message);
    }
}

// ==================== EDITAR PRODUCTO ====================

function abrirEditar(id, nombre, descripcion, precio, stock, imagen, categoriaId) {
    document.getElementById('editId').value          = id;
    document.getElementById('editNombre').value      = nombre;
    document.getElementById('editDescripcion').value = descripcion;
    document.getElementById('editPrecio').value      = precio;
    document.getElementById('editStock').value       = stock;
    document.getElementById('editImagen').value      = imagen;
    document.getElementById('editCategoriaId').value = categoriaId || '';
    document.getElementById('mensajeEditar').textContent = '';
    document.getElementById('modalEditar').style.display = 'flex';
}

function cerrarModal() {
    document.getElementById('modalEditar').style.display = 'none';
}

async function guardarEdicion() {
    const id          = document.getElementById('editId').value;
    const nombre      = document.getElementById('editNombre').value.trim();
    const descripcion = document.getElementById('editDescripcion').value.trim();
    const precioRaw   = document.getElementById('editPrecio').value;
    const precio      = parsearPrecio(precioRaw);
    const stock       = parseInt(document.getElementById('editStock').value);
    const imagenUrl   = document.getElementById('editImagen').value.trim();
    const categoriaId = document.getElementById('editCategoriaId').value;
    const mensaje     = document.getElementById('mensajeEditar');

    if (!nombre || !descripcion) {
        mensaje.style.color = 'red';
        mensaje.textContent = 'Completá todos los campos';
        return;
    }
    if (isNaN(precio) || precio <= 0) {
        mensaje.style.color = 'red';
        mensaje.textContent = 'El precio debe ser mayor a 0';
        return;
    }
    if (isNaN(stock) || stock < 0) {
        mensaje.style.color = 'red';
        mensaje.textContent = 'El stock no puede ser negativo';
        return;
    }

    try {
        const response = await fetch(`${API_URL}/productos/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                nombre, descripcion, precio, stock, imagenUrl,
                categoriaId: categoriaId ? parseInt(categoriaId) : undefined
            })
        });

        if (!response.ok) {
            const errorText = await response.text();
            mensaje.style.color = 'red';
            mensaje.textContent = errorText || 'Error al actualizar';
            return;
        }

        cerrarModal();
        cargarProductos();

    } catch (error) {
        mensaje.style.color = 'red';
        mensaje.textContent = 'Error al conectar con el servidor';
    }
}

// ==================== INIT ====================

document.addEventListener('DOMContentLoaded', () => {
    cargarCategorias();
    cargarProductos();

    const bar    = document.getElementById('bar');
    const close  = document.getElementById('close');
    const navbar = document.getElementById('navbar');
    if (bar && close && navbar) {
        bar.addEventListener('click',   () => navbar.classList.add('active'));
        close.addEventListener('click', () => navbar.classList.remove('active'));
    }
});