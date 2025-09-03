import React, { useState } from "react";

export default function LoadFundsButton() {
    const [loading, setLoading] = useState(false);

    const handleLoadFunds = async () => {
        setLoading(true);

        try {
            const response = await fetch("http://localhost:8080/wallet/load-funds", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    walletId: 1,
                    amount: 10000,
                }),
            });

            if (!response.ok) {
                throw new Error("Failed to load funds");
            }

            const data = await response.text();
            console.log("✅ Funds loaded:", data);
        } catch (error) {
            console.error("❌ Error loading funds:", error.message);
        } finally {
            setLoading(false);
            console.log("ℹ️ Request finished");
        }
    };

    return (
        <div className="p-4">
            <button
                onClick={handleLoadFunds}
                disabled={loading}
                className="px-6 py-2 bg-green-600 text-white font-semibold rounded-lg shadow-md hover:bg-green-700 disabled:bg-gray-400"
            >
                {loading ? "Loading..." : "💰 Load Funds"}
            </button>
        </div>
    );
}
