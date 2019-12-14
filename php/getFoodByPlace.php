<?php
    //mysqli_connect([아이피], [아이디], [비밀번호], [DB명]);
    require_once('dbConnect.php');

    // $code = "kze863tffit91h88p92j";
    // $place = "냉동";
    $code = $_POST["code"];
    $place = $_POST["place"];

    $statement = mysqli_prepare($con, "SELECT * from foodTBL WHERE code = ? AND place = ?");

    mysqli_stmt_bind_param($statement, "ss", $code, $place);
    mysqli_stmt_execute($statement);
    $result = mysqli_stmt_get_result($statement);

    $data = array();
    if($result){

      while($row=mysqli_fetch_array($result)){
        array_push($data,
            array("id"=>$row[0], "category"=>$row[1], "section"=>$row[2], "name"=>$row[3], "imagePath"=>$row[4], "memo"=>$row[5]
            , "purchaseDate"=>$row[6], "expirationDate"=>$row[7], "code"=>$row[8], "place"=>$row[9], "alarmID"=>$row[10]
        ));
    }

    header('Content-Type: application/json; charset=utf8');
    $json = json_encode(array("food"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
    echo $json;

  }
  else{
      echo "SQL문 처리중 에러 발생 : ";
      echo mysqli_error($con);
  }

  mysqli_close($con);

?>
