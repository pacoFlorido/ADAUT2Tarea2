#Aplicación con conexión a Base de Datos
___
##Tema escogido para la realización de esta Práctica
He escogido el tema de una maratón en la cual participan corredores pertenecientes
a un equipo.

##Requisitos
- Java
- Mysql

El proyecto está compilado en la versión 20 del jdk de java, para ejecutar él .jar
deberás tener como mínimo esa version instalada en tu terminal.

##Configuración
Para poner en marcha el proyecto tendrás que tener un servidor de 
base de datos iniciado. 

Una vez iniciado el servidor tendremos que ejecutar
el fichero de creación de la base de datos "maratón" llamado `DDLmaratonDB.sql`
que puedes encontrar en esta misma carpeta.

Además, si quieres tener algunos corredores y equipos de ejemplo puedes ejecutar
el archivo `DMLmaraton.sql` que también se encuentra en esta carpeta.

##Funcionamiento
El proyecto consiste en varios menus para introducir, modificar y eliminar
datos en la base de datos "maratón". 

Además, esta base de datos contiene una tabla
donde se almacenarán los usuarios del proyecto. 

La primera vez que iniciemos el 
proyecto no habrá ningún usuario creado, tendrás que crearte uno mediante la opción
1 del primer menu: `Resgistrate`. Una vez creado el usuario, la segunda vez que quieras
entrar ya podrás seleccionar opción 2 del primer menu: `Iniciar Sesión`, e iniciar sesión
con el usuario y contraseña que hayas introducido al registrarte.

Una vez dentro de la aplicación ya podrás utilizar las distintas opciones que 
ofrece la misma.

##Autor
Francisco Florido Quesada 2ºDAM
