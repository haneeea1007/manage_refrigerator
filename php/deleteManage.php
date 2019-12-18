<?php
    require_once('dbConnect.php');

    // $id = "1238162929";
    // $code = "48vs3qnp683d901o9x2e";
    $code = $_POST["code"];
    $id = $_POST["id"];

    $statement = mysqli_prepare($con, "DELETE FROM manageTBL WHERE code = ? AND id = ?");
    mysqli_stmt_bind_param($statement, "ss", $code, $id);
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;

    echo json_encode($response);

    mysqli_close($con);
?>
