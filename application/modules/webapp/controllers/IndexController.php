<?php

class Webapp_IndexController extends Zend_Controller_Action {
    
    private $logger;
    
    public function init() {
        $this->logger = Sos_Service_Logger::getLogger();
    }
    
    public function indexAction() {
        $phone = Sos_Service_Functions::webappAuth();
        Zend_Layout::getMvcInstance()->assign('title', 'Broadcast');
        $checkInOk = $this->_request->getParam('btCheckInOk', '');
        $checkInGroup = $this->_request->getParam('btCheckInGroup', '');
        $alertGroup = $this->_request->getParam('btAlertGroup', '');
        $message = '';
        if ($checkInOk) {
            try {
                $alert = new Sos_Service_Alert(2, $phone, 'I\'m OK', '', '', '0', '0');
                $alertSaved = $alert->saveAlert();
                $alert->sendAlertTask();
                $message = 'Check-in sent successfully.';
            } catch (Zend_Exception $e) {
                $message = $e->getMessage();
            }
        }
        if ($checkInGroup) {
            $this->getHelper('Redirector')->gotoUrl('/webapp/checkin');
        }
        if ($alertGroup) {
            $this->getHelper('Redirector')->gotoUrl('/webapp/checkin/index/type/alert');
        }
        $this->view->message = $message;
    }
}