<?php
    //mysqli_connect([아이피], [아이디], [비밀번호], [DB명]);
    require_once('dbConnect.php');

    // $id = "1";
    $id = $_POST["id"];

    $statement = mysqli_prepare($con, "SELECT refrigeratorTBL.code, refrigeratorTBL.name, refrigeratorTBL.type FROM refrigeratorTBL JOIN manageTBL on refrigeratorTBL.code = manageTBL.code WHERE manageTBL.id = ?");

    mysqli_stmt_bind_param($statement, "s", $id);
    mysqli_stmt_execute($statement);
    $result = mysqli_stmt_get_result($statement);

    $data = array();
    if($result){

      while($row=mysqli_fetch_array($result)){
        array_push($data,
            array("code"=>$row[0], "name"=>$row[1],"type"=>$row[2]
        ));
    }

    header('Content-Type: application/json; charset=utf8');
    $json = json_encode(array("refrigerator"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
    echo $json;

  }
  else{
      echo "SQL문 처리중 에러 발생 : ";
      echo mysqli_error($con);
  }

  mysqli_close($con);

?>
