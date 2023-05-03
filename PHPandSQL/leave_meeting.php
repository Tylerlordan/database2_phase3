<?php
    // Get the meeting ID from the URL parameter
    $meeting_id = $_POST['meeting_id'];
    $user_id = $_POST['user_id'];
    $student_id = $user_id;
    $user_type = $_POST['user_type'];
    $response['success'] = false;
    $response['message'] = 'Query Error';
    date_default_timezone_set('America/New_York');

    //$time_query = "SELECT day_of_the_week, start_time, end_time FROM time_slot WHERE time_slot_id = " . $meeting_row["time_slot_id"];
    //$time_result = mysqli_query($myconnection, $time_query) or die('Query failed: ' . mysqli_error());
    //$time_row = mysqli_fetch_array($time_result, MYSQLI_ASSOC);

    // Connect to the database
    $myconnection = mysqli_connect('localhost', 'root', '') or die(json_encode($response));
    $mydb = mysqli_select_db($myconnection, 'db2') or die(json_encode($response));

    $curr_date = date("Y-m-d");
    $curr_time = date("H:i:s");

    if($meeting_id != "") { // Leaving single meeting
        $valid_meeting_query = "SELECT COUNT(*) as count 
                                FROM meetings as m1, time_slot as t1
                                WHERE meeting_id = $meeting_id 
                                    AND meeting_id IN (SELECT meeting_id FROM enroll WHERE student_id = $student_id)
                                    AND DATE(m1.date) >= DATE '" . $curr_date . "'
                                    AND m1.time_slot_id = t1.time_slot_id
                                    AND TIME(t1.start_time) > TIME'".$curr_time."'";

    
        $valid_meeting_result = mysqli_query($myconnection, $valid_meeting_query) or die(json_encode($response));
        $valid_meeting = mysqli_fetch_array($valid_meeting_result, MYSQLI_ASSOC);
        if($valid_meeting['count'] == 0) {
            $response['message'] = 'Meeting not found, or you are unable to leave.';
            die(json_encode($response));
        }
        else {
            $delete_query = "DELETE FROM enroll WHERE meeting_id = $meeting_id AND student_id = $student_id";
            $delete_result = mysqli_query($myconnection, $delete_query) or die(json_encode($response));
            $response['message'] = "Successfully left meeting $meeting_id";
        }
    } else { // Leaving all meetings
            $delete_query = "DELETE FROM enroll WHERE student_id = $student_id 
                                AND meeting_id IN (
                                    SELECT meeting_id
                                    FROM meetings, time_slot
                                    WHERE DATE(meetings.date) >= DATE '".$curr_date."' 
                                        AND meetings.time_slot_id = time_slot.time_slot_id)";
            $delete_result = mysqli_query($myconnection, $delete_query) or die(json_encode($response));
            $response["message"] = "Successfully left all future meetings.";
    }

    $response['success'] = true;
    echo json_encode($response);
    

    mysqli_close($myconnection);
?>