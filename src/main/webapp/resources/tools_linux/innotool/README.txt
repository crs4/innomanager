##################################################################
##################################################################
INNO Admin Tools

SO: Linux Ubuntu Server 12.04
 
depends on: postgresql (v:9.3), postgis, gzip, tar,  zip, unzip, 
bash, couchbase (only for the tool cbdocloader) 

dipende da: postgresql (v:9.3), postgis, gzip, tar,  zip, unzip, 
bash, couchbase (solo per lo strumento cbdocloader)

##################################################################

This simple set of script permits to load a gis layer stored 
in a Esri shape file format into a couchbase 
http://www.couchbase.com/ ( a NoSQL database)

The resulting “Inno” layer is a set of JSON documents of 
vectorial tiles, alphanumeric attributes and spatial views (index).

This set of documents is structured to be added in a bucket of 
the couchbase NoSQL database and published via the Inno Restful 
interface (see the inno.zip file). 

The gis data are elaborated and geometries tiled using the PostgreSQL 
dbms with Postgis functionalities using a set of pgsql functions.
(etl_functions.sql file)

CONFIGURATION

To configure the tool first write the correct parameters in the
inno_set_global.sh file
The next step is to create a local postgresql database with postgis 
functionalities and then add to it the etl_functions with the comand:

psql -f /path_to/innotool/etl_functions.sql -d your_database   

EXECUTION

execute the command 

/path_to/innotool/inno_import.sh new_layer_name path_to_compressed_shapefile

parameters: 
 - new_layer_name : The name of the new layer
 - path_to_compressed_shapefile : The compressed shapefile 
 

     
##################################################################

Questo semplice insieme di script permette di caricare un dato gis
nel formato Esri shape file in un database couchbase  
http://www.couchbase.com/ ( un database NoSQL)

Il risultato dell’elaborazione è un insieme di documenti JSON di 
tasselli vettoriali, di dati alfanumerici e degli indici spaziali.

Questo insieme di documenti è strutturato per essere inserito in 
couchbase e pubblicato usando l’interfaccia applicativa di tipo 
RESTFul che si trova nel file “inno.zip”

I dati gis sono elaborati e le geometrie tassellate usando il dbms
PostgreSQL e le funzionalità PostGis usando un insieme di funzioni 
pgsql (file etl_functions.sql)

CONFIGURAZIONE

Per configurare gli strumenti bisogna prima inserire i parametri
corretti nel file inno_set_global.sh 

Lo step successivo è quello di create un database postgresql con 
le funzionalita’ postgis aggiungendovi le funzioni etl tramite
il comando

psql -f /path_to/innotool/etl_functions.sql -d il_tuo_database   

ESECUZIONE

esegue il comando 

/path_to/innotool/inno_import.sh nome_del_nuovo_lager percorso_per_lo_shapefile

parameters: 
 - nome_del_nuovo_layer : il nome del nuovo strato informativo
 - percorso_per_lo_shapefile : il percorso completo dello shapefile 
                               compresso  

###################################################################
 



