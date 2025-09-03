import React, { useState } from "react";

export default function ModePicker() {
    const [mode, setMode] = useState("LIVE");
    const [loading, setLoading] = useState(false);

    const handleModeChange = async (newMode) => {
        setLoading(true);
        try {
            const response = await fetch("http://localhost:8080/setMode", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ mode: newMode }),
            });

            if (!response.ok) {
                throw new Error("Failed to set mode");
            }

            const data = await response.text();
            console.log("✅ Mode set:", data);

            setMode(newMode);
        } catch (error) {
            console.error("❌ Error setting mode:", error.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="flex gap-2 p-4">
            <button
                onClick={() => handleModeChange("LIVE")}
                disabled={loading}
                className={`px-4 py-2 rounded-lg font-semibold shadow-md ${
                    mode === "LIVE"
                        ? "bg-blue-600 text-white"
                        : "bg-gray-200 text-gray-800 hover:bg-gray-300"
                }`}
            >
                Live
            </button>
            <button
                onClick={() => handleModeChange("TRAINING")}
                disabled={loading}
                className={`px-4 py-2 rounded-lg font-semibold shadow-md ${
                    mode === "TRAINING"
                        ? "bg-purple-600 text-white"
                        : "bg-gray-200 text-gray-800 hover:bg-gray-300"
                }`}
            >
                Training
            </button>
        </div>
    );
}
