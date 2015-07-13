<?php  if ( ! defined('BASEPATH')) exit('No direct script access allowed');
/*
| -------------------------------------------------------------------
| DATABASE CONNECTIVITY SETTINGS
| -------------------------------------------------------------------
| This file will contain the settings needed to access your database.
|
| For complete instructions please consult the 'Database Connection'
| page of the User Guide.
|
| -------------------------------------------------------------------
| EXPLANATION OF VARIABLES
| -------------------------------------------------------------------
|
|	['hostname'] The hostname of your database server.
|	['username'] The username used to connect to the database
|	['password'] The password used to connect to the database
|	['database'] The name of the database you want to connect to
|	['dbdriver'] The database type. ie: mysql.  Currently supported:
				 mysql, mysqli, postgre, odbc, mssql, sqlite, oci8
|	['dbprefix'] You can add an optional prefix, which will be added
|				 to the table name when using the  Active Record class
|	['pconnect'] TRUE/FALSE - Whether to use a persistent connection
|	['db_debug'] TRUE/FALSE - Whether database errors should be displayed.
|	['cache_on'] TRUE/FALSE - Enables/disables query caching
|	['cachedir'] The path to the folder where cache files should be stored
|	['char_set'] The character set used in communicating with the database
|	['dbcollat'] The character collation used in communicating with the database
|				 NOTE: For MySQL and MySQLi databases, this setting is only used
| 				 as a backup if your server is running PHP < 5.2.3 or MySQL < 5.0.7
|				 (and in table creation queries made with DB Forge).
| 				 There is an incompatibility in PHP with mysql_real_escape_string() which
| 				 can make your site vulnerable to SQL injection if you are using a
| 				 multi-byte character set and are running versions lower than these.
| 				 Sites using Latin-1 or UTF-8 database character set and collation are unaffected.
|	['swap_pre'] A default table prefix that should be swapped with the dbprefix
|	['autoinit'] Whether or not to automatically initialize the database.
|	['stricton'] TRUE/FALSE - forces 'Strict Mode' connections
|							- good for ensuring strict SQL while developing
|
| The $active_group variable lets you choose which connection group to
| make active.  By default there is only one group (the 'default' group).
|
| The $active_record variables lets you determine whether or not to load
| the active record class
*/

$active_group = 'sipac';
$active_record = TRUE;

$db['sipac']['hostname'] = 'crmsolutions-sipac.com';
$db['sipac']['username'] = 'sipac_user';
$db['sipac']['password'] = 'S1p@c_us3r';
$db['sipac']['database'] = 'sipac';
$db['sipac']['dbdriver'] = 'mysql';
$db['sipac']['dbprefix'] = '';
$db['sipac']['pconnect'] = FALSE;
$db['sipac']['db_debug'] = TRUE;
$db['sipac']['cache_on'] = FALSE;
$db['sipac']['cachedir'] = '';
$db['sipac']['char_set'] = 'utf8';
$db['sipac']['dbcollat'] = 'utf8_general_ci';
$db['sipac']['swap_pre'] = '';
$db['sipac']['autoinit'] = TRUE;
$db['sipac']['stricton'] = FALSE;

$db['ku']['hostname'] = 'crmsolutions-sipac.com';
$db['ku']['username'] = 'sipac_user';
$db['ku']['password'] = 'S1p@c_us3r';
$db['ku']['database'] = 'ku_bases_expulse';
$db['ku']['dbdriver'] = 'mysql';
$db['ku']['dbprefix'] = '';
$db['ku']['pconnect'] = FALSE;
$db['ku']['db_debug'] = TRUE;
$db['ku']['cache_on'] = FALSE;
$db['ku']['cachedir'] = '';
$db['ku']['char_set'] = 'utf8';
$db['ku']['dbcollat'] = 'utf8_general_ci';
$db['ku']['swap_pre'] = '';
$db['ku']['autoinit'] = TRUE;
$db['ku']['stricton'] = FALSE;

$db['k7']['hostname'] = 'crmsolutions-sipac.com';
$db['k7']['username'] = 'sipac_user';
$db['k7']['password'] = 'S1p@c_us3r';
$db['k7']['database'] = 'k7_bases_expulse';
$db['k7']['dbdriver'] = 'mysql';
$db['k7']['dbprefix'] = '';
$db['k7']['pconnect'] = FALSE;
$db['k7']['db_debug'] = TRUE;
$db['k7']['cache_on'] = FALSE;
$db['k7']['cachedir'] = '';
$db['k7']['char_set'] = 'utf8';
$db['k7']['dbcollat'] = 'utf8_general_ci';
$db['k7']['swap_pre'] = '';
$db['k7']['autoinit'] = TRUE;
$db['k7']['stricton'] = FALSE;

$db['ks']['hostname'] = 'crmsolutions-sipac.com';
$db['ks']['username'] = 'sipac_user';
$db['ks']['password'] = 'S1p@c_us3r';
$db['ks']['database'] = 'ks_bases_expulse';
$db['ks']['dbdriver'] = 'mysql';
$db['ks']['dbprefix'] = '';
$db['ks']['pconnect'] = FALSE;
$db['ks']['db_debug'] = TRUE;
$db['ks']['cache_on'] = FALSE;
$db['ks']['cachedir'] = '';
$db['ks']['char_set'] = 'utf8';
$db['ks']['dbcollat'] = 'utf8_general_ci';
$db['ks']['swap_pre'] = '';
$db['ks']['autoinit'] = TRUE;
$db['ks']['stricton'] = FALSE;

$db['mp']['hostname'] = 'crmsolutions-sipac.com';
$db['mp']['username'] = 'sipac_user';
$db['mp']['password'] = 'S1p@c_us3r';
$db['mp']['database'] = 'mp_bases_expulse';
$db['mp']['dbdriver'] = 'mysql';
$db['mp']['dbprefix'] = '';
$db['mp']['pconnect'] = FALSE;
$db['mp']['db_debug'] = TRUE;
$db['mp']['cache_on'] = FALSE;
$db['mp']['cachedir'] = '';
$db['mp']['char_set'] = 'utf8';
$db['mp']['dbcollat'] = 'utf8_general_ci';
$db['mp']['swap_pre'] = '';
$db['mp']['autoinit'] = TRUE;
$db['mp']['stricton'] = FALSE;


$db['jq']['hostname'] = 'crmsolutions-sipac.com';
$db['jq']['username'] = 'sipac_user';
$db['jq']['password'] = 'S1p@c_us3r';
$db['jq']['database'] = 'jq_bases_expulse';
$db['jq']['dbdriver'] = 'mysql';
$db['jq']['dbprefix'] = '';
$db['jq']['pconnect'] = FALSE;
$db['jq']['db_debug'] = TRUE;
$db['jq']['cache_on'] = FALSE;
$db['jq']['cachedir'] = '';
$db['jq']['char_set'] = 'utf8';
$db['jq']['dbcollat'] = 'utf8_general_ci';
$db['jq']['swap_pre'] = '';
$db['jq']['autoinit'] = TRUE;
$db['jq']['stricton'] = FALSE;

$db['mc']['hostname'] = 'crmsolutions-sipac.com';
$db['mc']['username'] = 'sipac_user';
$db['mc']['password'] = 'S1p@c_us3r';
$db['mc']['database'] = 'mc_bases_expulse';
$db['mc']['dbdriver'] = 'mysql';
$db['mc']['dbprefix'] = '';
$db['mc']['pconnect'] = FALSE;
$db['mc']['db_debug'] = TRUE;
$db['mc']['cache_on'] = FALSE;
$db['mc']['cachedir'] = '';
$db['mc']['char_set'] = 'utf8';
$db['mc']['dbcollat'] = 'utf8_general_ci';
$db['mc']['swap_pre'] = '';
$db['mc']['autoinit'] = TRUE;
$db['mc']['stricton'] = FALSE;

/* End of file database.php */
/* Location: ./application/config/database.php */