<?php
  $password = $_POST['password'];
  $email = $_POST['email'];
  $response["success"] = false;
  $response["message"] = "Database Error.";

  $myconnection = mysqli_connect('localhost', 'root', '') 
  or die ($response);

  $mydb = mysqli_select_db ($myconnection, 'db2') or die (json_encode($response));

  $query = 'SELECT id FROM users WHERE email = "'.$email.'" AND password = "'.$password.'"';
  $result = mysqli_query($myconnection, $query) or die (json_encode($response));
  $user = mysqli_fetch_array($result, MYSQLI_ASSOC);

  if($user === NULL) {
    $response["message"] = "Invalid User Credentials.";
    die(json_encode($response));
  }

  $is_student = 'SELECT COUNT(*) as count FROM students WHERE student_id='.$user['id'].'';
  $is_student_result = mysqli_query($myconnection, $is_student) or die (json_encode($response));
  $student = mysqli_fetch_array($is_student_result, MYSQLI_ASSOC);
  
  $is_parent = 'SELECT COUNT(*) as count FROM parents WHERE parent_id='.$user['id'].'';
  $is_parent_result = mysqli_query($myconnection, $is_parent) or die (json_encode($response));
  $parent = mysqli_fetch_array($is_parent_result, MYSQLI_ASSOC);

  $is_admin = 'SELECT COUNT(*) as count FROM admins WHERE admin_id='.$user['id'].'';
  $is_admin_result = mysqli_query($myconnection, $is_admin) or die (json_encode($response));
  $admin = mysqli_fetch_array($is_admin_result, MYSQLI_ASSOC);

  $usertype = "";

  if($user['id'] === false || $admin['count'] > 0) {
    $response["message"] = 'Invalid user info.';
    die(json_encode($response));
  }
  else {
    if($student['count'] > 0) {
        $usertype = "student";
    }
    elseif($parent['count'] > 0) {
        $usertype = "parent";
    }
    $response["success"] = true;
    $response["type"] = $usertype;
    $response["id"] = $user['id'];
    echo json_encode($response);

    mysqli_close($myconnection);
  }
?>