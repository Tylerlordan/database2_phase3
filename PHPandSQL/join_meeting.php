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

    $curr_week_day = date('l');

    $curr_date = date("Y-m-d");

    if($curr_week_day == "Friday" || $curr_week_day == "Saturday" || $curr_week_day == "Sunday") {
        $response['message'] = 'Users may only join meetings before Friday.';
        die(json_encode($response));
    }
    
    $i = 0;
    if($meeting_id != "") {
        $valid_meeting_query = "SELECT COUNT(*) as count 
                                FROM meetings as m1 
                                WHERE meeting_id = $meeting_id 
                                    AND group_id IN (SELECT group_id FROM member_of WHERE student_id = $student_id)
                                    AND meeting_id NOT IN (SELECT meeting_id FROM enroll WHERE student_id = $student_id)
                                    AND capacity NOT IN (SELECT COUNT(*) FROM enroll WHERE meeting_id = $meeting_id)
                                    AND DATE(m1.date) > DATE '" . $curr_date . "'";
    
        $valid_meeting_result = mysqli_query($myconnection, $valid_meeting_query) or die(json_encode($response));
        $valid_meeting = mysqli_fetch_array($valid_meeting_result, MYSQLI_ASSOC);
        if($valid_meeting['count'] == 0) {
            $response['message'] = 'Meeting not found, or you are unable to join.';
            die(json_encode($response));
        }
        else {
            $insert_query = "INSERT INTO enroll VALUES($meeting_id, $student_id)";
            $insert_result = mysqli_query($myconnection, $insert_query) or die(json_encode($response));
            $i += 1;
        }
        mysqli_free_result($valid_meeting_result);
    } else {
        $find_meetings_query = "SELECT meeting_id
                                FROM meetings as m1
                                WHERE group_id IN (SELECT group_id FROM member_of WHERE student_id = $student_id)
                                    AND meeting_id NOT IN (SELECT meeting_id FROM enroll WHERE student_id = $student_id)
                                    AND capacity NOT IN (SELECT COUNT(*) FROM enroll WHERE meeting_id = m1.meeting_id)
                                    AND DATE(m1.date) > DATE '" . $curr_date . "'";
    
        $find_meetings_result = mysqli_query($myconnection, $find_meetings_query) or die(json_encode($response));
        
        while($find_meetings = mysqli_fetch_array($find_meetings_result, MYSQLI_ASSOC)) {
            $meeting_id = $find_meetings["meeting_id"];
            $insert_query = "INSERT INTO enroll VALUES($meeting_id, $student_id)";
            $insert_result = mysqli_query($myconnection, $insert_query) or die(json_encode($response));
            $i += 1;
        }
        mysqli_free_result($find_meetings_result);
            
    }
    $response['success'] = true;
    if($i == 1) {
       $response['message'] = "Joined 1 meeting.";
    } else $response['message'] = "Joined $i meetings.";

    echo json_encode($response);
    
    
    mysqli_close($myconnection);
?>