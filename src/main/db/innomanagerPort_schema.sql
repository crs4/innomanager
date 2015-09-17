

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

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: categories; Type: TABLE; Schema: public; Owner: inno; Tablespace: 
--

CREATE TABLE categories (
    catcode character varying(30) NOT NULL,
    parentcode character varying(30) NOT NULL,
    titles character varying NOT NULL
);



--
-- Name: contentattributeroles; Type: TABLE; Schema: public; Owner: inno; Tablespace: 
--

CREATE TABLE contentattributeroles (
    contentid character varying(16) NOT NULL,
    attrname character varying(30) NOT NULL,
    rolename character varying(50) NOT NULL
);



--
-- Name: contentmodels; Type: TABLE; Schema: public; Owner: inno; Tablespace: 
--

CREATE TABLE contentmodels (
    modelid integer NOT NULL,
    contenttype character varying(30) NOT NULL,
    descr character varying(50) NOT NULL,
    model character varying,
    stylesheet character varying(50)
);



--
-- Name: contentrelations; Type: TABLE; Schema: public; Owner: inno; Tablespace: 
--

CREATE TABLE contentrelations (
    contentid character varying(16) NOT NULL,
    refpage character varying(30),
    refcontent character varying(16),
    refresource character varying(16),
    refcategory character varying(30),
    refgroup character varying(20)
);



--
-- Name: contents; Type: TABLE; Schema: public; Owner: inno; Tablespace: 
--

CREATE TABLE contents (
    contentid character varying(16) NOT NULL,
    contenttype character varying(30) NOT NULL,
    descr character varying(260) NOT NULL,
    status character varying(12) NOT NULL,
    workxml character varying NOT NULL,
    created character varying(20),
    lastmodified character varying(20),
    onlinexml character varying,
    maingroup character varying(20) NOT NULL,
    currentversion character varying(7) NOT NULL,
    lasteditor character varying(40)
);



--
-- Name: contentsearch; Type: TABLE; Schema: public; Owner: inno; Tablespace: 
--

CREATE TABLE contentsearch (
    contentid character varying(16) NOT NULL,
    attrname character varying(30) NOT NULL,
    textvalue character varying(255),
    datevalue date,
    numvalue integer,
    langcode character varying(2)
);



--
-- Name: guifragment; Type: TABLE; Schema: public; Owner: inno; Tablespace: 
--

CREATE TABLE guifragment (
    code character varying(50) NOT NULL,
    widgettypecode character varying(40),
    plugincode character varying(30),
    gui character varying,
    defaultgui character varying,
    locked smallint NOT NULL
);



--
-- Name: localstrings; Type: TABLE; Schema: public; Owner: inno; Tablespace: 
--

CREATE TABLE localstrings (
    keycode character varying(50) NOT NULL,
    langcode character varying(2) NOT NULL,
    stringvalue character varying NOT NULL
);



--
-- Name: pagemodels; Type: TABLE; Schema: public; Owner: inno; Tablespace: 
--

CREATE TABLE pagemodels (
    code character varying(40) NOT NULL,
    descr character varying(50) NOT NULL,
    frames character varying,
    plugincode character varying(30),
    templategui text
);



--
-- Name: pages; Type: TABLE; Schema: public; Owner: inno; Tablespace: 
--

CREATE TABLE pages (
    code character varying(30) NOT NULL,
    parentcode character varying(30),
    pos integer NOT NULL,
    modelcode character varying(40) NOT NULL,
    titles character varying,
    groupcode character varying(30) NOT NULL,
    showinmenu smallint NOT NULL,
    extraconfig character varying
);



--
-- Name: resourcerelations; Type: TABLE; Schema: public; Owner: inno; Tablespace: 
--

CREATE TABLE resourcerelations (
    resid character varying(16) NOT NULL,
    refcategory character varying(30)
);



--
-- Name: resources; Type: TABLE; Schema: public; Owner: inno; Tablespace: 
--

CREATE TABLE resources (
    resid character varying(16) NOT NULL,
    restype character varying(30) NOT NULL,
    descr character varying(260) NOT NULL,
    maingroup character varying(20) NOT NULL,
    resourcexml character varying NOT NULL,
    masterfilename character varying(100) NOT NULL,
    creationdate date,
    lastmodified date
);



--
-- Name: sysconfig; Type: TABLE; Schema: public; Owner: inno; Tablespace: 
--

CREATE TABLE sysconfig (
    version character varying(10) NOT NULL,
    item character varying(40) NOT NULL,
    descr character varying(100),
    config character varying
);



--
-- Name: uniquekeys; Type: TABLE; Schema: public; Owner: inno; Tablespace: 
--

CREATE TABLE uniquekeys (
    id integer NOT NULL,
    keyvalue integer NOT NULL
);



--
-- Name: widgetcatalog; Type: TABLE; Schema: public; Owner: inno; Tablespace: 
--

CREATE TABLE widgetcatalog (
    code character varying(40) NOT NULL,
    titles character varying NOT NULL,
    parameters character varying,
    plugincode character varying(30),
    parenttypecode character varying(40),
    defaultconfig character varying,
    locked smallint NOT NULL,
    maingroup character varying(20)
);



--
-- Name: widgetconfig; Type: TABLE; Schema: public; Owner: inno; Tablespace: 
--

CREATE TABLE widgetconfig (
    pagecode character varying(30) NOT NULL,
    framepos integer NOT NULL,
    widgetcode character varying(40) NOT NULL,
    config character varying
);



--
-- Name: workcontentattributeroles; Type: TABLE; Schema: public; Owner: inno; Tablespace: 
--

CREATE TABLE workcontentattributeroles (
    contentid character varying(16) NOT NULL,
    attrname character varying(30) NOT NULL,
    rolename character varying(50) NOT NULL
);


--
-- Name: workcontentrelations; Type: TABLE; Schema: public; Owner: inno; Tablespace: 
--

CREATE TABLE workcontentrelations (
    contentid character varying(16) NOT NULL,
    refcategory character varying(30)
);



--
-- Name: workcontentsearch; Type: TABLE; Schema: public; Owner: inno; Tablespace: 
--

CREATE TABLE workcontentsearch (
    contentid character varying(16),
    attrname character varying(30) NOT NULL,
    textvalue character varying(255),
    datevalue date,
    numvalue integer,
    langcode character varying(2)
);





--
-- Name: categories_pkey; Type: CONSTRAINT; Schema: public; Owner: inno; Tablespace: 
--

ALTER TABLE ONLY categories
    ADD CONSTRAINT categories_pkey PRIMARY KEY (catcode);


--
-- Name: contentmodels_pkey; Type: CONSTRAINT; Schema: public; Owner: inno; Tablespace: 
--

ALTER TABLE ONLY contentmodels
    ADD CONSTRAINT contentmodels_pkey PRIMARY KEY (modelid);


--
-- Name: contents_pkey; Type: CONSTRAINT; Schema: public; Owner: inno; Tablespace: 
--

ALTER TABLE ONLY contents
    ADD CONSTRAINT contents_pkey PRIMARY KEY (contentid);


--
-- Name: guifragment_pkey; Type: CONSTRAINT; Schema: public; Owner: inno; Tablespace: 
--

ALTER TABLE ONLY guifragment
    ADD CONSTRAINT guifragment_pkey PRIMARY KEY (code);


--
-- Name: localstrings_pkey; Type: CONSTRAINT; Schema: public; Owner: inno; Tablespace: 
--

ALTER TABLE ONLY localstrings
    ADD CONSTRAINT localstrings_pkey PRIMARY KEY (keycode, langcode);


--
-- Name: pagemodels_pkey; Type: CONSTRAINT; Schema: public; Owner: inno; Tablespace: 
--

ALTER TABLE ONLY pagemodels
    ADD CONSTRAINT pagemodels_pkey PRIMARY KEY (code);


--
-- Name: pages_pkey; Type: CONSTRAINT; Schema: public; Owner: inno; Tablespace: 
--

ALTER TABLE ONLY pages
    ADD CONSTRAINT pages_pkey PRIMARY KEY (code);


--
-- Name: resources_pkey; Type: CONSTRAINT; Schema: public; Owner: inno; Tablespace: 
--

ALTER TABLE ONLY resources
    ADD CONSTRAINT resources_pkey PRIMARY KEY (resid);


--
-- Name: system_pkey; Type: CONSTRAINT; Schema: public; Owner: inno; Tablespace: 
--

ALTER TABLE ONLY sysconfig
    ADD CONSTRAINT system_pkey PRIMARY KEY (version, item);


--
-- Name: uniquekeys_pkey; Type: CONSTRAINT; Schema: public; Owner: inno; Tablespace: 
--

ALTER TABLE ONLY uniquekeys
    ADD CONSTRAINT uniquekeys_pkey PRIMARY KEY (id);


--
-- Name: widgetcatalog_pkey; Type: CONSTRAINT; Schema: public; Owner: inno; Tablespace: 
--

ALTER TABLE ONLY widgetcatalog
    ADD CONSTRAINT widgetcatalog_pkey PRIMARY KEY (code);


--
-- Name: widgetconfig_pkey; Type: CONSTRAINT; Schema: public; Owner: inno; Tablespace: 
--

ALTER TABLE ONLY widgetconfig
    ADD CONSTRAINT widgetconfig_pkey PRIMARY KEY (pagecode, framepos);


--
-- Name: contentattrroles_contid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: inno
--

ALTER TABLE ONLY contentattributeroles
    ADD CONSTRAINT contentattrroles_contid_fkey FOREIGN KEY (contentid) REFERENCES contents(contentid);


--
-- Name: contentrelations_contentid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: inno
--

ALTER TABLE ONLY contentrelations
    ADD CONSTRAINT contentrelations_contentid_fkey FOREIGN KEY (contentid) REFERENCES contents(contentid);


--
-- Name: contentrelations_refcategory_fkey; Type: FK CONSTRAINT; Schema: public; Owner: inno
--

ALTER TABLE ONLY contentrelations
    ADD CONSTRAINT contentrelations_refcategory_fkey FOREIGN KEY (refcategory) REFERENCES categories(catcode);


--
-- Name: contentrelations_refcontent_fkey; Type: FK CONSTRAINT; Schema: public; Owner: inno
--

ALTER TABLE ONLY contentrelations
    ADD CONSTRAINT contentrelations_refcontent_fkey FOREIGN KEY (refcontent) REFERENCES contents(contentid);


--
-- Name: contentrelations_refpage_fkey; Type: FK CONSTRAINT; Schema: public; Owner: inno
--

ALTER TABLE ONLY contentrelations
    ADD CONSTRAINT contentrelations_refpage_fkey FOREIGN KEY (refpage) REFERENCES pages(code);


--
-- Name: contentrelations_refresource_fkey; Type: FK CONSTRAINT; Schema: public; Owner: inno
--

ALTER TABLE ONLY contentrelations
    ADD CONSTRAINT contentrelations_refresource_fkey FOREIGN KEY (refresource) REFERENCES resources(resid);


--
-- Name: contentsearch_contentid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: inno
--

ALTER TABLE ONLY contentsearch
    ADD CONSTRAINT contentsearch_contentid_fkey FOREIGN KEY (contentid) REFERENCES contents(contentid);


--
-- Name: pages_modelcode_fkey; Type: FK CONSTRAINT; Schema: public; Owner: inno
--

ALTER TABLE ONLY pages
    ADD CONSTRAINT pages_modelcode_fkey FOREIGN KEY (modelcode) REFERENCES pagemodels(code);


--
-- Name: resourcerelations_refcategory_fkey; Type: FK CONSTRAINT; Schema: public; Owner: inno
--

ALTER TABLE ONLY resourcerelations
    ADD CONSTRAINT resourcerelations_refcategory_fkey FOREIGN KEY (refcategory) REFERENCES categories(catcode);


--
-- Name: resourcerelations_resid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: inno
--

ALTER TABLE ONLY resourcerelations
    ADD CONSTRAINT resourcerelations_resid_fkey FOREIGN KEY (resid) REFERENCES resources(resid);


--
-- Name: widgetconfig_pagecode_fkey; Type: FK CONSTRAINT; Schema: public; Owner: inno
--

ALTER TABLE ONLY widgetconfig
    ADD CONSTRAINT widgetconfig_pagecode_fkey FOREIGN KEY (pagecode) REFERENCES pages(code);


--
-- Name: widgetconfig_widgetcode_fkey; Type: FK CONSTRAINT; Schema: public; Owner: inno
--

ALTER TABLE ONLY widgetconfig
    ADD CONSTRAINT widgetconfig_widgetcode_fkey FOREIGN KEY (widgetcode) REFERENCES widgetcatalog(code);


--
-- Name: widgettypecode_fkey; Type: FK CONSTRAINT; Schema: public; Owner: inno
--

ALTER TABLE ONLY guifragment
    ADD CONSTRAINT widgettypecode_fkey FOREIGN KEY (widgettypecode) REFERENCES widgetcatalog(code);


--
-- Name: workcontentattrroles_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: inno
--

ALTER TABLE ONLY workcontentattributeroles
    ADD CONSTRAINT workcontentattrroles_id_fkey FOREIGN KEY (contentid) REFERENCES contents(contentid);


--
-- Name: workcontentrelations_contentid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: inno
--

ALTER TABLE ONLY workcontentrelations
    ADD CONSTRAINT workcontentrelations_contentid_fkey FOREIGN KEY (contentid) REFERENCES contents(contentid);


--
-- Name: workcontentsearch_contentid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: inno
--

ALTER TABLE ONLY workcontentsearch
    ADD CONSTRAINT workcontentsearch_contentid_fkey FOREIGN KEY (contentid) REFERENCES contents(contentid);


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

