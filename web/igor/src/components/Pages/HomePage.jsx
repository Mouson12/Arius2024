import React from "react";
import AppointmentButton from "../AppointmentButton"; // Zmieniamy na odpowiednią ścieżkę

const HomePage = () => {
    return (
        <div className="background">
            {/* Napis */}
            <div
                style={{
                    position: "absolute", // Pozycjonowanie absolutne
                    top: "50%", // Środek w pionie
                    left: "8%", // Przesunięcie od lewej krawędzi
                    transform: "translateY(-50%)", // Wyrównanie do środka w pionie
                    color: "white", // Kolor tekstu
                    fontSize: "70px", // Rozmiar czcionki
                    textAlign: "left", // Wyrównanie tekstu do lewej
                    lineHeight: "1.2", // Wysokość linii
                }}
            >
                <div
                    style={{
                        padding: "20px 0",
                        color: "white", // Kolor tekstu
                        fontSize: "55px", // Rozmiar czcionki
                        textAlign: "left", // Wyrównanie tekstu na środku
                        fontWeight: "bold"
                    }}
                >
                    NAJLEPSZE SERWISY SAMOCHODÓW<br /> WYKONYWANE PRZEZ EKSPERTÓW
                </div>
                <div
                    style={{
                        padding: "5px 0",
                        color: "rgb(133, 0, 0)", // Kolor tekstu
                        fontSize: "35px", // Rozmiar czcionki
                        textAlign: "left", // Wyrównanie tekstu na środku
                        fontWeight: "bold"
                    }}
                >
                    UMÓW SIĘ NA JEDNĄ Z NASZYCH<br /> KOMPLEKSOWYCH USŁUG SERWISOWYCH:
                </div>
                <div
                    style={{
                        padding: "10px 0",
                        color: "white", // Kolor tekstu
                        fontSize: "25px", // Rozmiar czcionki
                        textAlign: "left", // Wyrównanie tekstu na środku
                        fontWeight: "bold"
                    }}
                >
                    <ul>
                        <li>NAPRAWA SILNIKÓW I UKŁADU NAPĘDOWEGO</li>
                        <li>OBSŁUGA UKŁADU HAMULCOWEGO</li>
                        <li>DIAGNOSTYKA KOMPUTEROWA</li>
                        <li>WYMIANA OPON I WYWAŻANIE KÓŁ</li>

                    </ul>
                </div>
                <AppointmentButton />
            </div>
        </div>
    );
};

export default HomePage;
