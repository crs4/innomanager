# Inno Manager Web Portal 

**Autore**: Roberto Demontis
**Email**: demontis@crs4.it
** © CRS4, 2015**

This repository contains the source code and resources of the **"InnoManager"** Web Portal.

It is based on the Entando Platform (http://www.entando.com/) and was developed for the project **INNO** (http://inno.crs4.it/innomanager). 

The web portal permits to read of the GIS layer stored on the INNO database a Couchbase database (http://www.couchbase.com/).  

With Innomanager is possible to see the tiles of a GIS layer and add new data from Esri shapefiles.

The "resources" folder of this distribution also contains the tool "InnoImportShapeFile" 
that allows to import data of large size (more than 100MB ) in a Linux environment using 
the System Shell. 

An example of the web app can be found at (http://inno.crs4.it/innomanager). 

The portal depends on PostgreSQL 9.3 or above, PostGIS, the tool cbdocloader of 
couchbase and was tested on a Linux Ubuntu Server 12.04 in a Tomcat7 web container

**Quick Start in a Linux Environment**

Note: is supposed that a cluster of couchbase composed by one or more nodes 
is up with a configured bucket named "Inno" and that the local server is able 
to connect to these nodes.
 
1. Download the innomanager source code from repository (e.g with clone)

2. Install Couchbase Server 2.2 Community Edition http://www.couchbase.com/download
   the couchbase server can be disabled (not working)
   If the path of cbdocloader differs to /opt/couchbase/bin/tools/cbdocloader you
   have to chnange the org.crs4.entando.innomanager.aps.system.InnoManagerSystemConstants.java
   java class 

3. Configure the filters in the main/filters folder 

4. Configure the psql.psw file in the main/config folder with the postgresql 
connections parameters 

5. Create the innomanagerPort database on postgresql and load the schema and after 
the data using the innomanagerPort*.sql files in the main/db folder.

6. Create the innomanagerServ database on postgresql and load the schema and after 
the data using the innomanagerServ*.sql files in the main/db folder.
 
7. Change the couchbase connector configuration 'innoConnectorConfig' in the 
'sysconfig' table of the innomanagerPort database :

UPDATE sysconfig SET  config = '<?xml version="1.0" encoding="UTF-8"?>
<innoConnectorConfig>
    <bucket name="nome_admin" password="password_admin" >
        <nodes>aserver1.adomain.it,aserver2.adomain.it,aserver3.adomain.it,aserver4.adomain.it</nodes> 
    </bucket>
</innoConnectorConfig>' WHERE item = 'innoConnectorConfig' ; 

8. Compile the project and deploy the WEB-APP (*.war) into Tomcat7

####### Import ShapeFile Data into a couchbase "inno" bucket
 
1. Unzip the InnoImportShapeFile and see the README.txt file

-------------------------------------------------------------------------------------

Questo repository contiene il codice sorgente e le risorse dell'applicazione 
web **"InnoManager"**  sviluppata per il progetto **INNO** (http://inno.crs4.it/innomanager). 

Innomanager è il portale web del progetto Inno. Tramite il portale oltre alle 
informazioni del progetto puoi verificare quali dati sono presenti nel database 
Inno su couchbase (http://www.couchbase.com/), caricare i tasselli di uno strato 
informativo GIS e aggiungerne uno nuovo da un Esri Shapefile di medie dimensioni 
(minore di 80MB)

La cartella "resources" della distribuzione contiene il tool "InnoImportShapeFile" 
che permette di caricare ed elaborare uno shapefile Esri compresso con zip di grandi 
dimensioni dentro il database INNO tramite uno script da eseguire nella shell 
di sistema di Linux. 

Un esempio dell'applicazione si può trovare all'indirizzo http://inno.crs4.it/innomanager

Il portale dipende da PostgreSQL versione 9.3 o superiore, PostGIS e dal tool di 
couchbase cbdocloader.E' stato testato in un ambiente Linux Ubuntu Server 12.04 
nel web container Tomcat7 

**Quick Start in un ambiente Linux**

Note: è supposto sia già presente un cluster di couchbase, composto da uno o più 
nodi e che esista un bucket per il caricamento dei dati e che il server su cui viene
installato il portale riesca a connettersi ai nodi del cluster.
 
1. Scaricare il codice sorgente di Innomanager dal repository  

2. Installare Couchbase Server 2.2 Community Edition http://www.couchbase.com/download
   Il server può essere disabilitato ma il tool cbdocloader deve essere presente 
   
3. Configure the filters in the main/filters folder 

4. Configure the psql.psw file in the main/config folder with the postgresql 
connections parameters 

5. Modificare la classe 
   org.crs4.entando.innomanager.aps.system.InnoManagerSystemConstants.java
   nel caso la path di cbdocloader non sia "/opt/couchbase/bin/tools/cbdocloader"

6. create the innomanagerPort database on postgresql and load the schema and after 
the data using the innomanagerPort*.sql files in the main/db folder.

7. create the innomanagerServ database on postgresql and load the schema and after 
the data using the innomanagerServ*.sql files in the main/db folder.
 
8. modificare la configurazione della connessione con couchbase nella tabella 
"sysconfig" del database innomanagerPort 

UPDATE sysconfig SET  config = '<?xml version="1.0" encoding="UTF-8"?>
<innoConnectorConfig>
    <bucket name="nome_admin" password="password_admin" >
        <nodes>aserver1.adomain.it,aserver2.adomain.it,aserver3.adomain.it,aserver4.adomain.it</nodes> 
    </bucket>
</innoConnectorConfig>' WHERE item = 'innoConnectorConfig' ; 

9. Compilare il progetto ed eseguire il deploy della WEB-APP (*.war) su Tomcat7

####### Import ShapeFile Data into couchbase "inno" bucket
 
1. Unzip the InnoImportShapeFile and see the README.txt file
