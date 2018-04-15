# Java Tech Test

## Prerequisitos

* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Maven 3](https://maven.apache.org/download.cgi)


## Ejectutar app (o desplegar WAR en cualquier servidor J2EE)
```
mvn jetty:run-war
```


## Consideraciones

* Como BD utiliza un MongoDB embebido el puerto 37700 y el nombre de la BD es mfdb. Para verificar los resultados:
```
mongo --port 37700
use mfdb
db.SiteResult.find({});
```
* Según la documentación no puedo utilizar ningún framewok adicional. Por este motivo no hay logs ni validaciones de tipo @NotNull.

## Author

Xavier Giménez