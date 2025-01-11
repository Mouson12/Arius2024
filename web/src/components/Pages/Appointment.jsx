import React from "react";
import CreateRepairOrder from "../CreateRepairOrder";
import HeaderTopic from "../HeaderTopic";
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
                <HeaderTopic header_text={"USŁUGI SERWISOWE"} underline_width={"25%"} fontSize={"40px"}></HeaderTopic>
                <div
                    style={{
                        // padding: "0px 20px",
                        display: "flex",
                        // gap: "200px",
                        // justifyContent: "space-evenly", // Równe rozmieszczenie
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
                        <HeaderTopic header_text={"UMÓW SIĘ NA SERWIS"} underline_width={"25%"} fontSize={"30px"}></HeaderTopic>
                        <CreateRepairOrder></CreateRepairOrder>
                    </div>
                    <div
                        style={{
                            // padding: "0px 100px",
                            paddingLeft: "200px"
                        }}
                    >
                        <HeaderTopic header_text={"HISTORIA USŁUG SERWISOWYCH"} underline_width={"30%"} fontSize={"30px"}></HeaderTopic>
                        <ScrollableList />
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Appointments;