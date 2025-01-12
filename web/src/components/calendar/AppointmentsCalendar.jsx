import React, { useState, useEffect } from 'react';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';

const AppointmentsCalendar = () => {
    const [appointments, setAppointments] = useState([]); // Daty spotkań
    const [highlightedDates, setHighlightedDates] = useState([]); // Daty do podświetlenia
    const [selectedDate, setSelectedDate] = useState(new Date()); // Wybrana data

    // Funkcja do pobierania danych z API
    const fetchAppointments = async () => {
        try {
            const response = await fetch('http://157.90.162.7:5001/api/appointments');
            if (!response.ok) {
                throw new Error('Failed to fetch appointment dates.');
            }
            const data = await response.json();
            const dates = data.map(item => new Date(item.appointment_date)); // Zamiana dat na obiekty Date
            setAppointments(dates);
        } catch (error) {
            console.error('Error fetching appointments:', error);
        }
    };

    // Wywołanie fetchAppointments po załadowaniu komponentu
    useEffect(() => {
        fetchAppointments();
    }, []);

    // Funkcja renderująca kafelki kalendarza
    const tileClassName = ({ date, view }) => {
        if (view === 'month') {
            const isHighlighted = appointments.some(
                (appointmentDate) =>
                    date.getFullYear() === appointmentDate.getFullYear() &&
                    date.getMonth() === appointmentDate.getMonth() &&
                    date.getDate() === appointmentDate.getDate()
            );
            return isHighlighted ? 'highlighted-date' : null;
        }
    };

    return (
        <div style={{ padding: '20px', color: 'white', backgroundColor: "black" }}>
            <Calendar
                onChange={setSelectedDate} // Ustawianie wybranej daty
                value={selectedDate}
                tileClassName={tileClassName} // Ustawianie klasy CSS na kafelkach
            />
            <p>Wybrana data: {selectedDate.toDateString()}</p>
            <style>

                {`
                    .react-calendar {
                        background-color: black;
                        color: white;
                        border: 1px solid white;
                        border-radius: 8px;
                    }
                    .react-calendar__navigation {
                        background-color: black;
                        color: white;
                    }
                    .react-calendar__tile {
                        background-color: black;
                        color: white;
                    }
                    .react-calendar__tile--active {
                        background-color: blue !important;
                        color: white !important;
                    }
                    .highlighted-date {
                        background-color: rgb(133, 0, 0) !important;
                        color: white !important;
                        border-radius: 50%;
                    }
                `}
            </style>
        </div>
    );
};

export default AppointmentsCalendar;
