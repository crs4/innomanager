
INSERT INTO sysconfig (version, item, descr, config) VALUES ('test', 'innoConnectorConfig', 'Configurazione del client che permette l''accesso al db Inno su couchbase', '<?xml version="1.0" encoding="UTF-8"?>
<innoConnectorConfig>
    <bucket name="myname" password="mypass" >
        <nodes>my.server.it</nodes> 
    </bucket>
</innoConnectorConfig>');

