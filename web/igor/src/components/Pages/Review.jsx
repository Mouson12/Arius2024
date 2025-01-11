import React from "react";
import AddReview from "../AddReview";
import HeaderTopic from "../HeaderTopic";
import ReviewList from "../RatingsList";

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
                <HeaderTopic header_text={"SPRAWDŹ NASZE OCENY"} underline_width={"25%"} fontSize={"40px"}></HeaderTopic>
                <div
                    style={{
                        // padding: "0px 20px",
                        display: "flex",
                        // gap: "200px",
                        // justifyContent: "space-evenly", // Równe rozmieszczenie
                        alignItems: "flex-start", // Opcjonalnie wyrównanie w pionie
                        width: "100%", // Dopasowanie szerokości do rodzica
                        // gap: "100px", // Odstęp między divami
                    }}
                >
                    <div style={{ display: "flex", gap: "200px" }}>
                        <div
                            style={{
                                paddingLeft: "30px"
                            }}
                        >
                            <HeaderTopic header_text={"OCEŃ NASZE USŁUGI"} underline_width={"25%"} fontSize={"30px"}></HeaderTopic>
                            <AddReview></AddReview>
                        </div>
                    </div>
                    <div
                        style={{
                            // padding: "0px 100px",
                            paddingLeft: "200px"
                        }}
                    >
                        <HeaderTopic header_text={"NASZE OCENY"} underline_width={"25%"} fontSize={"30px"}></HeaderTopic>
                        <ReviewList></ReviewList>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Review;