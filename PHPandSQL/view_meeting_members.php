<?php
    // Get the meeting ID from the URL parameter
    $meeting_id = $_POST['meeting_id'];
    $meeting_id = intval($meeting_id);
    $user_id = $_POST['user_id'];
    $user_id = intval($user_id);
    $user_type = $_POST['user_type'];

    $response["success"] = false;
    $response["message"] = "Query Error.";

    // Connect to the database
    $myconnection = mysqli_connect('localhost', 'root', '') or die(json_encode($response));
    $mydb = mysqli_select_db($myconnection, 'db2') or die(json_encode($response));


    // Query to get the list of students joining the meeting
    $enroll_query = "SELECT users.name, users.email FROM enroll JOIN users ON enroll.student_id = users.id WHERE enroll.meeting_id = $meeting_id";
    $enroll_result = mysqli_query($myconnection, $enroll_query) or die(json_encode($response));

    $i = 0;
    $members = [];

    while ($enroll_row = mysqli_fetch_array($enroll_result, MYSQLI_ASSOC)) {
        $member["name"] = $enroll_row["name"];
        $member["email"] = $enroll_row["email"];

        array_push($members, $member);
    }

    mysqli_free_result($enroll_result);

    $response["success"] = true;
    $response["members"] = $members;
    echo json_encode($response);

    mysqli_close($myconnection);
?>