<?php
include('ipinfodb.class.php');
 
//Load the class
$ipinfodb = new ipinfodb;
$ipinfodb->setKey('149048918a5419cf857a642d6013c2455eadb3f95e7df8e77f0f859d620fbd93');
 
//Get errors and locations
$locations = $ipinfodb->getGeoLocation($_SERVER['REMOTE_ADDR']);
//$errors = $ipinfodb->getErrors();
 
//Getting the result
//echo "<p>\n";
//echo "<strong>First result</strong><br />\n";
if (!empty($locations) && is_array($locations)) {
  foreach ($locations as $field => $val) {
    //echo $field . ' : ' . $val . "<br />\n";
    if($field == "CountryName")
    {
    	echo "El pais es:".$val;
    }
  }
}
//echo "</p>\n";

/*
//Show errors
echo "<p>\n";
echo "<strong>Dump of all errors</strong><br />\n";
if (!empty($errors) && is_array($errors)) {
  foreach ($errors as $error) {
    echo var_dump($error) . "<br /><br />\n";
  }
} else {
  echo "No errors" . "<br />\n";
}
echo "</p>\n";
*/
