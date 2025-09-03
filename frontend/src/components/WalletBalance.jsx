import { useEffect, useState } from "react";

export default function WalletBalance() {
    const [balance, setBalance] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchBalance = () => {
            fetch("http://localhost:8080/wallet/balance")
                .then(res => res.json())
                .then(data => setBalance(data))
                .catch(err => console.error("Error fetching balance:", err))
                .finally(() => setLoading(false));
        };

        fetchBalance(); // fetch on mount
        const interval = setInterval(fetchBalance, 35000); // poll every 35s

        return () => clearInterval(interval); // cleanup
    }, []);

    if (loading) return <div>Loading balance...</div>;
    if (balance === null) return <div>Balance not available</div>;

    return (
        <div className="wallet-balance">
            💰 Wallet Balance: <strong>${balance.toFixed(2)}</strong>
        </div>
    );
}
