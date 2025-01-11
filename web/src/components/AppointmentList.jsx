import React, { useEffect, useState } from "react";
import "./ScrollableList.css";

const AppointmentList = () => {
    const [appointmentList, setAppointmentList] = useState([]); // Stan na dane z API
    const [error, setError] = useState(null); // Obsługa błędów
    const [loading, setLoading] = useState(true); // Obsługa ładowania danych

    const fetchAppointmentList = async () => {
        const token = localStorage.getItem("token"); // Pobranie tokenu z localStorage

        if (!token) {
            setError("Brak tokenu. Zaloguj się ponownie.");
            setLoading(false);
            return;
        }

        try {
            const response = await fetch("http://157.90.162.7:5001/api/repair_orders/user", {
                method: "GET",
                headers: {
                    "Authorization": `Bearer ${token}`, // Dodanie tokenu do nagłówka
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
            setAppointmentList(data); // Ustawienie danych w stanie
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false); // Wyłączenie stanu ładowania
        }
    };

    useEffect(() => {
        fetchAppointmentList();
    }, []); // Wywołanie przy montowaniu komponentu

    if (loading) {
        return <p>Ładowanie danych...</p>;
    }

    if (error) {
        return <p className="error">{error}</p>;
    }

    return (
        <div className="error-message">
            {appointmentList.length === 0 ? (
                <p>Brak umówionych serwisów</p>
            ) : (
                <div className="scroll-container">
                    {appointmentList.map((appointment, index) => (
                        <div key={index} className="list-item">
                            <strong>MODEL AUTA:</strong> {appointment.vehicle_model} <br />
                            <strong>MODEL AUTA:</strong> {appointment.vehicle_model} <br />
                            <strong>OPIS:</strong> {appointment.description} <br />
                            <strong>STATUS i NUMER ZAMÓWIENIA:</strong> {appointment.status} {appointment.order_id} <br />
                            <strong>DATA SERWISU:</strong> {appointment.appointment_date}
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default AppointmentList;

