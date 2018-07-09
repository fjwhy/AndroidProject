<?php

require_once __DIR__ . '/db_config.php';

$projects = array();
$project = array();

$sql = "SELECT project_name, project_budget, project_status FROM project WHERE driver='".$_REQUEST['mail_id']."'";
$mysqli = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);
if($res = $mysqli->query($sql)){
	while($row=$res->fetch_assoc()){
        $project_name = $row['project_name'];
        $project_budget = $row['project_budget'];
        $project_status = $row['project_status'];
        $data= array("project_name"=>$project_name,"project_budget"=>$project_budget,"project_status"=>$project_status);
        $project[] = $data;
	}

        $projects = array("project"=>$project);

        echo json_encode($projects);
}


?>