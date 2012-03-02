<?php
// Define path to application directory
defined('APPLICATION_PATH')
    || define('APPLICATION_PATH', realpath(dirname(__FILE__) . '/application'));

// Define application environment
defined('APPLICATION_ENV')
    || define('APPLICATION_ENV', (getenv('APPLICATION_ENV') ? getenv('APPLICATION_ENV') : 'production'));

// Ensure library/ is on include_path
/*
If you do not have the Zend library already included in your PHP installation...

1.  Create a directory named library and place the Zend Framework into it.

NOTE: In my instance, I have the Zend library residing under /usr/share/php/   Because of this, the following lines are commented.
*/
/*set_include_path(implode(PATH_SEPARATOR, array(
    realpath(APPLICATION_PATH . '/library'),
    get_include_path(),
)));
*/

/** Zend_Application */
require_once 'Zend/Application.php'; 

// Create application, bootstrap, and run
$application = new Zend_Application(
    APPLICATION_ENV, 
    APPLICATION_PATH . '/configs/application.ini'
);
$application->bootstrap()->run();
