import React, { useState, useEffect } from 'react';
import "./ScrollableList.css";

const ServiceList = () => {
    // Funkcja renderująca kafelki kalendarza
    const [error, setError] = useState(null); // Obsługa błędów
    const [loading, setLoading] = useState(true); // Obsługa ładowania danych

    const fetchRepairHistory = async () => {
        const token = localStorage.getItem("token"); // Pobranie tokenu z localStorage

        if (!token) {
            setError("Brak tokenu. Zaloguj się ponownie.");
            setLoading(false);
            return;
        }

        try {
            const response = await fetch("http://157.90.162.7:5001/api/services", {
                method: "GET",
            });

            if (!response.ok) {
                if (response.status === 401) {
                    throw new Error("Nieautoryzowany. Zaloguj się ponownie.");
                } else {
                    throw new Error(`Błąd serwera: ${response.statusText}`);
                }
            }

            const data = await response.json();
            setRepairHistory(data); // Ustawienie danych w stanie
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false); // Wyłączenie stanu ładowania
        }
    };

    useEffect(() => {
        fetchRepairHistory();
    }, []); // Wywołanie przy montowaniu komponentu

    if (loading) {
        return <p>Ładowanie danych...</p>;
    }

    if (error) {
        return <p className="error">{error}</p>;
    }

    return (
        <div className="error-message">
            {
                repairHistory.length === 0 ? ( // Sprawdzenie, czy lista jest pusta
                    <p>Brak historii napraw</p>
                ) : (
                    <div className="scroll-container-history">
                        {
                            repairHistory.map((history, index) => (
                                <div key={index} className="list-item">
                                    <strong>Serwis {history.repair_order_id}</strong>: {history.report} <br />
                                    <strong>Zakończono:</strong> {new Date(history.completed_at).toLocaleString()}
                                </div>
                            ))
                        }
                    </div>
                )
            }
        </div>
    );
}

export default ServiceList;
