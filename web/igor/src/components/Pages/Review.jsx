import React from "react";
import CreateRepairOrder from "../CreateRepairOrder";
import HeaderTopic from "../HeaderTopic";

const Review = () => {
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
                <div style={{ display: "flex", gap: "50px" }}>

                    {/* <HeaderTopic header_text={"TWOJE SERWISY"} underline_width={"25%"}></HeaderTopic> */}
                    {/* <HeaderTopic header_text={"TWOJE SERWISY"} underline_width={"25%"}></HeaderTopic> */}
                </div>
                {/* <HeaderTopic header_text={"TWOJE SERWISY"} underline_width={"25%"}></HeaderTopic> */}
                {/* <CreateRepairOrder></CreateRepairOrder> */}
            </div>
        </div>
    );
};

export default Review;