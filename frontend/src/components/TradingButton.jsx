import { useEffect, useState } from "react";

export default function TradingButton() {
    const [isTrading, setIsTrading] = useState(false);
    const [loading, setLoading] = useState(true);

    // Fetch initial trading state
    useEffect(() => {
        fetch("http://localhost:8080/status")
            .then(res => res.json())
            .then(data => setIsTrading(data))
            .catch(err => console.error(err))
            .finally(() => setLoading(false));
    }, []);

    const toggleTrading = async () => {
        try {
            setLoading(true);
            const url = `http://localhost:8080/bot/${isTrading ? "stop" : "start"}`;
            await fetch(url, { method: "POST" });
            setIsTrading(!isTrading);
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <button
            onClick={toggleTrading}
            disabled={loading}
            style={{
                padding: "0.6em 1.2em",
                borderRadius: "8px",
                border: "none",
                backgroundColor: isTrading ? "#ff4d4f" : "#4caf50",
                color: "#fff",
                cursor: "pointer",
                fontWeight: 500,
            }}
        >
            {loading ? "Loading..." : isTrading ? "Stop Trading" : "Start Trading"}
        </button>
    );
}
