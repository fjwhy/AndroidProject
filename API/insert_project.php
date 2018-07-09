<?php
 

/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['project_name']) && isset($_POST['project_detail']) && isset($_POST['project_category']) && isset($_POST['project_budget']) && isset($_POST['project_date']) && isset($_POST['project_lat']) && isset($_POST['project_lang']) && isset($_POST['project_mail'])) {
 
    $project_name = $_POST['project_name'];
    $project_detail = $_POST['project_detail'];
    $project_category = $_POST['project_category'];
    $project_budget = $_POST['project_budget'];
	$project_date = $_POST['project_date'];
	$project_lat = $_POST['project_lat'];
    $project_lang = $_POST['project_lang'];
    $project_mail = $_POST['project_mail'];	    	
 
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
    
    /*$query = " SELECT * FROM project WHERE no_hp = '$no_hp'and (status=1 OR status=2)";
    $sql1=mysql_query($query);
    $row = mysql_fetch_array($sql1);
    if (!empty($row)) {
    	// failed to insert row
        $response["success"] = 0;
        $response["message"] = "Order Error Created.";
        // echoding JSON response
        echo json_encode($response);} else {*/
       
    // mysql inserting a new row
    $result = mysql_query("INSERT INTO project(project_name, project_detail, project_category, project_budget, project_date, project_lat, project_lang, project_status, project_mail) VALUES('$project_name', '$project_detail', '$project_category', '$project_budget', '$project_date', '$project_lat', '$project_lang', '1', '$project_mail')");
 
    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "New Project successfully created.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "New Project Error Created.";
 
        // echoing JSON response
        echo json_encode($response);
    } //}
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>