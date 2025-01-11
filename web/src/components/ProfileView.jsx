import React, { useState, useEffect } from "react";
import "./ScrollableList.css";

const ProfileView = () => {
    const [profile, setProfile] = useState([]); // Stan na dane z API
    const [error, setError] = useState(null); // Obsługa błędów
    const [loading, setLoading] = useState(true); // Obsługa ładowania danych

    const fetchProfileData = async () => {
        const token = localStorage.getItem("token"); // Pobranie tokenu z localStorage

        if (!token) {
            setError("Brak tokenu. Zaloguj się ponownie.");
            setLoading(false);
            return;
        }

        try {
            const response = await fetch("http://157.90.162.7:5001/api/user_data", {
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
            setProfile(data); // Ustawienie danych w stanie
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false); // Wyłączenie stanu ładowania
        }
    };

    useEffect(() => {
        fetchProfileData();
    }, []); // Wywołanie przy montowaniu komponentu

    if (loading) {
        return <p>Ładowanie danych...</p>;
    }

    if (error) {
        return <p className="error">{error}</p>;
    }

    return (
        <div className="error-message">
            {profile.length === 0 ? ( // Sprawdzenie, czy lista jest pusta
                <p>Brak danych uzytkownika</p>
            ) : (
                <p>
                    <strong>NAZWA UŻYTKOWNIKA:</strong> {profile.username}<br />
                    <strong>EMAIL:</strong> {profile.email}<br />
                    <strong>DATA DOŁĄCZENIA:</strong> {new Date(profile.created_at).toLocaleString()}
                </p>
            )}
        </div>
    );
};

export default ProfileView;

