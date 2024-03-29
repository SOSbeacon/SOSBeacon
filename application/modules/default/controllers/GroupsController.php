<?php

require_once 'BaseController.php';

class GroupsController extends BaseController {

    public function init() {
        $bootstrap = $this->getInvokeArg('bootstrap');
        $options = $bootstrap->getOption('resources');
        $contextSwitch = $this->_helper->getHelper('contextSwitch');
        $contextSwitch->addActionContext('index', array('xml', 'json'))
        ->addActionContext('get', array('xml', 'json'))
        ->addActionContext('post', array('xml', 'json'))
        ->addActionContext('put', array('xml', 'json'))
        ->addActionContext('delete', array('xml', 'json'))
        ->initContext();
    }
    
    /**
     * The index action handles index/list requests; it should respond with a
     * list of the requested resources.
     */
    public function indexAction() {
        $token = $this->_request->getParam('token', false);
        $phoneId = $this->_request->getParam('phoneId', false);
        $resObj = array();
        try {
            $this->authorizePhone($token, $phoneId);
            $groupMapper = new Sos_Model_ContactgroupMapper();
            $resObj['data'] = $groupMapper->getGroups($phoneId);
            $resObj['success'] = 'true';
        } catch (Zend_Exception $ex) {
            $resObj['success'] = 'false';
            $resObj['message'] = $ex->getMessage();
        }
        $this->view->response = $resObj;
    }

    /**
     * The post action handles POST requests; it should accept and digest a
     * POSTed resource representation and persist the resource state.
     */
    public function postAction() {
        $name = trim($this->_request->getParam('name', ''));
        $token = $this->_request->getParam('token', false);
        $phoneId = $this->_request->getParam('phoneId', false);
        $resObj['success'] = 'false';
        $resObj['message'] = '';
        $resObj = array();
        try {
            $this->authorizePhone($token, $phoneId);
            $groupMapper = new Sos_Model_ContactgroupMapper();
            $response = $groupMapper->saveGroup($name, $phoneId);
            $resObj['message'] = $response['message'];
            $resObj['success'] = $response['success'];
            $resObj['data'] = $response['data'];
        } catch (Zend_Exception $ex) {
            $resObj['success'] = 'false';
            $resObj['message'] = $ex->getMessage();
            $resObj['error'] = $ex->getMessage();
        }
        $this->view->response = $resObj;
    }

    /**
     * The put action handles PUT requests and receives an 'id' parameter; it 
     * should update the server resource state of the resource identified by 
     * the 'id' value.
     */
    public function putAction() {
        $groupId = $this->_request->getParam('id', false);
        $token = $this->_request->getParam('token');
        $name = trim($this->_request->getParam('name', ''));
        $resObj['success'] = 'false';
        $resObj['message'] = '';
        $resObj = array();
        try {
            $this->authorizeGroup($token, $groupId);
            $groupMapper = new Sos_Model_ContactgroupMapper();
            $group = new Sos_Model_Contactgroup();
            $group->find($groupId);
            if ($group->getId()) {
                $response = $groupMapper->saveGroup($name, $group->getPhoneId(), $group->getId());
                $resObj['message'] = $response['message'];
                $resObj['data'] = $response['data'];
                $resObj['success'] = $response['success'];
            } else {
                $resObj['message'] = 'Group is not exist';
            }
        } catch (Zend_Exception $ex) {
            $resObj['success'] = 'false';
            $resObj['message'] = $ex->getMessage();
            $resObj['error'] = $ex->getMessage();
        }
        $this->view->response = $resObj;
    }

    /**
     * The delete action handles DELETE requests and receives an 'id' 
     * parameter; it should update the server resource state of the resource
     * identified by the 'id' value.
     */
    public function deleteAction() {
        $groupId = $this->_request->getParam('id', false);
        $token = $this->_request->getParam('token');
        $resObj['success'] = 'false';
        $resObj['message'] = '';
        $resObj = array();
        try {
            $this->authorizeGroup($token, $groupId);
            $group = new Sos_Model_Contactgroup();
            $group->find($groupId);
            if ($group->getId() && $group->getType() > 2) {
                $group->delete('id = ' . $groupId);
                $resObj['success'] = "true";
                $resObj['message'] = 'Group deleted successfully';
                $groupMapper = new Sos_Model_ContactgroupMapper();
                $resObj['data'] = $groupMapper->getGroups($group->getPhoneId());
            } else {
                $resObj['success'] = 'false';
                $resObj['message'] = 'Cannot delete default group';
            }
        } catch (Zend_Exception $ex) {
            $resObj['success'] = 'false';
            $resObj['message'] = $ex->getMessage();
            $resObj['error'] = $ex->getMessage();
        }

        $this->view->response = $resObj;
    }
    
    public function getAction() {}
}