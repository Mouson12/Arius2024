import React from "react";
import profileIcon from "../../assets/profileIcon.jpg"; // Import obrazu
import ScrollableList from "../ScrollableList";

const Appointments = () => {
    const token = localStorage.getItem("token");
    console.log("Token z localStorage:", token); // Debug
    console.log("Token z localStorage:", localStorage.getItem("token"));

    return (
        <div className="profile-background" style={{ display: "flex", justifyContent: "center", alignItems: "center", height: "100vh" }}>
            <div
                style={{
                    width: "85%",
                    position: "absolute", // Pozycjonowanie absolutne
                    top: "100px", // Środek w pionie
                    backgroundColor: "black",
                    borderRadius: "15px",
                    display: "flex",
                    flexDirection: "column",
                    justifyContent: "flex-start",
                    alignItems: "flex-start",
                    padding: "20px",
                    opacity: 0.8,
                    height: "80vh",
                }}
            >
                {/* Nagłówek PROFIL */}
                <div
                    style={{
                        color: "white",
                        fontSize: "40px",
                        textAlign: "left",
                        fontWeight: "bold",
                        width: "100%",
                        marginBottom: "20px", // Dodanie odstępu
                    }}
                >
                    UMÓW SIĘ NA SPOTKANIE
                    <hr style={{ border: "1px solid rgb(133, 0, 0)", width: "47%", margin: "10px 0" }} />
                </div>
            </div>
        </div>
    );
};

export default Appointments;