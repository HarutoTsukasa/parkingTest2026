# API de Sistema de Gestión de Parqueadero

Esta API proporciona endpoints RESTful para realizar operaciones CRUD. Sistema web para la administración de un parqueadero que permite gestionar vehículos, tarifas, registros de entrada/salida y generar reportes. Desarrollado con Spring Boot, Java 25, MySQL, JPA/Hibernate, Thymeleaf y una interfaz moderna con Bootstrap 5, DataTables y gráficos dinámicos.

##✨ Características 

- **Gestión de Vehículos:** CRUD de vehículos con placa única, tipo (CARRO, MOTOCICLETA, BICICLETA, EBIKE), marca y modelo.

- **Tarifas Personalizables:** Configuración de tarifas por hora y por día para cada tipo de vehículo.

- **Registro de Entrada/Salida:** Control de tiempo de estadía, cálculo automático de costos según tarifas.

- **Validaciones:** Evita duplicados, registros activos simultáneos, integridad referencial.

- **Dashboard Interactivo:** Estadísticas en tiempo real, mapa de espacios, tabla de actividad reciente con DataTables.

- **Temas Claro/Oscuro:** Interfaz adaptable con persistencia de preferencia.

- **API REST:** Endpoints documentados para integración con otros sistemas.

## 🛠️ Tecnologías Utilizadas

- Spring Boot	4.0.5	Framework principal
- Java	25	Lenguaje de programación
- MySQL	8.x	Base de datos relacional
- Spring Data JPA	-	Persistencia y ORM
- Hibernate	-	Implementación de JPA
- Thymeleaf	-	Motor de plantillas para vistas
- Lombok	1.18.44	Reducción de código boilerplate
- Bootstrap	5.3.3	Diseño responsive y componentes UI
- DataTables	2.0.8	Tablas dinámicas
- Maven Wrapper	3.3.4	Gestión de dependencias sin Maven local

## 📦 Requisitos Previos

Asegúrate de tener instalado lo siguiente:

- JDK 25 (o superior compatible)

- MySQL Server (versión 8.x recomendada)

- Git (opcional, para clonar el repositorio)

No es necesario tener Maven instalado, ya que el proyecto incluye el Maven Wrapper (mvnw / mvnw.cmd).

## ⚙️ Configuración del Proyecto
1. Clonar el repositorio
2. **Configurar la base de datos:** Crea una base de datos en MySQL con el nombre parqueadero_db
3. Configurar el archivo application.properties
4. Configurar propiedad server.port en application.properties 
Si deseas cambiar el puerto del servidor
5. **La aplicación estará en:** http://localhost:8080 puerto por defecto


## 🔌 API REST Endpoints

### Vehículos (/api/vehiculos)

- GET	/	Listar todos los vehículos	
- GET	/{id}	Obtener vehículo por ID	
- GET	/placa/{placa}	Obtener por placa	
- POST	/	Crear nuevo vehículo	VehiculoDTO
- PUT	/{id}	Actualizar vehículo	VehiculoDTO
- DELETE	/{id}	Eliminar vehículo (si no tiene registros)	


### Tarifas (/api/tarifas)

- GET	/	Listar todas las tarifas	
- GET	/{id}	Obtener por ID	
- GET	/tipo/{tipo}	Obtener por tipo de vehículo	
- POST	/	Crear nueva tarifa	TarifaDTO
- PUT	/{id}	Actualizar tarifa	TarifaDTO
- DELETE	/{id}	Eliminar tarifa (si no hay vehículos asociados)	


### Registros (/api/registros)

- GET	/activos	Listar registros activos (vehículos dentro)	
- GET	/	Listar todos los registros	
- POST	/entrada	Registrar entrada de un vehículo	RegistroEntradaDTO (solo placa)
- PUT	/salida/{placa}	Registrar salida y calcular costo	

### 🚀 Ejemplo de JSON para creación de vehículos
```json
{
  "placa": "ABC123",
  "tipo": "MOTOCICLETA",
  "marca": "Honda",
  "modelo": "Wave S"
}
```

## 🗄️ Base de Datos

Las tablas se generan automáticamente con Hibernate. El modelo consta de:

-    **vehiculos:** almacena la información de los vehículos.

-    **tarifas:** configuración de precios por tipo de vehículo.

-    **registros:** control de entradas y salidas (relación con vehículos).

**Relaciones:**

-    Un Vehiculo puede tener muchos Registro.

-    Un Registro pertenece a un Vehiculo.

-    Tarifa se relaciona con TipoVehiculo (enum) de forma unívoca.

## 🌐 Interfaz Web

La interfaz está construida con Thymeleaf y ofrece un dashboard interactivo:

-    **Estadísticas:** total de espacios, ocupación, ingresos del día.

-    **Mapa de espacios:** simulación visual de ocupación.

-    **Tabla de actividad reciente:** muestra los últimos movimientos con opción de búsqueda y ordenamiento (DataTables).

-    **Accesos rápidos:** botones para acciones frecuentes.

-    **Selector de tema:** cambia entre modo oscuro y claro.

Puedes acceder directamente desde la raíz (/).

## 📧 Contacto

- **Desarrollado por:** [Néstor Javier Riaño Nossa / HarutoTsukasa].
- Para dudas o sugerencias, abre un issue en el repositorio.