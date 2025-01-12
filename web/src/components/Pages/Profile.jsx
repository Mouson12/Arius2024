import React from "react";
import profileIcon from "../../assets/profileIcon.jpg"; // Import obrazu
import AppointmentList from "../AppointmentList";
import HeaderTopic from "../HeaderTopic";
import ProfileView from "../ProfileView";
import UserAppointmentsCalendar from "../UserAppointments";

const Profile = () => {
    const token = localStorage.getItem("token");
    console.log("Token z localStorage:", token); // Debug
    console.log("Token z localStorage:", localStorage.getItem("token"));

    return (
        <div className="profile-background"
            style={{
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                height: "100vh"
            }}
        >
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
                    PROFIL
                    <hr style={{ border: "1px solid rgb(133, 0, 0)", width: "13%", margin: "10px 0" }} />
                </div>

                {/* Sekcja IMG i informacje o użytkowniku */}
                <div
                    style={{
                        display: "flex",
                        alignItems: "center",
                        color: "white",
                        fontSize: "20px",
                        marginBottom: "20px", // Dodanie odstępu
                    }}
                >
                    <img
                        src={profileIcon}
                        alt="Profile Icon"
                        style={{
                            width: "2.5cm",
                            height: "2.5cm",
                            borderRadius: "50%",
                            marginRight: "20px",
                        }}
                    />
                    <ProfileView></ProfileView>
                </div>
                <div
                    style={{
                        display: "flex",
                        alignItems: "flex-start", // Opcjonalnie wyrównanie w pionie
                        width: "100%", // Dopasowanie szerokości do rodzica
                        gap: "20px", // Odstęp między divami
                    }}
                >
                    <div
                        style={{
                            paddingLeft: "30px"
                        }}
                    >
                        <HeaderTopic header_text={"AKTUALNE SERWISY"} underline_width={"23%"} fontSize={"30px"}></HeaderTopic>
                        <AppointmentList />
                    </div>
                    <div
                        style={{
                            paddingLeft: "400px"
                        }}
                    >
                        <HeaderTopic header_text={"UMÓWIONE SERWISY"} underline_width={"30%"} fontSize={"30px"}></HeaderTopic>
                        <UserAppointmentsCalendar></UserAppointmentsCalendar>
                    </div>
                </div>

            </div>
        </div>
    );
};

export default Profile;
