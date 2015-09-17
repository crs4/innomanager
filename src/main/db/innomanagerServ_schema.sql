--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- Name: postgis; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA public;


--
-- Name: EXTENSION postgis; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION postgis IS 'PostGIS geometry, geography, and raster spatial types and functions';


SET search_path = public, pg_catalog;

CREATE FUNCTION _inno_compact_coord(x integer, y integer) RETURNS text
    LANGUAGE plpgsql
    AS $$DECLARE
result text = '';
BEGIN
     
     IF ( x < 0 OR x = 0 ) THEN 
        result = result || '00';
     ELSE IF ( x < 16 ) THEN 
        result = result || '0';
        result = result ||  to_hex(x);
     ELSE IF ( x > 255 ) THEN 
        result = result || 'ff';
     ELSE result = result ||  to_hex(x);
     END IF;END IF;END IF;
        
     IF ( y < 0 OR y = 0 ) THEN 
        result = result || '00';
     ELSE IF ( y < 16 ) THEN 
        result = result || '0';
        result = result ||  to_hex(y);
     ELSE IF ( y > 255 ) THEN 
        result = result || 'ff';
     ELSE result = result ||  to_hex(y);
     END IF;END IF;END IF;
     RETURN result;
END;
$$;


--
-- Name: _inno_compact_line(geometry); Type: FUNCTION; Schema: public; 
--

CREATE FUNCTION _inno_compact_line(line geometry) RETURNS text
    LANGUAGE plpgsql STRICT
    AS $$DECLARE
      result text = '';
      point geometry;
      x integer; 
      y integer;
BEGIN
   result = '2(';
   FOR i IN 1 .. st_npoints(line)
   LOOP 
       point = st_pointn(line,i);
       x = st_x(point);
       y = st_y(point);
       result = result || _inno_compact_coord(x,y);
   END LOOP;
   result = result || ')';
   RETURN result;
END;
$$;


--
-- Name: _inno_compact_point(geometry); Type: FUNCTION; Schema: public; 
--

CREATE FUNCTION _inno_compact_point(point geometry) RETURNS text
    LANGUAGE plpgsql STRICT
    AS $$DECLARE
      result text = '';
      point geometry;
      x integer; 
      y integer; 
BEGIN
   result = '1(';
   x = st_x(point);
   y = st_y(point);
   result = result || _inno_compact_coord(x,y);
   result = result || ')';
   RETURN result;
END;
$$;


--
-- Name: _inno_compact_poly(geometry); Type: FUNCTION; Schema: public; 
--

CREATE FUNCTION _inno_compact_poly(poly geometry) RETURNS text
    LANGUAGE plpgsql STRICT
    AS $$DECLARE
      result text = '';
      linear geometry;
      point geometry;
      rec record; 
      x integer; 
      y integer;
BEGIN
   result = '3(';
   FOR rec IN SELECT (st_dumprings (poly)).geom
   LOOP 
        linear = st_exteriorring (rec.geom);
        result = result || '(';
        FOR i IN 1 .. st_npoints(linear)
        LOOP 
             point = st_pointn(linear,i);
             x = st_x(point)::integer;
             y = st_y(point)::integer;

             result = result || _inno_compact_coord(x,y);
        END LOOP;
        result = result || ')';
   END LOOP;
   result = result || ')';
   RETURN result;
END;
$$;


--
-- Name: _inno_create_tiles(character varying, integer); Type: FUNCTION; Schema: public; 
--

CREATE FUNCTION _inno_create_tiles(layername character varying, zoom integer) RETURNS text
    LANGUAGE plpgsql
    AS $$DECLARE
    query text;
    type text;
    geom_type integer;
    data text;
    fullname1  text;
    fullname2  text;
    fullname3  text;
    time text := '';
    time1 text := '';
BEGIN
     SELECT clock_timestamp() into time1;
     EXECUTE 'SELECT public.geometrytype(geom) FROM "'||layername||'"."'||layername||'" LIMIT 1' INTO type;
     -- ordinamento delle features e verifica validita' (geometrie semplici)
     IF (  type = 'POLYGON' OR type = 'MULTIPOLYGON' ) THEN 
           geom_type := 3;
     ELSE IF ( type = 'LINESTRING' OR type = 'MULTILINESTRING' ) THEN
           geom_type := 2;
     ELSE IF ( type = 'POINT' OR type = 'MULTIPOINT' ) THEN
           geom_type := 1;
     ELSE  return 'Error 1';
     END IF; END IF; END IF;
     SELECT clock_timestamp() into time;
     RAISE NOTICE 'ZOOM %, START CREATION TILES AT: %', zoom, time;
     EXECUTE 'SELECT public._inno_tile_clipper( '''||layername|| ''','||zoom||', '||geom_type||')';
     SELECT  clock_timestamp() into time;
     RAISE NOTICE 'ZOOM %, CROUD TILES BUILT AT % ', zoom,  time;
     EXECUTE 'SELECT public._inno_tile_assembler('''||layername ||''','||zoom||', '||geom_type||')';
     SELECT  clock_timestamp()  into time;
     RAISE NOTICE 'ZOOM %, TILES COMPILED AT % ', zoom, time; 
     --EXECUTE 'SELECT public._inno_tile_writer('''||layername ||''',''/tmp'','||zoom||')';
     --SELECT  clock_timestamp()  into time;
     --RAISE NOTICE 'ZOOM % TILES WROTE AT % ', zoom, time; 
     --SELECT  clock_timestamp()  into time;
     RETURN 'Success:'||layername||' zoom['||zoom||'] start['||time1||'], end['||time||']';
END;
$$;


--
-- Name: _inno_info_writer(text, text); Type: FUNCTION; Schema: public; 
--

CREATE FUNCTION _inno_info_writer(layername text, path text) RETURNS text
    LANGUAGE plpgsql
    AS $$DECLARE
query text;
query_jsonall text;
query_jsontile text;
rec record;
type text;
json text;
data text;
adder text;
vertices integer;
count integer;
bbox_geom geometry;
bbox_text text;
attrs  text[];
istext text[]; 
levels text[]; 
add    text;
column_name text;
BEGIN 
vertices = 0;
count = 0;
--- INIZIO CREAZIONE JSON del layer
json := '';
--- Controllo sui nomi degli attributi e creazione del json delle informazioni sul layer 
query := 'SELECT column_name, data_type, character_octet_length FROM INFORMATION_SCHEMA.COLUMNS where table_name like ''' 
         ||layername||'%'' AND table_schema = '''||layername||''' AND NOT data_type = ''USER-DEFINED''';
FOR rec in EXECUTE query 
LOOP
    IF NOT json = '' THEN json = json || ','; END IF; 
    -- verifica su nomi riservati ('_innoname_', %'bbox_')
    IF rec.column_name = '_innoname_' OR rec.column_name LIKE '%bbox_' THEN
        column_name = rec.column_name || '_';
        EXECUTE 'ALTER TABLE "'||layername||'"."'||layername||'" RENAME '||rec.column_name||' TO '||column_name;
    ELSE column_name = rec.column_name;
    END IF;
    json := json||'{ "name": "'||column_name||'", "type":"'|| rec.data_type||'"}';
    attrs = attrs || column_name::text;
    IF rec.character_octet_length IS NULL 
    THEN  istext = istext || 'no'::text;
    ELSE  istext = istext || 'yes'::text;
    END IF;   
END LOOP;
IF json = '' THEN json = '{}';  END IF;
-- Calcolo GeoJson da layer extent  
EXECUTE 'SELECT st_asewkt ( ST_SetSRID(ST_Extent(geom), 4326) ) FROM "'||layername||'"."'||layername||'"' into bbox_text;
bbox_geom = st_envelope( st_geomfromewkt ( bbox_text ) ); 
-- Calcolo del numero dei vertici 
EXECUTE ' SELECT sum(st_npoints(geom)) from "'||layername||'"."'||layername||'"' INTO vertices;
-- Calcolo numero di elementi  
EXECUTE 'SELECT count(geom) from "'||layername||'"."'||layername||'"' INTO count;
EXECUTE 'SELECT geometrytype(geom) from "'||layername||'"."'||layername||'" LIMIT 1' INTO type;
json = '{ "_innoname_": "'|| layername||'", "_bbox_":'||ST_AsGeoJson( bbox_geom, 7, 0)||', '
      || '"vertices":'|| vertices||', "count": '||count||', "type": "'||type||'", "attributes": ['||json||'], "levels":[';
--- MANCA Levels: [ zoom, tiles]
adder = '';
FOR level IN 10..17 LOOP
    -- Verifica presenza tabella con tile
query := 'SELECT distinct table_name FROM INFORMATION_SCHEMA.COLUMNS where table_name = ''' 
         ||layername||'_tiles_'||level||''' AND table_schema = '''||layername||'''';
FOR rec in EXECUTE query 
LOOP
    json = json || adder || '{ "zoom":' ||level||',"tiles":['|| _inno_lon2tile ( st_xmin(bbox_geom), level )
                || ','|| _inno_lat2tile ( st_ymin(bbox_geom), level )
                || ','|| _inno_lon2tile ( st_xmax(bbox_geom), level )
                || ','|| _inno_lat2tile ( st_ymax(bbox_geom), level ) || ']}';
    adder = ',';
END LOOP;
END LOOP;
json = json || ']}';

--Scrittura file .json in /tmp/layername/info/docs
EXECUTE 'COPY ( select  '''|| json ||''' ) TO ''' ||path||'/'||layername||'/'||layername||'/docs/'||layername||'.json''' ;
--- FINE CREAZIONE JSON del layer

--- AGGIUNTA DEGLI INDICI 

--- FINE AGGIUNTA DEGLI INDICI 

--- INIZIO SCRITTURA JSON degli elemnenti
query = 'SELECT id, jsontile FROM "' || layername || '"."' || layername || '_info"';
count = 0;
FOR rec IN execute query LOOP
    json = quote_literal(rec.jsontile); 
    query := 'COPY ( SELECT ' || json || '  ) TO ''' || path || '/' || layername || '/' || layername || '/docs/' || layername || ':' || rec.id || '.json''' ;
    EXECUTE query;
    count = count + 1;
END LOOP;
--- FINE SCRITTURA JSON degli elemnenti
RETURN 'Success: '||count||'info json wrote';
END;$$;


--
-- Name: _inno_lat2tile(double precision, integer); Type: FUNCTION; Schema: public; 
--

CREATE FUNCTION _inno_lat2tile(lat double precision, zoom integer) RETURNS integer
    LANGUAGE plpgsql IMMUTABLE
    AS $$BEGIN
    return floor( (1.0 - ln(tan(radians(lat)) + 1.0 / cos(radians(lat))) / pi()) / 2.0 * (1 << zoom) )::integer;
END;
$$;


--
-- Name: _inno_lon2tile(double precision, integer); Type: FUNCTION; Schema: public; 
--

CREATE FUNCTION _inno_lon2tile(lon double precision, zoom integer) RETURNS integer
    LANGUAGE plpgsql IMMUTABLE
    AS $$BEGIN
    return floor( (lon + 180) / 360 * (1 << zoom) )::integer;
END;
$$;


--
-- Name: _inno_prepare_info(text); Type: FUNCTION; Schema: public; 
--

CREATE FUNCTION _inno_prepare_info(layername text) RETURNS text
    LANGUAGE plpgsql
    AS $$DECLARE
query text;
query_jsonall text;
query_jsontile text;
rec record;
type text;
json text;
data text;
adder text;
vertices integer;
count integer;
bbox_geom geometry;
bbox_text text;
attrs  text[];
istext text[]; 
levels text[]; 
add    text;
column_name text;
BEGIN 

--- Controllo sui nomi degli attributi e creazione del json delle informazioni sul layer 
query := 'SELECT column_name, data_type, character_octet_length FROM INFORMATION_SCHEMA.COLUMNS where table_name = ''' 
         ||layername||''' AND table_schema = '''||layername||''' AND NOT data_type = ''USER-DEFINED''';
FOR rec in EXECUTE query 
LOOP
    -- verifica su nomi riservati ('_innoname_', %'bbox_')
    IF rec.column_name = '_innoname_' OR rec.column_name LIKE '%bbox_' THEN
        column_name = rec.column_name || '_';
        EXECUTE 'ALTER TABLE "'||layername||'"."'||layername||'" RENAME '||rec.column_name||' TO '||column_name;
    ELSE column_name = rec.column_name;
    END IF;
    attrs = attrs || column_name::text;
    IF rec.character_octet_length IS NULL 
    THEN  istext = istext || 'no'::text;
    ELSE  istext = istext || 'yes'::text;
    END IF;   
END LOOP;

--- INIZIO CREAZIONE JSON degli elemnenti
json   = '';
query  = ' SELECT b."id" , b."geom4326", ';
query_jsontile = '''{"_' ||layername||'bbox_":''|| st_asGeoJson (st_envelope(geom4326),7,0) ';
query_jsonall =  query_jsontile || ' || '','' ';

FOR j IN 1..array_upper(attrs,1) LOOP
     IF j > 1 THEN 
        query = query||',';
        adder = ','; 
     ELSE 
        adder = ''; 
     END IF;
---  adattamento a json 
     query = query||' CASE WHEN a."'||attrs[j]||'" IS NULL THEN ''''::text ELSE '''||adder||to_json(attrs[j]::text)||':'; 
     IF istext[j]='yes' THEN query=query||'''|| to_json (a."'||attrs[j]||'"::text)'; 
     ELSE query = query||''' || a."'||attrs[j]||'"';
     END IF;
     query = query||' END  as  f' || j ||',';
     adder = ','; 
     query = query ||' CASE WHEN a."'||attrs[j]||'" IS NULL THEN ''''::text'; 
     query = query ||'      WHEN char_length(a."'||attrs[j]||'"::text) > 100  THEN '''||adder||to_json(attrs[j]::text)||':'; 
     IF istext[j]='yes' THEN query = query||'''|| to_json ((substring (a."'||attrs[j]||'" from 0 for 100 ) || ''...'')::text)'; 
     ELSE query = query||''' || a."'||attrs[j]||'"';
     END IF; 
     query = query||'      ELSE '''||adder||to_json(attrs[j]::text)||':'; 
     IF istext[j]='yes' THEN query = query||'''|| to_json ( a."'||attrs[j]||'"::text)'; 
     ELSE query = query||''' || a."'||attrs[j]||'"';
     END IF;
     query = query||' END  as  g' || j;  
     query_jsonall = query_jsonall||' || f' || j;
     query_jsontile = query_jsontile ||' || g' || j; 
END LOOP;

query = query||' FROM "'||layername||'"."'||layername||'" a, "'||layername||'"."'||layername||'_ord" b WHERE a.gid = b.gid ORDER BY id ';
query_jsonall = query_jsonall || ' || '',"id":"'' || id || ''"}'' as  json, '; 
query_jsontile  = query_jsontile || ' || '',"id":"'' || id || ''"}'' as jsontile ';
EXECUTE 'DROP TABLE IF EXISTS "' || layername || '"."' || layername || '_info"' ;
EXECUTE' CREATE TABLE "'||layername||'"."'||layername||'_info" as SELECT id, geom4326,'||query_jsonall||query_jsontile||'FROM ('||query||') a '; 
--- FINE CREAZIONE JSON degli elemnenti

RETURN 'Success: '|| query_jsontile;
END;$$;


--
-- Name: _inno_prepare_layer(character varying); Type: FUNCTION; Schema: public; 
--

CREATE FUNCTION _inno_prepare_layer(layername character varying) RETURNS character varying
    LANGUAGE plpgsql STRICT
    AS $$DECLARE
      count  integer;
      column_name text;
      query text;
      query_preord    text;
      query_master    text;
      query_point_ord text;
      result integer;
      rec    record;
      rec2   record;
      geom   geometry;
      type text;
      time text := '';
      time1 text := '';
      maxgid bigint;
      size integer;
BEGIN
SELECT clock_timestamp() into time;

--- Controllo sui nomi degli attributi 
query := 'SELECT column_name, data_type, character_octet_length FROM INFORMATION_SCHEMA.COLUMNS where table_name = ''' 
         ||layername||''' AND table_schema = '''||layername||''' AND NOT data_type = ''USER-DEFINED''';
FOR rec in EXECUTE query 
LOOP
    -- verifica su nomi riservati ('_innoname_', %'bbox_')
    IF rec.column_name = '_innoname_' OR rec.column_name LIKE '%bbox_' THEN
        column_name = rec.column_name || '_';
        EXECUTE 'ALTER TABLE "'||layername||'"."'||layername||'" RENAME '||rec.column_name||' TO '||column_name;
    ELSE IF rec.column_name LIKE '%:%' OR rec.column_name LIKE '% %'  THEN
        EXECUTE 'select replace('''||rec.column_name||''','':'',''_'')' into column_name;
        EXECUTE 'select replace('''||column_name||''','' '',''_'')' into column_name;
        EXECUTE 'select replace('''||column_name||''','';'',''_'')' into column_name;
        EXECUTE 'ALTER TABLE "'||layername||'"."'||layername||'" RENAME '||rec.column_name||' TO '||column_name;
    END IF; END IF;
END LOOP;
EXECUTE 'CREATE  TEMPORARY SEQUENCE '||layername||'_order_seq START WITH 1 '
|| ' INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;';
-- Verifica tipo geometria
EXECUTE 'SELECT public.geometrytype(geom) FROM "'||layername||'"."'||layername||'" LIMIT 1' INTO type;
-- Creazione della tabella per le geometrie ordinate
EXECUTE 'DROP TABLE IF EXISTS "'||layername||'"."'||layername||'_ord"' ;
--- La query principale che crea la tabella di ordinamento
query_master := 'CREATE TABLE "'||layername||'"."'||layername||'_'||'ord" AS'
       ||' SELECT nextval(''' ||layername||'_order_seq''::regclass) as priority, ''''::text as id, a.* FROM ';
-- la sub query query_preord : ( gid, geom4326, geom3857, measure )
-- contiene le geometrie valide dumped (scomposte) con una misura usata quale priorità nel rendering
query_preord = ' SELECT a.*,  public.st_transform( geom4326, 3857 ) as geom3857,';
IF ( type = 'POLYGON' OR type = 'MULTIPOLYGON' ) THEN
     query_preord := query_preord || 'public.st_area(geography(geom4326),true) as measure ';
ELSE IF ( type = 'LINESTRING' OR type = 'MULTILINESTRING' ) THEN
     query_preord := query_preord || ' public.st_length(geography(geom4326),true) as measure ';     
ELSE IF ( type = 'MULTIPOINT' OR type = 'POINT' ) THEN
     query_preord := query_preord ||  ' ST_GeoHash(geom4326,7) as hash';
ELSE RETURN 'Errore ordinamento';
END IF; END IF; END IF;
query_preord = query_preord || ' FROM ( SELECT DISTINCT gid, (public.st_dump(geom)).geom as geom4326' 
     || '                 FROM "'||layername||'"."'||layername||'" WHERE public.st_isValid(geom) ) a ';
-- finalizzazione per poligoni e linee
IF ( type = 'POLYGON' OR type = 'MULTIPOLYGON' OR type = 'LINESTRING' OR type = 'MULTILINESTRING' ) THEN
     query_master := query_master || '(' || query_preord || ' ORDER BY measure DESC )a' ;
-- finalizzazione per i punti
-- la sub query query_point ord ( first gid, measure, n°points )
-- contiene le chiavi hash ed il nunero di punti con la chiave hash e il gid minimo dei punti 
-- con stessa hash   (hash di 7 caratteri --> buffer di 70 metri) 
ELSE IF ( type = 'MULTIPOINT' OR type = 'POINT' ) THEN  
query_point_ord =  'SELECT min(gid) as mingid, hash, count(hash) as measure FROM (' || query_preord || ') a';
query_point_ord = ' SELECT a.gid, a.geom4326, a.geom3857, (CASE '
      || ' WHEN NOT a.gid = b.mingid::float THEN -b.measure::float ELSE b.measure ) as measure FROM ('
      || query_preord || ') a, (' || query_point_ord || ') b WHERE a.hash = b.hash'; 
query_master := query_master||'('||query_point_ord||' ORDER BY measure DESC '||')a';
END IF; END IF;
EXECUTE query_master;

-- assegna un id testuale agli elementi preservando l'ordinamento
EXECUTE 'SELECT nextval('''||layername||'_order_seq''::regclass)' into maxgid ;
size = char_length(maxgid::text);
EXECUTE 'UPDATE "'||layername||'"."'||layername||'_'||'ord" set id = priority::text';
count = size;
WHILE ( count > 1 ) LOOP
    EXECUTE 'UPDATE "'||layername||'"."'||layername||'_'||'ord" set id = ''0''||id WHERE char_length(id) < '||size;
    count = count - 1;
END LOOP;

EXECUTE 'DROP SEQUENCE "' ||  layername || '_order_seq"';
SELECT clock_timestamp() into time1;

RETURN 'Success:' || layername || ' - start[' || time || '], end[' || time1 || '])';
END;$$;


--
-- Name: _inno_tables_report(text); Type: FUNCTION; Schema: public; 
--

CREATE FUNCTION _inno_tables_report(layername text) RETURNS text
    LANGUAGE plpgsql
    AS $$DECLARE
query text;
query2 text;
rec record;
count integer;
table_name text;
report text;
BEGIN 
report='';
--- Controllo sui nomi degli attributi e creazione del json delle informazioni sul layer 
query := 'SELECT distinct table_name FROM INFORMATION_SCHEMA.COLUMNS where table_name like ''' 
         ||layername||'%'' AND table_schema = '''||layername||'''';
found = false; 
FOR rec in EXECUTE query 
LOOP
     table_name = rec.table_name;
   
     query2 := 'SELECT count(*) FROM "' ||layername||'"."'||table_name||'"' ;
     EXECUTE query2 INTO count;
     RAISE NOTICE 'values %,%', table_name, count;
     report = report || table_name || '=' || count || ';' ; 
END LOOP;
 


RETURN report;
END;$$;


--
-- Name: _inno_tile2lat(integer, integer); Type: FUNCTION; Schema: public; 
--

CREATE FUNCTION _inno_tile2lat(y integer, zoom integer) RETURNS double precision
    LANGUAGE plpgsql IMMUTABLE
    AS $$DECLARE
 n float;
 sinh float;
 E float = 2.7182818284;
BEGIN
    n = pi() - (2.0 * pi() * y) / power(2.0, zoom);
    sinh = (1 - power(E, -2*n)) / (2 * power(E, -n));
    return degrees(atan(sinh));
END;
$$;


--
-- Name: _inno_tile2lon(integer, integer); Type: FUNCTION; Schema: public; 
--

CREATE FUNCTION _inno_tile2lon(x integer, zoom integer) RETURNS double precision
    LANGUAGE plpgsql IMMUTABLE
    AS $$BEGIN
 return x * 1.0 / (1 << zoom) * 360.0 - 180.0;
END;
$$;


--
-- Name: _inno_tile_assembler(character varying, integer, integer); Type: FUNCTION; Schema: public; 
--

CREATE FUNCTION _inno_tile_assembler(layername character varying, zoom integer, geom_type integer) RETURNS text
    LANGUAGE plpgsql
    AS $$DECLARE
        id          text;
        id_base     text;
        json        text;
        json_base   text;
        json_master text;
        
        rec record;
        rec2 record;
        query text;
        res   text;
        count integer;
        time  text;
        page  integer;
        xminbox  integer;
        yminbox  integer;

        geom      geometry;
        geom_coll geometry;
        box       geometry;
        box3857   geometry;
        tilebox   geometry;
        resolution float;
BEGIN     
        query := 'DROP TABLE IF EXISTS "' || layername || '"."' || layername || '_json_' || zoom || '" CASCADE';
        EXECUTE query;

        query := 'CREATE TABLE "' || layername || '"."' || layername || '_json_' || zoom || '"';
        query := query || '( id_tile text CONSTRAINT firstkey' || layername || zoom || ' PRIMARY KEY, '
                       || 'x integer, y integer, json text, geom geometry );';
        EXECUTE query;
        resolution := 20037508.342789244 / 256 * 2 / 2^zoom;
        resolution := 1/resolution;        
        SELECT clock_timestamp() into time;
        query := 'SELECT distinct x_tile, y_tile from "'||layername||'"."'||layername||'_tiles_'||zoom||'"';
        count = 0;
        FOR rec IN EXECUTE query LOOP   
             box := st_setsrid( st_makebox2d ( st_makepoint ( _inno_tile2lon(rec.x_tile,zoom),
                                                              _inno_tile2lat(rec.y_tile+1,zoom)),
                                               st_makepoint ( _inno_tile2lon(rec.x_tile+1,zoom),
                                                              _inno_tile2lat(rec.y_tile, zoom)) ), 4326);
             box3857 = st_transform ( box, 3857);
             id_base := layername||':'||rec.x_tile||':'||rec.y_tile||':'||zoom ;
             json_base := '{"bbox":' || ST_AsGeoJSON(box,5) || ', "objs": [';
             query := 'SELECT * , (st_dump(geom)).geom as dump from "'||layername||'"."'||layername||'_tiles_' 
                   ||zoom||'" WHERE x_tile = '||rec.x_tile||' AND y_tile = '||rec.y_tile||' ORDER BY id';
             geom_coll := null;
             json := '';
             page := 1;
             xminbox := - (st_xmin(box3857)::integer);
             yminbox := - (st_ymin(box3857)::integer);
             FOR rec2 IN EXECUTE query LOOP 
                 geom = st_transform(rec2.dump, 3857); 
                 geom = ST_TransScale( geom, xminbox, yminbox, resolution, -resolution);
                 geom = ST_Translate( ST_SnapToGrid ( geom, 1, 1 ) , 0, 255 );
                 IF ( geom IS NOT NULL ) THEN
                      geom_coll := ST_CollectionExtract(st_collect(geom_coll,
                                                        st_translate ( geom, rec2.x_tile * 256, rec2.y_tile * 256 )),geom_type);
                      IF ( NOT json = '' ) THEN
                           json := json || ',';
                      END IF;
                      json := json || '{"id":"' || rec2.id || '","g":"';
                      IF ( geom_type = 3 ) THEN 
                           json := json || _inno_compact_poly(geom); 
                      ELSE IF ( geom_type = 2 ) THEN
                           json := json || _inno_compact_line(geom); 
                      ELSE IF ( geom_type = 1 ) THEN
                           json := json || _inno_compact_point(geom);  
                      END IF; END IF; END IF;     
                      json := json || '"}';
                 END IF;
                 IF json IS NOT NULL AND char_length(json) > 20500 THEN
                    IF page = 1 THEN 
                       json_master := json_base||json||'], "id":"'||id_base||'", "page":'||page;
                       id = id_base;
                    ELSE 
                       json_master := json_base||json||'], "id":"'||id_base||':'||page||'", "page":'||page;
                       id = id_base||':'||page ;
                    END IF;
                    IF ( geom_coll is not NULL ) THEN
                        EXECUTE 'INSERT INTO "' || layername || '"."' || layername || '_json_'||zoom||'" VALUES (''' 
                             ||id||''', '||rec.x_tile||','||rec.y_tile||','''||json_master||''', st_geomfromewkt ('''
                             ||st_asewkt (geom_coll)|| '''))';
                        count = count + 1;
                    END IF;
                    json = '';
                    geom_coll = null;
                    page:= page + 1;
                 END IF;
             END LOOP;
             IF json IS NOT NULL AND NOT json = '' THEN
                    IF page = 1 THEN 
                       json_master := json_base||json||'], "id":"'||id_base||'", "page":'||page;
                       id = id_base;
                    ELSE 
                       json_master := json_base||json||'], "id":"'||id_base||':'||page||'", "page":'||page;
                       id = id_base||':'||page ;
                    END IF;
                    IF ( geom_coll is not NULL ) THEN
                        EXECUTE 'INSERT INTO "' || layername || '"."' || layername || '_json_'||zoom||'" VALUES (''' 
                             ||id||''', '||rec.x_tile||','||rec.y_tile||','''||json_master||''', st_geomfromewkt ('''
                             ||st_asewkt (geom_coll)|| '''))';
                        count = count + 1;
                    END IF;
             END IF;
             EXECUTE 'UPDATE "'||layername||'"."'||layername||'_json_'||zoom||'" set json = json || '|| ''', "pages":'|| page || '}'' WHERE id_tile like ''' || id_base || '%'' ';                  
        END LOOP; 
        RETURN 'Success tiles:' || count ;
END;
$$;

--
-- Name: _inno_tile_clipper(character varying, integer, integer); Type: FUNCTION; Schema: public; 
--

CREATE FUNCTION _inno_tile_clipper(layername character varying, zoom integer, geom_type integer) RETURNS text
    LANGUAGE plpgsql
    AS $$DECLARE
      type text;
      geom_type integer;
      tabledata text;
      count  integer;
      query  text;
      result integer;
      macro integer;
      rec    record;
      rec2   record;

      tile_bbox geometry;
      geom4326   geometry;
      geomTiled   geometry;
      part geometry;
      geom_txt text;

      tx1   integer;
      ty1   integer;
      tx2   integer;
      ty2   integer;
      ty integer;
      ty_add integer;

      i integer;
      j integer;
      x   integer;
      y   integer;
      y_value_new varchar[];
      y_value_prev varchar[];
      y_new  integer[];
      y_prev integer[];
BEGIN

result:=0;
EXECUTE 'DROP TABLE IF EXISTS "'||layername||'"."'||layername||'_tiles_'||zoom||'"' ;

EXECUTE 'CREATE TABLE "' || layername || '"."' || layername || '_tiles_' || zoom || '"'
       || '( x_tile integer,'
       || '  y_tile integer,'
       || '  zoom integer,'
       || '  measure float,'
       || '  id character varying,'
       || '  geom geometry);';


query := 'SELECT priority, id, measure, geom4326 FROM "'
         ||layername||'"."'||layername ||'_ord'||'" WHERE st_isvalid(geom4326) order by priority';

macro = 0;
FOR rec IN EXECUTE query LOOP
    geom4326 = rec.geom4326;
    tx1 := _inno_lon2tile( st_xmin(geom4326), zoom);
    ty2 := _inno_lat2Tile( st_ymin(geom4326), zoom);
    tx2 := _inno_lon2tile( st_xmax(geom4326), zoom);
    ty1 := _inno_lat2Tile( st_ymax(geom4326), zoom);
    x := tx1;
    WHILE x < (tx2 + 1)
    LOOP
          y := ty1;
          WHILE y < (ty2 + 1)
          LOOP
                tile_bbox = ST_MakeEnvelope(_inno_tile2lon(x, zoom),_inno_tile2lat(y+1, zoom),
                                         _inno_tile2lon(x+1, zoom),_inno_tile2lat(y, zoom),4326);
                IF ST_Intersects( geom4326, tile_bbox )  THEN
                     geomTiled := ST_Intersection( geom4326, tile_bbox );
                     FOR part IN SELECT geom FROM st_dump ( st_geometryFromText(st_asText(geomTiled)))
                     LOOP
                         part = st_setsrid(part,4326);
                         IF st_geometrytype(part) = 'ST_Polygon' AND NOT ST_Contains( part, tile_bbox ) THEN
                            geom_txt := ST_asEWKT( part );
                            EXECUTE 'INSERT INTO "'||layername||'"."'||layername||'_tiles_'||zoom
                                  ||'" VALUES ('||x||','||y||','||zoom||','||rec.measure
                                  ||', '''||rec.id||''', St_geomFromEwkt('''||geom_txt||'''));';
                            result := result + 1;
                         ELSE IF NOT st_geometrytype(part) = 'ST_Polygon' THEN 
                            macro = macro + 1;
                         END IF; END IF;
                     END LOOP;
                END IF;
                
                y = y + 1;
          END LOOP;
          x = x + 1;       
    END LOOP;
END LOOP;
RETURN 'Success. Clips:' || result || ' macro:' || macro;
END;$$;


--
-- Name: _inno_tile_writer(character varying, character varying, integer); Type: FUNCTION; Schema: public; 
--

CREATE FUNCTION _inno_tile_writer(layername character varying, path character varying, zoom integer) RETURNS text
    LANGUAGE plpgsql
    AS $$DECLARE
query text;
rec record;
res integer;
json text;
BEGIN
res = 0;
query = 'SELECT id_tile, json FROM "' || layername || '"."' || layername || '_json_' || zoom || '"' ;
FOR rec IN execute query LOOP
    json = quote_literal(rec.json); 
    -- json = substring( quote_literal(json) from 2 for char_length(quote_literal(json)) - 2 );  
    -- RAISE NOTICE ' % ', json;
    query := 'COPY ( SELECT ' || json || '  ) TO ''' || path || '/' || layername || '/' || layername || zoom || '/docs/' || rec.id_tile || '.json''' ;
    EXECUTE query;
    res = res + 1;
END LOOP;
RETURN res || ' TILES FOR ZOOM ' || zoom || ' WROTE ' ;  
END;
$$;



--
-- Name: _inno_write_to_disk(text, text); Type: FUNCTION; Schema: public; 
--

CREATE FUNCTION _inno_write_to_disk(layername text, path text) RETURNS text
    LANGUAGE plpgsql
    AS $$DECLARE
result text;
BEGIN 

result = _inno_info_writer (layername,path);
FOR zoom in 10..17 LOOP
     result = result || _inno_tile_writer (layername,path,zoom);
END LOOP;

RETURN 'Success: ' || result;
END;$$;



--
-- Name: actionlogcommentrecords; Type: TABLE; Schema: public;  
--

CREATE TABLE actionlogcommentrecords (
    id integer NOT NULL,
    recordid integer NOT NULL,
    username character varying(20) NOT NULL,
    comment text NOT NULL,
    commentdate timestamp without time zone NOT NULL
);



--
-- Name: actionloglikerecords; Type: TABLE; Schema: public;  
--

CREATE TABLE actionloglikerecords (
    recordid integer NOT NULL,
    username character varying(20) NOT NULL,
    likedate timestamp without time zone NOT NULL
);



--
-- Name: actionlogrecords; Type: TABLE; Schema: public; ; Tablespace: 
--

CREATE TABLE actionlogrecords (
    id integer NOT NULL,
    username character varying(20),
    updatedate timestamp without time zone,
    actiondate timestamp without time zone,
    namespace character varying,
    actionname character varying(255),
    parameters character varying,
    activitystreaminfo character varying
);


--
-- Name: actionlogrelations; Type: TABLE; Schema: public; ; Tablespace: 
--

CREATE TABLE actionlogrelations (
    recordid integer NOT NULL,
    refgroup character varying(20)
);



--
-- Name: api_oauth_consumers; Type: TABLE; Schema: public; ; Tablespace: 
--

CREATE TABLE api_oauth_consumers (
    consumerkey character varying(100) NOT NULL,
    consumersecret character varying(100) NOT NULL,
    description character varying(500) NOT NULL,
    callbackurl character varying(500),
    expirationdate date
);



--
-- Name: api_oauth_tokens; Type: TABLE; Schema: public; ; Tablespace: 
--

CREATE TABLE api_oauth_tokens (
    accesstoken character(100) NOT NULL,
    tokensecret character varying(100) NOT NULL,
    consumerkey character varying(100) NOT NULL,
    lastaccess date NOT NULL,
    username character varying(40) NOT NULL
);


--
-- Name: apicatalog_methods; Type: TABLE; Schema: public; ; Tablespace: 
--

CREATE TABLE apicatalog_methods (
    resourcecode character varying(100) NOT NULL,
    httpmethod character varying(6) NOT NULL,
    isactive smallint NOT NULL,
    ishidden smallint NOT NULL,
    authenticationrequired smallint,
    authorizationrequired character varying(30)
);


--
-- Name: apicatalog_services; Type: TABLE; Schema: public; 
--

CREATE TABLE apicatalog_services (
    servicekey character varying(100) NOT NULL,
    resourcecode character varying(100) NOT NULL,
    description character varying NOT NULL,
    parameters character varying,
    tag character varying(100),
    freeparameters character varying,
    isactive smallint NOT NULL,
    ishidden smallint NOT NULL,
    myentando smallint NOT NULL,
    authenticationrequired smallint,
    requiredpermission character varying(30),
    requiredgroup character varying(20)
);



--
-- Name: authgroups; Type: TABLE; Schema: public; 
--

CREATE TABLE authgroups (
    groupname character varying(20) NOT NULL,
    descr character varying(50)
);



--
-- Name: authpermissions; Type: TABLE; Schema: public; 
--

CREATE TABLE authpermissions (
    permissionname character varying(30) NOT NULL,
    descr character varying(50)
);



--
-- Name: authrolepermissions; Type: TABLE; Schema: 
--

CREATE TABLE authrolepermissions (
    rolename character varying(20) NOT NULL,
    permissionname character varying(30) NOT NULL
);


--
-- Name: authroles; Type: TABLE; Schema: public; 
--

CREATE TABLE authroles (
    rolename character varying(20) NOT NULL,
    descr character varying(50)
);



--
-- Name: authusergrouprole; Type: TABLE; Schema: public;
--

CREATE TABLE authusergrouprole (
    username character varying(40) NOT NULL,
    groupname character varying(20),
    rolename character varying(20)
);



--
-- Name: authuserprofileattrroles; Type: TABLE; Schema: public;  
--

CREATE TABLE authuserprofileattrroles (
    username character varying(40) NOT NULL,
    rolename character varying(50) NOT NULL,
    attrname character varying(30) NOT NULL
);



--
-- Name: authuserprofiles; Type: TABLE; Schema: public;  
--

CREATE TABLE authuserprofiles (
    username character varying(40) NOT NULL,
    profiletype character varying(30) NOT NULL,
    profilexml character varying NOT NULL,
    publicprofile smallint NOT NULL
);



--
-- Name: authuserprofilesearch; Type: TABLE; Schema: public;  
--

CREATE TABLE authuserprofilesearch (
    username character varying(40) NOT NULL,
    attrname character varying(30) NOT NULL,
    textvalue character varying(255),
    datevalue date,
    numvalue integer,
    langcode character varying(2)
);


--
-- Name: authusers; Type: TABLE; Schema: public; 
--

CREATE TABLE authusers (
    username character varying(40) NOT NULL,
    passwd character varying(40),
    registrationdate date NOT NULL,
    lastaccess date,
    lastpasswordchange date,
    active smallint
);



--
-- Name: authusershortcuts; Type: TABLE; Schema:  
--

CREATE TABLE authusershortcuts (
    username character varying(40) NOT NULL,
    config character varying NOT NULL
);



--
-- Name: innomanager_layers; Type: TABLE; Schema: public;  
--

CREATE TABLE innomanager_layers (
    name character varying(40) NOT NULL,
    description text,
    status integer NOT NULL,
    tilemask integer NOT NULL,
    operator character varying(40) NOT NULL,
    log text,
    lastupdate date NOT NULL
);




--
-- Name: actionlogcommentrecords_pkey; Type: CONSTRAINT; Schema: public; 
--

ALTER TABLE ONLY actionlogcommentrecords
    ADD CONSTRAINT actionlogcommentrecords_pkey PRIMARY KEY (id);


--
-- Name: actionlogrecords_pkey; Type: CONSTRAINT; Schema: public;  
--

ALTER TABLE ONLY actionlogrecords
    ADD CONSTRAINT actionlogrecords_pkey PRIMARY KEY (id);


--
-- Name: api_oauth_consumers_pkey; Type: CONSTRAINT; Schema: public; 
--

ALTER TABLE ONLY api_oauth_consumers
    ADD CONSTRAINT api_oauth_consumers_pkey PRIMARY KEY (consumerkey);


--
-- Name: api_oauth_tokens_pkey; Type: CONSTRAINT; Schema: public; ; Tablespace: 
--

ALTER TABLE ONLY api_oauth_tokens
    ADD CONSTRAINT api_oauth_tokens_pkey PRIMARY KEY (accesstoken);


--
-- Name: apicatalog_services_pkey; Type: CONSTRAINT; Schema: public; ; Tablespace: 
--

ALTER TABLE ONLY apicatalog_services
    ADD CONSTRAINT apicatalog_services_pkey PRIMARY KEY (servicekey);


--
-- Name: apicatalog_status_pkey; Type: CONSTRAINT; Schema: public; ; Tablespace: 
--

ALTER TABLE ONLY apicatalog_methods
    ADD CONSTRAINT apicatalog_status_pkey PRIMARY KEY (resourcecode, httpmethod);


--
-- Name: authgroups_pkey; Type: CONSTRAINT; Schema: public; ; Tablespace: 
--

ALTER TABLE ONLY authgroups
    ADD CONSTRAINT authgroups_pkey PRIMARY KEY (groupname);


--
-- Name: authpermissions_pkey; Type: CONSTRAINT; Schema: public; ; Tablespace: 
--

ALTER TABLE ONLY authpermissions
    ADD CONSTRAINT authpermissions_pkey PRIMARY KEY (permissionname);


--
-- Name: authrolepermissions_pkey; Type: CONSTRAINT; Schema: public; ; Tablespace: 
--

ALTER TABLE ONLY authrolepermissions
    ADD CONSTRAINT authrolepermissions_pkey PRIMARY KEY (rolename, permissionname);


--
-- Name: authroles_pkey; Type: CONSTRAINT; Schema: public; ; Tablespace: 
--

ALTER TABLE ONLY authroles
    ADD CONSTRAINT authroles_pkey PRIMARY KEY (rolename);


--
-- Name: authusers_pkey; Type: CONSTRAINT; Schema: public; ; Tablespace: 
--

ALTER TABLE ONLY authusers
    ADD CONSTRAINT authusers_pkey PRIMARY KEY (username);


--
-- Name: authusershortcuts2_pkey; Type: CONSTRAINT; Schema: public; ; Tablespace: 
--

ALTER TABLE ONLY authusershortcuts
    ADD CONSTRAINT authusershortcuts2_pkey PRIMARY KEY (username);


--
-- Name: autuserprofiles_pkey; Type: CONSTRAINT; Schema: public; ; Tablespace: 
--

ALTER TABLE ONLY authuserprofiles
    ADD CONSTRAINT autuserprofiles_pkey PRIMARY KEY (username);


--
-- Name: attrname; Type: INDEX; Schema: public; ; Tablespace: 
--

CREATE INDEX attrname ON authuserprofileattrroles USING btree (attrname);


--
-- Name: rolename; Type: INDEX; Schema: public; ; Tablespace: 
--

CREATE INDEX rolename ON authuserprofileattrroles USING btree (rolename);


--
-- Name: username; Type: INDEX; Schema: public; ; Tablespace: 
--

CREATE INDEX username ON authuserprofileattrroles USING btree (username);


--
-- Name: actionloglikerec_recid_fkey; Type: FK CONSTRAINT; Schema: public; 
--

ALTER TABLE ONLY actionloglikerecords
    ADD CONSTRAINT actionloglikerec_recid_fkey FOREIGN KEY (recordid) REFERENCES actionlogrecords(id);


--
-- Name: actionloglikerec_recid_fkey; Type: FK CONSTRAINT; Schema: public; 
--

ALTER TABLE ONLY actionlogcommentrecords
    ADD CONSTRAINT actionloglikerec_recid_fkey FOREIGN KEY (recordid) REFERENCES actionlogrecords(id);


--
-- Name: actionlogrelations_recid_fkey; Type: FK CONSTRAINT; Schema: public; 
--

ALTER TABLE ONLY actionlogrelations
    ADD CONSTRAINT actionlogrelations_recid_fkey FOREIGN KEY (recordid) REFERENCES actionlogrecords(id);


--
-- Name: apicatalog_methods_authorizationrequired_fkey; Type: FK CONSTRAINT; Schema: public; 
--

ALTER TABLE ONLY apicatalog_methods
    ADD CONSTRAINT apicatalog_methods_authorizationrequired_fkey FOREIGN KEY (authorizationrequired) REFERENCES authpermissions(permissionname) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: apicatalog_services_requiredgroup_fkey; Type: FK CONSTRAINT; Schema: public; 
--

ALTER TABLE ONLY apicatalog_services
    ADD CONSTRAINT apicatalog_services_requiredgroup_fkey FOREIGN KEY (requiredgroup) REFERENCES authgroups(groupname) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: apicatalog_services_requiredpermission_fkey; Type: FK CONSTRAINT; Schema: public; 
--

ALTER TABLE ONLY apicatalog_services
    ADD CONSTRAINT apicatalog_services_requiredpermission_fkey FOREIGN KEY (requiredpermission) REFERENCES authpermissions(permissionname) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: authrolepermissions_permissionname_fkey; Type: FK CONSTRAINT; Schema: public; 
--

ALTER TABLE ONLY authrolepermissions
    ADD CONSTRAINT authrolepermissions_permissionname_fkey FOREIGN KEY (permissionname) REFERENCES authpermissions(permissionname) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: authrolepermissions_rolename_fkey; Type: FK CONSTRAINT; Schema: public; 
--

ALTER TABLE ONLY authrolepermissions
    ADD CONSTRAINT authrolepermissions_rolename_fkey FOREIGN KEY (rolename) REFERENCES authroles(rolename) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: authusergrouprole_grn_fkey; Type: FK CONSTRAINT; Schema: public; 
--

ALTER TABLE ONLY authusergrouprole
    ADD CONSTRAINT authusergrouprole_grn_fkey FOREIGN KEY (groupname) REFERENCES authgroups(groupname);


--
-- Name: authusergrouprole_rln_fkey; Type: FK CONSTRAINT; Schema: public; 
--

ALTER TABLE ONLY authusergrouprole
    ADD CONSTRAINT authusergrouprole_rln_fkey FOREIGN KEY (rolename) REFERENCES authroles(rolename);


--
-- Name: authuserprofileattrroles_fkey; Type: FK CONSTRAINT; Schema: public; 
--

ALTER TABLE ONLY authuserprofileattrroles
    ADD CONSTRAINT authuserprofileattrroles_fkey FOREIGN KEY (username) REFERENCES authuserprofiles(username);


--
-- Name: profilesearch_username_fkey; Type: FK CONSTRAINT; Schema: public;
--

ALTER TABLE ONLY authuserprofilesearch
    ADD CONSTRAINT profilesearch_username_fkey FOREIGN KEY (username) REFERENCES authuserprofiles(username);


--
-- Name: innomanager_layers_pkey; Type: FK CONSTRAINT; Schema: public;
--
ALTER TABLE ONLY innomanager_layers
    ADD CONSTRAINT innomanager_layers_pkey PRIMARY KEY (name);

--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

