import React, { useState, useEffect } from "react";
import "./RepairHistory.css";

const RepairHistory = () => {
    const [repairHistory, setRepairHistory] = useState([]); // Stan na dane z API
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
            const response = await fetch("http://157.90.162.7:5001/api/repair_history", {
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
        <div className="repair-history-container">
            {repairHistory.length === 0 ? (
                <p>Brak historii napraw.</p>
            ) : (
                repairHistory.map((history) => (
                    <div key={history.repair_history_id} className="repair-history-item">
                        <h4>Serwis #{history.service_id}</h4>
                        <p><strong>Zgłoszenie:</strong> {history.report}</p>
                        <p><strong>Zakończono:</strong> {new Date(history.completed_at).toLocaleString()}</p>
                        <p><strong>ID zamówienia:</strong> {history.repair_order_id}</p>
                    </div>
                ))
            )}
        </div>
    );
};

export default RepairHistory;
