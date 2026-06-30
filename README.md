# 🛒 Ecommerce API - Spring Boot

API REST para la gestión de un sistema de comercio (e-commerce), desarrollada con **Java 17** y **Spring Boot**. Permite administrar usuarios, roles, categorías, productos y carritos de compra. Incluye un frontend en HTML, CSS y JavaScript, proporcionado por la cursada y modificado para el proyecto.

---

## 📋 Descripción del proyecto

Este proyecto implementa el backend de una aplicación de comercio electrónico, exponiendo una API REST documentada con **Swagger / OpenAPI**, que permite:

- **Gestión de usuarios**: `POST`, `GET`, `DELETE`, con asignación de roles (`ROLE_ADMIN`, `ROLE_USER`, etc.).
- **Gestión de categorías**: CRUD completo de categorías de productos.
- **Gestión de productos**: CRUD completo de productos asociados a una categoría, con control de stock.
- **Gestión de carritos de compra**: CRUD completo, incluyendo creación de carritos vacíos asociados a un usuario y agregado de productos con cálculo automático del costo total y descuento de stock.

Además, el proyecto incluye un **frontend estático básico** (HTML, CSS y JS) ubicado en `src/main/resources/static/`, accesible directamente desde `http://localhost:8080/`.

### 🛠️ Tecnologías utilizadas

| Herramienta | Versión / Detalle |
|---|---|
| Lenguaje | Java 17 |
| Framework | Spring Boot |
| Base de datos | MySQL (MySQL Workbench 8.0) |
| IDE | IntelliJ IDEA |
| Documentación de API | Swagger / OpenAPI |
| Persistencia | Spring Data JPA / Hibernate |
| Build tool | Maven |

---

## ⚙️ Instrucciones para ejecutar la aplicación

### 1. Requisitos previos

- **Java 17** instalado.
- **MySQL Workbench 8.0** (o MySQL Server 8.0) instalado y en ejecución.
- **IntelliJ IDEA** (o cualquier IDE compatible con Maven).

### 2. Crear la base de datos

En MySQL Workbench, ejecutar:

```sql
CREATE DATABASE ecommerce;
```

> No es necesario crear las tablas manualmente: Spring Boot/Hibernate las genera automáticamente al levantar la aplicación (según la configuración de `ddl-auto` en `application.properties`).

### 3. Configurar la conexión a la base de datos

En el archivo `src/main/resources/application.properties`, configurar las credenciales según tu instalación local:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
spring.datasource.username=root
spring.datasource.password=tu_contraseña
```

### 4. Abrir y ejecutar el proyecto en IntelliJ IDEA

1. Abrir IntelliJ IDEA → `File` → `Open` → seleccionar la carpeta del proyecto.
2. Esperar a que se descarguen las dependencias de Maven.
3. Verificar que el **SDK del proyecto sea Java 17** (`File` → `Project Structure` → `Project SDK`).
4. Ejecutar la clase principal (`*Application.java`, la anotada con `@SpringBootApplication`).

### 5. Verificar que la aplicación está corriendo

Una vez iniciada, la aplicación queda disponible en:

```
http://localhost:8080
```

### 6. Acceder a Swagger UI

Toda la API se puede explorar y probar desde Swagger:

```
http://localhost:8080/swagger-ui/index.html
```

Desde ahí se pueden ejecutar todos los endpoints (`POST`, `GET`, `PUT`, `DELETE`, `PATCH`) de forma interactiva.

---

## ℹ️ Nota sobre los roles de usuario

Los roles **se generan automáticamente** al iniciar la aplicación, mediante un `DataInitializer` ubicado en el paquete `configuration/` (`src/main/java/com/techlab/ecommerce/configuration/`). Esta clase inserta los roles base en la base de datos la primera vez que se levanta el proyecto, por lo que:

- ✅ **No es necesario crear los roles manualmente en la base de datos** (ni con scripts SQL ni desde MySQL Workbench).
- ✅ Si borrás los datos de la tabla `roles` y reiniciás la aplicación, el `DataInitializer` los vuelve a crear automáticamente.

> ⚠️ **AVISO IMPORTANTE — Cómo asignar el rol:**
> Al crear un usuario (`POST /usuarios`), el campo `roles` **acepta solo el `nombre` del rol** (no es necesario ni funcional indicar el `id`). Completá el campo `nombre` con uno de los siguientes valores, ya generados automáticamente por el `DataInitializer`:
>
> | Nombre del rol |
> |---|
> | `ROLE_USER` |
> | `ROLE_ADMIN` |
>
> Ejemplo de cómo completar el campo en el `request body`:
> ```json
> "roles": [
>   { "nombre": "ROLE_ADMIN" }
> ]
> ```
> El sistema busca el rol existente en la base de datos por su `nombre` y lo asocia al usuario automáticamente.

---

## 🧪 Ejemplos de uso / datos de prueba

A continuación se detalla el flujo completo de prueba de la API, en el orden correcto para que las relaciones entre entidades funcionen correctamente.

### 1️⃣ Crear un usuario (con rol asignado)

**POST** `/usuarios`

**Request / Response:**
```json
{
  "id": 1,
  "nombre": "Tamara",
  "email": "tamaramicaelacamino@gmail.com",
  "activo": true,
  "fechaCreacion": "2026-06-30T19:29:41.382539",
  "roles": [
    {
      "id": 2,
      "nombre": "ROLE_ADMIN"
    }
  ]
}
```

### 2️⃣ Crear una categoría

**POST** `/categorias`

```json
{
  "id": 1,
  "nombreCategoria": "Tecnologia",
  "descripcionCategoria": "Productos electrónicos como laptops, celulares y accesorios"
}
```

### 3️⃣ Crear un producto

**POST** `/productos`

> Se debe indicar el `categoriaId` correspondiente a una categoría ya existente. El response devuelve el producto junto con el detalle completo de su categoría asociada.

```json
{
  "id": 1,
  "nombre": "Samsung Galaxy A06",
  "descripcion": "4gb ram, memoria interna de 64GB, color negro",
  "precio": 215999,
  "categoriaId": 1,
  "categoria": {
    "id": 1,
    "nombreCategoria": "Tecnologia",
    "descripcionCategoria": "Productos electrónicos como laptops, celulares y accesorios"
  },
  "imagenUrl": "https://http2.mlstatic.com/D_NQ_NP_2X_821107-MLA108497055364_032026-F.webp",
  "stock": 100
}
```

### 4️⃣ Crear un carrito vacío

**POST** `/carritos`

> Solo es necesario indicar el `usuario_id` al que se asociará el carrito. El resto de los campos se completan vacíos hasta que se agreguen productos.

```json
{
  "id": 1,
  "usuario_id": 1,
  "costoTotal": 0,
  "lineaCarrito": []
}
```

### 5️⃣ Agregar un producto al carrito

**POST** `/carritos/{carritoId}/productos/{productoId}`

> Se indican el `carritoId`, el `productoId` y la cantidad solicitada como parámetro. El `costoTotal` se calcula automáticamente y el `stock` del producto se descuenta según la cantidad agregada.

```json
{
  "id": 1,
  "usuario_id": 1,
  "costoTotal": 863996,
  "lineaCarrito": [
    {
      "id": 1,
      "productoId": 1,
      "cantidad": 4,
      "precioUnitario": 215999,
      "producto": {
        "id": 1,
        "nombre": "Samsung Galaxy A06",
        "descripcion": "4gb ram, memoria interna de 64GB, color negro",
        "precio": 215999,
        "categoriaId": 1,
        "categoria": {
          "id": 1,
          "nombreCategoria": "Tecnologia",
          "descripcionCategoria": "Productos electrónicos como laptops, celulares y accesorios"
        },
        "imagenUrl": "https://http2.mlstatic.com/D_NQ_NP_2X_821107-MLA108497055364_032026-F.webp",
        "stock": 96
      }
    }
  ]
}
```

---

## 📂 Estructura del proyecto

```
src/main/java/com/techlab/ecommerce/
├── configuration/      # CORS, ModelMapper, carga inicial de datos (DataInitializer)
├── controllers/api/    # Controllers REST
├── dtos/                # Objetos de transferencia de datos (DTOs)
├── entities/            # Entidades JPA (Usuario, Rol, Categoria, Producto, Carrito, LineaCarrito)
├── exceptions/          # Excepciones personalizadas y manejo global de errores
├── repositories/        # Repositorios Spring Data JPA
└── services/             # Lógica de negocio (interfaces + implementaciones)

src/main/resources/
├── static/               # Frontend (HTML, CSS, JS)
└── application.properties
```

---

## 👩‍💻 Autor
Tamara Camino 
