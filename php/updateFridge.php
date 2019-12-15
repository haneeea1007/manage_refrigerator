<?php
    require_once('dbConnect.php');

    $name = $_POST["name"];
    $code = $_POST["code"];

    $statement = mysqli_prepare($con, "UPDATE refrigeratorTBL SET name = ? WHERE code = ?");
    mysqli_stmt_bind_param($statement, "ss", $name, $code);
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;

    echo json_encode($response);

    mysqli_close($con);
?>
