<?php
    require_once('dbConnect.php');

    $id = $_POST["id"];
    $profile = $_POST["profile"];
    $name = $_POST["name"];

    $statement = mysqli_prepare($con, "INSERT INTO userTBL VALUES (?,?,?)");
    mysqli_stmt_bind_param($statement, "sss", $id, $profile, $name);
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;

    echo json_encode($response);

    mysqli_close($con);
?>
