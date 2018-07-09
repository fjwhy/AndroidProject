<?php
// include db connect class
require_once __DIR__ . '/db_connect.php';
$sql = "SELECT * FROM project";
$r = mysqli_query($con,$sql);
$result = array();
while($res = mysqli_fetch_array($r)){
    array_push($result,array(
        "AndroidNames"=>$res['project_name'],
        "ImagePath"=>$res['project_image']
    )
    );
}
echo json_encode(array("result"=>$result));
mysqli_close($con);

?>