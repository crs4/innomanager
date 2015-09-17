#!/bin/bash
#
#
# author Roberto Demontis <demontis@crs4.it>.
#
# - LOCAL_POSTGRES_DB e’ il database postgres usato per le elaborazioni, 
#   l’utente linux che esegue lo script deve essere il poprietario o avere 
#   i permessi di scrittura.
#   ( LOCAL_POSTGRES_DB is the local postgres database where the data are collected. 
#     The linux user should be owner of the database ) 
# - NODE_ADDR è l'indirizzo di un server con un nodo di couchbase 
#   ( NODE_ADDR is a ip address of a couches node )
# - ADMIN è il nome di login dell'amministratore di couchbase 
#   ( ADMIN is the login of the couchbase administrator )
# - ADMIN_PASSWD è la password di amministrazione di couchbase 
#   ( ADMIN_PASSWD is the administrator password )


export LOCAL_POSTGRES_DB=localdbase
export ADMIN=admin
export ADMIN_PASSWD=password
export NODE_ADDR=the.server.ip:8091
