import React from "react";
import { useNavigate } from "react-router-dom"; // Hook do nawigacji
import { FaArrowRight } from "react-icons/fa"; // Ikonka strzałki

const AppointmentButton = () => {
    const navigate = useNavigate(); // Inicjalizacja hooka nawigacji

    // Funkcja nawigująca do strony umówienia wizyty
    const handleClick = () => {
        navigate("/appointment");
    };

    return (
        <button
            onClick={handleClick}
            style={{
                height: "70px",
                backgroundColor: "rgb(133, 0, 0)", // Kolor tła
                color: "white", // Kolor tekstu
                padding: "10px 20px", // Wewnętrzne odstępy
                fontSize: "16px", // Rozmiar czcionki
                border: "none", // Brak obramowania
                borderRadius: "5px", // Zaokrąglone rogi
                display: "flex", // Flexbox dla tekstu i ikony
                alignItems: "center", // Wyrównanie w pionie
                cursor: "pointer", // Kursor wskazujący, że to przycisk
                transition: "background-color 0.3s", // Animacja zmiany koloru przy najechaniu
            }}
            onMouseEnter={(e) => (e.target.style.backgroundColor = "rgb(100, 0, 0)")} // Zmiana koloru przy najechaniu
            onMouseLeave={(e) => (e.target.style.backgroundColor = "rgb(133, 0, 0)")} // Przywrócenie koloru po opuszczeniu
        >
            UMÓW SIĘ NA WIZYTĘ
            <FaArrowRight style={{ marginLeft: "10px", fontSize: "20px" }} />
        </button>
    );
};

export default AppointmentButton;
