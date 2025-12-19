# spring-security-simple

### Rama main: ``Sistema de seguridad basico``

Creacion de un sistema tanto de autenticacion como de autorizacion basado unicamente en el modulo de seguridad propio
de Spring, ademas, el sistema esta enfocado en stateless para evitar todos los problemas de un sistema basado en sesiones
sustituyendolo por el uso de JWT para el manejo de toda la informacion del usuario en las diferentes request.

El fuerte de este proyecto es el total aprobechamiento de la abstraccion de procesos inplementados dentro del propio
modulo de spring security como es el manejo de cadenas de filtros para poder asegurar la autenticacion y autorizacion
antes del acceso a los recursos de las APIs aumentando la seguridad del sistema.

Desarrolle este proyecto porque opino que para soluciones empresariales a pequeña/mediana escala seria una opcion muy
interesante, y el unico inconveniente que identifique desarrollando la solucion fue la inexistente implementacion de
manejo de JWT no admitidos ya que estos son validos hasta su fecha de expiracion y eso puede no ser lo esperado. Para
solucionarlo, implemente el almacenamiento de estos JWT a modo de entidades para asi aplicar dos casos de uso importantes,
el primero seria el caso de querer no aceptar un JWT valido en un momento especifico, y el otro seria el mantener solo
una sesion iniciada en un dispositivo a la vez para no dejar debilidades en la aplicacion. Estos dos casos de uso se solucionaron
mediante estas mismas entidades, primero agregando tras su generacion y solo permitiendo que haya un JWT a nombre de un usuario,
y asi al estar en la BBDD, un SA podria tener la opcion de cambiar el estado de este a DISABLED para que asi cuando entre
en la cadena de filtros se identifique que no es valido.

Con esta ultima implementacion se podria tomar como un sistema de autorizacion completo, seguro y optimizado mediante el uso
de las propias funcionalidades de spring.

### Rama secundaria: ``Sistema de seguridad avanzado con BDD, y multiples ambientes incluido docker``

En esta segunda rama quise implementar 
