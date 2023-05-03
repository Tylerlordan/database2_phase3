<?php
    $user_type = $_POST['user_type'];
    $user_id = $_POST['user_id'];
    $user_id = intval($user_id);
    $id = $user_id;
    $new_name = $_POST['new_name'];
    $new_email = $_POST['new_email'];
    $new_phone = $_POST['new_phone'];
    $new_password = $_POST['new_password'];

    $response["success"] = false;
    $response["message"] = "Query Error.";

    $myconnection = mysqli_connect('localhost', 'root', '') 
    or die (json_encode($response));

    $mydb = mysqli_select_db ($myconnection, 'db2') or die (json_encode($response));

    // if the user is a parent, use the id of the
    if($user_type === 'parent') {
        if($_POST['student'] != NULL) {
            $student = $_POST['student'];

            $sid_query = 'SELECT id FROM users WHERE email = ' . "'$student'";
            $sid_result = mysqli_query($myconnection, $sid_query) or die (json_encode($response));
            $id_r = mysqli_fetch_array($sid_result, MYSQLI_ASSOC);
            if($id_r == NULL) {
                $response["message"] = "Could not find student.";
                die(json_encode($response));
            }

            $ischildof_query = 'SELECT COUNT(*) as count FROM child_of WHERE student_id = "'.$id_r['id'].'" AND parent_id = "'.$user_id.'"';
            $ischildof_result = mysqli_query($myconnection, $ischildof_query) or die (json_encode($response));
            $ischildof = mysqli_fetch_array($ischildof_result, MYSQLI_ASSOC);
            if($ischildof['count'] == 0) {
                $response["message"] = "User '$student' is not your child.";
                die(json_encode($response));
            }

            $id = $id_r['id'];
            mysqli_free_result($sid_result);
            mysqli_free_result($ischildof_result);
        }
    }

    $num_vals = 0;

    // Updating the values
    if($new_email !== '') {
        $check_taken = "SELECT COUNT(*) as count FROM users WHERE email='".$new_email."'";
        $taken_result = mysqli_query($myconnection, $check_taken) or die(json_encode($response));
        $isTaken = mysqli_fetch_array($taken_result, MYSQLI_ASSOC);
        if($isTaken['count'] > 0) {
            $response["message"] = "New email is already associated with another user.";
            die(json_encode($response));
        }

        $email_query = "UPDATE users SET email='".$new_email."' WHERE id='".$id."'";
        mysqli_query($myconnection, $email_query) or die(json_encode($response));
        mysqli_free_result($taken_result);
        $num_vals += 1;
    }
    if($new_name !== '') {
        $name_query = "UPDATE users SET name='".$new_name."' WHERE id='".$id."'";
        mysqli_query($myconnection, $name_query) or die(json_encode($response));
        $num_vals += 1;
    }
    if($new_phone !== '') {
        $phone_query = "UPDATE users SET phone='".$new_phone."' WHERE id='".$id."'";
        mysqli_query($myconnection, $phone_query) or die(json_encode($response));
        $num_vals += 1;
    }
    if($new_password !== '') {
        $password_query = "UPDATE users SET password='".$new_password."' WHERE id='".$id."'";
        mysqli_query($myconnection, $password_query) or die(json_encode($response));
        $num_vals += 1;
    }

    $response["message"] = "Successfully Updated User Info.";
    if($num_vals === 1) {
        $response["message"] .= " 1 value changed.";
    }
    else $response["message"] .= " $num_vals values have changed.";

    $response["success"] = true;

    echo json_encode($response);
    
    mysqli_close($myconnection);

?>