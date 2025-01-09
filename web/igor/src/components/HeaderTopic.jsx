import React, { useState } from 'react';

const HeaderTopic = ({ header_text, underline_width, fontSize }) => {
    return <div
        style={{
            color: "white",
            fontSize: fontSize,
            textAlign: "left",
            fontWeight: "bold",
            width: "100%",
            marginBottom: "20px", // Dodanie odstępu
        }}
    >
        {header_text}
        <hr style={{ border: "1px solid rgb(133, 0, 0)", width: underline_width, margin: "10px 0" }} />
    </div>
};

export default HeaderTopic;