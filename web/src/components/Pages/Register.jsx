import React, { useState } from "react";
import NavigationButton from "../NavigationButton";
import { Link, useNavigate } from "react-router-dom";  // Używamy useNavigate zamiast useHistory
import axios from "axios";

const Register = () => {
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const navigate = useNavigate();  // Zmieniono z useHistory na useNavigate

    const handleRegister = async () => {
        try {
            console.log("Dane wysyłane do serwera:", { username, email, password });
            const response = await axios.post("http://157.90.162.7:5001/auth/register", {
                username,
                email,
                password,
            });
            if (response.status === 200) {
                navigate("/login");
            } else {
                setErrorMessage("Registration failed. Please try again.");
            }
        } catch (error) {
            console.error("Błąd rejestracji:", error.response?.data);
            setErrorMessage(error.response?.data?.message || "Registration failed.");
        }
    };

    return (
        <div className="background-register">
            <div
                style={{
                    position: "absolute",
                    top: "50%",
                    left: "25%",
                    transform: "translate(-50%, -50%)",
                    width: "35%",
                    height: "50vh",
                    backgroundColor: "white",
                    borderRadius: "15px",
                    display: "flex",
                    flexDirection: "column",
                    justifyContent: "center",
                    alignItems: "flex-start",
                    padding: "20px",
                    opacity: 0.8,
                }}
            >
                <div
                    style={{
                        color: "black",
                        fontSize: "40px",
                        padding: "20px 0",
                        textAlign: "left",
                        fontWeight: "bold",
                        width: "100%",
                    }}
                >
                    REJESTRACJA
                    <hr style={{ border: "1px solid rgb(133, 0, 0)", width: "55%", margin: "10px 0" }} />
                </div>
                <input
                    type="text"
                    placeholder="Username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    style={{
                        color: "black",
                        background: "white",
                        fontSize: "20px",
                        width: "80%",
                        padding: "10px",
                        marginBottom: "15px",
                        borderRadius: "8px",
                        border: "1px solid #ccc",
                    }}
                />
                <input
                    type="email"
                    placeholder="Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    style={{
                        color: "black",
                        background: "white",
                        fontSize: "20px",
                        width: "80%",
                        padding: "10px",
                        marginBottom: "15px",
                        borderRadius: "8px",
                        border: "1px solid #ccc",
                    }}
                />
                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    style={{
                        color: "black",
                        background: "white",
                        fontSize: "20px",
                        width: "80%",
                        padding: "10px",
                        borderRadius: "8px",
                        border: "1px solid #ccc",
                    }}
                />
                {errorMessage && (
                    <div style={{ color: "red", marginTop: "10px" }}>{errorMessage}</div>
                )}
                <div
                    style={{
                        padding: "20px 0",
                        color: "rgb(133, 0, 0)",
                        fontSize: "18px",
                        textAlign: "left",
                        fontWeight: "bold",
                        width: "100%",
                    }}
                >
                    <span>MASZ JUŻ KONTO? </span>
                    <Link
                        to="/login"
                        style={{
                            color: "black",
                            textDecoration: "none",
                            fontWeight: "bold",
                        }}
                    >
                        ZALOGUJ SIĘ
                    </Link>
                </div>

                <div
                    style={{
                        width: "100%", // Ustawienie szerokości na 100%
                    }}
                >
                    <NavigationButton onClick={handleRegister} to="/login" buttonText="ZAREJSTRUJ SIĘ" />
                </div>
            </div>
        </div>
    );
};

export default Register;
