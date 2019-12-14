<?php
    require_once('dbConnect.php');

    // $code = "ht98dm2o65yfkhtoonwj"
    // $code = $_POST["code"];

    $statement = mysqli_prepare($con, "DELETE FROM refrigeratorTBL WHERE code = ht98dm2o65yfkhtoonwj");
    // mysqli_stmt_bind_param($statement, "s", $code);
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;

    echo json_encode($response);

    mysqli_close($con);
?>
