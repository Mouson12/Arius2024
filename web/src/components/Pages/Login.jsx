import React, { useState } from "react";
import NavigationButton from "../NavigationButton";
import { Link, useNavigate } from "react-router-dom";  // Użyj useNavigate zamiast useHistory
import axios from "axios";

const Login = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const navigate = useNavigate();  // Zmieniono z useHistory na useNavigate

    const handleLogin = async () => {
        try {
            const response = await axios.post("http://157.90.162.7:5001/auth/login", {
                email,
                password,
            });

            console.log("Odpowiedź serwera:", response.data); // Debugowanie

            if (response.status === 200 && response.data.access_token) {
                console.log("Token otrzymany:", response.data.access_token); // Debugowanie
                localStorage.setItem("token", response.data.access_token); // Zapisywanie tokenu
                console.log("Token zapisany w localStorage:", localStorage.getItem("token")); // Sprawdzanie, czy token jest zapisany
                window.location.reload();
                navigate("/profile"); // Przekierowanie do profilu
            } else {
                setErrorMessage("Login failed. No token received.");
            }
        } catch (error) {
            setErrorMessage(error.response?.data?.message || "Login failed.");
            console.error("Błąd logowania:", error); // Debugowanie
        }
    };

    return (
        <div className="background-login">
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
                    LOGOWANIE
                    <hr style={{ border: "1px solid rgb(133, 0, 0)", width: "50%", margin: "10px 0" }} />
                </div>
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
                    <span>NIE MASZ KONTA? </span>
                    <Link
                        to="/register"
                        style={{
                            color: "black",
                            textDecoration: "none",
                            fontWeight: "bold",
                        }}
                    >
                        ZAREJESTRUJ SIĘ
                    </Link>
                </div>
                <div
                    style={{
                        width: "100%",
                    }}
                >
                    <NavigationButton onClick={handleLogin} to="/profile" buttonText="ZALOGUJ SIĘ" />
                </div>
            </div>
        </div>
    );
};

export default Login;
