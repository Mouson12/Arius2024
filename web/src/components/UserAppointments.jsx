import React, { useState, useEffect } from "react";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";

const UserAppointmentsCalendar = () => {
    const [appointments, setAppointments] = useState([]); // Daty spotkań
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const [selectedDate, setSelectedDate] = useState(new Date()); // Wybrana data

    const fetchAppointment = async () => {
        const token = localStorage.getItem("token"); // Pobranie tokenu z localStorage

        if (!token) {
            setError("Brak tokenu. Zaloguj się ponownie.");
            setLoading(false);
            return;
        }

        try {
            const response = await fetch("http://157.90.162.7:5001/api/appointments/user", {
                method: "GET",
                headers: {
                    Authorization: `Bearer ${token}`, // Dodanie tokenu do nagłówka
                    "Content-Type": "application/json",
                },
            });

            if (!response.ok) {
                if (response.status === 401) {
                    throw new Error("Nieautoryzowany. Zaloguj się ponownie.");
                } else {
                    throw new Error(`Błąd serwera: ${response.statusText}`);
                }
            }

            const data = await response.json();
            const dates = data.map((item) => new Date(item.appointment_date)); // Zamiana dat na obiekty Date
            setAppointments(dates);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false); // Wyłączenie stanu ładowania
        }
    };

    useEffect(() => {
        fetchAppointment();
    }, []); // Wywołanie przy montowaniu komponentu

    if (loading) {
        return <p>Ładowanie danych...</p>;
    }

    if (error) {
        return <p className="error">{error}</p>;
    }

    // Funkcja renderująca kafelki kalendarza
    const tileClassName = ({ date, view }) => {
        if (view === "month") {
            const isHighlighted = appointments.some(
                (appointmentDate) =>
                    date.getFullYear() === appointmentDate.getFullYear() &&
                    date.getMonth() === appointmentDate.getMonth() &&
                    date.getDate() === appointmentDate.getDate()
            );
            return isHighlighted ? "highlighted-date" : null;
        }
    };

    return (
        <div style={{ color: "white", backgroundColor: "black" }}>
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

export default UserAppointmentsCalendar;
