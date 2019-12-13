<?php
define('HOST','localhost');
define('USER','soproject');
define('PASS','sojin4513!!');
define('DB','soproject');

$con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect');
mysqli_query($con,'SET NAMES utf8');
?>
