// src/components/Attendance.js
import React, { useState } from 'react';
import axios from 'axios';
import api from '../services/api';
import { useNavigate } from 'react-router-dom';

const Attendance = () => {
    const [attendanceType, setAttendanceType] = useState('');
    const [date, setDate] = useState('');
    const [message, setMessage] = useState('');
    const navigate = useNavigate();

    const handleAttendance = async () => {
        const token = localStorage.getItem('token');
        const email = localStorage.getItem('email');
        if(!token){
            alert("Please log in again.");
            navigate('/login');
            return;
        }
        const payload = {
            "loginOption": "tea_time", // The selected attendance type
            "user": {
                "id": 4 // Replace with dynamic user ID as needed
            },
            "instituteName": "apteknow", // Static value, change if necessary
            "email": "jash1@gmail.com", // Static email, update as needed
        };
        console.log(payload);

        try {
            const response = await api.post('/attendance/add', 
                { payload,     headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                } } );            
            setMessage(response.data.message);
            console.log("Attendance marked successfully: ", response.data);
            navigate('/success'); // Navigate to Success page after submitting

        } catch (error) {
            setMessage('Attendance marking failed: ' + error.response.data);
        }
    };

    return (
        <div>
            <h2>Mark Attendance</h2>
            <select value={attendanceType} onChange={(e) => setAttendanceType(e.target.value)} required>
                <option value="">Select Attendance Type</option>
                <option value="login">Login</option>
                <option value="Tea">Tea Break</option>
                <option value="lunch">Lunch</option>
                <option value="logout">Logout</option>
            </select>
            {/* <input type="date" value={date} onChange={(e) => setDate(e.target.value)} required /> */}
            <button onClick={handleAttendance}>Mark Attendance</button>
            {message && <p>{message}</p>}
        </div>
    );
};

export default Attendance;
