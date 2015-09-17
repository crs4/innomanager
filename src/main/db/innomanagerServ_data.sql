
--
-- Data for Name: authgroups; Type: TABLE DATA; Schema: public; Owner: inno
--

INSERT INTO authgroups VALUES ('administrators', 'Administrators');
INSERT INTO authgroups VALUES ('free', 'Free Access');
INSERT INTO authgroups VALUES ('innousers', 'Utenti inno ');


--
-- Data for Name: authroles; Type: TABLE DATA; Schema: public; Owner: inno
--

INSERT INTO authroles VALUES ('admin', 'Administrator');
INSERT INTO authroles VALUES ('innousers', 'Utenti con i permessi di esecuzione procedure ETL');


--
-- Data for Name: authpermissions; Type: TABLE DATA; Schema: public; Owner: inno
--

INSERT INTO authpermissions VALUES ('superuser', 'All functions');
INSERT INTO authpermissions VALUES ('validateContents', 'Supervision of contents');
INSERT INTO authpermissions VALUES ('manageResources', 'Operations on Resources');
INSERT INTO authpermissions VALUES ('managePages', 'Operations on Pages');
INSERT INTO authpermissions VALUES ('enterBackend', 'Access to Administration Area');
INSERT INTO authpermissions VALUES ('manageCategories', 'Operations on Categories');
INSERT INTO authpermissions VALUES ('editContents', 'Content Editing');
INSERT INTO authpermissions VALUES ('viewUsers', 'View Users and Profiles');
INSERT INTO authpermissions VALUES ('editUsers', 'User Editing');
INSERT INTO authpermissions VALUES ('editUserProfile', 'User Profile Editing');
INSERT INTO authpermissions VALUES ('innouser', 'gestisce i Work Layers');


--
-- Data for Name: authrolepermissions; Type: TABLE DATA; Schema: public; Owner: inno
--

INSERT INTO authrolepermissions VALUES ('admin', 'superuser');
INSERT INTO authrolepermissions VALUES ('innousers', 'innouser');



--
-- Data for Name: authusergrouprole; Type: TABLE DATA; Schema: public; Owner: inno
--

INSERT INTO authusergrouprole VALUES ('admin', 'administrators', 'admin');
INSERT INTO authusergrouprole VALUES ('testerinno', 'innousers', 'innousers');
INSERT INTO authusergrouprole VALUES ('admin', 'innousers', 'innousers');


--
-- Data for Name: authuserprofiles; Type: TABLE DATA; Schema: public; Owner: inno
--

INSERT INTO authuserprofiles VALUES ('testerinno', 'PFL', '<?xml version="1.0" encoding="UTF-8"?>
<profile id="testerinno" typecode="PFL" typedescr="Default user profile"><descr /><groups /><categories /><attributes><attribute name="fullname" attributetype="Monotext"><monotext>Tester</monotext></attribute><attribute name="email" attributetype="Monotext"><monotext>demontis@crs4.it</monotext></attribute></attributes></profile>
', 0);
INSERT INTO authuserprofiles VALUES ('admin', 'PFL', '<?xml version="1.0" encoding="UTF-8"?>
<profile id="admin" typecode="PFL" typedescr="Default user profile"><descr /><groups /><categories /><attributes><attribute name="fullname" attributetype="Monotext"><monotext>Administrator</monotext></attribute><attribute name="email" attributetype="Monotext"><monotext>demontis@crs4.it</monotext></attribute></attributes></profile>
', 0);


--
-- Data for Name: authuserprofileattrroles; Type: TABLE DATA; Schema: public; Owner: inno
--

INSERT INTO authuserprofileattrroles VALUES ('testerinno', 'userprofile:fullname', 'fullname');
INSERT INTO authuserprofileattrroles VALUES ('testerinno', 'userprofile:email', 'email');
INSERT INTO authuserprofileattrroles VALUES ('admin', 'userprofile:fullname', 'fullname');
INSERT INTO authuserprofileattrroles VALUES ('admin', 'userprofile:email', 'email');


--
-- Data for Name: authuserprofilesearch; Type: TABLE DATA; Schema: public; Owner: inno
--

INSERT INTO authuserprofilesearch VALUES ('testerinno', 'fullname', 'Tester', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch VALUES ('testerinno', 'email', 'demontis@crs4.it', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch VALUES ('admin', 'fullname', 'Administrator', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch VALUES ('admin', 'email', 'demontis@crs4.it', NULL, NULL, NULL);


--
-- Data for Name: authusers; Type: TABLE DATA; Schema: public; Owner: inno
--

INSERT INTO authusers VALUES ('testerinno', 'testerinnopassword', '2015-04-10', NULL, NULL, 1);
INSERT INTO authusers VALUES ('admin', 'adminpassword', '2008-10-10', '2015-08-31', '2015-06-29', 1);


--
-- Data for Name: authusershortcuts; Type: TABLE DATA; Schema: public; Owner: inno
--

INSERT INTO authusershortcuts VALUES ('admin', '<?xml version="1.0" encoding="UTF-8"?>
<shortcuts>
	<box pos="0">core.component.user.list</box>
	<box pos="1">core.component.labels.list</box>
	<box pos="2">core.tools.setting</box>
	<box pos="3">core.tools.entities</box>
	<box pos="4">jacms.content.new</box>
	<box pos="5">jacms.content.list</box>
	<box pos="6">jacms.contentType</box>
	<box pos="7">core.portal.pageTree</box>
	<box pos="8">core.portal.widgetType</box>
</shortcuts>

');

--
-- Data for Name: innomanager_layers; Type: TABLE DATA; Schema: public; Owner: inno
--

INSERT INTO innomanager_layers VALUES ('Prova', 'solo un test', 0, 'admin', '2015-08-31', '[Mon Aug 31 08:07:34 CEST 2015] Created.');

--
-- PostgreSQL database dump complete
--