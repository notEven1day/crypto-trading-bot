import { useState } from "react";

export default function UpdateCryptoList() {
    const [loading, setLoading] = useState(false);

    const handleUpdate = async () => {
        setLoading(true);
        try {
            const response = await fetch("http://localhost:8080/currencies/fetch");
            if (!response.ok) {
                throw new Error(`Error: ${response.status}`);
            }
            const data = await response.text();
            // Show response in a popup window
            window.alert("✅ Currencies Updated:\n\n" + JSON.stringify(data, null, 2));
        } catch (err) {
            console.error("Failed to update crypto list:", err);
            window.alert("❌ Failed to update crypto list: " + err.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="update-crypto-list">
            <button onClick={handleUpdate} disabled={loading}>
                {loading ? "⏳ Updating..." : "🔄 Update Crypto List"}
            </button>
        </div>
    );
}
