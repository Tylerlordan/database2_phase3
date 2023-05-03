<?php

$userId = $_POST['user_id'];
$userId = intval($userId);
$userType = $_POST['user_type'];

$response["success"] = false;
$response["message"] = "Query Error.";

// Connect to the database
$myconnection = mysqli_connect('localhost', 'root', '') or die(json_encode($response));
$mydb = mysqli_select_db($myconnection, 'db2') or die(json_encode($response));

// Query meetings table to get meeting details for the given year
$meetings_query = "SELECT meeting_id, meeting_name, start_time, date, end_time, name, announcement 
                    FROM meetings as m
                    LEFT JOIN time_slot as t
                    ON m.time_slot_id = t.time_slot_id
                    LEFT JOIN groups as g
                    ON m.group_id = g.group_id WHERE meeting_id IN (SELECT meeting_id
                        FROM enroll WHERE student_id = $userId)";
$meetings_result = mysqli_query($myconnection, $meetings_query) or die(json_encode($response));

// echo "<table>";
// // Print table headers
// echo '<tr bgcolor="#bbbbbb">
// <th>Meeting ID</th>
// <th>Name</th>
// <th>Time</th>
// <th>Capacity</th>
// <th>Group ID</th>
// <th>Announcement</th>
// </tr>';

$meetings = [];
$i = 0;

// Loop through meetings query results and print table rows
while ($meeting_row = mysqli_fetch_array($meetings_result, MYSQLI_ASSOC)) {
    $i += 1;
    
    $temp_meeting["meeting_id"] = $meeting_row["meeting_id"];
    $temp_meeting["meeting_name"] = $meeting_row["meeting_name"];
    $temp_meeting["date"] = $meeting_row["date"];
    $temp_meeting["start_time"] = $meeting_row["start_time"];
    $temp_meeting["end_time"] = $meeting_row["end_time"];
    $temp_meeting["group"] = $meeting_row["name"];
    $temp_meeting["announcement"] = $meeting_row["announcement"];

    array_push($meetings, $temp_meeting);
}
if($i == 0) {
    $response["message"] = "No meetings found.";
    die(json_encode($response));
}

$response["meetings"] = $meetings;
$response["success"] = true;
echo json_encode($response);

mysqli_close($myconnection);

?>
